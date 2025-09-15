package com.ecommerce.servlet;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
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

@WebServlet({"/cart", "/cart/add", "/cart/update", "/cart/remove"})
public class CartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);
    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            Cart cart = cartDAO.getCartByUserId(user.getId());
            
            if (cart == null) {
                cart = cartDAO.createCart(user.getId());
            }
            
            if (cart != null) {
                cart.setCartItems(cartDAO.getCartItemsByCartId(cart.getId()));
            }
            
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error in CartServlet GET", e);
            request.setAttribute("error", "An error occurred while loading cart");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        String path = request.getServletPath();
        User user = (User) session.getAttribute("user");
        
        try {
            switch (path) {
                case "/cart/add":
                    addToCart(request, response, user);
                    break;
                case "/cart/update":
                    updateCartItem(request, response, user);
                    break;
                case "/cart/remove":
                    removeFromCart(request, response, user);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            logger.error("Error in CartServlet POST", e);
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    
    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            String quantityParam = request.getParameter("quantity");
            
            if (productIdParam == null || quantityParam == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            
            if (quantity <= 0) {
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }
            
            Product product = productDAO.getProductById(productId);
            if (product == null || !"active".equals(product.getStatus())) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }
            
            if (product.getStockQuantity() < quantity) {
                request.setAttribute("error", "Insufficient stock. Available: " + product.getStockQuantity());
                request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(request, response);
                return;
            }
            
            Cart cart = cartDAO.getCartByUserId(user.getId());
            if (cart == null) {
                cart = cartDAO.createCart(user.getId());
            }
            
            if (cart != null) {
                cartDAO.addItemToCart(cart.getId(), productId, quantity);
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
    
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            String quantityParam = request.getParameter("quantity");
            
            if (productIdParam == null || quantityParam == null) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            
            Cart cart = cartDAO.getCartByUserId(user.getId());
            if (cart != null) {
                cartDAO.updateCartItemQuantity(cart.getId(), productId, quantity);
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            
            if (productIdParam == null) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            
            Cart cart = cartDAO.getCartByUserId(user.getId());
            if (cart != null) {
                cartDAO.removeItemFromCart(cart.getId(), productId);
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
