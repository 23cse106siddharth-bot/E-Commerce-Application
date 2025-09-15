package com.ecommerce.dao;

import com.ecommerce.model.Address;
import com.ecommerce.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {
    private static final Logger logger = LoggerFactory.getLogger(AddressDAO.class);
    
    public boolean createAddress(Address address) {
        String sql = "INSERT INTO addresses (user_id, type, street, city, state, zip_code, country, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, address.getUserId());
            stmt.setString(2, address.getType());
            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getCity());
            stmt.setString(5, address.getState());
            stmt.setString(6, address.getZipCode());
            stmt.setString(7, address.getCountry());
            stmt.setBoolean(8, address.isDefault());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        address.setId(generatedKeys.getInt(1));
                        logger.info("Address created successfully with ID: {}", address.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating address", e);
        }
        
        return false;
    }
    
    public Address getAddressById(int id) {
        String sql = "SELECT * FROM addresses WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting address by ID: {}", id, e);
        }
        
        return null;
    }
    
    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY is_default DESC, created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapResultSetToAddress(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting addresses by user ID: {}", userId, e);
        }
        
        return addresses;
    }
    
    public boolean updateAddress(Address address) {
        String sql = "UPDATE addresses SET type = ?, street = ?, city = ?, state = ?, zip_code = ?, country = ?, is_default = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, address.getType());
            stmt.setString(2, address.getStreet());
            stmt.setString(3, address.getCity());
            stmt.setString(4, address.getState());
            stmt.setString(5, address.getZipCode());
            stmt.setString(6, address.getCountry());
            stmt.setBoolean(7, address.isDefault());
            stmt.setInt(8, address.getId());
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Address updated: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating address with ID: {}", address.getId(), e);
        }
        
        return false;
    }
    
    public boolean deleteAddress(int id) {
        String sql = "DELETE FROM addresses WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Address deleted: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting address with ID: {}", id, e);
        }
        
        return false;
    }
    
    public boolean setDefaultAddress(int userId, int addressId) {
        String sql = "UPDATE addresses SET is_default = (id = ?) WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, addressId);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("Default address set: {} rows affected", affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error setting default address for user ID: {}", userId, e);
        }
        
        return false;
    }
    
    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setUserId(rs.getInt("user_id"));
        address.setType(rs.getString("type"));
        address.setStreet(rs.getString("street"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZipCode(rs.getString("zip_code"));
        address.setCountry(rs.getString("country"));
        address.setDefault(rs.getBoolean("is_default"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            address.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return address;
    }
}
