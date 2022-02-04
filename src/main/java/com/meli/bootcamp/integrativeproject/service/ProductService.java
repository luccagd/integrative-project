package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.response.WarehouseProductDTO;
import com.meli.bootcamp.integrativeproject.dto.response.WarehouseProductResponse;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository.ProductInWarehouse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();

        if (products == null || products.isEmpty()) {
            throw new NotFoundException("No products were found");
        }

        return products;
    }

    public List<Product> findAllByCategory(String category) {
        Category categoryEnum = getCorrespondingEnum(category);
        if (categoryEnum == null) {
            throw new InvalidEnumException();
        }

        List<Product> products = productRepository.findAllByCategory(categoryEnum);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for the given category");
        }

        return products;
    }

    private Category getCorrespondingEnum(String category) {
        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(category)) {
                return c;
            }
        }

        return null;
    }

    public List<Product> findAllByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for the given name");
        }

        return products;
    }

    public List<Product> findAllByNameAndDueDate(String name, String orderBy) {
        List<Product> products = findAllByName(name);

        products = products.stream().filter(product ->
                product.getDueDate().compareTo(LocalDate.now().plusWeeks(3)) > 0
        ).collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new NotFoundException("No products found within the due date for the given name");
        }

        if (orderBy != null) {
            sortList(products, orderBy);
        }

        return products;
    }

    private void sortList(List<Product> products, String orderBy) {
        switch (orderBy) {
            case "L":
                Collections.sort(products, Comparator.comparingInt(o -> o.getBatch().getBatchNumber()));
                break;
            case "C":
                Collections.sort(products, Comparator.comparingInt(o -> o.getQuantity()));
                break;
            case "F":
                Collections.sort(products, Comparator.comparing(Product::getDueDate));
                break;
            default:
                throw new BusinessException("Valid values for sorting are L, C or F");
        }
    }

    public WarehouseProductResponse findAllByNameInWarehouse(String name){
        List<WarehouseProductDTO> productDTOList = new ArrayList<>();
        WarehouseProductResponse response = new WarehouseProductResponse(name, productDTOList);
        List<ProductInWarehouse> productInWarehouseList = productRepository.findAllByNameInWarehousesIgnoreCase(name);

        if(productInWarehouseList.isEmpty())
            throw new NotFoundException("Product not found".toUpperCase());

        productInWarehouseList.stream().forEach(productInWarehouse -> {
            productDTOList.add(WarehouseProductDTO.builder()
                    .warehouseCode(productInWarehouse.getWarehouse_id())
                    .totalQuantity(productInWarehouse.getQuantity_product())
                    .build());
        });

        return response;
    }
}
