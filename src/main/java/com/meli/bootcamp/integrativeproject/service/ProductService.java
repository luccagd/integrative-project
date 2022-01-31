package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Product> findAllByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        if (products.isEmpty()) {
            throw new NotFoundException("No products found for the given name");
        }

        return products;
    }

    public List<Product> findAllByNameAndDueDate(String name) {
        List<Product> products = findAllByName(name);

        products = products.stream().filter(product ->
                product.getDueDate().compareTo(LocalDate.now().plusWeeks(3)) > 0
        ).collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new NotFoundException("No products found within the due date for the given name");
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
