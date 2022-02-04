package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import org.springframework.stereotype.Service;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.PurchaseOrderResponse;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;

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
        Buyer buyer = buyerRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new NotFoundException("Buyer not exists!"));

        Cart buyerCart = new Cart();
        buyerCart.setBuyer(buyer);
        buyer.getCarts().add(buyerCart);

        requestProductsValidations(request.getProducts());

        Double totalPurchase = request.getProducts().stream().mapToDouble(requestProduct -> {
            Product findProduct = productRepository.findById(requestProduct.getProductId()).get();
            WarehouseSection warehouseSection = warehouseSectionRepository.findWarehouseSectionByProductId(findProduct.getId());

            findProduct.setQuantity(findProduct.getQuantity() - requestProduct.getQuantity());

            updateWarehouseSection(warehouseSection, requestProduct.getQuantity());

            saveCartProduct(buyerCart, findProduct, requestProduct.getQuantity());
            return findProduct.getPrice() * requestProduct.getQuantity();
        }).sum();

        buyerCart.setStatus(CartStatus.FECHADO);
        buyerRepository.save(buyer);
        cartRepository.save(buyerCart);

        return PurchaseOrderResponse.builder()
                .totalPrice(valueOf(totalPurchase))
                .build();
    }

    @Transactional
    public PurchaseOrderResponse put(Long id, PurchaseOrderRequest request) {
        requestProductsValidations(request.getProducts());
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

    public void saveCartProduct(Cart cart, Product product, Integer quantity) {
        cartProductRepository.save(CartProduct.builder().cart(cart).product(product).quantity(quantity).build());
    }

    @Transactional
    public void updateWarehouseSection(WarehouseSection warehouseSection, Integer quantity) {
        if (warehouseSection.getTotalProducts() < quantity)
            throw new BusinessException("Ordered quantity is greater than what is in stock".toUpperCase());
        warehouseSection.setTotalProducts(warehouseSection.getTotalProducts() - quantity);
    }

    @Transactional
    public void requestProductsValidations(List<PurchaseOrderProductRequest> requestProductList) {
        requestProductList.stream().forEach(requestProduct -> {
            if (requestProduct.getQuantity() <= 0)
                throw new BusinessException("Quantity is less than 1".toUpperCase());
        });
    }
}