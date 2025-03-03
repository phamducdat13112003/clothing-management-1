<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Transfer Order Details</title>
</head>
<body>

<h1>Transfer Order Details</h1>

<!-- Display Transfer Order Info -->
<h3>Transfer Order ID: ${transferOrder.toID}</h3>
<p>Created By: ${transferOrder.createdBy}</p>
<p>Status: ${transferOrder.status}</p>

<hr>

<h3>TODetails</h3>

<!-- Table to display TODetails -->
<table border="1">
  <thead>
  <tr>
    <th>Product Detail ID</th>
    <th>Quantity</th>
    <th>Origin Bin ID</th>
    <th>Final Bin ID</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="toDetail" items="${toDetails}">
    <tr>
      <td>${toDetail.productDetailID}</td>
      <td>${toDetail.quantity}</td>
      <td>${toDetail.originBinID}</td>
      <td>${toDetail.finalBinID}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<hr>

<!-- Button to go back to the Transfer Order List -->
<a href="/ClothingManagement_war/transfer-order/list">Back to Transfer Orders</a>

</body>
</html>
