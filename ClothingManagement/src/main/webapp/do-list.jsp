<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/2/2025
  Time: 4:44 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>DO List</title>
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
<form id="poForm" action="GetDOServlet" method="post">
    <label for="supplier">Supplier:</label>

    <select name="supplier" id="supplier" >
        <option value="">-- Select Suppliers --</option>
        <c:forEach var="sup" items="${suppliers}">
            <option value="${sup.supplierId}">${sup.supplierName}</option>
        </c:forEach>
    </select>

<%--    <input type="text" name="poID" placeholder="Enter PO ID">--%>
    <input type="date" name="startDate" placeholder="startDate">
    <input type="date" name="endDate" placeholder="endDate">
<%--    <input type="text" name="createdBy" placeholder="createdBy">--%>
    <button type="submit" name="action" value="filter">Filter</button>
<%--    <button type="submit" name="action" value="selectAll">Select ALL</button>--%>
</form>
<script>
    document.getElementById("poForm").addEventListener("submit", function (event) {
        let startDate = document.querySelector("input[name='startDate']").value;
        let endDate = document.querySelector("input[name='endDate']").value;
        let poid = document.querySelector("input[name='poID']").value.trim();
        let createdBy = document.querySelector("input[name='createdBy']").value.trim();

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

        // Kiểm tra startDate không được sau endDate
        if (startDate && endDate && startDate > endDate) {
            alert("Start Date không được diễn ra sau End Date!");
            event.preventDefault();
            return;
        }

        // Kiểm tra CreatedBy (có thể null nhưng không được chứa khoảng trắng liên tục)
        if (createdBy.length > 0 && /\s{2,}/.test(createdBy)) {
            alert("Created By không được chứa khoảng trắng liên tục!");
            event.preventDefault();
            return;
        }
    });


</script>



    <table border="1">
    <tr>
        <th>Select</th>
        <th>DOID</th>
        <th>Planned Shipping Date</th>
        <th>POID</th>
        <th>Created By</th>
    </tr>
    <c:forEach var="dod" items="${activeDOs}">
        <tr>
            <td>
                <form action="SelectDOServlet" method="post">
                    <input type="hidden" name="doID" value="${dod.doID}">
                    <input type="hidden" name="poId" value="${dod.poID}">
                    <button type="submit">Select</button>
                </form>
            </td>
            <td>${dod.doID}</td>
            <td>${dod.plannedShippingDate}</td>
            <td>${dod.poID}</td>
            <td>${dod.createdBy}</td>
        </tr>
    </c:forEach>
</table>

</div>
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
