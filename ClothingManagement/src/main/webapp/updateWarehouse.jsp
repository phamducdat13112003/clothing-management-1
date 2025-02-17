<%@ page import="org.example.clothingmanagement.entity.Warehouse" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <!-- Meta Tags -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="Site keywords here">
    <meta name="description" content="#">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Site Title -->
    <title>Sherah - HTML eCommerce Dashboard Template</title>

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
        <style>
            /* Basic reset */
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: Arial, sans-serif;
        }

        /* Center the form container */
        .form-container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* Full viewport height */
            background-color: #f4f7fc; /* Light background color */
        }

        /* Form styling */
        form {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px; /* Limit the width */
            box-sizing: border-box;
        }

        /* Form elements styling */
        label {
            display: block;
            font-size: 14px;
            margin-bottom: 6px;
            color: #333;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }

        input:focus {
            border-color: #007bff; /* Highlight input on focus */
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        /* Optional: Responsive design for mobile */
        @media (max-width: 600px) {
            .form-container {
                padding: 10px;
            }

            form {
                padding: 15px;
            }

            button {
                font-size: 14px;
            }
        }
    </style>

    </style>


</head>
<body id="sherah-dark-light">

<div class="form-container">
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
</div>


<!-- sherah Scripts -->
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
