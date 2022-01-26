package com.meli.bootcamp.integrativeproject.entity;

import com.meli.bootcamp.integrativeproject.enums.Category;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double currentTemperature;

    private Double minimalTemperature;

    private Long quantity;

    private LocalDate dueDate;

    @ManyToOne
    private Batch batch;

    @Enumerated(EnumType.STRING)
    private Category category;
}
