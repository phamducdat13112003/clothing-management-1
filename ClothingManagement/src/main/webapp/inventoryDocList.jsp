<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 1/28/2025
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Site Title -->
    <title>Inventory Doc List</title>

    <!-- Font -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">

    <!-- Fav Icon -->
    <link rel="icon" href="img/favicon.png">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">


    <!-- sherah Stylesheet -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome-all.min.css">
    <link rel="stylesheet" href="css/charts.min.css">
    <link rel="stylesheet" href="css/datatables.min.css">
    <link rel="stylesheet" href="css/jvector-map.css">
    <link rel="stylesheet" href="css/slickslider.min.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* Định dạng tổng thể */
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f7fc;
            color: #333;
            margin: 0;
            padding: 0;
        }

        /* Container chính */
        .container {
            /*max-width: 1200px;*/
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }

        /* Tiêu đề */
        h2 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 20px;
        }

        /* Bảng */
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            margin-top: 10px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 15px;
            text-align: center;
        }

        th {
            background: #3498db;
            color: white;
            text-transform: uppercase;
        }

        tr:nth-child(even) {
            background: #f2f2f2;
        }

        tr:hover {
            background: #d6eaf8;
            transition: 0.3s;
        }

        /* Nút bấm */
        button {
            background: #2ecc71;
            border: none;
            padding: 8px 12px;
            color: white;
            font-weight: bold;
            cursor: pointer;
            border-radius: 5px;
            transition: 0.3s;
        }

        button:hover {
            background: #27ae60;
        }

        /* Nút dành cho quản lý */
        button.confirm-btn {
            background: #e67e22;
        }

        button.confirm-btn:hover {
            background: #d35400;
        }

    </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <h2>Inventory Doc List</h2>
        <c:if test="${account.roleId == 1 }">
            <form action="CreateInventoryDocServlet" method="get"><%-- tạo kiểm kho--%>
                <button type="submit">Add New InventoryDoc</button>
            </form>
        </c:if>
        <div class="search-form">
            <form action="SearchInventoryDocServlet" method="get" class="d-flex">
                <input type="text" name="query" class="form-control me-2" placeholder="Enter BinID or InventoryDoc">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>
        <table border="1">
            <tr>
                <th>InventoryDocID</th>
                <th>CreatedBy</th>
                <th>CreatedDate</th>
                <th>BinID</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <c:forEach var="doc" items="${listDoc}">
                <tr>
                    <td>${doc.inventoryDocId}</td>
                    <td>${getCreatedByToEmployeeNameMap[doc.createdBy]}</td>
                    <td>${doc.createdDate}</td>
                    <td>${doc.binId}</td>
<%--                    <td>${account.employeeId}</td>--%>
                    <td>${doc.status}</td>
                    <td>
                        <div class="d-inline-flex gap-1">
                            <c:if test="${account.roleId == 4 && (doc.status == 'Pending' || doc.status == 'Recount')}">
                                <form action="CountInventoryServlet" method="get">
                                    <input type="hidden" name="binId" value="${doc.binId}">
                                    <input type="hidden" name="employee" value="${account.employeeId}">
                                    <input type="hidden" name="inventoryDocId" value="${doc.inventoryDocId}">
                                    <input type="hidden" name="status" value="${doc.status}">
                                    <button type="submit" class="btn btn-primary btn-sm" data-bs-toggle="tooltip" title="Count">
                                        <i class="bi bi-chevron-double-right"></i>
                                    </button>
                                </form>
                            </c:if>

                            <form action="ViewInventoryDocDetail" method="get">
                                <input type="hidden" name="binId" value="${doc.binId}">
                                <input type="hidden" name="inventoryDocId" value="${doc.inventoryDocId}">
                                <button type="submit" class="btn btn-info btn-sm" data-bs-toggle="tooltip" title="Detail">
                                    <i class="bi bi-search"></i>
                                </button>
                            </form>

                            <c:if test="${account.roleId == 1 && doc.status != 'Cancel' && doc.status != 'Pending' && doc.status != 'Recount' && doc.status != 'Done' && doc.status != 'Confirmed'}">
                                <form action="ClearDifferenceServlet" method="get">
                                    <input type="hidden" name="binId" value="${doc.binId}">
                                    <input type="hidden" name="inventoryDocId" value="${doc.inventoryDocId}">
                                    <input type="hidden" name="status" value="${doc.status}">
                                    <button type="submit" class="btn btn-success btn-sm" data-bs-toggle="tooltip" title="Confirm">
                                        <i class="bi bi-chevron-double-right"></i>
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${account.roleId == 1 && doc.status != 'Cancel' && doc.status != 'Confirmed' && doc.status != 'Done'}">
                                <form action="form" method="post">
                                    <input type="hidden" name="binId" value="${doc.binId}">
                                    <input type="hidden" name="inventoryDocId" value="${doc.inventoryDocId}">
                                    <input type="hidden" name="status" value="${doc.status}">
                                    <button type="submit" class="btn btn-danger btn-sm" data-bs-toggle="tooltip" title="Cancel">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${account.roleId == 1 && doc.status == 'Confirmed'}">
                                <form action="ClearDifferenceServlet" method="get">
                                    <input type="hidden" name="binId" value="${doc.binId}">
                                    <input type="hidden" name="inventoryDocId" value="${doc.inventoryDocId}">
                                    <input type="hidden" name="status" value="${doc.status}">
                                    <button type="submit" class="btn btn-warning btn-sm" data-bs-toggle="tooltip" title="Clear Diffrence">
                                        <i class="bi bi-chevron-double-right"></i>
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</section>
<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/charts.js"></script>
<script src="js/final-countdown.min.js"></script>
<script src="js/fancy-box.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/circle-progress.min.js"></script>
<script src="js/jquery-jvectormap.js"></script>
<script src="js/jvector-map.js"></script>
<script src="js/main.js"></script>
</body>
</html>

