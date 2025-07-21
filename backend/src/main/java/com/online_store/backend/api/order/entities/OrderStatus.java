package com.online_store.backend.api.order.entities;

public enum OrderStatus {
    PAYMENT_PENDING,
    PAYMENT_RECEIVED,
    ORDER_RECEIVED,
    PREPARING,
    PACKAGING,
    READY_TO_SHIP,
    SHIPPED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED,
    CANCELLATION_REQUESTED,
    RETURN_REQUESTED,
    RETURN_APPROVED,
    RETURN_REJECTED,
    RETURNED;
}
