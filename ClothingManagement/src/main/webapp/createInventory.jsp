mau
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
  <title>Create InventoryDoc</title>

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
    /* Tạo giao diện đẹp hơn cho form */
    form {
      background: #f9f9f9;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      max-width: 600px;
      margin: auto;
    }

    /* Căn chỉnh label */
    label {
      font-weight: bold;
      display: block;
      margin-bottom: 8px;
    }

    /* Style cho select và input */
    select, input[type="radio"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
      font-size: 16px;
      background: white;
    }

    /* Style cho bảng */
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 15px;
      background: white;
      border-radius: 8px;
      overflow: hidden;
    }

    table th, table td {
      padding: 12px;
      border: 1px solid #ddd;
      text-align: center;
    }

    table th {
      background: #007BFF;
      color: white;
    }

    /* Style nút bấm */
    button {
      background: #28a745;
      color: white;
      border: none;
      padding: 10px 15px;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
      display: block;
      width: 100%;
      margin-top: 10px;
    }

    button:hover {
      background: #218838;
    }

  </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
  <div class="container">
    <h2>Create InventoryDoc</h2>

    <form>
      <label for="sectionType">Select Section Type:</label>
      <select id="sectionType" name="sectionTypeID" onchange="fetchSections()">
        <option value="">-- Select Section Type --</option>
        <c:forEach var="type" items="${sectionTypes}">
          <option value="${type.sectionTypeId}" ${param.sectionTypeID == type.sectionTypeId ? 'selected' : ''}>${type.sectionTypeName}</option>
        </c:forEach>
      </select>

      <c:if test="${not empty sections}">
        <label for="section">Select Section:</label>
        <select id="section" name="sectionID" onchange="fetchBins()">
          <option value="">-- Select Section --</option>
          <c:forEach var="section" items="${sections}">
            <option value="${section.sectionID}" ${param.sectionID == section.sectionID ? 'selected' : ''}>${section.sectionName}</option>
          </c:forEach>
        </select>
      </c:if>
    </form>


    <c:if test="${not empty binsStatusOne}">
      <form action="CreateInventoryDocServlet" method="post">
        <select name="employee" required>
          <option value="">-- Assign Employee --</option>
          <c:forEach var="employee" items="${employeeList}">
            <option value="${employee.employeeID}">${employee.employeeName}</option>
          </c:forEach>
        </select>
        <table border="1">
          <tr>
            <th>Chọn</th>
            <th>Bin ID</th>
            <th>Bin Name</th>
            <th>Max Capacity</th>
          </tr>
          <c:forEach var="bin" items="${binsStatusOne}">
            <tr>
              <td>
                <input type="radio" name="binId" value="${bin.binID}" required>
              </td>
              <td>${bin.binID}</td>
              <td>${bin.binName}</td>
              <td>${bin.maxCapacity}</td>
            </tr>
          </c:forEach>
        </table>
        <br>
        <button type="submit">Create New Inventory Doc</button>
      </form>
    </c:if>


    <script>
      function fetchSections() {
        var sectionTypeID = document.getElementById("sectionType").value;
        if (sectionTypeID) {
          window.location.href = "form?sectionTypeID=" + sectionTypeID;
        }
      }

      function fetchBins() {
        var sectionID = document.getElementById("section").value;
        if (sectionID) {
          window.location.href = "form?sectionTypeID=${param.sectionTypeID}&sectionID=" + sectionID;
        }
      }
    </script>

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

