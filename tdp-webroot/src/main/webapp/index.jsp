<!DOCTYPE html>
<html lang="en" class="no-js">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>金盾tdp</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <c:set var="ctxJs" value="${pageContext.request.contextPath}/js"/>
    <c:set var="ctxCss" value="${pageContext.request.contextPath}/skin/default/css"/>
    <c:set var="ctxImg" value="${pageContext.request.contextPath}/skin/default/images"/>
    <c:set var="version" value="1"/>
    <link rel="icon" href="${ctxImg}/logo.ico" type="image/x-ico"/>
    <link rel="stylesheet" href="${ctxJs}/plugins/gdui/css/gdui.min.css?v=${version}" />
    <script type="text/javascript" src="${ctxJs}/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/vue.min.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gdui.min.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/sha/sha256.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/sha/encrypt.js?v=${version}"></script>

    <style>
        /*------登录页面的背景图-------*/
        .gd-login .gd-login-cover-box img {
            -webkit-transform: perspective(2000px) rotateX(72deg) translateX(-50%)!important;
            transform: perspective(2000px) rotateX(72deg) translateX(-50%)!important;
        }
        /* .gd-login .gd-login-cover-box .gd-login-cover-item {
            background:url("${ctxImg}/login/login_bg.png");
            background-size: cover;
        } */

    </style>
    <script>
        var ctx = "${ctx}";
        var ctxJs = "${ctxJs}";
        var ctxImg = "${ctxImg}";
        var _csrf = "${_csrf.parameterName}";
    </script>

    <%--<link rel="stylesheet" href="${ctxCss}/animate/animate.css">
    <link href="${ctxCss}/login/login.css" rel="stylesheet">
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${ctxJs}/html5.js"></script>
    <![endif]-->
    <script type="text/javascript" src="${ctxJs}/plugins/sha/sha256.js"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/sha/encrypt.js"></script>
    <script type="text/javascript" src="${ctxJs}/login/login.js"></script>--%>
</head>

<body>


<div id="app" v-cloak>
    <gd-login :config="loginConfig"></gd-login>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" class="csrf_name"/>
    <%--<object id="vskctrl" name="vskctrl" classid="clsid:1D43F2A0-DDDB-4777-A602-6FB49E676C1D"
            width="0" height="0">
    </object>--%>
    <%--<form id="loginForm" name='loginForm' action="${ctx}/login" method='POST' hidden>
        <div class="form-group">
            <div class="form-group-in-user">
                <input id="name" name="username" type="text" class="form-control in" autocomplete="off">
            </div>
        </div>
        <div class="form-group">
            <div class="form-group-in-pass">
                <input id="password" name="password" type="password" class="password form-control in">
            </div>
        </div>
        <div class="login">
            <div id="submit_btn" class="login_button">
                <span id="loginsubmitword"></span>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>--%>
</div>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            loginConfig: [{
                name: 'tdp',
                api: ctx+'/login',
                logo: ctxImg + '/login/login_ra.png',
                cover: ctxImg + '/login/tsa_cover.jpg',
                href: ctx+'/homepage/index?navId=1',
                encrypt: function (data) {
                    data.password = encrypt(data.password).toUpperCase();
                    data[_csrf] = $(".csrf_name").val();
                    return data;
                },
                success: function (data) {
                    console.log(data);
                    /*if(data.resultCode == 0){
                        $("body #name").val($("body .gd-login-user").val());
                        $("body #password").val(encrypt($("body .gd-login-password").val()).toUpperCase());
                        console.log($("body #name").val())
                        console.log($("body #password").val())

//                        $("body #loginForm").submit();
                    }else{

                    }*/

                },
                error:function(err){
                    console.log(err);

                }
            }]
        }
    });
</script>






<%--
<div id="page-container">

    </div>
<div class="main animated fadeIn">
    <div class="login_logo">
        <img src="${ctxImg}/Logo.png">
    </div>
    <div class="login_box">
        <div class="login_form">
            <form id="loginForm" name='loginForm' action="${ctx}/login" method='POST'>
                <div class="form-group">
                    <div class="form-group-in-user">
                        <input id="name" value="" name="username" type="text" class="form-control in" autocomplete="off">
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-group-in-pass">
                        <input id="password" value="" onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false"  name="password" type="password" class="password form-control in">
                        <div class="capslock" id="capital_password">
                            <span class="caret"></span>
                            <span>大小写锁定已打开</span>
                        </div>
                    </div>
                </div>
                <c:if test="${SEC_CODE_FLAG == true}">
                    <div class="form-group">
                        <div class="form-group-in-code">
                            <input value="" name="sec_code_parameter" placeholder="验证码" type="text" class="code form-control in">
                            <img id="img_code" src="${ctx}/getCode" data-src="${ctx}/getCode" class="img-code">
                        </div>
                    </div>
                </c:if>
                <div class="login">
                    <label class="t"></label>
                    <div id="submit_btn" class="login_button">
                        <span id="loginsubmitword">&nbsp;登&nbsp;录&nbsp;</span>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="errorMessage" id="errorMessage">
                    ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message} ${error}
                </div>
            </form>
        </div>
    </div>

</div>
--%>

    <!-- Javascript -->
    <script src="${ctxJs}/judgeIe.js"></script>
</body>

</html>
<script>
//     判断浏览器版本是否过低
  if(BrowserType()){
    window.location.href = "${ctx}/downloadBrowserPage";
  }
</script>