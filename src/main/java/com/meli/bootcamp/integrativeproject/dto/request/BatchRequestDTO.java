package com.meli.bootcamp.integrativeproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchRequestDTO {
    private Integer batchNumber;

    private List<ProductRequestDTO> products;

    public Integer calculateBatchSize() {
        return products.stream().mapToInt(product -> product.getQuantity()).sum();
    }
}
