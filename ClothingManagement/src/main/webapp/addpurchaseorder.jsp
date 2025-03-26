<%--<jsp:useBean id="product" scope="request" type=""/>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Add Purchase Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
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
        .form-container {
            max-width: 80%;
            margin-left: 20%;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .order-container {
            display: flex;
            gap: 20px;
            align-items: flex-start;
        }

        .left-section {
            flex: 7;
        }

        .right-section {
            flex: 3;
        }

        .box {
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 15px;
            background: #fff;
        }

        /**/
        .product-list {
            margin-top: 15px;
        }

        .product-item {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px;
            cursor: pointer;
            border-bottom: 1px solid #eee;
        }

        .product-item img {
            width: 40px;
            height: 40px;
            object-fit: cover;
            border-radius: 5px;
        }

        .product-item:hover {
            background: #f5f5f5;
        }

        .search-container {
            position: relative;
        }

        .search-container i {
            position: absolute;
            left: 10px;
            top: 50%;
            transform: translateY(-50%);
            color: #aaa;
        }

        .search-container input {
            padding-left: 30px;
        }

        .required {
            color: red;
            font-weight: bold;
        }

        #productSelect {
            max-height: 200px;
            overflow-y: auto;
        }

        .dropdown-container {
            position: absolute;
            width: 100%;
            max-height: 300px;
            overflow-y: auto;
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            display: none;
            z-index: 1000;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }


        #productSelect {
            width: 100%;
            border: none;
            box-shadow: none;
        }
    </style>
</head>
<body id="sherah-dark-light">
<div class="sherah-body-area">
    <jsp:include page="include/sidebar.jsp"></jsp:include>
    <jsp:include page="include/header.jsp"></jsp:include>
    <div class="container mt-4">
        <div class="form-container">
            <h3 class="mb-3 text-center">Create Purchaseorder</h3>
            <form action="addpurchaseorder" method="get">
                <div class="order-container">
                    <div class="left-section">
                        <div class="box mb-3">
                            <div class="form-group">
                                <label for="searchProduct">Product<span class="required">*</span></label>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert" style="color: red">${errorMessage}</div>
                                    <% session.removeAttribute("errorMessage"); %> <!-- Xóa sau khi hiển thị -->
                                </c:if>
                                <div class="search-container">
                                    <i class="bi bi-search"></i>
                                    <input type="text" id="searchProduct" class="form-control"
                                           placeholder="Tìm theo tên" required>
                                    <div class="dropdown-container">
                                        <div id="productSelect">
                                            <c:forEach var="product" items="${productList}">
                                                <div class="product-item"
                                                     data-id="${product.ProductID}"
                                                     data-name="${product.ProductName}"
                                                     data-productdetailid="${product.ProductDetailID}"
                                                     data-price="${not empty product.Price ? product.Price : 'N/A'}"
                                                     data-size="${not empty product.Size ? product.Size : 'N/A'}"
                                                     data-color="${not empty product.Color ? product.Color : 'N/A'}"
                                                     data-image="${product.ProductImage}"
                                                     data-supplier="${product.SupplierID}">
                                                    <img src="${product.ProductImage}" alt="Product Image">
                                                    <div>
                                                        <input hidden="hidden" value="${product.SupplierID}">
                                                        <strong>${product.ProductName}</strong>
                                                        <p>Price: ${product.Price} | Size: ${product.Size} |
                                                            Color: ${product.Color} | Quantity: ${product.Quantity} |
                                                            SupplierID: ${product.SupplierID}</p>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>

                                    </div>
                                </div>

                            </div>

                            <div class="product-list">

                            </div>
                            <div class="box">
                                <h5>Total Amount PO</h5>
                                <input type="hidden" name="totalAmountPO" id="hiddenTotalAmountPO">
                                <p>Total: <span id="totalAmountPO">0</span>VNĐ</p>
                            </div>

                        </div>

                    </div>
                    <div class="right-section">
                        <div class="box mb-3">
                            <label class="form-label">Suppliers<span class="required">*</span></label>
                            <c:if test="${not empty errorMessageSupplier}">
                                <div class="alert" style="color: red">${errorMessageSupplier}</div>
                                <% session.removeAttribute("errorMessageSupplier"); %> <!-- Xóa sau khi hiển thị -->
                            </c:if>
                            <select class="form-control" id="supplierId" name="supplierid" required
                                    onchange="updateSupplierInfo()">
                                <option value="">Select Supplier</option>
                                <c:forEach items="${suppliers}" var="supplier">
                                    <option value="${supplier.supplierId}"
                                            data-name="${supplier.supplierName}"
                                            data-email="${supplier.email}"
                                            data-phone="${supplier.phone}"
                                            data-address="${supplier.address}">
                                            ${supplier.supplierId} | ${supplier.supplierName}
                                    </option>
                                </c:forEach>
                            </select>
                            <!-- Bảng hiển thị thông tin nhà cung cấp -->
                            <table class="table table-bordered mt-3">
                                <tr>
                                    <th>Email</th>
                                    <td id="supEmail">-</td>
                                </tr>
                                <tr>
                                    <th>Phone</th>
                                    <td id="supPhone">-</td>
                                </tr>
                                <tr>
                                    <th>Address</th>
                                    <td id="supAddress">-</td>
                                </tr>
                            </table>
                        </div>

                        <div class="box">
                            <h5>Information Other</h5>
                            <input type="text" class="form-control" name="employeeid" hidden=""
                                   value="${employee.employeeID}" readonly>
                            <label class="form-label"> Create By </label>
                            <input type="text" class="form-control" value="${employee.employeeName}" readonly>
                            <label class="form-label"> Email Actor </label>
                            <input type="text" class="form-control" value="${employee.email}" readonly>
                            <label class="form-label"> Phone Actor </label>
                            <input type="text" class="form-control" value="${employee.phone}" readonly>
                            <label class="form-label"> Address Actor </label>
                            <input type="text" class="form-control" value="${employee.address}" readonly>
                            <hr>
                            <label class="form-label mt-2">Create Date</label>
                            <input type="date" id="date" name="createddate"
                                   value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                                   readonly>

                            <%--                            <label class="form-label mt-2">Mã đơn nhập</label>--%>
                            <%--                            <input type="text" class="form-control">--%>
                            <%--                            <label class="form-label mt-2">Tham chiếu</label>--%>
                            <%--                            <input type="text" class="form-control">--%>
                        </div>
                    </div>
                </div>
                <div class="text-center mt-3">
                    <button type="submit" class="btn btn-success">Create Po</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function updateSupplierInfo() {
        var select = document.getElementById("supplierId");
        var emailInput = document.getElementById("email");
        var selectedOption = select.options[select.selectedIndex];

        // document.getElementById("supName").textContent = selectedOption.getAttribute("data-name") || "-";
        document.getElementById("supEmail").textContent = selectedOption.getAttribute("data-email") || "-";
        document.getElementById("supPhone").textContent = selectedOption.getAttribute("data-phone") || "-";
        document.getElementById("supAddress").textContent = selectedOption.getAttribute("data-address") || "-";

        emailInput.value = selectedOption.getAttribute("data-email") || "";
    }
