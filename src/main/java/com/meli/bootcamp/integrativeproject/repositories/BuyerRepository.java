package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
