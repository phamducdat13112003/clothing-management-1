<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .password-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .password-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .password-form {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body class="bg-light">
<!-- Include your navigation/header here -->

<div class="container password-container">
    <div class="password-header">
        <h2>Change Password</h2>
    </div>

    <div class="password-form">
        <c:if test="${not empty passwordError}">
            <div class="alert alert-danger" role="alert">
                    ${passwordError}
            </div>
        </c:if>

        <c:if test="${not empty passwordSuccess}">
            <div class="alert alert-success" role="alert">
                    ${passwordSuccess}
            </div>
        </c:if>

        <form action="changePassword" method="post" onsubmit="return validateForm()">
            <div class="mb-3">
                <label for="currentPassword" class="form-label">Current Password</label>
                <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
            </div>

            <div class="mb-3">
                <label for="newPassword" class="form-label">New Password</label>
                <input type="password" class="form-control" id="newPassword" name="newPassword" required>
            </div>

            <div class="mb-3">
                <label for="confirmPassword" class="form-label">Confirm New Password</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                <div id="passwordMismatch" class="text-danger" style="display: none;">
                    Passwords do not match!
                </div>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary">Change Password</button>
                <a href="profile" class="btn btn-outline-secondary ms-2">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function validateForm() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const mismatchAlert = document.getElementById('passwordMismatch');

        if (newPassword !== confirmPassword) {
            mismatchAlert.style.display = 'block';
            return false;
        }

        mismatchAlert.style.display = 'none';
        return true;
    }
</script>
</body>
</html>