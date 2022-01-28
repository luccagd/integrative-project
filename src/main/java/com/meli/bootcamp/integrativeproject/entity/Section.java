package com.meli.bootcamp.integrativeproject.entity;

import com.meli.bootcamp.integrativeproject.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "total_products", nullable = false)
    private Integer totalProducts;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", nullable = false)
    private Warehouse warehouse;

    public Integer calculateRemainingSize() {
        return size - totalProducts;
    }

    public void increaseTotalProducts(Integer batchSize) {
        this.totalProducts += batchSize;
    }

    public void decreaseTotalProducts(Integer batchSize) {
        this.totalProducts -= batchSize;
    }
}
