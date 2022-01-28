package com.meli.bootcamp.integrativeproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.meli.bootcamp.integrativeproject.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
}
