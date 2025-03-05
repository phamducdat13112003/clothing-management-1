<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Danh sách Purchase Order</title>
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
        /* Căn giữa toàn bộ nội dung */
        .container {
            width: 65%;
            margin: auto;
            text-align: center;
        }

        /* Form lọc */
        #poForm {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        #poForm select,
        #poForm input {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 20%;
        }

        /* Style cho các nút */
        #poForm button {
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        #poForm button:hover {
            background-color: #0056b3;
        }

        /* Bảng dữ liệu */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid black;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        td {
            background-color: #f8f9fa;
        }

        /* Form chọn sản phẩm */
        form[action="SelectDataForDO"] button {
            background-color: #28a745;
            padding: 6px 12px;
            border-radius: 5px;
        }

        form[action="SelectDataForDO"] button:hover {
            background-color: #218838;
        }


    </style>

</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<div class="container">
<h2>Select Data for DO</h2>






<c:if test="${empty poList}">
<form id="poForm" action="GetDODetailData" method="post">
    <label for="supplier">Supplier:</label>

    <select name="supplier" id="supplier" >
        <option value="">-- Select Suppliers --</option>
        <c:forEach var="sup" items="${suppliers}">
            <option value="${sup.supplierId}">${sup.supplierName}</option>
        </c:forEach>
    </select>

    <input type="text" name="poID" placeholder="Enter PO ID">
    <input type="date" name="startDate">
    <input type="date" name="endDate">

    <button type="submit" name="action" value="filter">Filter</button>
    <button type="submit" name="action" value="selectAll">Select ALL</button>
</form>
</c:if>



    <c:if test="${not empty poList}">
        <table border="1">
            <tr>
                <th>Select</th>
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
            <c:forEach var="detail" items="${poDetails}">
                <c:set var="productDetailInfo" value="${productDetails.stream().filter(p -> p.ProductDetailID == detail.productDetailID).findFirst().orElse(null)}" />
                <tr>
                    <td>
                        <form action="SelectDataForDO" method="get">
                            <input type="hidden" name="poID" value="${detail.poID}">
                            <input type="hidden" name="productDetailID" value="${detail.productDetailID}">
                            <input type="hidden" name="quantity" value="${detail.quantity}">
                            <button type="submit">Select</button>
                        </form>
                    </td>
                    <td>
                        <input type="text" name="productDetailID_${detail.productDetailID}" value="${detail.productDetailID}" readonly>
                    </td>
                    <td>${productDetailInfo.ProductName}</td>
                    <td>${productDetailInfo.Gender}</td>
                    <td>${productDetailInfo.Seasons}</td>
                    <td>${productDetailInfo.Material}</td>
                    <td>${productDetailInfo.Weight}</td>
                    <td>${productDetailInfo.Color}</td>
                    <td>${productDetailInfo.Size}</td>
                    <td>${detail.quantity}</td>
                    <td><fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true" /></td>
                    <td><fmt:formatNumber value="${detail.totalPrice}" type="number" groupingUsed="true" /></td>

                </tr>
            </c:forEach>
        </table>
    </c:if>

</div>


<script>
    document.getElementById("poForm").addEventListener("submit", function(event) {
        let poid = document.querySelector("input[name='poID']").value.trim();
        let startDate = document.querySelector("input[name='startDate']").value;
        let endDate = document.querySelector("input[name='endDate']").value;

        // Kiểm tra POID: Nếu không rỗng, phải hợp lệ
        if (poid !== "") {
            let poidRegex = /^PO\d+$/i; // Dùng flag "i" để không phân biệt hoa thường
            if (!poidRegex.test(poid)) {
                alert("POID phải bắt đầu bằng 'PO' (không phân biệt hoa thường) và theo sau là số!");
                event.preventDefault();
                return;
            }

            // Kiểm tra không có khoảng trắng liên tục
            if (/\s{2,}/.test(poid)) {
                alert("POID không được chứa khoảng trắng liên tục!");
                event.preventDefault();
                return;
            }
        }

        // Kiểm tra Start Date và End Date nếu cả hai cùng được nhập
        if (startDate && endDate) {
            let start = new Date(startDate);
            let end = new Date(endDate);

            if (start >= end) {
                alert("Start Date phải diễn ra trước End Date!");
                event.preventDefault();
                return;
            }
        }
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
