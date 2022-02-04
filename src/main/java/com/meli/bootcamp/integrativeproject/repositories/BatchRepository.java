package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.entity.Batch;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    @Query(value = "SELECT b.BATCH_NUMBER, p.ID as PRODUCT_ID, p.CATEGORY as PRODUCT_CATEGORY, p.DUE_DATE, p.QUANTITY, DATEDIFF(day, CURRENT_DATE, p.DUE_DATE) AS DATEDIFF  " +
            "FROM BATCHES B " +
            "JOIN PRODUCTS P  ON (P.BATCH_ID = B.ID) " +
            "JOIN SECTIONS S ON (b.SECTION_ID = S.id) " +
            "where p.CATEGORY = :sectionName  " +
            "GROUP BY p.DUE_DATE, b.BATCH_NUMBER, p.ID " +
            "ORDER BY p.DUE_DATE, b.BATCH_NUMBER", nativeQuery = true)


    List<BatchResponse> findAllBySectionNameAndDueDate(@Param("sectionName") String sectionName);

    interface BatchResponse{
        Integer getBatch_number();
        Long getProduct_id();
        String getProduct_category();
        LocalDate getDue_date();
        Integer getQuantity();
        Integer getDatediff();
    }
}
