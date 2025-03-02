<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Transfer Order</title>
</head>
<body>

<h1>Update Transfer Order</h1>

<form action="/ClothingManagement_war/transfer-order/update" method="post">
    <!-- Transfer Order ID -->
    <label for="toID">Transfer Order ID:</label><br>
    <input type="text" id="toID" name="toID" value="${transferOrder.toID}" readonly><br><br>

    <!-- Created Date -->
    <label for="createdDate">Created Date:</label><br>
    <input type="date" id="createdDate" name="createdDate" value="${transferOrder.createdDate}" required><br><br>

    <label for="createdBy">Created By:</label>
    <select name="createdBy" id="createdBy" required>
        <c:forEach var="employeeID" items="${employeeIds}">
            <option value="${employeeID}" ${employeeID == transferOrder.createdBy ? 'selected' : ''}>${employeeID}</option>
        </c:forEach>
    </select>


    <select name="status" id="status" required>
        <option value="Processing" ${'Processing' == transferOrder.status ? 'selected' : ''}>Processing</option>
        <option value="Done" ${'Done' == transferOrder.status ? 'selected' : ''}>Done</option>
        <option value="Cancelled" ${'Cancelled' == transferOrder.status ? 'selected' : ''}>Cancelled</option>
    </select>


    <!-- Origin Bin ID -->
    <label for="originBinID">Origin Bin ID:</label><br>
    <input type="text" id="originBinID" name="originBinID" value="${toDetails[0].originBinID}" required><br><br>

    <!-- Final Bin ID -->
    <label for="finalBinID">Final Bin ID:</label><br>
    <input type="text" id="finalBinID" name="finalBinID" value="${toDetails[0].finalBinID}" required><br><br>

    <!-- Product Details -->
    <h3>Product Details</h3>
    <div id="productDetailsContainer">
        <c:forEach var="toDetail" items="${toDetails}">
            <div class="product-row">
                <%--@declare id="productdetailid"--%><%--@declare id="quantity"--%><label for="productDetailID">Product Detail ID:</label>
                <input type="text" name="productDetailID[]" value="${toDetail.productDetailID}" required><br><br>

                <label for="quantity">Quantity:</label>
                <input type="number" name="quantity[]" value="${toDetail.quantity}" required><br><br>
            </div>
        </c:forEach>
    </div>

    <button type="button" onclick="addProductRow()">Add Another Product</button><br><br>

    <button type="submit">Update Transfer Order</button>

    <!-- Display error message if any -->
    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>

    <!-- Display success message if any -->
    <c:if test="${not empty successMessage}">
        <p style="color:green;">${successMessage}</p>
    </c:if>
</form>

<script>
    // Function to add another product row dynamically
    function addProductRow() {
        var container = document.getElementById("productDetailsContainer");

        // Create a new div element to hold the inputs
        var newRow = document.createElement("div");
        newRow.classList.add("product-row");

        // Create the Product Detail ID input field
        var productDetailLabel = document.createElement("label");
        productDetailLabel.setAttribute("for", "productDetailID");
        productDetailLabel.innerText = "Product Detail ID:";
        var productDetailInput = document.createElement("input");
        productDetailInput.setAttribute("type", "text");
        productDetailInput.setAttribute("name", "productDetailID[]");
        productDetailInput.setAttribute("required", true);

        // Create the Quantity input field
        var quantityLabel = document.createElement("label");
        quantityLabel.setAttribute("for", "quantity");
        quantityLabel.innerText = "Quantity:";
        var quantityInput = document.createElement("input");
        quantityInput.setAttribute("type", "number");
        quantityInput.setAttribute("name", "quantity[]");
        quantityInput.setAttribute("required", true);

        // Append the elements to the new row
        newRow.appendChild(productDetailLabel);
        newRow.appendChild(productDetailInput);
        newRow.appendChild(quantityLabel);
        newRow.appendChild(quantityInput);

        // Append the new row to the container
        container.appendChild(newRow);
    }
</script>

</body>
</html>
