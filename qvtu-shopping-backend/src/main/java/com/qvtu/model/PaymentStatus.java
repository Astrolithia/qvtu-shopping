package com.qvtu.model;

public enum PaymentStatus {
    PENDING,      // 待支付
    COMPLETED,    // 已完成
    CAPTURED,     // 已捕获
    REFUNDED,     // 已退款
    CANCELED,     // 已取消
    REQUIRES_ACTION, // 需要操作
    FAILED        // 失败
} 