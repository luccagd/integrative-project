package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fresh-products")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

}
