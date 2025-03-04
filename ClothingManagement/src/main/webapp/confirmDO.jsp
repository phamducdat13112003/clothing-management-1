<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Delivery Order</title>
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
            display: flex;
            gap: 20px;
        }
        .order-info, .product-list {
            flex: 1;
            padding: 15px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
            border-radius: 5px;
        }
        h3 {
            color: #333;
        }
    </style>
</head>
<body>

<h2>Xác nhận Đơn hàng</h2>

<div class="container">
    <div class="order-info">
        <h3>Purchase Order Detail</h3>
        <p><strong>PO ID:</strong> ${poID}</p>
        <p><strong>Quantiy Need To Receipt:</strong> ${quantity}</p>
        <p><strong>Confirm Person:</strong> <%= session.getAttribute("account_id") %></p>
    </div>

    <div class="product-list">
        <h3>Product Detail Information</h3>
        <c:if test="${not empty productDetail}">
            <div class="product-detail">
                <div><strong>ProductDetailID:</strong> ${productDetail.ProductDetailID}</div>
                <div><strong>ProductName:</strong> ${productDetail.ProductName}</div>
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

<form action="ConfirmDOServlet" method="post">
    <table>
        <tr>
            <th>DOID</th>
            <th>Số lượng</th>
            <th>ReceiptDate</th>
        </tr>
        <tr>
            <td><input type="text" name="doId" value="${doID}" readonly></td>
            <td><input type="number" name="quantity" min="1" required></td>
            <td><input type="date" name="ReceiptDate" required></td>
        </tr>
    </table>
    <input type="hidden" name="poId" value="${poID}">
    <input type="hidden" name="createBy" value="<%= session.getAttribute("account_id") %>">
    <input type="hidden" name="productDetailID" value="${productDetail.ProductDetailID}">
    <input type="hidden" name="quantityCheck" value="${quantity}">
    <button type="submit">Xác nhận</button>
</form>

<script>
    $(document).ready(function () {
        $('form').submit(function (event) {
            let doidInput = $('input[name="doId"]').val().trim();
            let quantityInput = $('input[name="quantity"]').val();
            let maxQuantity = ${quantity};

            let doidPattern = /^do\d+$/i;
            if (!doidPattern.test(doidInput)) {
                alert("DOID phải bắt đầu bằng 'do' và theo sau là số. Ví dụ: do123");
                event.preventDefault();
                return;
            }

            if (quantityInput === "" || parseInt(quantityInput) > maxQuantity) {
                alert("Số lượng không được để trống và không vượt quá " + maxQuantity);
                event.preventDefault();
            }
        });
    });
</script>

</body>
</html>
