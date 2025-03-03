<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/2/2025
  Time: 4:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Delivery Order For Warehouse Staff</title>
    <!-- Meta Tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

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
        .container {
            width: 60%;
            margin: auto;
            padding: 20px;
        }

        /* Đảm bảo form và bảng nằm trên dưới */
        .content {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 20px;
        }

        /* Form styling */
        .form-container {
            width: 60%;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
        }

        .form-container input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        /* Table container */
        .table-container {
            width: 100%;
            overflow-x: auto; /* Cho phép cuộn ngang nếu bảng quá lớn */
            display: flex;
            justify-content: center;
        }

        /* Table styling */
        table {
            width: 80%;
            border-collapse: collapse;
            margin-top: 20px;
            min-width: 600px; /* Đảm bảo bảng không quá nhỏ */
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        th {
            background-color: #f4f4f4;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        /* Buttons */
        button {
            padding: 10px 20px;
            margin: 10px;
            border: none;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border-radius: 5px;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<div class="container">

<h2>Delivery Order For Warehouse Staff</h2>

         <form action="DOListOpen" method="get">
             <button type="submit">Choose DO</button>
         </form>
    <%-- Hiển thị lỗi nếu có --%>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
<form action="ConfirmDOServlet" method="post">
    <%-- Hiển thị bảng nếu có dữ liệu --%>
    <c:if test="${not empty doDetails}">
        <table border="1">
            <tr>
                <th>Remove</th>
                <th>Product Detail ID</th>
                <th>Product Name</th>
                <th>Gender</th>
                <th>Seasons</th>
                <th>Material</th>
                <th>Weight</th>
                <th>Color</th>
                <th>Size</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total Price</th>
            </tr>
            <c:forEach var="detail" items="${doDetails}">
                <tr>
                    <!-- Ô checkbox để chọn mục (không ảnh hưởng đến dữ liệu gửi đi) -->
                    <td><input type="checkbox" class="remove-checkbox" value="${detail.DODetailID}"></td>

                    <!-- DODetailID ẩn để gửi dữ liệu -->
                    <input type="hidden" name="DODetailID[]" value="${detail.DODetailID}">

                    <td>${detail.ProductDetailID}</td>
                    <td>${detail.ProductName}</td>
                    <td>${detail.Gender}</td>
                    <td>${detail.Seasons}</td>
                    <td>${detail.Material}</td>
                    <td>${detail.Weight}</td>
                    <td>${detail.Color}</td>
                    <td>${detail.Size}</td>

                    <!-- Input số lượng -->
                    <td>
                        <input type="number"
                               name="quantity[]"
                               value="${detail.Quantity}"
                               min="1"
                               max="${detail.Quantity}"
                               class="quantity-input"
                               data-price="${detail.Price}">
                    </td>

                    <td>${detail.Price}</td>
                    <td><input type="text" class="total-price" value="0.00" readonly></td>
                </tr>
            </c:forEach>

        </table>


        <label for="ReceiptDate">ReceiptDate:</label>
        <input type="date" id="ReceiptDate" name="ReceiptDate">

        <label for="createBy">Created By:</label>
        <input type="text" id="createBy" name="createBy"
               value="<%= session.getAttribute("account_id") %>" readonly>


        <button type="submit">Submit</button>
        <button id="removeSelected">Remove Selected</button>
        <button type="reset">reset</button>

    </c:if>

</form>


</div>
     <script>
         // document.addEventListener("DOMContentLoaded", function () {
         //     document.querySelectorAll(".quantity-input").forEach(input => {
         //         input.addEventListener("input", function () {
         //             let min = 0;
         //             let max = parseInt(this.getAttribute("max"), 10);
         //             let value = parseInt(this.value, 10);
         //
         //             if (isNaN(value) || value < min) {
         //                 alert("Số lượng không thể <= 0.");
         //                 this.value = min;
         //             } else if (value > max) {
         //                 alert("Số lượng vượt quá số lượng đã đặt.");
         //                 this.value = max;
         //             }
         //
         //             // Cập nhật tổng giá
         //             let quantity = parseFloat(this.value) || 0;
         //             let price = parseFloat(this.dataset.price) || 0;
         //             let totalPriceField = this.closest("tr").querySelector(".total-price");
         //             totalPriceField.value = (quantity * price).toFixed(2);
         //         });
         //     });
         //
         //     // Xóa dòng được chọn
         //     document.getElementById("removeSelected").addEventListener("click", function () {
         //         document.querySelectorAll(".remove-checkbox:checked").forEach(function (checkbox) {
         //             checkbox.closest("tr").remove();
         //         });
         //     });
         // });
         document.addEventListener("DOMContentLoaded", function () {
             document.querySelectorAll(".quantity-input").forEach(input => {
                 input.addEventListener("input", function () {
                     let min = 1; // Không cho phép số lượng nhỏ hơn 1
                     let max = parseInt(this.getAttribute("max"), 10);
                     let value = parseInt(this.value, 10);

                     if (isNaN(value) || value < min) {
                         this.value = min;
                     } else if (value > max) {
                         this.value = max;
                     }

                     // Cập nhật tổng giá dựa trên số lượng và giá sản phẩm
                     let quantity = parseFloat(this.value) || 0;
                     let price = parseFloat(this.dataset.price) || 0;
                     let totalPriceField = this.closest("tr").querySelector(".total-price");
                     totalPriceField.value = (quantity * price).toFixed(2);
                 });

                 // Kích hoạt cập nhật tổng giá ngay khi tải trang
                 input.dispatchEvent(new Event("input"));
             });

             // Xóa các dòng được chọn
             document.getElementById("removeSelected").addEventListener("click", function () {
                 let checkedBoxes = document.querySelectorAll(".remove-checkbox:checked");
                 if (checkedBoxes.length === 0) {
                     alert("Vui lòng chọn ít nhất một mục để xóa.");
                     return;
                 }

                 checkedBoxes.forEach(function (checkbox) {
                     checkbox.closest("tr").remove();
                 });
             });
         });

     </script>
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
</body>
</html>
