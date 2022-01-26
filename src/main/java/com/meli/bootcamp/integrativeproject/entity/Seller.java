package com.meli.bootcamp.integrativeproject.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Batch> batches;
}
