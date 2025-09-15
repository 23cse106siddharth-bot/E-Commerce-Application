<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Admin Panel</title>
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=dashboard">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=products">
                        <i class="fas fa-box"></i> Products
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=categories">
                        <i class="fas fa-tags"></i> Categories
                    </a>
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin?action=users">
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
                        <h2>Manage Users</h2>
                        <span class="text-muted">${users.size()} users registered</span>
                    </div>
                    
                    <c:if test="${error != null}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <!-- Users Table -->
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Phone</th>
                                            <th>Registered</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${users}">
                                            <tr>
                                                <td>${user.id}</td>
                                                <td>${user.name}</td>
                                                <td>${user.email}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${user.phone != null && !user.phone.isEmpty()}">
                                                            ${user.phone}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Not provided</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${user.createdAt}" pattern="MMM dd, yyyy"/>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-sm btn-outline-info" 
                                                            onclick="viewUser(${user.id}, '${user.name}', '${user.email}', '${user.phone}', '${user.createdAt}')">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-outline-danger" 
                                                            onclick="deleteUser(${user.id})">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- View User Modal -->
    <div class="modal fade" id="viewUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">User Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-borderless">
                        <tr>
                            <td><strong>ID:</strong></td>
                            <td id="viewId"></td>
                        </tr>
                        <tr>
                            <td><strong>Name:</strong></td>
                            <td id="viewName"></td>
                        </tr>
                        <tr>
                            <td><strong>Email:</strong></td>
                            <td id="viewEmail"></td>
                        </tr>
                        <tr>
                            <td><strong>Phone:</strong></td>
                            <td id="viewPhone"></td>
                        </tr>
                        <tr>
                            <td><strong>Registered:</strong></td>
                            <td id="viewCreated"></td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete User Modal -->
    <div class="modal fade" id="deleteUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Delete User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete this user? This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form method="POST" action="${pageContext.request.contextPath}/admin" class="d-inline">
                        <input type="hidden" name="action" value="deleteUser">
                        <input type="hidden" name="id" id="deleteId">
                        <button type="submit" class="btn btn-danger">Delete User</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function viewUser(id, name, email, phone, created) {
            document.getElementById('viewId').textContent = id;
            document.getElementById('viewName').textContent = name;
            document.getElementById('viewEmail').textContent = email;
            document.getElementById('viewPhone').textContent = phone || 'Not provided';
            document.getElementById('viewCreated').textContent = created;
            
            new bootstrap.Modal(document.getElementById('viewUserModal')).show();
        }
        
        function deleteUser(id) {
            document.getElementById('deleteId').value = id;
            new bootstrap.Modal(document.getElementById('deleteUserModal')).show();
        }
    </script>
</body>
</html>
