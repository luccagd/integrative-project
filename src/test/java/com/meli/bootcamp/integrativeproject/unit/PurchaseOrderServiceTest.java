package com.meli.bootcamp.integrativeproject.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.PurchaseOrderServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.*;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
import com.meli.bootcamp.integrativeproject.mocks.InboundOrderServiceMocks;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class PurchaseOrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private WarehouseSectionRepository warehouseSectionRepository;

    private PurchaseOrderService service;

    private Long existentBuyerId = 1L;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        service = new PurchaseOrderService(productRepository, buyerRepository, cartRepository, cartProductRepository, warehouseSectionRepository);
    }

    @Test
    public void shouldBeThrowIfBuyerNotExistsWhenTrySave() {
        Long notExistentBuyerId = 999L;

        var request = PurchaseOrderServiceMocks.makeFakePurchaseOrderRequest();
        request.setBuyerId(notExistentBuyerId);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(request));

        assertEquals("Buyer not exists!", exception.getMessage());
    }
}
