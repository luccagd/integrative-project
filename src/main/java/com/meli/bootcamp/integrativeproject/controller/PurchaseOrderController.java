package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.service.CartProductService;
import com.meli.bootcamp.integrativeproject.service.CartService;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fresh-products")
public class PurchaseOrderController {
    private CartProductService cartProductService;
    private CartService cartService;
    private PurchaseOrderService purchaseOrderService;

   public PurchaseOrderController(PurchaseOrderService purchaseOrderService, CartService cartService,CartProductService cartProductService){
        this.cartProductService = cartProductService;
        this.cartService = cartService;
        this.purchaseOrderService = purchaseOrderService; 
   }

    @PostMapping("/orders")
    public ResponseEntity<Object> save(@RequestBody PurchaseOrderRequest purchaseOrderRequest){
        return ResponseEntity.ok().body(purchaseOrderService.save(purchaseOrderRequest));
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Object> put(@PathVariable Long id, @RequestBody PurchaseOrderRequest purchaseOrderRequest ){
        return ResponseEntity.ok().body(purchaseOrderService.put(id, purchaseOrderRequest));
    }

    @GetMapping("/teste/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(purchaseOrderService.findById(id));
    }
}
