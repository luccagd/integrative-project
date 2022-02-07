package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.PurchaseOrderResponse;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.*;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {
    private ProductRepository productRepository;
    private BuyerRepository buyerRepository;
    private CartRepository cartRepository;
    private CartProductRepository cartProductRepository;
    private WarehouseSectionRepository warehouseSectionRepository;

    public PurchaseOrderService(ProductRepository productRepository, BuyerRepository buyerRepository, CartRepository cartRepository, CartProductRepository cartProductRepository, WarehouseSectionRepository warehouseSectionRepository) {
        this.productRepository = productRepository;
        this.buyerRepository = buyerRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.warehouseSectionRepository = warehouseSectionRepository;
    }

    public List<CartProduct> findByCartId(Long id) {
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(id);

        if (cartProducts == null || cartProducts.isEmpty()) {
            throw new NotFoundException("No orders were found for the given id");
        }

        return cartProducts;
    }

    @Transactional
    public PurchaseOrderResponse save(PurchaseOrderRequest request) {
        AtomicReference<Double> cartTotalPrice = new AtomicReference<>(0.0);

        Buyer buyer = buyerRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new NotFoundException("Buyer not exists!"));

        List<PurchaseOrderProductRequest> productRequests = request.getProducts();
        validateIfProductHaveEnoughStock(productRequests);
        validateIfProductHasAnExpirationDateOfLessThan3Weeks(productRequests);

        Cart cart = Cart.builder().buyer(buyer).status(CartStatus.FECHADO).build();
        cart.setCartsProducts(addProductsToCart(productRequests, cart, cartTotalPrice));

        cartRepository.save(cart);

        return PurchaseOrderResponse.builder()
                .totalPrice(BigDecimal.valueOf(cartTotalPrice.get()))
                .build();
    }

    @Transactional
    public PurchaseOrderResponse update(PurchaseOrderRequest request, Long purchaseOrderId) {
        AtomicReference<Double> cartTotalPrice = new AtomicReference<>(0.0);

        Cart cart = cartRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new NotFoundException("Cart not exists!"));

        request.getProducts().forEach(requestProduct -> {
            if (!cartProductRepository.existsByProduct_IdAndCart_Id(requestProduct.getProductId(), purchaseOrderId))
                throw new BusinessException("This product does not exist in this cart!");
        });

        request.getProducts().stream().forEach(requestProduct -> {
            CartProduct findCartProduct = cart.getCartsProducts().stream()
                    .filter(cartProduct -> cartProduct.getProduct().getId().equals(requestProduct.getProductId()))
                    .findAny().get();

            WarehouseSection findWarehouseSection = warehouseSectionRepository.findWarehouseSectionByProductId(requestProduct.getProductId());

            if (findCartProduct.getProduct().getId().equals(requestProduct.getProductId())) {
                Integer diff = findCartProduct.getQuantity() - requestProduct.getQuantity();

                if (findCartProduct.getProduct().getQuantity() + diff < 0) {
                    throw new BusinessException("Ordered quantity to product " + findCartProduct.getProduct().getName() + " is greater than what is in stock!");
                }

                findCartProduct.getProduct().setQuantity(findCartProduct.getProduct().getQuantity() + diff);
                findCartProduct.setQuantity(requestProduct.getQuantity());

                if (diff < 0) {
                    findWarehouseSection.decreaseTotalProducts(diff);
                }

                if (diff > 0) {
                    findWarehouseSection.increaseTotalProducts(diff);
                }
            }

            cartProductRepository.save(findCartProduct);
            warehouseSectionRepository.save(findWarehouseSection);

            cartTotalPrice.updateAndGet(v -> v + (findCartProduct.getProduct().getPrice() * requestProduct.getQuantity()));
        });

        return PurchaseOrderResponse.builder()
                .totalPrice(BigDecimal.valueOf(cartTotalPrice.get()))
                .build();
    }

    private List<CartProduct> addProductsToCart(List<PurchaseOrderProductRequest> productsRequest, Cart cart, AtomicReference<Double> totalPrice) {
        return productsRequest.stream().map(requestProduct -> {
            Product productInStock = productRepository.findById(requestProduct.getProductId()).get();
            WarehouseSection warehouseSectionForProductInStock = warehouseSectionRepository.findWarehouseSectionByProductId(productInStock.getId());

            Integer requestProductQuantity = requestProduct.getQuantity();

            updateProductQuantityAndDecreaseTotalProducts(productInStock, warehouseSectionForProductInStock, requestProductQuantity);

            totalPrice.updateAndGet(v -> v + (productInStock.getPrice() * requestProductQuantity));

            return CartProduct.builder()
                    .cart(cart)
                    .product(productInStock)
                    .quantity(requestProductQuantity)
                    .build();
        }).collect(Collectors.toList());
    }

    private void updateProductQuantityAndDecreaseTotalProducts(Product product, WarehouseSection warehouseSection, Integer quantity) {
        product.setQuantity(product.getQuantity() - quantity);

        warehouseSection.decreaseTotalProducts(quantity);
    }

    private void validateIfProductHaveEnoughStock(List<PurchaseOrderProductRequest> purchaseOrderProductRequests) {
        purchaseOrderProductRequests.stream().forEach(requestProduct -> {
            Product findProductInStock = productRepository.findById(requestProduct.getProductId()).get();

            if (requestProduct.getQuantity() > findProductInStock.getQuantity()) {
                throw new BusinessException("Product " + findProductInStock.getName() + " does not have enough stock for this quantity!");
            }
        });
    }

    private void validateIfProductHasAnExpirationDateOfLessThan3Weeks(List<PurchaseOrderProductRequest> purchaseOrderProductRequests) {
        purchaseOrderProductRequests.stream().forEach(requestProduct -> {
            Product findProductInStock = productRepository.findById(requestProduct.getProductId()).get();

            LocalDate dueDateDeadlineTo3Weeks = LocalDate.now().plusWeeks(3);

            if (findProductInStock.getDueDate().isBefore(dueDateDeadlineTo3Weeks)) {
                throw new BusinessException("Product " + findProductInStock.getName() + " has an expiration date of less than 3 weeks!");
            }
        });
    }
}