package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cart {
    private int id;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItem> cartItems;
    
    public Cart() {}
    
    public Cart(int userId) {
        this.userId = userId;
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
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}
