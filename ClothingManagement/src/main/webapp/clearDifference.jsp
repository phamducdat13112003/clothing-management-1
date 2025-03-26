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
    <title>Clear Difference</title>

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

        #staffInput {
            margin-top: 15px;
            padding: 10px;
            background: #f9f9f9;
            border-radius: 5px;
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
        <h2>Confirm Inventory Doc</h2>

        <form id="actionForm" method="post">
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
                        <td>
                            <input type="hidden" name="productDetailId[]" value="${detail.productDetailId}">
                                ${detail.productDetailId}
                        </td>
                        <td>${detail.productName}</td> <!-- Hiển thị tên sản phẩm -->
                        <td>${detail.color}</td>
                        <td>${detail.size}</td>
                        <td>${detail.originalQuantity}</td>
                        <td>
                            <input type="hidden" name="recountQuantity[]" value="${detail.recountQuantity}">
                            <c:choose>
                                <c:when test="${detail.recountQuantity == -1}">Uncounted</c:when>
                                <c:otherwise>${detail.recountQuantity}</c:otherwise>
                            </c:choose>
                        </td>
                        <td> <c:set var="diff" value="${(detail.recountQuantity == -1 ? 0 : detail.recountQuantity)-detail.originalQuantity}" />

                            <input type="hidden" name="difference[]" value="${diff}">${diff}</td>
                    </tr>
                </c:forEach>
            </table>

            <input type="hidden" name="inventoryDocId" value="${inventoryDocId}">
            <input type="hidden" name="binId" value="${binId}">
            <input type="hidden" name="need" value="${need}">

            <c:set var="hasRecountId" value="false"/>
            <c:forEach var="detail" items="${listInvenDoc}">
                <c:if test="${detail.recounterId != null}">
                    <c:set var="hasRecountId" value="true"/>
                </c:if>
            </c:forEach>

            <c:if test="${account.roleId == 1}">
            <label>Select Action:</label>
            <div>
                <c:if test="${need ne 'daydu' and status eq 'Confirmed' and status ne 'Done'}">
                    <button type="button" onclick="handleAction('clearDifference')">Clear Difference</button>
                </c:if>
                <c:if test="${status ne 'Cancel' and status ne 'Confirmed' and status ne 'Done'}">
                    <button type="button" onclick="handleAction('confirm')">Confirm</button>
                </c:if>
                <c:if test="${!hasRecountId and status ne 'Cancel' and status ne 'Confirmed' and status ne 'Done'}">
                    <button type="button" onclick="handleAction('recount')">Recount</button>
                </c:if>
                <c:if test="${status ne 'Cancel' and status ne 'Confirmed' and status ne 'Done'}">
                    <button type="button" onclick="handleAction('cancel')">Cancel</button>
                </c:if>
            </div>

            <!-- Ô nhập nhân viên (ẩn mặc định) -->
            <div id="staffInput" style="display: none;">
                <label>Employee:</label>
                <select name="employee">
                    <option value="">-- Assign Employee Recount --</option>
                    <c:forEach var="employee" items="${employeeList}">
                        <option value="${employee.employeeID}">${employee.employeeName}</option>
                    </c:forEach>
                </select>
                <button type="submit" id="recountSubmit" formaction="RecountInventoryServlet">Recount</button>
            </div>
            </c:if>

            <script>
                function handleAction(action) {
                    var form = document.getElementById("actionForm");
                    var staffInput = document.getElementById("staffInput");

                    if (action === "clearDifference") {
                        if (confirm("Do you want to Clear Difference?")) {
                            form.action = "ClearDifferenceServlet";
                            form.submit();
                        }
                    } else if (action === "confirm") {
                        if (confirm("Do you really want to Confirm Inventory?")) {
                            form.action = "ConfirmInvenServlet";
                            form.submit();
                        }
                    } else if (action === "cancel") {
                        if (confirm("Do you really want to Cancel?")) {
                            form.action = "CancelInventoryServlet"; // Cập nhật đúng servlet xử lý
                            form.submit();
                        }
                    } else if (action === "recount") {
                        staffInput.style.display = "block"; // Hiện ô nhập nhân viên
                    } else {
                        staffInput.style.display = "none"; // Ẩn ô nhập nhân viên nếu không chọn Recount
                    }
                }
            </script>


        <%--            <c:if test="${account.roleId == 1}">--%>

