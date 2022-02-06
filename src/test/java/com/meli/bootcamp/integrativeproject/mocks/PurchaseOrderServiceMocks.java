package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
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

        for (int i = 1; i <= size; i++) {
            Product product = Product.builder()
                    .id(Integer.toUnsignedLong(i))
                    .name("any name")
                    .currentTemperature(1.0)
                    .minimalTemperature(1.0)
                    .quantity(10)
                    .dueDate(LocalDate.of(2022, 10, 10))
                    .price(10.0)
                    .category(Category.CONGELADO)
                    .build();

            listOfProducts.add(product);
        }

        return listOfProducts;
    }

    public static List<CartProduct> makeFakeListOfCartsProduct(Integer size) {
        List<CartProduct> listOfCartsProducts = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            CartProduct cartProduct = CartProduct.builder()
                    .id(Integer.toUnsignedLong(i))
                    .quantity(10)
                    .build();

            listOfCartsProducts.add(cartProduct);
        }

        return listOfCartsProducts;
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

    public static Cart makeFakeCart() {
        return new Cart(1L, CartStatus.ABERTO, null, null, null);
    }
}
