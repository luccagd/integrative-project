package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private List<WarehouseSection> warehouseSections;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private List<WarehouseSellers> warehouseSellers;

    @OneToOne(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private Agent agent;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private List<Batch> batches;
}
