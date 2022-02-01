package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.BatchResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.InboundOrderRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import com.meli.bootcamp.integrativeproject.utils.GenerateRandomNumber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderService {
    private InboundOrderRepository inboundOrderRepository;

    private WarehouseRepository warehouseRepository;

    private SellerRepository sellerRepository;

    public InboundOrderService(InboundOrderRepository inboundOrderRepository, WarehouseRepository warehouseRepository, SellerRepository sellerRepository) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.sellerRepository = sellerRepository;
    }

    public InboundOrderResponseDTO save(InboundOrderRequestDTO inboundOrderRequestDTO, Long agentId) {
        doValidations(inboundOrderRequestDTO, agentId);

        Warehouse warehouse = warehouseRepository.findById(inboundOrderRequestDTO.getWarehouseId()).get();
        Seller seller = sellerRepository.findById(inboundOrderRequestDTO.getSellerId()).get();
        WarehouseSection warehouseSection = getSectionFromWarehouse(warehouse, inboundOrderRequestDTO.getSectionId());

        Batch batch = Batch.builder()
                .batchNumber(GenerateRandomNumber.generateRandomBatchNumber())
                .section(warehouseSection.getSection())
                .seller(seller)
                .warehouse(warehouse)
                .build();

        List<Product> products = inboundOrderRequestDTO.getBatchStock().getProducts().stream().map(productRequestDTO -> {
                    Product product = ProductRequestDTO.toEntity(productRequestDTO);
                    product.setBatch(batch);

                    return product;
        }).collect(Collectors.toList());

        batch.setProducts(products);

        InboundOrder inboundOrder = InboundOrder.builder()
                .agent(warehouse.getAgent())
                .batch(batch)
                .seller(seller)
                .build();

        Integer productsQuantityInTheBatch = inboundOrderRequestDTO.getBatchStock().calculateProductsQuantityInTheBatch();
        warehouseSection.increaseTotalProducts(productsQuantityInTheBatch);

        inboundOrder = inboundOrderRepository.save(inboundOrder);

        return InboundOrderResponseDTO.builder()
                .orderDate(inboundOrder.getDateOrder())
                .batchStock(inboundOrder.getBatch())
                .build();
    }

    public void doValidations(InboundOrderRequestDTO inboundOrderRequestDTO, Long agentId) {
        validateIfSellerExists(inboundOrderRequestDTO.getSellerId());

        Warehouse warehouse = validateByIdIfWarehouseExists(inboundOrderRequestDTO.getWarehouseId());

        Long agentIdFromWarehouse = warehouse.getAgent().getId();
        validateIfAgentBelongToWarehouse(agentIdFromWarehouse, agentId);

        WarehouseSection warehouseSection = validateIfWarehouseHaveTheGivenSection(warehouse.getWarehouseSections(), inboundOrderRequestDTO.getSectionId());

        Category categoryFromWarehouseSection = warehouseSection.getSection().getCategory();
        List<ProductRequestDTO> productsDTOFromRequest = inboundOrderRequestDTO.getBatchStock().getProducts();
        validateIfProductCategoryIsEqualSectionCategory(categoryFromWarehouseSection, productsDTOFromRequest);

        validateIfWarehouseSectionHaveEnoughSpace(warehouseSection, inboundOrderRequestDTO.getBatchStock().calculateProductsQuantityInTheBatch());
    }

    private void validateIfSellerExists(Long id) {
        sellerRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Seller not found for the given id"));
    }

    private Warehouse validateByIdIfWarehouseExists(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Warehouse not found for the given id"));
    }

    private void validateIfAgentBelongToWarehouse(Long agentIdFromWarehouse, Long agentIdFromRequest) {
        if (!agentIdFromWarehouse.equals(agentIdFromWarehouse)) {
            throw new BusinessException("Agent does not belong to the given warehouse");
        }
    }

    private WarehouseSection validateIfWarehouseHaveTheGivenSection(List<WarehouseSection> warehouseSections, Long sectionIdFromRequest) {
        return warehouseSections.stream().filter(warehouseSection ->
                        warehouseSection.getSection().getId().equals(sectionIdFromRequest)
                ).findAny().orElseThrow(() -> new BusinessException("Warehouse dont have the given section"));
    }

    private void validateIfProductCategoryIsEqualSectionCategory(Category sectionCategory, List<ProductRequestDTO> productDTOs) {
        productDTOs.stream().forEach(productRequestDTO -> {
            if (!sectionCategory.equals(productRequestDTO.getCategory())) {
                throw new BusinessException("Product category is not equal to section category");
            }
        });
    }

    private void validateIfWarehouseSectionHaveEnoughSpace(WarehouseSection warehouseSection, Integer batchSize) {
        if (batchSize > warehouseSection.calculateRemainingSize()) {
            throw new BusinessException("Batch is bigger than section size");
        }
    }

    private WarehouseSection getSectionFromWarehouse(Warehouse warehouse, Long sectionIdFromRequest) {
        return warehouse.getWarehouseSections().stream().filter(warehouseSection ->
                warehouseSection.getSection().getId().equals(sectionIdFromRequest)
        ).findAny().get();
    }

    public BatchResponseDTO update(ProductRequestDTO productRequestDTO, Long inboundOrderId, Long productId) {
        return null;
    }
}
