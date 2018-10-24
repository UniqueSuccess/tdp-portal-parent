<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title>金盾VDP</title>
    <%--<link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>--%>
    <link rel="stylesheet" href="${ctxCss}/font_icon/iconfont.css?v=${version}" />
    <script src="${ctxJs}/jquery-2.2.1.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gd_iecheck.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/vue.min.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gdui.min.js?v=${version}"></script>
    <link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctxJs}/plugins/gdui/css/gdui.min.css?v=${version}" />
    <script>
        var ctx = "${ctx}";
        var applicantOrType = "${applicantOrType}";


    </script>
</head>
<div id="client_approve" class="" v-cloak style="height: calc(100%)">
    <%--<gd-toolbar :config="toolbarConfig"></gd-toolbar>--%>
    <gd-table :config="tableConfig"></gd-table>
</div>

<%--<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>--%>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/approve/client/index.js"></script>
