package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import com.meli.bootcamp.integrativeproject.repositories.CartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CartProductService {

     @Autowired
     private CartProductRepository repository;

    public List<CartProduct> findByIdCart(Long id) {
        return  repository.findAll();
    }
}
