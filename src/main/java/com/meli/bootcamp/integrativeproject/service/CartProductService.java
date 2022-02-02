package com.meli.bootcamp.integrativeproject.service;
import com.meli.bootcamp.integrativeproject.repositories.CartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meli.bootcamp.integrativeproject.repositories.CartProductRepository.CartProductDto;

import java.util.List;

@Service
public class CartProductService {

     @Autowired
     private CartProductRepository repository;

    public List<CartProductDto> getjoininfo(Long id) {

     return repository.findByCart_Id(id);
    }

}
