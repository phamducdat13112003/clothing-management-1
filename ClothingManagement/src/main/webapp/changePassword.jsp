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

    <!-- Site Title -->
    <title>Manage Employee</title>

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
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <div class="container password-container">
            <div class="password-header">
                <h2>Change Password</h2>
            </div>

            <div class="password-form">

                <form action="changePassword" method="post" onsubmit="return validateForm()">
                    <div class="mb-3">
                        <label for="currentPassword" class="form-label">Current Password</label>
                        <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                    </div>

                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                        <div id="passwordRequirements" class="text-muted small mt-1">
                            Password must:
                            <ul class="ps-3">
                                <li id="lengthReq">Be at least 8 characters long</li>
                                <li id="letterReq">Contain letters</li>
                                <li id="numberReq">Contain numbers</li>
                                <li id="specialReq">Contain special characters</li>
                                <li id="noWhitespaceReq">Not contain whitespace</li>
                            </ul>
                        </div>
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
                        <a href="${pageContext.request.contextPath}/employee?action=view&employeeID=${employeeID}" class="btn btn-outline-secondary ms-2">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function validateForm() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const mismatchAlert = document.getElementById('passwordMismatch');

        // Reset requirement indicators
        ['lengthReq', 'letterReq', 'numberReq', 'specialReq', 'noWhitespaceReq']
            .forEach(id => document.getElementById(id).classList.remove('text-success', 'text-danger'));

        // Validation checks
        let isValid = true;

        // Check password match
        if (newPassword !== confirmPassword) {
            mismatchAlert.style.display = 'block';
            isValid = false;
        } else {
            mismatchAlert.style.display = 'none';
        }

        // Check length
        const lengthReq = document.getElementById('lengthReq');
        if (newPassword.length < 8) {
            lengthReq.classList.add('text-danger');
            isValid = false;
        } else {
            lengthReq.classList.add('text-success');
        }

        // Check for letters
        const letterReq = document.getElementById('letterReq');
        if (!/[a-zA-Z]/.test(newPassword)) {
            letterReq.classList.add('text-danger');
            isValid = false;
        } else {
            letterReq.classList.add('text-success');
        }

        // Check for numbers
        const numberReq = document.getElementById('numberReq');
        if (!/[0-9]/.test(newPassword)) {
            numberReq.classList.add('text-danger');
            isValid = false;
        } else {
            numberReq.classList.add('text-success');
        }

        // Check for special characters
        const specialReq = document.getElementById('specialReq');
        if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(newPassword)) {
            specialReq.classList.add('text-danger');
            isValid = false;
        } else {
            specialReq.classList.add('text-success');
        }

        // Check for whitespace
        const noWhitespaceReq = document.getElementById('noWhitespaceReq');
        if (/\s/.test(newPassword) || newPassword.trim() === '') {
            noWhitespaceReq.classList.add('text-danger');
            isValid = false;
        } else {
            noWhitespaceReq.classList.add('text-success');
        }

        return isValid;
    }
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