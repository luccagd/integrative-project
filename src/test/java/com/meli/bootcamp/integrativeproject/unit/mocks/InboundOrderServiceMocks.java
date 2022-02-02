package com.meli.bootcamp.integrativeproject.unit.mocks;

import com.meli.bootcamp.integrativeproject.dto.request.BatchRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.InboundOrderRequestDTO;
import com.meli.bootcamp.integrativeproject.dto.request.ProductRequestDTO;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class InboundOrderServiceMocks {
    public static ProductRequestDTO makeFakeProductRequestDTO(Category productCategory) {
        return new ProductRequestDTO("Any Product", 1.0, 1.0, 10, LocalDate.of(2022, 10, 10), productCategory, 10.00);
    }

    public static BatchRequestDTO makeFakeBatchRequestDTO(Category productCategory) {
        return new BatchRequestDTO(Arrays.asList(makeFakeProductRequestDTO(productCategory)));
    }

    public static InboundOrderRequestDTO makeFakeInboundOrderRequestDTO() {
        InboundOrderRequestDTO inboundOrderRequestDTO = new InboundOrderRequestDTO();

        Category defaultCategory = Category.CONGELADO;

        inboundOrderRequestDTO.setBatchStock(makeFakeBatchRequestDTO(defaultCategory));

        return inboundOrderRequestDTO;
    }

    public static Seller makeFakeSeller() {
        return new Seller(1L, "João", null, null);
    }

    public static Warehouse makeFakeWarehouse() {
        return new Warehouse(1L, "Armazém de São Paulo", null, null, null);
    }

    public static Agent makeFakeAgent() {
        return new Agent(1L, "Maria", null);
    }

    public static WarehouseSection makeFakeWarehouseSection() {
        return new WarehouseSection(1L, null, null, 100, 0);
    }

    public static Section makeFakeSection() {
        return new Section(1L, Category.CONGELADO, null);
    }

    public static InboundOrder makeFakeInboundOrder() {
        return new InboundOrder(1L, LocalDateTime.now(), null, null, null);
    }

    public static Batch makeFakeBatch() {
        return new Batch(1L, 12345678, null, null, null, null);
    }

    public static Product makeFakeProduct() {
        return new Product(1L, "Produto A", 1.0, 1.0, 10, LocalDate.of(2022, 10, 10), 10.0, null, Category.CONGELADO, null);
    }
}
