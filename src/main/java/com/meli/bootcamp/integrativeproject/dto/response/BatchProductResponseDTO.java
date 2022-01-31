package com.meli.bootcamp.integrativeproject.dto.response;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchProductResponseDTO {

    private Long batchId;

    private Integer batchNumber;

    private Long productId;

    private String productName;

    private Double currentTemperature;

    private Double minimalTemperature;

    private Integer quantity;

    private LocalDate dueDate;

    private Category category;

    public static BatchProductResponseDTO toDTO(Product product) {
        return BatchProductResponseDTO.builder()
                .batchId(product.getBatch().getId())
                .batchNumber(product.getBatch().getBatchNumber())
                .productId(product.getId())
                .productName(product.getName())
                .currentTemperature(product.getCurrentTemperature())
                .minimalTemperature(product.getMinimalTemperature())
                .quantity(product.getQuantity())
                .dueDate(product.getDueDate())
                .category(product.getCategory())
                .build();
    }

    public static List<BatchProductResponseDTO> entityListToDtoList(List<Product> products) {
        List<BatchProductResponseDTO> batchProductResponseDTOs = new ArrayList<>();
        for (Product product : products) {
            if (product.getDueDate().compareTo(LocalDate.now().plusWeeks(3)) > 0) {
                batchProductResponseDTOs.add(toDTO(product));
            }
        }

        return batchProductResponseDTOs;
    }
}
