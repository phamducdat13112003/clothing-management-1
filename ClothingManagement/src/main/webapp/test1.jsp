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
            margin-left: 311px;
            margin-top: 33px;
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

        /* Red color scheme for Action button and header */
        .delete-btn {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
        }

        .delete-btn:hover {
            background-color: #d32f2f;
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
                <th>Action</th>
            </tr>
            </thead>
            <tbody id="productDetailsBody">

            </tbody>
        </table>

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

    function searchProductDetails() {
        const searchValue = document.getElementById("productDetailSearch").value.trim();

        console.log("Searching for:", searchValue);  // Debugging line

        if (searchValue.length > 2) {  // Wait until user types more than 2 characters
            fetch(`/ClothingManagement_war/transfer-order/searchProductDetail?query=${searchValue}`)
                .then(response => response.json())
                .then(data => {
                    const suggestionBox = document.getElementById("suggestionBox");
                    suggestionBox.innerHTML = "";  // Clear previous suggestions

                    if (data.success && data.suggestions.length > 0) {
                        suggestionBox.style.display = "block";  // Show suggestion box

                        // Loop through suggestions and create divs for each suggestion
                        data.suggestions.forEach(suggestion => {
                            const div = document.createElement("div");
                            div.classList.add("suggestion-item");

// Explicitly convert each property to string and check for `null` or `undefined`
                            const productDetailID = (suggestion.productDetailID != null) ? String(suggestion.productDetailID) : "No ID";
                            const productName = (suggestion.productName != null) ? String(suggestion.productName) : "No name";
                            const weight = (suggestion.weight != null) ? String(suggestion.weight) : "No weight";  // Explicitly convert weight to string

                            console.log("Final suggestion values:", productDetailID, productName, weight);

// Use textContent to safely set the text
                            div.textContent = `${productDetailID} - ${productName} - ${weight}`;

// Debugging to ensure the text content is being set correctly
                            console.log("Final suggestion item text:", div.textContent);

                            // Add click event to select suggestion
                            div.onclick = () => selectSuggestion(suggestion);

                            // Append the suggestion to the box
                            suggestionBox.appendChild(div);
                        });


                    } else {
                        suggestionBox.style.display = "none";  // Hide if no results
                    }
                })
                .catch(error => {
                    console.error('Error fetching product details:', error);
                    alert('An error occurred while fetching product details.');
                });
        } else {
            document.getElementById("suggestionBox").style.display = "none";  // Hide suggestion box if input is empty
        }
    }




    function selectSuggestion(suggestion) {
        const tableBody = document.getElementById("productDetailsBody");

        // Always add a new row when a suggestion is clicked
        addProductRow();  // This ensures a new row is always added

        // Fill the last row with selected product details
        const lastRow = tableBody.lastElementChild;

        // Fill the product details in the last row
        lastRow.querySelector('input[name="productDetailID[]"]').value = suggestion.productDetailID;
        lastRow.querySelector(`#productName${rowCount}`).innerText = suggestion.productName;
        lastRow.querySelector(`#productWeight${rowCount}`).innerText = suggestion.weight;

        // Hide the suggestion box
        document.getElementById("suggestionBox").style.display = "none";

        // Debugging logs
        console.log("Selected Product:", suggestion.productDetailID, suggestion.productName, suggestion.weight);
    }


    function selectSuggestion(suggestion) {
        const tableBody = document.getElementById("productDetailsBody");

        // Always add a new row when a suggestion is clicked
        addProductRow();  // This ensures a new row is always added

        // Fill the last row with selected product details
        const lastRow = tableBody.lastElementChild;

        // Fill the product details in the last row
        lastRow.querySelector('input[name="productDetailID[]"]').value = suggestion.productDetailID;
        lastRow.querySelector(`#productName${rowCount}`).innerText = suggestion.productName;
        lastRow.querySelector(`#productWeight${rowCount}`).innerText = suggestion.weight;

        // Hide the suggestion box
        document.getElementById("suggestionBox").style.display = "none";

        // Debugging logs
        console.log("Selected Product:", suggestion.productDetailID, suggestion.productName, suggestion.weight);
    }






    function addProductRow() {
        // Increment row count after creating a row
        var tableBody = document.getElementById("productDetailsBody");

        // Create a new row element
        var newRow = document.createElement("tr");

        // Create cell for # (row number)
        var cell1 = document.createElement("td");
        cell1.textContent = rowCount;  // Set the row number

        // Create the other cells (Product Detail ID, Product Name, Weight, Quantity)
        var cell2 = document.createElement("td");
        var productDetailInput = document.createElement("input");
        productDetailInput.setAttribute("type", "text");
        productDetailInput.setAttribute("name", "productDetailID[]");
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

        // Create a cell for the delete button
        var cell6 = document.createElement("td");
        var deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.classList.add("delete-btn"); // Add a class for styling

        // Add event listener to delete button
        deleteButton.onclick = function() {
            tableBody.removeChild(newRow);  // Remove the row from the table
        };

        cell6.appendChild(deleteButton);

        // Append the cells to the new row
        newRow.appendChild(cell1);
        newRow.appendChild(cell2);
        newRow.appendChild(cell3);
        newRow.appendChild(cell4);
        newRow.appendChild(cell5);
        newRow.appendChild(cell6); // Append the delete button column

        // Append the new row to the table body
        tableBody.appendChild(newRow);

        // Increment row count after the row is added
        rowCount++;  // Now increment after adding the row
    }



</script>

</body>
</html>
