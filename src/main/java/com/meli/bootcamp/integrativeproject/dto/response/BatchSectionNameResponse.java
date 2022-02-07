package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BatchSectionNameResponse {
    private List<BatchStock> batchStock;
}
