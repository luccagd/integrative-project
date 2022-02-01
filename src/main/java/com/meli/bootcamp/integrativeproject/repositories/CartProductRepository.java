package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct,Long>{


    @Query(value = "select p.price, cp.quantity, p.name, cp.cart_id, cp.quantity as quantity_itens, c.created_at"
            + "  from carts_products cp inner join products p on cp.id = p.id inner join carts c on cp.cart_id = c.id  where cart_id = :id ", nativeQuery = true)
    List<CartProductDto>findByCart_Id(@Param("id")Long id);


public  interface CartProductDto{

    Long  getcart_id();
    String getName();
    Long getPrice();
    Integer getQuantity();
    Date getCreated_at();

                              }
}


