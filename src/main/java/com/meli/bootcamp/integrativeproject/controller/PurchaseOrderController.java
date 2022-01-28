package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.service.CartService;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fresh-products")
public class PurchaseOrderController {
    private CartService cartService;
    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, CartService cartService){
        this.purchaseOrderService = purchaseOrderService;
        this.cartService = cartService;
    }


    @PostMapping("/orders")
    public ResponseEntity<Object> save(@RequestBody PurchaseOrderRequest purchaseOrderRequest){
        return ResponseEntity.ok().body(purchaseOrderService.save(purchaseOrderRequest));
    }

    @GetMapping("/orders/{idOrder}")
    public ResponseEntity<List<Product>> listProducts(@PathVariable Long idOrder){
        return ResponseEntity.ok().body(cartService.productsByOrder(idOrder));
    }
}
