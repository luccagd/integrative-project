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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Test
    public void shouldBeThrowIfProductDoesNotHaveEnoughStockWhenTrySave() {
        var request = PurchaseOrderServiceMocks.makeFakePurchaseOrderRequest();
        request.setBuyerId(existentBuyerId);

        var fakeBuyer = PurchaseOrderServiceMocks.makeFakeBuyer();

        var fakeProduct = PurchaseOrderServiceMocks.makeFakeProduct();
        fakeProduct.setQuantity(1);

        when(buyerRepository.findById(request.getBuyerId())).thenReturn(Optional.of(fakeBuyer));
        when(productRepository.findById(request.getProducts().get(0).getProductId())).thenReturn(Optional.of(fakeProduct));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request));

        assertEquals("Product " + fakeProduct.getName() + " does not have enough stock for this quantity!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfProductHasAnExpirationDateOfLessThan3WeeksWhenTrySave() {
        var request = PurchaseOrderServiceMocks.makeFakePurchaseOrderRequest();
        request.setBuyerId(existentBuyerId);

        var fakeBuyer = PurchaseOrderServiceMocks.makeFakeBuyer();

        var fakeProduct = PurchaseOrderServiceMocks.makeFakeProduct();
        fakeProduct.setQuantity(20);
        fakeProduct.setDueDate(LocalDate.now().plusWeeks(4));

        when(buyerRepository.findById(request.getBuyerId())).thenReturn(Optional.of(fakeBuyer));
        when(productRepository.findById(request.getProducts().get(0).getProductId())).thenReturn(Optional.of(fakeProduct));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request));

        assertEquals("Product " + fakeProduct.getName() + " has an expiration date of less than 3 weeks!", exception.getMessage());
    }
}
