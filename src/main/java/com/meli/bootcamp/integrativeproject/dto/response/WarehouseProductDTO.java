package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WarehouseProductDTO {
    private String warehouseCode;
    private Integer totalQuantity;
}
