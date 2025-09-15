    </main>

    <!-- Footer -->
    <footer class="footer mt-5 py-4">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>E-Commerce Store</h5>
                    <p class="text-muted">Your one-stop shop for all your needs.</p>
                </div>
                <div class="col-md-6">
                    <h5>Quick Links</h5>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/user/login" class="text-decoration-none">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/user/register" class="text-decoration-none">Register</a></li>
                    </ul>
                </div>
            </div>
            <hr>
            <div class="text-center text-muted">
                <p>&copy; 2024 E-Commerce Store. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>
