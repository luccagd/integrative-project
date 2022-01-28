package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.*;
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

    public InboundOrderService(InboundOrderRepository inboundOrderRepository, WarehouseRepository warehouseRepository,
            SectionRepository sectionRepository, AgentRepository agentRepository, BatchRepository batchRepository,
            ProductRepository productRepository) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.sectionRepository = sectionRepository;
        this.agentRepository = agentRepository;
        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
    }

    public InboundOrderResponseDTO save(InboundOrderRequestDTO inboundOrderRequestDTO, Long agentId) {
        // Verifica existencia do armazem
        Warehouse warehouse = warehouseRepository.findById(inboundOrderRequestDTO.getWarehouseId()).orElse(null);
        if (warehouse == null) {
            throw new RuntimeException("");
        }

        // Verifica representante
        Agent agent = agentRepository.findById(agentId).orElse(null);
        if (agent == null) {
            throw new RuntimeException("");
        }

        if (!warehouse.getAgent().getId().equals(agent.getId())) {
            throw new RuntimeException("");
        }

        // Verificações do setor
        Section section = sectionRepository.findById(inboundOrderRequestDTO.getSectionId()).orElse(null);
        if (section == null) {
            throw new RuntimeException("");
        }

        Integer batchSize = inboundOrderRequestDTO.getBatchStock().calculateBatchSize();
        if (batchSize > section.calculateRemainingSize()) {
            throw new RuntimeException("");
        }

        section.increaseTotalProducts(batchSize);

        Batch batch = Batch.builder()
                .batchNumber(GenerateRandomNumber.generateRandomBatchNumber())
                .section(section)
                .build();

        batchRepository.save(batch);

        List<Product> products = new ArrayList<>();
        for (ProductRequestDTO productRequestDTO : inboundOrderRequestDTO.getBatchStock().getProducts()) {
            if (!section.getCategory().equals(productRequestDTO.getCategory())) {
                throw new RuntimeException("");
            }

            Product product = Product.builder()
                    .name(productRequestDTO.getName())
                    .currentTemperature(productRequestDTO.getCurrentTemperature())
                    .minimalTemperature(productRequestDTO.getMinimalTemperature())
                    .quantity(productRequestDTO.getQuantity())
                    .dueDate(productRequestDTO.getDueDate())
                    .category(productRequestDTO.getCategory())
                    .batch(batch)
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

    public InboundOrderResponseDTO update(ProductRequestDTO productRequestDTO, Long inboundOrderId,
            Long productId) {
        InboundOrder inboundOrder = inboundOrderRepository.findById(inboundOrderId).orElse(null);
        if (inboundOrder == null) {
            throw new RuntimeException("");
        }

        Product findProduct = inboundOrder.getBatch().getProducts().stream().filter(product -> {
            return product.getId().equals(productId);
        }).findAny().orElse(null);
        if (findProduct == null) {
            throw new RuntimeException("");
        }

        int diffQuantity = productRequestDTO.getQuantity() - findProduct.getQuantity();
        if (diffQuantity > 0) {
            if (diffQuantity > inboundOrder.getBatch().getSection().calculateRemainingSize()) {
                throw new RuntimeException("");
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

        InboundOrderResponseDTO inboundOrderResponseDTO = InboundOrderResponseDTO.builder()
                .orderDate(inboundOrder.getDateOrder())
                .batchStock(inboundOrder.getBatch())
                .build();

        return inboundOrderResponseDTO;
    }
}