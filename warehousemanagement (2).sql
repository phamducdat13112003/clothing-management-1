-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 27, 2025 at 06:15 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `warehousemanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `AccountID` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `Status` varchar(10) NOT NULL,
  `EmployeeID` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`AccountID`, `Email`, `Password`, `LastUpdate`, `RoleID`, `Status`, `EmployeeID`) VALUES
('ACC001', 'truongnvhe170937@fpt.edu.vn', '6aef9ebaad2fb22c8c2e6381edff438f', '2025-02-25 10:00:00', 1, 'Active', 'EP001'),
('ACC002', 'bob@example.com', '6aef9ebaad2fb22c8c2e6381edff438f', '2025-02-25 11:00:00', 4, 'Active', 'EP002'),
('ACC003', 'ducnthe181557@fpt.edu.vn', '6aef9ebaad2fb22c8c2e6381edff438f', '2025-03-05 01:21:33', 2, 'Active', 'EP003'),
('ACC004', 'hoangnhhe123213@fpt.edu.vn', '6aef9ebaad2fb22c8c2e6381edff438f', '0000-00-00 00:00:00', 3, 'Active', 'EP004');

-- --------------------------------------------------------

--
-- Table structure for table `bin`
--

CREATE TABLE `bin` (
  `BinID` varchar(50) NOT NULL,
  `BinName` varchar(100) NOT NULL,
  `MaxCapacity` decimal(10,2) NOT NULL,
  `Status` tinyint(1) NOT NULL,
  `SectionID` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bin`
--

INSERT INTO `bin` (`BinID`, `BinName`, `MaxCapacity`, `Status`, `SectionID`) VALUES
('FW001-001', 'Bin Fall/Winter 01', 100.00, 1, 'FW001'),
('FW001-002', 'Bin Fall/Winter 02', 100.00, 1, 'FW001'),
('FW001-003', 'Bin Fall/Winter 03', 100.00, 1, 'FW001'),
('FW001-004', 'Bin Fall/Winter 04', 100.00, 1, 'FW001'),
('FW001-005', 'Bin Fall/Winter 05', 100.00, 1, 'FW001'),
('FW001-006', 'Bin Fall/Winter 06', 100.00, 1, 'FW001'),
('FW001-007', 'Bin Fall/Winter 07', 100.00, 1, 'FW001'),
('FW001-008', 'Bin Fall/Winter 08', 100.00, 1, 'FW001'),
('FW001-009', 'Bin Fall/Winter 09', 100.00, 1, 'FW001'),
('OT001-002', 'Bin Others 01', 100.00, 1, 'OT001'),
('PF001-001', 'Bin Pre-Fall 01', 100.00, 1, 'PF001'),
('PF001-002', 'Bin Pre-Fall 02', 100.00, 1, 'PF001'),
('RP001-001', 'Bin Receipt 01', 100.00, 1, 'RP001'),
('RP001-002', 'Bin Receipt 02', 100.00, 1, 'RP001'),
('RP001-003', 'Bin Receipt 03', 100.00, 1, 'RP001'),
('RP001-004', 'Bin Receipt 04', 100.00, 1, 'RP001'),
('SS001-001', 'Bin Spring-Summer 01', 100.00, 1, 'SS001'),
('SS001-002', 'Bin Spring-Summer 02', 100.00, 1, 'SS001'),
('SS001-003', 'Bin Spring-Summer 03', 100.00, 1, 'SS001');

-- --------------------------------------------------------

--
-- Table structure for table `bindetail`
--

