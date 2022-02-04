package com.meli.bootcamp.integrativeproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonKey;
import com.meli.bootcamp.integrativeproject.enums.CartStatus;
import jdk.jfr.Timestamp;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus status = CartStatus.ABERTO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<CartProduct> cartsProducts;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    @Column(name = "created_at")
    @JsonIgnore
    private LocalDate createdAt = LocalDate.now();
}
