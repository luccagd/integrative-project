package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "batchs")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_number", nullable = false)
    private Integer batchNumber;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Section section;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Seller seller;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(mappedBy = "batch")
    @JsonIgnore
    private InboundOrder inboundOrder;    
}
