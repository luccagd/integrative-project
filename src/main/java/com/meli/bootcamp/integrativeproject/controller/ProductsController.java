package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.response.BatchProductResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.ProductResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fresh-products")
public class ProductsController {

    private ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<Product> products = service.findAll();

        return ResponseEntity.ok(ProductResponseDTO.entityListToDtoList(products));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseDTO>> findAllByCategory(@RequestParam String category) {
        List<Product> products = service.findAllByCategory(category);

        return ResponseEntity.ok(ProductResponseDTO.entityListToDtoList(products));
    }

    @GetMapping("/list/byName")
    public ResponseEntity<List<BatchProductResponseDTO>> findAllByNameAndDueDate(@RequestParam String name) {
        List<Product> products = service.findAllByNameAndDueDate(name);

        return ResponseEntity.ok(BatchProductResponseDTO.entityListToDtoList(products));
    }
}
