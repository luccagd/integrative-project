package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.entity.Batch;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.ProductsServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService service;

    private List<Product> listOfFakeProducts;

    private Batch fakeBatch;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        listOfFakeProducts = ProductsServiceMocks.makeFakeListOfProducts(2);
        fakeBatch = ProductsServiceMocks.makeFakeBatch();

        service = new ProductService(productRepository);
    }

    @Test
    public void shouldBeThrowIfNoProductsWereFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findAll());

        assertEquals("No products were found", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProducts() {
        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(1).setName("Frango");

        when(productRepository.findAll()).thenReturn(listOfFakeProducts);

        var response = service.findAll();

        assertEquals("Salsicha", response.get(0).getName());
        assertEquals("Frango", response.get(1).getName());
    }

    @Test
    public void shouldBeThrowIfNoProductsFoundForTheGivenName() {
        String productNameNotExistent = "";
        String orderBy = "";

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findAllByNameAndDueDate(productNameNotExistent, orderBy));

        assertEquals("No products found for the given name", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfNoProductsFoundWithinTheDueDateForTheGivenName() {
        String productNameExistent = "Salsicha";
        String orderBy = "";

        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setDueDate(LocalDate.now().plusWeeks(2));

        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setDueDate(LocalDate.now().plusWeeks(2));

        when(productRepository.findAllByName(anyString())).thenReturn(listOfFakeProducts);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findAllByNameAndDueDate(productNameExistent, orderBy));

        assertEquals("No products found within the due date for the given name", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfInvalidValueForSortingIsProvided() {
        String productNameExistent = "Salsicha";
        String orderBy = "INVALID_SORT_VALUE";

        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setDueDate(LocalDate.now().plusWeeks(4));

        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setDueDate(LocalDate.now().plusWeeks(2));

        when(productRepository.findAllByName(anyString())).thenReturn(listOfFakeProducts);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.findAllByNameAndDueDate(productNameExistent, orderBy));

        assertEquals("Valid values for sorting are L, C or F", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsByNameAndDueDateOrderedByBatchNumber() {
        String productNameExistent = "Salsicha";
        String orderBy = "L";

        var fakeBatch1 = ProductsServiceMocks.makeFakeBatch();
        fakeBatch1.setBatchNumber(12345);
        var fakeBatch2 = ProductsServiceMocks.makeFakeBatch();
        fakeBatch2.setBatchNumber(23456);

        listOfFakeProducts.get(0).setName("Salsicha 1");
        listOfFakeProducts.get(0).setDueDate(LocalDate.now().plusWeeks(4));
        listOfFakeProducts.get(0).setBatch(fakeBatch2);

        listOfFakeProducts.get(1).setName("Salsicha 2");
        listOfFakeProducts.get(1).setDueDate(LocalDate.now().plusWeeks(4));
        listOfFakeProducts.get(1).setBatch(fakeBatch1);

        when(productRepository.findAllByName(anyString())).thenReturn(listOfFakeProducts);

        var response = service.findAllByNameAndDueDate(productNameExistent, orderBy);

        assertEquals("Salsicha 2", response.get(0).getName());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsByNameAndDueDateOrderedByQuantity() {
        String productNameExistent = "Salsicha";
        String orderBy = "C";

        listOfFakeProducts.get(0).setName("Salsicha 1");
        listOfFakeProducts.get(0).setDueDate(LocalDate.now().plusWeeks(4));
        listOfFakeProducts.get(0).setQuantity(10);


        listOfFakeProducts.get(1).setName("Salsicha 2");
        listOfFakeProducts.get(1).setDueDate(LocalDate.now().plusWeeks(4));
        listOfFakeProducts.get(1).setQuantity(5);

        when(productRepository.findAllByName(anyString())).thenReturn(listOfFakeProducts);

        var response = service.findAllByNameAndDueDate(productNameExistent, orderBy);

        assertEquals("Salsicha 2", response.get(0).getName());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsByNameAndDueDateOrderedByDueDate() {
        String productNameExistent = "Salsicha";
        String orderBy = "F";

        listOfFakeProducts.get(0).setName("Salsicha 1");
        listOfFakeProducts.get(0).setDueDate(LocalDate.now().plusWeeks(5));

        listOfFakeProducts.get(1).setName("Salsicha 2");
        listOfFakeProducts.get(1).setDueDate(LocalDate.now().plusWeeks(4));

        when(productRepository.findAllByName(anyString())).thenReturn(listOfFakeProducts);

        var response = service.findAllByNameAndDueDate(productNameExistent, orderBy);

        assertEquals("Salsicha 2", response.get(0).getName());
    }

    @Test
    public void shouldBeThrowIfInvalidEnumProvided() {
        String invalidCategory = "INVALID_CATEGORY";

        InvalidEnumException exception = assertThrows(InvalidEnumException.class, () -> service.findAllByCategory(invalidCategory));

        assertEquals("Category sent as parameter does not exist! The valid categories are CONGELADO, REFRIGERADO and FRESH", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfNoProductsFoundForTheGivenCategory() {
        String validCategory = "CONGELADO";

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findAllByCategory(validCategory));

        assertEquals("No products found for the given category", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsByCategory() {
        String validCategory = "CONGELADO";

        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setCategory(Category.CONGELADO);

        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setCategory(Category.CONGELADO);

        when(productRepository.findAllByCategory(any(Category.class))).thenReturn(listOfFakeProducts);

        var response = service.findAllByCategory(validCategory);

        assertEquals("Salsicha", response.get(0).getName());
        assertEquals("Frango", response.get(1).getName());
    }

    @Test
    public void shouldBeThrowIfProductNotFound() {
        String productNameNotExistent = "Pão";

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findAllByNameInWarehouse(productNameNotExistent));

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsByNameInWarehouse() {
        String productNameExistent = "Salsicha";

        var fakeProductInWarehouse1 = ProductsServiceMocks.makeFakeProductInWarehouse(22, 1L, "Salsicha");
        var fakeProductInWarehouse2 = ProductsServiceMocks.makeFakeProductInWarehouse(33, 2L, "Salsicha");

        when(productRepository.findAllByNameInWarehousesIgnoreCase(anyString())).thenReturn(Arrays.asList(fakeProductInWarehouse1, fakeProductInWarehouse2));

        var response = service.findAllByNameInWarehouse(productNameExistent);

        assertEquals("Salsicha", response.getProductId());
        assertEquals(1, response.getProductsDTO().get(0).getWarehouseCode());
        assertEquals(22, response.getProductsDTO().get(0).getTotalQuantity());
        assertEquals(2, response.getProductsDTO().get(1).getWarehouseCode());
        assertEquals(33, response.getProductsDTO().get(1).getTotalQuantity());
    }
}
