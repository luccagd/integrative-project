package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Batch;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchService {

    @Autowired
    BatchRepository repository;

    public Batch saveBatch(Long id, Batch batch) {
    return repository.save(batch);

    }

    public Batch updateBatch(Long id, Batch batch) {
       return  repository.save(batch);
    }

    public  void  deleteBatchById(Long id){
        repository.deleteById(id);
    }
    public Batch  findBatchById(Long id){

      return  repository.findById(id).orElseThrow(() -> new NullPointerException("Batch not Found"));
    }


}
