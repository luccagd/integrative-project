package com.meli.bootcamp.integrativeproject.dto.response;

import com.meli.bootcamp.integrativeproject.entity.Batch;
import com.meli.bootcamp.integrativeproject.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchResponseDTO {
    private Integer batchNumber;

    private List<ProductResponseDTO> products;

    public static BatchResponseDTO toDTO(Batch batch) {
        return BatchResponseDTO.builder()
                .batchNumber(batch.getBatchNumber())
                .products(ProductResponseDTO.entityListToDtoList(batch.getProducts()))
                .build();
    }
}
