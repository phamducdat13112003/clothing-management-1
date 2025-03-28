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
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    .search-form {
      display: flex;
      justify-content: flex-end;
      margin-bottom: 15px;
    }
    .search-input {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 4px 0 0 4px;
      width: 250px;
    }
    .search-button {
      padding: 8px 15px;
      background-color: #5830E0;
      color: white;
      border: none;
      border-radius: 0 4px 4px 0;
      cursor: pointer;
    }
    .search-button:hover {
      background-color: #4120b9;
    }
    .pagination {
      display: flex;
      justify-content: center;
      margin-top: 20px;
    }
    .pagination a {
      color: #5830E0;
      padding: 8px 16px;
      text-decoration: none;
      border: 1px solid #ddd;
      margin: 0 4px;
    }
    .pagination a.active {
      background-color: #5830E0;
      color: white;
      border: 1px solid #5830E0;
    }
    .pagination a:hover:not(.active) {background-color: #ddd;}

    /* Filter styles */
    .filter-container {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 5px;
      margin-bottom: 20px;
      border: 1px solid #ddd;
    }
    .filter-row {
      display: flex;
      flex-wrap: wrap;
      margin-bottom: 10px;
      align-items: center;
    }
    .filter-group {
      margin-right: 15px;
      margin-bottom: 10px;
      flex: 1;
      min-width: 200px;
    }
    .filter-label {
      display: block;
      margin-bottom: 5px;
      font-weight: 500;
    }
    .filter-select, .filter-input {
      width: 100%;
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    .filter-actions {
      display: flex;
      justify-content: flex-end;
      margin-top: 10px;
    }
    .filter-button {
      padding: 8px 20px;
      background-color: #5830E0;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      margin-left: 10px;
    }
    .filter-button.reset {
      background-color: #6c757d;
    }
    .filter-button:hover {
      opacity: 0.9;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
      .filter-group {
        flex: 100%;
        margin-right: 0;
      }
    }
  </style>
</head>
<body id="sherah-dark-light">
<jsp:include page="include/sidebar.jsp"></jsp:include>
<jsp:include page="include/header.jsp"></jsp:include>
<!-- sherah Dashboard -->
<section class="sherah-adashboard sherah-show">
  <div class="container">
    <div class="row">
      <div class="col-12">
        <div class="sherah-body">
          <div class="sherah-dsinner">
            <div class="row align-items-center justify-content-between">
              <div class="col-6">
                <div class="sherah-breadcrumb mg-top-30">
                  <h2 class="sherah-breadcrumb__title">View Transfer Order List</h2>
                  <ul class="sherah-breadcrumb__list">
                    <li><a href="viewbininventory">Home</a></li>
                  </ul>
                </div>
              </div>
              <div class="col-6">
                <form action="TOList" method="post" class="search-form">
                  <input type="text" name="search" placeholder="Search..." value="${search}" class="search-input">
                  <button type="submit" class="search-button">Search</button>
                </form>
              </div>
            </div>

            <!-- Filter Section -->
            <div class="filter-container">
              <form action="TOList" method="post">
                <div class="filter-row">
                  <div class="filter-group">
                    <label class="filter-label">Status</label>
                    <select name="statusFilter" class="filter-select">
                      <option value="">All Statuses</option>
                      <c:forEach items="${allStatuses}" var="status">
                        <option value="${status}" ${statusFilter eq status ? 'selected' : ''}>${status}</option>
                      </c:forEach>
                    </select>
                  </div>
                  <div class="filter-group">
                    <label class="filter-label">Created By</label>
                    <select name="createdByFilter" class="filter-select">
                      <option value="">All Users</option>
                      <c:forEach items="${allCreatedBy}" var="creator">
                        <option value="${creator}" ${createdByFilter eq creator ? 'selected' : ''}>${creator}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
                <div class="filter-row">
                  <div class="filter-group">
                    <label class="filter-label">Date From</label>
                    <input type="date" name="dateFrom" value="${dateFrom}" class="filter-input">
                  </div>
                  <div class="filter-group">
                    <label class="filter-label">Date To</label>
                    <input type="date" name="dateTo" value="${dateTo}" class="filter-input">
                  </div>
                </div>

                <!-- Keep search value when applying filters -->
                <input type="hidden" name="search" value="${search}">

                <div class="filter-actions">
                  <a href="${pageContext.request.contextPath}/TOList" class="filter-button reset">Reset</a>
                  <button type="submit" class="filter-button">Apply Filters</button>
                </div>
              </form>
            </div>

            <!-- Messages section -->
            <c:if test="${not empty sessionScope.successMessage}">
              <div class="alert alert-success" role="alert">
                  ${sessionScope.successMessage}
                <c:remove var="successMessage" scope="session"/>
              </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
              <div class="alert alert-danger" role="alert">
                  ${errorMessage}
              </div>
            </c:if>

            <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
              <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                <thead class="sherah-table__head">
                <tr>
                  <th class="sherah-table__column-1 sherah-table__h2">TOID</th>
                  <th class="sherah-table__column-2 sherah-table__h1">Created By</th>
                  <th class="sherah-table__column-2 sherah-table__h2">Status</th>
                  <th class="sherah-table__column-2 sherah-table__h2">Created Date</th>
                  <th class="sherah-table__column-2 sherah-table__h2">Action</th>
                </tr>
                </thead>
                <tbody class="sherah-table__body">
                <c:if test="${not empty transferOrders}">
                  <c:forEach var="order" items="${transferOrders}">
                    <tr>
                      <td class="sherah-table__column-2 sherah-table__data-2">
                        <div class="sherah-table__product-content">
                          <a href="${pageContext.request.contextPath}/TODetail?toID=${order.toID}">${order.toID}</a>
                        </div>
                      </td>
                      <td class="sherah-table__column-2 sherah-table__data-2">
                        <div class="sherah-table__product-content">
                          <p class="sherah-table__product-desc">${order.createdBy}</p>
                        </div>
                      </td>
                      <td class="sherah-table__column-2 sherah-table__data-2">
                        <div class="sherah-table__product-content">
                          <p class="sherah-table__product-desc">${order.status}</p>
                        </div>
                      </td>
                      <td class="sherah-table__column-2 sherah-table__data-2">
                        <div class="sherah-table__product-content">
                          <p class="sherah-table__product-desc">${order.createdDate}</p>
                        </div>
                      </td>
                      <td class="sherah-table__column-2 sherah-table__data-2 d-flex align-items-center">
                        <!-- Link to edit the transfer order -->
                        <c:if test="${account.getRoleId() == 4 || account.getRoleId() == 1}">
                        <a href="${pageContext.request.contextPath}/TOUpdate?toID=${order.toID}" class="text-primary me-2" title="View and edit">
                          <i class="bi bi-eye"></i>
                        </a>
                        </c:if>

                        <c:if test="${account.getRoleId() == 4}">
                        <c:if test="${order.status == 'Pending'}">
                        <a href="${pageContext.request.contextPath}/TOList?action=process&toID=${order.toID}&page=${currentPage}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}"
                           onclick="return confirm('Are you sure you want to mark this transfer order as processing?');"
                           class="text-success me-2" title="Processing">
                          <i class="bi-chevron-double-right"></i>
                        </a>
                        </c:if>
                        </c:if>

                        <c:if test="${account.getRoleId() == 4}">
                        <!-- Complete button - only show if status is not already COMPLETED -->
                          <c:if test="${order.status == 'Processing'}">
                            <a href="${pageContext.request.contextPath}/TOList?action=done&toID=${order.toID}&page=${currentPage}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}"
                              onclick="return confirm('Are you sure you want to mark this transfer order as completed?');"
                              class="text-success me-2" title="Complete">
                              <i class="bi bi-check-circle"></i>
                            </a>
                          </c:if>
                        </c:if>

                        <c:if test="${account.getRoleId() == 4}">
                        <!-- Link to delete the transfer order -->
                        <c:if test="${order.status == 'Pending'}">
                        <a href="${pageContext.request.contextPath}/TOList?action=cancel&toID=${order.toID}&page=${currentPage}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}"
                           onclick="return confirm('Are you sure you want to cancel this transfer order?');"
                           class="text-danger" title="Cancel">
                          <i class="bi bi-trash"></i>
                        </a>
                        </c:if>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                </c:if>
                <c:if test="${empty transferOrders}">
                  <tr>
                    <td colspan="5" class="text-center">No transfer orders found</td>
                  </tr>
                </c:if>
                </tbody>
              </table>

              <!-- Pagination section -->
              <c:if test="${not empty transferOrders}">
                <div class="pagination">
                  <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/TOList?page=${currentPage - 1}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}">Previous</a>
                  </c:if>

                  <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                      <c:when test="${i == currentPage}">
                        <a href="#" class="active">${i}</a>
                      </c:when>
                      <c:otherwise>
                        <a href="${pageContext.request.contextPath}/TOList?page=${i}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}">${i}</a>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>

                  <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/TOList?page=${currentPage + 1}&search=${search}&statusFilter=${statusFilter}&dateFrom=${dateFrom}&dateTo=${dateTo}&createdByFilter=${createdByFilter}">Next</a>
                  </c:if>
                </div>
              </c:if>
            </div>
          </div>
          <!-- End Dashboard Inner -->
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