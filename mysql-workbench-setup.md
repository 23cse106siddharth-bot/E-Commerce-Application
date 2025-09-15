# MySQL Workbench Setup Guide for E-Commerce Application

## Prerequisites
- MySQL Server 8.0+ installed and running
- MySQL Workbench installed
- Java application ready to deploy

## Step-by-Step Database Setup

### 1. **Open MySQL Workbench**
- Launch MySQL Workbench
- Connect to your local MySQL server (usually localhost:3306)
- Use your root credentials or create a new user

### 2. **Create Database**
```sql
-- Run this in MySQL Workbench Query Editor
CREATE DATABASE IF NOT EXISTS ecommerce_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Select the database
USE ecommerce_db;
```

### 3. **Run the Complete Schema**
Copy and paste the entire content from `database/schema.sql` into MySQL Workbench Query Editor and execute it.

### 4. **Verify Database Creation**
```sql
-- Check if all tables are created
SHOW TABLES;

-- Verify sample data
SELECT * FROM categories;
SELECT * FROM products;
SELECT * FROM admins;
```

### 5. **Test Database Connection**
```sql
-- Test basic queries
SELECT COUNT(*) as category_count FROM categories;
SELECT COUNT(*) as product_count FROM products;
SELECT COUNT(*) as admin_count FROM admins;
```

## Database Configuration

### Update Connection Settings
1. **Open** `src/main/resources/database.properties`
2. **Update** the following values according to your MySQL setup:

```properties
# Database Configuration for MySQL Workbench
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=your_mysql_username
db.password=your_mysql_password
```

### Common MySQL Workbench Settings

#### For Default MySQL Installation:
```properties
db.username=root
db.password=your_root_password
```

#### For Custom User:
```properties
db.username=ecommerce_user
db.password=your_secure_password
```

## Creating a Dedicated Database User (Recommended)

### 1. **Create User in MySQL Workbench**
```sql
-- Create a dedicated user for the application
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'secure_password_123';

-- Grant all privileges on the ecommerce database
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;
```

### 2. **Update Application Configuration**
```properties
db.username=ecommerce_user
db.password=secure_password_123
```

## Troubleshooting Common Issues

### 1. **Connection Refused Error**
**Problem**: `java.sql.SQLException: Connection refused`

**Solutions**:
- Ensure MySQL service is running
- Check if port 3306 is available
- Verify MySQL is listening on localhost

**Check in MySQL Workbench**:
```sql
SHOW VARIABLES LIKE 'port';
```

### 2. **Access Denied Error**
**Problem**: `Access denied for user 'root'@'localhost'`

**Solutions**:
- Verify username and password
- Check if user has proper privileges
- Try resetting MySQL root password

**Fix in MySQL Workbench**:
```sql
-- Check user privileges
SELECT user, host FROM mysql.user WHERE user = 'root';

-- Grant privileges if needed
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

### 3. **Character Encoding Issues**
**Problem**: Special characters not displaying correctly

**Solutions**:
- Ensure database uses utf8mb4 character set
- Check connection string includes characterEncoding=UTF-8

**Verify in MySQL Workbench**:
```sql
SHOW VARIABLES LIKE 'character_set%';
```

### 4. **Timezone Issues**
**Problem**: `The server time zone value 'UTC' is unrecognized`

**Solutions**:
- Add `serverTimezone=UTC` to connection string
- Set MySQL timezone

**Fix in MySQL Workbench**:
```sql
SET time_zone = '+00:00';
```

## Database Schema Verification

### 1. **Check Table Structure**
```sql
-- Verify all tables exist
DESCRIBE users;
DESCRIBE products;
DESCRIBE categories;
DESCRIBE orders;
```

### 2. **Check Foreign Key Constraints**
```sql
-- Verify foreign key relationships
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    REFERENCED_TABLE_SCHEMA = 'ecommerce_db';
```

### 3. **Test Sample Queries**
```sql
-- Test product listing with category
SELECT 
    p.id, 
    p.name, 
    p.price, 
    c.name as category_name
FROM products p 
LEFT JOIN categories c ON p.category_id = c.id 
WHERE p.status = 'active';

-- Test user registration
INSERT INTO users (name, email, password_hash, phone) 
VALUES ('Test User', 'test@example.com', '$2a$10$test.hash', '1234567890');

-- Test admin login
SELECT * FROM admins WHERE username = 'admin';
```

## Performance Optimization for MySQL Workbench

### 1. **Enable Query Cache**
```sql
-- Check if query cache is enabled
SHOW VARIABLES LIKE 'query_cache%';

-- Enable if not already enabled (add to my.cnf)
-- query_cache_type = 1
-- query_cache_size = 64M
```

### 2. **Optimize InnoDB Settings**
```sql
-- Check InnoDB settings
SHOW VARIABLES LIKE 'innodb%';
```

### 3. **Monitor Performance**
```sql
-- Check slow queries
SHOW VARIABLES LIKE 'slow_query_log';

-- View current connections
SHOW PROCESSLIST;
```

## Backup and Recovery

### 1. **Create Database Backup**
```sql
-- In MySQL Workbench, use Data Export wizard
-- Or use command line:
-- mysqldump -u root -p ecommerce_db > ecommerce_backup.sql
```

### 2. **Restore Database**
```sql
-- In MySQL Workbench, use Data Import wizard
-- Or use command line:
-- mysql -u root -p ecommerce_db < ecommerce_backup.sql
```

## Security Best Practices

### 1. **Use Strong Passwords**
- Minimum 12 characters
- Mix of letters, numbers, and symbols
- Avoid common passwords

### 2. **Limit User Privileges**
```sql
-- Create user with limited privileges
CREATE USER 'ecommerce_readonly'@'localhost' IDENTIFIED BY 'readonly_password';
GRANT SELECT ON ecommerce_db.* TO 'ecommerce_readonly'@'localhost';
```

### 3. **Enable SSL (Optional)**
```sql
-- Check SSL status
SHOW VARIABLES LIKE 'have_ssl';
```

## Next Steps

1. **Complete Database Setup** using the steps above
2. **Update** `database.properties` with your credentials
3. **Deploy** the Java application
4. **Test** the connection by running the application
5. **Verify** all features work correctly

## Support

If you encounter any issues:
1. Check MySQL error logs
2. Verify connection parameters
3. Test queries in MySQL Workbench
4. Check Java application logs

The application should now work seamlessly with MySQL Workbench!
