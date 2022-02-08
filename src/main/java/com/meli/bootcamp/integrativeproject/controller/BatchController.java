package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.service.BatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fresh-products/")
public class BatchController {

    private BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/due-date")
    public ResponseEntity<Object> findBySectionName(@RequestParam(name = "sectionName") String sectionName,
                                                    @RequestParam(name = "numberOfDays") Integer numberOfDays, @RequestParam(name = "asc") String asc) {
        return ResponseEntity.ok().body(batchService.findAllBySectionName(sectionName, numberOfDays, asc));
    }
}
