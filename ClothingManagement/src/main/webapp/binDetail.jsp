<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bin Details</title>
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
                    <h2>Bin Details</h2>
                    <ul>
                        <li><a href="dashboard.jsp">Home</a></li>
                        <li><a href="section?action=list">Sections</a></li>
                        <li><a href="viewSection?id=${bin.sectionID}">Section Details</a></li>
                        <li>Bin Details</li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="sherah-card">
                    <div class="sherah-card-header">
                        <h4>Bin Information: ${bin.binName}</h4>
                    </div>
                    <div class="sherah-card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Bin ID</label>
                                    <div class="form-control-static">${bin.binID}</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Bin Name</label>
                                    <div class="form-control-static">${bin.binName}</div>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Status</label>
                                    <div class="form-control-static">
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
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Section</label>
                                    <div class="form-control-static">${section.sectionName}</div>
                                </div>
                            </div>
                        </div>

                        <%
                            ServletContext context = application;
                            String binID = request.getParameter("binID");

                            // Check for success message
                            String successMessage = (String) context.getAttribute("successMessage_" + binID);
                            if (successMessage != null && !successMessage.isEmpty()) {
                        %>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <%= successMessage %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <%
                                // Clear the message after displaying it
                                context.removeAttribute("successMessage_" + binID);
                            }

                            // Check for error message
                            String errorMessage = (String) context.getAttribute("errorMessage_" + binID);
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                        %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <%= errorMessage %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <%
                                // Clear the message after displaying it
                                context.removeAttribute("errorMessage_" + binID);
                            }
                        %>

                        <!-- Filter section -->
                        <div class="sherah-card-header mt-4">
                            <h4>Filter Products</h4>
                        </div>
                        <form action="viewBin" method="get" class="filter-form mt-3">
                            <input type="hidden" name="binID" value="${bin.binID}">

                            <div class="row">
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="search" class="form-label">Search:</label>
                                        <input type="text" id="search" name="search" value="${search}"
                                               class="form-control">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="material" class="form-label">Material:</label>
                                        <select id="material" name="material" class="form-control">
                                            <option value="">All Materials</option>
                                            <c:forEach var="mat" items="${allMaterials}">
                                                <option value="${mat}" ${material eq mat ? 'selected' : ''}>${mat}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="season" class="form-label">Season:</label>
                                        <select id="season" name="season" class="form-control">
                                            <option value="">All Seasons</option>
                                            <c:forEach var="s" items="${allSeasons}">
                                                <option value="${s}" ${season eq s ? 'selected' : ''}>${s}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="madeIn" class="form-label">Made In:</label>
                                        <select id="madeIn" name="madeIn" class="form-control">
                                            <option value="">All Countries</option>
                                            <c:forEach var="country" items="${allMadeIn}">
                                                <option value="${country}" ${madeIn eq country ? 'selected' : ''}>${country}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group mt-3">
                                <button type="submit" class="btn btn-primary">Apply Filters</button>
                                <a href="viewBin?binID=${bin.binID}" class="btn btn-secondary">Clear Filters</a>
                            </div>
                        </form>

                        <!-- Products in this bin -->
                        <div class="sherah-card-header mt-4 d-flex justify-content-between align-items-center">
                            <h4>Products in this Bin</h4>
                            <a href="addProductToBin?binID=${bin.binID}" class="btn btn-success btn-sm">
                                <i class="fas fa-plus-circle"></i> Add Product
                            </a>
                        </div>

                        <c:if test="${not empty binDetails}">
                            <div class="table-responsive">
                                <table class="table sherah-table">
                                    <thead>
                                    <tr>
                                        <th>Product ID</th>
                                        <th>Product Name</th>
                                        <th>Quantity</th>
                                        <th>Color</th>
                                        <th>Size</th>
                                        <th>Material</th>
                                        <th>Season</th>
                                        <th>Made In</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="detail" items="${binDetails}">
                                        <tr>
                                            <td>${detail.id}</td>
                                            <td>${detail.product.name}</td>
                                            <td>${detail.quantity}</td>
                                            <td>${detail.color}</td>
                                            <td>${detail.size}</td>
                                            <td>${detail.product.material}</td>
                                            <td>${detail.product.seasons}</td>
                                            <td>${detail.product.madeIn}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination controls -->
                            <c:if test="${totalPages > 1}">
                                <div class="sherah-pagination">
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="viewBin?binID=${bin.binID}&page=${i}&search=${search}&material=${material}&season=${season}&madeIn=${madeIn}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:if>
                        </c:if>

                        <c:if test="${empty binDetails}">
                            <div class="alert alert-info mt-3">
                                <p class="mb-0">No products found matching the criteria.</p>
                            </div>
                        </c:if>

                        <div class="form-group mt-4">
                            <a href="viewSection?id=${bin.sectionID}" class="btn btn-secondary">Back to Section</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Delete Bin Modal -->
<div class="modal fade" id="deleteBinModal" tabindex="-1" aria-labelledby="deleteBinModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteBinModalLabel">Confirm Delete</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this bin? This action cannot be undone.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <form action="bin" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="binID" value="${bin.binID}">
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addProductModalLabel">Add Product</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <a href="addProductToBin?binID=${bin.binID}" class="btn btn-primary w-100">Proceed to Add Product</a>
            </div>
        </div>
    </div>
</div>

<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>