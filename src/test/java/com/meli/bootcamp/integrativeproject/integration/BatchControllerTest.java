package com.meli.bootcamp.integrativeproject.integration;

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
public class BatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public void beforeAll() {
        jdbcTemplate.execute("INSERT INTO warehouses (name) VALUES ('Armazém de São Paulo')");
        jdbcTemplate.execute("INSERT INTO seller (name) VALUES ('Sadia')");

        jdbcTemplate.execute("INSERT INTO sections (category) VALUES ('REFRIGERADO')");
        jdbcTemplate.execute("INSERT INTO sections (category) VALUES ('CONGELADO')");
        jdbcTemplate.execute("INSERT INTO sections (category) VALUES ('FRESCO')");

        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 30, 1, 1)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 30, 2, 1)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 30, 3, 1)");

        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (123456, 1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (234567, 1, 1, 1)");

        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (345678, 2, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (456789, 2, 1, 1)");

        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (567890, 3, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (678901, 3, 1, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, DATEADD('DAY', 15, CURRENT_DATE), 0, 'Presunto', 12.35, 5, 2)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, DATEADD('DAY', 16, CURRENT_DATE), 0, 'Mussarela', 18.90, 5, 1)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, DATEADD('DAY', 3, CURRENT_DATE), 0, 'Salsicha', 12.35, 5, 4)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('CONGELADO', -10, DATEADD('DAY', 9, CURRENT_DATE), 0, 'Frango', 12.35, 5, 3)");

        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, DATEADD('DAY', 21, CURRENT_DATE), 0, 'Tilápia', 12.35, 5, 6)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('FRESCO', -10, DATEADD('DAY', 28, CURRENT_DATE), 0, 'Tucunaré', 18.90, 5, 5)");
    }

    @Test
    @Order(1)
    public void shouldBeReturns400IfAscNotBeTrueOrFalse() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FF")
                        .param("numberOfDays", "1")
                        .param("asc", "INVALID_ASC_PARAMETER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("asc can only be true or false"));
    }

    @Test
    @Order(2)
    public void shouldBeReturns400IfInvalidSectionNameIsProvided() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "INVALID_SECTION_NAME")
                        .param("numberOfDays", "1")
                        .param("asc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Section INVALID_SECTION_NAME not found"));
    }

    @Test
    @Order(3)
    public void shouldBeReturns400IfNumberOfDaysIsLessThan0() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FF")
                        .param("numberOfDays", "-1")
                        .param("asc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("number of days cannot be less than 0"));
    }

    @Test
    @Order(4)
    public void shouldBeReturns200WithListOffAllProductsFilteredByFrescoSectionNameOrderedByAscDueDate() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FS")
                        .param("numberOfDays", "28")
                        .param("asc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("5"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FS")
                        .param("numberOfDays", "28")
                        .param("asc", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("6"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FS")
                        .param("numberOfDays", "21")
                        .param("asc", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("1"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("5"));
    }

    @Test
    @Order(5)
    public void shouldBeReturns200WithListOffAllProductsFilteredByCongeladoSectionNameOrderedByAscDueDate() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FF")
                        .param("numberOfDays", "9")
                        .param("asc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("3"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FF")
                        .param("numberOfDays", "9")
                        .param("asc", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("4"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "FF")
                        .param("numberOfDays", "3")
                        .param("asc", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("1"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("3"));
    }

    @Test
    @Order(6)
    public void shouldBeReturns200WithListOffAllProductsFilteredByRefrigeradoSectionNameOrderedByAscDueDate() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "RF")
                        .param("numberOfDays", "16")
                        .param("asc", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("1"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "RF")
                        .param("numberOfDays", "16")
                        .param("asc", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("2"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("2"));

        this.mockMvc
                .perform(get("/fresh-products/due-date")
                        .param("sectionName", "RF")
                        .param("numberOfDays", "15")
                        .param("asc", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchStock.length()").value("1"))
                .andExpect(jsonPath("$.batchStock[0].productId").value("1"));
    }
}
