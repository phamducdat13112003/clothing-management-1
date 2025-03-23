<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 3/23/2025
  Time: 8:08 AM
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
    <title>Confirm TO</title>

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
        .error-message {
            color: red;
            font-size: 12px;
        }

        .message {
            color: green;
            font-size: 12px;
        }
        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .pagination a {
            padding: 8px 12px;
            margin: 0 5px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
        }

        .pagination a:active {
            background-color: #09ad95;
            color: white;
            font-weight: bold;
        }

        .pagination a:hover {
            background-color: #ddd;
        }

        .search-form {
            display: flex;
            justify-content: flex-end;
            align-items: center;
        }

        .search-input {
            padding: 6px 12px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 5px;
            width: 250px;
        }

        .search-button {
            padding: 5px 15px;
            background-color: #09ad95;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .search-button:hover {
            background-color: #078c76;
        }
    </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="sherah-body">
                    <div class="sherah-dsinner">
                        <div class="row align-items-center justify-content-between">
                            <div class="col-6">
                                <div class="sherah-breadcrumb mg-top-30">
                                    <h2 class="sherah-breadcrumb__title">Confirm Transfer Order List</h2>
                                    <ul class="sherah-breadcrumb__list">
                                        <li><a href="confirmTO">Home</a></li>
                                    </ul>
                                    <c:if test="${not empty message}">
                                        <span class="error-message">${message}</span>
                                    </c:if>
                                    <c:if test="${not empty messageSuccess}">
                                        <span class="message">${messageSuccess}</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="col-6">
                                <form action="confirmTO" method="post" class="search-form">
                                    <input type="text" name="search" placeholder="Search..." value="${search}"
                                           class="search-input">
                                    <button type="submit" class="search-button">Search</button>
                                </form>
                            </div>
                        </div>
                        <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                            <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                                <thead class="sherah-table__head">
                                <tr>
                                    <th class="sherah-table__column-1 sherah-table__h2">TOID</th>
                                    <th class="sherah-table__column-2 sherah-table__h1">Created By</th>
                                    <th class="sherah-table__column-2 sherah-table__h2">Status</th>
                                    <th class="sherah-table__column-2 sherah-table__h2">Created Date</th>
                                    <th class="sherah-table__column-2 sherah-table__h2">Action</th>
                                </tr>
                                </thead>
                                <tbody class="sherah-table__body">
                                <c:if test="${not empty transferOrders}">
                                <c:forEach var="order" items="${transferOrders}">
                                <tr>
                                    <td class="sherah-table__column-2 sherah-table__data-2">
                                        <div class="sherah-table__product-content">
                                            <a href="${pageContext.request.contextPath}/TODetail?toID=${order.toID}">${order.toID}</a>
                                        </div>
                                    </td>
                                    <td class="sherah-table__column-2 sherah-table__data-2">
                                        <div class="sherah-table__product-content">
                                            <p class="sherah-table__product-desc">${order.createdBy}</p>
                                        </div>
                                    </td>
                                    <td class="sherah-table__column-2 sherah-table__data-2">
                                        <div class="sherah-table__product-content">
                                            <p class="sherah-table__product-desc">${order.status}</p>
                                        </div>
                                    </td>
                                    <td class="sherah-table__column-2 sherah-table__data-2">
                                        <div class="sherah-table__product-content">
                                            <p class="sherah-table__product-desc">${order.createdDate}</p>
                                        </div>
                                    </td>
                                    <td class="sherah-table__column-2 sherah-table__data-2">
                                        <!-- Link to edit the transfer order -->
                                        <a href="${pageContext.request.contextPath}/confirmTO?toID=${order.toID}">Update</a>
                        </div>
                        </td>
                        </tr>
                        </c:forEach>
                        </c:if>
                        <c:if test="${empty transferOrders}">
                            <tr>
                                <td colspan="12" class="text-center">No transfer order status pending</td>
                            </tr>
                        </c:if>
                        </tbody>
                        </table>
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="confirmTO?page=${currentPage - 1}&search=${search}">Previous</a>
                            </c:if>

                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <a href="confirmTO?page=${i}&search=${search}"
                                   class="${i == currentPage ? 'active' : ''}">${i}</a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="confirmTO?page=${currentPage + 1}&search=${search}">Next</a>
                            </c:if>
                        </div>
                    </div>
                </div>
                <!-- End Dashboard Inner -->
            </div>
        </div>
    </div>

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
