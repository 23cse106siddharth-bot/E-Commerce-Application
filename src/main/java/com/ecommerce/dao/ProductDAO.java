package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category_id, image_url, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setInt(5, product.getCategoryId());
            stmt.setString(6, product.getImageUrl());
            stmt.setString(7, product.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                        logger.info("Product created successfully with ID: {}", product.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating product", e);
        }
        
        return false;
    }
    
    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.name as category_name, c.description as category_description " +
                    "FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting product by ID: {}", id, e);
        }
        
        return null;
    }
    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, c.description as category_description " +
                    "FROM products p LEFT JOIN categories c ON p.category_id = c.id " +
                    "WHERE p.status = 'active' ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error getting all products", e);
        }
        
        return products;
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, c.description as category_description " +
                    "FROM products p LEFT JOIN categories c ON p.category_id = c.id " +
                    "WHERE p.category_id = ? AND p.status = 'active' ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting products by category: {}", categoryId, e);
        }
        
        return products;
    }
    
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, c.description as category_description " +
                    "FROM products p LEFT JOIN categories c ON p.category_id = c.id " +
                    "WHERE (p.name LIKE ? OR p.description LIKE ?) AND p.status = 'active' " +
                    "ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error searching products with term: {}", searchTerm, e);
        }
        
        return products;
    }
    
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, c.description as category_description " +
                    "FROM products p LEFT JOIN categories c ON p.category_id = c.id " +
                    "WHERE p.price BETWEEN ? AND ? AND p.status = 'active' " +
                    "ORDER BY p.price ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting products by price range: {} - {}", minPrice, maxPrice, e);
        }
        
        return products;
    }
    
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, " +
                    "category_id = ?, image_url = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setInt(5, product.getCategoryId());
            stmt.setString(6, product.getImageUrl());
            stmt.setString(7, product.getStatus());
            stmt.setInt(8, product.getId());
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Product updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating product with ID: {}", product.getId(), e);
        }
        
        return false;
    }
    
    public boolean deleteProduct(int id) {
        String sql = "UPDATE products SET status = 'inactive' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Product deleted: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting product with ID: {}", id, e);
        }
        
        return false;
    }
    
    public boolean updateStock(int productId, int quantity) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ? AND stock_quantity >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Stock updated for product {}: {} rows affected", productId, affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating stock for product: {}", productId, e);
        }
        
        return false;
    }
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setImageUrl(rs.getString("image_url"));
        product.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            product.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Set category information
        String categoryName = rs.getString("category_name");
        if (categoryName != null) {
            Category category = new Category();
            category.setId(rs.getInt("category_id"));
            category.setName(categoryName);
            category.setDescription(rs.getString("category_description"));
            product.setCategory(category);
        }
        
        return product;
    }
    
    public boolean updateStock(int productId, int quantityToReduce) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ? AND stock_quantity >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantityToReduce);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantityToReduce);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Product stock updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating stock for product ID: {}", productId, e);
        }
        
        return false;
    }
}
