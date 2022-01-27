package com.meli.bootcamp.integrativeproject.entity;

import com.meli.bootcamp.integrativeproject.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer size;

    private Integer totalProducts;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private Warehouse warehouse;

    public Integer calculateRemainingSize() {
        return size - totalProducts;
    }

    public void setTotalProducts(Integer batchSize) {
        this.totalProducts += batchSize;
    }
}
