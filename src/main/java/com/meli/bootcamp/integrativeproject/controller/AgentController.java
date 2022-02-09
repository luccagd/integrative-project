package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.request.AgentRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.AgentResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private AgentService service;

    public AgentController(AgentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AgentResponseDTO>> findAll() {
        List<Agent> agents = service.findAll();

        return ResponseEntity.ok(AgentResponseDTO.entityListToDtoList(agents));
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<AgentResponseDTO> findById(@PathVariable Long agentId) {
        Agent agent = service.findById(agentId);

        return ResponseEntity.ok(AgentResponseDTO.toDTO(agent));
    }

    @PostMapping
    public ResponseEntity<AgentResponseDTO> save(@RequestBody AgentRequestDTO agentRequestDTO) {
        Agent agent = service.save(agentRequestDTO);

        return ResponseEntity.ok(AgentResponseDTO.toDTO(agent));
    }

    @PutMapping("/{agentId}")
    public ResponseEntity<AgentResponseDTO> update(@RequestBody AgentRequestDTO agentRequestDTO, @PathVariable Long agentId) {
        Agent agent = service.update(agentRequestDTO, agentId);

        return ResponseEntity.ok(AgentResponseDTO.toDTO(agent));
    }
}
