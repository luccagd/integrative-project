package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.ProductsServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService service;

    private List<Product> listOfFakeProducts;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        listOfFakeProducts = ProductsServiceMocks.makeFakeListOfProducts(2);

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
}
