package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fresh-products/orders")
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody PurchaseOrderRequest request){
        return ResponseEntity.created(null).body(purchaseOrderService.save(request));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestParam(name = "idOrder") Long id, @RequestBody PurchaseOrderRequest request) {
        return ResponseEntity.ok().body(purchaseOrderService.update(request, id));
    }
}
