<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/system/log/index.css" rel="stylesheet" type="text/css"/>
</head>
<div class="clientcon">
    <div class="processtop">
        <%--<div class="left">--%>
            <%----%>
        <%--</div>--%>
        <div class="right bar-item-box">
            <div class="bar-item bar-item-search wind-content datatimechoose_client datatimechoose">
                <input type="text" class="inputtime_client wind-content-input wind-content-input-date" placeholder="请选择时间" value="今天">
                <ul class="datachange_client datachange" style="display: none;">
                    <li class="daychoose" data-type="day">今天</li>
                    <li class="weekchoose" data-type="week">最近7天</li>
                    <li id="reservation_client" data-type="month">时间范围</li>
                </ul>
            </div>
            <div class="bar-item bar-item-search">
                <input id="bar_searchstr_client" type="text" placeholder="用户名">
                <i id="bar_searchstr_icon_client" class="iconfont icon-btn-search1 text-lg"></i>
            </div>
            <a id="bar_export_client" class="bar-item bar-item-icon iconfont icon-btn-export" title="导出"></a>
        </div>

    </div>
    <div class="processshow">
        <table id="clientTable" cellspacing="0" cellpadding="0" border="0" width="100%">
            <thead>
            <tr>
                <th>用户名</th>
                <th>事件类型</th>
                <th>时间</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/datarangepicker/moment.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/datarangepicker/daterangepicker.js" type="text/javascript"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/system/log/client.js"></script>
<script>
  var ctx = "${ctx}";


</script>