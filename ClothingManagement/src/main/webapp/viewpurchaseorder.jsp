<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2/27/2025
  Time: 5:05 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            from { opacity: 0; transform: scale(0.9); }
            to { opacity: 1; transform: scale(1); }
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
                                            <li><a href="">Home</a></li>
                                            <li class="active"><a href="#">Purchase Order List</a></li>
                                        </ul>
                                    </div>
                                <form action="searchpurchaseorder" method="GET" class="search-form">
                                    <input value="${txtS}"  type="text" name="txt" placeholder="Search PO ID, Supplier, Status..." class="search-input">
                                    <button type="submit" class="search-btn">Search</button>
                                </form>

                                <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                                    <thead class="sherah-table__head">
                                    <tr>
                                        <th class="sherah-table__column-1 sherah-table__h1">PO ID</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Created Date</th>
                                        <th class="sherah-table__column-3 sherah-table__h3">Supplier Name</th>
                                        <th class="sherah-table__column-4 sherah-table__h4">Created By</th>
                                        <th class="sherah-table__column-5 sherah-table__h5">Status</th>
                                        <th class="sherah-table__column-6 sherah-table__h6">Total Price</th>
                                    </tr>
                                    </thead>
                                    <tbody class="sherah-table__body">
                                    <c:forEach items="${purchaseOrderList}" var="listpo">
                                        <tr>
                                            <td class="sherah-table__column-1">
                                                <a href="purchaseorderdetail?poID=${listpo.poID}" class="sherah-color1">${listpo.poID}</a>
                                            </td>
                                            <td class="sherah-table__column-2">${listpo.createdDate}</td>
                                            <td class="sherah-table__column-3">${listpo.supplierID}</td>
                                            <td class="sherah-table__column-4">${listpo.createdBy}</td>
                                            <td class="sherah-table__column-5">${listpo.status}</td>
                                            <td class="sherah-table__column-6">${listpo.totalPrice}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

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
    function openPopup(popupId, content) {
        document.getElementById(popupId).style.display = "block";
        document.getElementById(popupId + "Content").innerText = content;
    }

    function closePopup(popupId) {
        document.getElementById(popupId).style.display = "none";
    }
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
