package com.example.ordermanagement.entity;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    READY_FOR_PICKUP,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        if (this == newStatus) {
            return false;
        }
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == READY_FOR_PICKUP || newStatus == CANCELLED;
            case READY_FOR_PICKUP -> newStatus == DELIVERED || newStatus == CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}
