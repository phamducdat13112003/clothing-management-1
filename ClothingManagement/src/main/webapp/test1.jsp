<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Transfer Order</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f6f8;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .form-container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .form-row {
            display: flex;
            margin-bottom: 20px;
            justify-content: space-between;
        }

        .form-row label {
            font-weight: bold;
            margin-bottom: 8px;
            color: #333;
            width: 30%;
        }

        .form-row input,
        .form-row select {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 65%;
        }

        .form-buttons {
            text-align: center;
        }

        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        .suggestion-box {
            position: absolute;
            background-color: white;
            border: 1px solid #ccc;
            width: 65%;
            z-index: 10;
        }

        .suggestion-item {
            padding: 8px;
            cursor: pointer;
        }

        .suggestion-item:hover {
            background-color: #ddd;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table th, table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        table th {
            background-color: #f2f2f2;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .add-product-btn {
            background-color: #008CBA;
            margin-top: 10px;
        }

        .add-product-btn:hover {
            background-color: #007B8C;
        }
    </style>
</head>
<body>

<h1>Create Transfer Order</h1>

<div class="form-container">
    <form action="/ClothingManagement_war/transfer-order/create" method="post">

        <!-- Transfer Order ID -->
        <div class="form-row">
            <label for="toID">Transfer Order ID:</label>
            <input type="text" id="toID" name="toID" required>
        </div>

        <!-- Created Date -->
        <div class="form-row">
            <label for="createdDate">Created Date:</label>
            <input type="date" id="createdDate" name="createdDate" value="<%= java.time.LocalDate.now() %>" readonly>
        </div>

        <!-- Created By (Employee ID) -->
        <div class="form-row">
            <label for="createdBy">Created By (Employee ID):</label>
            <select name="createdBy" id="createdBy" required>
                <c:forEach var="employeeID" items="${employeeIds}">
                    <option value="${employeeID}">${employeeID}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Origin Bin -->
        <div class="form-row">
            <%--@declare id="originbinid"--%><label for="originBinID">Origin Bin ID:</label>
            <input type="text" name="originBinID" required>
        </div>

        <!-- Final Bin -->
        <div class="form-row">
            <%--@declare id="finalbinid"--%><label for="finalBinID">Final Bin ID:</label>
            <input type="text" name="finalBinID" required>
        </div>

        <!-- Error messages for Bin IDs -->
        <c:if test="${not empty errorOriginBin}">
            <p class="error-message">${errorOriginBin}</p>
        </c:if>
        <c:if test="${not empty errorFinalBin}">
            <p class="error-message">${errorFinalBin}</p>
        </c:if>

        <c:if test="${not empty errorBinSame}">
            <p class="error-message">${errorBinSame}</p>
        </c:if>

        <!-- Product Detail Search and Table -->
        <div class="form-row">
            <label for="productDetailSearch">Search Product Detail ID:</label>
            <input type="text" id="productDetailSearch" name="productDetailSearch" oninput="searchProductDetails()" autocomplete="off">
            <div id="suggestionBox" class="suggestion-box" style="display: none;"></div>
        </div>

        <!-- Product Detail ID, Product Name, Weight, and Quantity Table -->
        <table id="productDetailsTable">
            <thead>
            <tr>
                <th>#</th>
                <th>Product Detail ID</th>
                <th>Product Name</th>
                <th>Weight</th>
                <th>Quantity</th>
            </tr>
            </thead>
            <tbody id="productDetailsBody">
            <!-- Empty row for adding products dynamically -->
            <tr>
                <td>1</td>
                <td><input type="text" name="productDetailID[]" id="productDetailID1" required readonly></td>
                <td><span id="productName1"></span></td>
                <td><span id="productWeight1"></span></td>
                <td><input type="number" name="quantity[]" required></td>
            </tr>
            </tbody>
        </table>

        <div class="form-buttons">
            <button type="button" class="add-product-btn" onclick="addProductRow()">Add Another Product</button>
        </div>

        <div class="form-buttons">
            <button type="submit">Create Transfer Order</button>
        </div>

        <c:if test="${not empty errorGeneral}">
            <p class="error-message">${errorGeneral}</p>
        </c:if>

        <c:if test="${not empty successMessage}">
            <p style="color:green;">${successMessage}</p>
        </c:if>

    </form>
