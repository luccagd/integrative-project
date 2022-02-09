package com.meli.bootcamp.integrativeproject.dto.request;

import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentRequestDTO {

    private String name;

    private String userName;

    private String password;

    private Long warehouseId;

    public static Agent toEntity(AgentRequestDTO agentRequestDTO, Warehouse warehouse) {
        Agent agent = Agent.builder()
                .name(agentRequestDTO.getName())
                .warehouse(warehouse)
                .build();

        agent.setUserName(agentRequestDTO.getUserName());
        agent.setPassword(new BCryptPasswordEncoder().encode(agentRequestDTO.getPassword()));

        return agent;
    }
}
