package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.PurchaseOrderResponse;
import com.meli.bootcamp.integrativeproject.entity.Buyer;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.entity.Section;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.SectionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PurchaseOrderService {

    private ProductRepository productRepository;
    private BuyerRepository buyerRepository;
    private SectionRepository sectionRepository;

    public PurchaseOrderService(ProductRepository productRepository, BuyerRepository buyerRepository, SectionRepository sectionRepository){
        this.productRepository = productRepository;
        this.buyerRepository = buyerRepository;
        this.sectionRepository = sectionRepository;
    }

    public PurchaseOrderResponse save(PurchaseOrderRequest request){
        BigDecimal totalPrice = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);

        request.getProducts().forEach(product -> {
            Product newProduct = productRepository.findById(product.getProductId())
                                                  .orElseThrow(() -> new NotFoundException("Product Id is not found"));

            if(product.getQuantity() <= 0)
                throw new BusinessException("Quantity is less than 1".toUpperCase());
            if(product.getQuantity() > newProduct.getQuantity())
                throw new BusinessException("Ordered quantity is greater than what is in stock".toUpperCase());

            totalPrice.add(new BigDecimal(newProduct.getPrice() * product.getQuantity());
            addToCart(buyerRepository.getById(request.getBuyerId()), newProduct, product.getQuantity());
            updateSection(newProduct.getBatch().getSection(), product.getQuantity());
            newProduct.setQuantity(newProduct.getQuantity() - product.getQuantity());

            productRepository.save(newProduct);
        });


        return  PurchaseOrderResponse.builder()
                .totalPrice(totalPrice)
                .build();
    }

    public void updateSection(Section newSection, Integer quantity){
        newSection.setTotalProducts(newSection.getTotalProducts() - quantity);
        sectionRepository.save(newSection);
    }

    public void addToCart(Buyer buyer, Product product, Integer quantity){
        if(buyer.getCart().getStatus().equals(CartStatus.FECHADO))
            throw new BusinessException("Cart is closed".toUpperCase());
        product.setQuantity(quantity);
        buyer.getCart().getProducts().add(product);
    }
}
