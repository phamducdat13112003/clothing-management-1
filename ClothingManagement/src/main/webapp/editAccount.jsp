<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 2/13/2025
  Time: 10:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit Account</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/Account.css">
    <style>
        .required {
            color: red;
            font-weight: bold;
        }
        .error-message {
            color: red;
            font-size: 12px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2>Edit Account</h2>
    <div class="back-home">
        <a href="account">← Back to Home</a>
        <c:if test="${not empty message}">
            <span class="error-message">${message}</span>
        </c:if>
    </div>
    <form id="addAccountForm" action="editaccount" method="post">
        <div class="mb-3">
            <label for="AccountID" class="form-label">Account ID: <span class="required">*</span></label>
            <input type="text" class="form-control" value="${account.id}" id="accountID" name="accountID" readonly>
        </div>
        <div class="mb-3">
            <label for="roleId" class="form-label">Role: <span class="required">*</span></label>
            <select id="roleId" name="roleId" class="form-select" required>
                <c:forEach var="role" items="${roles}">
                    <option value="${role.id}"
                            <c:if test="${role.id == account.roleId}">selected</c:if>>${role.roleName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email <span class="required">*</span></label>
            <input type="email" class="form-control" id="email" name="email" value="${account.email}" readonly>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <div class="input-group">
                <input minlength="8" type="password" class="form-control" id="password" name="password">
                <span class="input-group-text" id="togglePassword" style="cursor: pointer;">
                    <i class="bi bi-eye-slash" id="eyeIcon"></i> <!-- Mặc định là icon mắt đóng -->
                </span>
            </div>
            <c:if test="${not empty errorPassword}">
                <p class="text-danger">${errorPassword}</p>
            </c:if>
        </div>
        <div class="mb-3">
            <input type="hidden" name="status" value="${account.status}">
        </div>
        <div class="form-buttons">
            <button style="margin-top: 10px" type="submit" class="btn btn-success">Save</button>
            <button type="reset" class="btn btn-secondary btn-reset">Cancel</button>
        </div>
    </form>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
    // Lấy các phần tử cần thiết
    const passwordField = document.getElementById("password");
    const togglePassword = document.getElementById("togglePassword");
    const eyeIcon = document.getElementById("eyeIcon");

    // Thêm sự kiện click cho icon mắt
    togglePassword.addEventListener("click", function() {
        // Kiểm tra nếu type là password thì chuyển thành text và ngược lại
        const type = passwordField.type === "password" ? "text" : "password";
        passwordField.type = type;

        // Thay đổi biểu tượng của mắt
        eyeIcon.classList.toggle("bi-eye"); // Đổi thành mắt mở
        eyeIcon.classList.toggle("bi-eye-slash"); // Đổi thành mắt đóng
    });
</script>
</body>
</html>
