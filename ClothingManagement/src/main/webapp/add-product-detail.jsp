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
    <title>Add Product Detail</title>

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
        #image {
            margin-top: 10px;            /* Khoảng cách giữa ảnh và nút */
            max-width: 200px;            /* Giới hạn chiều rộng tối đa của ảnh */
            height: auto;                /* Giữ tỷ lệ cho ảnh */
            border-radius: 8px;          /* Bo góc cho ảnh */
            border: 2px solid #ddd;     /* Viền cho ảnh */
            display: block;              /* Đảm bảo ảnh không bị kéo giãn */
            margin-left: auto;           /* Căn giữa ảnh */
            margin-right: auto;          /* Căn giữa ảnh */
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
                                                    <div class="row">
                                                        <div class="col-12">
                                                            <div class="sherah-ptabs__separate">
                                                                <div class="sherah-ptabs__form-main">
                                                                    <div class="sherah__item-group sherah-default-bg sherah-border">
                                                                        <!-- End Profile Cover Info -->

                                                                        <div class="sherah-profile-info__v2 mg-top-30">
                                                                            <h3 class="sherah-profile-info__heading mg-btm-30">
                                                                                Add New Product Detail</h3>
                                                                            <div class="col-lg-12 col-12">
                                                                                <!-- Basic Information -->
                                                                                <div class="product-form-box sherah-border mg-top-30">
                                                                                    <h4 class="form-title m-0">Basic
                                                                                        Information
                                                                                        Of ${product.name}</h4>
                                                                                    <form name="myForm"
                                                                                            class="sherah-wc__form-main"
                                                                                          action="${pageContext.request.contextPath}/add-product-detail"
                                                                                          onsubmit="return validateForm()"
                                                                                          method="post"
                                                                                          enctype="multipart/form-data"
                                                                                    >

                                                                                        <div class="row">
                                                                                            <input type="hidden"
                                                                                                   name="id"
                                                                                                   value="${product.id}"/>
                                                                                            <div class="col-12">
                                                                                                <div class="form-group">
                                                                                                    <label class="sherah-wc__form-label">Image </label>
                                                                                                    <div class="form-group__input">
                                                                                                        <input type="file" name="img" class="form-control d-none" id="inputGroupFile04" onchange="chooseFile(this)" accept="image/gif,image/jpeg,image/png" aria-describedby="inputGroupFileAddon04" aria-label="Upload">
                                                                                                        <label for="inputGroupFile04" class="choose-file-label">Choose file</label>
<%--                                                                                                        <img src="${product.image}" id="image" class="img-thumbnail rounded-5" width="100%" alt="${employee.image}">--%>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>

                                                                                            <div class="form-group">
                                                                                                <label class="sherah-wc__form-label">Weight
                                                                                                    <span class="required">*</span></label>
                                                                                                <div class="form-group__input">
                                                                                                    <input class="sherah-wc__form-input"
                                                                                                           placeholder="Kilogram"
                                                                                                           type="number"
                                                                                                           name="weight"
                                                                                                           min="0.1"
                                                                                                           step="0.1"
                                                                                                           max="3"
                                                                                                           required>
                                                                                                    <small class="error-message"
                                                                                                           style="color: red;"></small>
                                                                                                </div>
                                                                                            </div>

                                                                                            <div class="form-group">
                                                                                                <label class="sherah-wc__form-label">Size
                                                                                                    <span class="required">*</span></label>
                                                                                                <div class="form-group__input">
                                                                                                    <select class="sherah-wc__form-input"
                                                                                                            id="size"
                                                                                                            name="size">
                                                                                                        <option value="XS">
                                                                                                            XS
                                                                                                        </option>
                                                                                                        <option value="S">
                                                                                                            S
                                                                                                        </option>
                                                                                                        <option value="M">
                                                                                                            M
                                                                                                        </option>
                                                                                                        <option value="L">
                                                                                                            L
                                                                                                        </option>
                                                                                                        <option value="XL">
                                                                                                            XL
                                                                                                        </option>
                                                                                                        <option value="XXL">
                                                                                                            XXL
                                                                                                        </option>
<%--                                                                                                        <option value="OneSize">--%>
<%--                                                                                                            One Size--%>
<%--                                                                                                        </option>--%>

                                                                                                    </select>
                                                                                                </div>
                                                                                            </div>

                                                                                            <div class="form-group">
                                                                                                <label class="sherah-wc__form-label">Color
                                                                                                    <span class="required">*</span></label>
                                                                                                <div class="form-group__input">
                                                                                                    <input class="sherah-wc__form-input"
                                                                                                           type="text"
                                                                                                           name="color"
                                                                                                           maxlength="50"
                                                                                                           required>
                                                                                                    <small class="error-message"
                                                                                                           style="color: red;"></small>

                                                                                                </div>
                                                                                            </div>

                                                                                            <div>
                                                                                                <button type="submit"
                                                                                                        class="btn btn-primary">
                                                                                                    Save
                                                                                                </button>
                                                                                                <button type="reset"
                                                                                                        class="btn btn-secondary">
                                                                                                    Reset
                                                                                                </button>
                                                                                            </div>

                                                                                        </div>
                                                                                    </form>
                                                                                </div>
                                                                                <!-- End Basic Information -->
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
<script type="text/javascript">
    function validateForm() {
        var color = document.forms["myForm"]["color"].value;



        // Kiểm tra xem trường "name" có bị bỏ trống không
        if (color === "") {
            alert("Color must be filled out");
            return false;
        }

        if(color.trim() === "" ){
            alert("Color cannot contain only space");
            return false
        }

        var colorPattern = /^[a-zA-Z\s]+$/; // Biểu thức chính quy chỉ cho phép chữ và số
        if (!colorPattern.test(color)) {
            alert("Color must contain only letters");
            return false;
        }


    }
</script>
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
