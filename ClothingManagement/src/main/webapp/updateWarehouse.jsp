<%@ page import="org.example.clothingmanagement.entity.Warehouse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Warehouse Form</title>
</head>
<body>
<form action="<%= request.getAttribute("formAction") %>" method="POST">
    <input type="hidden" name="warehouseId" value="<%= request.getAttribute("warehouse") != null ? ((Warehouse) request.getAttribute("warehouse")).getWarehouseId() : "" %>">

    <label for="warehouseName">Warehouse Name</label>
    <input type="text" id="warehouseName" name="warehouseName" value="<%= request.getAttribute("warehouse") != null ? ((Warehouse) request.getAttribute("warehouse")).getWarehouseName() : "" %>" required>

    <label for="address">Address</label>
    <input type="text" id="address" name="address" value="<%= request.getAttribute("warehouse") != null ? ((Warehouse) request.getAttribute("warehouse")).getAddress() : "" %>" required>

    <label for="branchId">Branch</label>
    <input type="number" id="branchId" name="branchId" value="<%= request.getAttribute("warehouse") != null ? ((Warehouse) request.getAttribute("warehouse")).getBranchId() : "" %>" required>

    <button type="submit">
        <%= request.getAttribute("warehouse") != null ? "Update Warehouse" : "Add Warehouse" %>
    </button>
</form>
</body>
</html>
