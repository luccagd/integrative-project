package com.meli.bootcamp.integrativeproject.entity;

import javax.persistence.*;

@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToOne(mappedBy = "agent", cascade = CascadeType.ALL)
    private Warehouse warehouse;
}
