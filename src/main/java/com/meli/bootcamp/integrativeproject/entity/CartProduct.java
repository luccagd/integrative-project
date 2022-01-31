package com.meli.bootcamp.integrativeproject.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "carts_products")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "products_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;






}
