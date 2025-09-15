package com.ecommerce.util;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtil {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final int ROUNDS = 12;
    
    /**
     * Hash a password using BCrypt
     * @param password Plain text password
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt(ROUNDS));
        } catch (Exception e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Password hashing failed", e);
        }
    }
    
    /**
     * Verify a password against its hash
     * @param password Plain text password
     * @param hashedPassword Hashed password from database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Check if a password meets minimum requirements
     * @param password Password to validate
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        // Check for at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasLetter && hasNumber;
    }
}
