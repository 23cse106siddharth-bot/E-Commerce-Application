# 🚀 Complete Step-by-Step Guide to Run E-Commerce Project

## 📋 **Prerequisites Checklist**

Before starting, ensure you have:
- [ ] **Java JDK 8 or higher** installed
- [ ] **Apache Tomcat 9 or higher** installed
- [ ] **MySQL Server 8.0+** installed and running
- [ ] **MySQL Workbench** installed
- [ ] **Apache NetBeans** (or any Java IDE)

---

## 🗄️ **STEP 1: Database Setup (MySQL Workbench)**

### 1.1 **Start MySQL Service**
```bash
# Windows (Run as Administrator)
net start mysql

# Or start MySQL Workbench and connect to localhost:3306
```

### 1.2 **Open MySQL Workbench**
1. Launch **MySQL Workbench**
2. Connect to your local MySQL server
3. Use your MySQL root credentials

### 1.3 **Create Database**
1. **Open** the file: `database/mysql-workbench-setup.sql`
2. **Copy** the entire content
3. **Paste** into MySQL Workbench Query Editor
4. **Execute** the script:
   - Press `Ctrl + Shift + Enter`
   - Wait for "Query executed successfully"

### 1.4 **Verify Database Creation**
Run this in MySQL Workbench:
```sql
USE ecommerce_db;
SHOW TABLES;
SELECT COUNT(*) FROM products;
SELECT COUNT(*) FROM categories;
SELECT COUNT(*) FROM admins;
```

**Expected Results:**
- 11 tables created
- 7 products
- 5 categories  
- 1 admin user

---

## ⚙️ **STEP 2: Configure Database Connection**

### 2.1 **Update Database Properties**
1. **Open** file: `src/main/resources/database.properties`
2. **Update** your MySQL credentials:
```properties
# Database Configuration for MySQL Workbench
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

**Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password**

---

## 🏗️ **STEP 3: Build and Deploy Project**

### 3.1 **Open Project in Apache NetBeans**
1. Launch **Apache NetBeans**
2. **File** → **Open Project**
3. Navigate to your project folder: `C:\Users\paths\Downloads\project 1`
4. Select the project folder and click **Open**

### 3.2 **Clean and Build Project**
1. **Right-click** on the project name in NetBeans
2. Select **Clean and Build**
3. Wait for build to complete successfully
4. Check for any errors in the output window

### 3.3 **Configure Tomcat Server**
1. **Tools** → **Servers**
2. **Add Server** → **Apache Tomcat**
3. Browse to your Tomcat installation directory
4. Set username/password (default: admin/admin)
5. Click **Finish**

### 3.4 **Deploy to Tomcat**
1. **Right-click** on the project
2. Select **Deploy**
3. Choose your Tomcat server
4. Wait for deployment to complete

---

## 🚀 **STEP 4: Start Tomcat Server**

### 4.1 **Start Tomcat from NetBeans**
1. **Right-click** on Tomcat server in Services tab
2. Select **Start**
3. Wait for server to start (check output window)

### 4.2 **Or Start Tomcat Manually**
```bash
# Navigate to Tomcat bin directory
cd C:\apache-tomcat-9.0.xx\bin

# Start Tomcat
startup.bat
```

---

## 🌐 **STEP 5: Access the Application**

### 5.1 **Open Web Browser**
1. Open your web browser (Chrome, Firefox, Edge)
2. Navigate to: `http://localhost:8080/ecommerce-app/`

### 5.2 **Test User Interface**
1. **Home Page**: Should show featured products
2. **Products Page**: Browse all products
3. **Register**: Create a new user account
4. **Login**: Test user login

### 5.3 **Test Admin Panel**
1. Navigate to: `http://localhost:8080/ecommerce-app/admin`
2. **Login Credentials**:
   - Username: `admin`
   - Password: `admin123`
3. **Test Admin Features**:
   - View dashboard
   - Manage products
   - Manage categories
   - View users

---

## ✅ **STEP 6: Verify Everything Works**

### 6.1 **Test User Registration**
1. Go to **Register** page
2. Fill in registration form
3. Submit and verify account creation
4. Login with new account

### 6.2 **Test Shopping Cart**
1. Browse products
2. Add items to cart
3. View cart
4. Update quantities
5. Remove items

### 6.3 **Test Admin Functions**
1. Login to admin panel
2. Add a new product
3. Edit existing product
4. Add new category
5. View user list

---

## 🔧 **Troubleshooting Common Issues**

### **Issue 1: Database Connection Error**
**Error**: `java.sql.SQLException: Access denied`

**Solution**:
1. Check MySQL service is running
2. Verify credentials in `database.properties`
3. Test connection in MySQL Workbench

### **Issue 2: Build Errors**
**Error**: `Maven build failed`

**Solution**:
1. Check Java version: `java -version`
2. Clean project: **Clean and Build**
3. Check for missing dependencies

### **Issue 3: Deployment Failed**
**Error**: `Application failed to start`

**Solution**:
1. Check Tomcat logs
2. Verify port 8080 is available
3. Check server configuration

### **Issue 4: Page Not Found (404)**
**Error**: `HTTP Status 404 - Not Found`

**Solution**:
1. Verify correct URL: `http://localhost:8080/ecommerce-app/`
2. Check if application deployed successfully
3. Restart Tomcat server

---

## 📊 **Expected Results**

### **Home Page Features**:
- ✅ Hero section with "Welcome to E-Commerce Store"
- ✅ Featured products grid
- ✅ Category navigation
- ✅ Responsive design

### **Products Page Features**:
- ✅ Product listing with images
- ✅ Search functionality
- ✅ Category filtering
- ✅ Price display

### **Admin Panel Features**:
- ✅ Dashboard with statistics
- ✅ Product management (CRUD)
- ✅ Category management
- ✅ User management

---

## 🎯 **Quick Test Checklist**

- [ ] Database created successfully
- [ ] Application builds without errors
- [ ] Tomcat server starts
- [ ] Home page loads
- [ ] User registration works
- [ ] User login works
- [ ] Product browsing works
- [ ] Shopping cart works
- [ ] Admin login works
- [ ] Admin functions work

---

## 🆘 **Need Help?**

### **Check Logs**:
1. **NetBeans Output**: View build and deployment logs
2. **Tomcat Logs**: Check `logs/catalina.out`
3. **Application Logs**: Check console output

### **Common Commands**:
```bash
# Check Java version
java -version

# Check MySQL status
mysql --version

# Check Tomcat status
# Look for running java processes
```

### **Reset Everything**:
1. Stop Tomcat server
2. Clean and rebuild project
3. Redeploy application
4. Restart Tomcat

---

## 🎉 **Success!**

If you've followed all steps and see the e-commerce application running, congratulations! You now have a fully functional e-commerce application with:

- ✅ User registration and authentication
- ✅ Product catalog with search and filtering
- ✅ Shopping cart functionality
- ✅ Admin panel for management
- ✅ Responsive web design
- ✅ MySQL database integration

**Default Admin Login:**
- URL: `http://localhost:8080/ecommerce-app/admin`
- Username: `admin`
- Password: `admin123`

**User Interface:**
- URL: `http://localhost:8080/ecommerce-app/`

Enjoy your e-commerce application! 🛒✨
