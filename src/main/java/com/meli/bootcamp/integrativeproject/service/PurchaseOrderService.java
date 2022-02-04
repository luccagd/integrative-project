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
import java.util.ArrayList;
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

    @Transactional
    public PurchaseOrderResponse put(Long id, PurchaseOrderRequest request) {
        // requestProductsValidations(request.getProducts());
        List<CartProduct> cartProductList = new ArrayList<>();

        request.getProducts().forEach(requestProduct -> {
            cartProductList.add(cartProductRepository.findByCart_IdAndProduct_Id(id, requestProduct.getProductId()));
        });

        request.getProducts().forEach(requestProduct -> {
            if (!cartProductRepository.existsByProduct_IdAndCart_Id(requestProduct.getProductId(), id))
                throw new BusinessException("This product does not exist in this cart".toUpperCase());
        });

        Double totalPurchase = request.getProducts().stream().mapToDouble(requestProduct -> {
            Product product = productRepository.findById(requestProduct.getProductId()).orElseThrow(() -> new NotFoundException("Product not found".toUpperCase()));
            WarehouseSection warehouseSection = warehouseSectionRepository.findWarehouseSectionByProductId(requestProduct.getProductId());

            for (CartProduct cartProduct : cartProductList) {
                if (cartProduct.getCart().getId().equals(id) && cartProduct.getProduct().getId().equals(requestProduct.getProductId())) {
                    Integer diff = cartProduct.getQuantity() - requestProduct.getQuantity();

                    if (product.getQuantity() + diff < 0)
                        throw new BusinessException("Ordered quantity is greater than what is in stock".toUpperCase());

                    product.setQuantity(product.getQuantity() + diff);
                    cartProduct.setQuantity(requestProduct.getQuantity());

                    warehouseSection.increaseTotalProducts(diff);

                    productRepository.save(product);
                    cartProductRepository.save(cartProduct);
                    warehouseSectionRepository.save(warehouseSection);
                    break;
                }
            }
            ;

            return product.getPrice() * requestProduct.getQuantity();
        }).sum();

        return PurchaseOrderResponse.builder()
                .totalPrice(BigDecimal.valueOf(totalPurchase))
                .build();
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