-- Create database
CREATE DATABASE IF NOT EXISTS WarehouseManagement;
USE WarehouseManagement;



-- Table Account
CREATE TABLE Account (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    RoleID INT NOT NULL,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
);

-- Table Branch
CREATE TABLE Branch (
    BranchID INT AUTO_INCREMENT PRIMARY KEY,
    BranchName VARCHAR(100) NOT NULL,
    Location VARCHAR(255)
);

-- Table Warehouse
CREATE TABLE Warehouse (
    WarehouseID INT AUTO_INCREMENT PRIMARY KEY,
    WarehouseName VARCHAR(100) NOT NULL,
    BranchID INT NOT NULL,
    Address VARCHAR(255),
    FOREIGN KEY (BranchID) REFERENCES Branch(BranchID)
);

-- Table Employee
CREATE TABLE Employee (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeName VARCHAR(100) NOT NULL,
    Email VARCHAR(100),
    Phone VARCHAR(20),
    Address VARCHAR(100),
    Gender TINYINT NOT NULL DEFAULT 1,
    DateOfBirth DATETIME,
    Status TINYINT NOT NULL DEFAULT 1,
    AccountID INT UNIQUE NOT NULL,
    WarehouseID INT NOT NULL,
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID),
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse(WarehouseID)
);

-- Table Zone
CREATE TABLE Zone (
    ZoneID INT AUTO_INCREMENT PRIMARY KEY,
    ZoneName VARCHAR(100) NOT NULL,
    WarehouseID INT NOT NULL,
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse(WarehouseID)
);

-- Table Bin
CREATE TABLE Bin (
    BinID INT AUTO_INCREMENT PRIMARY KEY,
    BinName VARCHAR(100) NOT NULL,
    ZoneID INT NOT NULL,
    MaxCapacity INT NOT NULL,
    FOREIGN KEY (ZoneID) REFERENCES Zone(ZoneID)
);

-- Table Supplier
CREATE TABLE Supplier (
    SupplierId INT AUTO_INCREMENT PRIMARY KEY,
    SupplierName VARCHAR(100),
    Address VARCHAR(255),
    ContactEmail VARCHAR(100),
    Phone VARCHAR(20)
);

-- Table Category
CREATE TABLE Category (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName VARCHAR(50) NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    CreatedBy INT NOT NULL,
    FOREIGN KEY (CreatedBy) REFERENCES Employee(EmployeeID)
);

-- Table Product
CREATE TABLE Product (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    BinID INT NOT NULL,
    CategoryId INT NOT NULL,
    Material VARCHAR(100),
    Gender VARCHAR(10),
    Seasons VARCHAR(20),
    MinQuantity INT NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Description VARCHAR(255),
    CreatedBy INT NOT NULL,
    SupplierId INT NOT NULL,
    MadeIn VARCHAR(50),
    FOREIGN KEY (BinID) REFERENCES Bin(BinID),
    FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId),
    FOREIGN KEY (CreatedBy) REFERENCES Employee(EmployeeID),
    FOREIGN KEY (SupplierId) REFERENCES Supplier(SupplierId)
);

