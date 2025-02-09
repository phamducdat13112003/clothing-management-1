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
    <title>Product List</title>

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
                                <h2 class="sherah-breadcrumb__title">Product</h2>
                                <%--                                <ul class="sherah-breadcrumb__list">--%>
                                <%--                                    <li><a href="#">Home</a></li>--%>
                                <%--                                    <li class="active"><a href="profile-info.html">Personal Information</a></li>--%>
                                <%--                                </ul>--%>
                            </div>
                            <!-- End Sherah Breadcrumb -->
                            <!-- Product List -->
                            <div class="container">
                                <table class="table table-bordered">
                                    <thead class="table-light">
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>...</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>...</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:if test="${not empty products}">
                                        <c:forEach var="product" items="${products}">
                                            <tr>
                                                <td>${product.id}</td>
                                                <td>${product.name}</td>
                                                <td>
                                                    <img src="https://drive.google.com/uc?id=${product.urlImage}" alt="Product Image" width="100" height="100">
                                                </td>
                                                <td>${product.price}</td>
                                                <td>${product.totalQuantity}</td>
                                                <td>
                                                    <!-- Delete product form with JSTL expression for productId -->
                                                    <form action="delete-product" method="post">
                                                        <input type="hidden" name="productId" id="productId" value="${product.id}">
                                                        <button type="submit">Delete</button>
                                                    </form>
                                                <td>
                                                    <button onclick="window.location.href='product-detail?id=${product.id}'">View Details</button>
                                                </td>




                                            </tr>
                                        </c:forEach>

                                    </c:if>
                                    <c:if test="${empty products}">
                                        <tr>
                                            <td colspan="4" class="text-center">No products available</td>
                                        </tr>
                                    </c:if>
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
<script>
    window.onload = () => {
        const urlParams = new URLSearchParams(window.location.search);
        const errorCode = parseInt(urlParams.get('message'));
        switch (errorCode) {
            case 1:
                window.alert('Successful')
                break;
            case 2:
                window.alert('Something went wrong');
                break;
        }
    };
</script>
</body>
</html>
