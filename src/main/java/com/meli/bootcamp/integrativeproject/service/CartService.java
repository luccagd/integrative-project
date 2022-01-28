package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Cart;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private CartRepository cartRepository;

    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public List<Cart> findAll(){
        return cartRepository.findAll();
    }

    public Cart findById(Long id){
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }

    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public void deleteById(Long id){
        cartRepository.deleteById(id);
    }
}
