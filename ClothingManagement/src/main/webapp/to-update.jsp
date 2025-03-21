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
      margin-top: 20px;
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
      color: red;
      font-size: 14px;
      font-weight: bold;
      margin-top: 10px;
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
          <h2 class="sherah-breadcrumb__title">Update Transfer Order</h2>
          <ul class="sherah-breadcrumb__list">
            <li><a href="homepage.jsp">Home</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div style="margin-top: 50px;">
      <form action="${pageContext.request.contextPath}/transfer-order/update" method="post">

        <!-- Transfer Order ID -->
        <div class="form-row">
          <label for="toID">Transfer Order ID:</label>
          <input type="text" id="toID" name="toID" value="${transferOrder.toID}" readonly>
        </div>

        <!-- Created Date -->
        <div class="form-row">
          <label for="createdDate">Created Date:</label>
          <input type="date" id="createdDate" name="createdDate" value="${transferOrder.createdDate}" required>
        </div>

        <!-- Created By (Employee ID) -->
        <div class="form-row">
          <label for="createdBy">Created By:</label>
          <select name="createdBy" id="createdBy" required>
            <c:forEach var="employeeID" items="${employeeIds}">
              <option value="${employeeID}" ${employeeID == transferOrder.createdBy ? 'selected' : ''}>${employeeID}</option>
            </c:forEach>
          </select>
        </div>

        <!-- Status -->
        <div class="form-row">
          <label for="status">Status:</label>
          <select name="status" id="status" required>
            <option value="Pending" ${'Pending' == transferOrder.status ? 'selected' : ''}>Pending</option>
            <option value="Done" ${'Done' == transferOrder.status ? 'selected' : ''}>Done</option>
            <option value="Cancelled" ${'Cancelled' == transferOrder.status ? 'selected' : ''}>Cancelled</option>
          </select>
        </div>

        <!-- Origin Bin -->
        <div class="form-row">
          <label for="originBinID">Origin Bin ID:</label>
          <input type="text" id="originBinID" name="originBinID" value="${toDetails[0].originBinID}" required>
        </div>

        <!-- Final Bin -->
        <div class="form-row">
          <label for="finalBinID">Final Bin ID:</label>
          <input type="text" id="finalBinID" name="finalBinID" value="${toDetails[0].finalBinID}" required>
        </div>

        <!-- Search Product Detail -->
        <div class="form-row">
          <label for="productDetailSearch">Search Product Detail ID:</label>
          <input type="text" id="productDetailSearch" name="productDetailSearch" autocomplete="off">
          <div id="suggestionBox" class="suggestion-box" style="display: none;"></div>
        </div>

        <!-- Product Details -->
        <h3>Product Details</h3>
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
          <c:forEach var="toDetail" items="${toDetails}" varStatus="status">
            <p>Debug - ProductID: ${toDetail.productDetailID}, Quantity: ${toDetail.quantity}</p>
            <tr>
              <td>${status.index + 1}</td>
              <td>
                <input type="text" name="productDetailID[]" value="${toDetail.productDetailID}" required readonly>
              </td>
              <td>
                <span id="productName${status.index + 1}">${toDetail.productName}</span>
              </td>
              <td>
                <span id="productWeight${status.index + 1}">${toDetail.weight}</span>
              </td>
              <td>
                <input type="number" name="quantity[]" value="${toDetail.quantity}" required min="1" data-available-quantity="${toDetail.availableQuantity}">
                <span class="quantity-error" style="color:red;display:none;">Maximum available quantity: ${toDetail.availableQuantity}</span>
              </td>
              <td>
                <button type="button" class="delete-btn" onclick="removeRow(this)">Remove</button>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>

        <div class="form-buttons">
          <button type="submit">Update Transfer Order</button>
        </div>

        <!-- Display error message if any -->
        <c:if test="${not empty errorMessage}">
          <p class="error-message">${errorMessage}</p>
        </c:if>

        <!-- Display success message if any -->
        <c:if test="${not empty successMessage}">
          <p style="color:green;">${successMessage}</p>
        </c:if>
      </form>
    </div>
  </div>
</section>
<script>
  // Access the hidden input field with the employee ID
  var employeeID = document.getElementById('createdBy').value;
  console.log("Employee ID: " + employeeID);
