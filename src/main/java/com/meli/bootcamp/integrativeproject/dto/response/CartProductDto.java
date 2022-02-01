package com.meli.bootcamp.integrativeproject.dto.response;


import java.util.Date;

public interface CartProductDto {

    Long  getcart_id();
    String getName();
    Long getPrice();
    Integer getQuantity();
    Date  getCreated_at();

}
