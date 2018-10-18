<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/system/log/index.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="top-bar">
        <div class="top-title">系统日志</div>
    </div>
    <div class="content-box">
        <ul class="titleTab" id="titleTabul">
            <li class="logoset titleTabactive" data-class="logo">登录日志</li>
            <li class="operset" data-class="oper">操作日志</li>
            <c:forEach items="${modules}" var="module">
                <c:choose>
                    <c:when test="${module == 'clientLog'}">
                        <li class="clientset" data-class="client">客户端日志</li>
                    </c:when>
                </c:choose>
            </c:forEach>
        </ul>
        <div class="main-contentall">


        </div>
    </div>
</div>
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/datarangepicker/moment.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/datarangepicker/daterangepicker.js" type="text/javascript"></script>
<script>
    var ctx = "${ctx}";
    $(".main-contentall").load(ctx + '/systemLog/listPage?logType=logo');
    $("body")
    //tab页导航
        .on("click", ".titleTab li", function () {
            $(this).addClass("titleTabactive").siblings("li").removeClass("titleTabactive");
            var classcon = $(this).data("class");
            $(".main-contentall").load(ctx + '/systemLog/listPage?logType=' + classcon);
        })
</script>