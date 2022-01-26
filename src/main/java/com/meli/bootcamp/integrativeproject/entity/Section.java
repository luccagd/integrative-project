package com.meli.bootcamp.integrativeproject.entity;

import com.meli.bootcamp.integrativeproject.enums.Category;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long size;

    private Long totalProducts;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private Warehouse warehouse;
}
