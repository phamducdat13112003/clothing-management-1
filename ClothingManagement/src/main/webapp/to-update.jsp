<%@ page import="java.util.List" %>
<%@ page import="org.example.clothingmanagement.entity.TransferOrder" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html class="no-js" lang="zxx">
<head>
  <!-- Meta Tags -->
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="keywords" content="Site keywords here">
  <meta name="description" content="#">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
  <!-- Site Title -->
  <title>Clothing Management - Update Transfer Order</title>
  <!-- Font -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;0,900;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">
  <!-- Fav Icon -->
  <link rel="icon" href="img/favicon.png">
  <!-- Stylesheet -->
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

<div class="sherah-body-area">
  <jsp:include page="include/sidebar.jsp"></jsp:include>
  <jsp:include page="include/header.jsp"></jsp:include>

  <section class="sherah-adashboard sherah-show">
    <div class="container">
      <div class="row">
        <div class="col-12">
          <div class="sherah-body">
            <div class="sherah-dsinner">
              <div class="row mg-top-30">
                <div class="col-12 sherah-flex-between">
                  <div class="sherah-breadcrumb">
                    <h2 class="sherah-breadcrumb__title">Transfer Order Detail</h2>
                    <ul class="sherah-breadcrumb__list">
                      <li><a href="TOList">Transfer Order List</a></li>
                      <li class="active"><a href="#">Transfer Order Detail</a></li>
                      <li class="active"><a href="#">${transferOrder.toID}</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>

            <%-- Messages section --%>
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
              <div class="cart-wrapper">
                <div class="container">
                  <!-- General Information Section -->
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="summary-card p-4 border rounded">
                        <h5 class="mb-4">General Information</h5>
                        <div class="d-flex justify-content-between mb-3">
                          <span class="text-muted">Transfer Order ID</span>
                          <span class="text-muted">${transferOrder.toID}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                          <span class="text-muted">Created By</span>
                          <span class="text-muted">${employeeName}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                          <span class="text-muted">Create Date</span>
                          <span class="text-muted">${transferOrder.createdDate}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                          <span class="text-muted">Status</span>
                          <span class="badge bg-${transferOrder.status == 'Pending' ? 'warning' :
                              transferOrder.status == 'Completed' ? 'success' :
                              transferOrder.status == 'Cancelled' ? 'danger' :
                              transferOrder.status == 'Processing' ? 'primary' : 'secondary'}">
                              ${transferOrder.status}
                          </span>

                        </div>
                      </div>
                    </div>

                    <!-- Transfer Order Destination Section -->
                    <div class="col-lg-6">
                      <div class="summary-card p-4 border rounded">
                        <h5 class="mb-4">Transfer Order Destination</h5>

                        <div class="d-flex justify-content-between align-items-center">
                          <!-- Left/Origin Warehouse -->
                          <div class="text-center">
                            <div class="mb-3">
                              <i class="bi bi-box-seam-fill" style="font-size: 3rem;"></i>
                            </div>
                            <div class="mb-2">
                              <strong>Section ID:</strong><br>
                              ${originalSectionID}
                            </div>
                            <div>
                              <strong>Bin:</strong><br>
                              ${toDetails[0].originBinID}
                            </div>
                          </div>

                          <!-- Arrow with Status -->
                          <div class="text-center">
                            <div style="position: relative;">
                              <hr style="width: 120px; height: 2px; background-color: #000;">
                              <i class="bi bi-arrow-right" style="position: absolute; top: -10px; right: -10px; font-size: 1.5rem;"></i>
                            </div>
                            <div class="mt-2">
                              <span class="badge bg-${transferOrder.status == 'Pending' ? 'warning' :
                                transferOrder.status == 'Completed' ? 'success' :
                                transferOrder.status == 'Cancelled' ? 'danger' :
                                transferOrder.status == 'Processing' ? 'primary' : 'secondary'}">
                                ${transferOrder.status}
                              </span>
                            </div>
                          </div>

                          <!-- Right/Destination Warehouse (Improved) -->
                          <div class="text-center">
                            <div class="mb-3">
                              <i class="bi bi-box-seam-fill" style="font-size: 3rem;"></i>
                            </div>
                            <div class="mb-2">
                              <strong>Section ID:</strong>
                              <div class="d-flex align-items-center justify-content-center">
                                <span id="finalSectionDisplay">${finalSectionID}</span>
                              </div>
                            </div>
                            <div>
                              <strong>Bin:</strong>
                              <div class="d-flex align-items-center justify-content-center">
                                <span id="finalBinDisplay">${toDetails[0].finalBinID}</span>
                              </div>
                            </div>

                            <!-- Single Edit Button -->

                            <c:if test="${transferOrder.status eq 'Pending'}">
                            <c:if test="${account.getRoleId() == 4}">
                              <a href="updateLocation?toID=${transferOrder.toID}" class="btn btn-sm btn-outline-primary mt-2">
                                <i class="bi bi-pencil-square"></i> Edit Location
                              </a>
                            </c:if>
                            </c:if>
                          </div>

                          <!-- Combined Edit Modal -->
                          <div class="modal fade" id="combinedEditModal" tabindex="-1" aria-labelledby="combinedEditModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 class="modal-title" id="combinedEditModalLabel">Edit Destination Location</h5>
                                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                  <form id="locationEditForm" onsubmit="event.preventDefault();">
                                    <div class="mb-3">
                                      <label for="finalSectionSelect" class="form-label">Section ID</label>
                                      <select class="form-select" id="finalSectionSelect" name="finalSectionSelect">
                                        <option value="">Select Section</option>
                                        <c:forEach items="${sections}" var="section">
                                          <option value="${section.sectionID}" ${section.sectionID == finalSectionID ? 'selected' : ''}>${section.sectionName}</option>
                                        </c:forEach>
                                      </select>
                                      <div class="invalid-feedback">Please select a section</div>
                                    </div>
                                    <div class="mb-3">
                                      <label for="finalBinSelect" class="form-label">Bin ID</label>
                                      <select class="form-select" id="finalBinSelect" name="finalBinSelect" disabled>
                                        <option value="">Select Bin</option>
                                      </select>
                                      <div class="invalid-feedback">Please select a bin</div>
                                    </div>
                                  </form>
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                  <button type="submit" form="locationEditForm" class="btn btn-primary">Save Changes</button>
                                </div>
                              </div>
                            </div>
                          </div>


                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Order Details Section -->
                  <div class="row mt-4">
                    <div class="col-12">
                      <div class="card">
                        <div class="card-header">
                          <h5 class="card-title">Transfer Order Details</h5>
                        </div>
                        <div class="card-body">
                          <div class="table-responsive">
                            <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                              <thead class="sherah-table__head">
                              <tr>
                                <th class="sherah-table__column-1">Product Detail ID</th>
                                <th class="sherah-table__column-5">Product Name</th>
                                <th class="sherah-table__column-5">Quantity</th>
                              </tr>
                              </thead>
                              <tbody class="sherah-table__body">
                              <c:if test="${not empty toDetails}">
                                <c:forEach var="toDetail" items="${toDetails}">
                                  <tr>
                                    <td class="sherah-table__column-1">${toDetail.productDetailID}</td>
                                    <td class="sherah-table__column-2">${toDetail.productName}</td>
                                    <td class="sherah-table__column-5">${toDetail.quantity}</td>
                                  </tr>
                                </c:forEach>
                              </c:if>
                              <c:if test="${empty toDetails}">
                                <tr>
                                  <td colspan="6" class="text-center">No transfer order details exist</td>
                                </tr>
                              </c:if>
                              </tbody>
                            </table>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Action Buttons -->
                  <div class="row mt-4">
                    <div class="col-12">
                      <div class="d-flex justify-content-between">
                        <a href="TOList" class="btn btn-secondary">Back to List</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>


<!-- JavaScript -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/main.js"></script>



</body>
</html>