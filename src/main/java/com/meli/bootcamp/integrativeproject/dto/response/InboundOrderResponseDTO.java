package com.meli.bootcamp.integrativeproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.meli.bootcamp.integrativeproject.entity.Batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InboundOrderResponseDTO {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("orderDate")
    private LocalDateTime orderDate;

    private BatchResponseDTO batchStock;
}
