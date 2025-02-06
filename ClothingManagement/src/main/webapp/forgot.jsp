<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section class="sherah-wc sherah-wc__full sherah-bg-cover" style="background-image: url('img/credential-bg.svg');">
    <div class="container-fluid p-0">
        <div class="row g-0">
            <div class="col-lg-6 col-md-6 col-12 sherah-wc-col-one">
                <div class="sherah-wc__inner" style="background-image: url('img/welcome-bg.png');">
                    <div class="sherah-wc__logo">
                        <a href=""><img src="img/logo.png" alt="#"></a>
                    </div>
                    <h2 class="sherah-wc__title">Forgot Password</h2>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-12 sherah-wc-col-two">
                <div class="sherah-wc__form">
                    <div class="sherah-wc__form-inner">
                        <h3 class="sherah-wc__form-title">Enter your email to reset your password</h3>
                        <!-- Forgot Password Form -->
                        <form class="sherah-wc__form-main" action="forgotpassword" method="post">
                            <c:if test="${requestScope.check == null}"></c:if>
                            <c:if test="${requestScope.check != null}">
                                <c:if test="${requestScope.check == 'true' && !(requestScope.message == 'Xin lỗi, mã đặt lại không chính xác')}">
                                    <p style="color: green">${requestScope.message}</p>
                                </c:if>
                                <c:if test="${requestScope.check == 'false'}">
                                    <p style="color: red">${requestScope.message}</p>
                                </c:if>
                                <c:if test="${requestScope.check == 'true' && requestScope.message == 'Xin lỗi, mã đặt lại không chính xác'}">
                                    <p style="color: red">${requestScope.message}</p>
                                </c:if>
                            </c:if>
                            <c:if test="${requestScope.check == null || requestScope.check == 'false'}">
                            <div class="form-group">
                                <label class="sherah-wc__form-label">Email Address</label>
                                <div class="form-group__input">
                                    <input class="sherah-wc__form-input" type="email" name="email" placeholder="Enter your email" required value="${param.email}">
                                </div>
                            </div>
                            </c:if>
                            <c:if test="${requestScope.check == null || requestScope.check == 'false'}">
                            <div class="form-group form-mg-top25">
                                <div class="sherah-wc__button sherah-wc__button--bottom">
                                    <button class="ntfmax-wc__btn" type="submit">Send to Mail</button>
                                </div>
                            </div>
                            </c:if>
                        </form>
                        <!-- Reset Code Form (conditionally displayed) -->
                        <c:if test="${requestScope.check != null && requestScope.check == 'true'}">
                            <form class="sherah-wc__form-main" action="confirmresetcode" method="post">
                                <input name="email" value="${requestScope.email}" type="hidden">
                                <div class="form-group">
                                    <label class="sherah-wc__form-label">Reset Code</label>
                                    <div class="form-group__input">
                                        <input class="sherah-wc__form-input" type="text" name="resetcode" placeholder="Enter the reset code" required value="${requestScope.code}">
                                    </div>
                                </div>
                            <c:if test="${requestScope.check != null && requestScope.check == 'true'}">
                                <div class="form-group form-mg-top25">
                                    <div class="sherah-wc__button sherah-wc__button--bottom">
                                        <button class="ntfmax-wc__btn" type="submit">Confirm Reset Code</button>
                                    </div>
                                </div>
                            </c:if>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>
