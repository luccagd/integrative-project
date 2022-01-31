package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.PurchaseOrderResponse;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PurchaseOrderService {

    private ProductRepository productRepository;
    private BuyerRepository buyerRepository;
    private SectionRepository sectionRepository;
    private CartRepository cartRepository;
    private CartProductRepository cartProductRepository;

    public PurchaseOrderService(ProductRepository productRepository, BuyerRepository buyerRepository, SectionRepository sectionRepository, CartRepository cartRepository, CartProductRepository cartProductRepository) {
        this.productRepository = productRepository;
        this.buyerRepository = buyerRepository;
        this.sectionRepository = sectionRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public PurchaseOrderResponse save(PurchaseOrderRequest request) {
        Buyer buyer = buyerRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new NotFoundException("Buyer Id is not found".toUpperCase()));

        Cart buyerCart = new Cart();
        buyerCart.setBuyer(buyer);
        buyer.getCarts().add(buyerCart);

        Double totalPurchase = request.getProducts().stream().mapToDouble(product -> {
            Product findProduct = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product Id is not found".toUpperCase()));

            if (product.getQuantity() <= 0)
                throw new BusinessException("Quantity is less than 1".toUpperCase());
            if (product.getQuantity() > findProduct.getQuantity())
                throw new BusinessException("Ordered quantity is greater than what is in stock".toUpperCase());

            findProduct.setQuantity(findProduct.getQuantity() - product.getQuantity());

            updateSection(findProduct.getBatch().getSection(), product.getQuantity());

            saveCartProduct(buyerCart, findProduct, product.getQuantity());

            return findProduct.getPrice() * product.getQuantity();
        }).sum();

        buyerCart.setStatus(CartStatus.FECHADO);

        buyerRepository.save(buyer);

        cartRepository.save(buyerCart);


        return PurchaseOrderResponse.builder()
                .totalPrice(BigDecimal.valueOf(totalPurchase))
                .build();
    }

    public void updateSection(Section newSection, Integer quantity) {
        Integer total = newSection.getTotalProducts() - quantity;

        if (total < 0)
            throw new BusinessException("Quantity is less than 0");
        newSection.decreaseTotalProducts(quantity);
        sectionRepository.save(newSection);
    }

    public void saveCartProduct(Cart cart, Product product, Integer quantity) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(quantity);
        cartProductRepository.save(cartProduct);
    }

    public void put(Long id, PurchaseOrderRequest request){
        CartProduct cartProduct = cartProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart is not found".toUpperCase()));
    }
}
