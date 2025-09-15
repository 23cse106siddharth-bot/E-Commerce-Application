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
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String categoryIdParam = request.getParameter("category");
            String searchParam = request.getParameter("search");
            String minPriceParam = request.getParameter("minPrice");
            String maxPriceParam = request.getParameter("maxPrice");
            
            List<Product> products;
            List<Category> categories = categoryDAO.getAllCategories();
            
            // Filter products based on parameters
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                products = productDAO.searchProducts(searchParam.trim());
                request.setAttribute("searchTerm", searchParam.trim());
            } else if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(categoryIdParam);
                    products = productDAO.getProductsByCategory(categoryId);
                    request.setAttribute("selectedCategoryId", categoryId);
                } catch (NumberFormatException e) {
                    products = productDAO.getAllProducts();
                }
            } else if (minPriceParam != null && maxPriceParam != null && 
                      !minPriceParam.isEmpty() && !maxPriceParam.isEmpty()) {
                try {
                    BigDecimal minPrice = new BigDecimal(minPriceParam);
                    BigDecimal maxPrice = new BigDecimal(maxPriceParam);
                    products = productDAO.getProductsByPriceRange(minPrice, maxPrice);
                    request.setAttribute("minPrice", minPrice);
                    request.setAttribute("maxPrice", maxPrice);
                } catch (NumberFormatException e) {
                    products = productDAO.getAllProducts();
                }
            } else {
                products = productDAO.getAllProducts();
            }
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error in ProductServlet", e);
            request.setAttribute("error", "An error occurred while loading products");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
