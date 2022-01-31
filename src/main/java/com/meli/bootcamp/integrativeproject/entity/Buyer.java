package com.meli.bootcamp.integrativeproject.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buyers")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = true)
    private Cart cart;
}
