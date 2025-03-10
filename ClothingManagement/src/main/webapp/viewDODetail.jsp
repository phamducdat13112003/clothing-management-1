<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/8/2025
  Time: 7:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Danh sách DODetail cho DOID: ${doid}</h2>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<c:if test="${empty doDetails}">
    <p>Không có chi tiết nào cho DOID này.</p>
</c:if>

<c:if test="${not empty doDetails}">
    <table>
        <tr>
            <th>DODetail ID</th>
            <th>Product Detail ID</th>
            <th>Số lượng</th>
        </tr>
        <c:forEach var="detail" items="${doDetails}">
            <tr>
                <td>${detail.dODetailID}</td>
                <td>${detail.productDetailID}</td>
                <td>${detail.quantity}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<br>
<a href="index.jsp">Quay lại</a>
</body>
</html>
