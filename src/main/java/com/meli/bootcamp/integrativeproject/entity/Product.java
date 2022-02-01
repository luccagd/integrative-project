package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meli.bootcamp.integrativeproject.enums.Category;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "current_temperature", nullable = false)
    private Double currentTemperature;

    @Column(name = "minimal_temperature", nullable = false)
    private Double minimalTemperature;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "batch_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Batch batch;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    private List<CartProduct> cartProducts;

}
