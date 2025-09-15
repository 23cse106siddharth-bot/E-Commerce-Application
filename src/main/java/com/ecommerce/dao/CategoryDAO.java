package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);
    
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setId(generatedKeys.getInt(1));
                        logger.info("Category created successfully with ID: {}", category.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating category", e);
        }
        
        return false;
    }
    
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting category by ID: {}", id, e);
        }
        
        return null;
    }
    
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error getting all categories", e);
        }
        
        return categories;
    }
    
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Category updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating category with ID: {}", category.getId(), e);
        }
        
        return false;
    }
    
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Category deleted: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting category with ID: {}", id, e);
        }
        
        return false;
    }
    
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            category.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return category;
    }
}
