<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Category</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
        }
    </style>
</head>
<body>
<h1>Quản lý Category</h1>

<!-- Form Tìm Kiếm -->
<form action="#" method="get">
    <select name="searchType">
        <option value="categoryName">Category Name</option>
        <option value="categoryID">Category ID</option>
        <option value="createdDate">Created Date</option>
        <option value="createdBy">Created By</option>
    </select>
    <input type="text" name="query" placeholder="Please Enter...">
    <button type="submit">Tìm kiếm</button>
</form>

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
                <form action="#" method="post" style="display:inline;">
                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                    <button type="submit" onclick="return confirm('Do you want to delete?')">Delete</button>
                </form>
                <form action="#" method="get" style="display:inline;">
                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<!-- Nút Tạo Category Mới -->
<form action="add.jsp" method="get">
    <button type="submit">Tạo Category Mới</button>
</form>
</body>
</html>
