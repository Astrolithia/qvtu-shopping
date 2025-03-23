package com.qvtu.model;

public enum OrderStatus {
    DRAFT,              // 草稿状态
    PENDING,            // 待处理状态
    PROCESSING,         // 处理中状态
    AWAITING_PAYMENT,   // 等待支付状态
    AWAITING_FULFILLMENT, // 等待履行状态
    AWAITING_PICKUP,    // 等待取货状态
    AWAITING_SHIPMENT,  // 等待发货状态
    PARTIALLY_SHIPPED,  // 部分发货状态
    SHIPPED,            // 已发货状态
    DELIVERED,          // 已送达状态
    COMPLETED,          // 已完成状态
    CANCELLED,          // 已取消状态
    REFUNDED,           // 已退款状态
    REQUIRES_ACTION,    // 需要操作状态
    ARCHIVED            // 已归档状态
} 