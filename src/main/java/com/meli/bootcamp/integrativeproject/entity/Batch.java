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
    @JsonIgnoreProperties (value = "batch")
    private List<Product> products;

    @OneToOne(mappedBy = "batch")
    @JsonIgnore
    private InboundOrder inboundOrder;    
}
