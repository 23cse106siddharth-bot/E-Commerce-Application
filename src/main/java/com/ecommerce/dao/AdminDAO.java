package com.ecommerce.dao;

import com.ecommerce.model.Admin;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);
    
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admins WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting admin by username: {}", username, e);
        }
        
        return null;
    }
    
    public Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admins WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting admin by email: {}", email, e);
        }
        
        return null;
    }
    
    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admins WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting admin by ID: {}", id, e);
        }
        
        return null;
    }
    
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admins ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error getting all admins", e);
        }
        
        return admins;
    }
    
    public boolean createAdmin(Admin admin) {
        String sql = "INSERT INTO admins (username, email, password_hash) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getPasswordHash());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        admin.setId(generatedKeys.getInt(1));
                        logger.info("Admin created successfully with ID: {}", admin.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating admin", e);
        }
        
        return false;
    }
    
    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE admins SET username = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getEmail());
            stmt.setInt(3, admin.getId());
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Admin updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating admin with ID: {}", admin.getId(), e);
        }
        
        return false;
    }
    
    public boolean deleteAdmin(int id) {
        String sql = "DELETE FROM admins WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Admin deleted: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting admin with ID: {}", id, e);
        }
        
        return false;
    }
    
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if username exists: {}", username, e);
        }
        
        return false;
    }
    
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM admins WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if email exists: {}", email, e);
        }
        
        return false;
    }
    
    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setUsername(rs.getString("username"));
        admin.setEmail(rs.getString("email"));
        admin.setPasswordHash(rs.getString("password_hash"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            admin.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return admin;
    }
}
