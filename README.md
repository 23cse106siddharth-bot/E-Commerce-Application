# 🛒 E-Commerce Web Application

A full-featured e-commerce web application built with JSP, Java Servlets, JDBC, and MySQL.

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-green.svg)](https://www.mysql.com/)
[![Tomcat](https://img.shields.io/badge/Tomcat-9+-red.svg)](https://tomcat.apache.org/)

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

## Features

### User Features
- User registration and login
- Product browsing with search and filtering
- Shopping cart functionality
- Product details and reviews
- Order placement and tracking
- User profile management

### Admin Features
- Admin dashboard with statistics
- Product management (CRUD operations)
- Category management
- User management
- Order management
- Inventory tracking

## Technology Stack

- **Backend**: Java Servlets, JSP
- **Database**: MySQL with JDBC
- **Frontend**: HTML5, CSS3, Bootstrap 5, JavaScript
- **Build Tool**: Maven
- **Server**: Apache Tomcat
- **Connection Pooling**: HikariCP
- **Security**: BCrypt password hashing

## Prerequisites

- Java 8 or higher
- Apache Tomcat 9 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Apache NetBeans (recommended IDE)

## Installation & Setup

### 1. Database Setup with MySQL Workbench

#### Option A: Using MySQL Workbench (Recommended)
1. **Open MySQL Workbench** and connect to your MySQL server
2. **Run the setup script**:
   - Open `database/mysql-workbench-setup.sql` in MySQL Workbench
   - Execute the entire script (Ctrl+Shift+Enter)
   - Verify all tables are created successfully

#### Option B: Using Command Line
1. Create a MySQL database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Run the schema script:
```bash
mysql -u root -p ecommerce_db < database/schema.sql
```

### 2. Configure Database Connection

Update database credentials in `src/main/resources/database.properties`:
```properties
# For MySQL Workbench users
db.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=your_mysql_username
db.password=your_mysql_password
```

**Default Admin Credentials:**
- Username: `admin`
- Password: `admin123`

### 2. Build and Deploy

1. Clone or download the project
2. Open in Apache NetBeans
3. Right-click on the project and select "Clean and Build"
4. Deploy to Apache Tomcat server

### 3. Access the Application

- **User Interface**: `http://localhost:8080/ecommerce-app/`
- **Admin Panel**: `http://localhost:8080/ecommerce-app/admin`

### 4. Default Admin Credentials

- **Username**: admin
- **Password**: admin123

## Project Structure

```
ecommerce-app/
├── src/main/java/com/ecommerce/
│   ├── model/          # Entity classes
│   ├── dao/            # Data Access Objects
│   ├── servlet/        # Servlet controllers
│   └── util/           # Utility classes
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── views/      # JSP pages
│   │   └── web.xml     # Web configuration
│   └── resources/      # Static resources
├── database/
│   └── schema.sql      # Database schema
├── pom.xml             # Maven configuration
└── README.md
```

## Key Features Implemented

### Security
- Password hashing with BCrypt
- SQL injection prevention
- Session management
- Input validation

### Database Design
- Normalized database schema
- Foreign key relationships
- Indexes for performance
- Connection pooling

### User Experience
- Responsive design with Bootstrap
- Intuitive navigation
- Shopping cart functionality
- Product search and filtering

### Admin Panel
- Complete CRUD operations
- Dashboard with statistics
- Product and category management
- User management

## API Endpoints

### User Endpoints
- `GET /` - Home page
- `GET /products` - Product listing
- `GET /product?id={id}` - Product details
- `GET /cart` - Shopping cart
- `POST /cart/add` - Add to cart
- `POST /cart/update` - Update cart item
- `POST /cart/remove` - Remove from cart
- `GET /user/login` - Login page
- `POST /user/login` - User login
- `GET /user/register` - Registration page
- `POST /user/register` - User registration
- `GET /user/logout` - User logout

### Admin Endpoints
- `GET /admin` - Admin login
- `POST /admin` - Admin login
- `GET /admin?action=dashboard` - Admin dashboard
- `GET /admin?action=products` - Product management
- `GET /admin?action=categories` - Category management
- `GET /admin?action=users` - User management

## Configuration

### Database Configuration
Edit `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/ecommerce_db
db.username=your_username
db.password=your_password
```

### Tomcat Configuration
1. Deploy the WAR file to Tomcat's webapps directory
2. Start Tomcat server
3. Access the application via browser

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials
   - Ensure database exists

2. **Build Errors**
   - Check Java version compatibility
   - Verify Maven dependencies
   - Clean and rebuild project

3. **Deployment Issues**
   - Check Tomcat server status
   - Verify WAR file deployment
   - Check server logs

## Future Enhancements

- Payment gateway integration
- Order management system
- Email notifications
- Advanced search and filtering
- Product reviews and ratings
- Multi-language support
- Mobile app integration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact the development team or create an issue in the repository.
