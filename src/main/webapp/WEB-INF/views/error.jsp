<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="pageTitle" value="Error - E-Commerce Store"/>
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-6 text-center">
        <div class="card">
            <div class="card-body py-5">
                <i class="fas fa-exclamation-triangle fa-4x text-warning mb-4"></i>
                <h2>Oops! Something went wrong</h2>
                <p class="text-muted mb-4">
                    <c:choose>
                        <c:when test="${error != null}">
                            ${error}
                        </c:when>
                        <c:otherwise>
                            We're sorry, but something went wrong. Please try again later.
                        </c:otherwise>
                    </c:choose>
                </p>
                <div class="d-grid gap-2 d-md-block">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                        <i class="fas fa-home"></i> Go Home
                    </a>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary">
                        <i class="fas fa-shopping-bag"></i> Browse Products
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
