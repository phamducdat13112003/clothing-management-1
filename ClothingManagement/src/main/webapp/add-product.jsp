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
    <title>Product Lists</title>

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
            color: red; /* Makes the asterisk red */
            font-weight: bold;
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
                                                        <form name="myForm" action="${pageContext.request.contextPath}/add-product" onsubmit="return validateForm()" method="post">
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
                                                                                        <label for="productName">Product Name <span class="required">*</span></label>
                                                                                        <input type="text" id="productName" name="productName" maxlength="100" required>

<%--                                                                                        <label for="urlImage">Upload Image:</label>--%>
<%--                                                                                        <input type="file" id="urlImage" name="image" accept="image/*" required>--%>

                                                                                        <!-- Price -->
                                                                                        <label for="price">Price <span class="required">*</span></label>
                                                                                        <input type="number" id="price" name="price" step="1000" min="1000" max="10000000" required>

<%--                                                                                        <!-- Bin ID -->--%>
<%--                                                                                        <label for="binID">Bin ID:</label>--%>
<%--                                                                                        <input type="number" id="binID" name="binID" required>--%>

                                                                                        <!-- Category -->
                                                                                        <label for="categoryID">Category <span class="required">*</span></label>
                                                                                        <select id="categoryID" name="categoryID">
                                                                                            <c:forEach var="category" items="${categories}">
                                                                                                <option value="${category.categoryID}">${category.categoryName}</option>
                                                                                            </c:forEach>
                                                                                        </select>

                                                                                        <!-- Material -->
                                                                                        <label for="material">Material <span class="required">*</span></label>
                                                                                        <input type="text" id="material" name="material" maxlength="100" required>

                                                                                        <!-- Gender -->
                                                                                        <label for="gender">Gender <span class="required">*</span></label>
                                                                                        <select id="gender" name="gender">
                                                                                            <option value="male">Male</option>
                                                                                            <option value="female">Female</option>
                                                                                            <option value="unisex">Unisex</option>
                                                                                        </select>

                                                                                        <!-- Seasons -->
                                                                                        <label for="seasons">Seasons <span class="required">*</span></label>
                                                                                        <select id="seasons" name="seasons">
                                                                                            <option value="Spring/Summer">Spring/Summer</option>
                                                                                            <option value="Fall/Winter">Fall/Winter</option>
                                                                                            <option value="Pre-Fall">Pre-Fall</option>
                                                                                            <option value="Others">Others</option>
                                                                                        </select>

                                                                                        <!-- Min Quantity -->
                                                                                        <label for="minQuantity">Minimum Quantity <span class="required">*</span></label>
                                                                                        <input type="number" id="minQuantity" name="minQuantity"  min="1" max="1000" required>

                                                                                        <!-- Description -->
                                                                                        <label for="description">Description:</label>
                                                                                        <textarea id="description" name="description" rows="3" maxlength="255"></textarea>


                                                                                        <!-- Supplier ID -->
                                                                                        <label for="supplierID">Supplier <span class="required">*</span></label>
                                                                                        <select id="supplierID" name="supplierID">
                                                                                            <c:forEach var="supplier" items="${suppliers}">
                                                                                                <option value="${supplier.supplierId}">${supplier.supplierName}</option>
                                                                                            </c:forEach>
                                                                                        </select>

                                                                                        <!-- Made In -->
                                                                                        <label for="madeIn">Made In:</label>
                                                                                        <input type="text" id="madeIn" name="madeIn">

                                                                                        <input type="hidden" name=employeeId value="${sessionScope.employeeId}" />

                                                                                        <input type="hidden" id="createdDate" name="createdDate" value="<%= java.time.LocalDate.now() %>">




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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script type="text/javascript">
    function validateForm() {
        var name = document.forms["myForm"]["productName"].value;
        var material = document.forms["myForm"]["material"].value;
        var madeIn = document.forms["myForm"]["madeIn"].value;

        // Kiểm tra xem trường "name" có bị bỏ trống không
        if (name === "") {
            alert("Name must be filled out");
            return false;
        }

        if(name.trim() === "" ){
            alert("Name cannot contain only space");
            return false
        }

        var namePattern = /^[a-zA-Z0-9\s]+$/; // Biểu thức chính quy chỉ cho phép chữ và số
        if (!namePattern.test(name)) {
            alert("Name must contain only letters and numbers");
            return false;
        }

        if(material === ""){
            alert("Material must be filled out");
            return false;
        }

        if(material.trim() === ""){
            alert("Material cannot contain only space");
            return false;
        }

        var materialPattern = /^[a-zA-Z0-9%,\s:\/àáảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ\s]+$/;
        if (!materialPattern.test(material)) {
            alert("Material must contain only letters, numbers, and the '%' character");
            return false;
        }

        if (madeIn !== "" && madeIn.trim() !== "") { // Kiểm tra nếu người dùng nhập dữ liệu
            var madeInPattern = /^[a-zA-Z\s]+$/;  // Biểu thức chính quy cho chỉ chữ cái
            if (!madeInPattern.test(madeIn)) {
                alert("Made In can only contain letters");
                return false;  // Ngừng gửi form nếu không hợp lệ
            }
        }
    }
</script>
<script>
    window.onload = function () {
        const alertMessage = '<%= request.getAttribute("alertMessage") != null ? request.getAttribute("alertMessage") : "" %>';
        const alertType = '<%= request.getAttribute("alertType") != null ? request.getAttribute("alertType") : "" %>';
        if (alertMessage.trim() !== "" && alertType.trim() !== "") {
            Swal.fire({
                icon: alertType,
                title: alertMessage,
                showConfirmButton: false,
                timer: 2000
            });
        }

    };
</script>
</body>
</html>
