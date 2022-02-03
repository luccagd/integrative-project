package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.WarehouseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseSectionRepository extends JpaRepository<WarehouseSection, Long> {

    @Query(value = "SELECT WS.* FROM PRODUCTS P JOIN BATCHES B ON (P.BATCH_ID = B.ID) " +
                   "JOIN SECTIONS S ON (S.ID = B.SECTION_ID) JOIN WAREHOUSES W ON (W.ID = B.WAREHOUSE_ID) " +
                   "JOIN WAREHOUSES_SECTIONS WS ON (WS.SECTION_ID = S.ID AND WS.WAREHOUSE_ID = W.ID) " +
                   "WHERE p.ID = :idProduct", nativeQuery = true)
    WarehouseSection findWarehouseSectionByProductId(@Param("idProduct") Long productId);
}
