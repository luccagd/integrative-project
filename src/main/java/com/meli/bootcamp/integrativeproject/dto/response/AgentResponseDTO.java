package com.meli.bootcamp.integrativeproject.dto.response;

import com.meli.bootcamp.integrativeproject.entity.Agent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponseDTO {

    private Long id;

    private String name;

    private String userName;

    private Long warehouseId;

    private String warehouseName;

    public static AgentResponseDTO toDTO(Agent agent) {
        return AgentResponseDTO.builder()
                .id(agent.getId())
                .name(agent.getName())
                .userName(agent.getName())
                .warehouseId(agent.getWarehouse().getId())
                .warehouseName(agent.getWarehouse().getName())
                .build();
    }

    public static List<AgentResponseDTO> entityListToDtoList(List<Agent> agents) {
        return agents.stream().map(agent -> toDTO(agent)).collect(Collectors.toList());
    }
}
