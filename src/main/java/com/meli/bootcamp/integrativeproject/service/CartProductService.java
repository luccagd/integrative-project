package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.response.CartProductDto;
import com.meli.bootcamp.integrativeproject.entity.Cart;
import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import com.meli.bootcamp.integrativeproject.repositories.CartProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.CartRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CartProductService {

     @Autowired
     private CartProductRepository repository;



    public List<CartProductDto> getjoininfo(Long id) {

     return repository.findByCart_Id(id);
    }

}