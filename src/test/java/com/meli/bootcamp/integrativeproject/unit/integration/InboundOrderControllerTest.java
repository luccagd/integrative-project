package com.meli.bootcamp.integrativeproject.unit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import com.meli.bootcamp.integrativeproject.repositories.WarehouseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

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
        ;
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
}
