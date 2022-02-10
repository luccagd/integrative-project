package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.dto.request.AgentRequestDTO;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.AgentServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import com.meli.bootcamp.integrativeproject.service.AgentService;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AgentServiceTest {

    private AgentService agentService;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        agentService = new AgentService(agentRepository, warehouseRepository);
    }

    @Test
    public void shouldThrowExceptionIfAgentNotFoundWhenFindAll() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> agentService.findAll());
        assertEquals("No Agents were found", exception.getMessage());
    }

    @Test
    public void shouldFindAllAgents() {
        List<Agent> agents = Arrays.asList(AgentServiceMocks.makeFakeAgent(), AgentServiceMocks.makeFakeAgent());

        when(agentRepository.findAll()).thenReturn(agents);
        List<Agent> response = agentService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }


    @Test
    public void shouldThrowExceptionIfAgentNotFoundWhenFindById() {
        var agentId = 1L;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> agentService.findById(agentId));
        assertEquals("No Agent were found for the given id", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfWarehouseNotFoundWhenTrySave() {
        var agentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> agentService.save(agentRequestDTO));
        assertEquals("No Warehouse were found for the given id", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfWarehouseAlreadyHaveAgent() {
        var agentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();

        Warehouse fakeWarehouse = AgentServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(AgentServiceMocks.makeFakeAgent());
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(fakeWarehouse));

        BusinessException exception = assertThrows(BusinessException.class, () -> agentService.save(agentRequestDTO));
        assertEquals("The given warehouse is already assigned to an agent", exception.getMessage());
    }

    @Test
    public void shouldCreateAgentSuccessfully() {
        var fakeAgentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();
        var fakeAgent = AgentServiceMocks.makeFakeAgent();

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(AgentServiceMocks.makeFakeWarehouse()));
        when(agentRepository.save(any(Agent.class))).thenReturn(fakeAgent);
        var response = agentService.save(fakeAgentRequestDTO);

        assertNotNull(response.getId());
        assertEquals(fakeAgentRequestDTO.getName(), response.getName());
    }

    @Test
    public void shouldThrowExceptionIfWarehouseNotFoundWhenTryUpdate() {
        var agentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();
        var agentId = 1L;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> agentService.update(agentRequestDTO, agentId));
        assertEquals("No Warehouse were found for the given id", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfWarehouseAgentIsDifferentFromTheAgentBeingUpdated() {
        var agentIdFromRequest = 1L;

        var fakeAgentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();
        var fakeAgent = AgentServiceMocks.makeFakeAgent();
        fakeAgent.setId(2L);

        Warehouse fakeWarehouse = AgentServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(AgentServiceMocks.makeFakeAgent());
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(fakeWarehouse));

        when(agentRepository.findById(anyLong())).thenReturn(Optional.of(fakeAgent));

        BusinessException exception = assertThrows(BusinessException.class, () -> agentService.update(fakeAgentRequestDTO, agentIdFromRequest));
        assertEquals("The given warehouse is already assigned to an agent", exception.getMessage());
    }

    @Test
    public void shouldUpdateAgentSuccessfully() {
        var agentIdFromRequest = 1L;
        var fakeAgentRequestDTO = AgentServiceMocks.makeFakeAgentRequestDTO();
        fakeAgentRequestDTO.setName("Doe John");

        var fakeAgent = AgentServiceMocks.makeFakeAgent();

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(AgentServiceMocks.makeFakeWarehouse()));

        when(agentRepository.findById(anyLong())).thenReturn(Optional.of(fakeAgent));
        when(agentRepository.save(any(Agent.class))).thenReturn(fakeAgent);

        var updatedAgent = agentService.update(fakeAgentRequestDTO, agentIdFromRequest);
        assertEquals(agentIdFromRequest, updatedAgent.getId());
        assertEquals(fakeAgentRequestDTO.getName(), updatedAgent.getName());
    }

}
