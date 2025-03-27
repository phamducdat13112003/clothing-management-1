<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 3/16/2025
  Time: 3:06 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <style>
        .equal-height {
            display: flex;
            flex-wrap: wrap;
        }

        .equal-height > .col-lg-4 {
            display: flex;
            flex-direction: column;
        }

        .summary-card {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .summary-card h5 {
            font-size: 1.2rem;
            font-weight: bold;
            color: #333;
            text-align: center;
        }
    </style>
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
                                         <c:if test='${purchaseOrder.status == "Processing"}'>
                                        <h2 class="sherah-breadcrumb__title"> Import Good </h2>
                                         </c:if>
                                        <c:if test='${purchaseOrder.status == "Done"}'>
                                            <h2 class="sherah-breadcrumb__title">Purchase Order Detail</h2>
                                        </c:if>
                                        <c:if test='${purchaseOrder.status == "Confirmed"}'>
                                            <h2 class="sherah-breadcrumb__title">Purchase Order Detail</h2>
                                        </c:if>
                                        <ul class="sherah-breadcrumb__list">
                                            <li><a href="viewdeliveryorder">Delivery Order List</a></li>
                                            <li class="active"><a href="#">${purchaseOrder.poID}</a></li>
                                        </ul>
                                    </div>
                                    <div class="">
                                        <c:if test='${purchaseOrder.status == "Confirmed"}'>
                                            <form action="updatestatusdeliveryorder" method="GET" class="d-inline">
                                                <input type="hidden" name="status" value="Processing">
                                                <input type="hidden" name="poid" value="${purchaseOrder.poID}">
                                                <button type="submit"
                                                        class="btn btn-warning px-4 py-2 rounded-pill shadow-sm text-white fw-semibold"
                                                        onclick="return confirm('Are you sure you want to process this order?');">
                                                    <i class="bi bi-hourglass-split me-2"></i>Change status to Processing
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test='${purchaseOrder.status == "Done"}'>
                                            <div class="alert alert-success" role="alert">
                                                    ${purchaseOrder.poID} a has Done
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty updatepostatussuccessfully}">
                                            <div class="alert" style="color: green">${updatepostatussuccessfully}</div>
                                            <% session.removeAttribute("updatepostatussuccessfully"); %> <!-- Xóa sau khi hiển thị -->
                                        </c:if>
                                        <c:if test="${not empty transferdofail}">
                                            <div class="alert-danger">${transferdofail}</div>
                                            <% session.removeAttribute("transferdofail"); %> <!-- Xóa sau khi hiển thị -->
                                        </c:if>

                                    </div>

                                </div>
                                <%--                                    <a href="add" class="sherah-btn sherah-gbcolor">Add New Purchase Order</a>--%>
                            </div>
                        </div>
                        <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                            <div class="cart-wrapper">
                                <div class="container">
                                    <c:if test='${purchaseOrder.status == "Processing"}'>
                                        <div class="row equal-height">


                                            <div class="col-lg-4">
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
                                            <div class="col-lg-4">
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
                                            <!-- Section and Bin Selection -->
                                            <div class="col-lg-4">
                                                <div class="summary-card p-4 border rounded">
                                                    <h5 class="mb-4">Select Section and Bin</h5>
                                                    <form action="createdeliveryorder" method="GET" id="sectionBinForm">
                                                        <input type="hidden" name="poid" value="${purchaseOrder.poID}">

                                                        <!-- Select Section -->
                                                        <div class="mb-3">
                                                            <label for="sectionSelect" class="form-label">Select
                                                                Section</label>
                                                            <select class="form-select" id="sectionSelect"
                                                                    name="sectionid"
                                                                    required>
                                                                <option value="">-- Select Section --</option>
                                                                <c:forEach var="section" items="${section}">
                                                                    <option value="${section.sectionID}">${section.sectionID}
                                                                        - ${section.sectionName}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>

                                                        <!-- Select Bin (Hidden Initially) -->
                                                        <div class="mb-3" id="binContainer" style="display: none;">
                                                            <label for="binSelect" class="form-label">Select Bin</label>
                                                            <select class="form-select" id="binSelect" name="binid"
                                                                    required>
                                                                <option value="">-- Select Bin --</option>
                                                                <c:forEach var="bin" items="${bins}">
                                                                    <option value="${bin.binId}"
                                                                            data-section="${bin.sectionID}">
                                                                            ${bin.binID} - ${bin.BinName} - ${bin.TotalWeightBins}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>

                                                        <div class="text-end">
                                                            <button type="submit" class="btn btn-primary">import goods
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>

                                        </div>
                                    </c:if>
                                    <c:if test='${purchaseOrder.status == "Confirmed" or purchaseOrder.status == "Done"}'>
                                        <div class="row equal-height">
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
                                    </c:if>
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
                                <a class="nav-link" href="viewdeliveryorder">Back</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script>
    document.getElementById("sectionSelect").addEventListener("change", function () {
        let sectionId = this.value;
        console.log("Selected sectionId:", sectionId); // Kiểm tra giá trị

        let binSelect = document.getElementById("binSelect");
        let binContainer = document.getElementById("binContainer");

        if (sectionId) {
            fetch(`getbinsbysection?sectionId=` + sectionId + ``)
                .then(response => response.json())
                .then(data => {
                    console.log("Received bins:", data); // Kiểm tra dữ liệu nhận được

                    binSelect.innerHTML = '<option value="">-- Select Bin --</option>';
                    data.forEach(bin => {
                        let option = document.createElement("option");
                        option.value = bin.binID;
                        option.textContent = `` + bin.binID + ` - ` + bin.binName + ` - ` + bin.currentCapacity + `Kg`;
                        if (bin.currentCapacity === 100 || bin.currentCapacity >= 100) {
                            option.disabled = true;
                        }
                        binSelect.appendChild(option);
                    });
                    binContainer.style.display = "block";
                })
                .catch(error => console.error("Error fetching bins:", error));
        } else {
            binContainer.style.display = "none";
        }
    });
</script>

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
