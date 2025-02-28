<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2/27/2025
  Time: 11:44 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
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
</head>
<body id="sherah-dark-light">

<div class="sherah-body-area">
    <jsp:include page="include/sidebar.jsp"></jsp:include>
    <jsp:include page="include/header.jsp"></jsp:include>

    <section class="sherah-adashboard sherah-show">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="sherah-body">
                        <div class="sherah-dsinner">
                            <div class="row mg-top-30">
                                <div class="col-12 sherah-flex-between">
                                    <div class="sherah-breadcrumb">
                                        <h2 class="sherah-breadcrumb__title">Purchase Order Detail</h2>
                                        <ul class="sherah-breadcrumb__list">
                                            <li><a href="viewpurchaseorder">Purchase Order List</a></li>
                                            <li class="active"><a href="#">Purchase Order Detail</a></li>
                                        </ul>
                                    </div>
<%--                                    <a href="add" class="sherah-btn sherah-gbcolor">Add New Purchase Order</a>--%>
                                </div>
                            </div>
                            <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                                <div class="cart-wrapper">
                                    <div class="container">
                                        <div class="row g-4">
                                            <div class="col-lg-6">
                                                <div class="summary-card">
                                                    <h5 class="mb-4">Information Supplier</h5>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Supplier Name:</span>
                                                        <span class="text-muted">${supplier.supplierName}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Supplier Address:</span>
                                                        <span class="text-muted">${supplier.address}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Supplier Email:</span>
                                                        <span class="text-muted">${supplier.email}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Supplier Phone:</span>
                                                        <span class="text-muted">${supplier.phone}</span>
                                                    </div>
                                                    <hr>
                                                </div>
                                            </div>
                                            <div class="col-lg-6">
                                                <div class="summary-card">
                                                    <h5 class="mb-4">Information Other</h5>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Actor Name</span>
                                                        <span class="text-muted">${employee.employeeName}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Actor Email</span>
                                                        <span class="text-muted">${employee.email}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Actor Phone</span>
                                                        <span class="text-muted">${employee.phone}</span>
                                                    </div>
                                                    <div class="d-flex justify-content-between mb-3">
                                                        <span class="text-muted">Create Date PO</span>
                                                        <span class="text-muted">${purchaseOrder.createdDate}</span>
                                                    </div>
                                                    <hr>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-card p-3 shadow-sm">
                                        <div class="row align-items-center">
                                            <div class="col-md-2">
                                                <img src="${productDetail.image}" alt="Product" class="product-image">
                                            </div>
                                            <div class="col-md-2">
                                                <h6 class="mb-1">Product Name</h6>
                                                <p class="text-muted mb-0">${product.name}</p>
                                            </div>
                                            <div class="col-md-1">
                                                <h6 class="mb-1">Product weight</h6>
                                                <p class="text-muted mb-0">${productDetail.weight}</p>
                                            </div>
                                            <div class="col-md-2">
                                                <h6 class="mb-1">Product color</h6>
                                                <p class="text-muted mb-0">${productDetail.color}</p>
                                            </div>
                                            <div class="col-md-1">
                                                <h6 class="mb-1">Product size</h6>
                                                <p class="text-muted mb-0">${productDetail.size}</p>
                                            </div>
                                            <div class="col-md-2">
                                                <h6 class="mb-1">Quantity Product PO</h6>
                                                <p class="text-muted mb-0">${purchaseOrderDetail.quantity}</p>
                                            </div>
                                            <div class="col-md-2">
                                                <h6 class="mb-1">Price Of one product</h6>
                                                <p class="text-muted mb-0">${purchaseOrderDetail.price}</p>
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row mt-2">
                                            <div class="col-md-12 text-end">
                                                <h5 class="text-primary fw-bold fs-5">Total Price: ${purchaseOrderDetail.totalPrice}<Span> VND</Span></h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
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