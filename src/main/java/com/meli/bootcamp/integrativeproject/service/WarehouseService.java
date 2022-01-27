package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> findAll(){
        return warehouseRepository.findAll();
    }

    public Warehouse findById(Long id){
        return warehouseRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    public Warehouse save(Warehouse warehouse){
        return warehouseRepository.save(warehouse);
    }

    public void deleteById(Long id){
        warehouseRepository.deleteById(id);
    }
}
