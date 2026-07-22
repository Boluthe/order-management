package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderRequest;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.exception.InvalidStatusException;
import com.example.ordermanagement.exception.OrderNotFoundException;
import com.example.ordermanagement.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest request) {
        // sanity check
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setVendorName(request.getVendorName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.calculateTotal();

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Couldn't find order ID: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = getOrderById(id);

        if (!order.getOrderStatus().canTransitionTo(newStatus)) {
            throw new InvalidStatusException("Cannot transition from " + order.getOrderStatus() + " to " + newStatus);
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        
        // flip it to CANCELLED and save
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
}
