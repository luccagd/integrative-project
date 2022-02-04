package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.Buyer;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;

import java.time.LocalDate;
import java.util.Arrays;

public class PurchaseOrderServiceMocks {
    public static PurchaseOrderProductRequest makeFakePurchaseOrderProductRequest() {
        PurchaseOrderProductRequest purchaseOrderProductRequest = new PurchaseOrderProductRequest();
        purchaseOrderProductRequest.setProductId(1L);
        purchaseOrderProductRequest.setQuantity(10);

        return purchaseOrderProductRequest;
    }

    public static PurchaseOrderRequest makeFakePurchaseOrderRequest() {
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
        purchaseOrderRequest.setBuyerId(1L);
        purchaseOrderRequest.setDate(LocalDate.now());
        purchaseOrderRequest.setProducts(Arrays.asList(makeFakePurchaseOrderProductRequest()));

        return purchaseOrderRequest;
    }

    public static Buyer makeFakeBuyer() {
        return new Buyer(1L, "John", null);
    }

    public static Product makeFakeProduct() {
        return new Product(1L, "Produto A", 1.0, 1.0, 10, LocalDate.of(2022, 10, 10), 10.0, null, Category.CONGELADO, null);
    }
}
