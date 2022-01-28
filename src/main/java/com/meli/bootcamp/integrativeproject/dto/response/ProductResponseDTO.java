package com.meli.bootcamp.integrativeproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {

    private String name;

    private Double currentTemperature;

    private Double minimalTemperature;

    private Long quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dueDate;

    private Category category;

    public static ProductResponseDTO toDTO(Product product) {
        return ProductResponseDTO.builder()
                .name(product.getName())
                .currentTemperature(product.getCurrentTemperature())
                .minimalTemperature(product.getMinimalTemperature())
                .quantity(product.getQuantity().longValue())
                .dueDate(product.getDueDate())
                .category(product.getCategory())
                .build();
    }

    public static List<ProductResponseDTO> entityListToDtoList(List<Product> products) {
        return products.stream().map(product -> ProductResponseDTO.toDTO(product)).collect(Collectors.toList());
    }
}
