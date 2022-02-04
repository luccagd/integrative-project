package com.meli.bootcamp.integrativeproject.dto.response;

import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartProductResponseDTO {

    private ProductResponseDTO product;

    public static CartProductResponseDTO toDTO(CartProduct cartProduct) {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.toDTO(cartProduct.getProduct());
        productResponseDTO.setQuantity(cartProduct.getQuantity().longValue());

        return CartProductResponseDTO.builder()
                .product(productResponseDTO)
                .build();
    }

    public static List<CartProductResponseDTO> entityListToResponseDtoList(List<CartProduct> cartProducts) {
        return cartProducts.stream().map(cartProduct -> toDTO(cartProduct)).collect(Collectors.toList());
    }
}
