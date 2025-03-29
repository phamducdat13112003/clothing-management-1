<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <!-- Meta Tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Site Title -->
    <title>Section Details</title>

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

        .pagination a:active, .pagination a.active {
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
            margin-bottom: 15px;
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

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .table-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            background-color: #f8f9fa;
            border-bottom: 1px solid #ddd;
        }

        .table-header .btn-add {
            background-color: #09ad95;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 5px;
        }

        .table-header .btn-add:hover {
            background-color: #078c76;
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
                        <div class="sherah-dsinner">
                            <div class="row align-items-center justify-content-between">
                                <div class="col-6">
                                    <div class="sherah-breadcrumb mg-top-30">
                                        <h2 class="sherah-breadcrumb__title">Section Details</h2>
                                        <ul class="sherah-breadcrumb__list">
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <form action="list-bin" method="get" class="search-form">
                                        <input type="hidden" name="id" value="${section.sectionID}">
                                        <input type="text" name="search" placeholder="Search bins..."
                                               value="${param.search}" class="search-input">
                                        <button type="submit" class="search-button">
                                            <i class="fas fa-search"></i> Search
                                        </button>
                                    </form>
                                </div>
                            </div>

                            <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                                <div class="table-header">
                                    <h4 class="sherah-table__title">Bins in this Section</h4>
                                    <a href="addBin?sectionID=${section.sectionID}" class="btn-add">
                                        <i class="fas fa-plus-circle"></i> Add New Bin
                                    </a>
                                </div>

                                <!-- Bins Table -->
                                <table class="sherah-table__main sherah-table__main-v3">
                                    <thead class="sherah-table__head">
                                    <tr>
                                        <th class="sherah-table__column-2 sherah-table__h2">Bin ID</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Bin Name</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Status</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody class="sherah-table__body">
                                    <c:if test="${not empty bins}">
                                        <c:forEach var="bin" items="${bins}">
                                            <tr>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <p class="sherah-table__product-desc">${bin.binID}</p>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <p class="sherah-table__product-desc">${bin.binName}</p>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <c:choose>
                                                        <c:when test="${bin.status == 'true'}">
                                                            <span class="badge bg-success">Unlocked</span>
                                                        </c:when>
                                                        <c:when test="${bin.status == 'false'}">
                                                            <span class="badge bg-danger">Locked</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-warning">${bin.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <a href="${pageContext.request.contextPath}/view-bin-detail?id=${bin.binID}"
                                                           class="btn btn-sm btn-info">
                                                            Detail
                                                        </a>
                                                        <c:if test="${account.getRoleId() == 1}">
                                                            <a href="updateBin?binID=${bin.binID}"
                                                               class="btn btn-sm btn-warning">
                                                                Update
                                                            </a>
                                                            <a href="deletebin?binId=${bin.binID}&sectionId=${section.sectionID}"
                                                               onclick="return confirm('Are you sure you want to lock this bin?');"
                                                               class="btn btn-sm btn-danger">
                                                                Delete
                                                            </a>
                                                        </c:if>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty bins}">
                                        <tr>
                                            <td colspan="4" class="text-center">No bins available in this section</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>

                                <!-- Pagination -->
                                <div class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <a href="list-bin?id=${section.sectionID}&page=${currentPage - 1}">Previous</a>
                                    </c:if>

                                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                                        <a href="list-bin?id=${section.sectionID}&page=${pageNumber}"
                                           class="${pageNumber == currentPage ? 'active' : ''}">${pageNumber}</a>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <a href="list-bin?id=${section.sectionID}&page=${currentPage + 1}">Next</a>
                                    </c:if>
                                </div>

                                <!-- Action Buttons -->

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<!-- Scripts -->
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    window.onload = function () {
        const alertMessage = '<%= session.getAttribute("alertMessage") != null ? session.getAttribute("alertMessage") : "" %>';
        const alertType = '<%= session.getAttribute("alertType") != null ? session.getAttribute("alertType") : "" %>';
        if (alertMessage.trim() !== "" && alertType.trim() !== "") {
            Swal.fire({
                icon: alertType,
                title: alertMessage,
                showConfirmButton: false,
                timer: 2000
            });
        }
        <%
            session.removeAttribute("alertMessage");
            session.removeAttribute("alertType");
        %>
    };
</script>
</body>
</html>