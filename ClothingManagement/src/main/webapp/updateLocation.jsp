<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Update Transfer Order Location</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card">
        <div class="card-header">
          <h3>Update Location for Transfer Order: ${transferOrder.toID}</h3>
        </div>

        <%-- Success Message Section --%>
        <c:if test="${not empty param.updateSuccess}">
          <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>Success!</strong> Transfer Order location updated successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Error!</strong> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </c:if>

        <div class="card-body">
          <form action="updateLocation" method="post">
            <input type="hidden" name="toID" value="${transferOrder.toID}">

            <div class="mb-3">
              <label for="finalSectionSelect" class="form-label">Section ID</label>
              <select class="form-select" id="finalSectionSelect" name="finalSectionID" required>
                <option value="">Select Section</option>
                <c:forEach items="${sections}" var="section">
                  <option value="${section.sectionID}"
                    ${section.sectionID == finalSectionID ? 'selected' : ''}>
                      ${section.sectionName}
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="mb-3">
              <label for="finalBinSelect" class="form-label">Bin ID</label>
              <select class="form-select" id="finalBinSelect" name="finalBinID" required disabled>
                <option value="">Select Bin</option>
              </select>
            </div>

            <div class="d-flex justify-content-between">
              <a href="TOUpdate?toID=${transferOrder.toID}" class="btn btn-secondary">Cancel</a>
              <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>


<script>
  document.addEventListener("DOMContentLoaded", function() {
    const contextPath = "${pageContext.request.contextPath}";
    const finalSectionSelect = document.getElementById("finalSectionSelect");
    const finalBinSelect = document.getElementById("finalBinSelect");

    // Pre-select the current section and enable bin selection immediately
    if (finalSectionSelect.value) {
      finalBinSelect.disabled = false;
      loadBins(finalSectionSelect.value, "${currentFinalBinID}");
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
  });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>