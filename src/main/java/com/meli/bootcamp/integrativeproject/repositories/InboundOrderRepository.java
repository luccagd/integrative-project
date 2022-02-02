package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.InboundOrder;

import com.meli.bootcamp.integrativeproject.entity.Section;
import com.meli.bootcamp.integrativeproject.entity.WarehouseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {

    @Query(value = "SELECT ws FROM WarehouseSection ws WHERE ws.warehouse.id = :warehouseId AND ws.section.id = :sectionId")
    WarehouseSection findWarehouseSectionByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("sectionId") Long sectionId);
}
