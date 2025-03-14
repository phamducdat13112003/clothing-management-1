<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2/27/2025
  Time: 5:05 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html class="no-js" lang="zxx">
<head>
    <!-- Meta Tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Site Title -->
    <title>Sherah - HTML eCommerce Dashboard Template</title>
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
        .sherah-popup {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .sherah-popup-content {
            background-color: #fff;
            margin: 15% auto;
            padding: 20px;
            width: 50%;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5);
            border-radius: 5px;
            animation: fadeIn 0.3s ease-in-out;
        }

        .sherah-popup-close {
            float: right;
            font-size: 24px;
            cursor: pointer;
        }

        .popup-title {
            text-align: center;
            font-size: 22px;
            color: #2c3e50;
            font-weight: bold;
        }

        .popup-body {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }

        .supplier-info, .product-info, .order-info {
            width: 48%;
            background: #ecf0f1;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 10px;
        }

        .product-image {
            display: block;
            width: 100px;
            height: 100px;
            object-fit: cover;
            margin-bottom: 10px;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: scale(0.9);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }

        .search-form {
            display: flex;
            align-items: center;
            gap: 10px; /* Khoảng cách giữa input và button */
        }

        .search-form input {
            flex: 1; /* Input mở rộng để chiếm hết không gian trống */
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .search-form button {
            padding: 8px 12px;
            border-radius: 5px;
            background-color: #6c757d;
            color: white;
            border: none;
            cursor: pointer;
        }

        .search-form button:hover {
            background-color: #5a6268;
        }

        .pagination-container {
            margin-top: 15px;
            text-align: center;
        }

        .pagination-btn {
            margin: 2px;
            padding: 5px 10px;
            border: 1px solid #ccc;
            cursor: pointer;
        }

        .pagination-btn.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        /* Định dạng chung cho các thẻ <a> */
        .sherah-table__product-content a {
            font-weight: bold;
            text-decoration: none;
            padding: 4px 8px;
            border-radius: 5px;
            transition: all 0.3s ease-in-out;
            display: inline-block;
        }

        /* Màu sắc theo trạng thái */
        .status-pending a {
            color: #f39c12; /* Cam vàng */
            background: rgba(243, 156, 18, 0.1);
            border: 1px solid #f39c12;
        }

        .status-confirmed a {
            color: #3498db; /* Xanh dương */
            background: rgba(52, 152, 219, 0.1);
            border: 1px solid #3498db;
        }

        .status-processing a {
            color: #e67e22; /* Cam */
            background: rgba(230, 126, 34, 0.1);
            border: 1px solid #e67e22;
        }

        .status-done a {
            color: #2ecc71; /* Xanh lá */
            background: rgba(46, 204, 113, 0.1);
            border: 1px solid #2ecc71;
        }

        .status-cancel a {
            color: #e74c3c; /* Đỏ */
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid #e74c3c;
        }

        /* Hiệu ứng hover */
        .sherah-table__product-content a:hover {
            background: rgba(0, 0, 0, 0.1);
            color: #000;
            transform: scale(1.05);
        }



    </style>

</head>
<body id="sherah-dark-light">
<div class="sherah-body-area">
    <jsp:include page="include/sidebar.jsp"></jsp:include>
    <jsp:include page="include/header.jsp"></jsp:include>
    <!-- sherah Dashboard -->
    <section class="sherah-adashboard sherah-show">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="sherah-body">
                        <!-- Dashboard Inner -->
                        <div class="sherah-dsinner">
                            <div class="row mg-top-30">
                                <div class="col-12 sherah-flex-between">
                                    <!-- Sherah Breadcrumb -->
                                    <div class="sherah-breadcrumb">

                                        <h2 class="sherah-breadcrumb__title">Purchase Order list</h2>
                                        <ul class="sherah-breadcrumb__list">
                                            <li><a href="addpurchaseorder.jsp">Home</a></li>
                                            <li class="active"><a href="#">Purchase Order List</a></li>
                                        </ul>
                                    </div>
                                    <!-- End Sherah Breadcrumb -->
                                    <a href="addinfomationpo" class="sherah-btn sherah-gbcolor">Add New
                                        PurchaseOrder</a>
                                </div>
                            </div>
                            <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                                <c:if test="${not empty addposuccessfully}">
                                    <div class="alert" style="color: green">${addposuccessfully}</div>
                                    <% session.removeAttribute("addposuccessfully"); %> <!-- Xóa sau khi hiển thị -->
                                </c:if>
                                <c:if test="${not empty updatepostatussuccessfully}">
                                    <div class="alert" style="color: green">${updatepostatussuccessfully}</div>
                                    <% session.removeAttribute("updatepostatussuccessfully"); %> <!-- Xóa sau khi hiển thị -->
                                </c:if>
                                <c:if test="${not empty updateposuccessfully}">
                                    <div class="alert" style="color: green">${updateposuccessfully}</div>
                                    <% session.removeAttribute("updateposuccessfully"); %> <!-- Xóa sau khi hiển thị -->
                                </c:if>
                                <div class="search-container">
                                    <form action="searchpurchaseorder" method="GET" class="search-form">
                                        <input value="${txtS}" type="text" id="customSearch" name="txt"
                                               placeholder="Search PO ID, CreateDate, Supplier, CreateBy, TotalPrice, Status,...">
                                        <button type="submit" class="btn btn-secondary btn-number">
                                            <i class="fa fa-search"></i>
                                        </button>
                                    </form>
                                </div>
                                <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                                    <!-- sherah Table Head -->
                                    <thead class="sherah-table__head">
                                    <tr>
                                        <th class="sherah-table__column-1 sherah-table__h1">PO ID</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Crated Date</th>
                                        <th class="sherah-table__column-3 sherah-table__h3">Supplier Name</th>
                                        <th class="sherah-table__column-4 sherah-table__h4">CreatBy</th>
                                        <th class="sherah-table__column-5 sherah-table__h5">Status</th>
                                        <th class="sherah-table__column-6 sherah-table__h6">TotalPrice</th>
                                    </tr>
                                    </thead>
                                    <tbody class="sherah-table__body">
                                    <c:forEach items="${purchaseOrderList}" var="listpo">
                                        <tr> <!-- Thêm thẻ <tr> ở đây -->
                                            <td class="sherah-table__column-1 sherah-table__data-1">
                                                <div class="sherah-language-form__input">
                                                    <p class="crany-table__product--number">
                                                        <a href="purchaseorderdetail?poID=${listpo.poID}"
                                                           class="sherah-color1">${listpo.poID}</a>
                                                    </p>
                                                </div>
                                            </td>
                                            <td class="sherah-table__column-2 sherah-table__data-2">
                                                <p class="sherah-table__product-desc">${listpo.createdDate}</p>
                                            </td>
                                            <td class="sherah-table__column-3 sherah-table__data-3">
                                                <div class="sherah-table__product-content">
                                                    <p class="sherah-table__product-desc">${listpo.supplierID}</p>
                                                </div>
                                            </td>
                                            <td class="sherah-table__column-4 sherah-table__data-4">
                                                <div class="sherah-table__product-content">
                                                    <div class="sherah-table__status sherah-color1 sherah-color1__bg--opactity">
                                                        <a href="javascript:void(0);" class="sherah-color1"
                                                           onclick="openPopup('popupCreatedBy', '${listpo.createdBy}')">
                                                                ${listpo.createdBy}
                                                        </a>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="sherah-table__column-5 sherah-table__data-5">
                                                <div class="sherah-table__product-content">
                                                    <p class="
                                                    <c:choose>
                                                    <c:when test="${listpo.status == 'Pending'}">status-pending</c:when>
                                                    <c:when test="${listpo.status == 'Confirmed'}">status-confirmed</c:when>
                                                    <c:when test="${listpo.status == 'Processing'}">status-processing</c:when>
                                                    <c:when test="${listpo.status == 'Done'}">status-done</c:when>
                                                    <c:when test="${listpo.status == 'Cancel'}">status-cancel</c:when>
                                                    </c:choose>">
                                                        <a href="purchaseorderdetail?poID=${listpo.poID}"> ${listpo.status}</a>
                                                    </p>
                                                </div>
                                            </td>
                                            <td class="sherah-table__column-6 sherah-table__data-6">
                                                <div class="sherah-table__product-content">
                                                    <p class="sherah-table__product-desc">
                                                        <fmt:formatNumber value="${listpo.totalPrice}" type="number"
                                                                          groupingUsed="true"/> VNĐ
                                                    </p>
                                                </div>
                                            </td>
                                        </tr>
                                        <!-- Đóng thẻ <tr> -->
                                    </c:forEach>
                                    </tbody>

                                </table>
                                <div id="pagination" class="pagination-container"></div>
                            </div>
                        </div>
                        <!-- End Dashboard Inner -->
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- End sherah Dashboard -->
</div>

<!-- sherah Scripts -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        let table = document.querySelector(".sherah-table__body");
        let rows = table.querySelectorAll("tr");
        let rowsPerPage = 5; // Số dòng trên mỗi trang
        let currentPage = 1;

        function displayTablePage(page) {
            let start = (page - 1) * rowsPerPage;
            let end = start + rowsPerPage;
            rows.forEach((row, index) => {
                row.style.display = index >= start && index < end ? "table-row" : "none";
            });
        }

        function createPaginationButtons() {
            let totalPages = Math.ceil(rows.length / rowsPerPage);
            let paginationContainer = document.getElementById("pagination");
            paginationContainer.innerHTML = "";

            for (let i = 1; i <= totalPages; i++) {
                let btn = document.createElement("button");
                btn.innerText = i;
                btn.classList.add("pagination-btn");
                if (i === currentPage) {
                    btn.classList.add("active");
                }
                btn.addEventListener("click", function () {
                    currentPage = i;
                    displayTablePage(currentPage);
                    updateActiveButton();
                });
                paginationContainer.appendChild(btn);
            }
        }

        function updateActiveButton() {
            let buttons = document.querySelectorAll(".pagination-btn");
            buttons.forEach((btn, index) => {
                btn.classList.toggle("active", index + 1 === currentPage);
            });
        }

        displayTablePage(currentPage);
        createPaginationButtons();
    });

</script>
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
