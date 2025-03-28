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
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="sherah-breadcrumb mg-top-30">
                    <h2 class="sherah-breadcrumb__title">Add New Bin for Section: ${section.sectionName}</h2>
                </div>

                <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                    <div class="sherah-table__heading">
                        <h3>Bin Information</h3>
                    </div>

                    <%
                        // Retrieve messages from ServletContext
                        String errorMessage = (String) application.getAttribute("errorMessage");
                        String successMessage = (String) application.getAttribute("successMessage");
                    %>

                    <c:if test="<%= errorMessage != null && !errorMessage.isEmpty() %>">
                        <div class="error-message">
                            <%= errorMessage %>
                        </div>
                        <% application.removeAttribute("errorMessage"); %>
                    </c:if>

                    <c:if test="<%= successMessage != null && !successMessage.isEmpty() %>">
                        <div class="success-message">
                            <%= successMessage %>
                        </div>
                        <% application.removeAttribute("successMessage"); %>
                    </c:if>

                    <form action="addBin" method="post" class="sherah-form">
                        <input type="hidden" name="sectionID" value="${section.sectionID}">

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="sherah-form__label">Bin ID</label>
                                    <input type="text" name="binID" class="sherah-form__input"
                                           value="${nextBinID}" readonly>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="sherah-form__label">Bin Name</label>
                                    <input type="text" name="binName" class="sherah-form__input"
                                           placeholder="Enter Bin Name" required>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="sherah-form__label">Max Capacity (kg)</label>
                                    <select name="maxCapacity" class="sherah-form__input" required>
                                        <option value="">Select Bin Capacity</option>
                                        <option value="50.00">50 kg</option>
                                        <option value="100.00">100 kg</option>
                                        <option value="150.00">150 kg</option>
                                    </select>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="sherah-form__label">Status</label>
                                    <select name="status" class="sherah-form__input">
                                        <option value="true">Unlock</option>
                                        <option value="false">Lock</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="form-group mg-top-20">
                            <button type="submit" class="sherah-btn sherah-btn__primary">
                                Add Bin
                            </button>
                            <a href="list-bin?id=${section.sectionID}" class="sherah-btn sherah-btn__secondary">
                                Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</section>
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