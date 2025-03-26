<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 1/28/2025
  Time: 3:07 PM
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
    <title>Add Employee</title>

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

        .error-message {
            color: red;
            font-size: 12px;
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

        #image {
            margin-top: 10px; /* Khoảng cách giữa ảnh và nút */
            max-width: 200px; /* Giới hạn chiều rộng tối đa của ảnh */
            height: auto; /* Giữ tỷ lệ cho ảnh */
            border-radius: 8px; /* Bo góc cho ảnh */
            border: 2px solid #ddd; /* Viền cho ảnh */
            display: block; /* Đảm bảo ảnh không bị kéo giãn */
            margin-left: auto; /* Căn giữa ảnh */
            margin-right: auto; /* Căn giữa ảnh */
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
                                    <h2 class="sherah-breadcrumb__title">Add Employee</h2>
                                    <ul class="sherah-breadcrumb__list">
                                        <li><a href="manageemployee">Home</a></li>
                                    </ul>
                                    <c:if test="${not empty message}">
                                        <span class="error-message">${message}</span>
                                    </c:if>
                                    <c:if test="${not empty messageSuccess}">
                                        <span class="message">${messageSuccess}</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="sherah-page-inner sherah-border sherah-basic-page sherah-default-bg mg-top-25 p-0">
                            <form class="sherah-wc__form-main" action="addemployee" method="post"
                                  enctype="multipart/form-data">
                                <div class="row">
                                    <div class="col-lg-12 col-12">
                                        <!-- Basic Information -->
                                        <div class="product-form-box sherah-border mg-top-30">
                                            <h4 class="form-title m-0">Basic Information</h4>
                                            <div class="row">
                                                <div class="col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Employee Name <span
                                                                class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input value="${name}" class="sherah-wc__form-input"
                                                                   type="text" name="name" required>
                                                            <c:if test="${not empty errorName}">
                                                                <span class="error-message">${errorName}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Employee Image </label>
                                                        <div class="form-group__input">
                                                            <input type="file" name="img" class="form-control d-none"
                                                                   id="inputGroupFile04" onchange="chooseFile(this)"
                                                                   accept="image/gif,image/jpeg,image/png"
                                                                   aria-describedby="inputGroupFileAddon04"
                                                                   aria-label="Upload">
                                                            <label for="inputGroupFile04" class="choose-file-label">Choose
                                                                file</label>
                                                            <img src="${employee.image}" id="image"
                                                                 class="img-thumbnail rounded-5" width="100%"
                                                                 alt="${employee.image}">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Email <span
                                                                class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Email"
                                                                   type="email" name="email" value="${email}" required>
                                                            <c:if test="${not empty errorEmail}">
                                                                <span class="error-message">${errorEmail}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Phone <span
                                                                class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Phone"
                                                                   type="text" name="phone" value="${phone}" required>
                                                            <c:if test="${not empty errorPhone}">
                                                                <span class="error-message">${errorPhone}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Address <span
                                                                class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input class="sherah-wc__form-input" placeholder="Address"
                                                                   type="text" name="address" value="${address}"
                                                                   required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Gender <span
                                                                class="required">*</span></label>
                                                        <select class="form-group__input" name="gender"
                                                                aria-label="Default select example" required>
                                                            <option value="1" selected>Male</option>
                                                            <option value="0">Female</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-lg-6 col-md-6 col-12">
                                                    <div class="form-group">
                                                        <label class="sherah-wc__form-label">Date of birth <span
                                                                class="required">*</span></label>
                                                        <div class="form-group__input">
                                                            <input value="${dateOfBirth}" class="sherah-wc__form-input"
                                                                   placeholder="Date of birth" type="date" name="dob"
                                                                   required>
                                                            <c:if test="${not empty errorDateofBirth}">
                                                                <span class="error-message">${errorDateofBirth}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mg-top-40 sherah-dflex sherah-dflex-gap-30 justify-content-end">
                                            <button type="submit" class="sherah-btn sherah-btn__primary">Add</button>
                                            <button type="reset" class="sherah-btn sherah-btn__third">Cancel</button>
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
<script type="text/javascript">
    function chooseFile(fileInput) {
        if (fileInput.files && fileInput.files[0]) {
            var file = fileInput.files[0];
            var fileType = file.type;
            var validImageTypes = ["image/gif", "image/jpeg", "image/png"];

            if (!validImageTypes.includes(fileType)) {
                alert("Only image files (JPG, PNG, GIF) are allowed.");
                fileInput.value = ""; // Clear the input
                return;
            }

            var reader = new FileReader();

            reader.onload = function (e) {
                $('#image').attr('src', e.target.result);
            };
            reader.readAsDataURL(file); // đọc nội dung tệp dưới dạng url
        }
    }
</script>
<script>
    jQuery(document).ready(function ($) {
        $('[data-countdown]').each(function () {
            var $this = $(this), finalDate = $(this).data('countdown');
            $this.countdown(finalDate, function (event) {
                $this.html(event.strftime(' %H : %M : %S'));
            });
        });
    });
</script>

</body>
</html>

