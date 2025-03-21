<%@ page import="org.example.clothingmanagement.entity.PurchaseOrderDetailDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.example.clothingmanagement.entity.PurchaseOrder" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<!DOCTYPE html>
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
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
                                            <li class="active"><a href="#">${purchaseOrder.poID}</a></li>
                                        </ul>
                                    </div>
                                    <div class="">
                                        <c:if test='${employee.employeeID == sessionScope.account.employeeId and purchaseOrder.status == "Pending"}'>
                                            <form action="updateinfomationpo" method="GET" class="d-inline">
                                                <input type="hidden" name="poid" value="${purchaseOrder.poID}">
                                                <button type="submit" class="btn btn-warning text-white me-2">
                                                    <i class="bi bi-pencil-square"></i> Edit
                                                </button>
                                            </form>
                                            <form action="updatestatuspurchaseorder" method="GET" class="d-inline">
                                                <input type="hidden" name="poid" value="${purchaseOrder.poID}">

                                                <div class="d-inline-block position-relative">
                                                    <select name="status"
                                                            class="form-select d-inline-block w-auto me-2">
                                                        <option value="Confirmed" ${purchaseOrder.status == 'Confirmed' ? 'selected' : ''}>
                                                            ✅ Confirmed
                                                        </option>
                                                        <option value="Cancel" ${purchaseOrder.status == 'Cancel' ? 'selected' : ''}>
                                                            ❌ Cancel
                                                        </option>
                                                    </select>
                                                </div>

                                                <button type="submit" class="btn btn-primary"
                                                        onclick="return confirm('Are you sure you want to update the status?');">
                                                    <i class="bi bi-check-circle"></i> Update Status
                                                </button>
                                            </form>
                                        </c:if>

                                        <c:if test='${employee.employeeID == sessionScope.account.employeeId and purchaseOrder.status == "Confirmed"}'>
                                            <form action="updateinfomationpo" method="GET" class="d-inline">
                                                <input type="hidden" name="poid" value="${purchaseOrder.poID}">
                                                <button type="submit" class="btn btn-warning text-white me-2">
                                                    <i class="bi bi-pencil-square"></i> Edit
                                                </button>
                                            </form>
                                            <form action="updatestatuspurchaseorder" method="GET" class="d-inline">
                                                <input type="hidden" name="status" value="Cancel">
                                                <input type="hidden" name="poid" value="${purchaseOrder.poID}">
                                                <button type="submit" class="btn btn-danger"
                                                        onclick="return confirm('Are you sure you want to cancel this order?');">
                                                    <i class="bi bi-x-circle"></i> Cancel
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test='${purchaseOrder.status == "Cancel"}'>
                                            <div class="alert alert-danger" role="alert">
                                                    ${purchaseOrder.poID} a has Cancel
                                            </div>
                                        </c:if>
                                        <c:if test='${purchaseOrder.status == "Processing"}'>
                                            <div class="alert alert-warning" role="alert">
                                                    ${purchaseOrder.poID} a has Processing
                                            </div>
                                        </c:if>
                                        <c:if test='${purchaseOrder.status == "Done"}'>
                                            <div class="alert alert-success" role="alert">
                                                    ${purchaseOrder.poID} a has Done
                                            </div>
                                        </c:if>

                                    </div>

                                </div>
                                <%--                                    <a href="add" class="sherah-btn sherah-gbcolor">Add New Purchase Order</a>--%>
                            </div>
                        </div>
                        <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                            <div class="cart-wrapper">
                                <div class="container">

                                    <div class="row">
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
                                        <!-- Supplier Information -->
                                        <div class="col-lg-6">
                                            <div class="summary-card p-4 border rounded">
                                                <h5 class="mb-4">Supplier Information</h5>
                                                <div class="mb-3 d-flex justify-content-between">
                                                    <span class="text-muted">Name:</span>
                                                    <span>${supplier.supplierName}</span>
                                                </div>
                                                <div class="mb-3 d-flex justify-content-between">
                                                    <span class="text-muted">Address:</span>
                                                    <span>${supplier.address}</span>
                                                </div>
                                                <div class="mb-3 d-flex justify-content-between">
                                                    <span class="text-muted">Email:</span>
                                                    <span>${supplier.email}</span>
                                                </div>
                                                <div class="mb-3 d-flex justify-content-between">
                                                    <span class="text-muted">Phone:</span>
                                                    <span>${supplier.phone}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row g-4">

                                        <!-- Product Information -->
                                        <div class="col-12">
                                            <h5 class="mb-4">Product Information</h5>
                                            <%
                                                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                                                symbols.setGroupingSeparator('.'); // Đổi dấu phân tách từ ',' thành '.'

                                                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                                                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                                List<PurchaseOrderDetailDTO> purchaseOrderDetailDTOs = (List<PurchaseOrderDetailDTO>) request.getAttribute("purchaseOrderDetailDTOs");
                                                if (purchaseOrderDetailDTOs != null) {
                                                    for (PurchaseOrderDetailDTO dto : purchaseOrderDetailDTOs) {
                                            %>
                                            <div class="summary-card p-4 border rounded">
                                                <div class="row align-items-center">
                                                    <div class="col-lg-4 col-md-6 text-center">
                                                        <img src="<%= dto.getImage() %>" alt="Product Image"
                                                             class="img-fluid rounded" style="max-width: 200px;">
                                                    </div>
                                                    <div class="col-lg-8 col-md-6">
                                                        <div class="row">
                                                            <div class="col-md-4 mb-3"><strong>Product
                                                                Name:</strong> <%= dto.getProductName() %>
                                                            </div>
                                                            <div class="col-md-4 mb-3">
                                                                <strong>Weight:</strong> <%= dto.getWeight() %> kg
                                                            </div>
                                                            <div class="col-md-4 mb-3">
                                                                <strong>Color:</strong> <%= dto.getColor() %>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-4 mb-3">
                                                                <strong>Size:</strong> <%= dto.getSize() %>
                                                            </div>
                                                            <div class="col-md-4 mb-3">
                                                                <strong>Quantity:</strong> <%= dto.getQuantity() %>
                                                            </div>
                                                            <div class="col-md-4 mb-3"><strong>Price Per
                                                                Unit:</strong> <%= decimalFormat.format(dto.getPrice()) %>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-12 mb-3 text-end">
                                                                <strong>Total Price Of
                                                                    PoDetail: <%= decimalFormat.format(dto.getTotalPrice()) %>
                                                                </strong>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                    }
                                                }
                                            %>
                                            <hr>
                                            <div class="row mt-2">
                                                <div class="col-md-12 text-end">
                                                    <h5 class="text-primary fw-bold fs-5">Total Price PO:
                                                        <fmt:formatNumber value="${purchaseOrder.totalPrice}"
                                                                          type="number" groupingUsed="true"/>
                                                        VNĐ</h5>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link" href="viewpurchaseorder">Back</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
</div>
</section>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const quantityInputs = document.querySelectorAll('input[name="productQuantity"]');
        const priceInputs = document.querySelectorAll('input[name="productPrice"]');
        const totalPriceInputs = document.querySelectorAll('input[name="totalPrice"]');

        function updateTotalPrice(index) {
            const quantity = parseFloat(quantityInputs[index].value) || 0;
            const price = parseFloat(priceInputs[index].value) || 0;
            totalPriceInputs[index].value = (quantity * price).toFixed(2);
        }

        quantityInputs.forEach((input, index) => {
            input.addEventListener("input", () => updateTotalPrice(index));
        });

        priceInputs.forEach((input, index) => {
            input.addEventListener("input", () => updateTotalPrice(index));
        });
    });

    function confirmCancel(event) {
        if (!confirm("Are you sure CANCEL?")) {
            event.preventDefault(); // Ngăn chặn hành động nếu người dùng nhấn "Cancel"
        }
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