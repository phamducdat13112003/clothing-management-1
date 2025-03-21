<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 2/13/2025
  Time: 9:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add Account</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/Account.css">
    <style>
        .container {
            max-width: 900px;
            margin: auto;
            padding: 20px;
        }

        .back-home {
            margin-bottom: 15px;
        }

        .row {
            display: flex;
            flex-wrap: wrap;
        }

        .col-md-6 {
            flex: 1;
            padding: 10px;
        }

        .form-container {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }

        .form-buttons {
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 8px 16px;
            font-size: 16px;
            border-radius: 5px;
        }

        .btn-success {
            background-color: #28a745;
            color: white;
            border: none;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
            border: none;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        .table th, .table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        .table th {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2>Add New Account</h2>
    <div class="back-home">
        <a href="account">← Back to Home</a>
    </div>
    <div class="row">
        <!-- Form Thêm Tài Khoản -->
        <div class="col-md-6">
            <form id="addAccountForm" action="addaccount" method="post">
                <div class="mb-3">
                    <label for="employeeId" class="form-label">Employee ID: <span class="required">*</span></label>
                    <select id="employeeId" name="employeeId" class="form-select" required
                            onchange="updateEmployeeInfo()">
                        <option value="">Select EmployeeID</option>
                        <c:forEach var="employee" items="${employees}">
                            <option value="${employee.employeeID}"
                                    data-name="${employee.employeeName}"
                                    data-email="${employee.email}"
                                    data-phone="${employee.phone}">
                                    ${employee.employeeID}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="roleId" class="form-label">Role: <span class="required">*</span></label>
                    <select id="roleId" name="roleId" class="form-select" required>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.id}">${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email <span class="required">*</span></label>
                    <input type="email" class="form-control" id="email" name="email" value="" required readonly>
                </div>
                <div class="form-buttons">
                    <button style="margin-top: 10px" type="submit" class="btn btn-success">Add</button>
                    <button type="reset" class="btn btn-secondary btn-reset">Cancel</button>
                </div>
            </form>
        </div>

        <!-- Bảng Thông Tin Nhân Viên -->
        <div class="col-md-6">
            <h4>Employee Information</h4>
            <table class="table table-bordered">
                <tr>
                    <th>Name</th>
                    <td id="empName">-</td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td id="empEmail">-</td>
                </tr>
                <tr>
                    <th>Phone</th>
                    <td id="empPhone">-</td>
                </tr>
            </table>
        </div>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
    function updateEmployeeInfo() {
        var select = document.getElementById("employeeId");
        var emailInput = document.getElementById("email");
        var selectedOption = select.options[select.selectedIndex];

        document.getElementById("empName").textContent = selectedOption.getAttribute("data-name") || "-";
        document.getElementById("empEmail").textContent = selectedOption.getAttribute("data-email") || "-";
        document.getElementById("empPhone").textContent = selectedOption.getAttribute("data-phone") || "-";

        emailInput.value = selectedOption.getAttribute("data-email") || "";
    }
</script>
</body>
</html>