</div>

<script>
    let rowCount = 1;  // Start row number from 1

    // Function to search product details as user types
    function searchProductDetails() {
        const searchValue = document.getElementById("productDetailSearch").value.trim();

        if (searchValue.length > 0) {
            // Make an AJAX request to search product details
            fetch(`/ClothingManagement_war/transfer-order/searchProductDetail?query=${searchValue}`)
                .then(response => response.json())
                .then(data => {
                    const suggestionBox = document.getElementById("suggestionBox");
                    suggestionBox.innerHTML = "";  // Clear previous suggestions
                    if (data.success && data.suggestions.length > 0) {
                        // Show suggestion box with results
                        suggestionBox.style.display = "block";
                        data.suggestions.forEach(suggestion => {
                            const div = document.createElement("div");
                            div.classList.add("suggestion-item");
                            div.textContent = `${suggestion.productDetailID} - ${suggestion.productName} - ${suggestion.weight}`;
                            div.onclick = () => selectSuggestion(suggestion);
                            suggestionBox.appendChild(div);
                        });
                    } else {
                        suggestionBox.style.display = "none";
                    }
                })
                .catch(error => {
                    console.error('Error fetching product details:', error);
                    alert('An error occurred while fetching product details.');
                });
        } else {
            document.getElementById("suggestionBox").style.display = "none";
        }
    }

    // Function to select a suggestion and fill in the Product Detail ID, Name, and Weight
    function selectSuggestion(suggestion) {
        document.getElementById("productDetailSearch").value = suggestion.productDetailID;
        document.getElementById("productName1").innerText = suggestion.productName;
        document.getElementById("productWeight1").innerText = suggestion.weight;
        document.getElementById("suggestionBox").style.display = "none";  // Hide suggestions

        // Insert the product detail into the table
        document.getElementById("productDetailID1").value = suggestion.productDetailID;  // Set Product Detail ID
    }

    // Function to add another product row dynamically
    function addProductRow() {
        rowCount++;  // Increment row count

        var tableBody = document.getElementById("productDetailsBody");

        var newRow = document.createElement("tr");

        var cell1 = document.createElement("td");
        cell1.textContent = rowCount;  // Auto-increment the number

        var cell2 = document.createElement("td");
        var productDetailInput = document.createElement("input");
        productDetailInput.setAttribute("type", "text");
        productDetailInput.setAttribute("name", "productDetailID[]");
        productDetailInput.setAttribute("id", `productDetailID${rowCount}`);
        productDetailInput.setAttribute("readonly", true);  // Set to readonly, only allow editing quantity
        cell2.appendChild(productDetailInput);

        var cell3 = document.createElement("td");
        var productNameSpan = document.createElement("span");
        productNameSpan.setAttribute("id", `productName${rowCount}`);
        cell3.appendChild(productNameSpan);

        var cell4 = document.createElement("td");
        var productWeightSpan = document.createElement("span");
        productWeightSpan.setAttribute("id", `productWeight${rowCount}`);
        cell4.appendChild(productWeightSpan);

        var cell5 = document.createElement("td");
        var quantityInput = document.createElement("input");
        quantityInput.setAttribute("type", "number");
        quantityInput.setAttribute("name", "quantity[]");
        quantityInput.setAttribute("required", true);
        cell5.appendChild(quantityInput);

        newRow.appendChild(cell1);
        newRow.appendChild(cell2);
        newRow.appendChild(cell3);
        newRow.appendChild(cell4);
        newRow.appendChild(cell5);

        tableBody.appendChild(newRow);
    }
</script>

</body>
</html>
