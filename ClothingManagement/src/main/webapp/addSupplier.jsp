<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 2/28/2025
  Time: 8:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>Add Supplier</title>

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
        .required {
            color: red;
            font-weight: bold;
        }
        .error-message {
            color: red;
            font-size: 12px;
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
                        <div class="row">
                            <div class="col-12">
                                <div class="sherah-breadcrumb mg-top-30">
                                    <h2 class="sherah-breadcrumb__title">Add Supplier</h2>
                                    <ul class="sherah-breadcrumb__list">
                                        <li><a href="managesupplier">Home</a></li>
                                    </ul>
                                    <c:if test="${not empty message}">
                                        <span class="error-message">${message}</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="sherah-page-inner sherah-border sherah-basic-page sherah-default-bg mg-top-25 p-0">
                            <form class="sherah-wc__form-main" action="addsupplier" method="post">
                                <div class="row">
                                    <div class="col-12">
                                        <!-- Basic Information -->
                                        <div class="product-form-box sherah-border mg-top-30">
                                            <h4 class="form-title m-0">Basic Information</h4>
                                            <div class="row">
                                                <div class="col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Supplier Name <span class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input value="${name}" class="sherah-wc__form-input" type="text" name="name" required>
                                                            <c:if test="${not empty errorName}">
                                                                <span class="error-message">${errorName}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Email <span class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Email" type="email" name="email" value="${email}" required>
                                                            <c:if test="${not empty errorEmail}">
                                                                <span class="error-message">${errorEmail}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Phone <span class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Phone" type="text" name="phone" value="${phone}" required>
                                                            <c:if test="${not empty errorPhone}">
                                                                <span class="error-message">${errorPhone}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Address <span class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Address" type="text" name="address" value="${address}" required>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        <!-- End Basic Information -->
                                        </div>
                                        <div class="mg-top-40 sherah-dflex sherah-dflex-gap-30">
                                            <button type="submit" class="sherah-btn sherah-btn__primary">Add</button>
                                            <button type="reset" class="sherah-btn sherah-btn__third">Cancel</button>
                                        </div>
                                    </div>
                                </div>
                            </form>
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
<script>
    jQuery(document).ready(function($) {
        $('[data-countdown]').each(function() {
            var $this = $(this), finalDate = $(this).data('countdown');
            $this.countdown(finalDate, function(event) {
                $this.html(event.strftime(' %H : %M : %S'));
            });
        });
    });
</script>
<script>
    const ctx_side_two = document.getElementById('myChart_Side_One').getContext('2d');
    const myChart_Side_One = new Chart(ctx_side_two, {
        type: 'doughnut',

        data: {
            labels: [
                'Total Sold',
                'Total Cancel',
                'Total Cancel',
                'Total Planding'
            ],
            datasets: [{
                label: 'My First Dataset',
                data: [16, 16, 16, 30],
                backgroundColor: [
                    '#5356FB',
                    '#F539F8',
                    '#FFC210',
                    '#E3E4FE'
                ],
                hoverOffset: 2,
                borderWidth: 0,
            }]
        },

        options: {

            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                    display: false,
                },
                title: {
                    display: false,
                    text: 'Sell History'
                }
            }
        }

    });

    const ctx_side_three = document.getElementById('myChart_Side_Two').getContext('2d');
    const myChart_Side_Two = new Chart(ctx_side_three, {
        type: 'line',

        data: {
            labels: ['12:00 AM', '04:00 AM', '08:00 AM'],
            datasets: [{
                label: 'Visitor',
                data: [40, 90, 10],
                backgroundColor: '#D8D8FE',
                borderColor:'#5356FB',
                pointRadius: 3,
                pointBackgroundColor: '#5356FB',
                pointBorderColor: '#5356FB',
                borderWidth:4,
                tension: 0.9,
                fill:true,
                fillColor:'#fff',

            }]
        },

        options: {
            responsive: true,
            scales: {
                x:{
                    grid:{
                        display:false,
                        drawBorder: false,
                    },

                },
                y:{
                    grid:{
                        display:false,
                        drawBorder: false,
                    },
                    ticks:{
                        display:false
                    }
                },
            },

            plugins: {
                legend: {
                    position: 'top',
                    display: false,
                },
                title: {
                    display: false,
                    text: 'Visitor: 2k'
                }
            }
        }
    });
</script>
</body>
</html>


