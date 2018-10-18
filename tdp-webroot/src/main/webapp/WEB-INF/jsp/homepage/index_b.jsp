<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>-->

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/homepage/index.css" rel="stylesheet" type="text/css" />
</head>

<div class="main_right">
    <div class="row-top">
        <div class="cell">
            <p class="section-title">潜在风险</p>
            <div id="potent_risk" class="echarts-content"></div>
        </div>
        <div class="cell">
            <p class="section-title">视频数据导出Top10</p>
            <div id="vedio_export" class="echarts-content"></div>
        </div>
        <div class="cell">
            <p class="section-title">基本信息统计</p>
            <%--<p class="section-title">视频数据导出Top10</p>--%>
            <%--<div class="curr-visit-chart-box">
                <div id="curr_Visit_Chart"></div>
                <div class="curr_Visit_Number">
                    <span>当前正在访问数</span> <span class="number"></span>
                </div>
            </div>--%>
            <div class="serverinfo-chart-box">
                <div class="serverinfo-chart-title">
                    CPU
                    <%--<div class="serverinfo-chart-value"><span class="cpu"></span>%</div>--%>
                </div>
                <div id="serverinfo_cpu_chart" class="echarts-content serverinfo-content"></div>
            </div>
            <div class="serverinfo-chart-box">
                <div class="serverinfo-chart-title">
                    MYSQL
                    <%--<div class="serverinfo-chart-value"><span class="sql"></span>%</div>--%>
                </div>
                <div id="serverinfo_mysql_chart" class="echarts-content serverinfo-content"></div>
            </div>
            <div class="serverinfo-chart-box">
                <div class="serverinfo-chart-title">
                    内存
                    <%--<div class="serverinfo-chart-value"><span class="mem"></span>%</div>--%>
                </div>
                <div id="serverinfo_mem_chart" class="echarts-content serverinfo-content"></div>
            </div>
        </div>

    </div>
    <div class="row-bottom">
        <div class="cell">
            <!--<p class="section-title">数据导出统计</p>-->
            <div id="data_export" class="echarts-content"></div>
        </div>
    </div>
</div>

<script src="${ctxJs}/plugins/echarts/echarts.common.min.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/homepage/index.js"></script>
<script>
  var ctx = "${ctx}";
</script>