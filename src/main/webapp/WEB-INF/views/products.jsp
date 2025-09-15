<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Products - E-Commerce Store"/>
</jsp:include>

<div class="row">
    <!-- Sidebar -->
    <div class="col-md-3">
        <div class="card">
            <div class="card-header">
                <h5>Filters</h5>
            </div>
            <div class="card-body">
                <!-- Search -->
                <form method="GET" action="${pageContext.request.contextPath}/products">
                    <div class="mb-3">
                        <label for="search" class="form-label">Search</label>
                        <input type="text" class="form-control" id="search" name="search" 
                               value="${searchTerm}" placeholder="Search products...">
                    </div>
                    
                    <!-- Categories -->
                    <div class="mb-3">
                        <label class="form-label">Categories</label>
                        <div class="list-group">
                            <a href="${pageContext.request.contextPath}/products" 
                               class="list-group-item list-group-item-action ${selectedCategoryId == null ? 'active' : ''}">
                                All Categories
                            </a>
                            <c:forEach var="category" items="${categories}">
                                <a href="${pageContext.request.contextPath}/products?category=${category.id}" 
                                   class="list-group-item list-group-item-action ${selectedCategoryId == category.id ? 'active' : ''}">
                                    ${category.name}
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <!-- Price Range -->
                    <div class="mb-3">
                        <label class="form-label">Price Range</label>
                        <div class="row">
                            <div class="col-6">
                                <input type="number" class="form-control" name="minPrice" 
                                       placeholder="Min" value="${minPrice}">
                            </div>
                            <div class="col-6">
                                <input type="number" class="form-control" name="maxPrice" 
                                       placeholder="Max" value="${maxPrice}">
                            </div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Products -->
    <div class="col-md-9">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>
                <c:choose>
                    <c:when test="${searchTerm != null}">
                        Search Results for "${searchTerm}"
                    </c:when>
                    <c:when test="${selectedCategoryId != null}">
                        <c:forEach var="category" items="${categories}">
                            <c:if test="${category.id == selectedCategoryId}">
                                ${category.name} Products
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        All Products
                    </c:otherwise>
                </c:choose>
            </h2>
            <span class="text-muted">${products.size()} products found</span>
        </div>
        
        <c:choose>
            <c:when test="${products != null && !products.isEmpty()}">
                <div class="row">
                    <c:forEach var="product" items="${products}">
                        <div class="col-lg-4 col-md-6 mb-4">
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
                                    <p class="card-text text-muted flex-grow-1">
                                        <c:choose>
                                            <c:when test="${product.description.length() > 100}">
                                                ${product.description.substring(0, 100)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${product.description}
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <div class="mt-auto">
                                        <h6 class="text-primary">$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></h6>
                                        <c:if test="${product.category != null}">
                                            <small class="text-muted">${product.category.name}</small>
                                        </c:if>
                                        <div class="d-flex justify-content-between align-items-center mt-2">
                                            <small class="text-muted">
                                                <c:choose>
                                                    <c:when test="${product.stockQuantity > 0}">
                                                        In Stock (${product.stockQuantity})
                                                    </c:when>
                                                    <c:otherwise>
                                                        Out of Stock
                                                    </c:otherwise>
                                                </c:choose>
                                            </small>
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
            </c:when>
            <c:otherwise>
                <div class="text-center py-5">
                    <i class="fas fa-search fa-3x text-muted mb-3"></i>
                    <h4>No products found</h4>
                    <p class="text-muted">Try adjusting your search criteria or browse all products.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">View All Products</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
