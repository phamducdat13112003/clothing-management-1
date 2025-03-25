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
  <title>Inventory Detail</title>

  <!-- Font -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">

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
    table {
      width: 100%;
      border-collapse: collapse;
      margin: 20px 0;
      font-size: 16px;
      text-align: left;
    }

    h2 {
      text-align: center;
      color: #2c3e50;
      margin-bottom: 20px;
    }

    th, td {
      padding: 12px;
      border: 1px solid #ddd;
    }

    th {
      background-color: #4CAF50;
      color: white;
    }

    tr:nth-child(even) {
      background-color: #f2f2f2;
    }

    tr:hover {
      background-color: #ddd;
    }

    select, input, button {
      padding: 10px;
      margin: 5px;
      font-size: 16px;
      border-radius: 5px;
    }

    select {
      border: 1px solid #ccc;
    }

    button {
      background-color: #007bff;
      color: white;
      border: none;
      cursor: pointer;
    }

    button:hover {
      background-color: #0056b3;
    }

    label {
      font-weight: bold;
    }

  </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
  <div class="container">
<h2>View InventoryDoc Detail</h2>
      <table border="1">
        <tr>
          <th>Product Detail ID</th>
          <th>Product Name</th>
          <th>Color</th>
          <th>Size</th>
          <th>Original Quantity</th>
          <th>Recount Quantity</th>
          <th>Difference</th>
        </tr>
        <c:forEach var="detail" items="${listInvenDoc}">
          <tr>
            <td>${detail.productDetailId}</td>
            <td>${detail.productName}</td>
            <td>${detail.color}</td>
            <td>${detail.size}</td>
            <td>${detail.originalQuantity}</td>
            <td>
              <c:choose>
                <c:when test="${detail.recountQuantity == -1}">Uncounted</c:when>
                <c:otherwise>${detail.recountQuantity}</c:otherwise>
              </c:choose>
            </td>
            <td>${(detail.recountQuantity == -1 ? 0 : detail.recountQuantity)-detail.originalQuantity}</td>
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