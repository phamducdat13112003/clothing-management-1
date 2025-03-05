<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/3/2025
  Time: 7:13 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>View DO List </title>
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
      padding: 20px;
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
      width: 60%;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;
    }

    .form-container input {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    /* Table container */
    .table-container {
      width: 100%;
      overflow-x: auto; /* Cho phép cuộn ngang nếu bảng quá lớn */
      display: flex;
      justify-content: center;
    }

    /* Table styling */
    table {
      width: 80%;
      border-collapse: collapse;
      margin-top: 20px;
      min-width: 600px; /* Đảm bảo bảng không quá nhỏ */
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
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<div class="container">
<form action="GetDOServlet" method="post">
  <table border="1">
    <tr>
      <th>DOID</th>
      <th>Planned Shipping Date</th>
      <th>POID</th>
      <th>Created By</th>
      <th>Action</th>

    </tr>
    <c:forEach var="dod" items="${DOs}">
      <tr>
        <td>${dod.doID}</td>
        <td>${dod.plannedShippingDate}</td>
        <td>${dod.poID}</td>
        <td>${dod.createdBy}</td>
        <td>
<%--          <a href="UpdateDOServlet?doID=${dod.doID}">Update</a> |--%>
          <a href="DeleteDOServlet?doID=${dod.doID}" onclick="return confirm('Are you sure you want to delete this item?');">Delete</a> |
<%--          <a href="ViewDODetailServlet?doID=${dod.doID}">View Detail</a>--%>
        </td>
      </tr>
    </c:forEach>
  </table>
  <button type="submit">Submit</button>
</form>
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
