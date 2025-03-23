<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add New Section</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
  <h2>Thêm Section Mới</h2>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
        ${errorMessage}
    </div>
  </c:if>

  <form action="section?action=add" method="post">
    <div class="mb-3">
      <label for="sectionID" class="form-label">Section ID</label>
      <input type="text" class="form-control" id="sectionID" name="sectionID" value="${sectionID}" readonly>
    </div>

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
      <button type="submit" class="btn btn-primary">Lưu</button>
      <a href="section?action=list" class="btn btn-secondary">Hủy</a>
    </div>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
