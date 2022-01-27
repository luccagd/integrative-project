package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.InboundOrder;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("fresh-products/inboundorder")
public class InboundOrderController {

    private InboundOrderService service;

    public InboundOrderController(InboundOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InboundOrderResponseDTO> save(@Valid @RequestBody InboundOrderRequestDTO inboundOrderRequestDTO, @RequestHeader(value = "agentId") Long agentId) {
            InboundOrderResponseDTO inboundOrderResponseDTO = service.save(inboundOrderRequestDTO, agentId);
            return ResponseEntity.created(null).body(inboundOrderResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<InboundOrder>> getAll(){
        return ResponseEntity.ok().body(service.getAll());
    }
}
