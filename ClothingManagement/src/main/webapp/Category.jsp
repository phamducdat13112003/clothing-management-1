<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>

    <!-- Meta Tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Category</title>
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
        /* Căn giữa form và bảng */
        .container {
            width: 60%;
            margin: auto;
            text-align: center;
        }

        /* Style cho form tìm kiếm */
        .search-form {
            display: flex;
            justify-content: center;
            gap: 10px; /* Khoảng cách giữa các ô input */
            margin-bottom: 20px; /* Tạo khoảng cách với bảng */
        }

        .search-form input {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 20%; /* Điều chỉnh độ rộng để cân đối */
        }

        .search-form button {
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .search-form button:hover {
            background-color: #0056b3;
        }

        /* Style cho nút tạo mới */
        .add-category-form {
            text-align: center;
            margin-bottom: 20px;
        }

        .add-category-form button {
            padding: 10px 15px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .add-category-form button:hover {
            background-color: #218838;
        }

        .delete-form button {
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .delete-form button:hover {
            background-color: #a52834;
        }
        .edit-form button {
            padding: 8px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .edit-form button:hover {
            background-color: #0056b3;
        }

        /* Style cho bảng */
        table {
            border: 1px solid black;
            border-collapse: collapse;
            width: 60%;
            margin: auto;
        }

        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
    </style>

</head>
<body id="sherah-dark-light">

<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- Form Tìm Kiếm -->
<div class="container">
    <!-- Form tìm kiếm -->
    <form action="SelectCategory" method="get" class="search-form">
        <input type="text" name="categoryName" placeholder="Please Enter Name...">
        <input type="date" name="startDate" placeholder="Please Enter Start Date...">
        <input type="date" name="endDate" placeholder="Please Enter End Date...">
        <input type="text" name="createBy" placeholder="Please Enter Created By...">
        <button type="submit">Search</button>
    </form>


    <!-- Form tạo Category mới -->
    <form action="addCategory.jsp" method="get" class="add-category-form">
        <button type="submit">Add New Category</button>
    </form>
</div>

<!-- Bảng hiển thị Category -->
<table border="1">
    <tr>
        <th>STT</th> <!-- Số thứ tự -->
        <th>Category Name</th>
        <th>Create Date</th>
        <th>Create By</th>
        <th>Action</th>
    </tr>

    <c:forEach var="category" items="${categories}" varStatus="loop">
        <tr>
            <td>${loop.index + 1}</td> <!-- Hiển thị số thứ tự -->
            <td>${category.categoryName}</td>
            <td>${category.createdDate}</td>
            <td>${createdByNames[category.createdBy]}</td>
            <td>
                <form action="DeleteCategoryServlet" method="get" class="delete-form" style="display:inline;">
                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                    <button type="submit" onclick="return confirm('Do you want to delete?')">Delete</button>
                </form>
                <form action="EditCategoryServlet" method="get" class="edit-form" style="display:inline;">
                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                    <button type="submit">Edit</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>


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
