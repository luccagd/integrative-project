package com.meli.bootcamp.integrativeproject.unit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/initializeDataDatabase.sql")
@ActiveProfiles("integration_tests")
public class InboundOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldBeReturns404IfSellerNotFoundWhenTrySave() throws Exception {
        String httpRequest = "{\"sectionId\":1,\"warehouseId\":1,\"sellerId\":999,\"batchStock\":{\"products\":[{\"name\":\"PEIXE\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":1,\"dueDate\":\"25-02-2022\",\"category\":\"CONGELADO\",\"price\":20.00},{\"name\":\"FRANGO\",\"currentTemperature\":10,\"minimalTemperature\":5,\"quantity\":10,\"dueDate\":\"15-03-2022\",\"category\":\"CONGELADO\",\"price\":10.00}]}}";

        this.mockMvc
                .perform(post("/fresh-products/inboundorder")
                        .header("agentId", 1)
                        .content(httpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller not found for the given id"));
    }
}
