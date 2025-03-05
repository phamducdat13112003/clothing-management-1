<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/4/2025
  Time: 9:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng nhập dữ liệu</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        button {
            margin-top: 10px;
        }
        .container {
            display: flex; /* Sử dụng flexbox để đặt hai div nằm ngang */
            gap: 20px; /* Khoảng cách giữa hai div */
        }
        .order-info, .product-list {
            flex: 1; /* Chia đều kích thước */
            padding: 15px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
            border-radius: 5px;
        }
        .product-container {
            border: 1px solid #ddd;
            padding: 10px;
            margin: 10px 0;
            background-color: #fff;
            border-radius: 5px;
        }
        h3 {
            color: #333;
        }
    </style>
</head>
<body>

<h2>Bảng Nhập Dữ Liệu</h2>

<div class="container">
    <!-- Hiển thị thông tin đơn hàng -->
    <div class="order-info">
        <h3>Purchase Order Information</h3>
        <p><strong>PO ID:</strong> ${poID}</p>
        <p><strong>Quantity:</strong> ${quantity}</p>
        <p><strong>Created By:</strong> <%= session.getAttribute("account_id") %></p>
    </div>

    <!-- Hiển thị danh sách sản phẩm -->
    <div class="product-list">
        <h3>Product Details</h3>
        <c:if test="${not empty productDetail}">
            <div class="product-detail">
                <div><strong>Product Detail ID:</strong> ${productDetail.ProductDetailID}</div>
                <div><strong>Product Name:</strong> ${productDetail.ProductName}</div>
                <div><strong>Gender:</strong> ${productDetail.Gender}</div>
                <div><strong>Season:</strong> ${productDetail.Seasons}</div>
                <div><strong>Material:</strong> ${productDetail.Material}</div>
                <div><strong>Weight:</strong> ${productDetail.Weight} kg</div>
                <div><strong>Color:</strong> ${productDetail.Color}</div>
                <div><strong>Size:</strong> ${productDetail.Size}</div>
                <div><strong>Price:</strong> ${productDetail.Price} VND</div>
            </div>
        </c:if>
    </div>
</div>
<form action="AddDOServlet" method="post">
<table id="dataTable">
    <tr>
        <th>DOID</th>
        <th>Quantity</th>
        <th>PlannedShippingDate</th>
    </tr>
    <tr>
        <td><input type="text" name="doId"></td>
        <td><input type="number" name="quantity" min="1" ></td>
        <td><input type="date" name="plannedShippingDate" required></td>
    </tr>
</table>
    <input type="hidden" name="poId" value="${poID}">
    <input type="hidden" name="createBy"
           value="<%= session.getAttribute("account_id") %>" >
    <input type="hidden" name="productDetailID" value="${productDetail.ProductDetailID}">

    <button type="submit">ADD</button>
</form>
<script>
    $(document).ready(function () {
        // Cập nhật tổng giá khi thay đổi số lượng
        $(".quantity-input").on("input", function () {
            let quantity = parseFloat($(this).val()) || 0;
            let price = parseFloat($(this).data("price")) || 0;
            let totalPriceField = $(this).closest("tr").find(".total-price");
            totalPriceField.val((quantity * price).toFixed(2));
        });

        // Kiểm tra khi submit form
        $('form').submit(function (event) {
            let doidInput = $('input[name="doId"]').val().trim();
            let quantityInput = $('input[name="quantity"]').val();
            let maxQuantity = ${quantity}; // Lấy giá trị quantity từ JSP

            // Kiểm tra điều kiện DOID
            let doidPattern = /^do\d+$/i; // "do" + số (không phân biệt hoa thường)
            if (!doidPattern.test(doidInput)) {
                alert("DOID phải theo cú pháp 'do' theo sau là số. Ví dụ: do123");
                event.preventDefault();
                return;
            }

            // Kiểm tra khoảng trắng liên tục
            if (/\s{2,}/.test(doidInput)) {
                alert("DOID không được chứa khoảng trắng liên tục.");
                event.preventDefault();
                return;
            }

            // Kiểm tra số lượng
            if (quantityInput === "" || parseInt(quantityInput) > maxQuantity) {
                alert("Số lượng không được để trống và không được vượt quá " + maxQuantity);
                event.preventDefault();
            }
        });
    });

</script>
</body>
</html>
