package com.ecommerce.servlet;

import com.ecommerce.dao.OrderDAO;
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
import java.util.List;

@WebServlet({"/order/place", "/order/success", "/order/history"})
public class OrderServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
    private final OrderDAO orderDAO = new OrderDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/order/success":
                showOrderSuccess(request, response);
                break;
            case "/order/history":
                showOrderHistory(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/order/place".equals(path)) {
            // This is handled by CheckoutServlet
            response.sendRedirect(request.getContextPath() + "/checkout");
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    private void showOrderSuccess(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String orderNumber = request.getParameter("orderNumber");
            
            if (orderNumber == null || orderNumber.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            Order order = orderDAO.getOrderByOrderNumber(orderNumber);
            
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }
            
            // Get order items
            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
            order.setOrderItems(orderItems);
            
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error showing order success", e);
            request.setAttribute("error", "An error occurred while loading order details");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
            
            // Get order items for each order
            for (Order order : orders) {
                List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
                order.setOrderItems(orderItems);
            }
            
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/views/order-history.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error showing order history", e);
            request.setAttribute("error", "An error occurred while loading order history");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
