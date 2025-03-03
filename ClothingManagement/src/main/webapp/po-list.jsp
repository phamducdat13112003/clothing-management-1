<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Danh sách Purchase Order</title>
</head>
<body>
<h2>Danh sách Purchase Order</h2>
<form id="poForm" action="GetPoServlet" method="post">
    <table border="1">
        <tr>
            <th>Select</th>
            <th>PO ID</th>
            <th>Created Date</th>
            <th>Supplier</th>
            <th>Created By</th>
            <th>Total Price</th>
        </tr>
        <c:forEach var="po" items="${poList}">
            <tr>
                <td><input type="checkbox" name="poID" value="${po.poID}"></td>
                <td>${po.poID}</td>
                <td>${po.createdDate}</td>
                <td>${supplierNames[po.supplierID] != null ? supplierNames[po.supplierID] : "Unknown Supplier"}</td>
                <td>${employeeNames[po.createdBy] != null ? employeeNames[po.createdBy] : "Unknown Employee"}</td>
                <td>${po.totalPrice}</td>
            </tr>
        </c:forEach>
    </table>

    <button type="submit">Submit</button>
</form>
</body>
</html>
