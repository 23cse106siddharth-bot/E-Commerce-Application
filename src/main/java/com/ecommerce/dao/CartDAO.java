package com.ecommerce.dao;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);
    
    public Cart getCartByUserId(int userId) {
        String sql = "SELECT * FROM carts WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = mapResultSetToCart(rs);
                    cart.setCartItems(getCartItemsByCartId(cart.getId()));
                    return cart;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting cart by user ID: {}", userId, e);
        }
        
        return null;
    }
    
    public Cart createCart(int userId) {
        String sql = "INSERT INTO carts (user_id) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Cart cart = new Cart();
                        cart.setId(generatedKeys.getInt(1));
                        cart.setUserId(userId);
                        logger.info("Cart created successfully with ID: {}", cart.getId());
                        return cart;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating cart for user: {}", userId, e);
        }
        
        return null;
    }
    
    public boolean addItemToCart(int cartId, int productId, int quantity) {
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, quantity);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Item added to cart: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error adding item to cart", e);
        }
        
        return false;
    }
    
    public boolean updateCartItemQuantity(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            return removeItemFromCart(cartId, productId);
        }
        
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartId);
            stmt.setInt(3, productId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Cart item quantity updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating cart item quantity", e);
        }
        
        return false;
    }
    
    public boolean removeItemFromCart(int cartId, int productId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Item removed from cart: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error removing item from cart", e);
        }
        
        return false;
    }
    
    public boolean clearCart(int cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Cart cleared: {} rows affected", affectedRows);
            return true; // Return true even if no items were in cart
            
        } catch (SQLException e) {
            logger.error("Error clearing cart", e);
        }
        
        return false;
    }
    
    public List<CartItem> getCartItemsByCartId(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.*, p.name, p.price, p.image_url, p.stock_quantity " +
                    "FROM cart_items ci " +
                    "JOIN products p ON ci.product_id = p.id " +
                    "WHERE ci.cart_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem cartItem = mapResultSetToCartItem(rs);
                    cartItems.add(cartItem);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting cart items for cart ID: {}", cartId, e);
        }
        
        return cartItems;
    }
    
    public int getCartItemCount(int cartId) {
        String sql = "SELECT SUM(quantity) FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting cart item count for cart ID: {}", cartId, e);
        }
        
        return 0;
    }
    
    private Cart mapResultSetToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getInt("id"));
        cart.setUserId(rs.getInt("user_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            cart.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            cart.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return cart;
    }
    
    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getInt("id"));
        cartItem.setCartId(rs.getInt("cart_id"));
        cartItem.setProductId(rs.getInt("product_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            cartItem.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            cartItem.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Set product information
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImageUrl(rs.getString("image_url"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        cartItem.setProduct(product);
        
        return cartItem;
    }
    
    public boolean clearCart(int cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Cart cleared: {} items removed", affectedRows);
            return true;
            
        } catch (SQLException e) {
            logger.error("Error clearing cart with ID: {}", cartId, e);
        }
        
        return false;
    }
}
