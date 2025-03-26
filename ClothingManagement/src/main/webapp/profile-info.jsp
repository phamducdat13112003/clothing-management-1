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
		.profile-container {
			max-width: 800px;
			margin: 0 auto;
			padding: 20px;
		}
		.profile-header {
			text-align: center;
			margin-bottom: 30px;
		}
		.profile-avatar {
			width: 150px;
			height: 150px;
			border-radius: 50%;
			margin: 0 auto;
			overflow: hidden;
			position: relative;
			background-color: #e9ecef;
			border: 3px solid #fff;
			box-shadow: 0 0 10px rgba(0,0,0,0.1);
		}
		.profile-avatar img {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
		.profile-avatar .edit-avatar {
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			background: rgba(0,0,0,0.6);
			color: white;
			text-align: center;
			padding: 5px;
			cursor: pointer;
		}
		.profile-info {
			background-color: white;
			border-radius: 10px;
			padding: 20px;
			box-shadow: 0 0 15px rgba(0,0,0,0.1);
		}
		.info-group {
			margin-bottom: 15px;
		}
		.upload-form {
			display: none;
		}
	</style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
	<div class="container profile-container">
		<div class="profile-header">
			<div class="profile-avatar">
				<c:choose>
					<c:when test="${not empty employee.image}">
						<img src="${pageContext.request.contextPath}/img/${employee.image}" alt="Employee Photo">
					</c:when>
					<c:otherwise>
						<div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 50px; color: #6c757d;">
							<i class="fas fa-user"></i>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="edit-avatar" onclick="toggleUploadForm()">
					<i class="fas fa-camera"></i> Change Photo
				</div>
			</div>

			<div id="uploadFormContainer" class="upload-form mt-3">
				<form id="imageUploadForm" action="${pageContext.request.contextPath}/employee" method="post" enctype="multipart/form-data" onsubmit="return validateImageUpload()">
					<div class="input-group mb-3">
						<input type="file"
							   class="form-control"
							   id="profileImage"
							   name="profileImage"
							   accept="image/*"
							   required>
						<button class="btn btn-primary" type="submit">Upload</button>
						<button class="btn btn-outline-secondary" type="button" onclick="toggleUploadForm()">Cancel</button>
					</div>
					<div id="imageValidationError" class="text-danger mt-2" style="display: none;"></div>
				</form>
			</div>

			<h2 class="mt-3">${employeeName}</h2>

			<c:choose>
				<c:when test="${role == 1}">
					<span class="badge bg-primary">Manager</span>
				</c:when>
				<c:when test="${role == 2}">
					<span class="badge bg-success">Purchase Order Staff</span>
				</c:when>
				<c:when test="${role == 3}">
					<span class="badge bg-danger">Admin</span>
				</c:when>
				<c:when test="${role == 4}">
					<span class="badge bg-warning">Storage Staff</span>
				</c:when>
			</c:choose>
		</div>

		<div class="profile-info">
			<c:if test="${not empty imageUpdateSuccess}">
				<div class="alert alert-success alert-dismissible fade show" role="alert">
						${imageUpdateSuccess}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:if>

			<c:if test="${not empty imageUpdateError}">
				<div class="alert alert-danger alert-dismissible fade show" role="alert">
						${imageUpdateError}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:if>

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

			<h4 class="mb-4">Personal Information</h4>

			<div class="row">
				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Employee ID:</label>
						<div>${employeeId}</div>
					</div>
				</div>

				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Full Name:</label>
						<div>${employeeName}</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Warehouse ID:</label>
						<div>${warehouseId}</div>
					</div>
				</div>

				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Email:</label>
						<div>${account.email}</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Account Status:</label>
						<div>
							<c:choose>
								<c:when test="${account.status == 'Active'}">
									<span class="text-success">${account.status}</span>
								</c:when>
								<c:otherwise>
									<span class="text-danger">${account.status}</span>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>

				<div class="col-md-6">
					<div class="info-group">
						<label class="fw-bold">Role:</label>
						<div>
							<c:choose>
								<c:when test="${role == 1}">Manager</c:when>
								<c:when test="${role == 2}">Purchase Order Staff</c:when>
								<c:when test="${role == 3}">Admin</c:when>
								<c:when test="${role == 4}">Storage Staff</c:when>
							</c:choose>
						</div>
					</div>
				</div>
			</div>

			<c:if test="${not empty employee.phone}">
				<div class="row">
					<div class="col-md-6">
						<div class="info-group">
							<label class="fw-bold">Phone:</label>
							<div>${employee.phone}</div>
						</div>
					</div>

					<c:if test="${not empty employee.address}">
						<div class="col-md-6">
							<div class="info-group">
								<label class="fw-bold">Address:</label>
								<div>${employee.address}</div>
							</div>
						</div>
					</c:if>
				</div>
			</c:if>

			<div class="text-center mt-4">
				<a href="changePassword" class="btn btn-outline-secondary">Change Password</a>
			</div>
		</div>
	</div>

</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	function validateImageUpload() {
		const fileInput = document.getElementById('profileImage');
		const errorDiv = document.getElementById('imageValidationError');
		const file = fileInput.files[0];

		// Reset error message
		errorDiv.style.display = 'none';
		errorDiv.textContent = '';

		// Check if a file is selected
		if (!file) {
			errorDiv.textContent = 'Please select an image.';
			errorDiv.style.display = 'block';
			return false;
		}

		// Validate file size (2MB = 2 * 1024 * 1024 bytes)
		const maxSizeBytes = 2 * 1024 * 1024;
		if (file.size > maxSizeBytes) {
			errorDiv.textContent = 'Image size must be less than 2MB.';
			errorDiv.style.display = 'block';
			return false;
		}

		// Validate file type
		const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
		if (!allowedTypes.includes(file.type)) {
			errorDiv.textContent = 'Please upload a valid image file (JPEG, PNG, GIF, WebP).';
			errorDiv.style.display = 'block';
			return false;
		}

		return true;
	}

	function toggleUploadForm() {
		const uploadForm = document.getElementById('uploadFormContainer');
		uploadForm.style.display = uploadForm.style.display === 'none' ? 'block' : 'none';
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