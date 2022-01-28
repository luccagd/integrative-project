package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Buyer;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {

    private BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository){
        this.buyerRepository = buyerRepository;
    }

    public List<Buyer> findAll(){
        return buyerRepository.findAll();
    }

    public Buyer findById(Long id){
        return buyerRepository.findById(id).orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }

    public Buyer save(Buyer buyer){
        return buyerRepository.save(buyer);
    }

    public void deleteById(Long id){
        buyerRepository.deleteById(id);
    }
}
