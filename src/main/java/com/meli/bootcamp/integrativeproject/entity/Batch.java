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
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_number", nullable = false)
    private Integer batchNumber;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id", nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    private Seller seller;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @JsonIgnoreProperties (value = "batch")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", nullable = false)
    private Warehouse warehouse;
}