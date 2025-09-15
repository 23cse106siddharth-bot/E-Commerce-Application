<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - E-Commerce Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background-color: #343a40;
        }
        .sidebar .nav-link {
            color: #adb5bd;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            color: #fff;
            background-color: #495057;
        }
        .main-content {
            background-color: #f8f9fa;
            min-height: 100vh;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar p-0">
                <div class="p-3">
                    <h5 class="text-white">Admin Panel</h5>
                </div>
                <nav class="nav flex-column">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin?action=dashboard">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=products">
                        <i class="fas fa-box"></i> Products
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=categories">
                        <i class="fas fa-tags"></i> Categories
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=users">
                        <i class="fas fa-users"></i> Users
                    </a>
                    <hr class="text-white">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=logout">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/">
                        <i class="fas fa-store"></i> View Store
                    </a>
                </nav>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 main-content">
                <div class="p-4">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2>Dashboard</h2>
                        <span class="text-muted">Welcome, ${sessionScope.adminUsername}</span>
                    </div>
                    
                    <c:if test="${error != null}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <!-- Statistics Cards -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="card bg-primary text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h4>${productCount}</h4>
                                            <p class="mb-0">Products</p>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-box fa-2x"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card bg-success text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h4>${categoryCount}</h4>
                                            <p class="mb-0">Categories</p>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-tags fa-2x"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card bg-info text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h4>${userCount}</h4>
                                            <p class="mb-0">Users</p>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-users fa-2x"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card bg-warning text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h4>0</h4>
                                            <p class="mb-0">Orders</p>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-shopping-cart fa-2x"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Recent Products -->
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Recent Products</h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${products != null && !products.isEmpty()}">
                                            <div class="table-responsive">
                                                <table class="table table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th>Name</th>
                                                            <th>Price</th>
                                                            <th>Stock</th>
                                                            <th>Status</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="product" items="${products}">
                                                            <tr>
                                                                <td>${product.name}</td>
                                                                <td>$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></td>
                                                                <td>${product.stockQuantity}</td>
                                                                <td>
                                                                    <span class="badge bg-${product.status == 'active' ? 'success' : 'secondary'}">
                                                                        ${product.status}
                                                                    </span>
                                                                </td>
                                                                <td>
                                                                    <a href="${pageContext.request.contextPath}/admin?action=products" 
                                                                       class="btn btn-sm btn-outline-primary">Manage</a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No products found.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
