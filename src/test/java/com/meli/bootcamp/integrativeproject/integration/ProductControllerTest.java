package com.meli.bootcamp.integrativeproject.integration;

import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration_tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public void beforeAll() {
        jdbcTemplate.execute("INSERT INTO warehouses (name) VALUES ('Armazém de São Paulo')");
        jdbcTemplate.execute("INSERT INTO seller (name) VALUES ('Sadia')");
        jdbcTemplate.execute("INSERT INTO sections (category) VALUES ('REFRIGERADO')");
        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 90, 1, 1)");

        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (123678, 1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (234567, 1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (345678, 1, 1, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Presunto', 12.35, 5, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Mussarela', 18.90, 5, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-02-22', 0, 'Requeijão', 8.74, 10, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-02-28', 0, 'Salsicha', 12.35, 10, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -8.8, '2022-05-28', 0, 'Salsicha', 12.35, 5, 2)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -9.5, '2022-04-28', 0, 'Salsicha', 12.35, 6, 3)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-02-28', 0, 'Frango', 18.90, 10, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -9, '2022-02-28', 0, 'Frango', 18.90, 10, 2)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -8.4, '2022-02-28', 0, 'Frango', 18.90, 10, 3)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-02-22', 0, 'Carne', 8.74, 10, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-03-22', 0, 'Tilápia', 12.35, 5, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-03-22', 0, 'Tucunaré', 18.90, 2, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-02-22', 0, 'Sardinha', 8.74, 2, 1)");
    }

    @Test
    @Order(1)
    public void shouldBeReturns200WithListOfProducts() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(13))
                .andExpect(jsonPath("$[?(@.name == \"Tucunaré\")]").exists());
    }

    @Test
    @Order(2)
    public void shouldBeReturns400IfInvalidEnumProvided() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byCategory")
                        .param("category", "INVALID_ENUM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Category sent as parameter does not exist! The valid categories are CONGELADO, REFRIGERADO and FRESH"));
    }

    @Test
    @Order(3)
    public void shouldBeReturns200WithListOfProductsFilteredByCategory() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byCategory")
                        .param("category", "CONGELADO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[?(@.name == \"Frango\")]").exists());
    }

    @Test
    @Order(4)
    public void shouldBeReturns404IfNoProductsFoundForTheGivenName() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Pão")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products found for the given name"));
    }

    @Test
    @Order(5)
    public void shouldBeReturns404IfNoProductsFoundWithinTheDueDateForTheGivenName() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Frango")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products found within the due date for the given name"));
    }

    @Test
    @Order(6)
    public void shouldBeReturns400IfInvalidSortValueIsProvided() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Salsicha")
                        .param("orderBy", "INVALID_SORT_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Valid values for sorting are L, C or F"));
    }

    @Test
    @Order(7)
    public void shouldBeReturns200WithListOfProductsFilteredByNameAndDueDateOrderedByBatchNumber() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Salsicha")
                        .param("orderBy", "L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("2"))
                .andExpect(jsonPath("$[0].batch.productQuantity").value("5"))
                .andExpect(jsonPath("$[1].batch.productQuantity").value("6"));
    }

    @Test
    @Order(8)
    public void shouldBeReturns200WithListOfProductsFilteredByNameAndDueDateOrderedByQuantity() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Salsicha")
                        .param("orderBy", "C")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("2"))
                .andExpect(jsonPath("$[0].batch.batchNumber").value("234567"))
                .andExpect(jsonPath("$[1].batch.batchNumber").value("345678"));
    }

    @Test
    @Order(9)
    public void shouldBeReturns200WithListOfProductsFilteredByNameAndDueDateOrderedByDueDate() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byName")
                        .param("name", "Salsicha")
                        .param("orderBy", "F")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("2"))
                .andExpect(jsonPath("$[0].batch.batchId").value("3"))
                .andExpect(jsonPath("$[1].batch.batchId").value("2"));
    }

    @Test
    @Order(10)
    public void shouldBeReturns404IfProductNameNotFoundInWarehouse() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/warehouse")
                        .param("product_name", "PRODUCT_NAME_NOT_EXISTENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    @Order(11)
    public void shouldBeReturns200WithListOfProductsFilteredByNameInWarehouse() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/warehouse")
                        .param("product_name", "Salsicha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("Salsicha"))
                .andExpect(jsonPath("$.warehouses[0].totalQuantity").value("21"));
    }

    @Test
    @Order(12)
    public void shouldBeReturns404IfNoProductsWereFound() throws Exception {
        productRepository.deleteAll();

        this.mockMvc
                .perform(get("/fresh-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products were found"));
    }

    @Test
    @Order(13)
    public void shouldBeReturns404IfNoProductsFoundForTheGivenCategory() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byCategory")
                        .param("category", "CONGELADO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products found for the given category"));
    }
}