<%--            <label>Select Action:</label>--%>
<%--            <select id="actionSelect" onchange="handleSelectionChange()">--%>
<%--                <option value="">-- Select --</option>--%>

<%--                <c:if test="${need ne 'daydu' and status eq 'Confirmed'and status ne 'Done'}">--%>
<%--                    <option value="clearDifference">Clear Difference</option>--%>
<%--                </c:if>--%>
<%--                <c:if test="${status ne 'Cancel' and status ne 'Confirmed'and status ne 'Done'}">--%>
<%--                <option value="confirm">Confirm</option>--%>
<%--                </c:if>--%>
<%--                <c:if test="${!hasRecountId and status ne 'Cancel' and status ne 'Confirmed'and status ne 'Done'}">--%>
<%--                    <option value="recount">Recount</option>--%>
<%--                </c:if>--%>

<%--                <c:if test="${status ne 'Cancel' and status ne 'Confirmed'and status ne 'Done'}">--%>
<%--                <option value="cancel">Cancel</option>--%>
<%--                </c:if>--%>
<%--            </select>--%>

<%--            <!-- Ô nhập nhân viên (ẩn mặc định) -->--%>
<%--            <div id="staffInput" style="display: none;">--%>
<%--                <label>Employee:</label>--%>
<%--                <select name="employee">--%>
<%--                    <option value="">-- Assign Employee Recount --</option>--%>
<%--                    <c:forEach var="employee" items="${employeeList}">--%>
<%--                        <option value="${employee.employeeID}">${employee.employeeName}</option>--%>
<%--                    </c:forEach>--%>
<%--                </select>--%>
<%--                <button type="submit" id="recountSubmit" formaction="RecountInventoryServlet">Recount</button>--%>
<%--            </div>--%>
<%--            </c:if>--%>
<%--        </form>--%>

<%--        <script>--%>
<%--            function handleSelectionChange() {--%>
<%--                var actionSelect = document.getElementById("actionSelect");--%>
<%--                var staffInput = document.getElementById("staffInput");--%>
<%--                var form = document.getElementById("actionForm");--%>

<%--                if (actionSelect.value === "clearDifference") {--%>
<%--                    var confirmAction = confirm("Do you want to Clear Difference?");--%>
<%--                    if (confirmAction) {--%>
<%--                        form.action = "ClearDifferenceServlet";--%>
<%--                        form.submit();--%>
<%--                    } else {--%>
<%--                        actionSelect.value = ""; // Giữ trạng thái ban đầu--%>
<%--                    }--%>
<%--                }--%>
<%--                else if (actionSelect.value === "recount") {--%>
<%--                    staffInput.style.display = "block"; // Hiện ô nhập nhân viên--%>
<%--                }--%>
<%--                else if (actionSelect.value === "confirm") {--%>
<%--                    var confirmAction = confirm("Do you really want to Confirm Inventory?");--%>
<%--                    if (confirmAction) {--%>
<%--                        form.action = "ConfirmInvenServlet";--%>
<%--                        form.submit();--%>
<%--                    } else {--%>
<%--                        actionSelect.value = ""; // Giữ trạng thái ban đầu--%>
<%--                    }--%>
<%--                }--%>
<%--                else if (actionSelect.value === "cancel") {--%>
<%--                    var confirmCancel = confirm("Do you really want to Cancel?");--%>
<%--                    if (confirmCancel) {--%>
<%--                        form.action = "form";--%>
<%--                        form.submit();--%>
<%--                    } else {--%>
<%--                        actionSelect.value = ""; // Giữ trạng thái ban đầu--%>
<%--                    }--%>
<%--                }--%>
<%--                else {--%>
<%--                    staffInput.style.display = "none"; // Ẩn ô nhập nhân viên nếu không chọn Recount--%>
<%--                }--%>
<%--            }--%>
<%--        </script>--%>

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