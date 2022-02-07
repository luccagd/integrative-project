package com.meli.bootcamp.integrativeproject.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "warehouses_sections")
public class WarehouseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id" , referencedColumnName = "id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "section_id" , referencedColumnName = "id")
    private Section section;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "total_products", nullable = false)
    private Integer totalProducts;

    public Integer calculateRemainingSize() {
        return size - totalProducts;
    }

    public void increaseTotalProducts(Integer batchSize) {
        this.totalProducts += Math.abs(batchSize);
    }

    public void decreaseTotalProducts(Integer batchSize) {
        this.totalProducts -= Math.abs(batchSize);
    }

}
