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
                    <h2 class="sherah-breadcrumb__title">Update Transfer Order</h2>
                    <ul class="sherah-breadcrumb__list">
                      <li><a href="TOList">Transfer Order List</a></li>
                      <li class="active"><a href="#">Update Transfer Order</a></li>
                      <li class="active"><a href="#">${transferOrder.toID}</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>

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
                                                 transferOrder.status == 'Done' ? 'success' :
                                                 transferOrder.status == 'Cancelled' ? 'danger' : 'secondary'}">
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
                                                     transferOrder.status == 'Done' ? 'success' :
                                                     transferOrder.status == 'Cancelled' ? 'danger' : 'secondary'}">
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
                            <c:if test="${transferOrder.status == 'Pending'}">
                              <button class="btn btn-sm btn-outline-primary mt-2" data-bs-toggle="modal" data-bs-target="#combinedEditModal">
                                <i class="bi bi-pencil-square"></i> Edit Location
                              </button>
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
                                  <form id="locationEditForm">
                                    <div class="mb-3">
                                      <label for="finalSectionSelect" class="form-label">Section ID</label>
                                      <select class="form-select" id="finalSectionSelect">
                                        <option value="">Select Section</option>
                                        <c:forEach items="${sections}" var="section">
                                          <option value="${section.sectionID}" ${section.sectionID == finalSectionID ? 'selected' : ''}>${section.sectionName}</option>
                                        </c:forEach>
                                      </select>
                                      <div class="invalid-feedback">Please select a section</div>
                                    </div>
                                    <div class="mb-3">
                                      <label for="finalBinSelect" class="form-label">Bin ID</label>
                                      <select class="form-select" id="finalBinSelect" disabled>
                                        <option value="">Select Bin</option>
                                      </select>
                                      <div class="invalid-feedback">Please select a bin</div>
                                    </div>
                                  </form>
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                  <button type="button" class="btn btn-primary" id="saveLocationChanges">Save Changes</button>
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

                        <c:if test="${transferOrder.status == 'Pending'}">
                          <div>
                            <button type="button" class="btn btn-danger me-2" data-bs-toggle="modal" data-bs-target="#cancelModal">
                              Cancel Order
                            </button>
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#completeModal">
                              Complete Order
                            </button>
                          </div>
                        </c:if>
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

<!-- Modal for editing Section ID -->

<script>
  document.addEventListener("DOMContentLoaded", function() {
    const contextPath = "${pageContext.request.contextPath}";
    const finalSectionSelect = document.getElementById("finalSectionSelect");
    const finalBinSelect = document.getElementById("finalBinSelect");
    const saveButton = document.getElementById("saveLocationChanges");

    // Pre-select the current section and enable bin selection immediately
    if (finalSectionSelect.value) {
      finalBinSelect.disabled = false;
      loadBins(finalSectionSelect.value, "${toDetails[0].finalBinID}");
    }

    // Add event listener for section selection
    finalSectionSelect.addEventListener("change", function() {
      if (this.value) {
        finalBinSelect.disabled = false;
        loadBins(this.value);
      } else {
        finalBinSelect.disabled = true;
        finalBinSelect.innerHTML = '<option value="">Select Bin</option>';
      }
    });

    // Function to load bins for a section
    function loadBins(sectionID, selectedBinID) {
      if (!sectionID) {
        finalBinSelect.innerHTML = '<option value="">Select Bin</option>';
        finalBinSelect.disabled = true;
        return;
      }

      // Create URL for the API call
      const url = new URL(contextPath + "/getBinsBySection", window.location.origin);
      url.searchParams.append("sectionID", sectionID);

      // Fetch bins for the selected section
      fetch(url.toString())
              .then(response => {
                if (!response.ok) {
                  throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
              })
              .then(data => {
                // Clear current options
                finalBinSelect.innerHTML = '<option value="">Select Bin</option>';

                if (data.success && data.bins && data.bins.length > 0) {
                  // Add new options
                  data.bins.forEach(bin => {
                    const option = document.createElement('option');
                    option.value = bin.id || bin.binID;
                    option.textContent = bin.name || bin.binName || bin.id || bin.binID;

                    // If there's a previously selected bin, select it
                    if (selectedBinID && (bin.id === selectedBinID || bin.binID === selectedBinID)) {
                      option.selected = true;
                    }

                    finalBinSelect.appendChild(option);
                  });
                } else {
                  finalBinSelect.innerHTML = '<option value="">No bins available</option>';
                }
              })
              .catch(error => {
                console.error("Error fetching bins:", error);
                finalBinSelect.innerHTML = '<option value="">Error loading bins</option>';
              });
    }

    // Handle save button click
    saveButton.addEventListener("click", function() {
      const newSectionID = finalSectionSelect.value;
      const newSectionText = finalSectionSelect.options[finalSectionSelect.selectedIndex].textContent;
      const newBinID = finalBinSelect.value;
      const newBinText = finalBinSelect.options[finalBinSelect.selectedIndex].textContent;

      // Validate selections
      let isValid = true;

      if (!newSectionID) {
        finalSectionSelect.classList.add("is-invalid");
        isValid = false;
      } else {
        finalSectionSelect.classList.remove("is-invalid");
      }

      if (!newBinID) {
        finalBinSelect.classList.add("is-invalid");
        isValid = false;
      } else {
        finalBinSelect.classList.remove("is-invalid");
      }

      if (!isValid) {
        return;
      }

      // Send update to server
      const transferOrderID = "${transferOrder.toID}";
      const url = contextPath + "/updateTransferOrderLocation";

      const formData = new FormData();
      formData.append("toID", transferOrderID);
      formData.append("finalSectionID", newSectionID);
      formData.append("finalBinID", newBinID);

      fetch(url, {
        method: "POST",
        body: formData
      })
              .then(response => response.json())
              .then(data => {
                if (data.success) {
                  // Update the displayed values
                  document.getElementById('finalSectionDisplay').textContent = newSectionText;
                  document.getElementById('finalBinDisplay').textContent = newBinText;

                  // Show success message
                  alert("Location updated successfully");
                } else {
                  alert("Failed to update location: " + (data.message || "Unknown error"));
                }
              })
              .catch(error => {
                console.error("Error updating location:", error);
                alert("Error updating location. See console for details.");
              })
              .finally(() => {
                // Close the modal
                bootstrap.Modal.getInstance(document.getElementById('combinedEditModal')).hide();
              });
    });

    // When the modal is about to be shown, ensure bins are loaded
    document.getElementById('combinedEditModal').addEventListener('show.bs.modal', function() {
      if (finalSectionSelect.value) {
        loadBins(finalSectionSelect.value, "${toDetails[0].finalBinID}");
      }
    });
  });
</script>
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