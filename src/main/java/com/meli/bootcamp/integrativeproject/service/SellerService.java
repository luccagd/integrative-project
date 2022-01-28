package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Seller;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;


    public List<Seller> findAll(){
        return sellerRepository.findAll();
    }

    public Seller findById(Long id){
        return sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }

    public Seller save(Seller seller){
        return sellerRepository.save(seller);
    }

    public void deleteById(Long id){
        sellerRepository.deleteById(id);
    }
}
