package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.BatchResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fresh-products/inboundorder")
public class InboundOrderController {
    private InboundOrderService service;

    public InboundOrderController(InboundOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InboundOrderResponseDTO> save(@RequestBody InboundOrderRequestDTO inboundOrderRequestDTO,
            @RequestHeader(value = "agentId") Long agentId) {
        InboundOrderResponseDTO inboundOrderResponseDTO = service.save(inboundOrderRequestDTO, agentId);

        return ResponseEntity.created(null).body(inboundOrderResponseDTO);
    }

    @PutMapping(path = "/{inboundOrderId}/{productId}")
    public ResponseEntity<BatchResponseDTO> update(@RequestBody ProductRequestDTO productRequestDTO,
                                                   @PathVariable("inboundOrderId") Long inboundOrderId,
                                                   @PathVariable("productId") Long productId) {
        BatchResponseDTO batchResponseDTO = service.update(productRequestDTO, inboundOrderId, productId);

        return ResponseEntity.created(null).body(batchResponseDTO);
    }

}
