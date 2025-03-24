<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Bin</title>
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
                    <h2>Add New Bin</h2>
                    <ul>
                        <li><a href="dashboard.jsp">Home</a></li>
                        <li><a href="section?action=list">Sections</a></li>
                        <li><a href="section?action=view&id=${param.sectionID}">Section Details</a></li>
                        <li>Add New Bin</li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="sherah-card">
                    <div class="sherah-card-header">
                        <h4>Add New Bin to Section: ${sectionName}</h4>
                    </div>
                    <div class="sherah-card-body">
                        <form action="bin?action=add" method="post">
                            <input type="hidden" name="sectionID" value="${param.sectionID}">

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="nextBinID" class="form-label">Bin ID</label>
                                        <input type="text" id="nextBinID" name="nextBinID" class="form-control" value="${nextBinID}" required>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="maxCapacity" class="form-label">Max Capacity</label>
                                        <input type="text" id="maxCapacity" name="maxCapacity" class="form-control" value="100.00 kg" readonly>
                                        <input type="hidden" name="maxCapacity" value="100.00">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="binName" class="form-label">Bin Name</label>
                                        <input type="text" id="binName" name="binName" class="form-control" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="status" class="form-label">Status</label>
                                    <select id="status" name="status" class="form-control">
                                        <option value="true" selected>Unlock</option>
                                        <option value="false">Lock</option>
                                    </select>
                                    <small class="form-text text-muted">Status will be set to unlock by default</small>
                                </div>
                                <!-- This field will be hidden since binID will be generated automatically -->
                                <input type="hidden" id="binID" name="binID" value="">
                            </div>

                            <div class="form-group mt-4">
                                <button type="submit" class="btn btn-primary">Save Bin</button>
                                <a href="section?action=view&id=${param.sectionID}" class="btn btn-secondary">Cancel</a>
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