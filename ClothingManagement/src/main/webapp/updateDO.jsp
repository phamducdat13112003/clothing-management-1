<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Update DO</title>
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
        .do-container {
            border: 2px solid #007bff;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            background-color: #e9f5ff;
        }
        input {
            width: 100%;
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<h2>Update Delivery Order</h2>

<!-- Form cập nhật thông tin DO -->
<form action="UpdateDOServlet" method="post">
    <div class="do-container">
        <div class="dodetail-title">
            <label><span>Delivery Order ID:</span></label>
            <input type="text" name="doID" value="${dor.doID}" readonly>
        </div>

        <div class="dodetail-info">
            <label><span>Created By:</span></label>
            <input type="text" name="createdBy"  value="<%=session.getAttribute("account_id") %>" readonly>

            <label><span>Planned Shipping Date:</span></label>
            <input type="date" name="plannedShippingDate" value="${dor.plannedShippingDate}" required>
        </div>
    </div>

    <!-- Lặp qua từng DODetail -->
    <c:forEach var="doDetail" items="${doDetails}">
        <div class="dodetail-container">
            <div class="dodetail-title">DODetail ID: ${doDetail.doDetailID}</div>

            <div class="dodetail-info">
                <span>ProductDetailID:</span> ${doDetail.productDetailID}
                <input type="hidden" name="productDetailID_${doDetail.doDetailID}" value="${doDetail.productDetailID}">
                <span>DOID:</span> ${doDetail.doID}
<%--                <span>Remain Quantity:</span> ${remainingQuantities[doDetail.productDetailID] + doDetail.quantity}--%>
            </div>

            <div class="dodetail-info">
                <label><span>Số lượng:</span></label>
                <input type="number" name="quantity_${doDetail.doDetailID}"
                       value="${doDetail.quantity}"
                       min="1"
                       max="${remainingQuantities[doDetail.productDetailID] + doDetail.quantity}"
                       readonly>
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
<%--                    <th>Price</th>--%>
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
<%--                            <td>${product.Price}</td>--%>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </c:forEach>
    <button type="submit">Update</button>
</form>

</body>
</html>
