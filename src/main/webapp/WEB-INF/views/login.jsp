<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Login - E-Commerce Store"/>
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-6 col-lg-4">
        <div class="card">
            <div class="card-header text-center">
                <h4><i class="fas fa-sign-in-alt"></i> Login</h4>
            </div>
            <div class="card-body">
                <c:if test="${error != null}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                
                <c:if test="${success != null}">
                    <div class="alert alert-success" role="alert">
                        ${success}
                    </div>
                </c:if>
                
                <form method="POST" action="${pageContext.request.contextPath}/user/login">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email Address</label>
                        <input type="email" class="form-control" id="email" name="email" 
                               value="${param.email}" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    
                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">Login</button>
                    </div>
                </form>
                
                <hr>
                
                <div class="text-center">
                    <p class="mb-0">Don't have an account?</p>
                    <a href="${pageContext.request.contextPath}/user/register" class="btn btn-outline-primary">
                        Create Account
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
