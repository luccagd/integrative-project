package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.BatchResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.dto.response.ProductResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.*;
import com.meli.bootcamp.integrativeproject.utils.GenerateRandomNumber;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InboundOrderService {
    private InboundOrderRepository inboundOrderRepository;

    private WarehouseRepository warehouseRepository;

    private SectionRepository sectionRepository;

    private AgentRepository agentRepository;

    private BatchRepository batchRepository;

    private ProductRepository productRepository;

    private SellerRepository sellerRepository;

    public InboundOrderService(InboundOrderRepository inboundOrderRepository, WarehouseRepository warehouseRepository,
            SectionRepository sectionRepository, AgentRepository agentRepository, BatchRepository batchRepository,
            ProductRepository productRepository, SellerRepository sellerRepository) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.sectionRepository = sectionRepository;
        this.agentRepository = agentRepository;
        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    public InboundOrderResponseDTO save(InboundOrderRequestDTO inboundOrderRequestDTO, Long agentId)
            throws NotFoundException, BusinessException {
        // Verifica existencia do armazem
        Warehouse warehouse = warehouseRepository.findById(inboundOrderRequestDTO.getWarehouseId()).orElse(null);
        if (warehouse == null) {
            throw new NotFoundException("WAREHOUSE NOT FOUND");
        }

        // Verifica representante
        Agent agent = agentRepository.findById(agentId).orElse(null);
        if (agent == null) {
            throw new NotFoundException("AGENT NOT FOUND");
        }

        if (!warehouse.getAgent().getId().equals(agent.getId())) {
            throw new BusinessException("AGENT ID IS NOT EQUAL");
        }

        // Verificações do setor
        Section section = sectionRepository.findById(inboundOrderRequestDTO.getSectionId()).orElse(null);
        if (section == null) {
            throw new NotFoundException("SECTION NOT FOUND");
        }

        Integer batchSize = inboundOrderRequestDTO.getBatchStock().calculateBatchSize();
        if (batchSize > section.calculateRemainingSize()) {
            throw new BusinessException("BATCH IS BIGGER THAN SECTION SIZE");
        }

        Seller seller = sellerRepository.findById(inboundOrderRequestDTO.getSellerId()).orElse(null);
        if (seller == null) {
            throw new NotFoundException("SELLER NOT FOUND");
        }

        section.increaseTotalProducts(batchSize);

        Batch batch = Batch.builder()
                .batchNumber(GenerateRandomNumber.generateRandomBatchNumber())
                .section(section)
                .seller(seller)
                .build();

        batchRepository.save(batch);

        List<Product> products = new ArrayList<>();
        for (ProductRequestDTO productRequestDTO : inboundOrderRequestDTO.getBatchStock().getProducts()) {
            if (!section.getCategory().equals(productRequestDTO.getCategory())) {
                throw new BusinessException("PRODUCT CATEGORY IS NOT EQUAL TO SECTION CATEGORY");
            }

            Product product = Product.builder()
                    .name(productRequestDTO.getName())
                    .currentTemperature(productRequestDTO.getCurrentTemperature())
                    .minimalTemperature(productRequestDTO.getMinimalTemperature())
                    .quantity(productRequestDTO.getQuantity())
                    .dueDate(productRequestDTO.getDueDate())
                    .category(productRequestDTO.getCategory())
                    .batch(batch)
                    .price(productRequestDTO.getPrice())
                    .build();

            products.add(product);

            productRepository.save(product);
        }

        batch.setProducts(products);

        InboundOrder inboundOrder = InboundOrder.builder()
                .agent(agent)
                .batch(batch)
                .build();

        inboundOrder = inboundOrderRepository.save(inboundOrder);

        InboundOrderResponseDTO inboundOrderResponseDTO = InboundOrderResponseDTO.builder()
                .orderDate(inboundOrder.getDateOrder())
                .batchStock(inboundOrder.getBatch())
                .build();

        return inboundOrderResponseDTO;
    }

    public BatchResponseDTO update(ProductRequestDTO productRequestDTO, Long inboundOrderId,
                                   Long productId) {
        InboundOrder inboundOrder = inboundOrderRepository.findById(inboundOrderId).orElse(null);
        if (inboundOrder == null) {
            throw new NotFoundException("INBOUND_ORDER NOT FOUND");
        }

        Product findProduct = inboundOrder.getBatch().getProducts().stream().filter(product -> {
            return product.getId().equals(productId);
        }).findAny().orElse(null);
        if (findProduct == null) {
            throw new NotFoundException("PRODUCT NOT FOUND");
        }

        int diffQuantity = productRequestDTO.getQuantity() - findProduct.getQuantity();
        if (diffQuantity > 0) {
            if (diffQuantity > inboundOrder.getBatch().getSection().calculateRemainingSize()) {
                throw new BusinessException("SECTION CAPACITY EXCEEDED");
            }

            inboundOrder.getBatch().getSection().increaseTotalProducts(diffQuantity);
            findProduct.setQuantity(productRequestDTO.getQuantity());
        }

        if (diffQuantity < 0) {
            inboundOrder.getBatch().getSection().decreaseTotalProducts(diffQuantity);
            findProduct.setQuantity(productRequestDTO.getQuantity());
        }

        findProduct.setName(productRequestDTO.getName());
        findProduct.setCurrentTemperature(productRequestDTO.getCurrentTemperature());
        findProduct.setMinimalTemperature(productRequestDTO.getMinimalTemperature());
        findProduct.setDueDate(productRequestDTO.getDueDate());

        inboundOrder = inboundOrderRepository.save(inboundOrder);

        BatchResponseDTO batchResponseDTO = BatchResponseDTO.builder()
                .batchNumber(inboundOrder.getBatch().getBatchNumber())
                .products(inboundOrder.getBatch().getProducts())
                .build();

        return batchResponseDTO;
    }
}
