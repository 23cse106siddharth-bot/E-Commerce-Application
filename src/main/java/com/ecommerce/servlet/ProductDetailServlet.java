package com.ecommerce.servlet;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailServlet.class);
    private final ProductDAO productDAO = new ProductDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("id");
            
            if (productIdParam == null || productIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }
            
            try {
                int productId = Integer.parseInt(productIdParam);
                Product product = productDAO.getProductById(productId);
                
                if (product == null) {
                    request.setAttribute("error", "Product not found");
                    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                    return;
                }
                
                request.setAttribute("product", product);
                request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/products");
            }
            
        } catch (Exception e) {
            logger.error("Error in ProductDetailServlet", e);
            request.setAttribute("error", "An error occurred while loading product details");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
