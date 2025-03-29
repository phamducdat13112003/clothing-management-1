<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 1/28/2025
  Time: 10:50 AM
  To change this template use File | Settings | File Templates.
--%>
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
        .row {
            width: 100%;
            margin: 0;
        }

        .sherah-personals__content {
            width: 100%;
            padding: 0;
        }

        .sherah-ptabs__inner {
            margin: 0;
            padding: 0;
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
                    <!-- Dashboard Inner -->
                    <div class="sherah-dsinner">
                        <!-- Sherah Breadcrumb -->
                        <div class="sherah-breadcrumb mg-top-30">
                            <h2 class="sherah-breadcrumb__title">Setting</h2>
                            <ul class="sherah-breadcrumb__list">
                                <li><a href="manageemployee">Home</a></li>
                                <li class="active"><a href="##">Personal Information</a></li>
                            </ul>
                        </div>
                        <!-- End Sherah Breadcrumb -->
                        <div class="sherah-personals">
                            <div class="row">
                                <div class="col-12 sherah-personals__content mg-top-30">
                                    <div class="sherah-ptabs">
                                        <div class="sherah-ptabs__inner">
                                            <div class="tab-content" id="nav-tabContent">
                                                <div class="tab-pane fade show active" id="id1" role="tabpanel">
                                                    <form action="#">
                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="sherah-ptabs__separate">
                                                                    <div class="sherah-ptabs__form-main">
                                                                        <div class="sherah__item-group sherah-default-bg sherah-border">
                                                                            <!--Profile Cover Info -->
                                                                            <div class="sherah-profile-cover sherah-offset-bg sherah-dflex">
                                                                                <div class="sherah-profile-cover__img">
                                                                                    <img src="${employee.image}"
                                                                                         alt="Employee Image">
                                                                                </div>
                                                                                <div class="sherah-profile-cover__content">
                                                                                    <h3 class="sherah-profile-cover__title">${employee.employeeName}</h3>
                                                                                </div>
                                                                            </div>
                                                                            <!-- End Profile Cover Info -->

                                                                            <div class="sherah-profile-info__v2 mg-top-30">
                                                                                <h3 class="sherah-profile-info__heading mg-btm-30">
                                                                                    Personal Information</h3>
                                                                                <ul class="sherah-profile-info__list sherah-dflex-column">
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            EmployeeID :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.employeeID}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Full Name :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.employeeName}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Email :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.email}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Phone :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.phone}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Address :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.address}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Gender :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.gender ? "Nam" : "Nữ"}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Date of Birth :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.dob}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Warehouse :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.warehouseName}</p>
                                                                                    </li>
                                                                                    <li class="sherah-dflex">
                                                                                        <h4 class="sherah-profile-info__title">
                                                                                            Status :</h4>
                                                                                        <p class="sherah-profile-info__text">${employee.status}</p>
                                                                                    </li>
                                                                                </ul>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- End Dashboard Inner -->
                </div>
            </div>


        </div>
    </div>
</section>
<!-- End sherah Dashboard -->

<!-- sherah Scripts -->
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

