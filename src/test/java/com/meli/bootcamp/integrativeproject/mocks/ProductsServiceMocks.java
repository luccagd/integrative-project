package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsServiceMocks {
    public static List<Product> makeFakeListOfProducts(int size) {
        List<Product> listOfProducts = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            Product product = Product.builder()
                    .batch(null)
                    .build();

            listOfProducts.add(product);
        }

        return listOfProducts;
    }
}
