package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WarehouseProductResponse {
    private String productId;
    private List<WarehouseProductDTO> warehouses;
}