-- Table ProductDetail
CREATE TABLE ProductDetail (
    ProductDetailID INT AUTO_INCREMENT PRIMARY KEY,
    Quantity INT NOT NULL,
    Weight DECIMAL(10, 2),
    Color VARCHAR(50),
    Size VARCHAR(50),
    ProductImage VARCHAR(255),
    ProductID INT NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

-- Insert data into Role
INSERT INTO Role (RoleName)
VALUES 
('Manager'),
('Sale'),
('Storage Staff'),
('Admin');

-- Insert data into Account
INSERT INTO Account (Email, Password, CreatedDate, RoleID)
VALUES 
('nguyenvana@example.com', 'password123', CURRENT_TIMESTAMP, 1), -- Manager
('tranthib@example.com', 'password456', CURRENT_TIMESTAMP, 4), -- Admin
('phamthic@example.com', 'password789', CURRENT_TIMESTAMP, 2), -- Sale
('levand@example.com', 'password101', CURRENT_TIMESTAMP, 3); -- Storage Staff

-- Insert data into Branch
INSERT INTO Branch (BranchName, Location)
VALUES 
('HN', 'Hà Nội'),
('HCM', 'Hồ Chí Minh');

-- Insert data into Warehouse
INSERT INTO Warehouse (WarehouseName, BranchID, Address)
VALUES 
('Hoa Lac', 1, 'Hòa Lạc, Hà Nội'),
('Ha Dong', 1, 'Hà Đông, Hà Nội'),
('Ba Dinh', 1, 'Ba Đình, Hà Nội'),
('Tan Binh', 2, 'Tân Bình, Hồ Chí Minh'),
('Thu Duc', 2, 'Thủ Đức, Hồ Chí Minh');

-- Insert data into Employee
INSERT INTO Employee (EmployeeName, Email, Phone, Address, Gender, DateOfBirth, Status, AccountID, WarehouseID)
VALUES 
('Nguyễn Văn A', 'nguyenvana.manager@example.com', '0123456789', 'Hà Nội', 1, '1985-01-01', 1, 1, 1), -- Manager
('Trần Thị B', 'tranthib.admin@example.com', '0987654321', 'Hà Nội', 0, '1990-02-01', 1, 2, 1), -- Admin
('Phạm Thị C', 'phamthic.sale@example.com', '0112233445', 'Hồ Chí Minh', 0, '1992-03-01', 1, 3, 2), -- Sale
('Lê Văn D', 'levand.staff@example.com', '0223344556', 'Hồ Chí Minh', 1, '1993-04-01', 1, 4, 3); -- Storage Staff

-- Insert data into Zone
INSERT INTO Zone (ZoneName, WarehouseID)
VALUES 
('Upper Storage Area', 1),
('Downer Storage Area', 1),
('Other Products Area', 1),
('Defective Products Area', 1);

-- Insert data into Bin
INSERT INTO Bin (BinName, ZoneID, MaxCapacity)
VALUES 
('Hoodie Bin', 1, 200),
('Jeans Bin', 1, 200),
('Shirt Bin', 2, 200),
('T-shirt Bin', 2, 200),
('Socks Bin', 3, 200),
('Others Bin', 4, 200);

-- Insert data into Supplier
INSERT INTO Supplier (SupplierName, Address, ContactEmail, Phone)
VALUES 
('ABC Supplies', '123 Main St, Hà Nội', 'abc@supplies.com', '0123456789'),
('XYZ Wholesalers', '456 Market St, Hồ Chí Minh', 'xyz@wholesalers.com', '0987654321');

-- Insert data into Category
INSERT INTO Category (CategoryName, CreatedBy)
VALUES 
('Hoodie', 1),
('Jacket', 1),
('T-shirt', 1),
('Shirt', 1),
('Sweater', 1),
('Tank Top', 1),
('Coat', 1),
('Jeans', 1),
('Trousers', 1),
('Shorts', 1),
('Socks', 1),
('Others', 1);

-- Insert data into Product
INSERT INTO Product (ProductName, Price, BinID, CategoryId, Material, Gender, Seasons, MinQuantity, CreatedBy, SupplierId, MadeIn)
VALUES 
('Winter Hoodie', 500000, 1, 1, 'Cotton', 'Unisex', 'Winter', 10, 1, 1, 'Vietnam'),
('Denim Jeans', 700000, 2, 8, 'Denim', 'Male', 'All Season', 20, 1, 2, 'Vietnam');

-- Insert data into ProductDetail
INSERT INTO ProductDetail (Quantity, Weight, Color, Size, ProductImage, ProductID)
VALUES 
(50, 1.2, 'Black', 'L', 'images/hoodie_black_L.jpg', 1),
(30, 1.5, 'Blue', 'M', 'images/jeans_blue_M.jpg', 2);
