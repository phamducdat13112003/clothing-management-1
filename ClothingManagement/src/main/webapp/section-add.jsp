<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Section</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">

    <!-- Fav Icon -->
    <link rel="icon" href="img/favicon.png">
</head>
<style>
    body {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background: linear-gradient(to right, #74ebd5, #acb6e5); /* Gradient màu nền */
        font-family: 'Roboto', sans-serif;
    }

    .container {
        width: 100%;
        max-width: 500px;
        background: white;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
        animation: fadeIn 0.5s ease-in-out;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    h2 {
        text-align: center;
        margin-bottom: 20px;
        font-weight: bold;
        color: #333;
    }

    .home-link {
        display: block;
        text-align: center;
        margin-bottom: 15px;
        font-size: 16px;
        text-decoration: none;
        font-weight: bold;
        color: #007bff;
        transition: color 0.3s ease;
    }

    .home-link:hover {
        color: #0056b3;
        text-decoration: underline;
    }

    /* Form Styling */
    .form-label {
        font-weight: 600;
        color: #555;
    }

    .form-control, .form-select {
        border-radius: 8px;
        border: 1px solid #ccc;
        transition: all 0.3s ease;
        padding: 10px;
    }

    .form-control:focus, .form-select:focus {
        border-color: #007bff;
        box-shadow: 0 0 8px rgba(0, 123, 255, 0.3);
    }

    /* Buttons */
    .btn-primary {
        background: #007bff;
        border: none;
        border-radius: 8px;
        padding: 10px;
        transition: all 0.3s ease-in-out;
    }

    .btn-primary:hover {
        background: #0056b3;
    }

    .btn-secondary {
        background: #6c757d;
        border: none;
        border-radius: 8px;
        padding: 10px;
        transition: all 0.3s ease-in-out;
    }

    .btn-secondary:hover {
        background: #5a6268;
    }

    /* Alert Styling */
    .alert {
        font-size: 14px;
        text-align: center;
        border-radius: 8px;
    }
</style>
<body>
<div class="container mt-4">
    <h2>Add New Section </h2>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>
    <a href="view-list-section-type" class="home-link">Home</a>
    <form action="addsection" method="post">
        <div class="mb-3">
            <label for="sectionName" class="form-label">Section Name</label>
            <input type="text" class="form-control" id="sectionName" name="sectionName" required>
        </div>

        <div class="mb-3">
            <label for="sectionTypeID" class="form-label">Section Type</label>
            <select class="form-select" id="sectionTypeID" name="sectionTypeID" required>
                <option value="">Select Section Type</option>
                <c:forEach var="type" items="${sectionTypes}">
                    <option value="${type.sectionTypeId}">${type.sectionTypeName} - ${type.description}</option>
                </c:forEach>
            </select>
        </div>
        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">Add</button>
            <button type="reset" class="btn btn-secondary">Cancel</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
