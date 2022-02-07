package com.meli.bootcamp.integrativeproject.integration;

import com.meli.bootcamp.integrativeproject.entity.CartProduct;
import com.meli.bootcamp.integrativeproject.entity.Product;
import com.meli.bootcamp.integrativeproject.entity.WarehouseSection;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration_tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private WarehouseSectionRepository warehouseSectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public void beforeAll() {
        jdbcTemplate.execute("INSERT INTO warehouses (name) VALUES ('Armazém de São Paulo')");
        jdbcTemplate.execute("INSERT INTO seller (name) VALUES ('Sadia')");
        jdbcTemplate.execute("INSERT INTO buyers (name) VALUES ('Lemuel')");
        jdbcTemplate.execute("INSERT INTO sections (category) VALUES ('REFRIGERADO')");
        jdbcTemplate.execute("INSERT INTO warehouses_sections (size, total_products, section_id, warehouse_id) VALUES (100, 60, 1, 1)");
        jdbcTemplate.execute("INSERT INTO batches (batch_number, section_id, seller_id, warehouse_id) VALUES (123678, 1, 1, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Salsicha', 12.35, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-03-22', 0, 'Frango', 18.90, 20, 1)");
        jdbcTemplate.execute("INSERT INTO products (category, current_temperature, due_date, minimal_temperature, name, price, quantity, batch_id) VALUES ('REFRIGERADO', -10, '2022-02-22', 0, 'Presunto', 8.74, 20, 1)");
    }

    @Test
    @Order(1)
    public void shouldBeReturns404IfBuyerNotExists() throws Exception {
        String httpRequestForCreateCart = "{\"buyerId\":999,\"date\":\"02-02-2022\",\"status\":\"ABERTO\",\"products\":[{\"productId\":1,\"quantity\":5},{\"productId\":2,\"quantity\":5}]}";

        this.mockMvc
                .perform(post("/fresh-products/orders")
                        .content(httpRequestForCreateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Buyer not exists!"));
    }

    @Test
    @Order(2)
    public void shouldBeReturns400IfProductDoesNotHaveEnoughStock() throws Exception {
        String httpRequestForCreateCart = "{\"buyerId\":1,\"date\":\"02-02-2022\",\"status\":\"ABERTO\",\"products\":[{\"productId\":1,\"quantity\":50},{\"productId\":2,\"quantity\":5}]}";

        this.mockMvc
                .perform(post("/fresh-products/orders")
                        .content(httpRequestForCreateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product Salsicha does not have enough stock for this quantity!"));
    }

    @Test
    @Order(3)
    public void shouldBeReturns400IfProductHasAnExpirationDateOfLessThan3Weeks() throws Exception {
        String httpRequestForCreateCart = "{\"buyerId\":1,\"date\":\"02-02-2022\",\"status\":\"ABERTO\",\"products\":[{\"productId\":1,\"quantity\":5},{\"productId\":3,\"quantity\":5}]}";

        this.mockMvc
                .perform(post("/fresh-products/orders")
                        .content(httpRequestForCreateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product Presunto has an expiration date of less than 3 weeks!"));
    }

    @Test
    @Order(4)
    public void shouldBeReturns201IfCreatedSuccessfullyPurchaseOrder() throws Exception {
        String httpRequestForCreateCart = "{\"buyerId\":1,\"date\":\"02-02-2022\",\"status\":\"ABERTO\",\"products\":[{\"productId\":1,\"quantity\":4},{\"productId\":2,\"quantity\":3}]}";

        this.mockMvc
                .perform(post("/fresh-products/orders")
                        .content(httpRequestForCreateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value("" + BigDecimal.valueOf((4 * 12.35) + (3 * 18.90))));

        CartProduct createdCartProduct1 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 1L);
        CartProduct createdCartProduct2 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 2L);
        WarehouseSection createdWarehouseSection = warehouseSectionRepository.findById(1L).get();
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        assertEquals(4, createdCartProduct1.getQuantity());
        assertEquals(3, createdCartProduct2.getQuantity());
        assertEquals(53, createdWarehouseSection.getTotalProducts());
        assertEquals(16, product1.getQuantity());
        assertEquals(17, product2.getQuantity());
    }

    @Test
    @Order(5)
    public void shouldBeReturns404IfCartNotExistsWhenTryUpdate() throws Exception {
        String httpRequestForUpdateCart = "{\"buyerId\":1,\"products\":[{\"productId\":1,\"quantity\":5},{\"productId\":2,\"quantity\":5}]}";

        this.mockMvc
                .perform(put("/fresh-products/orders")
                        .param("idOrder", "999")
                        .content(httpRequestForUpdateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cart not exists!"));
    }

    @Test
    @Order(6)
    public void shouldBeReturns400IfProductDoesNotExistsInTheCart() throws Exception {
        String httpRequestForUpdateCart = "{\"buyerId\":1,\"products\":[{\"productId\":999,\"quantity\":5},{\"productId\":2,\"quantity\":5}]}";

        this.mockMvc
                .perform(put("/fresh-products/orders")
                        .param("idOrder", "1")
                        .content(httpRequestForUpdateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("This product does not exist in this cart!"));
    }

    @Test
    @Order(7)
    public void shouldBeReturns400IfOrderedQuantityIsGreaterThanIsInStock() throws Exception {
        String httpRequestForUpdateCart = "{\"buyerId\":1,\"products\":[{\"productId\":1,\"quantity\":5},{\"productId\":2,\"quantity\":21}]}";

        this.mockMvc
                .perform(put("/fresh-products/orders")
                        .param("idOrder", "1")
                        .content(httpRequestForUpdateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ordered quantity to product Frango is greater than what is in stock!"));
    }

    @Test
    @Order(8)
    public void shouldBeReturns200IfUpdatedSuccessfullyPurchaseOrderAndDecreaseTotalProducts() throws Exception {
        String httpRequestForUpdateCart = "{\"buyerId\":1,\"products\":[{\"productId\":1,\"quantity\":8},{\"productId\":2,\"quantity\":9}]}";

        this.mockMvc
                .perform(put("/fresh-products/orders")
                        .param("idOrder", "1")
                        .content(httpRequestForUpdateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("" + BigDecimal.valueOf((8 * 12.35) + (9 * 18.90))));

        CartProduct createdCartProduct1 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 1L);
        CartProduct createdCartProduct2 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 2L);
        WarehouseSection createdWarehouseSection = warehouseSectionRepository.findById(1L).get();
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        assertEquals(8, createdCartProduct1.getQuantity());
        assertEquals(9, createdCartProduct2.getQuantity());
        assertEquals(43, createdWarehouseSection.getTotalProducts());
        assertEquals(12, product1.getQuantity());
        assertEquals(11, product2.getQuantity());
    }

    @Test
    @Order(9)
    public void shouldBeReturns200IfUpdatedSuccessfullyPurchaseOrderAndIncreaseTotalProducts() throws Exception {
        String httpRequestForUpdateCart = "{\"buyerId\":1,\"products\":[{\"productId\":1,\"quantity\":3},{\"productId\":2,\"quantity\":1}]}";

        this.mockMvc
                .perform(put("/fresh-products/orders")
                        .param("idOrder", "1")
                        .content(httpRequestForUpdateCart)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("" + BigDecimal.valueOf((3 * 12.35) + (1 * 18.90))));

        CartProduct createdCartProduct1 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 1L);
        CartProduct createdCartProduct2 = cartProductRepository.findByCart_IdAndProduct_Id(1L, 2L);
        WarehouseSection createdWarehouseSection = warehouseSectionRepository.findById(1L).get();
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        assertEquals(3, createdCartProduct1.getQuantity());
        assertEquals(1, createdCartProduct2.getQuantity());
        assertEquals(56, createdWarehouseSection.getTotalProducts());
        assertEquals(17, product1.getQuantity());
        assertEquals(19, product2.getQuantity());
    }

    @Test
    @Order(10)
    public void shouldBeReturns200WithListOfCartProducts() throws Exception {
        this.mockMvc
                .perform(get("/fresh-products/orders/{idOrder}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product.name").value("Salsicha"))
                .andExpect(jsonPath("$[1].product.name").value("Frango"));
    }
}
