package com.meli.bootcamp.integrativeproject.integration;

import com.meli.bootcamp.integrativeproject.repositories.CartProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.ProductRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseSectionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 60, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (123678, 1, 1, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Presunto', 12.35, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Mussarela', 18.90, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-02-22', 0, 'Requeijão', 8.74, 20, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-03-22', 0, 'Salsicha', 12.35, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-03-22', 0, 'Frango', 18.90, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, '2022-02-22', 0, 'Carne', 8.74, 20, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-03-22', 0, 'Tilápia', 12.35, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-03-22', 0, 'Tucunaré', 18.90, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, '2022-02-22', 0, 'Sardinha', 8.74, 20, 1)");
    }

    @Test
    @Order(1)
    public void shouldBeReturns200WithListOfProducts() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
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
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.name == \"Frango\")]").exists());
    }

    @Test
    @Order(4)
    public void shouldBeReturns404IfNoProductsWereFound() throws Exception {
        productRepository.deleteAll();

        this.mockMvc
                .perform(get("/fresh-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products were found"));
    }

    @Test
    @Order(5)
    public void shouldBeReturns404IfNoProductsFoundForTheGivenCategory() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/list/byCategory")
                        .param("category", "CONGELADO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products found for the given category"));
    }
}
