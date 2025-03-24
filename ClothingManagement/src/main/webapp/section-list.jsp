<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Section List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }

        .container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #343a40;
        }

        .table {
            background: white;
            border-radius: 10px;
            overflow: hidden;
        }

        .table th {
            background-color: #007bff;
            color: white;
            text-align: center;
        }

        .table td {
            vertical-align: middle;
            text-align: center;
        }

        .btn {
            border-radius: 5px;
        }

        .btn-sm {
            padding: 5px 10px;
            font-size: 14px;
        }

        .alert {
            border-radius: 5px;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .pagination button {
            margin: 0 5px;
            padding: 5px 10px;
            border: none;
            background-color: #007bff;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .pagination button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }

    </style>
</head>
<body>
<div class="container mt-4">
    <h2>Danh sách Section</h2>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" role="alert">
                ${successMessage}
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <a href="section?action=showAdd" class="btn btn-primary mb-3">Thêm Section</a>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Section ID</th>
            <th>Section Name</th>
            <th>Section Type</th>
            <th>Description</th>
            <th>Total Bins</th>
            <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="section" items="${sections}">
            <tr>
                <td>${section.sectionID}</td>
                <td>${section.sectionName}</td>
                <td>${section.sectionTypeName}</td>
                <td>${section.description}</td>
                <td>
                    <span class="badge bg-primary">${section.totalBins}</span>
                </td>
                <td class="sherah-table__column-2 sherah-table__data-2">
                    <div class="sherah-table__product-content">
                        <p class="sherah-table__product-desc" style="display: inline-block; margin-right: 10px;">
                            <a href="section?action=view&id=${section.sectionID}">Detail</a>
                        </p>
                        <p class="sherah-table__product-desc" style="display: inline-block; margin-right: 10px;">
                            <a href="section?action=showEdit&id=${section.sectionID}">Edit</a>
                        </p>
                        <c:if test="${account.getRoleId() == 1}">
                            <p class="sherah-table__product-desc" style="display: inline-block;">
                                <a href="javascript:void(0);" onclick="confirmDeleteSection('${section.sectionID}')" class="delete-link">Delete</a>
                            </p>
                        </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function confirmDelete(id) {
        if (confirm("Bạn có chắc chắn muốn xóa section này?")) {
            window.location.href = "section?action=delete&id=" + id;
        }
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>