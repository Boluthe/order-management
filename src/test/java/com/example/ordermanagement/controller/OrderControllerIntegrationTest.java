package com.example.ordermanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // creating an order and testing the api to see if it returns 201
    @Test
    public void testCreateOrder_validRequest_returns201() throws Exception {
        String json = """
                {
                    "customerName": "John",
                    "vendorName": "Vendor A",
                    "productName": "Laptop",
                    "quantity": 2,
                    "unitPrice": 1000.0,
                    "deliveryAddress": "21 Allen Avenue"
                }
                """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }
    // pulling all the order and testing if it returns 200
    @Test
    public void testGetAllOrders_returns200() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    // testing a bad ID to make sure it throws a 404
    @Test
    public void testGetOrderById_invalidId_returns404() throws Exception {
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
