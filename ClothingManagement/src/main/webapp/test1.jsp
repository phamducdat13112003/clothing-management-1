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

        #suggestionBox {
            border: 1px solid #ccc;
            max-height: 300px;
            overflow-y: auto;
            position: absolute;
            background-color: white;
            width: 100%;
            z-index: 1000;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
    </style>

</head>
<body>

<h1>Create Transfer Order</h1>

<div class="form-container">
    <form action="/ClothingManagement_war_exploded/transfer-order/create" method="post">

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
            <label for="createdBy">Created By:</label>
            <select name="createdBy" id="createdBy" required>
                <c:forEach var="employeeID" items="${employeeIds}">
                    <option value="${employeeID}">${employeeID}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Origin Bin -->
        <div class="form-row">
            <label for="originBinID">Origin Bin:</label>
            <select name="originBinID" id="originBinID">
                <option value="">Select Origin Bin</option>
                <c:forEach var="binId" items="${binIds}">
                    <option value="${binId}">${binId}</option>
                </c:forEach>
            </select>

        </div>

        <!-- Final Bin -->
        <div class="form-row">
            <label for="finalBinID">Final Bin:</label>
            <select name="finalBinID" id="finalBinID">
                <option value="">Select Final Bin</option>
                <c:forEach var="binId" items="${binIds}">
                    <option value="${binId}">${binId}</option>
                </c:forEach>
            </select>

        </div>

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
                <th>Weight(Gam)</th>
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
        <c:if test="${not empty errorToID}">
            <p class="error-message">${errorToID}</p>
        </c:if>
        <c:if test="${not empty errorBinSame}">
            <p class="error-message">${errorBinSame}</p>
        </c:if>
        <c:if test="${not empty errorQuantity1}">
            <p class="error-message">${errorQuantity1}</p>
        </c:if>
        <c:if test="${not empty errorQuantity2}">
            <p class="error-message">${errorQuantity2}</p>
        </c:if>
        <c:if test="${not empty errorDetail}">
            <p class="error-message">${errorDetail}</p>
        </c:if>
        <c:if test="${not empty errorBin}">
            <p class="error-message">${errorBin}</p>
        </c:if>
        <c:if test="${not empty errorWeight}">
            <p class="error-message">${errorWeight}</p>
        </c:if>

    </form>
</div>