CREATE TABLE `bindetail` (
  `binDetailId` varchar(50) NOT NULL,
  `binId` varchar(50) NOT NULL,
  `ProductDetailId` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bindetail`
--

INSERT INTO `bindetail` (`binDetailId`, `binId`, `ProductDetailId`, `quantity`) VALUES
('FW001-001-01', 'FW001-001', 'P001-001', 0),
('FW001-001-02', 'FW001-001', 'P001-002', 50),
('FW001-002-01', 'FW001-002', 'P001-003', 50),
('FW001-002-02', 'FW001-002', 'P001-004', 50),
('FW001-003-01', 'FW001-003', 'P001-005', 50),
('FW001-003-02', 'FW001-003', 'P001-006', 50),
('FW001-004-01', 'FW001-004', 'P001-007', 50),
('FW001-004-02', 'FW001-004', 'P001-008', 50),
('FW001-005-01', 'FW001-005', 'P001-009', 50),
('FW001-005-02', 'FW001-005', 'P002-001', 50),
('FW001-006-01', 'FW001-006', 'P002-002', 50),
('FW001-006-02', 'FW001-006', 'P002-003', 50),
('FW001-007-01', 'FW001-007', 'P002-004', 20),
('FW001-007-02', 'FW001-007', 'P002-005', 20),
('FW001-007-03', 'FW001-007', 'P002-006', 20),
('FW001-007-04', 'FW001-007', 'P002-007', 40),
('FW001-008-01', 'FW001-008', 'P002-008', 50),
('FW001-008-02', 'FW001-008', 'P003-001', 20),
('FW001-008-03', 'FW001-008', 'P007-001', 20),
('FW001-008-04', 'FW001-008', 'P008-001', 1),
('FW001-009-01', 'FW001-009', 'P008-001', 19),
('FW001-009-02', 'FW001-009', 'P001-001', 38),
('PF001-001-01', 'PF001-001', 'P004-001', 50),
('PF001-001-02', 'PF001-001', 'P006-001', 20),
('RP001-001-01', 'RP001-001', 'P001-001', 12),
('RP001-001-02', 'RP001-001', 'P001-002', 9),
('SS001-001-01', 'SS001-001', 'P005-001', 20);

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `BranchID` int(11) NOT NULL,
  `BranchName` varchar(100) NOT NULL,
  `Location` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`BranchID`, `BranchName`, `Location`) VALUES
(1, 'Branch 1', 'HCMC'),
(2, 'Branch 2', 'Ha Noi');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `CategoryID` int(11) NOT NULL,
  `CategoryName` varchar(50) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `CreatedBy` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`CategoryID`, `CategoryName`, `CreatedDate`, `CreatedBy`) VALUES
(1, 'Hoodie', '2025-02-25 14:00:00', 'EP001'),
(2, 'Sweater', '2025-03-26 00:00:00', 'EP001'),
(3, 'Jeans', '2025-03-21 00:00:00', 'EP001'),
(4, 'T-shirt', '2025-03-21 00:00:00', 'EP001'),
(5, 'Jacket', '2025-03-21 00:00:00', 'EP001'),
(6, 'Puffer Jacket', '2025-03-26 00:00:00', 'EP001');

-- --------------------------------------------------------

--
-- Table structure for table `do`
--

CREATE TABLE `do` (
  `DOID` varchar(50) NOT NULL,
  `PlannedShippingDate` date NOT NULL,
  `ReceiptDate` date NOT NULL,
  `POID` varchar(50) DEFAULT NULL,
  `CreatedBy` varchar(50) DEFAULT NULL,
  `Recipient` varchar(50) DEFAULT NULL,
  `Status` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dodetail`
--

CREATE TABLE `dodetail` (
  `DODetailID` varchar(50) NOT NULL,
  `ProductDetailID` varchar(50) DEFAULT NULL,
  `Quantity` int(11) NOT NULL,
  `DOID` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `EmployeeID` varchar(50) NOT NULL,
  `EmployeeName` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Phone` varchar(20) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Gender` tinyint(1) DEFAULT NULL,
  `Dob` date NOT NULL,
  `Status` varchar(10) DEFAULT NULL,
  `Image` varchar(255) DEFAULT NULL,
  `WarehouseID` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`EmployeeID`, `EmployeeName`, `Email`, `Phone`, `Address`, `Gender`, `Dob`, `Status`, `Image`, `WarehouseID`) VALUES
('EP001', 'Alice', 'alice@example.com', '0123456789', '123 Street', 0, '1995-03-10', 'Active', NULL, 'W001'),
('EP002', 'Bob', 'bob@example.com', '0987654321', '456 Street', 1, '1990-07-15', 'Active', 'img/Employee/EP002.png?1742796717820', 'W001'),
('EP003', 'Nguyen Van Truong', 'truongnguyen29062003@gmail.com', '0965255438', 'Lai Chau', 1, '2003-06-29', 'Active', '/img/Employee/EP001.png', 'W001'),
('EP004', 'Hoang Nghia Hung', 'hoangnhhe123213@fpt.edu.vn', '0941545454', 'Ha Noi', 1, '2004-05-19', 'Active', '', 'W001');

-- --------------------------------------------------------

--
-- Table structure for table `inventorydoc`
--

CREATE TABLE `inventorydoc` (
  `InventoryDocID` varchar(50) NOT NULL,
  `CreatedBy` varchar(50) DEFAULT NULL,
  `CreatedDate` date NOT NULL,
  `BinID` varchar(50) DEFAULT NULL,
  `Status` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventorydoc`
--

INSERT INTO `inventorydoc` (`InventoryDocID`, `CreatedBy`, `CreatedDate`, `BinID`, `Status`) VALUES
('INV00001', 'EP001', '2025-03-23', 'RP001-001', 'Counted'),
('INV00002', 'EP001', '2025-03-25', 'SS001-001', 'Pending'),
('INV00003', 'EP001', '2025-03-26', 'FW001-001', 'Done'),
('INV00004', 'EP001', '2025-03-26', 'FW001-009', 'Done'),
('INV00005', 'EP001', '2025-03-27', 'SS001-001', 'Done');

-- --------------------------------------------------------

--
-- Table structure for table `inventorydocdetail`
--

CREATE TABLE `inventorydocdetail` (
  `InventoryDocDetailID` varchar(50) NOT NULL,
  `ProductDetailID` varchar(50) DEFAULT NULL,
  `CounterID` varchar(50) DEFAULT NULL,
  `ReCounterID` varchar(50) DEFAULT NULL,
  `OriginQuantity` int(11) NOT NULL,
  `RecountQuantity` int(11) NOT NULL,
  `UpdatedDate` date NOT NULL,
  `InventoryDocID` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventorydocdetail`
--

INSERT INTO `inventorydocdetail` (`InventoryDocDetailID`, `ProductDetailID`, `CounterID`, `ReCounterID`, `OriginQuantity`, `RecountQuantity`, `UpdatedDate`, `InventoryDocID`) VALUES
('IDD00001', 'P002-001', 'EP003', NULL, 50, 50, '2025-03-25', 'INV00001'),
('IDD00002', 'P002-002', 'EP003', NULL, 50, 50, '2025-03-25', 'INV00001'),
('IDD00003', 'P002-003', 'EP001', NULL, 50, -1, '2025-03-25', 'INV00002'),
('IDD00004', 'P002-005', 'EP001', NULL, 50, -1, '2025-03-25', 'INV00002'),
('IDD00005', 'P001-001', 'EP002', NULL, 29, 17, '2025-03-26', 'INV00003'),
('IDD00006', 'P001-002', 'EP002', NULL, 50, 50, '2025-03-26', 'INV00003'),
('IDD00007', 'P008-001', 'EP002', NULL, 19, 19, '2025-03-27', 'INV00004'),
('IDD00008', 'P001-001', 'EP002', 'EP002', 21, 21, '2025-03-27', 'INV00004'),
('IDD00009', 'P005-001', 'EP002', NULL, 50, 20, '2025-03-27', 'INV00005');

-- --------------------------------------------------------

--
-- Table structure for table `po`
--

CREATE TABLE `po` (
  `POID` varchar(50) NOT NULL,
  `CreatedDate` date NOT NULL,
  `SupplierID` varchar(50) DEFAULT NULL,
  `CreatedBy` varchar(50) DEFAULT NULL,
  `Status` varchar(10) NOT NULL,
  `TotalPrice` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `po`
--

INSERT INTO `po` (`POID`, `CreatedDate`, `SupplierID`, `CreatedBy`, `Status`, `TotalPrice`) VALUES
('PO2025032112911', '2025-03-21', 'SP001', 'EP002', 'Processing', 39999996),
('PO2025032523942', '2025-03-25', 'SP001', 'EP001', 'Done', 50004),
('PO2025032552387', '2025-03-25', 'SP001', 'EP001', 'Done', 650000),
('PO2025032552702', '2025-03-25', 'SP001', 'EP001', 'Pending', 100000),
('PO2025032579173', '2025-03-25', 'SP001', 'EP001', 'Cancel', 500000),
('PO2025032627850', '2025-03-26', 'SP001', 'EP003', 'Done', 250000),
('PO2025032655115', '2025-03-26', 'SP001', 'EP003', 'Done', 862491),
('PO2025032718172', '2025-03-27', 'SP001', 'EP003', 'Pending', 312000),
('PO2025032735243', '2025-03-27', 'SP001', 'EP003', 'Done', 104000),
('PO2025032735638', '2025-03-27', 'SP001', 'EP003', 'Pending', 364000),
('PO2025032740884', '2025-03-27', 'SP001', 'EP003', 'Done', 364000);

-- --------------------------------------------------------

--
-- Table structure for table `podetail`
--

CREATE TABLE `podetail` (
  `POdetailID` varchar(50) NOT NULL,
  `POID` varchar(50) DEFAULT NULL,
  `ProductDetailID` varchar(50) DEFAULT NULL,
  `Quantity` int(11) NOT NULL,
  `Price` int(11) NOT NULL,
  `TotalPrice` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `podetail`
--

INSERT INTO `podetail` (`POdetailID`, `POID`, `ProductDetailID`, `Quantity`, `Price`, `TotalPrice`) VALUES
('POD20250321214231', 'PO2025032112911', 'P001-001', 4, 9999999, 39999996),
('POD20250325222514', 'PO2025032579173', 'P001-001', 10, 50000, 500000),
('POD20250325222543', 'PO2025032523942', 'P001-002', 1, 50004, 50004),
('POD20250325223152', 'PO2025032552387', 'P001-001', 4, 50000, 200000),
('POD20250325223154', 'PO2025032552387', 'P001-003', 4, 50000, 200000),
('POD20250325223155', 'PO2025032552387', 'P001-004', 1, 50000, 50000),
('POD20250325223157', 'PO2025032552387', 'P001-005', 3, 50000, 150000),
('POD20250325223200', 'PO2025032552387', 'P001-006', 1, 50000, 50000),
('POD20250325223446', 'PO2025032552702', 'P001-001', 1, 50000, 50000),
('POD20250325223447', 'PO2025032552702', 'P001-003', 1, 50000, 50000),
('POD20250326083117', 'PO2025032627850', 'P001-001', 4, 50000, 200000),
('POD20250326083355', 'PO2025032627850', 'P001-002', 1, 50000, 50000),
('POD20250326225125', 'PO2025032655115', 'P001-002', 7, 123213, 862491),
('POD20250327083319', 'PO2025032718172', 'P001-001', 3, 52000, 156000),
('POD20250327083321', 'PO2025032718172', 'P001-002', 3, 52000, 156000),
('POD20250327083340', 'PO2025032735243', 'P001-001', 1, 52000, 52000),
('POD20250327083345', 'PO2025032735243', 'P001-002', 1, 52000, 52000),
('POD20250327230637', 'PO2025032740884', 'P001-001', 7, 52000, 364000);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `ProductID` varchar(50) NOT NULL,
  `ProductName` varchar(100) NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  `Material` varchar(100) NOT NULL,
  `Gender` varchar(10) NOT NULL,
  `Seasons` varchar(20) NOT NULL,
  `MinQuantity` int(11) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `MadeIn` varchar(50) NOT NULL,
  `CategoryID` int(11) DEFAULT NULL,
  `CreatedBy` varchar(50) DEFAULT NULL,
  `SupplierID` varchar(50) DEFAULT NULL,
  `Status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`ProductID`, `ProductName`, `Price`, `Material`, `Gender`, `Seasons`, `MinQuantity`, `CreatedDate`, `Description`, `MadeIn`, `CategoryID`, `CreatedBy`, `SupplierID`, `Status`) VALUES
('P001', 'Hoodie Reunion', 52000.00, 'Cotton', 'Male', 'Fall/Winter', 10, '2025-02-25 15:00:00', 'A heavy fleece hoodie with a soft brushed interior, jersey-lined hood with drawstring, dropped shoulders, long sleeves, kangaroo pocket, and wide ribbed cuffs and hem. Relaxed fit for comfort without looking oversized.\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n', 'Vietnam', 1, 'EP001', 'SP001', 1),
('P002', 'Hoodie Nelly Heybig', 200000.00, 'Cotton', 'Female', 'Fall/Winter', 15, '2025-02-25 15:10:00', 'A heavy fleece hoodie with a soft brushed interior, jersey-lined hood with drawstring, dropped shoulders, long sleeves, kangaroo pocket, and wide ribbed cuffs and hem. Relaxed fit for comfort without looking oversized.\n\n\n\n\n\n\n\n\n', 'Vietnam', 1, 'EP001', 'SP002', 1),
('P003', 'Puffer Jacket SOLADO ', 1000000.00, 'Polyester', 'Unisex', 'Fall/Winter', 20, '2025-02-25 15:20:00', 'SOLADO Puffer Jacket – Cropped boxy fit, water-resistant fabric, modern and youthful style.\n\n\n\n\n\n\n\n\n', 'Vietnam', 1, 'EP001', 'SP003', 1),
('P004', 'Vegsivir Basic Sweater', 570000.00, 'Cotton', 'Unisex', 'Pre-Fall', 21, '2025-02-25 15:30:00', 'Lightweight fleece sweatshirt made from a cotton blend with a soft brushed interior. Crew neck with ribbed trim, dropped shoulders, long sleeves, and wide ribbed cuffs and hem. Relaxed fit for comfort without looking oversized.\n\n\n\n\n\n\n\n\n', 'Viet Nam', 2, 'EP001', 'SP004', 1),
('P005', 'Regular Fit Tshirt', 50000.00, 'Cotton', 'Unisex', 'Spring/Summer', 30, '2025-02-25 15:40:00', 'Heavyweight cotton jersey T-shirt with a front graphic, crew neck with ribbed trim, and dropped shoulders. Straight hem with side slits, slightly longer back. Relaxed fit for comfort without looking baggy.\n\n\n\n\n\n\n\n\n', 'Vietnam', 4, 'EP001', 'SP001', 1),
('P006', 'Loose Fit Sweater', 600000.00, 'Cotton', 'Female', 'Pre-Fall', 35, '2025-02-25 15:50:00', 'Lightweight fleece sweatshirt made from a cotton blend with a soft brushed interior. Features a crew neck with ribbed trim, dropped shoulders, long sleeves, and wide ribbed cuffs and hem. Relaxed fit for comfort without looking baggy.\n\n\n\n\n\n\n\n\n', 'Vietnam', 2, 'EP001', 'SP001', 1),
('P007', 'Worker Denim Loose Fit', 1500000.00, 'Cotton denim', 'Male', 'Fall/Winter', 40, '2025-02-25 16:00:00', 'Sturdy denim jacket with a collar, zipper front, chest and side pockets, buttoned cuffs, and adjustable side tabs. Relaxed fit for comfort.\n\n\n\n\n\n\n\n\n', 'Vietnam', 1, 'EP001', 'SP001', 1),
('P008', 'Light Regular Fit Jacket', 800000.00, 'Polyexte 100%', 'Female', 'Fall/Winter', 45, '2025-02-25 16:10:00', 'Lightweight boxy woven shirt jacket with a spread collar, button placket, back yoke, open chest pocket, and buttoned long sleeves with slit cuffs. Straight hem, regular fit for classic comfort.\n\n\n\n\n\n\n\n\n', 'Vietnam', 1, 'EP001', 'SP002', 1),
('P009', 'Ultra Light Down Jacket', 110000.00, '100% cốt tông', 'Male', 'Fall/Winter', 213, '2025-03-26 00:00:00', 'Ultra Light Down is our lightest down. These featherlight designs contain premium down padding but can be folded into a pouch for easy, portable warmth. This is your ultimate transitional layer to navigate the change in seasons, or the perfect choice for ', '', 1, 'EP001', 'SP001', 1);

-- --------------------------------------------------------

--
-- Table structure for table `productdetail`
--

CREATE TABLE `productdetail` (
  `ProductDetailID` varchar(50) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Weight` decimal(10,2) NOT NULL,
  `Color` varchar(50) NOT NULL,
  `Size` varchar(50) NOT NULL,
  `ProductImage` varchar(255) DEFAULT NULL,
  `ProductID` varchar(50) DEFAULT NULL,
  `Status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productdetail`
--

INSERT INTO `productdetail` (`ProductDetailID`, `Quantity`, `Weight`, `Color`, `Size`, `ProductImage`, `ProductID`, `Status`) VALUES
('P001-001', 50, 1.00, 'Red', 'M', 'img/ProductDetail/P001-001.png?1742954544700', 'P001', 1),
('P001-002', 59, 1.00, 'Green', 'S', 'img/ProductDetail/P001-002.png?1742954598501', 'P001', 1),
('P001-003', 50, 1.00, 'Black', 'L', 'img/ProductDetail/P001-003.png?1742954634816', 'P001', 1),
('P001-004', 50, 1.00, 'White', 'XL', 'img/ProductDetail/P001-004.png?1742954685526', 'P001', 1),
('P001-005', 50, 1.00, 'Brown', 'M', 'img/ProductDetail/P001-005.png?1742954859777', 'P001', 1),
('P001-006', 50, 1.00, 'Red', 'XS', 'img/ProductDetail/P001-006.png?1742998435856', 'P001', 1),
('P001-007', 50, 1.00, 'Black', 'XS', 'img/ProductDetail/P001-007.png?1742954722310', 'P001', 1),
('P001-008', 50, 1.00, 'Blue', 'L', 'img/ProductDetail/P001-008.png?1742954757244', 'P001', 1),
('P001-009', 50, 1.00, 'Black', 'XXL', 'img/ProductDetail/P001-009.png?1742954767200', 'P001', 1),
('P002-001', 50, 1.00, 'Blue', 'L', 'img/ProductDetail/P002-001.png?1742954779319', 'P002', 1),
('P002-002', 50, 1.00, 'Purple', 'M', 'img/ProductDetail/P002-002.png', 'P002', 1),
('P002-003', 50, 1.00, 'Orange', 'L', 'img/ProductDetail/P002-003.png', 'P002', 1),
('P002-004', 20, 1.00, 'Black', 'S', 'img/ProductDetail/P002-004.png?1742798775308', 'P002', 1),
('P002-005', 20, 1.00, 'Brown', 'XL', 'img/ProductDetail/P002-005.png?1742798536927', 'P002', 1),
('P002-006', 20, 1.00, 'Gray', 'M', 'img/ProductDetail/P002-006.png?1742798597176', 'P002', 1),
('P002-007', 40, 1.00, 'Green', 'L', 'img/ProductDetail/P002-007.png?1742798731501', 'P002', 1),
('P002-008', 50, 1.00, 'Blue', 'M', 'img/ProductDetail/P002-008.png', 'P002', 1),
('P003-001', 20, 1.20, 'Black', 'S', 'img/ProductDetail/P003-001.png?1742955360637', 'P003', 1),
('P004-001', 50, 0.70, 'Black', 'XS', 'img/ProductDetail/P004-001.png?1742955913040', 'P004', 1),
('P005-001', 20, 0.20, 'Black', 'XS', 'img/ProductDetail/P005-001.png?1743095250505', 'P005', 1),
('P006-001', 20, 1.00, 'Navy', 'XS', 'img/ProductDetail/P006-001.png?1742956693938', 'P006', 1),
('P007-001', 20, 1.10, 'Light Grey', 'M', 'img/ProductDetail/P007-001.png', 'P007', 1),
('P008-001', 20, 0.90, 'Grey', 'XS', 'img/ProductDetail/P008-001.png?1742957140166', 'P008', 1),
('P009-001', 0, 0.80, 'XXX', 'L', 'img/ProductDetail/P009-001.png', 'P009', 1);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `RoleID` int(11) NOT NULL,
  `RoleName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`RoleID`, `RoleName`) VALUES
(1, 'Manager'),
(2, 'Purchase Staff'),
(3, 'Admin'),
(4, 'Storage Staff');

-- --------------------------------------------------------

--
-- Table structure for table `section`
--

CREATE TABLE `section` (
  `SectionID` varchar(50) NOT NULL,
  `SectionName` varchar(50) NOT NULL,
  `SectionTypeID` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `section`
--

INSERT INTO `section` (`SectionID`, `SectionName`, `SectionTypeID`, `status`) VALUES
('FW001', 'Storage Fall/Winter', 3, 1),
('OT001', 'Storage for Other Product', 5, 1),
('PF001', 'Storage Pre-Fall', 4, 1),
('RP001', 'Storage Received', 1, 1),
('SS001', 'Section Spring', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `sectiontype`
--

CREATE TABLE `sectiontype` (
  `SectionTypeID` int(11) NOT NULL,
  `SectionTypeName` varchar(100) NOT NULL,
  `WarehouseID` varchar(50) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sectiontype`
--

INSERT INTO `sectiontype` (`SectionTypeID`, `SectionTypeName`, `WarehouseID`, `Description`) VALUES
(1, 'Receipt Storage', 'W001', 'for new received good only'),
(2, 'SS Storage', 'W001', 'for Spring/Summer Product'),
(3, 'FW Storage', 'W001', 'for Fall/Winter Product'),
(4, 'PF Storage', 'W001', 'for Pre-Fall product'),
(5, 'Others Storage', 'W001', 'for Others product');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `SupplierID` varchar(50) NOT NULL,
  `SupplierName` varchar(100) NOT NULL,
  `Address` varchar(255) NOT NULL,
  `ContactEmail` varchar(100) NOT NULL,
  `Phone` varchar(20) NOT NULL,
  `Status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`SupplierID`, `SupplierName`, `Address`, `ContactEmail`, `Phone`, `Status`) VALUES
('SP001', 'Hanesbrands', '123 Supplier St.', 'supplier1@example.com', '0123456789', 1),
('SP002', 'Gildan Activewear', '456 Supplier St.', 'supplier2@example.com', '0987654321', 1),
('SP003', 'Fruit of the Loom', '789 Supplier St.', 'supplier3@example.com', '0978654321', 1),
('SP004', 'Next Level Apparel', '101 Supplier St.', 'supplier4@example.com', '0967543210', 0);

-- --------------------------------------------------------

--
-- Table structure for table `todetail`
--

CREATE TABLE `todetail` (
  `TODetailID` varchar(50) NOT NULL,
  `ProductDetailID` varchar(50) DEFAULT NULL,
  `Quantity` int(11) NOT NULL,
  `TOID` varchar(50) DEFAULT NULL,
  `OriginBinId` varchar(50) NOT NULL,
  `FinalBinId` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `todetail`
--

INSERT INTO `todetail` (`TODetailID`, `ProductDetailID`, `Quantity`, `TOID`, `OriginBinId`, `FinalBinId`) VALUES
('3e178fd1-778f-4aa0-a4f2-dd41c8c33e44', 'P008-001', 19, 'TO006', 'FW001-009', 'FW001-001'),
('4a2da4d2-2be7-4faf-803a-eab8bc495a20', 'P001-001', 21, 'TO003', 'FW001-001', 'FW001-009'),
('55b55a0c-1a0b-4761-b9b4-4d97e9742551', 'P001-005', 10, 'TO001', 'PF001-001', 'RP001-002'),
('56042599-97c3-4969-a7bb-2a7d1dc65edc', 'P001-001', 10, 'TO007', 'RP001-001', 'SS001-002'),
('610d99f7-99aa-4e73-89b8-c8244e919494', 'P001-001', 17, 'TO005', 'FW001-001', 'FW001-009'),
('89049d4b-ddfa-4079-b505-3ac3e8d2fe15', 'P001-003', 20, 'TO004', 'FW001-002', 'SS001-001'),
('d0649af3-afd1-4528-9f09-ee8c3ff59663', 'P001-001', 30, 'TO002', 'FW001-002', 'RP001-002');

-- --------------------------------------------------------

--
-- Table structure for table `transferorder`
--

CREATE TABLE `transferorder` (
  `TOID` varchar(50) NOT NULL,
  `CreatedDate` date NOT NULL,
  `CreatedBy` varchar(50) NOT NULL,
  `Status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transferorder`
--

INSERT INTO `transferorder` (`TOID`, `CreatedDate`, `CreatedBy`, `Status`) VALUES
('TO001', '2025-03-25', 'EP001', 'Completed'),
('TO002', '2025-03-25', 'EP001', 'Completed'),
('TO003', '2025-03-26', 'EP001', 'Completed'),
('TO004', '2025-03-27', 'EP002', 'Pending'),
('TO005', '2025-03-27', 'EP002', 'Completed'),
('TO006', '2025-03-27', 'EP002', 'Pending'),
('TO007', '2025-03-27', 'EP002', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `virtualbin`
--

CREATE TABLE `virtualbin` (
  `VirtualBinID` varchar(50) NOT NULL,
  `ProductDetailID` varchar(50) NOT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `OriginBinID` varchar(50) NOT NULL,
  `FinalBinID` varchar(50) NOT NULL,
  `TOID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `warehouse`
--

CREATE TABLE `warehouse` (
  `WarehouseID` varchar(50) NOT NULL,
  `WarehouseName` varchar(100) NOT NULL,
  `Address` varchar(255) NOT NULL,
  `BranchID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `warehouse`
--

INSERT INTO `warehouse` (`WarehouseID`, `WarehouseName`, `Address`, `BranchID`) VALUES
('W001', 'Warehouse 1', '123 Street, HCMC', 1),
('W002', 'Warehouse 2', '456 Street, Ha Noi', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`AccountID`),
  ADD UNIQUE KEY `Email` (`Email`),
  ADD KEY `RoleID` (`RoleID`),
  ADD KEY `EmployeeID` (`EmployeeID`);

--
-- Indexes for table `bin`
--
ALTER TABLE `bin`
  ADD PRIMARY KEY (`BinID`),
  ADD KEY `SectionID` (`SectionID`);

--
-- Indexes for table `bindetail`
--
ALTER TABLE `bindetail`
  ADD PRIMARY KEY (`binDetailId`),
  ADD KEY `binId` (`binId`),
  ADD KEY `ProductDetailId` (`ProductDetailId`);

--
-- Indexes for table `branch`
--
ALTER TABLE `branch`
  ADD PRIMARY KEY (`BranchID`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`CategoryID`),
  ADD KEY `CreatedBy` (`CreatedBy`);

--
-- Indexes for table `do`
--
ALTER TABLE `do`
  ADD PRIMARY KEY (`DOID`),
  ADD KEY `POID` (`POID`),
  ADD KEY `CreatedBy` (`CreatedBy`),
  ADD KEY `Recipient` (`Recipient`);

--
-- Indexes for table `dodetail`
--
ALTER TABLE `dodetail`
  ADD PRIMARY KEY (`DODetailID`),
  ADD KEY `ProductDetailID` (`ProductDetailID`),
  ADD KEY `DOID` (`DOID`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`EmployeeID`),
  ADD UNIQUE KEY `Email` (`Email`),
  ADD KEY `WarehouseID` (`WarehouseID`);

--
-- Indexes for table `inventorydoc`
--
ALTER TABLE `inventorydoc`
  ADD PRIMARY KEY (`InventoryDocID`),
  ADD KEY `CreatedBy` (`CreatedBy`),
  ADD KEY `BinID` (`BinID`);

--
-- Indexes for table `inventorydocdetail`
--
ALTER TABLE `inventorydocdetail`
  ADD PRIMARY KEY (`InventoryDocDetailID`),
  ADD KEY `ProductDetailID` (`ProductDetailID`),
  ADD KEY `CounterID` (`CounterID`),
  ADD KEY `ReCounterID` (`ReCounterID`),
  ADD KEY `InventoryDocID` (`InventoryDocID`);

--
-- Indexes for table `po`
--
ALTER TABLE `po`
  ADD PRIMARY KEY (`POID`),
  ADD KEY `SupplierID` (`SupplierID`),
  ADD KEY `CreatedBy` (`CreatedBy`);

--
-- Indexes for table `podetail`
--
ALTER TABLE `podetail`
  ADD PRIMARY KEY (`POdetailID`),
  ADD KEY `POID` (`POID`),
  ADD KEY `ProductDetailID` (`ProductDetailID`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ProductID`),
  ADD KEY `CategoryID` (`CategoryID`),
  ADD KEY `CreatedBy` (`CreatedBy`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Indexes for table `productdetail`
--
ALTER TABLE `productdetail`
  ADD PRIMARY KEY (`ProductDetailID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`RoleID`);

--
-- Indexes for table `section`
--
ALTER TABLE `section`
  ADD PRIMARY KEY (`SectionID`),
  ADD KEY `SectionTypeID` (`SectionTypeID`);

--
-- Indexes for table `sectiontype`
--
ALTER TABLE `sectiontype`
  ADD PRIMARY KEY (`SectionTypeID`),
  ADD KEY `WarehouseID` (`WarehouseID`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`SupplierID`);

--
-- Indexes for table `todetail`
--
ALTER TABLE `todetail`
  ADD PRIMARY KEY (`TODetailID`),
  ADD KEY `ProductDetailID` (`ProductDetailID`),
  ADD KEY `TOID` (`TOID`),
  ADD KEY `OriginBinId` (`OriginBinId`),
  ADD KEY `FinalBinId` (`FinalBinId`);

--
-- Indexes for table `transferorder`
--
ALTER TABLE `transferorder`
  ADD PRIMARY KEY (`TOID`),
  ADD KEY `CreatedBy` (`CreatedBy`);

--
-- Indexes for table `virtualbin`
--
ALTER TABLE `virtualbin`
  ADD PRIMARY KEY (`VirtualBinID`) USING BTREE;

--
-- Indexes for table `warehouse`
--
ALTER TABLE `warehouse`
  ADD PRIMARY KEY (`WarehouseID`),
  ADD KEY `BranchID` (`BranchID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `branch`
--
ALTER TABLE `branch`
  MODIFY `BranchID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `CategoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `RoleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sectiontype`
--
ALTER TABLE `sectiontype`
  MODIFY `SectionTypeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`),
  ADD CONSTRAINT `account_ibfk_2` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`);

--
-- Constraints for table `bin`
--
ALTER TABLE `bin`
  ADD CONSTRAINT `bin_ibfk_1` FOREIGN KEY (`SectionID`) REFERENCES `section` (`SectionID`);

--
-- Constraints for table `bindetail`
--
ALTER TABLE `bindetail`
  ADD CONSTRAINT `bindetail_ibfk_1` FOREIGN KEY (`binId`) REFERENCES `bin` (`BinID`),
  ADD CONSTRAINT `bindetail_ibfk_2` FOREIGN KEY (`ProductDetailId`) REFERENCES `productdetail` (`ProductDetailID`);

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `category_ibfk_1` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`);

--
-- Constraints for table `do`
--
ALTER TABLE `do`
  ADD CONSTRAINT `do_ibfk_1` FOREIGN KEY (`POID`) REFERENCES `po` (`POID`),
  ADD CONSTRAINT `do_ibfk_2` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `do_ibfk_3` FOREIGN KEY (`Recipient`) REFERENCES `employee` (`EmployeeID`);

--
-- Constraints for table `dodetail`
--
ALTER TABLE `dodetail`
  ADD CONSTRAINT `dodetail_ibfk_1` FOREIGN KEY (`ProductDetailID`) REFERENCES `productdetail` (`ProductDetailID`),
  ADD CONSTRAINT `dodetail_ibfk_2` FOREIGN KEY (`DOID`) REFERENCES `do` (`DOID`);

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`WarehouseID`) REFERENCES `warehouse` (`WarehouseID`);

--
-- Constraints for table `inventorydoc`
--
ALTER TABLE `inventorydoc`
  ADD CONSTRAINT `inventorydoc_ibfk_1` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `inventorydoc_ibfk_2` FOREIGN KEY (`BinID`) REFERENCES `bin` (`BinID`);

--
-- Constraints for table `inventorydocdetail`
--
ALTER TABLE `inventorydocdetail`
  ADD CONSTRAINT `inventorydocdetail_ibfk_1` FOREIGN KEY (`ProductDetailID`) REFERENCES `productdetail` (`ProductDetailID`),
  ADD CONSTRAINT `inventorydocdetail_ibfk_2` FOREIGN KEY (`CounterID`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `inventorydocdetail_ibfk_3` FOREIGN KEY (`ReCounterID`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `inventorydocdetail_ibfk_4` FOREIGN KEY (`InventoryDocID`) REFERENCES `inventorydoc` (`InventoryDocID`);

--
-- Constraints for table `po`
--
ALTER TABLE `po`
  ADD CONSTRAINT `po_ibfk_1` FOREIGN KEY (`SupplierID`) REFERENCES `supplier` (`SupplierID`),
  ADD CONSTRAINT `po_ibfk_2` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`);

--
-- Constraints for table `podetail`
--
ALTER TABLE `podetail`
  ADD CONSTRAINT `podetail_ibfk_1` FOREIGN KEY (`POID`) REFERENCES `po` (`POID`),
  ADD CONSTRAINT `podetail_ibfk_2` FOREIGN KEY (`ProductDetailID`) REFERENCES `productdetail` (`ProductDetailID`);

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_2` FOREIGN KEY (`CategoryID`) REFERENCES `category` (`CategoryID`),
  ADD CONSTRAINT `product_ibfk_3` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `product_ibfk_4` FOREIGN KEY (`SupplierID`) REFERENCES `supplier` (`SupplierID`);

--
-- Constraints for table `productdetail`
--
ALTER TABLE `productdetail`
  ADD CONSTRAINT `productdetail_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`);

--
-- Constraints for table `section`
--
ALTER TABLE `section`
  ADD CONSTRAINT `section_ibfk_1` FOREIGN KEY (`SectionTypeID`) REFERENCES `sectiontype` (`SectionTypeID`);

--
-- Constraints for table `sectiontype`
--
ALTER TABLE `sectiontype`
  ADD CONSTRAINT `sectiontype_ibfk_1` FOREIGN KEY (`WarehouseID`) REFERENCES `warehouse` (`WarehouseID`);

--
-- Constraints for table `todetail`
--
ALTER TABLE `todetail`
  ADD CONSTRAINT `todetail_ibfk_1` FOREIGN KEY (`ProductDetailID`) REFERENCES `productdetail` (`ProductDetailID`),
  ADD CONSTRAINT `todetail_ibfk_2` FOREIGN KEY (`TOID`) REFERENCES `transferorder` (`TOID`),
  ADD CONSTRAINT `todetail_ibfk_3` FOREIGN KEY (`OriginBinId`) REFERENCES `bin` (`BinID`),
  ADD CONSTRAINT `todetail_ibfk_4` FOREIGN KEY (`FinalBinId`) REFERENCES `bin` (`BinID`);

--
-- Constraints for table `transferorder`
--
ALTER TABLE `transferorder`
  ADD CONSTRAINT `transferorder_ibfk_1` FOREIGN KEY (`CreatedBy`) REFERENCES `employee` (`EmployeeID`);

--
-- Constraints for table `warehouse`
--
ALTER TABLE `warehouse`
  ADD CONSTRAINT `warehouse_ibfk_1` FOREIGN KEY (`BranchID`) REFERENCES `branch` (`BranchID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
