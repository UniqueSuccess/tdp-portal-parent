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

                },
                error:function(err){
                    console.log(err);

                }
            }]
        }
    });
</script>

</body>

</html>
