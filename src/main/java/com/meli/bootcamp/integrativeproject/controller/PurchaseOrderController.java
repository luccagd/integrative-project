package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.dto.response.CartProductResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fresh-products/orders")
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<List<CartProductResponseDTO>> findByCartId(@PathVariable Long idOrder) {
        List<CartProduct> cartProducts = purchaseOrderService.findByCartId(idOrder);

        return ResponseEntity.ok().body(CartProductResponseDTO.entityListToResponseDtoList(cartProducts));
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
