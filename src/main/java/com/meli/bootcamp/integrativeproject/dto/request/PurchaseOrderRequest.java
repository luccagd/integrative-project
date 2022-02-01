package com.meli.bootcamp.integrativeproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PurchaseOrderRequest {
    private Long buyerId;
    private List<PurchaseOrderProductRequest> products;
}
