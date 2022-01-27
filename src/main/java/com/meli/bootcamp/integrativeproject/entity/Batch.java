package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer batchNumber;

    @ManyToOne
    @JsonIgnore
    private Section section;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(mappedBy = "batch")
    @JsonIgnore
    private InboundOrder inboundOrder;
}
