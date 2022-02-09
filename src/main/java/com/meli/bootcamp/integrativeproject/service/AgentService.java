package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.AgentRequestDTO;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private AgentRepository agentRepository;

    private WarehouseRepository warehouseRepository;

    public AgentService(AgentRepository agentRepository, WarehouseRepository warehouseRepository) {
        this.agentRepository = agentRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public Agent save(AgentRequestDTO agentRequestDTO) {
        Warehouse warehouse = warehouseRepository.findById(agentRequestDTO.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("No Warehouse were found for the given id"));

        if (warehouse.getAgent() != null) {
            throw new BusinessException("The given warehouse is already assigned to an agent");
        }

        Agent agent = AgentRequestDTO.toEntity(agentRequestDTO, warehouse);

        return agentRepository.save(agent);
    }
}
