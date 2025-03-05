<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transfer Order List</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<h1>Transfer Order List</h1>

<c:if test="${not empty errorMessage}">
    <div style="color: red;">
        <p>${errorMessage}</p>
    </div>
</c:if>

<!-- Create Transfer Order Button -->
<div>
    <a href="/ClothingManagement_war_exploded/transfer-order/create">
        <button>Create Transfer Order</button>
    </a>

</div>
<c:if test="${not empty successMessage}">
    <div style="color: limegreen;">
        <p>${successMessage}</p>
    </div>
</c:if>


<table border="1">
    <thead>
    <tr>
        <th>TOID</th>
        <th>Created By</th>
        <th>Status</th>
        <th>Created Date</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${transferOrders}">
        <tr>
            <td>${order.toID}</td>
            <td>${order.createdBy}</td>
            <td>${order.status}</td>
            <td>${order.createdDate}</td>
            <td>
                <!-- Link to view the transfer order details -->
                <a href="/ClothingManagement_war_exploded/transfer-order/view?toID=${order.toID}">View</a>

                <!-- Link to edit the transfer order -->
                <a href="/ClothingManagement_war_exploded/transfer-order/update?toID=${order.toID}">Edit</a>

                <!-- Link to delete the transfer order -->
                <a href="/ClothingManagement_war_exploded/transfer-order/list?action=delete&toID=${order.toID}"
                   onclick="return confirm('Are you sure you want to delete this transfer order?');">Delete</a>
            </td>

        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination controls (Optional, based on your pagination logic) -->
<div>
    <c:if test="${page > 1}">
        <a href="transfer-order?action=list&page=${page - 1}">Previous</a>
    </c:if>

    <c:if test="${not empty transferOrders}">
        <a href="transfer-order?action=list&page=${page + 1}">Next</a>
    </c:if>
</div>

</body>
</html>
