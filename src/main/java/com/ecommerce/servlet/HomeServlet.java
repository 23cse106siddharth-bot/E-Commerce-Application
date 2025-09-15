package com.ecommerce.servlet;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get featured products (first 8 products)
            List<Product> featuredProducts = productDAO.getAllProducts();
            if (featuredProducts.size() > 8) {
                featuredProducts = featuredProducts.subList(0, 8);
            }
            
            // Get all categories
            List<Category> categories = categoryDAO.getAllCategories();
            
            request.setAttribute("featuredProducts", featuredProducts);
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error in HomeServlet", e);
            request.setAttribute("error", "An error occurred while loading the home page");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
