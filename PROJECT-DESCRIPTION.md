# 🛒 E-Commerce Web Application

A full-featured e-commerce web application built with Java technologies including JSP, Servlets, JDBC, and MySQL.

## 🌟 Features

### User Features
- ✅ User registration and authentication
- ✅ Product catalog with search and filtering
- ✅ Shopping cart functionality
- ✅ Product details and reviews
- ✅ Order placement and tracking
- ✅ User profile management

### Admin Features
- ✅ Admin dashboard with statistics
- ✅ Product management (CRUD operations)
- ✅ Category management
- ✅ User management
- ✅ Order management
- ✅ Inventory tracking

## 🛠️ Technology Stack

- **Backend**: Java Servlets, JSP
- **Database**: MySQL with JDBC
- **Frontend**: HTML5, CSS3, Bootstrap 5, JavaScript
- **Build Tool**: Maven
- **Server**: Apache Tomcat
- **Security**: BCrypt password hashing
- **Connection Pooling**: HikariCP

## 📁 Project Structure

```
ecommerce-app/
├── src/main/java/com/ecommerce/
│   ├── model/          # Entity classes
│   ├── dao/            # Data Access Objects
│   ├── servlet/        # Controllers
│   └── util/           # Utility classes
├── src/main/webapp/
│   ├── WEB-INF/views/  # JSP pages
│   └── resources/      # Static resources
├── database/
│   └── schema.sql      # Database schema
└── pom.xml             # Maven configuration
```

## 🚀 Quick Start

1. **Clone the repository**
2. **Set up MySQL database** using `database/schema.sql`
3. **Configure database** in `src/main/resources/database.properties`
4. **Build and deploy** using Maven
5. **Access** at `http://localhost:8080/ecommerce-app/`

## 🔐 Default Credentials

- **Admin Panel**: `http://localhost:8080/ecommerce-app/admin`
- **Username**: `admin`
- **Password**: `admin123`

## 📋 Requirements

- Java 8 or higher
- Apache Tomcat 9 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 📄 License

This project is licensed under the MIT License.

## 👥 Contributing
git remote add origin https://github.com/23cse106siddharth-bot/ecommerce-web-application.git

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📞 Support

For support and questions, please create an issue in the repository.
