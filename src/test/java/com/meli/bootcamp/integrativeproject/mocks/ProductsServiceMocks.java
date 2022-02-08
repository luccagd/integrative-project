package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.entity.Batch;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;

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

    public static Batch makeFakeBatch() {
        return Batch.builder()
                .warehouse(null)
                .section(null)
                .products(null)
                .build();
    }

    public static ProductRepository.ProductInWarehouse makeFakeProductInWarehouse(Integer productQuantity, Long warehouseId, String productName) {
        ProductRepository.ProductInWarehouse productInWarehouse = new ProductInWarehouse(productQuantity, warehouseId, productName);

        return productInWarehouse;
    }

    public static class ProductInWarehouse implements ProductRepository.ProductInWarehouse {
        private Integer productQuantity;
        private Long warehouseId;
        private String productName;

        public ProductInWarehouse(Integer productQuantity, Long warehouseId, String productName) {
            this.productQuantity = productQuantity;
            this.warehouseId = warehouseId;
            this.productName = productName;
        }

        @Override
        public Integer getQuantity_product() {
            return this.productQuantity;
        }

        @Override
        public Long getWarehouse_id() {
            return this.warehouseId;
        }

        @Override
        public String getProduct_name() {
            return this.productName;
        }
    }
}
