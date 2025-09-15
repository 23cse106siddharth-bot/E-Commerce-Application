<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Shopping Cart - E-Commerce Store"/>
</jsp:include>

<div class="row">
    <div class="col-12">
        <h2><i class="fas fa-shopping-cart"></i> Shopping Cart</h2>
        
        <c:choose>
            <c:when test="${cart != null && cart.cartItems != null && !cart.cartItems.isEmpty()}">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Total</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart.cartItems}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <c:choose>
                                                <c:when test="${item.product.imageUrl != null && !item.product.imageUrl.isEmpty()}">
                                                    <img src="${item.product.imageUrl}" 
                                                         class="me-3" style="width: 60px; height: 60px; object-fit: cover;" 
                                                         alt="${item.product.name}">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="me-3 bg-light d-flex align-items-center justify-content-center" 
                                                         style="width: 60px; height: 60px;">
                                                        <i class="fas fa-image text-muted"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <div>
                                                <h6 class="mb-0">${item.product.name}</h6>
                                                <small class="text-muted">${item.product.description}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>$<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form method="POST" action="${pageContext.request.contextPath}/cart/update" 
                                              class="d-inline">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <div class="input-group" style="width: 120px;">
                                                <input type="number" class="form-control" name="quantity" 
                                                       value="${item.quantity}" min="1" max="${item.product.stockQuantity}">
                                                <button type="submit" class="btn btn-outline-secondary btn-sm">
                                                    <i class="fas fa-sync-alt"></i>
                                                </button>
                                            </div>
                                        </form>
                                    </td>
                                    <td>$<fmt:formatNumber value="${item.totalPrice}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form method="POST" action="${pageContext.request.contextPath}/cart/remove" 
                                              class="d-inline">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                    onclick="return confirm('Remove this item from cart?')">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="row">
                    <div class="col-md-8">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary">
                            <i class="fas fa-arrow-left"></i> Continue Shopping
                        </a>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-body">
                                <h5>Order Summary</h5>
                                <hr>
                                <div class="d-flex justify-content-between">
                                    <span>Subtotal:</span>
                                    <span>
                                        <c:set var="subtotal" value="0"/>
                                        <c:forEach var="item" items="${cart.cartItems}">
                                            <c:set var="subtotal" value="${subtotal + item.totalPrice}"/>
                                        </c:forEach>
                                        $<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/>
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span>Shipping:</span>
                                    <span>Free</span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between">
                                    <strong>Total:</strong>
                                    <strong>$<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></strong>
                                </div>
                                <div class="d-grid mt-3">
                                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg">
                                        <i class="fas fa-credit-card"></i> Proceed to Checkout
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5">
                    <i class="fas fa-shopping-cart fa-4x text-muted mb-3"></i>
                    <h4>Your cart is empty</h4>
                    <p class="text-muted">Add some products to get started!</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                        <i class="fas fa-shopping-bag"></i> Start Shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
