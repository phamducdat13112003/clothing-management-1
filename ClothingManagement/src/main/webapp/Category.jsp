<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Category</title>


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
<body>
<h1><a href="homepage.jsp">Category</a></h1>

<!-- Form Tìm Kiếm -->
<div class="container">
    <!-- Form tìm kiếm -->
    <form action="SelectCategory" method="get" class="search-form">
        <input type="number" name="categoryId" placeholder="Please Enter ID...">
        <input type="text" name="categoryName" placeholder="Please Enter CategoryName...">
        <input type="date" name="createDate" placeholder="Please Enter Create Date...">
        <input type="number" name="createBy" placeholder="Please Enter Create By...">
        <button type="submit">Search</button>
    </form>

    <!-- Form tạo Category mới -->
    <form action="addCategory.jsp" method="get" class="add-category-form">
        <button type="submit">Add New Category</button>
    </form>
</div>

<!-- Bảng hiển thị Category -->
<table>
    <tr>
        <th>Category ID</th>
        <th>Category Name</th>
        <th>CreateDate</th>
        <th>CreateBy</th>
        <th>Action</th>
    </tr>

    <c:forEach var="category" items="${categories}">
        <tr>
            <td>${category.categoryID}</td>
            <td>${category.categoryName}</td>
            <td>${category.createdDate}</td>
            <td>${category.createdBy}</td>
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


</body>
</html>
