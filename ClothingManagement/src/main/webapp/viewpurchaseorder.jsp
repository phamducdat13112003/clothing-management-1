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
    <link rel="stylesheet" href="css/styleviewpo.css">
    <style>
        .btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            border: none;
            padding: 6px 12px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .btn i {
            font-size: 1rem;
        }

        .btn-warning:hover {
            background-color: #d39e00;
        }

        .btn-success:hover {
            background-color: #218838;
        }

        .btn-danger:hover {
            background-color: #c82333;
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
                                               placeholder="Search PO ID, CreateDate, Supplier, Employee, TotalPrice, Status,...">
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
                                        <th class="sherah-table__column-4 sherah-table__h4">Employee</th>
                                        <th class="sherah-table__column-5 sherah-table__h5">Status</th>
                                        <th class="sherah-table__column-6 sherah-table__h6">TotalPrice</th>
                                        <th class="sherah-table__column-6 sherah-table__h6">Action</th>
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
<%--                                                        <a href="purchaseorderdetail?poID=${listpo.poID}"> ${listpo.status}</a>--%>
                                                        <a href=""> ${listpo.status}</a>
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
                                            <td class="sherah-table__column-7 sherah-table__data-7">
                                                <div class="sherah-table__product-content d-flex gap-2">
                                                    <c:if test='${listpo.status == "Pending"}'>
                                                        <!-- Nút Edit -->
                                                        <a href="updateinfomationpo?poid=${listpo.poID}"
                                                           class="btn btn-warning text-white d-flex align-items-center gap-1">
                                                            <i class="bi bi-pencil-square"></i> Edit
                                                        </a>

                                                        <!-- Nút Confirm -->
                                                        <form action="updatestatuspurchaseorder" method="GET" class="d-inline">
                                                            <input type="hidden" name="poid" value="${listpo.poID}">
                                                            <input type="hidden" name="status" value="Confirmed">
                                                            <button type="submit" class="btn btn-success text-white d-flex align-items-center gap-1"
                                                                    onclick="return confirm('Are you sure you want to confirm this PO?');">
                                                                <i class="bi bi-check-circle"></i> Confirm
                                                            </button>
                                                        </form>

                                                        <!-- Nút Cancel -->
                                                        <form action="updatestatuspurchaseorder" method="GET" class="d-inline">
                                                            <input type="hidden" name="poid" value="${listpo.poID}">
                                                            <input type="hidden" name="status" value="Cancel">
                                                            <button type="submit" class="btn btn-danger text-white d-flex align-items-center gap-1"
                                                                    onclick="return confirm('Are you sure you want to cancel this PO?');">
                                                                <i class="bi bi-x-circle"></i> Cancel
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <c:if test='${listpo.status == "Confirmed"}'>
                                                        <!-- Nút Edit -->
                                                        <a href="updateinfomationpo?poid=${listpo.poID}"
                                                           class="btn btn-warning text-white d-flex align-items-center gap-1">
                                                            <i class="bi bi-pencil-square"></i> Edit
                                                        </a>
                                                        <!-- Nút Cancel -->
                                                        <form action="updatestatuspurchaseorder" method="GET" class="d-inline">
                                                            <input type="hidden" name="poid" value="${listpo.poID}">
                                                            <input type="hidden" name="status" value="Cancel">
                                                            <button type="submit" class="btn btn-danger text-white d-flex align-items-center gap-1"
                                                                    onclick="return confirm('Are you sure you want to cancel this PO?');">
                                                                <i class="bi bi-x-circle"></i> Cancel
                                                            </button>
                                                        </form>
                                                    </c:if>
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
        let pagesToShow = 5; // Số trang hiển thị trong dãy
        let totalPages = Math.ceil(rows.length / rowsPerPage);
        let startPage = 1; // Trang bắt đầu của dãy

        function displayTablePage(page) {
            let start = (page - 1) * rowsPerPage;
            let end = start + rowsPerPage;
            rows.forEach((row, index) => {
                row.style.display = index >= start && index < end ? "table-row" : "none";
            });
        }

        function createPaginationButtons() {
            let paginationContainer = document.getElementById("pagination");
            paginationContainer.innerHTML = "";

            if (totalPages > pagesToShow) {
                let prevBtn = document.createElement("button");
                prevBtn.innerText = "Previous";
                prevBtn.disabled = startPage === 1;
                prevBtn.addEventListener("click", function () {
                    if (startPage > 1) {
                        startPage -= pagesToShow;
                        updatePagination();
                    }
                });
                paginationContainer.appendChild(prevBtn);
            }

            let endPage = Math.min(startPage + pagesToShow - 1, totalPages);
            for (let i = startPage; i <= endPage; i++) {
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

            if (totalPages > pagesToShow) {
                let nextBtn = document.createElement("button");
                nextBtn.innerText = "Next";
                nextBtn.disabled = endPage === totalPages;
                nextBtn.addEventListener("click", function () {
                    if (endPage < totalPages) {
                        startPage += pagesToShow;
                        updatePagination();
                    }
                });
                paginationContainer.appendChild(nextBtn);
            }
        }

        function updatePagination() {
            createPaginationButtons();
            displayTablePage(currentPage);
            updateActiveButton();
        }

        function updateActiveButton() {
            let buttons = document.querySelectorAll(".pagination-btn");
            buttons.forEach((btn, index) => {
                btn.classList.toggle("active", parseInt(btn.innerText) === currentPage);
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
