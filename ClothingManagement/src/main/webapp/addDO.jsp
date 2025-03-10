<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/7/2025
  Time: 10:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
  <head>
    <title>ADD DO</title>
    <!-- Meta Tags -->
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
      .container {
        width: 60%;
        margin: auto;
        text-align: center;

      }

      form {
        margin-bottom: 20px; /* Thêm khoảng cách giữa form và bảng */
        display: flex;
        flex-direction: column;
        align-items: center; /* Căn giữa nội dung */
        gap: 10px; /* Khoảng cách giữa các input */
      }

      table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 30px; /* Đẩy bảng xuống thấp hơn */
      }

      th, td {
        border: 1px solid black;
        padding: 10px;
        text-align: center;
      }

      th {
        background-color: #f2f2f2;
      }
    </style>

  </head>
  <body>
  <jsp:include page="include/sidebar.jsp"></jsp:include>

  <div class="container">
  <h2>Create Delivery Order</h2>
  <form id="poForm" action="AddDOServlet" method="post">
    <label for="supplier">Supplier:</label>

    <select name="supplier" id="supplier" required>
      <option value="">-- Select Suppliers --</option>
      <c:forEach var="sup" items="${suppliers}">
        <option value="${sup.supplierId}">${sup.supplierName}</option>
      </c:forEach>
    </select>

<%--    <input type="text" name="poID" placeholder="Enter PO ID">--%>
<%--    <input type="date" name="startDate" placeholder="startDate">--%>
<%--    <input type="date" name="endDate" placeholder="endDate">--%>
<%--    <input type="text" name="createdBy" placeholder="createdBy">--%>
    <button type="submit" name="action" value="filter">Filter</button>
  </form>


<table border="1">
<tr>
  <th>Select</th>
  <th>PO ID</th>
  <th>Created Date</th>
  <th>Supplier</th>
  <th>Created By</th>
  <th>Total Price</th>
</tr>
<c:forEach var="po" items="${poList}">
  <tr>
  <td>
    <form action="CreatedDOServlet" method="post">
      <input type="submit" name="poID" value="${po.poID}">
    </form>
  </td>
  <td>${po.poID}</td>
  <td>${po.createdDate}</td>
  <td>${supplierNames[po.supplierID] != null ? supplierNames[po.supplierID] : "Unknown Supplier"}</td>
  <td>${employeeNames[po.createdBy] != null ? employeeNames[po.createdBy] : "Unknown Employee"}</td>
  <td>${po.totalPrice}</td>
  </tr>
  </c:forEach>
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
