package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
