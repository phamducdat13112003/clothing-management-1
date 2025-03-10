<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/7/2025
  Time: 8:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>View Delivery Order</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Font -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">

    <!-- Fav Icon -->
    <link rel="icon" href="img/favicon.png">

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
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f8f9fa; /* Màu nền nhẹ */
        }

        .container {
            width: 80%;
            max-width: 900px; /* Giới hạn chiều rộng */
            background: white;
            padding: 20px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }

        /* Đảm bảo form và bảng nằm trên dưới */
        .content {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 20px;
        }

        /* Form styling */
        .form-container {
            width: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
        }

        .form-container input,
        .form-container select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        /* Table container */
        .table-container {
            width: 100%;
            overflow-x: auto;
            display: flex;
            justify-content: center;
        }

        /* Table styling */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            min-width: 600px;
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        th {
            background-color: #f4f4f4;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        /* Buttons */
        button {
            padding: 10px 20px;
            margin: 10px;
            border: none;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border-radius: 5px;
        }

        button:hover {
            background-color: #0056b3;
        }

    </style>
</head>
<body>

<div class="container">
    <jsp:include page="include/sidebar.jsp"></jsp:include>

<h2>View Delivery Order</h2>
<form id="poForm" action="DeliveryOrderServlet" method="post">
    <label for="supplier">Supplier:</label>

    <select name="supplier" id="supplier" >
        <option value="">-- Select Suppliers --</option>
        <c:forEach var="sup" items="${suppliers}">
            <option value="${sup.supplierId}">${sup.supplierName}</option>
        </c:forEach>
    </select>

    <input type="text" name="poID" placeholder="Enter PO ID">
<%--    <input type="date" name="startDate" placeholder="startDate">--%>
<%--    <input type="date" name="endDate" placeholder="endDate">--%>
<%--    <input type="text" name="createdBy" placeholder="createdBy">--%>
    <button type="submit" name="action" value="filter">Filter</button>
    <button type="submit" name="action" value="selectAll">Select ALL</button>
</form>
<form action="AddDOServlet" method="get">
    <button type="submit">ADD DO</button>
</form>

<table border="1">
    <thead>
    <tr>
        <th>DOID</th>
        <th>Planned Shipping Date</th>
        <th>Receipt Date</th>
        <th>POID</th>
        <th>Created By</th>
        <th>Recipient</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="dos" items="${deliveryOrders}">
        <tr>
            <td>${dos.doID}</td>
            <td>${dos.plannedShippingDate}</td>
            <td>${dos.receiptDate == '1970-01-01' ? 'Not Receipted' : dos.receiptDate}</td>
            <td>${dos.poID}</td>
            <td>${createdByList[dos.createdBy] != null ? createdByList[dos.createdBy] : "Unknown CreatedBy"}</td>
            <td>${recipientList[dos.recipient] != null ? recipientList[dos.recipient] : "Unknown Receipient"}</td>
            <td>${dos.status ? 'Done' : 'Not Done'}</td>
            <td>

<%--                <form action="DeleteDOServlet" method="get" style="display:inline;">--%>
<%--                    <input type="hidden" name="doId" value="${dos.doID}">--%>
<%--                    <button type="submit">Delete</button>--%>
<%--                </form>--%>

    <c:if test="${!dos.status}">
        <form action="UpdateDOServlet" method="get" style="display:inline;">
            <input type="hidden" name="poId" value="${dos.poID}">
            <input type="hidden" name="doId" value="${dos.doID}">
            <button type="submit">Update</button>
        </form>
    </c:if>


    <form action="DODetailServlet" method="get" style="display:inline;">
                    <input type="hidden" name="doId" value="${dos.doID}">
                    <button type="submit">Detail</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/charts.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/circle-progress.min.js"></script>
<script src="js/jquery-jvectormap.js"></script>
<script src="js/jvector-map.js"></script>
<script src="js/main.js"></script>

</body>
</html>
