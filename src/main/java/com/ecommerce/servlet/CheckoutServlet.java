package com.ecommerce.servlet;

import com.ecommerce.dao.AddressDAO;
import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Address;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
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
import java.util.List;
import java.util.UUID;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    private final CartDAO cartDAO = new CartDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final OrderDAO orderDAO = new OrderDAO();
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
            
            if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Get user addresses
            List<Address> addresses = addressDAO.getAddressesByUserId(user.getId());
            
            request.setAttribute("cart", cart);
            request.setAttribute("addresses", addresses);
            
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error in CheckoutServlet GET", e);
            request.setAttribute("error", "An error occurred while loading checkout page");
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
        
        try {
            User user = (User) session.getAttribute("user");
            String action = request.getParameter("action");
            
            if ("addAddress".equals(action)) {
                addAddress(request, response, user);
            } else if ("placeOrder".equals(action)) {
                placeOrder(request, response, user);
            } else {
                response.sendRedirect(request.getContextPath() + "/checkout");
            }
            
        } catch (Exception e) {
            logger.error("Error in CheckoutServlet POST", e);
            request.setAttribute("error", "An error occurred during checkout");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    private void addAddress(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            String type = request.getParameter("type");
            String street = request.getParameter("street");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zipCode = request.getParameter("zipCode");
            String country = request.getParameter("country");
            boolean isDefault = "on".equals(request.getParameter("isDefault"));
            
            if (street == null || street.trim().isEmpty() ||
                city == null || city.trim().isEmpty() ||
                state == null || state.trim().isEmpty() ||
                zipCode == null || zipCode.trim().isEmpty() ||
                country == null || country.trim().isEmpty()) {
                
                request.setAttribute("error", "All address fields are required");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }
            
            Address address = new Address();
            address.setUserId(user.getId());
            address.setType(type);
            address.setStreet(street.trim());
            address.setCity(city.trim());
            address.setState(state.trim());
            address.setZipCode(zipCode.trim());
            address.setCountry(country.trim());
            address.setDefault(isDefault);
            
            if (addressDAO.createAddress(address)) {
                logger.info("Address created successfully for user: {}", user.getId());
            } else {
                request.setAttribute("error", "Failed to create address");
            }
            
            response.sendRedirect(request.getContextPath() + "/checkout");
            
        } catch (Exception e) {
            logger.error("Error adding address", e);
            request.setAttribute("error", "An error occurred while adding address");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
    
    private void placeOrder(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            String shippingAddressIdStr = request.getParameter("shippingAddressId");
            String billingAddressIdStr = request.getParameter("billingAddressId");
            
            if (shippingAddressIdStr == null || shippingAddressIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Please select a shipping address");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }
            
            Cart cart = cartDAO.getCartByUserId(user.getId());
            if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Calculate total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem item : cart.getCartItems()) {
                totalAmount = totalAmount.add(item.getTotalPrice());
            }
            
            // Generate order number
            String orderNumber = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Create order
            Order order = new Order();
            order.setUserId(user.getId());
            order.setOrderNumber(orderNumber);
            order.setTotalAmount(totalAmount);
            order.setShippingAddressId(Integer.parseInt(shippingAddressIdStr));
            order.setBillingAddressId(billingAddressIdStr != null && !billingAddressIdStr.trim().isEmpty() ? 
                                    Integer.parseInt(billingAddressIdStr) : Integer.parseInt(shippingAddressIdStr));
            order.setStatus("pending");
            
            if (orderDAO.createOrder(order)) {
                // Create order items
                for (CartItem cartItem : cart.getCartItems()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(order.getId());
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    
                    orderDAO.createOrderItem(orderItem);
                    
                    // Update product stock
                    productDAO.updateStock(cartItem.getProductId(), cartItem.getQuantity());
                }
                
                // Clear cart
                cartDAO.clearCart(cart.getId());
                
                logger.info("Order created successfully: {}", orderNumber);
                response.sendRedirect(request.getContextPath() + "/order/success?orderNumber=" + orderNumber);
            } else {
                request.setAttribute("error", "Failed to create order");
                response.sendRedirect(request.getContextPath() + "/checkout");
            }
            
        } catch (Exception e) {
            logger.error("Error placing order", e);
            request.setAttribute("error", "An error occurred while placing order");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
