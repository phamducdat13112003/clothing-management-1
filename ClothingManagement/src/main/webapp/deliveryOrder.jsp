
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>Delivery Order</title>
  <!-- Meta Tags -->
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="keywords" content="Site keywords here">
  <meta name="description" content="#">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <!-- Font -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">

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
    .container {
      width: 60%;
      margin: auto;
      padding: 20px;
    }

    /* Đảm bảo form và bảng nằm trên dưới */
    .content {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 20px;
    }

    /* Form styling */
    .form-container {
      width: 60%;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;
    }

    .form-container input {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    /* Table container */
    .table-container {
      width: 100%;
      overflow-x: auto; /* Cho phép cuộn ngang nếu bảng quá lớn */
      display: flex;
      justify-content: center;
    }

    /* Table styling */
    table {
      width: 80%;
      border-collapse: collapse;
      margin-top: 20px;
      min-width: 600px; /* Đảm bảo bảng không quá nhỏ */
    }

    th, td {
      padding: 10px;
      text-align: center;
      border: 1px solid #ddd;
    }

    th {
      background-color: #f4f4f4;
    }

    tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    /* Buttons */
    button {
      padding: 10px 20px;
      margin: 10px;
      border: none;
      cursor: pointer;
      background-color: #007bff;
      color: white;
      border-radius: 5px;
    }

    button:hover {
      background-color: #0056b3;
    }




  </style>

</head>

  <body id="sherah-dark-light">

  <jsp:include page="include/sidebar.jsp"></jsp:include>
  <jsp:include page="include/header.jsp"></jsp:include>


  <div class="container">
    <h1>Create Delivery Order</h1>

    <c:if test="${not empty error}">
      <p style="color:red;">${error}</p>
    </c:if>

    <c:if test="${not empty poDetails}">
      <form action="CreatedDOServlet" method="get">
        <table>
          <tr>
<%--            <th>Remove</th>--%>
            <th>Product Detail ID</th>
            <th>Product Name</th>
            <th>Gender</th>
            <th>Seasons</th>
            <th>Material</th>
            <th>Weight</th>
            <th>Color</th>
            <th>Size</th>
<%--            <th>Remaining Quantity</th>--%>
            <th>Order Quantity</th>
            <th>Price</th>
            <th>Total Price</th>
          </tr>

          <c:forEach var="detail" items="${poDetails}">
            <c:set var="productDetailInfo" value="${productDetails.stream().filter(p -> p.ProductDetailID == detail.productDetailID).findFirst().orElse(null)}" />
<%--            <c:set var="remainingQuantity" value="${remainingQuantities[detail.productDetailID]}" />--%>

            <tr>
<%--              <td><input type="checkbox" class="remove-checkbox"></td>--%>
              <td>
                <input type="text" name="productDetailID_${detail.productDetailID}" value="${detail.productDetailID}" readonly>
              </td>
              <td>${productDetailInfo.ProductName}</td>
              <td>${productDetailInfo.Gender}</td>
              <td>${productDetailInfo.Seasons}</td>
              <td>${productDetailInfo.Material}</td>
              <td>${productDetailInfo.Weight}</td>
              <td>${productDetailInfo.Color}</td>
              <td>${productDetailInfo.Size}</td>
<%--              <td>${remainingQuantity}</td> <!-- Hiển thị số lượng tồn kho -->--%>
              <td>
                <input type="number" name="quantity_${detail.productDetailID}"
                       value="${detail.quantity}" min="1" max="${remainingQuantity}"
                       class="quantity-input" data-price="${detail.price}" readonly>
              </td>
              <td>
                <input type="text" name="price_${detail.productDetailID}" value="${detail.price}" readonly>
              </td>
              <td>
                <input type="text" name="totalPrice_${detail.productDetailID}" value="${detail.totalPrice}" readonly class="total-price">
              </td>
            </tr>
          </c:forEach>
        </table>

        <c:forEach var="poID" items="${poIDList}">
          <input type="hidden" name="poID" value="${poID}">
        </c:forEach>

        <label for="plannedShippingDate">Planned Shipping Date:</label>
        <input type="date" id="plannedShippingDate" name="plannedShippingDate" required>

<%--        <label for="createBy">Created By:</label>--%>
        <input type="hidden" id="createBy" name="createBy"
               value="<%= session.getAttribute("account_id") %>" readonly>

        <button type="submit">Submit</button>
<%--        <button type="button" id="removeSelected">Remove Selected</button>--%>
        <button type="reset">Reset</button>
      </form>
    </c:if>

    <script>
      document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".quantity-input").forEach(input => {
          input.addEventListener("input", function () {
            let min = 1;
            let max = parseInt(this.getAttribute("max"), 10);
            let value = parseInt(this.value, 10);

            if (isNaN(value) || value < min) {
              alert("Số lượng không thể nhỏ hơn 1.");
              this.value = min;
            } else if (value > max) {
              alert("Số lượng vượt quá số lượng tồn kho.");
              this.value = max;
            }

            let price = parseFloat(this.dataset.price) || 0;
            let totalPriceField = this.closest("tr").querySelector(".total-price");
            totalPriceField.value = (value * price).toFixed(2);
          });
        });
        document.querySelectorAll("tr").forEach(row => {
          let remainingQuantityCell = row.querySelector("td:nth-child(10)"); // Cột Remaining Quantity
          if (remainingQuantityCell && parseInt(remainingQuantityCell.textContent, 10) === 0) {
            row.remove();
          }
        });
        document.getElementById("removeSelected").addEventListener("click", function () {
          document.querySelectorAll(".remove-checkbox:checked").forEach(function (checkbox) {
            checkbox.closest("tr").remove();
          });
        });
      });
    </script>
  </div>
  <script src="js/jquery.min.js"></script>
  <script src="js/jquery-migrate.js"></script>
  <script src="js/jquery-ui.min.js"></script>
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/charts.js"></script>
  <script src="js/datatables.min.js"></script>
  <script src="js/circle-progress.min.js"></script>
  <script src="js/jquery-jvectormap.js"></script>
  <script src="js/jvector-map.js"></script>
  <script src="js/main.js"></script>

  </body>
</html>