<script>
    let rowCount = 1;  // Start row number from 1

    document.addEventListener("DOMContentLoaded", function () {
        const originBinSelect = document.getElementById("originBinID");
        const finalBinSelect = document.getElementById("finalBinID");

        // Function to update the available options in Final Bin dropdown
        function updateBinSelections() {
            const originBinID = originBinSelect.value;
            const finalBinID = finalBinSelect.value;

            // If Origin Bin is selected, disable that option in Final Bin
            Array.from(finalBinSelect.options).forEach(option => {
                if (originBinID === option.value && originBinID !== "") {
                    option.disabled = true;
                } else {
                    option.disabled = false;
                }
            });

            // If "Select Origin Bin" is selected, enable all options in Final Bin
            if (originBinID === "") {
                Array.from(finalBinSelect.options).forEach(option => {
                    option.disabled = false;
                });
            }
        }

        // Call the function initially to set the disabled states correctly when page loads
        updateBinSelections();

        // Add event listeners for changes in both bin selects
        originBinSelect.addEventListener("change", updateBinSelections);
        finalBinSelect.addEventListener("change", updateBinSelections);

        // Product search functionality
        const searchInput = document.getElementById("productDetailSearch");
        const suggestionBox = document.getElementById("suggestionBox");

        // Function to fetch and display product suggestions based on search input
        function searchProductDetails() {
            const searchValue = searchInput.value.trim();
            if (searchValue.length > 0) {
                const url = new URL("/ClothingManagement_war_exploded/transfer-order/searchProductDetail", window.location.origin);
                url.searchParams.append("query", searchValue);

                fetch(url.toString())
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(data => {
                        suggestionBox.innerHTML = "";

                        if (data.success && data.suggestions && data.suggestions.length > 0) {
                            suggestionBox.style.display = "block";
                            data.suggestions.forEach(suggestion => {
                                const div = document.createElement("div");
                                div.classList.add("suggestion-item");

                                const productDetailID = suggestion.productDetailID || "No ID";
                                const productName = suggestion.productName || "No name";
                                const weight = suggestion.weight || "No weight";

                                div.textContent = [productDetailID, productName, weight].filter(Boolean).join(' - ');
                                div.onclick = () => selectSuggestion(suggestion);

                                suggestionBox.appendChild(div);
                            });
                        } else {
                            suggestionBox.style.display = "none";
                        }
                    })
                    .catch(error => {
                        suggestionBox.innerHTML = `<div class="error-message">Unable to fetch results. Please try again.</div>`;
                        suggestionBox.style.display = "block";
                    });
            } else {
                suggestionBox.style.display = "none";
            }
        }

        // Attach event listener for the search input
        searchInput.addEventListener("input", searchProductDetails);

        // Select product suggestion
        function selectSuggestion(suggestion) {
            const tableBody = document.getElementById("productDetailsBody");

            // Add a new row with product details
            addProductRow();

            const lastRow = tableBody.lastElementChild;

            // Fill the product details in the last row
            lastRow.querySelector('input[name="productDetailID[]"]').value = suggestion.productDetailID;
            lastRow.querySelector(`#productName${rowCount}`).innerText = suggestion.productName;
            lastRow.querySelector(`#productWeight${rowCount}`).innerText = suggestion.weight;

            // Hide the suggestion box
            suggestionBox.style.display = "none";
        }

        function addProductRow() {
            const tableBody = document.getElementById("productDetailsBody");

            // Get the product ID or product name you want to check for duplicates
            const productId = `productDetailID${rowCount}`;  // Example: generate product ID for comparison

            // Check if this product already exists in the table
            const existingProduct = Array.from(tableBody.getElementsByTagName("tr")).some(row => {
                const productDetailInput = row.querySelector('input[name="productDetailID[]"]');
                return productDetailInput && productDetailInput.value === productId;  // Check product ID
            });

            // If the product is already in the table, don't add a new row
            if (existingProduct) {
                alert("This product is already added.");
                return;
            }

            const newRow = document.createElement("tr");

            const cell1 = document.createElement("td");
            cell1.textContent = rowCount;

            const cell2 = document.createElement("td");
            const productDetailInput = document.createElement("input");
            productDetailInput.setAttribute("type", "text");
            productDetailInput.setAttribute("name", "productDetailID[]");
            productDetailInput.setAttribute("readonly", true);
            cell2.appendChild(productDetailInput);

            const cell3 = document.createElement("td");
            const productNameSpan = document.createElement("span");
            productNameSpan.setAttribute("id", `productName${rowCount}`);
            cell3.appendChild(productNameSpan);

            const cell4 = document.createElement("td");
            const productWeightSpan = document.createElement("span");
            productWeightSpan.setAttribute("id", `productWeight${rowCount}`);
            cell4.appendChild(productWeightSpan);



            const cell5 = document.createElement("td");
            const quantityInput = document.createElement("input");
            quantityInput.setAttribute("type", "number");
            quantityInput.setAttribute("name", "quantity[]");
            quantityInput.setAttribute("value", 0);  // Set initial quantity to 0
            quantityInput.setAttribute("required", true);
            cell5.appendChild(quantityInput);

            const cell6 = document.createElement("td");
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Remove";
            deleteButton.classList.add("delete-btn");

            // Add event listener to delete button
            deleteButton.onclick = function () {
                tableBody.removeChild(newRow);  // Remove the row from the table
                updateRowNumbers();  // Reassign row numbers after deletion
            };

            cell6.appendChild(deleteButton);

            // Append the cells to the new row
            newRow.appendChild(cell1);
            newRow.appendChild(cell2);
            newRow.appendChild(cell3);
            newRow.appendChild(cell4);
            newRow.appendChild(cell5);
            newRow.appendChild(cell6);  // Append the delete button column

            // Append the new row to the table body
            tableBody.appendChild(newRow);

            rowCount++;
        }


        // Function to update row numbers after adding/removing a product
        function updateRowNumbers() {
            const rows = document.querySelectorAll("#productDetailsBody tr");
            rows.forEach((row, index) => {
                row.querySelector('td').textContent = index + 1;  // Update row number starting from 1
            });
            rowCount = rows.length + 1;  // Update rowCount to next available row number
        }



        // Validation before form submission
        const form = document.querySelector('form');
        form.addEventListener('submit', function (event) {
            const productDetailsBody = document.getElementById("productDetailsBody");
            const rows = productDetailsBody.querySelectorAll('tr');

            // If there are no rows (products) in the table, prevent form submission
            if (rows.length === 0) {
                event.preventDefault(); // Prevent the form from submitting
                alert("Please add at least one product to the transfer order.");
            }
        });
    });


</script>

</body>
</html>
