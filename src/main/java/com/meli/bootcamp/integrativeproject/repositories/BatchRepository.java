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

    @Query(value = "SELECT B.BATCH_NUMBER, P.ID as PRODUCT_ID, P.CATEGORY as PRODUCT_CATEGORY, P.DUE_DATE, P.QUANTITY," +
            " DATEDIFF(day, CURRENT_DATE, p.DUE_DATE) AS DATEDIFF  " +
            "FROM BATCHES B JOIN PRODUCTS P  ON (P.BATCH_ID = B.ID) JOIN SECTIONS S ON (B.SECTION_ID = S.id) " +
            "WHERE P.CATEGORY = :sectionName GROUP BY P.DUE_DATE, B.BATCH_NUMBER, B.ID ORDER BY B.BATCH_NUMBER"
            , nativeQuery = true)
    List<BatchResponse> findAllBySectionNameAndDueDate(@Param("sectionName") String sectionName);

    interface BatchResponse {
        Integer getBatch_number();

        Long getProduct_id();

        String getProduct_category();

        LocalDate getDue_date();

        Integer getQuantity();

        Integer getDatediff();
    }
}
