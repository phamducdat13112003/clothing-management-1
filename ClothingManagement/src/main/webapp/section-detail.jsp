<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Section Details</title>
  <!-- Include your CSS files here -->
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="sherah-body-area">
  <div class="container">
    <div class="row">
      <div class="col-12">
        <div class="sherah-breadcrumb">
          <h2>Section Details</h2>
          <ul>
            <li><a href="dashboard.jsp">Home</a></li>
            <li><a href="section?action=list">Sections</a></li>
            <li>Section Details</li>
          </ul>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-12">
        <div class="sherah-card">
          <div class="sherah-card-header">
            <h4>Section Information</h4>
          </div>
          <div class="sherah-card-body">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Section ID</label>
                  <div class="form-control-static">${section.sectionID}</div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Section Name</label>
                  <div class="form-control-static">${section.sectionName}</div>
                </div>
              </div>
            </div>

            <div class="row mt-3">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Section Type</label>
                  <div class="form-control-static">${sectionType.sectionTypeName}</div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Description</label>
                  <div class="form-control-static">${sectionType.description}</div>
                </div>
              </div>
            </div>

            <!-- Bins associated with this section, if any -->
            <div class="sherah-card-header mt-4 d-flex justify-content-between align-items-center">
              <h4>Bins in this Section</h4>
              <a href="addBin?sectionID=${section.sectionID}" class="btn btn-success btn-sm">
                <i class="fas fa-plus-circle"></i> Add New Bin
              </a>
            </div>

            <c:if test="${not empty bins}">
              <div class="table-responsive">
                <table class="table sherah-table">
                  <thead>
                  <tr>
                    <th>Bin ID</th>
                    <th>Bin Name</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="bin" items="${bins}">
                    <tr>
                      <td>${bin.binID}</td>
                      <td>${bin.binName}</td>
                      <td>
                        <c:choose>
                          <c:when test="${bin.status == 'true'}">
                            <span class="badge bg-success">Unlocked</span>
                          </c:when>
                          <c:when test="${bin.status == 'false'}">
                            <span class="badge bg-danger">Locked</span>
                          </c:when>
                          <c:otherwise>
                            <span class="badge bg-warning">${bin.status}</span>
                          </c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <a href="viewBin?binID=${bin.binID}" class="btn btn-sm btn-info">View</a>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:if>

            <c:if test="${empty bins}">
              <div class="alert alert-info mt-3">
                <p class="mb-0">No bins found in this section.</p>
                <button type="button" class="btn btn-outline-success mt-2" data-bs-toggle="modal" data-bs-target="#addBinModal">
                  <i class="fas fa-plus-circle"></i> Add your first bin
                </button>
              </div>
            </c:if>

            <div class="form-group mt-4">
              <a href="section?action=showEdit&id=${section.sectionID}" class="btn btn-primary">Edit Section</a>
              <a href="section?action=list" class="btn btn-secondary">Back to List</a>
              <a href="#" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete Section</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

      </form>
    </div>
  </div>
</div>



<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>