package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderRequest;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.exception.InvalidStatusException;
import com.example.ordermanagement.repository.OrderRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void testUpdateOrderStatus_validTransition_succeeds() {
        // Given: an existing order in PENDING status
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // call updateOrderStatus(id, CONFIRMED)
        Order updatedOrder = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        // the order status changes to CONFIRMED and is saved
        assertEquals(OrderStatus.CONFIRMED, updatedOrder.getOrderStatus(), "Status should be updated to CONFIRMED");
    }

    @Test
    public void testUpdateOrderStatus_invalidTransition_throwsException() {
        // Given: an existing order in DELIVERED status
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        // try to call updateOrderStatus(id, PENDING)
        // InvalidStatusException is thrown for illegal status transition
        assertThrows(InvalidStatusException.class, () -> {
            orderService.updateOrderStatus(1L, OrderStatus.PENDING);
        }, "Should throw InvalidStatusException for illegal state transition");
    }
}
