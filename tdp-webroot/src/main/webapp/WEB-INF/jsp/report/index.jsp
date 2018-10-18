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
    <link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/magnify/jquery.magnify.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/report/index.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="top-bar">
        <div class="top-title">视频流转日志</div>
        <div class="bar-item-box">
            <div class="bar-item bar-item-dropdown dropdown-tree-box">
                <input id="bar_dept" class="dropdown-input cursor-pointer" type="text" value="" readonly placeholder="请选择部门">
                <ul id="department_tree" class="dropdown-tree ztree"></ul>
            </div>
            <div class="bar-item bar-item-search">
                <select name="" id="alltype" class="selectwidth  w120">
                    <option value=" ">全部</option>
                    <option value="OUTCFG">视频外发</option>
                    <option value="OPT">视频导出</option>
                </select>
            </div>
            <div class="bar-item bar-item-search wind-content datatimechoose">
                <!--<input type="text" class="wind-content-input wind-content-input-date valid" readonly="" aria-invalid="false" id="timechange">-->
                <input type="text" class="inputtime wind-content-input wind-content-input-date" readonly
                       placeholder="请选择时间" value="最近7天">
                <ul class="datachange" style="display: none;">
                    <li class="daychoose" data-type="day">今天</li>
                    <li class="weekchoose" data-type="week">最近7天</li>
                    <li class="allchoose" data-type="all">全部</li>
                    <li id="reservation" data-type="month">时间范围</li>
                </ul>
            </div>
            <div class="bar-item bar-item-search">
                <input id="bar_searchstr" type="text" placeholder="用户名/真实姓名/文件名/接收方">
                <i id="bar_searchstr_icon" class="iconfont icon-btn-search1 text-lg"></i>
            </div>
        </div>
    </div>
    <div class="content-box">
        <div class="cell-top">
            <div id="exportReport"></div>
        </div>
        <div style="clear: both;"></div>
        <div class="cell-bottom">
            <div class="" id="exportList">
                <table id="exportListTable" width="100%" cellSpacing="0" cellPadding="0" border="0">
                    <thead>
                    <tr>
                        <th>部门</th>
                        <th>用户名</th>
                        <th>真实姓名</th>
                        <th>文件名</th>
                        <th>接收方</th>
                        <th>流转类型</th>
                        <th>时间</th>
                        <th style="text-align:center;">图片摘要</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="${ctxJs}/plugins/echarts/echarts.common.min.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
<!--<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js" type="text/javascript"></script>-->
<!--<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>-->
<script src="${ctxJs}/plugins/datarangepicker/moment.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/datarangepicker/daterangepicker.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/magnify/jquery.magnify.js" type="text/javascript"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/report/index.js"></script>
<script>
    var ctx = "${ctx}";


</script>