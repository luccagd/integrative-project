package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
