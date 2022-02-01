package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "seller")
    private List<Batch> batches;

    @OneToMany(mappedBy = "seller")
    @JsonIgnoreProperties(value = "seller")
    private List<WarehouseSellers> warehouseSellers;
}
