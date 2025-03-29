<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 1/28/2025
  Time: 8:52 PM
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
    <title>View Product Detail</title>

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


        .choose-file-label {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            font-size: 14px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-align: center;
            margin-top: 10px;
        }

        .choose-file-label:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }

        .choose-file-label:active {
            background-color: #003f7f;
        }



        /* Căn chỉnh ảnh vào giữa */
        .product-image {
            margin: 0 auto;
            max-width: 100%;  /* Đảm bảo ảnh không vượt quá chiều rộng của container */
            height: 250px;
            width: 250px;
            display: block;
            justify-content: center;  /* Căn giữa theo chiều ngang */
            align-items: center;

            border-radius: 10px; /* Thêm bo góc cho ảnh */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Thêm bóng cho ảnh */
        }

        /* Thêm hiệu ứng khi di chuột qua */
        .product-image:hover {
            transform: scale(1.05); /* Phóng to ảnh khi di chuột qua */
            transition: transform 0.3s ease-in-out; /* Thêm hiệu ứng mượt mà */
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
                                    <h2 class="sherah-breadcrumb__title">product detail</h2>
                                </div>
                            </div>
                        </div>
                        <div class="sherah-page-inner sherah-border sherah-basic-page sherah-default-bg mg-top-25 p-0">
                            <form class="sherah-wc__form-main"
                                  action="${pageContext.request.contextPath}/view-product-detail" method="post"
                                  enctype="multipart/form-data" >
                                <div class="row">
                                    <div class="col-lg-6 col-12">
                                        <!-- Organization -->
                                        <div class="product-form-box sherah-border mg-top-30">
                                            <h4 class="form-title m-0"></h4>
                                                <div class="form-group">
                                                    <img src="${pd.image}" class="product-image" alt="Product Image">
                                                </div>
                                        </div>
                                    </div>

                                    <div class="col-lg-6 col-12">
                                        <!-- Basic Information -->
                                        <div class="product-form-box sherah-border mg-top-30">
                                            <h4 class="form-title m-0">Basic Information Of ${p.name}</h4>
                                            <div class="row">
                                                <input type="hidden" name="id" value="${pd.id}"/>
                                                <div class="col-12">
                                                    <div class="form-group">
                                                        <div class="form-group__input">
                                                            <c:if test="${sessionScope.role == 1}">
                                                                <div>
                                                                    <label class="sherah-wc__form-label">Product Image Upload </label>
                                                                </div>
                                                                <input type="file" name="img" class="form-control d-none"
                                                                   id="inputGroupFile04" onchange="chooseFile(this)"
                                                                   accept="image/gif,image/jpeg,image/png"
                                                                   aria-describedby="inputGroupFileAddon04"
                                                                   aria-label="Upload">
                                                            <label for="inputGroupFile04" class="choose-file-label">Choose file</label>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Weight</label>
                                                        <div class="form-group__input">
                                                            <c:if test="${sessionScope.role == 1}">
                                                                <input class="sherah-wc__form-input" placeholder="Kilogram"
                                                                   type="number" name="weight" min="0.1" step="0.1" max="3.0"
                                                                   value="${pd.weight}" required>
                                                            </c:if>
                                                            <c:if test="${sessionScope.role != 1}">
                                                                <input class="sherah-wc__form-input" placeholder="Kilogram"
                                                                       type="number" name="weight" min="0.1" step="0.1" max="10.0"
                                                                       value="${pd.weight}" readonly>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Size</label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="size"
                                                                   type="text" name="size"
                                                                   value="${pd.size}" readonly>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Color</label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="color"
                                                                   type="text" name="color"
                                                                   value="${pd.color}" readonly>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div>
                                                <c:if test="${sessionScope.role == 1}">
                                                    <button type="submit" class="btn btn-primary">Update</button>
                                                    <button type="reset" class="btn btn-secondary">Reset</button>
                                                </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- End Basic Information -->
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
<input type="file" id="inputGroupFile04" />
<script>
    const fileInput = document.getElementById("inputGroupFile04");

    fileInput.addEventListener("change", function() {
        const file = fileInput.files[0]; // Lấy file được chọn
        if (file) {
            const fileName = file.name;
            const fileExtension = fileName.split('.').pop().toLowerCase(); // Lấy phần mở rộng của file

            if (fileExtension === "png" || fileExtension === "jpg") {
                alert("File hợp lệ!");
            } else {
                alert("Vui lòng chọn file có đuôi .png hoặc .jpg");
                fileInput.value = ""; // Xóa file đã chọn nếu không hợp lệ
            }
        }
    });
</script>

</body>
</html>


