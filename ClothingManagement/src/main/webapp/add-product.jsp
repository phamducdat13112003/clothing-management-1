<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

</html>
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

                            </div>
                            <!-- End Sherah Breadcrumb -->
                            <div class="sherah-personals">
                                <div class="row">


                                    <div class="col-lg-9 col-md-10 col-12  sherah-personals__content mg-top-30">
                                        <div class="sherah-ptabs">

                                            <div class="sherah-ptabs__inner">
                                                <div class="tab-content" id="nav-tabContent">
                                                    <!--  Features Single Tab -->
                                                    <div class="tab-pane fade show active" id="id1" role="tabpanel">
                                                        <form action="add-product" method="post">
                                                            <div class="row">
                                                                <div class="col-12">
                                                                    <div class="sherah-ptabs__separate">
                                                                        <div class="sherah-ptabs__form-main">
                                                                            <div class="sherah__item-group sherah-default-bg sherah-border">
                                                                                <!-- End Profile Cover Info -->

                                                                                <div class="sherah-profile-info__v2 mg-top-30">
                                                                                    <h3 class="sherah-profile-info__heading mg-btm-30">Personal Information</h3>


                                                                                        <h2>Add New Product</h2>

                                                                                        <!-- Product Name -->
                                                                                        <label for="productName">Product Name:</label>
                                                                                        <input type="text" id="productName" name="productName" required>

<%--                                                                                        <label for="urlImage">Upload Image:</label>--%>
<%--                                                                                        <input type="file" id="urlImage" name="image" accept="image/*" required>--%>

                                                                                        <!-- Price -->
                                                                                        <label for="price">Price:</label>
                                                                                        <input type="number" id="price" name="price" step="10000" min="1" required>

<%--                                                                                        <!-- Bin ID -->--%>
<%--                                                                                        <label for="binID">Bin ID:</label>--%>
<%--                                                                                        <input type="number" id="binID" name="binID" required>--%>

                                                                                        <!-- Category -->
                                                                                        <label for="categoryID">Category:</label>
                                                                                        <select id="categoryID" name="categoryID">
                                                                                            <c:forEach var="category" items="${categories}">
                                                                                                <option value="${category.categoryID}">${category.categoryName}</option>
                                                                                            </c:forEach>
                                                                                        </select>

                                                                                        <!-- Material -->
                                                                                        <label for="material">Material:</label>
                                                                                        <input type="text" id="material" name="material">

                                                                                        <!-- Gender -->
                                                                                        <label for="gender">Gender:</label>
                                                                                        <select id="gender" name="gender">
                                                                                            <option value="male">Male</option>
                                                                                            <option value="female">Female</option>
                                                                                            <option value="unisex">Unisex</option>
                                                                                        </select>

                                                                                        <!-- Seasons -->
                                                                                        <label for="seasons">Seasons:</label>
                                                                                        <select id="seasons" name="seasons">
                                                                                            <option value="Spring/Summer">Spring/Summer</option>
                                                                                            <option value="Fall/Winter">Fall/Winter</option>
                                                                                            <option value="Pre-Fall">Pre-Fall</option>
                                                                                            <option value="Others">Others</option>
                                                                                        </select>

                                                                                        <!-- Min Quantity -->
                                                                                        <label for="minQuantity">Minimum Quantity:</label>
                                                                                        <input type="number" id="minQuantity" name="minQuantity"  min="1" required>

                                                                                        <!-- Description -->
                                                                                        <label for="description">Description:</label>
                                                                                        <textarea id="description" name="description" rows="3"></textarea>


                                                                                        <!-- Supplier ID -->
                                                                                        <label for="supplierID">Supplier:</label>
                                                                                        <select id="supplierID" name="supplierID">
                                                                                            <c:forEach var="supplier" items="${suppliers}">
                                                                                                <option value="${supplier.id}">${supplier.name}</option>
                                                                                            </c:forEach>
                                                                                        </select>

                                                                                        <!-- Made In -->
                                                                                        <label for="madeIn">Made In:</label>
                                                                                        <input type="text" id="madeIn" name="madeIn">

                                                                                        <!-- Submit Button -->
                                                                                        <button type="submit">Add Product</button>

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
<script src="js/final-countdown.min.js"></script>
<script src="js/fancy-box.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/circle-progress.min.js"></script>
<script src="js/jquery-jvectormap.js"></script>
<script src="js/jvector-map.js"></script>
<script src="js/main.js"></script>
<script>
    window.onload = () => {
        const urlParams = new URLSearchParams(window.location.search);
        const errorCode = parseInt(urlParams.get('abc'));
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