</script>
//
<script>
    document.getElementById("searchProduct").addEventListener("focus", function () {
        document.querySelector(".dropdown-container").style.display = "block";
    });

    document.getElementById("searchProduct").addEventListener("input", function () {
        let searchValue = this.value.toLowerCase();
        let products = document.querySelectorAll(".product-item");

        products.forEach(item => {
            let name = item.getAttribute("data-name").toLowerCase();
            item.style.display = name.includes(searchValue) ? "flex" : "none";
        });
    });

    document.addEventListener("click", function (event) {
        let searchBox = document.querySelector(".search-container");
        if (!searchBox.contains(event.target)) {
            document.querySelector(".dropdown-container").style.display = "none";
        }
    });

    // Khi người dùng chọn sản phẩm
    document.querySelectorAll(".product-item").forEach(item => {
        item.addEventListener("click", function () {
            document.getElementById("searchProduct").value = this.getAttribute("data-name");
            document.querySelector(".dropdown-container").style.display = "none";
        });
    });
    console.log(document.querySelectorAll(".product-item")); // Xem có lấy được danh sách sản phẩm không


</script>

<script>
    let selectedProducts = [];

    document.querySelectorAll(".product-item").forEach(item => {
        item.addEventListener("click", function () {
            let productId = this.getAttribute("data-productdetailid");
            let productName = this.getAttribute("data-name");
            let productImage = this.getAttribute("data-image");
            let productColor = this.getAttribute("data-color");
            let productSize = this.getAttribute("data-size");
            let productPrice = this.getAttribute("data-price");
            let productSupplier = this.getAttribute("data-supplier");
            console.log(productId)
            console.log(productName)
            console.log(productColor)
            console.log(productPrice)
            console.log(productSize)
            console.log(productImage)

            // Tạo PoDetailID dựa trên thời gian hiện tại
            let now = new Date();
            let poDetailId = "POD" + now.getFullYear() +
                ("0" + (now.getMonth() + 1)).slice(-2) +
                ("0" + now.getDate()).slice(-2) +
                ("0" + now.getHours()).slice(-2) +
                ("0" + now.getMinutes()).slice(-2) +
                ("0" + now.getSeconds()).slice(-2);

            // Kiểm tra nếu sản phẩm đã được chọn trước đó
            if (selectedProducts.some(p => p.productId === productId)) {
                alert("Sản phẩm này đã được thêm!");
                return;
            }

            //tạo đối tượng lưu thông tin
            let product = {
                poDetailId: poDetailId,
                productId: productId,
                productName: productName,
                productImage: productImage,
                productColor: productColor,
                productSize: productSize,
                productPrice: productPrice,
                productSupplier: productSupplier
            }

            // Thêm sản phẩm vào danh sách
            selectedProducts.push(product);


            // Render danh sách sản phẩm
            renderSelectedProducts();
        });
    });

    function renderSelectedProducts() {
        let productList = document.querySelector(".product-list");
        productList.innerHTML = ""; // Xóa danh sách cũ


        selectedProducts.forEach(product => {
            let productHtml = `
                                <div class="selected-product d-flex align-items-center border p-2 mb-2 rounded shadow-sm bg-light">
                                <input type="text" name="productdetailid[]" value="` + product.productId + `" hidden>
                                <input type="text" name="podetailid[]" value="` + product.poDetailId + `" hidden>
                                <input type="text" name="supplierid[]" value="` + product.productSupplier + `" hidden>
                                <img src="` + product.productImage + `" width="50" height="50" class="me-3 rounded">

                                <div class="flex-grow-1">
                                <strong>` + product.productName + `</strong>
                                <p class="text-muted small mb-1">Color: ` + product.productColor + ` | Size: ` + product.productSize + ` | Supplier: ` + product.productSupplier + `</p>
                                </div>

                                <div class="d-flex align-items-center">
                                <input type="number" name="priceinput[]" class="form-control text-center me-2"
                                placeholder="Price" value="` + product.productPrice + `" style="width: 100px;">

                                <input type="number" name="quantity[]" class="form-control text-center me-2"
                                placeholder="Qty" value="1" min="1" max="1000" style="width: 80px;">

                                <input type="text" name="totalpricepod[]" class="form-control text-center"
                                placeholder="Total" value="` + product.productPrice + `" readonly style="width: 120px;">

                                <button type="button" class="btn btn-outline-danger btn-sm ms-3"
                                onclick="removeProduct('` + product.poDetailId + `')">
                                <i class="bi bi-trash"></i>
                                </button>
                                </div>
                                </div>
                                `;
            productList.insertAdjacentHTML('beforeend', productHtml);
        });
        // Cập nhật tổng tiền ngay sau khi thêm sản phẩm
        updateTotal();
        // Cập nhật tổng tiền khi thay đổi số lượng hoặc giá
        document.querySelectorAll('input[name="quantity[]"], input[name="priceinput[]"]').forEach(input => {
            input.addEventListener("input", updateTotal);
        });
    }

    function updateTotal() {
        let totalAmount = 0;
        let productContainers = document.querySelectorAll(".selected-product");

        productContainers.forEach(container => {
            let priceInput = container.querySelector('input[name="priceinput[]"]');
            let quantityInput = container.querySelector('input[name="quantity[]"]');
            let totalPriceInput = container.querySelector('input[name="totalpricepod[]"]');

            let price = parseFloat(priceInput.value) || 0;
            let quantity = parseInt(quantityInput.value) || 0;
            let totalPrice = price * quantity;
            totalPriceInput.value = totalPrice.toFixed(2);

            totalAmount += totalPrice;
        });

        document.getElementById("totalAmountPO").textContent = totalAmount.toLocaleString();
        document.getElementById("hiddenTotalAmountPO").value = totalAmount; // Cập nhật giá trị vào input ẩn
    }


    // Xóa sản phẩm khỏi danh sách
    // Xóa sản phẩm khỏi danh sách
    function removeProduct(poDetailId) {
        selectedProducts = selectedProducts.filter(p => p.poDetailId !== poDetailId);
        renderSelectedProducts();
        updateTotal(); // Cập nhật lại tổng tiền
    }


</script>
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
