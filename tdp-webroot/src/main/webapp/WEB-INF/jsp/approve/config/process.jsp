<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title></title>
    <%--<link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>
</head>
<div id="approve" class="" v-cloak style="height: calc(100% - 37px)">
    <gd-toolbar :config="toolbarConfig"></gd-toolbar>
    <gd-table :config="tableConfig"></gd-table>
</div>
<%--<div class="processcon">
    <div class="processtop">
        <div class="left">
            <div class="beauty-checkbox">
                <input id="onlyProcess" name="onlyProcess" type="checkbox" class="" value="1" checked>
                <label for="onlyProcess" class="checkbox-icon"></label>
            </div>
            <span>我的待办</span>
        </div>
        <div class="right bar-item-box">
            <div class="bar-item bar-item-search wind-content datatimechoose_pro datatimechoose">
                <input type="text" class="inputtime wind-content-input wind-content-input-date" placeholder="请选择时间" value="全部">
                <ul class="datachange datachange_pro" style="display: none;">
                    <li class="daychoose" data-type="day">今天</li>
                    <li class="weekchoose" data-type="week">最近7天</li>
                    <li class="allchoose" data-type="all">全部</li>
                    <li id="reservation" data-type="month">请选择时间段</li>
                </ul>
            </div>
            <div class="bar-item bar-item-search">
                <input id="bar_searchstr" type="text" placeholder="提交人">
                <i id="bar_searchstr_icon" class="iconfont icon-btn-search1 text-lg"></i>
            </div>
        </div>

    </div>
    <div class="processshow">
        <table id="processTable" cellspacing="0" cellpadding="0" border="0" width="100%">
            <thead>
            <tr>
                <th>流程名称</th>
                <th>审批类型</th>
                <th>提交人</th>
                <th>当前环节</th>
                <th>提交时间</th>
                <th style="text-align:center;">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>--%>


<%--<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>--%>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/approve/config/process.js"></script>
<script>
    var ctx = "${ctx}";


</script>