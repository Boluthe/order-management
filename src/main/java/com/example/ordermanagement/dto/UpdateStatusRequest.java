package com.example.ordermanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateStatusRequest {
    
    @NotBlank(message = "Order status is required")
    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
