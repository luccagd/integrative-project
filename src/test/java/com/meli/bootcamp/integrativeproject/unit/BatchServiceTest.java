package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.mocks.BatchServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;
import com.meli.bootcamp.integrativeproject.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    private BatchService service;

    private String validSectionName = "FF";
    private Integer validNumberOfDays = 1;
    private String validAscParameter = "true";

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        service = new BatchService(batchRepository);
    }

    @Test
    public void shouldBeThrowIfAscParameterNotBeTrueOrFalse() {
        String invalidAscParameter = "INVALID_ASC_PARAMETER";

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(validSectionName, validNumberOfDays, invalidAscParameter));

        assertEquals("asc can only be true or false", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfSectionNameNotFound() {
        String invalidSectionName = "INVALID_SECTION_NAME";

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(invalidSectionName, validNumberOfDays, validAscParameter));

        assertEquals("Section " + invalidSectionName + " not found", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfNumberOfDaysLessThan0() {
        Integer invalidNumberOfDays = -1;

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(validSectionName, invalidNumberOfDays, validAscParameter));

        assertEquals("number of days cannot be less than 0", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByFrescoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithFrescoSection());

        validSectionName = "FS";

        validAscParameter = "true";
        validNumberOfDays = 28;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 28;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 21;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(6, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(5, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(6, response3.getBatchStock().get(0).getProductId());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByCongeladoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithCongeladoSection());

        validSectionName = "FF";

        validAscParameter = "true";
        validNumberOfDays = 9;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 9;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 3;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(2, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(1, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(2, response3.getBatchStock().get(0).getProductId());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByRefrigeradoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithRefrigeradoSection());

        validSectionName = "RF";

        validAscParameter = "true";
        validNumberOfDays = 16;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 16;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 15;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(4, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(3, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(4, response3.getBatchStock().get(0).getProductId());
    }
}
