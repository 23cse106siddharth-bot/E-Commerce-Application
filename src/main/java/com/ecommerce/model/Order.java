package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private int shippingAddressId;
    private int billingAddressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;
    private Address shippingAddress;
    private Address billingAddress;
    private User user;
    
    public Order() {}
    
    public Order(int userId, String orderNumber, BigDecimal totalAmount) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = "pending";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getShippingAddressId() {
        return shippingAddressId;
    }
    
    public void setShippingAddressId(int shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
    
    public int getBillingAddressId() {
        return billingAddressId;
    }
    
    public void setBillingAddressId(int billingAddressId) {
        this.billingAddressId = billingAddressId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public Address getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public Address getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNumber='" + orderNumber + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}
