package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.Product;

import com.meli.bootcamp.integrativeproject.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameIgnoreCase(String name);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByName(String name);
}
