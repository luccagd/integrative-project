package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.response.BatchSectionNameResponse;
import com.meli.bootcamp.integrativeproject.dto.response.BatchStock;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository.BatchResponse;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {

    private BatchRepository batchRepository;
    private ProductRepository productRepository;

    public BatchService(BatchRepository batchRepository, ProductRepository productRepository)
    {
        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
    }


    public BatchSectionNameResponse findAllBySectionName(String sectionName, Integer numberOfDays) {

        List<BatchResponse> batchResponseList = batchRepository.findAllBySectionNameAndDueDate(sectionName);
        List<BatchStock> batchStockList = new ArrayList<>();

        batchResponseList.stream().forEach(batchResponse ->{

            if (batchResponse.getDatediff() >= 0 && batchResponse.getDatediff() <= numberOfDays){
                    BatchStock batchStock = BatchStock.builder()
                                                      .batchNumber(batchResponse.getBatch_number())
                                                      .productId(batchResponse.getProduct_id())
                                                      .productTypeId(batchResponse.getProduct_category())
                                                      .dueDate(batchResponse.getDue_date())
                                                      .dateDiff(batchResponse.getDatediff())
                                                      .quantity(batchResponse.getQuantity())
                                                      .build();
                    batchStockList.add(batchStock);
        }});

        return BatchSectionNameResponse.builder()
                .batchStock(batchStockList).build();
    }
}
