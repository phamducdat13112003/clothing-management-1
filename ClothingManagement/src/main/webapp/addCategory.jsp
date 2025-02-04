<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/29/2025
  Time: 5:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>
<%@ page session="true" %>
<%
    // Lấy ngày hiện tại theo định dạng YYYY-MM-DD
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    String currentDate = sdf.format(new Date());

    // Lấy userId từ session (Giả sử session có lưu user ID với key "userId")
//    Integer userId = (Integer) session.getAttribute("userId");
//
//    // Kiểm tra nếu chưa đăng nhập thì chuyển hướng về trang login
//    if (userId == null) {
//        response.sendRedirect("login.jsp");
//        return;
//    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Danh Mục</title>
</head>
<body>
<h2>Thêm Danh Mục Mới</h2>
<form action="AddCategoryServlet" method="post">

    <%--@declare id="categoryname"--%><label for="categoryName">CategoryName:</label>
    <input type="text" name="categoryName" required><br><br>

    <button type="submit">Thêm danh mục</button>
</form>
</body>
</html>

