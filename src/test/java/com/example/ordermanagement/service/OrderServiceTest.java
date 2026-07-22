package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderRequest;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder_validRequest_succeeds() {
        // Given: valid CreateOrderRequest
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("John Doe");
        request.setVendorName("Vendor A");
        request.setProductName("Laptop");
        request.setQuantity(2);
        request.setUnitPrice(1000.0);
        request.setDeliveryAddress("123 Main St");

        // mock the repository to return the order passed in mimicking DB behavior
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        // create order method is called
        Order result = orderService.createOrder(request);

        // order is saved with PENDING status and totalAmount calculated
        assertNotNull(result.getId(), "order ID is populated after saving");
        assertEquals(OrderStatus.PENDING, result.getOrderStatus(), "status default is PENDING");
        assertEquals(2000.0, result.getTotalAmount(), "total amount is quantity * unitPrice");
    }
}
