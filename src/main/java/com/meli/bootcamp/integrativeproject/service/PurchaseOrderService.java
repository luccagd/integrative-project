package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.PurchaseOrderResponse;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import com.meli.bootcamp.integrativeproject.repositories.CartRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.SectionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PurchaseOrderService {

    private ProductRepository productRepository;
    private BuyerRepository buyerRepository;
    private SectionRepository sectionRepository;
    private CartRepository cartRepository;

    public PurchaseOrderService(ProductRepository productRepository, BuyerRepository buyerRepository, SectionRepository sectionRepository, CartRepository cartRepository){
        this.productRepository = productRepository;
        this.buyerRepository = buyerRepository;
        this.sectionRepository = sectionRepository;
        this.cartRepository = cartRepository;
    }

    public PurchaseOrderResponse save(PurchaseOrderRequest request){
        Buyer buyer = buyerRepository.findById(request.getBuyerId())
                                     .orElseThrow(() -> new NotFoundException("Buyer Id is not found".toUpperCase()));

        Cart cart = buyer.getCart();

        Double totalPurchase = request.getProducts().stream().mapToDouble(product -> {
            Product newProduct = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product Id is not found".toUpperCase()));


            if (product.getQuantity() <= 0)
                throw new BusinessException("Quantity is less than 1".toUpperCase());
            if (product.getQuantity() > newProduct.getQuantity())
                throw new BusinessException("Ordered quantity is greater than what is in stock".toUpperCase());


            addToCart(cart, newProduct, product.getQuantity());
            updateSection(newProduct.getBatch().getSection(), product.getQuantity());

            productRepository.save(newProduct);

            return newProduct.getPrice() * product.getQuantity();
        }).sum();

        cart.setStatus(CartStatus.FECHADO);

        return  PurchaseOrderResponse.builder()
                .totalPrice(BigDecimal.valueOf(totalPurchase))
                .build();
    }

    public void updateSection(Section newSection, Integer quantity){
        Integer total = newSection.getTotalProducts() - quantity;

        if(total < 0)
            throw new BusinessException("Quantity is less than 0");

        newSection.decreaseTotalProducts(quantity);
        sectionRepository.save(newSection);
    }

    public void addToCart(Cart cart, Product product, Integer quantity){

        if(cart.getStatus().equals(CartStatus.FECHADO))
            throw new BusinessException("Cart is closed".toUpperCase());

        product.setQuantity(product.getQuantity() - quantity);
        cart.getProducts().add(product);
        cartRepository.save(cart);
    }
}
