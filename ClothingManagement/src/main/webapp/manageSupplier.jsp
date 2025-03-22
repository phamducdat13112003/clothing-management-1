<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 2/27/2025
  Time: 8:02 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <title>Manage Supplier</title>

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
        .error-message {
            color: red;
            font-size: 12px;
        }

        .message {
            color: green;
            font-size: 12px;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .pagination a {
            padding: 8px 12px;
            margin: 0 5px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
        }

        .pagination a:active {
            background-color: #09ad95;
            color: white;
            font-weight: bold;
        }

        .pagination a:hover {
            background-color: #ddd;
        }

        .search-form {
            display: flex;
            justify-content: flex-end;
            align-items: center;
        }

        .search-input {
            padding: 6px 12px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 5px;
            width: 250px;
        }

        .search-button {
            padding: 5px 15px;
            background-color: #09ad95;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .search-button:hover {
            background-color: #078c76;
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
                    <!-- Dashboard Inner -->
                    <div class="sherah-dsinner">
                        <div class="row align-items-center justify-content-between">
                            <div class="col-6">
                                <div class="sherah-breadcrumb mg-top-30">
                                    <h2 class="sherah-breadcrumb__title">Manage Supplier</h2>
                                    <ul class="sherah-breadcrumb__list">
                                        <li><a href="managesupplier">Home</a></li>
                                        <li class="active"><a href="addsupplier">Add Supplier</a></li>
                                    </ul>
                                    <c:if test="${not empty message}">
                                        <span class="error-message">${message}</span>
                                    </c:if>
                                    <c:if test="${not empty messageSuccess}">
                                        <span class="message">${messageSuccess}</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="col-6">
                                <form action="searchsupplier" method="post" class="search-form">
                                    <input type="text" name="search" placeholder="Search..." value="${search}"
                                           class="search-input">
                                    <button type="submit" class="search-button">Search</button>
                                </form>
                            </div>
                        </div>

                        <div class="sherah-table sherah-page-inner sherah-border sherah-default-bg mg-top-25">
                            <table id="sherah-table__vendor" class="sherah-table__main sherah-table__main-v3">
                                <!-- sherah Table Head -->
                                <thead class="sherah-table__head">
                                <tr>
                                    <th class="sherah-table__column-2 sherah-table__h2">SupplierID</th>
                                    <th class="sherah-table__column-3 sherah-table__h3">SupplierName</th>
                                    <th class="sherah-table__column-7 sherah-table__h4">Address</th>
                                    <th class="sherah-table__column-4 sherah-table__h4">Email</th>
                                    <th class="sherah-table__column-4 sherah-table__h4">Phone</th>
                                    <th class="sherah-table__column-2 sherah-table__h4">Status</th>
                                    <th class="sherah-table__column-2 sherah-table__h8" style="text-align: center;">
                                        Action
                                    </th>
                                </tr>
                                </thead>
                                <tbody class="sherah-table__body">
                                <c:forEach items="${list}" var="supplier">
                                    <tr>
                                        <td class="sherah-table__column-2 sherah-table__data-2">
                                            <div class="sherah-table__product-content">
                                                <p class="sherah-table__product-desc">${supplier.supplierId}</p>
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-3 sherah-table__data-3">
                                            <div class="sherah-table__product-content">
                                                    ${supplier.supplierName}
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-7 sherah-table__data-2">
                                            <div class="sherah-table__product-content">
                                                <p class="sherah-table__product-desc">${supplier.address}</p>
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-2 sherah-table__data-2">
                                            <div class="sherah-table__product-content">
                                                <p class="sherah-table__product-desc">${supplier.email}</p>
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-2 sherah-table__data-2">
                                            <div class="sherah-table__product-content">
                                                <p class="sherah-table__product-desc">${supplier.phone}</p>
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-2 sherah-table__data-2">
                                            <div class="sherah-table__product-content">
                                                <p class="sherah-table__product-desc">${supplier.status ? 'Active' : 'Inactive'}</p>
                                            </div>
                                        </td>
                                        <td class="sherah-table__column-4 sherah-table__data-7"
                                            style="text-align: center;">
                                            <div class="sherah-table__status__group"
                                                 style="display: inline-flex; gap: 10px;">
                                                <a href="editsupplier?supplierId=${supplier.supplierId}"
                                                   class="sherah-table__action sherah-color2 sherah-color3__bg--opactity">
                                                    <svg class="sherah-color3__fill" xmlns="http://www.w3.org/2000/svg"
                                                         width="18.29" height="18.252" viewBox="0 0 18.29 18.252">
                                                        <g id="Group_132" data-name="Group 132"
                                                           transform="translate(-234.958 -37.876)">
                                                            <path id="Path_481" data-name="Path 481"
                                                                  d="M242.545,95.779h-5.319a2.219,2.219,0,0,1-2.262-2.252c-.009-1.809,0-3.617,0-5.426q0-2.552,0-5.1a2.3,2.3,0,0,1,2.419-2.419q2.909,0,5.818,0c.531,0,.87.274.9.715a.741.741,0,0,1-.693.8c-.3.026-.594.014-.892.014q-2.534,0-5.069,0c-.7,0-.964.266-.964.976q0,5.122,0,10.245c0,.687.266.955.946.955q5.158,0,10.316,0c.665,0,.926-.265.926-.934q0-2.909,0-5.818a.765.765,0,0,1,.791-.853.744.744,0,0,1,.724.808c.007,1.023,0,2.047,0,3.07s.012,2.023-.006,3.034A2.235,2.235,0,0,1,248.5,95.73a1.83,1.83,0,0,1-.458.048Q245.293,95.782,242.545,95.779Z"
                                                                  transform="translate(0 -39.652)" fill="#09ad95"/>
                                                            <path id="Path_482" data-name="Path 482"
                                                                  d="M332.715,72.644l2.678,2.677c-.05.054-.119.133-.194.207q-2.814,2.815-5.634,5.625a1.113,1.113,0,0,1-.512.284c-.788.177-1.582.331-2.376.48-.5.093-.664-.092-.564-.589.157-.781.306-1.563.473-2.341a.911.911,0,0,1,.209-.437q2.918-2.938,5.853-5.86A.334.334,0,0,1,332.715,72.644Z"
                                                                  transform="translate(-84.622 -32.286)"
                                                                  fill="#09ad95"/>
                                                            <path id="Path_483" data-name="Path 483"
                                                                  d="M433.709,42.165l-2.716-2.715a15.815,15.815,0,0,1,1.356-1.248,1.886,1.886,0,0,1,2.579,2.662A17.589,17.589,0,0,1,433.709,42.165Z"
                                                                  transform="translate(-182.038)" fill="#09ad95"/>
                                                        </g>
                                                    </svg>
                                                </a>
                                                <a href="javascript:void(0);"
                                                   onclick="confirmDelete('${supplier.supplierId}')"
                                                   class="sherah-table__action sherah-color2 sherah-color2__bg--offset">
                                                    <svg class="sherah-color2__fill" xmlns="http://www.w3.org/2000/svg"
                                                         width="16.247" height="18.252" viewBox="0 0 16.247 18.252">
                                                        <g id="Icon" transform="translate(-160.007 -18.718)">
                                                            <path id="Path_484" data-name="Path 484"
                                                                  d="M185.344,88.136c0,1.393,0,2.786,0,4.179-.006,1.909-1.523,3.244-3.694,3.248q-3.623.007-7.246,0c-2.15,0-3.682-1.338-3.687-3.216q-.01-4.349,0-8.7a.828.828,0,0,1,.822-.926.871.871,0,0,1,1,.737c.016.162.006.326.006.489q0,4.161,0,8.321c0,1.061.711,1.689,1.912,1.69q3.58,0,7.161,0c1.2,0,1.906-.631,1.906-1.695q0-4.311,0-8.622a.841.841,0,0,1,.708-.907.871.871,0,0,1,1.113.844C185.349,85.1,185.343,86.618,185.344,88.136Z"
                                                                  transform="translate(-9.898 -58.597)"/>
                                                            <path id="Path_485" data-name="Path 485"
                                                                  d="M164.512,21.131c0-.517,0-.98,0-1.443.006-.675.327-.966,1.08-.967q2.537,0,5.074,0c.755,0,1.074.291,1.082.966.005.439.005.878.009,1.317a.615.615,0,0,0,.047.126h.428c1,0,2,0,3,0,.621,0,1.013.313,1.019.788s-.4.812-1.04.813q-7.083,0-14.165,0c-.635,0-1.046-.327-1.041-.811s.4-.786,1.018-.789C162.165,21.127,163.3,21.131,164.512,21.131Zm1.839-.021H169.9v-.764h-3.551Z"
                                                                  transform="translate(0 0)"/>
                                                            <path id="Path_486" data-name="Path 486"
                                                                  d="M225.582,107.622c0,.9,0,1.806,0,2.709a.806.806,0,0,1-.787.908.818.818,0,0,1-.814-.924q0-2.69,0-5.38a.82.82,0,0,1,.81-.927.805.805,0,0,1,.79.9C225.585,105.816,225.582,106.719,225.582,107.622Z"
                                                                  transform="translate(-58.483 -78.508)"/>
                                                            <path id="Path_487" data-name="Path 487"
                                                                  d="M266.724,107.63c0-.9,0-1.806,0-2.709a.806.806,0,0,1,.782-.912.818.818,0,0,1,.818.919q0,2.69,0,5.38a.822.822,0,0,1-.806.931c-.488,0-.792-.356-.794-.938C266.721,109.411,266.724,108.521,266.724,107.63Z"
                                                                  transform="translate(-97.561 -78.509)"/>
                                                        </g>
                                                    </svg>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="managesupplier?page=${currentPage - 1}&search=${search}">Previous</a>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <a href="managesupplier?page=${i}&search=${search}"
                                       class="${i == currentPage ? 'active' : ''}">${i}</a>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <a href="managesupplier?page=${currentPage + 1}&search=${search}">Next</a>
                                </c:if>
                            </div>

                        </div>
                    </div>
                    <!-- End Dashboard Inner -->
                </div>
            </div>
        </div>
    </div>
</section>
<!-- End sherah Dashboard -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-migrate.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery-jvectormap.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/charts.js"></script>
<script src="js/final-countdown.min.js"></script>
<script src="js/fancy-box.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/datatables.min.js"></script>
<script src="js/circle-progress.min.js"></script>
<script src="js/jvector-map.js"></script>
<script src="js/main.js"></script>
<script type="text/javascript">
    function confirmDelete(supplierId) {
        if (confirm("Are you sure want to delete this supplier?")) {
            window.location = "deletesupplier?supplierId=" + supplierId;
        }
    }
</script>
</body>
</html>

