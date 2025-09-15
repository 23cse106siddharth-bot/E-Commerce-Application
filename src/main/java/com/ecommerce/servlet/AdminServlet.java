package com.ecommerce.servlet;

import com.ecommerce.dao.AdminDAO;
import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.Admin;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
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
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "login";
        }
        
        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "dashboard":
                showDashboard(request, response);
                break;
            case "products":
                showProducts(request, response);
                break;
            case "categories":
                showCategories(request, response);
                break;
            case "users":
                showUsers(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                showLoginPage(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "addProduct":
                addProduct(request, response);
                break;
            case "updateProduct":
                updateProduct(request, response);
                break;
            case "deleteProduct":
                deleteProduct(request, response);
                break;
            case "addCategory":
                addCategory(request, response);
                break;
            case "updateCategory":
                updateCategory(request, response);
                break;
            case "deleteCategory":
                deleteCategory(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin");
        }
    }
    
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            // Get statistics
            List<Product> products = productDAO.getAllProducts();
            List<Category> categories = categoryDAO.getAllCategories();
            List<User> users = userDAO.getAllUsers();
            
            request.setAttribute("productCount", products.size());
            request.setAttribute("categoryCount", categories.size());
            request.setAttribute("userCount", users.size());
            request.setAttribute("products", products.subList(0, Math.min(5, products.size())));
            
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error in admin dashboard", e);
            request.setAttribute("error", "An error occurred while loading dashboard");
            request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
        }
    }
    
    private void showProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            List<Product> products = productDAO.getAllProducts();
            List<Category> categories = categoryDAO.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading products for admin", e);
            request.setAttribute("error", "An error occurred while loading products");
            request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
        }
    }
    
    private void showCategories(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading categories for admin", e);
            request.setAttribute("error", "An error occurred while loading categories");
            request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
        }
    }
    
    private void showUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            List<User> users = userDAO.getAllUsers();
            request.setAttribute("users", users);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading users for admin", e);
            request.setAttribute("error", "An error occurred while loading users");
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        }
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Username and password are required");
                request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
                return;
            }
            
            Admin admin = adminDAO.getAdminByUsername(username.trim());
            
            if (admin != null && PasswordUtil.verifyPassword(password, admin.getPasswordHash())) {
                HttpSession session = request.getSession();
                session.setAttribute("admin", admin);
                session.setAttribute("adminId", admin.getId());
                session.setAttribute("adminUsername", admin.getUsername());
                
                logger.info("Admin logged in successfully: {}", username);
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
            } else {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error during admin login", e);
            request.setAttribute("error", "An error occurred during login");
            request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
        }
    }
    
    private void addProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stockQuantity");
            String categoryIdStr = request.getParameter("categoryId");
            String imageUrl = request.getParameter("imageUrl");
            
            if (name == null || name.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                stockStr == null || stockStr.trim().isEmpty() ||
                categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                
                request.setAttribute("error", "All required fields must be filled");
                response.sendRedirect(request.getContextPath() + "/admin?action=products");
                return;
            }
            
            Product product = new Product();
            product.setName(name.trim());
            product.setDescription(description.trim());
            product.setPrice(new BigDecimal(priceStr));
            product.setStockQuantity(Integer.parseInt(stockStr));
            product.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            product.setStatus("active");
            
            if (productDAO.createProduct(product)) {
                logger.info("Product created successfully: {}", product.getName());
            } else {
                request.setAttribute("error", "Failed to create product");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
            
        } catch (Exception e) {
            logger.error("Error adding product", e);
            request.setAttribute("error", "An error occurred while adding product");
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
        }
    }
    
    private void updateProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stockQuantity");
            String categoryIdStr = request.getParameter("categoryId");
            String imageUrl = request.getParameter("imageUrl");
            String status = request.getParameter("status");
            
            if (idStr == null || idStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin?action=products");
                return;
            }
            
            Product product = new Product();
            product.setId(Integer.parseInt(idStr));
            product.setName(name != null ? name.trim() : "");
            product.setDescription(description != null ? description.trim() : "");
            product.setPrice(new BigDecimal(priceStr));
            product.setStockQuantity(Integer.parseInt(stockStr));
            product.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            product.setStatus(status != null ? status : "active");
            
            if (productDAO.updateProduct(product)) {
                logger.info("Product updated successfully: {}", product.getName());
            } else {
                request.setAttribute("error", "Failed to update product");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
            
        } catch (Exception e) {
            logger.error("Error updating product", e);
            request.setAttribute("error", "An error occurred while updating product");
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
        }
    }
    
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String idStr = request.getParameter("id");
            
            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);
                if (productDAO.deleteProduct(id)) {
                    logger.info("Product deleted successfully: {}", id);
                } else {
                    request.setAttribute("error", "Failed to delete product");
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
            
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            request.setAttribute("error", "An error occurred while deleting product");
            response.sendRedirect(request.getContextPath() + "/admin?action=products");
        }
    }
    
    private void addCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Category name is required");
                response.sendRedirect(request.getContextPath() + "/admin?action=categories");
                return;
            }
            
            Category category = new Category();
            category.setName(name.trim());
            category.setDescription(description != null ? description.trim() : null);
            
            if (categoryDAO.createCategory(category)) {
                logger.info("Category created successfully: {}", category.getName());
            } else {
                request.setAttribute("error", "Failed to create category");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
            
        } catch (Exception e) {
            logger.error("Error adding category", e);
            request.setAttribute("error", "An error occurred while adding category");
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
        }
    }
    
    private void updateCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            
            if (idStr == null || idStr.trim().isEmpty() || 
                name == null || name.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin?action=categories");
                return;
            }
            
            Category category = new Category();
            category.setId(Integer.parseInt(idStr));
            category.setName(name.trim());
            category.setDescription(description != null ? description.trim() : null);
            
            if (categoryDAO.updateCategory(category)) {
                logger.info("Category updated successfully: {}", category.getName());
            } else {
                request.setAttribute("error", "Failed to update category");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
            
        } catch (Exception e) {
            logger.error("Error updating category", e);
            request.setAttribute("error", "An error occurred while updating category");
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
        }
    }
    
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        try {
            String idStr = request.getParameter("id");
            
            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);
                if (categoryDAO.deleteCategory(id)) {
                    logger.info("Category deleted successfully: {}", id);
                } else {
                    request.setAttribute("error", "Failed to delete category");
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
            
        } catch (Exception e) {
            logger.error("Error deleting category", e);
            request.setAttribute("error", "An error occurred while deleting category");
            response.sendRedirect(request.getContextPath() + "/admin?action=categories");
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
