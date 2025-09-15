<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Checkout - E-Commerce Store"/>
</jsp:include>

<div class="row">
    <div class="col-12">
        <h2><i class="fas fa-credit-card"></i> Checkout</h2>
        
        <c:if test="${error != null}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${cart != null && cart.cartItems != null && !cart.cartItems.isEmpty()}">
            <div class="row">
                <!-- Order Summary -->
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Order Summary</h5>
                        </div>
                        <div class="card-body">
                            <c:forEach var="item" items="${cart.cartItems}">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>${item.product.name} x ${item.quantity}</span>
                                    <span>$<fmt:formatNumber value="${item.totalPrice}" pattern="#,##0.00"/></span>
                                </div>
                            </c:forEach>
                            <hr>
                            <div class="d-flex justify-content-between">
                                <strong>Total:</strong>
                                <strong>
                                    <c:set var="total" value="0"/>
                                    <c:forEach var="item" items="${cart.cartItems}">
                                        <c:set var="total" value="${total + item.totalPrice}"/>
                                    </c:forEach>
                                    $<fmt:formatNumber value="${total}" pattern="#,##0.00"/>
                                </strong>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Checkout Form -->
                <div class="col-md-8">
                    <form method="POST" action="${pageContext.request.contextPath}/checkout">
                        <input type="hidden" name="action" value="placeOrder">
                        
                        <!-- Address Selection -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5>Shipping Address</h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${addresses != null && !addresses.isEmpty()}">
                                        <div class="mb-3">
                                            <label class="form-label">Select Address:</label>
                                            <c:forEach var="address" items="${addresses}">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="shippingAddressId" 
                                                           value="${address.id}" id="address${address.id}" required>
                                                    <label class="form-check-label" for="address${address.id}">
                                                        <strong>${address.type}</strong><br>
                                                        ${address.getFullAddress()}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">No addresses found. Please add an address below.</p>
                                    </c:otherwise>
                                </c:choose>
                                
                                <!-- Add New Address -->
                                <div class="mt-3">
                                    <button type="button" class="btn btn-outline-primary" data-bs-toggle="collapse" data-bs-target="#newAddressForm">
                                        <i class="fas fa-plus"></i> Add New Address
                                    </button>
                                </div>
                                
                                <div class="collapse mt-3" id="newAddressForm">
                                    <div class="card">
                                        <div class="card-body">
                                            <h6>Add New Address</h6>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="type" class="form-label">Type</label>
                                                        <select class="form-select" id="type" name="type" required>
                                                            <option value="shipping">Shipping</option>
                                                            <option value="billing">Billing</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="checkbox" id="isDefault" name="isDefault">
                                                            <label class="form-check-label" for="isDefault">
                                                                Set as default address
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="mb-3">
                                                <label for="street" class="form-label">Street Address</label>
                                                <input type="text" class="form-control" id="street" name="street" required>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="city" class="form-label">City</label>
                                                        <input type="text" class="form-control" id="city" name="city" required>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="state" class="form-label">State</label>
                                                        <input type="text" class="form-control" id="state" name="state" required>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="zipCode" class="form-label">ZIP Code</label>
                                                        <input type="text" class="form-control" id="zipCode" name="zipCode" required>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="country" class="form-label">Country</label>
                                                        <input type="text" class="form-control" id="country" name="country" required>
                                                    </div>
                                                </div>
                                            </div>
                                            <button type="submit" name="action" value="addAddress" class="btn btn-primary">
                                                <i class="fas fa-save"></i> Add Address
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Payment Method -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5>Payment Method</h5>
                            </div>
                            <div class="card-body">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="cashOnDelivery" value="cash_on_delivery" checked>
                                    <label class="form-check-label" for="cashOnDelivery">
                                        <i class="fas fa-money-bill-wave"></i> Cash on Delivery
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="creditCard" value="credit_card" disabled>
                                    <label class="form-check-label" for="creditCard">
                                        <i class="fas fa-credit-card"></i> Credit Card (Coming Soon)
                                    </label>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Place Order Button -->
                        <div class="d-grid">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-shopping-cart"></i> Place Order
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
