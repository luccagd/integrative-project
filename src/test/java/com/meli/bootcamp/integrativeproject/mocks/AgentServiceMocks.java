package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.AgentRequestDTO;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;

public class AgentServiceMocks {

    public static AgentRequestDTO makeFakeAgentRequestDTO() {
        return new AgentRequestDTO(
                "John Doe", "username", "password", 1L
        );
    }

    public static Agent makeFakeAgent() {
        return Agent.builder()
                .id(1L)
                .name("John Doe")
                .build();
    }

    public static Warehouse makeFakeWarehouse() {
        return Warehouse.builder().build();
    }
}
