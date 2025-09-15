package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/user/login", "/user/register", "/user/logout"})
public class UserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/user/login":
                showLoginPage(request, response);
                break;
            case "/user/register":
                showRegisterPage(request, response);
                break;
            case "/user/logout":
                logout(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/user/login":
                handleLogin(request, response);
                break;
            case "/user/register":
                handleRegister(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            if (email == null || email.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Email and password are required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }
            
            User user = userDAO.getUserByEmail(email.trim());
            
            if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                
                logger.info("User logged in successfully: {}", email);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error during login", e);
            request.setAttribute("error", "An error occurred during login");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String phone = request.getParameter("phone");
            
            // Validation
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }
            
            if (password.length() < 6) {
                request.setAttribute("error", "Password must be at least 6 characters long");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }
            
            if (userDAO.emailExists(email.trim())) {
                request.setAttribute("error", "Email already exists");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }
            
            // Create user
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPasswordHash(PasswordUtil.hashPassword(password));
            user.setPhone(phone != null ? phone.trim() : null);
            
            if (userDAO.createUser(user)) {
                request.setAttribute("success", "Registration successful! Please login.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error during registration", e);
            request.setAttribute("error", "An error occurred during registration");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/");
    }
}
