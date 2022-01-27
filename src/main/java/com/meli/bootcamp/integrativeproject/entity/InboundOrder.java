package com.meli.bootcamp.integrativeproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_order")
    private LocalDateTime dateOrder;
    @ManyToOne
    private Agent agent;
    @OneToOne(mappedBy = "orderBatch", cascade = CascadeType.ALL)
    private Batch batch;
}
