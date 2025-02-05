<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/31/2025
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Category</title>
</head>
<body>
<h2>Edit Category</h2>
<form action="EditCategoryServlet" method="post">
    <input type="hidden" name="categoryId" value="${category.categoryID}">
    <label for="categoryName">Category Name:</label>
    <input type="text" id="categoryName" name="categoryName" value="${category.categoryName}" >
    <br>

    <button type="submit">Update</button>
</form>
</body>
</html>

