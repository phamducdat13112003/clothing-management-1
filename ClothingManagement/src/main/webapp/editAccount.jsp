<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Account</title>
    <link rel="stylesheet" href="css/EditAccount.css">
    <style>
        .required {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<form action="editaccount" method="post" class="edit-form">
    <h2>Edit Account</h2>
    <input style="width: 95%" type="hidden" id="accountID" name="accountID" value="${account.id}" readonly>

    <label for="email">Email <span class="required">*</span></label>
    <input style="width: 95%" type="email" id="email" name="email" value="${account.email}" required>

    <label for="password">Password <span class="required">*</span></label>
    <div class="password-toggle">
        <input type="password" id="password" name="password" value="${account.password}" required>
        <span id="togglePassword">👁️</span>
    </div>

    <label for="password">Confirm password <span class="required"></span></label>
    <div class="password-toggle">
        <input type="password" id="confirmPassword" name="confirmPassword">
    </div>

    <!-- Thêm trường Role -->
    <label for="role">Role <span class="required"></span></label>
    <select class="form-select" id="role" name="roleID" required>
        <c:forEach items="${roles}" var="role">
            <option value="${role.id}" ${role.id == account.roleId ? 'selected' : ''}>${role.roleName}</option>
        </c:forEach>
    </select>

    <div style="display: flex; justify-content: space-between;">
        <button type="submit">Save</button>
        <button type="button" class="cancel" onclick="window.location.reload();">Cancel</button>
    </div>
    <a style="width: 90%" href="account" class="back">Back to Account</a>
</form>

<script>
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    togglePassword.addEventListener('click', () => {
        const type = passwordInput.type === 'password' ? 'text' : 'password';
        passwordInput.type = type;
        togglePassword.textContent = type === 'password' ? '👁️' : '🙈';
    });
</script>
<c:if test="${not empty message}">
    <script>
        window.onload = function () {
            alert("${message}");
        };
    </script>
</c:if>
</body>
</html>
