<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 1/28/2025
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Site Title -->
    <title>Check Inventory</title>

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
        /* Tổng thể */
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
            color: #333;
        }

        /* Tiêu đề */
        h2 {
            text-align: center;
            font-size: 24px;
            margin-bottom: 20px;
            color: #007bff;
        }

        /* Bảng */
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            background: #fff;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border: 1px solid #ddd;
        }

        th {
            background: #007bff;
            color: white;
            font-weight: bold;
            text-transform: uppercase;
        }

        td {
            background: #f9f9f9;
        }

        /* Ô nhập số */
        input[type="number"] {
            width: 80px;
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 4px;
            text-align: center;
        }

        /* Các nút */
        button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
        }

        button[type="submit"] {
            background: #28a745;
            color: white;
        }

        button[type="reset"] {
            background: #dc3545;
            color: white;
        }

        button:hover {
            opacity: 0.8;
        }

        /* Hiệu ứng hover */
        tr:hover {
            background-color: #f1f1f1;
        }

        /* Định dạng cho ô chênh lệch */
        td[id^="difference"] {
            font-weight: bold;
            color: #d9534f;
        }

        /* Responsive */
        @media screen and (max-width: 768px) {
            table {
                font-size: 14px;
            }

            input[type="number"] {
                width: 60px;
            }

            button {
                font-size: 14px;
                padding: 8px 16px;
            }
        }

    </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <h2>Check Inventory</h2>
        <form id="inventoryForm" action="CountInventoryServlet" method="post" onsubmit="validateForm(event)">
            <table border="1">
                <tr>
                    <th>ProductDetailID</th>
                    <th>Product Name</th>
                    <th>Size</th>
                    <th>Color</th>
                    <th>Original Quantity</th>
                    <th>Recount Quantity</th>
                    <th>Difference</th>
                </tr>
                <c:forEach var="list" items="${listDetail}" varStatus="status">
                    <tr>
                        <td>
                            <input type="hidden" name="productdetailId[]" value="${list.productDetailId}">
                                ${list.productDetailId}
                        </td>
                        <td>${productDetailToProductName[list.productDetailId]}</td>
                        <td>${list.size}</td>
                        <td>${list.color}</td>
                        <td>
                            <input type="hidden" id="originalQuantity${status.index}" name="originalQuantity[]" value="${list.quantity}">
                                ${list.quantity}
                        </td>
                        <td>
                            <input type="number" id="realQuantity${status.index}" name="realQuantity[]" oninput="updateDifference(${status.index})"  required>
                        </td>
                        <td id="difference${status.index}">0</td>
                    </tr>
                </c:forEach>
            </table>

            <input type="hidden" name="inventoryDocId" value="${inventoryDocId}">
            <input type="hidden" name="binId" value="${binId}">
            <button type="submit">Submit</button>
            <button type="button" onclick="window.location.href='ViewInventoryDocList'">Cancel</button>
        </form>

        <script>

            document.addEventListener("input", function (event) {
                if (event.target.name === "realQuantity[]") {
                    if (event.target.value < 0) {
                        alert("Do not enter negative number. Set to 0.");
                        event.target.value = 0; // Đặt lại giá trị 0 ngay lập tức
                    }
                }
            });

            function updateDifference(index) {
                let originalQuantity = parseInt(document.getElementById("originalQuantity" + index).value);
                let realQuantityInput = document.getElementById("realQuantity" + index);

                // Nếu nhập số âm, đặt lại giá trị về 0 và hiển thị cảnh báo
                if (realQuantityInput.value < 0) {
                    alert("Do not enter negative number. Set to 0.");
                    realQuantityInput.value = 0; // Đặt lại giá trị ô nhập
                }

                let realQuantity = parseInt(realQuantityInput.value) || 0;
                let difference = originalQuantity - realQuantity;

                document.getElementById("difference" + index).innerText = difference;
            }


            function validateForm(event) {
                let inputs = document.getElementsByName("realQuantity[]");
                let originals = document.getElementsByName("originalQuantity[]");
                let isValid = true;

                for (let i = 0; i < inputs.length; i++) {
                    let realQuantity = parseInt(inputs[i].value) || 0;
                    let originalQuantity = parseInt(originals[i].value) || 0;

                    let difference = Math.abs(originalQuantity - realQuantity);
                    let threshold = originalQuantity * 0.2; // 20% của originalQuantity

                    if (difference > threshold) {
                        let confirmAction = confirm("The difference is greater than 20%. Do you want continue?");
                        if (!confirmAction) {
                            event.preventDefault(); // Hủy gửi form
                            return;
                        }
                        break; // Nếu chọn tiếp tục, bỏ qua kiểm tra các hàng khác
                    }
                }

                if (isValid) {
                    document.getElementById("inventoryForm").submit(); // Gửi form nếu hợp lệ
                }
            }
        </script>

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
</body>
</html>