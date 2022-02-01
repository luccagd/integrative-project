package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "carts_products")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "products_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;
}
