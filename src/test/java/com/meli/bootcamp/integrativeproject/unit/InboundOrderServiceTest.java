package com.meli.bootcamp.integrativeproject.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.InboundOrderRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import com.meli.bootcamp.integrativeproject.service.InboundOrderService;
import com.meli.bootcamp.integrativeproject.unit.mocks.InboundOrderServiceMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
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

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdNotExistent);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(request, agentId));

        assertEquals("Seller not found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfWarehouseNotExistsWhenTrySave() {
        var sellerIdExistent = 1L;
        var agentId = 1L;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(request, agentId));

        assertEquals("Warehouse not found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfAgentNotBelongToWarehouseWhenTrySave() {
        var sellerIdExistent = 1L;
        var warehouseIdExistent = 1L;
        var agentIdNotBelongToWarehouse = 2L;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);
        request.setWarehouseId(warehouseIdExistent);

        var fakeAgent = InboundOrderServiceMocks.makeFakeAgent();
        fakeAgent.setId(1L);

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(fakeAgent);

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));
        when(warehouseRepository.findById(request.getWarehouseId())).thenReturn(Optional.of(fakeWarehouse));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request, agentIdNotBelongToWarehouse));

        assertEquals("Agent does not belong to the given warehouse", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfWarehouseNotHaveTheGivenSectionWhenTrySave() {
        var sellerIdExistent = 1L;
        var warehouseIdExistent = 1L;
        var sectionIdThatNotBelongToAnyWarehouse = 1L;
        var agentId = 1L;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);
        request.setWarehouseId(warehouseIdExistent);
        request.setSectionId(sectionIdThatNotBelongToAnyWarehouse);

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        var fakeAgent = InboundOrderServiceMocks.makeFakeAgent();
        fakeAgent.setId(1L);

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();
        fakeSection.setId(2L);

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSection(fakeSection);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(fakeAgent);
        fakeWarehouse.setWarehouseSections(Arrays.asList(fakeWarehouseSection));

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));
        when(warehouseRepository.findById(request.getWarehouseId())).thenReturn(Optional.of(fakeWarehouse));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request, agentId));

        assertEquals("Warehouse dont have the given section", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfProductCategoryIsNotEqualSectionCategoryWhenTrySave() {
        var sellerIdExistent = 1L;
        var warehouseIdExistent = 1L;
        var sectionIdExistent = 1L;
        var agentId = 1L;
        var differentCategoryInRelationToRequest = Category.REFRIGERADO;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);
        request.setWarehouseId(warehouseIdExistent);
        request.setSectionId(sectionIdExistent);
        request.setBatchStock(InboundOrderServiceMocks.makeFakeBatchRequestDTO(Category.FRESCO));

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        var fakeAgent = InboundOrderServiceMocks.makeFakeAgent();

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();
        fakeSection.setCategory(differentCategoryInRelationToRequest);

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSection(fakeSection);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(fakeAgent);
        fakeWarehouse.setWarehouseSections(Arrays.asList(fakeWarehouseSection));

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));
        when(warehouseRepository.findById(request.getWarehouseId())).thenReturn(Optional.of(fakeWarehouse));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request, agentId));

        assertEquals("Product category is not equal to section category", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfWarehouseSectionNotHaveEnoughSpace() {
        var sellerIdExistent = 1L;
        var warehouseIdExistent = 1L;
        var sectionIdExistent = 1L;
        var agentId = 1L;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);
        request.setWarehouseId(warehouseIdExistent);
        request.setSectionId(sectionIdExistent);
        request.setBatchStock(InboundOrderServiceMocks.makeFakeBatchRequestDTO(Category.FRESCO));

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        var fakeAgent = InboundOrderServiceMocks.makeFakeAgent();

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();
        fakeSection.setCategory(Category.FRESCO);

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSection(fakeSection);
        fakeWarehouseSection.setSize(10);
        fakeWarehouseSection.setTotalProducts(9);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(fakeAgent);
        fakeWarehouse.setWarehouseSections(Arrays.asList(fakeWarehouseSection));

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));
        when(warehouseRepository.findById(request.getWarehouseId())).thenReturn(Optional.of(fakeWarehouse));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(request, agentId));

        assertEquals("Batch is bigger than section size", exception.getMessage());
    }

    @Test
    public void shouldBeCreatedSuccessfullyInboundOrder() {
        var sellerIdExistent = 1L;
        var warehouseIdExistent = 1L;
        var sectionIdExistent = 1L;
        var agentId = 1L;

        var request = InboundOrderServiceMocks.makeFakeInboundOrderRequestDTO();
        request.setSellerId(sellerIdExistent);
        request.setWarehouseId(warehouseIdExistent);
        request.setSectionId(sectionIdExistent);
        request.setBatchStock(InboundOrderServiceMocks.makeFakeBatchRequestDTO(Category.FRESCO));

        var fakeSeller = InboundOrderServiceMocks.makeFakeSeller();

        var fakeAgent = InboundOrderServiceMocks.makeFakeAgent();

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();
        fakeSection.setCategory(Category.FRESCO);

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSection(fakeSection);
        fakeWarehouseSection.setSize(10);
        fakeWarehouseSection.setTotalProducts(0);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();
        fakeWarehouse.setAgent(fakeAgent);
        fakeWarehouse.setWarehouseSections(Arrays.asList(fakeWarehouseSection));

        var fakeProduct = InboundOrderServiceMocks.makeFakeProduct();
        fakeProduct.setName("Tilápia");
        fakeProduct.setCategory(Category.FRESCO);

        var fakeBatch = InboundOrderServiceMocks.makeFakeBatch();
        fakeBatch.setProducts(Arrays.asList(fakeProduct));

        var fakeInboundOrder = InboundOrderServiceMocks.makeFakeInboundOrder();
        fakeInboundOrder.setDateOrder(LocalDateTime.now());
        fakeInboundOrder.setBatch(fakeBatch);

        when(sellerRepository.findById(request.getSellerId())).thenReturn(Optional.of(fakeSeller));
        when(warehouseRepository.findById(request.getWarehouseId())).thenReturn(Optional.of(fakeWarehouse));
        when(inboundOrderRepository.save(any(InboundOrder.class))).thenReturn(fakeInboundOrder);

        var response = service.save(request, agentId);

        assertEquals("Tilápia", response.getBatchStock().getProducts().get(0).getName());
        assertNotNull(response.getOrderDate());
    }

    @Test
    public void shouldBeThrowIfInboundOrderNotExistsWhenTryUpdate() {
        var inboundOrderIdNotExistent = 1L;
        var productIdExistent = 1L;

        var request = InboundOrderServiceMocks.makeFakeProductRequestDTO(Category.FRESCO);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(request, inboundOrderIdNotExistent, productIdExistent));

        assertEquals("Inbound order not found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfTheGivenInboundOrderNotHaveTheProductWhenTryUpdate() {
        var inboundOrderIdExistent = 1L;
        var productIdNotExistent = 999L;

        var request = InboundOrderServiceMocks.makeFakeProductRequestDTO(Category.FRESCO);

        var fakeProduct = InboundOrderServiceMocks.makeFakeProduct();
        fakeProduct.setName("Salsicha");

        var fakeBatch = InboundOrderServiceMocks.makeFakeBatch();
        fakeBatch.setProducts(Arrays.asList(fakeProduct));

        var fakeInboundOrder = InboundOrderServiceMocks.makeFakeInboundOrder();
        fakeInboundOrder.setBatch(fakeBatch);

        when(inboundOrderRepository.findById(inboundOrderIdExistent)).thenReturn(Optional.of(fakeInboundOrder));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(request, inboundOrderIdExistent, productIdNotExistent));

        assertEquals("Product not found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfSectionCapacityExceededWhenTryUpdate() {
        var inboundOrderIdExistent = 1L;
        var productIdExistent = 1L;

        var request = InboundOrderServiceMocks.makeFakeProductRequestDTO(Category.FRESCO);

        var fakeProduct = InboundOrderServiceMocks.makeFakeProduct();
        fakeProduct.setName("Salsicha");
        fakeProduct.setQuantity(1);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();

        var fakeBatch = InboundOrderServiceMocks.makeFakeBatch();
        fakeBatch.setProducts(Arrays.asList(fakeProduct));
        fakeBatch.setWarehouse(fakeWarehouse);
        fakeBatch.setSection(fakeSection);

        var fakeInboundOrder = InboundOrderServiceMocks.makeFakeInboundOrder();
        fakeInboundOrder.setBatch(fakeBatch);

        var warehouseId = fakeInboundOrder.getBatch().getWarehouse().getId();
        var sectionId = fakeInboundOrder.getBatch().getSection().getId();

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSize(100);
        fakeWarehouseSection.setTotalProducts(99);

        when(inboundOrderRepository.findById(inboundOrderIdExistent)).thenReturn(Optional.of(fakeInboundOrder));
        when(inboundOrderRepository.findWarehouseSectionByWarehouseId(warehouseId, sectionId)).thenReturn(fakeWarehouseSection);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.update(request, inboundOrderIdExistent, productIdExistent));

        assertEquals("Section capacity exceeded", exception.getMessage());
    }

    @Test
    public void shouldBeUpdatedSuccessfullyInboundOrderAndIncreaseTotalProducts() {
        var inboundOrderIdExistent = 1L;
        var productIdExistent = 1L;

        var request = InboundOrderServiceMocks.makeFakeProductRequestDTO(Category.FRESCO);

        var fakeProduct = InboundOrderServiceMocks.makeFakeProduct();
        fakeProduct.setName("Salsicha");
        fakeProduct.setQuantity(1);

        var fakeWarehouse = InboundOrderServiceMocks.makeFakeWarehouse();

        var fakeSection = InboundOrderServiceMocks.makeFakeSection();

        var fakeBatch = InboundOrderServiceMocks.makeFakeBatch();
        fakeBatch.setProducts(Arrays.asList(fakeProduct));
        fakeBatch.setWarehouse(fakeWarehouse);
        fakeBatch.setSection(fakeSection);

        var fakeInboundOrder = InboundOrderServiceMocks.makeFakeInboundOrder();
        fakeInboundOrder.setBatch(fakeBatch);

        var warehouseId = fakeInboundOrder.getBatch().getWarehouse().getId();
        var sectionId = fakeInboundOrder.getBatch().getSection().getId();

        var fakeWarehouseSection = InboundOrderServiceMocks.makeFakeWarehouseSection();
        fakeWarehouseSection.setSize(100);
        fakeWarehouseSection.setTotalProducts(80);

        when(inboundOrderRepository.findById(inboundOrderIdExistent)).thenReturn(Optional.of(fakeInboundOrder));
        when(inboundOrderRepository.findWarehouseSectionByWarehouseId(warehouseId, sectionId)).thenReturn(fakeWarehouseSection);
        when(inboundOrderRepository.save(any(InboundOrder.class))).thenReturn(fakeInboundOrder);

        var response = service.update(request, inboundOrderIdExistent, productIdExistent);

        assertEquals(89, fakeWarehouseSection.getTotalProducts());

        assertEquals(request.getName(), response.getProducts().get(0).getName());
        assertEquals(request.getCurrentTemperature(), response.getProducts().get(0).getCurrentTemperature());
        assertEquals(request.getDueDate(), response.getProducts().get(0).getDueDate());
    }
}
