<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title>金盾VDP</title>

    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <!--初始化样式-->
    <link href="${ctxCss}/init.css" rel="stylesheet" type="text/css"/>
    <!--整体结构-->
    <link href="${ctxCss}/world.css" rel="stylesheet" type="text/css"/>
    <!--右侧顶部-->
    <link href="${ctxCss}/wtop.css" rel="stylesheet" type="text/css"/>
    <!--图标集合-->
    <link href="${ctxCss}/icons.css" rel="stylesheet" type="text/css"/>
    <!--左侧菜单CSS-->
    <link href="${ctxCss}/leftmenu.css" rel="stylesheet" type="text/css"/>
    <!--公共样式-->
    <link href="${ctxCss}/common.css" rel="stylesheet" type="text/css"/>
    <!-- iconfont -->
    <link href="${ctxCss}/iconfont/iconfont.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/iconfont/iconfont-change.css" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="${ctxImg}/logo.ico" type="image/x-ico"/>

    <link href="${ctxCss}/fullscreen/index.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="upper">
        <div class="upper-left">
            <div class="term">
                <label for="">终端数量</label>
                <span class="terminal-num">0</span>
            </div>
            <div class="today-out">
                <label for="">今日外发</label>
                <span class="out-num">0</span>
            </div>
            <div class="today-export">
                <label for="">今日导出</label>
                <span class="export-num">0</span>
            </div>
        </div>
        <div class="upper-right">
            <span class="year">0000</span>
            <span class="line">-</span>
            <span class="month">00</span>
            <span class="line">-</span>
            <span class="day">00</span>
            <span class="time">00:00:00</span>
            <span class="week">--</span>
            <span class="exit">退出</span>
        </div>
    </div>
    <div class="lower">
        <div class="left fl">
            <div class="left-top">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">人员视频流转</div>
                    <div class="section-title-right"></div>

                </div>
                <div id="person_move" class="echarts-content"></div>
            </div>
            <div class="left-bottom">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">审批</div>
                    <div class="section-title-right"></div>
                </div>
                <div class="content">
                    <div class="top">
                        <div class="content-cell">
                            <div class="inner">
                                <div class="con">
                                    <div class="num j-waiting">0</div>
                                    <div class="title">待审批</div>
                                </div>
                            </div>

                        </div>
                        <div class="content-cell">
                            <div class="inner">
                                <div class="con">
                                    <div class="num j-all">0</div>
                                    <div class="title">已审批</div>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="bottom">
                        <div class="content-cell">
                            <div class="inner">
                                <div class="con">
                                    <div class="num j-yes">0</div>
                                    <div class="title">通过</div>
                                </div>
                            </div>
                        </div>
                        <div class="content-cell">
                            <div class="inner">
                                <div class="con">
                                    <div class="num j-no">0</div>
                                    <div class="title">拒绝</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="center fl">
            <div class="center-top">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">部门视频流转分布</div>
                    <div class="section-title-right"></div>
                </div>
                <div id="dept_move" class="echarts-content"></div>
            </div>
            <div class="center-bottom">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">七日视频流转</div>
                    <div class="section-title-right"></div>
                </div>
                <div id="video_move" class="echarts-content"></div>
            </div>
        </div>
        <div class="right fl">
            <div class="right-top">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">安全检测</div>
                    <div class="section-title-right"></div>
                </div>
                <div id="safe_check" class="echarts-content">

                </div>
            </div>
            <div class="right-bottom">
                <div class="section-title">
                    <div class="section-title-left"></div>
                    <div class="section-title-center">视频流转实时监控</div>
                    <div class="section-title-right"></div>
                </div>
                <div id="real_time_monitor" class="echarts-content">
                    <ul>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>
<script id="carousel" type="text/html">
    {{each $data item}}
    <li class="{{if ($index%2) == 1}}even{{else}}odd{{/if}}">
        <span class="name text-ellipsis">{{item.truename}}</span>
        <span class="type {{if item.fftype == 1 || item.fftype == 3}}m-export {{else}} m-out{{/if}}">{{if item.fftype == 1 || item.fftype == 3}}导出{{else}}外发{{/if}}</span>
        <span class="file text-ellipsis">{{item.fileName}}</span>
        <span class="move-time">{{item.transferTime}}</span>
    </li>
    {{/each}}
</script>
<script src="${ctxJs}/jquery-1.11.0.min.js"></script>
<script src="${ctxJs}/plugins/layer/layer.js" type="text/javascript"></script>
<script src="${ctxJs}/jquery.form.js" type="text/javascript"></script>
<script src="${ctxJs}/window.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<!--左侧菜单JS-->
<script src="${ctxJs}/menu.js"></script>
<script src="${ctxJs}/wtop.js"></script>
<!--公共js-->
<script src="${ctxJs}/common.js"></script>
<script src="${ctxJs}/plugins/echarts/echarts.min.js"></script>
<script src="${ctxJs}/plugins/echarts/echarts-liquidfill.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/jquery.domresize.js"></script>
<script src="${ctxJs}/plugins/template/template-web.js" type="text/javascript"></script>
<script src="${ctxJs}/fullscreen/jq_scroll.js"></script>
<script src="${ctxJs}/fullscreen/index.js"></script>
<script>
    var ctx = "${ctx}";
</script>