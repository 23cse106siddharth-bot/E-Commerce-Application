-- CLEAN DATABASE SETUP - Run this if you get errors
-- This will completely remove and recreate the database

-- 1. Drop the entire database (this will remove everything)
DROP DATABASE IF EXISTS ecommerce_db;

-- 2. Create fresh database
CREATE DATABASE ecommerce_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 3. Use the database
USE ecommerce_db;

-- 4. Create all tables (run the rest of the original script)
-- Copy and paste the rest from mysql-workbench-setup.sql starting from CREATE TABLE users...
