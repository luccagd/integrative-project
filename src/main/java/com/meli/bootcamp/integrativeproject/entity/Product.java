package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meli.bootcamp.integrativeproject.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double currentTemperature;

    private Double minimalTemperature;

    private Integer quantity;

    private LocalDate dueDate;

    @ManyToOne
    @JsonIgnore
    private Batch batch;

    @Enumerated(EnumType.STRING)
    private Category category;
}
