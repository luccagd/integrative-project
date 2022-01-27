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
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 100
    private Integer size;

    // 1
    private Integer totalProducts;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
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
