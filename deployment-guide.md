# E-Commerce Application Deployment Guide

## Prerequisites

Before deploying the application, ensure you have the following installed:

1. **Java Development Kit (JDK) 8 or higher**
   - Download from: https://adoptium.net/
   - Verify installation: `java -version`

2. **Apache Tomcat 9 or higher**
   - Download from: https://tomcat.apache.org/
   - Extract to a directory (e.g., `C:\apache-tomcat-9.0.xx`)

3. **MySQL 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Install and start MySQL service

4. **Apache NetBeans (Recommended IDE)**
   - Download from: https://netbeans.apache.org/
   - Or use any Java IDE (Eclipse, IntelliJ IDEA)

## Step-by-Step Deployment

### 1. Database Setup

1. **Start MySQL Service**
   ```bash
   # Windows (as Administrator)
   net start mysql
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. **Create Database**
   ```sql
   mysql -u root -p
   CREATE DATABASE ecommerce_db;
   USE ecommerce_db;
   ```

3. **Run Schema Script**
   ```bash
   mysql -u root -p ecommerce_db < database/schema.sql
   ```

4. **Verify Tables Created**
   ```sql
   SHOW TABLES;
   SELECT * FROM users LIMIT 1;
   SELECT * FROM products LIMIT 1;
   SELECT * FROM categories LIMIT 1;
   ```

### 2. Configure Database Connection

1. **Edit Database Properties**
   - Open `src/main/resources/database.properties`
   - Update the following values:
   ```properties
   db.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=your_mysql_username
   db.password=your_mysql_password
   ```

2. **Test Connection**
   - Ensure MySQL is running
   - Verify credentials are correct

### 3. Build the Application

#### Option A: Using Apache NetBeans

1. **Open Project**
   - Launch Apache NetBeans
   - File → Open Project
   - Select the project folder

2. **Clean and Build**
   - Right-click on project
   - Select "Clean and Build"
   - Wait for build to complete

3. **Deploy to Tomcat**
   - Right-click on project
   - Select "Deploy"
   - Choose your Tomcat server

#### Option B: Using Command Line

1. **Navigate to Project Directory**
   ```bash
   cd path/to/ecommerce-app
   ```

2. **Build with Maven**
   ```bash
   mvn clean package
   ```

3. **Deploy WAR File**
   - Copy `target/ecommerce-app.war` to Tomcat's `webapps` directory
   - Start Tomcat server

### 4. Configure Tomcat Server

1. **Set JAVA_HOME**
   - Windows: Add environment variable `JAVA_HOME` pointing to JDK installation
   - Linux/Mac: Add to `~/.bashrc` or `~/.zshrc`:
     ```bash
     export JAVA_HOME=/path/to/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```

2. **Configure Tomcat**
   - Edit `conf/server.xml` if needed
   - Ensure port 8080 is available (or change if needed)

3. **Start Tomcat**
   ```bash
   # Windows
   bin\startup.bat
   
   # Linux/Mac
   bin/startup.sh
   ```

### 5. Access the Application

1. **User Interface**
   - Open browser
   - Navigate to: `http://localhost:8080/ecommerce-app/`

2. **Admin Panel**
   - Navigate to: `http://localhost:8080/ecommerce-app/admin`
   - Login with:
     - Username: `admin`
     - Password: `admin123`

### 6. Verify Installation

1. **Test User Registration**
   - Go to user registration page
   - Create a test account
   - Verify account is created

2. **Test Product Browsing**
   - Browse products
   - Test search functionality
   - View product details

3. **Test Admin Functions**
   - Login to admin panel
   - Add/edit products
   - Manage categories
   - View users

## Troubleshooting

### Common Issues and Solutions

#### 1. Database Connection Error
**Error**: `java.sql.SQLException: Access denied for user`

**Solution**:
- Verify MySQL credentials in `database.properties`
- Ensure MySQL service is running
- Check if user has proper permissions:
  ```sql
  GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'your_username'@'localhost';
  FLUSH PRIVILEGES;
  ```

#### 2. Build Errors
**Error**: `Maven build failed`

**Solution**:
- Check Java version: `java -version`
- Clean Maven cache: `mvn clean`
- Update Maven dependencies: `mvn dependency:resolve`

#### 3. Deployment Issues
**Error**: `Application failed to start`

**Solution**:
- Check Tomcat logs in `logs/catalina.out`
- Verify WAR file is in `webapps` directory
- Ensure no port conflicts

#### 4. JSP Compilation Errors
**Error**: `JSP compilation failed`

**Solution**:
- Check JSP syntax
- Verify all required JAR files are present
- Check Tomcat version compatibility

### Log Files

1. **Application Logs**
   - Location: `logs/application.log`
   - Contains: Application-specific errors and info

2. **Tomcat Logs**
   - Location: `logs/catalina.out`
   - Contains: Server startup and deployment logs

3. **Database Logs**
   - Location: MySQL error log
   - Contains: Database connection and query errors

## Performance Optimization

### 1. Database Optimization
- Add indexes for frequently queried columns
- Optimize connection pool settings
- Regular database maintenance

### 2. Application Optimization
- Enable GZIP compression
- Optimize images and static resources
- Configure caching headers

### 3. Server Optimization
- Increase Tomcat memory settings
- Configure connection pooling
- Enable HTTP/2 if supported

## Security Considerations

### 1. Database Security
- Use strong passwords
- Limit database user permissions
- Enable SSL connections

### 2. Application Security
- Change default admin credentials
- Enable HTTPS in production
- Regular security updates

### 3. Server Security
- Keep Tomcat updated
- Configure firewall rules
- Regular security audits

## Production Deployment

### 1. Environment Setup
- Use production database
- Configure proper logging
- Set up monitoring

### 2. Load Balancing
- Configure multiple Tomcat instances
- Use reverse proxy (Apache/Nginx)
- Implement session clustering

### 3. Backup Strategy
- Regular database backups
- Application file backups
- Disaster recovery plan

## Support and Maintenance

### 1. Regular Maintenance
- Monitor application logs
- Check database performance
- Update dependencies

### 2. Backup Procedures
- Daily database backups
- Weekly application backups
- Test restore procedures

### 3. Monitoring
- Set up application monitoring
- Monitor server resources
- Track user activity

For additional support, refer to the main README.md file or contact the development team.
