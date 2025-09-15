<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Order Success - E-Commerce Store"/>
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="text-center">
            <div class="mb-4">
                <i class="fas fa-check-circle fa-5x text-success"></i>
            </div>
            <h2 class="text-success mb-3">Order Placed Successfully!</h2>
            <p class="lead">Thank you for your purchase. Your order has been confirmed.</p>
        </div>
        
        <c:if test="${order != null}">
            <div class="card">
                <div class="card-header">
                    <h5>Order Details</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Order Number:</strong> ${order.orderNumber}</p>
                            <p><strong>Order Date:</strong> 
                                <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                            </p>
                            <p><strong>Status:</strong> 
                                <span class="badge bg-warning">${order.status}</span>
                            </p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Total Amount:</strong> 
                                <span class="h5 text-primary">$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Order Items -->
            <div class="card mt-4">
                <div class="card-header">
                    <h5>Order Items</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.orderItems}">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <c:choose>
                                                    <c:when test="${item.product.imageUrl != null && !item.product.imageUrl.isEmpty()}">
                                                        <img src="${item.product.imageUrl}" 
                                                             class="me-3" style="width: 50px; height: 50px; object-fit: cover;" 
                                                             alt="${item.product.name}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="me-3 bg-light d-flex align-items-center justify-content-center" 
                                                             style="width: 50px; height: 50px;">
                                                            <i class="fas fa-image text-muted"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div>
                                                    <h6 class="mb-0">${item.product.name}</h6>
                                                </div>
                                            </div>
                                        </td>
                                        <td>${item.quantity}</td>
                                        <td>$<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                        <td>$<fmt:formatNumber value="${item.totalPrice}" pattern="#,##0.00"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
        
        <div class="text-center mt-4">
            <div class="d-grid gap-2 d-md-block">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home"></i> Continue Shopping
                </a>
                <a href="${pageContext.request.contextPath}/order/history" class="btn btn-outline-primary">
                    <i class="fas fa-history"></i> View Order History
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
