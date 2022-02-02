package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WarehouseProductDTO {
    private Long warehouseCode;
    private Integer totalQuantity;
}
