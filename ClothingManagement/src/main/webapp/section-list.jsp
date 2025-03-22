<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Section List</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
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
        <td>
          <a href="section?action=view&id=${section.sectionID}" class="btn btn-info btn-sm">Chi tiết</a>
          <a href="section?action=showEdit&id=${section.sectionID}" class="btn btn-warning btn-sm">Sửa</a>
          <a href="#" onclick="confirmDelete('${section.sectionID}')" class="btn btn-danger btn-sm">Xóa</a>
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