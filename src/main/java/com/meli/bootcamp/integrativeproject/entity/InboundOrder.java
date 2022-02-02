package com.meli.bootcamp.integrativeproject.entity;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inbound_orders")
public class InboundOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date_order", nullable = false)
    private LocalDateTime dateOrder;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false)
    private Agent agent;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "batch_id", referencedColumnName = "id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    private Seller seller;
}