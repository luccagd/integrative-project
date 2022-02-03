package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
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
    public ResponseEntity<Object> postPurchaseOrder(@RequestBody PurchaseOrderRequest request){
        return ResponseEntity.ok().body(purchaseOrderService.save(request));
    }

    @PutMapping
    public ResponseEntity<Object> putCartProducts(@RequestParam(name = "idOrder") Long id, @RequestBody PurchaseOrderRequest request) {
        return ResponseEntity.ok().body(purchaseOrderService.put(id, request));
    }
}
