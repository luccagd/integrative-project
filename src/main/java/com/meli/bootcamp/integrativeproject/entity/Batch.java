package com.meli.bootcamp.integrativeproject.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantityOfProducts;

    @ManyToOne
    private Section section;

    @ManyToOne
    private Seller seller;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<Product> product;
}
