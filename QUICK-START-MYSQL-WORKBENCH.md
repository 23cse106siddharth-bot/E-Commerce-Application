# 🚀 Quick Start Guide - MySQL Workbench

## ⚡ **5-Minute Setup**

### **Step 1: Open MySQL Workbench**
1. Launch MySQL Workbench
2. Connect to your local MySQL server (usually `localhost:3306`)
3. Use your MySQL root credentials

### **Step 2: Create Database**
1. **Open** `database/mysql-workbench-setup.sql` in MySQL Workbench
2. **Execute** the entire script:
   - Press `Ctrl + Shift + Enter` (or click Execute button)
   - Wait for "Query executed successfully" message
3. **Verify** by running: `SHOW TABLES;`

### **Step 3: Configure Application**
1. **Open** `src/main/resources/database.properties`
2. **Update** your MySQL credentials:
   ```properties
   db.username=root
   db.password=your_mysql_password
   ```

### **Step 4: Deploy Application**
1. **Open** project in Apache NetBeans
2. **Clean and Build** the project
3. **Deploy** to Tomcat server
4. **Access** at: `http://localhost:8080/ecommerce-app/`

## 🎯 **Test Your Setup**

### **1. Test Database Connection**
Run this in MySQL Workbench:
```sql
USE ecommerce_db;
SELECT COUNT(*) as products FROM products;
SELECT COUNT(*) as categories FROM categories;
SELECT COUNT(*) as admins FROM admins;
```

### **2. Test Application**
- **User Interface**: `http://localhost:8080/ecommerce-app/`
- **Admin Panel**: `http://localhost:8080/ecommerce-app/admin`
- **Admin Login**: username: `admin`, password: `admin123`

## 🔧 **Troubleshooting**

### **Database Connection Issues**
```sql
-- Check if MySQL is running
SHOW PROCESSLIST;

-- Check database exists
SHOW DATABASES LIKE 'ecommerce_db';

-- Check tables
USE ecommerce_db;
SHOW TABLES;
```

### **Application Issues**
1. **Check** `database.properties` credentials
2. **Verify** MySQL service is running
3. **Check** Tomcat server logs
4. **Ensure** port 8080 is available

## 📊 **Verify Sample Data**

After running the setup script, you should see:
- **5 Categories** (Electronics, Clothing, Books, etc.)
- **7 Products** (Smartphone, Laptop, T-Shirt, etc.)
- **1 Admin** (username: admin, password: admin123)

## 🎉 **You're Ready!**

Your e-commerce application is now ready to use with MySQL Workbench!

### **Next Steps:**
1. **Browse Products** - Test the product catalog
2. **Register User** - Create a test account
3. **Add to Cart** - Test shopping cart functionality
4. **Admin Panel** - Manage products and categories

### **Default Login Credentials:**
- **Admin**: username: `admin`, password: `admin123`
- **User**: Register a new account through the website

---

**Need Help?** Check the detailed `mysql-workbench-setup.md` guide for comprehensive instructions.
