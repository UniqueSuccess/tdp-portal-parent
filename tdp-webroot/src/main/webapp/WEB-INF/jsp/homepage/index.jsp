<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
-->

<head>
    <title></title>
    <link href="${ctxCss}/homepage/index.css" rel="stylesheet" type="text/css"/>

</head>

<div id="homepage" v-cloak class="gd-right-content gd-padding-lg">
    <div class="row-top">
        <div class="cell">
            <p class="section-title">安全指数</p>
            <div class="cell-top">
                <div id="potent_risk" class="echarts-content"></div>
            </div>
            <div class="cell-center"></div>
            <div class="score">分</div>
            <div class="cell-bottom">
                <div class="cell-bottom-cell">
                    <i class="iconfont icon-nav-monitor"></i>
                    <div>
                        <span class="screen"></span>人
                    </div>
                    <span>屏幕无水印</span>
                </div>
                <div class="cell-bottom-cell">
                    <i class="iconfont icon-btn-outgoing-doc"></i>
                    <div>
                        <span class="out"></span>人
                    </div>
                    <span>外发无水印</span>
                </div>
                <div class="cell-bottom-cell">
                    <i class="iconfont icon-btn-export"></i>
                    <div>
                        <span class="export"></span>人
                    </div>
                    <span>导出有风险</span>
                </div>
            </div>
        </div>
        <div class="cell">
            <p class="section-title">文件流转</p>
            <div id="video_move" class="echarts-content"></div>
            <%--<span class="video-move">单位 : 条</span>--%>
            <div class="no-data vedio_export_empty none"></div>
            <div class="data-select">
                <div class="right fr">
                    <span class="day color-white" data-type="day">天</span>
                    <span class="week" data-type="week">周</span>
                    <span class="month" data-type="month">月</span>
                </div>
            </div>
        </div>
    </div>
    <div class="row-bottom">
        <div class="cell">
            <p class="section-title">审批</p>
            <div class="content">
                <div class="top">
                    <div class="content-cell" data-value="0">
                        <div class="num j-waiting">0</div>
                        <div class="title">待审批</div>
                    </div>
                    <div class="content-cell" data-value="2">
                        <div class="num j-all">0</div>
                        <div class="title">已审批</div>
                    </div>
                </div>
                <div class="bottom">
                    <div class="content-cell" data-value="1">
                        <div class="num j-yes">0</div>
                        <div class="title">通过</div>
                    </div>
                    <div class="content-cell" data-value="-1">
                        <div class="num j-no">0</div>
                        <div class="title">拒绝</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="cell">
            <div class="cell-left">
                <p class="section-title">部门文件流转TOP5</p>
                <div id="dept_video" class="echarts-content"></div>
            </div>
            <div class="cell-right">
                <p class="section-title">文件流转类型排行</p>
                <div id="file_type" class="echarts-content"></div>
                <%--<span class="person-move">条</span>--%>
            </div>
        </div>
    </div>
</div>

<script src="${ctxJs}/plugins/echarts/echarts.min.js"></script>
<script src="${ctxJs}/plugins/echarts/echarts-liquidfill.js"></script>
<script src="${ctxJs}/homepage/index.js"></script>
<script>
    var ctx = "${ctx}";
</script>