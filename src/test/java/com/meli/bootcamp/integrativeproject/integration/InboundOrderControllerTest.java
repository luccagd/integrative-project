package com.meli.bootcamp.integrativeproject.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration_tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InboundOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public void beforeAll() {
        jdbcTemplate.execute("INSERT INTO warehouses(name) VALUES ('Armazém de São Paulo')");
        jdbcTemplate.execute("INSERT INTO warehouses(name) VALUES ('Armazém da Bahia')");

        jdbcTemplate.execute("INSERT INTO seller (name) VALUES ('Sadia')");
        jdbcTemplate.execute("INSERT INTO seller (name) VALUES ('Procter & Gamble')");

        jdbcTemplate.execute("INSERT INTO agents(name, warehouse_id) VALUES ('Lucca', 1)");
        jdbcTemplate.execute("INSERT INTO agents(name, warehouse_id) VALUES ('João', 2)");

        jdbcTemplate.execute("INSERT INTO sections(category) VALUES ('REFRIGERADO')");
        jdbcTemplate.execute("INSERT INTO sections(category) VALUES ('CONGELADO')");
        jdbcTemplate.execute("INSERT INTO sections(category) VALUES ('FRESCO')");

        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 1, 1)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 2, 1)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 3, 1)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 1, 2)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 2, 2)");
        jdbcTemplate.execute("INSERT INTO warehouses_sections(size, total_products, section_id, warehouse_id) VALUES (100, 0, 3, 2)");
    }

    @Test
    public void shouldBeReturns404IfSellerNotFoundWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":999,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"CONGELADO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller not found for the given id"));
    }

    @Test
    public void shouldBeReturns404IfWarehouseNotFoundWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":999,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"CONGELADO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Warehouse not found for the given id"));
    }

    @Test
    public void shouldBeReturns400IfAgentNotBelongToWarehouseWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"CONGELADO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 999)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Agent does not belong to the given warehouse"));
    }

    @Test
    public void shouldBeReturns400IfWarehouseNotHaveTheGivenSectionWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":999,\"warehouseId\":1,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"CONGELADO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Warehouse dont have the given section"));
    }

    @Test
    public void shouldBeReturns400IfProductCategoryIsNoEqualToSectionCategoryWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"FRESCO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product category is not equal to section category"));
    }

    @Test
    public void shouldBeReturns400IfWarehouseSectionNotHaveEnoughSpaceWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"Salsicha\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":55,\"dueDate\":\"25-02-2022\",\"category\":\"REFRIGERADO\",\"price\":20.00},{\"name\":\"Frango\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":55,\"dueDate\":\"15-03-2022\",\"category\":\"REFRIGERADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Batch is bigger than section size"));
    }

    @Test
    public void shouldBeReturns201IfCreatedSuccessfullyInboundOrder() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":1,\"batchStock\":{\"products\":[{\"name\":\"Salsicha\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"REFRIGERADO\",\"price\":20.00},{\"name\":\"Frango\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"15-03-2022\",\"category\":\"REFRIGERADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.batchStock.products[0].name").value("Salsicha"))
                .andExpect(jsonPath("$.batchStock.products[1].name").value("Frango"));
    }
}
