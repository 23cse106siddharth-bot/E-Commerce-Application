<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="${product.name} - E-Commerce Store"/>
</jsp:include>

<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        ${error}
    </div>
</c:if>

<c:if test="${product != null}">
    <div class="row">
        <div class="col-md-6">
            <c:choose>
                <c:when test="${product.imageUrl != null && !product.imageUrl.isEmpty()}">
                    <img src="${product.imageUrl}" class="img-fluid rounded" alt="${product.name}">
                </c:when>
                <c:otherwise>
                    <div class="bg-light d-flex align-items-center justify-content-center" style="height: 400px;">
                        <i class="fas fa-image fa-5x text-muted"></i>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="col-md-6">
            <h1 class="mb-3">${product.name}</h1>
            
            <c:if test="${product.category != null}">
                <p class="text-muted mb-3">
                    <i class="fas fa-tag"></i> ${product.category.name}
                </p>
            </c:if>
            
            <h3 class="text-primary mb-3">$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></h3>
            
            <div class="mb-3">
                <h5>Description</h5>
                <p>${product.description}</p>
            </div>
            
            <div class="mb-3">
                <h5>Availability</h5>
                <c:choose>
                    <c:when test="${product.stockQuantity > 0}">
                        <span class="badge bg-success">In Stock (${product.stockQuantity} available)</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-danger">Out of Stock</span>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <c:if test="${product.stockQuantity > 0}">
                <form method="POST" action="${pageContext.request.contextPath}/cart/add" class="mb-4">
                    <input type="hidden" name="productId" value="${product.id}">
                    <div class="row">
                        <div class="col-md-4">
                            <label for="quantity" class="form-label">Quantity</label>
                            <select class="form-select" id="quantity" name="quantity" required>
                                <c:forEach begin="1" end="${product.stockQuantity > 10 ? 10 : product.stockQuantity}" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mt-3">
                        <c:choose>
                            <c:when test="${sessionScope.user != null}">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-cart-plus"></i> Add to Cart
                                </button>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/user/login" class="btn btn-primary btn-lg">
                                    <i class="fas fa-sign-in-alt"></i> Login to Add to Cart
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </form>
            </c:if>
            
            <div class="mt-4">
                <h5>Product Information</h5>
                <table class="table table-sm">
                    <tr>
                        <td><strong>Product ID:</strong></td>
                        <td>${product.id}</td>
                    </tr>
                    <tr>
                        <td><strong>Price:</strong></td>
                        <td>$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></td>
                    </tr>
                    <tr>
                        <td><strong>Stock:</strong></td>
                        <td>${product.stockQuantity} units</td>
                    </tr>
                    <c:if test="${product.category != null}">
                        <tr>
                            <td><strong>Category:</strong></td>
                            <td>${product.category.name}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
    
    <!-- Related Products Section (if needed) -->
    <div class="row mt-5">
        <div class="col-12">
            <h3>You might also like</h3>
            <p class="text-muted">Discover more products in the same category</p>
            <a href="${pageContext.request.contextPath}/products?category=${product.categoryId}" 
               class="btn btn-outline-primary">View More in ${product.category.name}</a>
        </div>
    </div>
</c:if>

<jsp:include page="common/footer.jsp"/>
