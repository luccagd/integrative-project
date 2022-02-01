package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Cart;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.CartRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {
    private ProductRepository repository;
    private CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> findByIdCart() {
        return cartRepository.findAll();
    }

    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }


}
