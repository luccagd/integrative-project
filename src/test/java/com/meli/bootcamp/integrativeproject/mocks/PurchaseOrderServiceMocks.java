package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.Buyer;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.entity.WarehouseSection;
import com.meli.bootcamp.integrativeproject.enums.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderServiceMocks {
    public static List<PurchaseOrderProductRequest> makeFakeListPurchaseOrderProductRequest(Integer size) {
        List<PurchaseOrderProductRequest> listOfPurchaseOrderProductRequest = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            PurchaseOrderProductRequest purchaseOrderProductRequest = new PurchaseOrderProductRequest();

            listOfPurchaseOrderProductRequest.add(purchaseOrderProductRequest);
        }

        return listOfPurchaseOrderProductRequest;
    }

    public static List<Product> makeFakeListOfProduct(Integer size) {
        List<Product> listOfProducts = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Product product = new Product(1L, "any name", 1.0, 1.0, 10, LocalDate.of(2022, 10, 10), 10.0, null, Category.CONGELADO, null);

            listOfProducts.add(product);
        }

        return listOfProducts;
    }

    public static PurchaseOrderRequest makeFakePurchaseOrderRequest() {
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();

        return purchaseOrderRequest;
    }

    public static Buyer makeFakeBuyer() {
        return new Buyer(1L, "John", null);
    }

    public static WarehouseSection makeFakeWarehouseSection() {
        return new WarehouseSection(1L, null, null, 0, 0);
    }

}
