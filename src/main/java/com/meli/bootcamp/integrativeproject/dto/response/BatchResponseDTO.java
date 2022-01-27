package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponseDTO {
    private Integer batchNumber;

    private List<ProductResponseDTO> products;
}
