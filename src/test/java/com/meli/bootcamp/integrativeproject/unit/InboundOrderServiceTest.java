package com.meli.bootcamp.integrativeproject.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.entity.Seller;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.InboundOrderRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class InboundOrderServiceTest {

    private InboundOrderService service;

    @Mock
    private InboundOrderRepository inboundOrderRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SellerRepository sellerRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        service = new InboundOrderService(inboundOrderRepository, warehouseRepository, sellerRepository);
    }

    @Test
    public void shouldBeThrowIfSellerNotExistsWhenTrySave() {
        var sellerIdNotExistent = 1L;
        var agentId = 1L;
        var request = makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdNotExistent);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(request, agentId));

        assertEquals("Seller not found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfWarehouseNotExistsWhenTrySave() {
        var sellerIdExistent = 1L;
        var agentId = 1L;
        var request = makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);

        var seller = makeFakeSeller();

        when(sellerRepository.findById(sellerIdExistent)).thenReturn(Optional.of(seller));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(request, agentId));

        assertEquals("Warehouse not found for the given id", exception.getMessage());
    }

    private InboundOrderRequestDTO makeFakeInboundOrderRequestDTO() {
        return new InboundOrderRequestDTO();
    }

    private Seller makeFakeSeller() {
        return new Seller();
    }
}