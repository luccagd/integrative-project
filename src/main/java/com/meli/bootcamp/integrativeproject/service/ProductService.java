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
        return productRepository.findAll();
    }

    public List<Product> findAllByCategory(String category) {
        Category categoryEnum = getCorrespondingEnum(category);
        if (categoryEnum == null) {
            throw new InvalidEnumException();
        }

        List<Product> products = productRepository.findAllByCategory(categoryEnum);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for the indicated category");
        }

        return products;
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
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
