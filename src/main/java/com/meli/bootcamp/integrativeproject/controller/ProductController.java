package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.response.ProductBatchResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.ProductResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fresh-products")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<Product> products = service.findAll();

        return ResponseEntity.ok(ProductResponseDTO.entityListToDtoList(products));
    }

    @GetMapping("/list/byCategory")
    public ResponseEntity<List<ProductResponseDTO>> findAllByCategory(@RequestParam String category) {
        List<Product> products = service.findAllByCategory(category);

        return ResponseEntity.ok(ProductResponseDTO.entityListToDtoList(products));
    }

    @GetMapping("/list/byName")
    public ResponseEntity<List<ProductBatchResponseDTO>> findAllByNameAndDueDate(@RequestParam String name, @RequestParam(required = false) String orderBy) {
        List<Product> products = service.findAllByNameAndDueDate(name, orderBy);

        return ResponseEntity.ok(ProductBatchResponseDTO.entityListToDtoList(products));
    }

    @GetMapping(value = "/warehouse")
    public ResponseEntity<Object> getProductQuantityByName(@RequestParam(name = "product_name") String name) {
        return ResponseEntity.ok().body(service.findAllByNameInWarehouse(name));
    }
}
