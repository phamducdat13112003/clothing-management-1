<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>Product Detail of ${product.name}</title>

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
        .required {
            color: red;
            font-weight: bold;
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
                            <div class="row align-items-center justify-content-between">
                                <div class="col-6">
                                    <div class="sherah-breadcrumb mg-top-30">
                                        <h2 class="sherah-breadcrumb__title">Manage Product "${product.name}"</h2>
                                        <ul class="sherah-breadcrumb__list">
                                            <li><a href="list-product-detail?id=${product.id}">Home</a></li>
                                            <c:if test="${sessionScope.role == 1 || sessionScope.role == 2}">
                                            <li class="active"><a href="${pageContext.request.contextPath}/add-product-detail?id=${product.id}">Add Product Detail</a></li>
                                            </c:if>
                                        </ul>

                                    </div>
                                </div>
                            </div>
                            <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                                <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                                    <thead class="sherah-table__head">
                                    <tr>
                                        <th class="sherah-table__column-1 sherah-table__h2">Code</th>
                                        <th class="sherah-table__column-2 sherah-table__h1">Image</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Color</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Size</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Weight</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Quantity</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Status</th>
                                        <th class="sherah-table__column-2 sherah-table__h2">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody class="sherah-table__body">
                                    <c:if test="${not empty list}">
                                        <c:forEach var="list" items="${list}">
                                            <tr>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.id}</p>
                                                    </div>
                                                </td>

                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">
                                                            <img src="${list.image}"
                                                                 alt="Product Detail Image" width="100" height="100">
                                                        </p>
                                                    </div>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.color}</p>
                                                    </div>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.size}</p>
                                                    </div>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.weight}</p>
                                                    </div>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.quantity}</p>
                                                    </div>
                                                </td>
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">${list.status}</p>
                                                    </div>
                                                </td>
                                                <c:if test="${list.status==1}">
                                                    <c:if test="${list.quantity==0}">
                                                        <c:if test="${sessionScope.role == 1}">
                                                            <td class="sherah-table__column-2 sherah-table__data-2">
                                                                <div class="sherah-table__product-content">
                                                                    <p class="sherah-table__product-desc">
                                                                        <a href="#"
                                                                           onclick="return confirmDelete('${pageContext.request.contextPath}/delete-product-detail?id=${list.id}&productid=${list.productId}');">

                                                                            Delete
                                                                        </a>
                                                                    </p>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>

                                                <c:if test="${list.status==0}">
                                                <c:if test="${sessionScope.role == 1 || sessionScope.role == 2}">
                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                        <div class="sherah-table__product-content">
                                                            <p class="sherah-table__product-desc">
                                                                <a href="#"
                                                                   onclick="return confirmRecovery('${pageContext.request.contextPath}/recover-product-detail?id=${list.id}&productid=${list.productId}');">
                                                                    Recover
                                                                </a>
                                                            </p>
                                                        </div>
                                                    </td>
                                                </c:if>
                                                </c:if>

                                                <td class="sherah-table__column-2 sherah-table__data-2">
                                                    <div class="sherah-table__product-content">
                                                        <p class="sherah-table__product-desc">
                                                            <a href="${pageContext.request.contextPath}/view-product-detail?id=${list.id}">
                                                                Detail
                                                            </a>
                                                        </p>
                                                    </div>
                                                </td>

                                            </tr>
                                        </c:forEach>

                                    </c:if>
                                    <c:if test="${empty list}">
                                        <tr>
                                            <td colspan="4" class="text-center">No products available</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                                <div class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <a href="list-product-detail?id=${product.id}&page=${currentPage - 1}&search=${search}">Previous</a>
                                    </c:if>

                                    <c:forEach var="i" begin="${currentPage - 1 > 0 ? currentPage - 1 : 1}" end="${currentPage + 1 < totalPages ? currentPage + 1 : totalPages}">
                                        <a href="list-product-detail?id=${product.id}&page=${i}&search=${search}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <a href="list-product-detail?id=${product.id}&page=${currentPage + 1}&search=${search}">Next</a>
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
    <!-- End sherah Dashboard -->

</div>

<!-- sherah Scripts -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/charts.js"></script>
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

    function confirmDelete(url) {
        if (confirm("Are you sure you want to delete this product?")) {
            window.location.href = url;
        }
        return false;
    }

    function confirmRecovery(url) {
        if (confirm("Are you sure you want to recover this product?")) {
            window.location.href = url;
        }
        return false;
    }
</script>
<script type="text/javascript">
    function validateForm() {
        var name = document.forms["myForm"]["name"].value;
        var material = document.forms["myForm"]["material"].value;
        var madeIn = document.forms["myForm"]["madeIn"].value;

        // Kiểm tra xem trường "name" có bị bỏ trống không
        if (name === "") {
            alert("Name must be filled out");
            return false;
        }

        if (name.trim() === "") {
            alert("Name cannot contain only space");
            return false;
        }

        var namePattern = /^[a-zA-Z0-9\s]+$/; // Biểu thức chính quy chỉ cho phép chữ và số
        if (!namePattern.test(name)) {
            alert("Name must contain only letters and numbers");
            return false;
        }

        // Kiểm tra trường "material"
        if (material === "") {
            alert("Material must be filled out");
            return false;
        }

        if (material.trim() === "") {
            alert("Material cannot contain only space");
            return false;
        }

        var materialPattern = /^[a-zA-Z0-9%,\s]+$/; // Biểu thức chính quy cho phép chữ, số và ký tự '%'
        if (!materialPattern.test(material)) {
            alert("Material must contain only letters, numbers, and the '%' character");
            return false;
        }

        // Kiểm tra trường "madeIn" (nếu có dữ liệu nhập vào)
        if (madeIn !== "") { // Kiểm tra nếu người dùng nhập dữ liệu
            if(madeIn.trim() !== ""){
                var madeInPattern = /^[a-zA-Z\s]+$/;  // Biểu thức chính quy cho chỉ chữ cái
                if (!madeInPattern.test(madeIn)) {
                    alert("Made In can only contain letters");
                    return false;  // Ngừng gửi form nếu không hợp lệ
                }
            }
            else{
                alert("Made In cannot contain only space");
                return false;
            }

        }

        return true;  // Nếu tất cả kiểm tra hợp lệ, cho phép gửi form
    }
</script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</body>
</html>
