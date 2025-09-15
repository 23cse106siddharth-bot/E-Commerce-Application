<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Order History - E-Commerce Store"/>
</jsp:include>

<div class="row">
    <div class="col-12">
        <h2><i class="fas fa-history"></i> Order History</h2>
        
        <c:choose>
            <c:when test="${orders != null && !orders.isEmpty()}">
                <c:forEach var="order" items="${orders}">
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="mb-0">Order #${order.orderNumber}</h5>
                                <small class="text-muted">
                                    <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                                </small>
                            </div>
                            <div>
                                <span class="badge 
                                    <c:choose>
                                        <c:when test="${order.status == 'pending'}">bg-warning</c:when>
                                        <c:when test="${order.status == 'processing'}">bg-info</c:when>
                                        <c:when test="${order.status == 'shipped'}">bg-primary</c:when>
                                        <c:when test="${order.status == 'delivered'}">bg-success</c:when>
                                        <c:when test="${order.status == 'cancelled'}">bg-danger</c:when>
                                        <c:otherwise>bg-secondary</c:otherwise>
                                    </c:choose>
                                ">${order.status}</span>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-8">
                                    <h6>Order Items:</h6>
                                    <div class="table-responsive">
                                        <table class="table table-sm">
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
                                                                             class="me-2" style="width: 40px; height: 40px; object-fit: cover;" 
                                                                             alt="${item.product.name}">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <div class="me-2 bg-light d-flex align-items-center justify-content-center" 
                                                                             style="width: 40px; height: 40px;">
                                                                            <i class="fas fa-image text-muted"></i>
                                                                        </div>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <span>${item.product.name}</span>
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
                                <div class="col-md-4">
                                    <div class="text-end">
                                        <h5 class="text-primary">$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></h5>
                                        <p class="text-muted mb-0">Total Amount</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5">
                    <i class="fas fa-shopping-bag fa-4x text-muted mb-3"></i>
                    <h4>No Orders Found</h4>
                    <p class="text-muted">You haven't placed any orders yet.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                        <i class="fas fa-shopping-bag"></i> Start Shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
