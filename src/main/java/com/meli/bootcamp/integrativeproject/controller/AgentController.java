package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.AgentRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.AgentResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private AgentService service;

    public AgentController(AgentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgentResponseDTO> save(@RequestBody AgentRequestDTO agentRequestDTO) {
        Agent agent = service.save(agentRequestDTO);

        return ResponseEntity.ok(AgentResponseDTO.toDTO(agent));
    }
}
