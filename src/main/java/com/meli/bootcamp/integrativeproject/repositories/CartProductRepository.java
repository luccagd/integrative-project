package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct,Long> {



}
