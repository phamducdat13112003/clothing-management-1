<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Font -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">

    <!-- Fav Icon -->
    <link rel="icon" href="img/favicon.png">

    <!-- sherah Stylesheet -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome-all.min.css">
    <link rel="stylesheet" href="css/charts.min.css">
    <link rel="stylesheet" href="css/datatables.min.css">
    <link rel="stylesheet" href="css/jvector-map.css">
    <link rel="stylesheet" href="css/slickslider.min.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        h1 {
            text-align: center;
            color: #333;
        }

        .container {
            width: 100%;
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
            margin-top: 203px;
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

        .error-message {
            color: red;  /* Set the text color to red */
            font-size: 14px;  /* Optional: set the font size */
            font-weight: bold;  /* Optional: make the error message bold */
            margin-top: 10px;  /* Optional: add some spacing */
        }

    </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <div class="row align-items-center justify-content-between">
            <div class="col-6">
                <div class="sherah-breadcrumb mg-top-30">
                    <h2 class="sherah-breadcrumb__title">Create new transfer order</h2>
                    <ul class="sherah-breadcrumb__list">
                        <li><a href="a.jsp">Home</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div style="margin-top: 50px;">
            <form action="${pageContext.request.contextPath}/TOCreate" method="post">

            <!-- Transfer Order ID -->
                <div class="form-row">
                    <label for="toID">Transfer Order ID:</label>
                    <input type="text" id="toID" name="toID" value="${nextToID}" readonly>
                </div>

                <!-- Created Date -->
                <div class="form-row">
                    <label for="createdDate">Created Date:</label>
                    <input type="date" id="createdDate" name="createdDate" value="<%= java.time.LocalDate.now() %>" readonly>
                </div>

                <!-- Created By (Employee ID) -->
                <div class="form-row">
                    <label for="createdBy">Created By:</label>
                    <input type="text" id="createdByName" name="Name" value="${sessionScope.employeeName}" readonly>
                    <input type="hidden" id="createdBy" name="createdBy" value="${sessionScope.employeeId}">
                </div>

                <!-- Origin Section -->
                <div class="form-row">
                    <label for="originSectionID">Origin Section:</label>
                    <select name="originSectionID" id="originSectionID">
                        <option value="">Select Origin Section</option>
                        <c:forEach var="section" items="${sections}">
                            <option value="${section.sectionID}">${section.sectionName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Origin Bin -->
                <div class="form-row">
                    <label for="originBinID">Origin Bin:</label>
                    <select name="originBinID" id="originBinID" disabled>
                        <option value="">Select Origin Bin</option>
                    </select>
                </div>

                <!-- Final Section -->
                <div class="form-row">
                    <label for="finalSectionID">Final Section:</label>
                    <select name="finalSectionID" id="finalSectionID">
                        <option value="">Select Final Section</option>
                        <c:forEach var="section" items="${sections}">
                            <c:if test="${section.sectionName ne 'Storage Received'}">
                                <option value="${section.sectionID}">${section.sectionName}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <!-- Final Bin -->
                <div class="form-row">
                    <label for="finalBinID">Final Bin:</label>
                    <select name="finalBinID" id="finalBinID" disabled>
                        <option value="">Select Final Bin</option>
                    </select>
                </div>

                <!-- Product Detail Search and Table -->
                <div class="form-row">
                    <label for="productDetailSearch">Search Product Detail ID:</label>
                    <input type="text" id="productDetailSearch" name="productDetailSearch" autocomplete="off" disabled>
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

            </form>
        </div>

    </div>
</section>
<script>
    // Access the hidden input field with the employee ID
    var employeeID = document.getElementById('createdBy').value;
    var employeeName = document.getElementById('createdByName').value;
    console.log("employee Name: " + employeeName)

    // Log the employee ID to the console
    console.log("Employee ID: " + employeeID);
</script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Function to show alerts if errors exist
        function showErrorAlerts() {
            const errorMessages = [
                '${errorGeneral}',
                '${errorProduct}',
                '${errorToID}',
                '${errorBinSame}',
                '${errorQuantity1}',
                '${errorQuantity2}',
                '${errorDetail}',
                '${errorBin}',
                '${errorCapacity}',
                '${errorSection}'
            ];

            // Filter out empty error messages
            const validErrors = errorMessages.filter(msg => msg && msg.trim() !== '');

            // Show alerts if there are any errors
            if (validErrors.length > 0) {
                // Combine all non-empty error messages
                const combinedErrors = validErrors.join('\n\n');

                // Show a single alert with all errors
                window.alert('Errors occurred:\n\n' + combinedErrors);
            }
        }

        // Call the function to show alerts
        showErrorAlerts();
    });
