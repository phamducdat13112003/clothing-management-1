<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>DO Detail</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 0;
        }
        .dodetail-container {
            border: 2px solid #ccc;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .dodetail-title {
            font-size: 18px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }
        .dodetail-info {
            margin-bottom: 10px;
        }
        .dodetail-info span {
            font-weight: bold;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>

<h2>DO Detail</h2>

<!-- Lặp qua từng DODetail -->
<c:forEach var="doDetail" items="${doDetails}">
    <div class="dodetail-container">
        <div class="dodetail-title">DODetail ID: ${doDetail.doDetailID}</div>
        <div class="dodetail-info">
            <span>ProductDetailID:</span> ${doDetail.productDetailID} |
            <span>Số lượng:</span> ${doDetail.quantity} |
            <span>DOID:</span> ${doDetail.doID}
        </div>

        <!-- Bảng ProductDetail tương ứng với ProductDetailID -->
        <table>
            <tr>
                <th>ProductDetailID</th>
                <th>Product Name</th>
                <th>Gender</th>
                <th>Season</th>
                <th>Material</th>
                <th>Weight</th>
                <th>Color</th>
                <th>Size</th>
<%--                <th>Price</th>--%>
            </tr>
            <c:forEach var="product" items="${productDetails}">
                <c:if test="${product.ProductDetailID eq doDetail.productDetailID}">
                    <tr>
                        <td>${product.ProductDetailID}</td>
                        <td>${product.ProductName}</td>
                        <td>${product.Gender}</td>
                        <td>${product.Seasons}</td>
                        <td>${product.Material}</td>
                        <td>${product.Weight}</td>
                        <td>${product.Color}</td>
                        <td>${product.Size}</td>
<%--                        <td>${product.Price}</td>--%>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </div>
</c:forEach>

</body>
</html>
