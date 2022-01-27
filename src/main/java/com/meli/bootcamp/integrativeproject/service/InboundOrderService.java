package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.response.InboundOrderResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderService {

    private InboundOrderRepository inboundOrderRepository;

    private WarehouseRepository warehouseRepository;

    private SectionRepository sectionRepository;

    private AgentRepository agentRepository;

    private BatchRepository batchRepository;

    public InboundOrderService(InboundOrderRepository inboundOrderRepository, WarehouseRepository warehouseRepository, SectionRepository sectionRepository, AgentRepository agentRepository, BatchRepository batchRepository) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.sectionRepository = sectionRepository;
        this.agentRepository = agentRepository;
        this.batchRepository = batchRepository;
    }

    public List<InboundOrder> getAll() {
        return inboundOrderRepository.findAll();
    }

    public InboundOrderResponseDTO save(InboundOrderRequestDTO inboundOrderRequestDTO, Long agentId) throws NotFoundException, BusinessException {
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

            List<Product> products =
                    inboundOrderRequestDTO.getBatchStock().getProducts().stream().map(productRequestDTO -> {
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
                                .build();

                        return product;
                    }).collect(Collectors.toList());

            Integer batchSize = inboundOrderRequestDTO.getBatchStock().calculateBatchSize();
            if (batchSize > section.calculateRemainingSize()) {
                throw new BusinessException("BATCH IS BIGGER THAN SECTION SIZE");
            }

            section.setTotalProducts(batchSize);


            Batch batch = Batch.builder()
                    .batchNumber(inboundOrderRequestDTO.getBatchStock().getBatchNumber())
                    .section(section)
                    .products(products)
                    .build();
            batch = batchRepository.save(batch);

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
}