</script>
<script>
    let rowCount = 1;  // Start row number from 1
    const contextPath = "${pageContext.request.contextPath}";

    document.addEventListener("DOMContentLoaded", function () {
        const originSectionSelect = document.getElementById("originSectionID");
        const finalSectionSelect = document.getElementById("finalSectionID");
        const originBinSelect = document.getElementById("originBinID");
        const finalBinSelect = document.getElementById("finalBinID");
        const searchInput = document.getElementById("productDetailSearch");
        const suggestionBox = document.getElementById("suggestionBox");

        // Add event listeners for section selection
        originSectionSelect.addEventListener("change", function() {
            console.log("Origin section selected:", this.value);
            console.log("Selected option text:", this.options[this.selectedIndex].text);
            console.log("All options:", Array.from(this.options).map(opt => ({ value: opt.value, text: opt.text })));
            loadBins(this.value, originBinSelect);

            // Disable product search until origin bin is selected
            searchInput.disabled = true;
            searchInput.value = "";

            // Reset origin bin selection
            originBinSelect.innerHTML = '<option value="">Select Origin Bin</option>';
            originBinSelect.disabled = this.value === "";
        });

        finalSectionSelect.addEventListener("change", function() {
            loadBins(this.value, finalBinSelect);

            // Reset final bin selection
            finalBinSelect.innerHTML = '<option value="">Select Final Bin</option>';
            finalBinSelect.disabled = this.value === "";
        });

        function loadBins(sectionID, binSelect) {
            console.log("loadBins called with sectionID:", sectionID, "Type:", typeof sectionID);

            if (!sectionID) {
                console.log("No sectionID provided, disabling bin select");
                binSelect.innerHTML = '<option value="">Select Bin</option>';
                binSelect.disabled = true;
                return;
            }

            // Enable the bin select
            binSelect.disabled = false;

            // Create URL object using the same approach as searchProductDetails
            const url = new URL(contextPath + "/getBinsBySection", window.location.origin);
            url.searchParams.append("sectionID", sectionID);

            console.log("Fetching bins from URL:", url.toString());

            // Fetch bins for the selected section
            fetch(url.toString())
                .then(response => {
                    console.log("Response status:", response.status);
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Full API response:", JSON.stringify(data));

                    // Clear current options
                    binSelect.innerHTML = '<option value="">Select Bin</option>';

                    if (data.success && data.bins && data.bins.length > 0) {
                        // Add new options
                        data.bins.forEach(bin => {
                            const option = document.createElement('option');
                            option.value = bin.id || bin.binID;
                            option.textContent = bin.name || bin.binName || bin.id || bin.binID;

                            // Disable the option if status is false
                            if (bin.status === false) {
                                option.disabled = true;
                                option.textContent += " (Inactive)";
                            }

                            binSelect.appendChild(option);
                        });
                    } else {
                        console.log("No bins found or API error:", data.message || "Unknown error");
                        binSelect.innerHTML = '<option value="">No bins available</option>';
                    }

                    // Update bin selections after loading new bins
                    updateBinSelections();
                })
                .catch(error => {
                    console.error("Error fetching bins:", error);
                    binSelect.innerHTML = '<option value="">Error loading bins</option>';
                });
        }

        // Add this function to disable matching bins in the other dropdown
        function updateBinSelections() {
            const originSectionID = originSectionSelect.value;
            const finalSectionID = finalSectionSelect.value;
            const originBinID = originBinSelect.value;
            const finalBinID = finalBinSelect.value;

            // disable product search input khi original bin chưa được select
            searchInput.disabled = originBinID === "";

            // Add an error message div after the search input if it doesn't exist
            let errorDiv = document.getElementById("searchError");
            if (!errorDiv) {
                const searchRow = searchInput.parentElement;
                errorDiv = document.createElement("div");
                errorDiv.className = "error-message";
                errorDiv.id = "searchError";
                errorDiv.style.display = "none";
                searchRow.appendChild(errorDiv);
            }

            if (originBinID === "" && searchInput.value.trim() !== "") {
                errorDiv.textContent = "You must choose an Origin Bin first.";
                errorDiv.style.display = "block";
                searchInput.value = "";
                suggestionBox.style.display = "none";
            }

            // disable option đã được select ở origin bin ở final bin
            if (originSectionID && finalSectionID) {
                if (originSectionID === finalSectionID) {
                    if (originBinID) {
                        Array.from(finalBinSelect.options).forEach(option => {
                            if (option.value === originBinID) {
                                option.disabled = true;
                            } else {
                                option.disabled = false;
                            }
                        });
                    }

                    // disable option đã select ở origin bin
                    if (finalBinID) {
                        Array.from(originBinSelect.options).forEach(option => {
                            if (option.value === finalBinID) {
                                option.disabled = true;
                            } else {
                                option.disabled = false;
                            }
                        });
                    }
                } else {
                    // selected option khác nhau, enable all bin selection
                    Array.from(finalBinSelect.options).forEach(option => {
                        option.disabled = false;
                    });

                    Array.from(originBinSelect.options).forEach(option => {
                        option.disabled = false;
                    });
                }
            }
        }

        // Modify the section change event listeners to also update bin selections
        originSectionSelect.addEventListener("change", function() {
            console.log("Origin section selected:", this.value);
            console.log("Selected option text:", this.options[this.selectedIndex].text);
            console.log("All options:", Array.from(this.options).map(opt => ({ value: opt.value, text: opt.text })));
            loadBins(this.value, originBinSelect);

            // disable search input khi chưa select origin bin
            searchInput.disabled = true;
            searchInput.value = "";

            // Reset origin bin selection
            originBinSelect.innerHTML = '<option value="">Select Origin Bin</option>';
            originBinSelect.disabled = this.value === "";

            // Update bin selections after loading bins
            setTimeout(updateBinSelections, 500); // Add a small delay to ensure bins are loaded
        });

        finalSectionSelect.addEventListener("change", function() {
            loadBins(this.value, finalBinSelect);

            // Reset final bin selection
            finalBinSelect.innerHTML = '<option value="">Select Final Bin</option>';
            finalBinSelect.disabled = this.value === "";

            // Update bin selections after loading bins
            setTimeout(updateBinSelections, 500); // Add a small delay to ensure bins are loaded
        });

        // Prevent searching if no origin bin is selected
        searchInput.addEventListener("focus", function() {
            const originBinID = originBinSelect.value;
            const errorDiv = document.getElementById("searchError");

            if (originBinID === "") {
                if (errorDiv) {
                    errorDiv.textContent = "You must choose an Origin Bin first.";
                    errorDiv.style.display = "block";
                }
                this.blur(); // Remove focus from the search input
            } else if (errorDiv) {
                errorDiv.style.display = "none";
            }
        });

        // Function to search product details
        function searchProductDetails() {
            const searchValue = searchInput.value.trim();
            const originBinID = originBinSelect.value;
            const errorDiv = document.getElementById("searchError");

            // Check if origin bin is selected
            if (originBinID === "") {
                if (errorDiv) {
                    errorDiv.textContent = "You must choose an Origin Bin first.";
                    errorDiv.style.display = "block";
                }
                suggestionBox.style.display = "none";
                return;
            } else if (errorDiv) {
                errorDiv.style.display = "none";
            }

            if (searchValue.length > 1) {
                // Call search API
                const url = new URL(contextPath + "/searchProductDetail", window.location.origin);
                url.searchParams.append("query", searchValue);
                url.searchParams.append("binID", originBinID);

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
                                console.log("Full suggestion object:", JSON.stringify(suggestion));

                                const div = document.createElement("div");
                                div.classList.add("suggestion-item");

                                // Extract raw values
                                const productDetailID = suggestion.productDetailID;
                                const productName = suggestion.productName;
                                const weight = suggestion.weight;
                                const quantity = suggestion.quantity;

                                console.log("Raw productDetailID:", productDetailID, typeof productDetailID);
                                console.log("Raw productName:", productName, typeof productName);
                                console.log("Raw weight:", weight, typeof weight);
                                console.log("Raw quantity:", quantity, typeof quantity);

                                // Create display values with explicit checking
                                let displayID = "No ID";
                                if (productDetailID !== undefined && productDetailID !== null && productDetailID !== "") {
                                    displayID = productDetailID;
                                }

                                let displayName = "No Name";
                                if (productName !== undefined && productName !== null && productName !== "") {
                                    displayName = productName;
                                }

                                let displayWeight = "No Weight";
                                if (weight !== undefined && weight !== null) {
                                    displayWeight = weight.toString() + " kg";
                                }

                                let displayQuantity = "No Quantity";
                                if (quantity !== undefined && quantity !== null) {
                                    displayQuantity = quantity.toString();
                                }

                                // Set content for search suggestion
                                div.textContent = displayID + " - " + displayName + " - " + displayWeight + " - Quantity: " + displayQuantity;

                                // If click on a search suggestion, call select suggestion function
                                div.onclick = () => selectSuggestion(suggestion);
                                suggestionBox.appendChild(div);
                            });
                        } else {
                            suggestionBox.innerHTML = "<div class='suggestion-item'>No products found in this bin</div>";
                            suggestionBox.style.display = "block";
                        }
                    })
                    .catch(error => {
                        console.error("Error fetching suggestions:", error);
                        suggestionBox.innerHTML = `<div class="error-message">Unable to fetch results. Please try again.</div>`;
                        suggestionBox.style.display = "block";
                    });
            } else {
                suggestionBox.style.display = "none";
            }
        }

        // Attach event listener for the search input
        searchInput.addEventListener("input", searchProductDetails);

        // Function to add row to table when selecting a product from search suggestion
        function selectSuggestion(suggestion) {
            const tableBody = document.getElementById("productDetailsBody");

            // Check for duplicate product detail id in table
            const existingProduct = Array.from(tableBody.getElementsByTagName("tr")).some(row => {
                const productDetailInput = row.querySelector('input[name="productDetailID[]"]');
                return productDetailInput && productDetailInput.value === suggestion.productDetailID;
            });

            // If product exists, show error
            if (existingProduct) {
                alert("This product is already added.");
                searchInput.value = "";
                return;
            }

            // If product doesn't exist, create new row in the table
            const newRow = document.createElement("tr");

            const cell1 = document.createElement("td");
            cell1.textContent = rowCount;

            const cell2 = document.createElement("td");
            const productDetailInput = document.createElement("input");
            productDetailInput.setAttribute("type", "text");
            productDetailInput.setAttribute("name", "productDetailID[]");
            productDetailInput.setAttribute("readonly", true);
            productDetailInput.value = suggestion.productDetailID;
            cell2.appendChild(productDetailInput);

            const cell3 = document.createElement("td");
            const productNameSpan = document.createElement("span");
            productNameSpan.setAttribute("id", `productName${rowCount}`);
            productNameSpan.textContent = suggestion.productName;
            cell3.appendChild(productNameSpan);

            const cell4 = document.createElement("td");
            const productWeightSpan = document.createElement("span");
            productWeightSpan.setAttribute("id", `productWeight${rowCount}`);
            productWeightSpan.textContent = suggestion.weight;
            cell4.appendChild(productWeightSpan);

            const cell5 = document.createElement("td");
            const quantityInput = document.createElement("input");
            quantityInput.setAttribute("type", "number");
            quantityInput.setAttribute("name", "quantity[]");
            quantityInput.setAttribute("value", 1);  // Set default value = 1
            quantityInput.setAttribute("min", 1);    // Set min value = 1
            quantityInput.setAttribute("required", true);

            // Store available quantity as a data attribute
            quantityInput.dataset.availableQuantity = suggestion.quantity || 0;

            // Get available quantity from search suggestion
            const displayQuantity = suggestion.quantity || 0;

            // Error message for quantity exceeding available quantity
            const errorSpan = document.createElement("span");
            errorSpan.className = "quantity-error";
            errorSpan.style.color = "red";
            errorSpan.style.display = "none";
            errorSpan.textContent = `Number exceeds available quantity! `;

            // Add input event listener to validate quantity
            quantityInput.addEventListener("input", function() {
                const inputValue = parseInt(this.value) || 0;
                const availableQty = parseInt(this.dataset.availableQuantity) || 0;

                if (inputValue > availableQty) {
                    errorSpan.style.display = "block";
                    this.setCustomValidity(`Number exceeds available quantity!`);
                } else {
                    errorSpan.style.display = "none";
                    this.setCustomValidity("");
                }
            });

            cell5.appendChild(quantityInput);
            cell5.appendChild(errorSpan);

            const cell6 = document.createElement("td");
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Remove";
            deleteButton.classList.add("delete-btn");
            deleteButton.type = "button"; // Prevent form submission

            // Add event listener to delete button
            deleteButton.onclick = function () {
                tableBody.removeChild(newRow);  // Remove row from table
                updateRowNumbers();  // Reset row numbers
            };

            cell6.appendChild(deleteButton);

            // Append the cells to the new row
            newRow.appendChild(cell1);
            newRow.appendChild(cell2);
            newRow.appendChild(cell3);
            newRow.appendChild(cell4);
            newRow.appendChild(cell5);
            newRow.appendChild(cell6);

            // Append the new row to the table body
            tableBody.appendChild(newRow);

            rowCount++;

            // Clear search input and hide suggestion box
            searchInput.value = "";
            suggestionBox.style.display = "none";
        }

        // Update row numbers if adding or removing products from table
        function updateRowNumbers() {
            const rows = document.querySelectorAll("#productDetailsBody tr");
            rows.forEach((row, index) => {
                row.querySelector('td').textContent = index + 1;
            });
            rowCount = rows.length + 1;
        }

        // Validation before form submission
        const form = document.querySelector('form');
        form.addEventListener('submit', function (event) {
            const productDetailsBody = document.getElementById("productDetailsBody");
            const rows = productDetailsBody.querySelectorAll('tr');
            const originSectionID = originSectionSelect.value;
            const finalSectionID = finalSectionSelect.value;
            const originBinID = originBinSelect.value;
            const finalBinID = finalBinSelect.value;

            // Check if origin section is selected
            if (originSectionID === "") {
                event.preventDefault();
                alert("Please select an Origin Section.");
                return;
            }

            // Check if final section is selected
            if (finalSectionID === "") {
                event.preventDefault();
                alert("Please select a Final Section.");
                return;
            }

            // Check if origin bin is selected
            if (originBinID === "") {
                event.preventDefault();
                alert("Please select an Origin Bin.");
                return;
            }

            // Check if final bin is selected
            if (finalBinID === "") {
                event.preventDefault();
                alert("Please select a Final Bin.");
                return;
            }

            // Check if origin and final bins are the same
            if (originBinID === finalBinID) {
                event.preventDefault();
                alert("Origin Bin and Final Bin cannot be the same.");
                return;
            }

            // If no products in table, don't allow creation
            if (rows.length === 0) {
                event.preventDefault(); // Prevent the form from submitting
                alert("Please add at least one product to the transfer order.");
                return;
            }

            // Validate quantities
            let hasInvalidQuantity = false;
            let errorMessage = "";

            rows.forEach(row => {
                const quantityInput = row.querySelector('input[name="quantity[]"]');
                const productName = row.querySelector('[id^="productName"]').textContent;
                const inputValue = parseInt(quantityInput.value) || 0;
                const availableQty = parseInt(quantityInput.dataset.availableQuantity) || 0;

                if (inputValue <= 0) {
                    hasInvalidQuantity = true;
                    errorMessage = "All product quantities must be greater than 0.";
                } else if (inputValue > availableQty) {
                    hasInvalidQuantity = true;
                    errorMessage = `Cannot transfer ${inputValue} units of ${productName}. Maximum available: ${availableQty}`;
                }
            });

            if (hasInvalidQuantity) {
                event.preventDefault();
                alert(errorMessage);
                return;
            }
        });
    });
</script>

<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/charts.js"></script>
<script src="js/final-countdown.min.js"></script>
<script src="js/fancy-box.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/circle-progress.min.js"></script>
<script src="js/jquery-jvectormap.js"></script>
<script src="js/jvector-map.js"></script>
<script src="js/main.js"></script>
</body>
</html>