</script>
<script>
  let rowCount = ${toDetails.size() + 1};  // Start row number from after existing rows

  // Function to remove a row
  function removeRow(button) {
    var row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
    updateRowNumbers();
  }

  // Function to update row numbers after adding/removing a product
  function updateRowNumbers() {
    const rows = document.querySelectorAll("#productDetailsBody tr");
    rows.forEach((row, index) => {
      row.cells[0].textContent = index + 1;
    });
    rowCount = rows.length + 1;  // Update rowCount to next available row number
  }

  document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById("productDetailSearch");
    const suggestionBox = document.getElementById("suggestionBox");
    const originBinID = document.getElementById("originBinID").value;

    // Add an error message div after the search input
    const searchRow = searchInput.parentElement;
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-message";
    errorDiv.id = "searchError";
    errorDiv.style.display = "none";
    searchRow.appendChild(errorDiv);

    // Function to search product details
    function searchProductDetails() {
      const searchValue = searchInput.value.trim();

      // Check if origin bin is selected
      if (!originBinID) {
        errorDiv.textContent = "You must have an Origin Bin ID.";
        errorDiv.style.display = "block";
        suggestionBox.style.display = "none";
        return;
      } else {
        errorDiv.style.display = "none";
      }

      if (searchValue.length > 0) {
        // Replace with your actual search endpoint
        const url = new URL("/ClothingManagement_war/searchProductDetail", window.location.origin);
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
                      const div = document.createElement("div");
                      div.classList.add("suggestion-item");

                      // Extract values with null/undefined checks
                      const productDetailID = suggestion.productDetailID || "No ID";
                      const productName = suggestion.productName || "No Name";
                      const weight = suggestion.weight ? suggestion.weight + " kg" : "No Weight";
                      const quantity = suggestion.quantity !== undefined ? suggestion.quantity.toString() : "No Quantity";

                      // Set text content
                      div.textContent = productDetailID + " - " + productName + " - " + weight + " - Quantity: " + quantity;

                      div.onclick = function() {
                        selectSuggestion(suggestion);
                      };
                      suggestionBox.appendChild(div);
                    });
                  } else {
                    suggestionBox.innerHTML = "<div class='suggestion-item'>No products found in this bin</div>";
                    suggestionBox.style.display = "block";
                  }
                })
                .catch(error => {
                  console.error("Error fetching suggestions:", error);
                  suggestionBox.innerHTML = "<div class='suggestion-item'>Error fetching results</div>";
                  suggestionBox.style.display = "block";
                });
      } else {
        suggestionBox.style.display = "none";
      }
    }

    // Function to select a suggestion
    function selectSuggestion(suggestion) {
      const tableBody = document.getElementById("productDetailsBody");

      // Check if this product already exists in the table
      const existingProduct = Array.from(tableBody.rows).some(row => {
        const productDetailInput = row.querySelector('input[name="productDetailID[]"]');
        return productDetailInput && productDetailInput.value === suggestion.productDetailID;
      });
      // If the product is already in the table, don't add a new row
      if (existingProduct) {
        alert("This product is already added.");
        searchInput.value = "";
        suggestionBox.style.display = "none";
        return;
      }

      // Add the product to the table
      const newRow = document.createElement("tr");

      // Cell 1: Row number
      const cell1 = document.createElement("td");
      cell1.textContent = rowCount;

      // Cell 2: Product Detail ID
      const cell2 = document.createElement("td");
      const productDetailInput = document.createElement("input");
      productDetailInput.setAttribute("type", "text");
      productDetailInput.setAttribute("name", "productDetailID[]");
      productDetailInput.setAttribute("value", suggestion.productDetailID);
      productDetailInput.setAttribute("readonly", true);
      cell2.appendChild(productDetailInput);

      // Cell 3: Product Name
      const cell3 = document.createElement("td");
      const productNameSpan = document.createElement("span");
      productNameSpan.setAttribute("id", `productName${rowCount}`);
      productNameSpan.textContent = suggestion.productName;
      cell3.appendChild(productNameSpan);

      // Cell 4: Weight
      const cell4 = document.createElement("td");
      const productWeightSpan = document.createElement("span");
      productWeightSpan.setAttribute("id", `productWeight${rowCount}`);
      productWeightSpan.textContent = suggestion.weight;
      cell4.appendChild(productWeightSpan);

      // Cell 5: Quantity
      const cell5 = document.createElement("td");
      const quantityInput = document.createElement("input");
      quantityInput.setAttribute("type", "number");
      quantityInput.setAttribute("name", "quantity[]");
      quantityInput.setAttribute("value", "1");
      quantityInput.setAttribute("min", "1");
      quantityInput.setAttribute("required", true);

      // Store available quantity as a data attribute
      quantityInput.dataset.availableQuantity = suggestion.quantity || 0;

      // Add error message element
      const errorSpan = document.createElement("span");
      errorSpan.className = "quantity-error";
      errorSpan.style.color = "red";
      errorSpan.style.display = "none";
      errorSpan.textContent = `Maximum available quantity: ${suggestion.quantity || 0}`;

      // Add input event listener to validate quantity
      quantityInput.addEventListener("input", function() {
        const inputValue = parseInt(this.value) || 0;
        const availableQty = parseInt(this.dataset.availableQuantity) || 0;

        if (inputValue > availableQty) {
          errorSpan.style.display = "block";
          this.setCustomValidity(`Maximum available quantity is ${availableQty}`);
        } else {
          errorSpan.style.display = "none";
          this.setCustomValidity("");
        }
      });

      cell5.appendChild(quantityInput);
      cell5.appendChild(errorSpan);

      // Cell 6: Action (Remove button)
      const cell6 = document.createElement("td");
      const removeButton = document.createElement("button");
      removeButton.setAttribute("type", "button");
      removeButton.setAttribute("class", "delete-btn");
      removeButton.textContent = "Remove";
      removeButton.onclick = function() {
        removeRow(this);
      };
      cell6.appendChild(removeButton);

      // Append cells to row
      newRow.appendChild(cell1);
      newRow.appendChild(cell2);
      newRow.appendChild(cell3);
      newRow.appendChild(cell4);
      newRow.appendChild(cell5);
      newRow.appendChild(cell6);

      // Add the row to the table
      tableBody.appendChild(newRow);

      // Increment row count
      rowCount++;

      // Clear search input and hide suggestion box
      searchInput.value = "";
      suggestionBox.style.display = "none";
    }

    // Attach event listener for the search input
    searchInput.addEventListener("input", searchProductDetails);

    // Close suggestion box when clicking outside
    document.addEventListener("click", function(event) {
      if (!searchInput.contains(event.target) && !suggestionBox.contains(event.target)) {
        suggestionBox.style.display = "none";
      }
    });

    // Set up quantity input validation for existing rows
    const existingQuantityInputs = document.querySelectorAll('input[name="quantity[]"]');
    existingQuantityInputs.forEach(input => {
      input.addEventListener("input", function() {
        const errorSpan = this.nextElementSibling;
        const inputValue = parseInt(this.value) || 0;
        const availableQty = parseInt(this.dataset.availableQuantity) || 0;

        if (inputValue > availableQty) {
          errorSpan.style.display = "block";
          this.setCustomValidity(`Maximum available quantity is ${availableQty}`);
        } else {
          errorSpan.style.display = "none";
          this.setCustomValidity("");
        }
      });
    });

    // Validation before form submission
    const form = document.querySelector('form');
    form.addEventListener('submit', function(event) {
      const productDetailsBody = document.getElementById("productDetailsBody");
      const rows = productDetailsBody.querySelectorAll('tr');

      // If there are no rows (products) in the table, prevent form submission
      if (rows.length === 0) {
        event.preventDefault();
        alert("Please add at least one product to the transfer order.");
        return;
      }

      // Validate all quantity inputs
      let hasInvalidQuantity = false;
      rows.forEach(row => {
        const quantityInput = row.querySelector('input[name="quantity[]"]');
        const productDetailInput = row.querySelector('input[name="productDetailID[]"]');
        const inputValue = parseInt(quantityInput.value) || 0;

        if (inputValue <= 0) {
          hasInvalidQuantity = true;
          alert(`Please enter a valid quantity for product ${productDetailInput.value}.`);
          event.preventDefault();
          return;
        }

        // Check against available quantity if data attribute is present
        if (quantityInput.dataset.availableQuantity) {
          const availableQty = parseInt(quantityInput.dataset.availableQuantity);
          if (inputValue > availableQty) {
            hasInvalidQuantity = true;
            alert(`Cannot transfer ${inputValue} units of product ${productDetailInput.value}. Maximum available: ${availableQty}`);
            event.preventDefault();
            return;
          }
        }
      });
    });
  });
</script>