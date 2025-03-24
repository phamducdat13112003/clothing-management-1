<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Bin</title>
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
                    <h2>Update Bin</h2>
                    <ul>
                        <li><a href="dashboard.jsp">Home</a></li>
                        <li><a href="section?action=list">Sections</a></li>
                        <li><a href="viewSection?sectionID=${section.sectionID}">Section Details</a></li>
                        <li>Update Bin</li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="sherah-card">
                    <div class="sherah-card-header">
                        <h4>Update Bin Information</h4>
                    </div>
                    <%
                        ServletContext context = application;
                        String binID = request.getParameter("binID");
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
                    <div class="sherah-card-body">
                        <form action="updateBin" method="post">
                            <input type="hidden" name="binID" value="${bin.binID}">
                            <input type="hidden" name="sectionID" value="${section.sectionID}">

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Bin ID</label>
                                        <div class="form-control-static">${bin.binID}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Section</label>
                                        <div class="form-control-static">${section.sectionName}</div>
                                    </div>
                                </div>
                            </div>

                            <div class="row mt-3">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label" for="binName">Bin Name</label>
                                        <input type="text" class="form-control" id="binName" name="binName" value="${bin.binName}" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Status</label>
                                        <select class="form-select" name="status">
                                            <option value="true" ${bin.status ? 'selected' : ''}>Unlocked</option>
                                            <option value="false" ${!bin.status ? 'selected' : ''}>Locked</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group mt-4">
                                <button type="submit" class="btn btn-primary">Update Bin</button>
                                <a href="viewSection?sectionID=${section.sectionID}" class="btn btn-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>