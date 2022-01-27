package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Seller;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public List<Seller> findAll(){
        return sellerRepository.findAll();
    }

    public Seller findById(Long id){
        return sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    public Seller save(Seller seller){
        return sellerRepository.save(seller);
    }

    public void deleteById(Long id){
        sellerRepository.deleteById(id);
    }
}
