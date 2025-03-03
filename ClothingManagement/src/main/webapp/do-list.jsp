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
</head>
<body>
<form action="GetDOServlet" method="post">
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
                <td><input type="checkbox" name="doID" value="${dod.doID}"></td>
                <td>${dod.doID}</td>
                <td>${dod.plannedShippingDate}</td>
                <td>${dod.poID}</td>
                <td>${dod.createdBy}</td>

            </tr>
        </c:forEach>
    </table>
    <button type="submit">Submit</button>
</form>


</body>
</html>
