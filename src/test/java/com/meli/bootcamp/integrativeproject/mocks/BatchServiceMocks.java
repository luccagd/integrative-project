package com.meli.bootcamp.integrativeproject.mocks;

import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BatchServiceMocks {
    public static List<BatchRepository.BatchResponse> makeFakeListOfBatchResponseWithCongeladoSection() {
        var fakeBatchResponseToCongeladoSection1 = BatchServiceMocks.makeFakeBatchResponse(12345, 1L, "CONGELADO", LocalDate.now().plusDays(9), 24, 9);
        var fakeBatchResponseToCongeladoSection2 = BatchServiceMocks.makeFakeBatchResponse(12345, 2L, "CONGELADO", LocalDate.now().plusDays(3), 12, 3);

        return Arrays.asList(fakeBatchResponseToCongeladoSection1, fakeBatchResponseToCongeladoSection2);
    }

    public static List<BatchRepository.BatchResponse> makeFakeListOfBatchResponseWithRefrigeradoSection() {
        var fakeBatchResponseToRefrigeradoSection1 = BatchServiceMocks.makeFakeBatchResponse(23456, 3L, "REFRIGERADO", LocalDate.now().plusDays(16), 36, 16);
        var fakeBatchResponseToRefrigeradoSection2 = BatchServiceMocks.makeFakeBatchResponse(23456, 4L, "REFRIGERADO", LocalDate.now().plusDays(15), 48, 15);

        return Arrays.asList(fakeBatchResponseToRefrigeradoSection1, fakeBatchResponseToRefrigeradoSection2);
    }

    public static List<BatchRepository.BatchResponse> makeFakeListOfBatchResponseWithFrescoSection() {
        var fakeBatchResponseToFrescoSection1 = BatchServiceMocks.makeFakeBatchResponse(34567, 5L, "FRESCO", LocalDate.now().plusDays(28), 60, 28);
        var fakeBatchResponseToFrescoSection2 = BatchServiceMocks.makeFakeBatchResponse(34567, 6L, "FRESCO", LocalDate.now().plusDays(21), 72, 21);

        return Arrays.asList(fakeBatchResponseToFrescoSection1, fakeBatchResponseToFrescoSection2);
    }

    public static BatchRepository.BatchResponse makeFakeBatchResponse(Integer batchNumber, Long productId, String productCategory, LocalDate dueDate, Integer quantity, Integer dateDiff) {
        BatchRepository.BatchResponse batchResponse = new BatchServiceMocks.BatchResponse(batchNumber, productId, productCategory, dueDate, quantity, dateDiff);

        return batchResponse;
    }

    public static class BatchResponse implements BatchRepository.BatchResponse {
        private Integer batchNumber;
        private Long productId;
        private String productCategory;
        private LocalDate dueDate;
        private Integer quantity;
        private Integer dateDiff;

        public BatchResponse(Integer batchNumber, Long productId, String productCategory, LocalDate dueDate, Integer quantity, Integer dateDiff) {
            this.batchNumber = batchNumber;
            this.productId = productId;
            this.productCategory = productCategory;
            this.dueDate = dueDate;
            this.quantity = quantity;
            this.dateDiff = dateDiff;
        }

        @Override
        public Integer getBatch_number() {
            return this.batchNumber;
        }

        @Override
        public Long getProduct_id() {
            return this.productId;
        }

        @Override
        public String getProduct_category() {
            return this.productCategory;
        }

        @Override
        public LocalDate getDue_date() {
            return this.dueDate;
        }

        @Override
        public Integer getQuantity() {
            return this.quantity;
        }

        @Override
        public Integer getDatediff() {
            return this.dateDiff;
        }
    }
}
