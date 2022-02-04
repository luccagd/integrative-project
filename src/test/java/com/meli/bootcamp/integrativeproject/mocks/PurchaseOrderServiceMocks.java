package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;

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
}
