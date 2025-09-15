package com.ecommerce.dao;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
    
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address_id, billing_address_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, order.getUserId());
            stmt.setString(2, order.getOrderNumber());
            stmt.setString(3, order.getStatus());
            stmt.setBigDecimal(4, order.getTotalAmount());
            stmt.setInt(5, order.getShippingAddressId());
            stmt.setInt(6, order.getBillingAddressId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                        logger.info("Order created successfully with ID: {}", order.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating order", e);
        }
        
        return false;
    }
    
    public boolean createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getProductId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Order item created: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error creating order item", e);
        }
        
        return false;
    }
    
    public Order getOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting order by ID: {}", id, e);
        }
        
        return null;
    }
    
    public Order getOrderByOrderNumber(String orderNumber) {
        String sql = "SELECT * FROM orders WHERE order_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, orderNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting order by order number: {}", orderNumber, e);
        }
        
        return null;
    }
    
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting orders by user ID: {}", userId, e);
        }
        
        return orders;
    }
    
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.image_url " +
                    "FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.id " +
                    "WHERE oi.order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(mapResultSetToOrderItem(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting order items for order ID: {}", orderId, e);
        }
        
        return orderItems;
    }
    
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Order status updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating order status for order ID: {}", orderId, e);
        }
        
        return false;
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setShippingAddressId(rs.getInt("shipping_address_id"));
        order.setBillingAddressId(rs.getInt("billing_address_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            order.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return order;
    }
    
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getBigDecimal("price"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            orderItem.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return orderItem;
    }
}
