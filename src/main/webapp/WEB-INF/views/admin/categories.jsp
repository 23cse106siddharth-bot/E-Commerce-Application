<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - Admin Panel</title>
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin?action=categories">
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
                        <h2>Manage Categories</h2>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                            <i class="fas fa-plus"></i> Add Category
                        </button>
                    </div>
                    
                    <c:if test="${error != null}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <!-- Categories Table -->
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Description</th>
                                            <th>Created</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="category" items="${categories}">
                                            <tr>
                                                <td>${category.id}</td>
                                                <td>${category.name}</td>
                                                <td>${category.description}</td>
                                                <td>${category.createdAt}</td>
                                                <td>
                                                    <button type="button" class="btn btn-sm btn-outline-primary" 
                                                            onclick="editCategory(${category.id}, '${category.name}', '${category.description}')">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-outline-danger" 
                                                            onclick="deleteCategory(${category.id})">
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

    <!-- Add Category Modal -->
    <div class="modal fade" id="addCategoryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="addCategory">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="name" class="form-label">Category Name</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add Category</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Category Modal -->
    <div class="modal fade" id="editCategoryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="updateCategory">
                    <input type="hidden" name="id" id="editId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="editName" class="form-label">Category Name</label>
                            <input type="text" class="form-control" id="editName" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="editDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="editDescription" name="description" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update Category</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Category Modal -->
    <div class="modal fade" id="deleteCategoryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Delete Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete this category? This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form method="POST" action="${pageContext.request.contextPath}/admin" class="d-inline">
                        <input type="hidden" name="action" value="deleteCategory">
                        <input type="hidden" name="id" id="deleteId">
                        <button type="submit" class="btn btn-danger">Delete Category</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function editCategory(id, name, description) {
            document.getElementById('editId').value = id;
            document.getElementById('editName').value = name;
            document.getElementById('editDescription').value = description;
            
            new bootstrap.Modal(document.getElementById('editCategoryModal')).show();
        }
        
        function deleteCategory(id) {
            document.getElementById('deleteId').value = id;
            new bootstrap.Modal(document.getElementById('deleteCategoryModal')).show();
        }
    </script>
</body>
</html>
