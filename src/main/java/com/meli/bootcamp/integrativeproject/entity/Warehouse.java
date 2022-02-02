package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<WarehouseSection> warehouseSections;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnore
    private List<WarehouseSellers> warehouseSellers;

    @OneToOne(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private Agent agent;
}
