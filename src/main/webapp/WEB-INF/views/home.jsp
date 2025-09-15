<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Home - E-Commerce Store"/>
</jsp:include>

<!-- Hero Section -->
<div class="hero-section bg-primary text-white py-5 mb-5">
    <div class="container text-center">
        <h1 class="display-4 fw-bold">Welcome to E-Commerce Store</h1>
        <p class="lead">Discover amazing products at great prices</p>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-light btn-lg">Shop Now</a>
    </div>
</div>

<!-- Categories Section -->
<c:if test="${categories != null && !categories.isEmpty()}">
    <div class="row mb-5">
        <div class="col-12">
            <h2 class="text-center mb-4">Shop by Category</h2>
            <div class="row">
                <c:forEach var="category" items="${categories}">
                    <div class="col-md-3 mb-3">
                        <div class="card h-100 text-center">
                            <div class="card-body">
                                <h5 class="card-title">${category.name}</h5>
                                <p class="card-text text-muted">${category.description}</p>
                                <a href="${pageContext.request.contextPath}/products?category=${category.id}" 
                                   class="btn btn-outline-primary">Browse Products</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>

<!-- Featured Products Section -->
<c:if test="${featuredProducts != null && !featuredProducts.isEmpty()}">
    <div class="row">
        <div class="col-12">
            <h2 class="text-center mb-4">Featured Products</h2>
            <div class="row">
                <c:forEach var="product" items="${featuredProducts}">
                    <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                        <div class="card product-card h-100">
                            <c:choose>
                                <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                                    <img src="${product.imageUrl}" class="card-img-top product-image" alt="${product.name}">
                                </c:when>
                                <c:otherwise>
                                    <div class="card-img-top product-image bg-light d-flex align-items-center justify-content-center">
                                        <i class="fas fa-image fa-3x text-muted"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text text-muted flex-grow-1">${product.description}</p>
                                <div class="mt-auto">
                                    <h6 class="text-primary">$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></h6>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <small class="text-muted">Stock: ${product.stockQuantity}</small>
                                        <div>
                                            <a href="${pageContext.request.contextPath}/product?id=${product.id}" 
                                               class="btn btn-sm btn-outline-primary">View Details</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">View All Products</a>
            </div>
        </div>
    </div>
</c:if>

<!-- Features Section -->
<div class="row mt-5">
    <div class="col-12">
        <h2 class="text-center mb-4">Why Choose Us?</h2>
        <div class="row">
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <i class="fas fa-shipping-fast fa-3x text-primary mb-3"></i>
                        <h5>Fast Shipping</h5>
                        <p class="text-muted">Quick and reliable delivery to your doorstep</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <i class="fas fa-shield-alt fa-3x text-primary mb-3"></i>
                        <h5>Secure Payment</h5>
                        <p class="text-muted">Safe and secure payment processing</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <i class="fas fa-headset fa-3x text-primary mb-3"></i>
                        <h5>24/7 Support</h5>
                        <p class="text-muted">Round-the-clock customer support</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
