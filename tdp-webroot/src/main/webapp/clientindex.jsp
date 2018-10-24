<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ page import="sun.misc.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <title>金盾VDP</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <c:set var="ctxJs" value="${pageContext.request.contextPath}/js"/>
    <c:set var="ctxCss" value="${pageContext.request.contextPath}/skin/default/css"/>
    <c:set var="ctxImg" value="${pageContext.request.contextPath}/skin/default/images"/>
    <c:set var="version" value="1"/>
    <%--<link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>--%>
    <link rel="stylesheet" href="${ctxCss}/font_icon/iconfont.css?v=${version}"/>
    <script src="${ctxJs}/jquery-2.2.1.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gd_iecheck.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/vue.min.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gdui.min.js?v=${version}"></script>
    <link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctxJs}/plugins/gdui/css/gdui.min.css?v=${version}"/>
    <style>
        .wind-row{
            padding: 6px 0;
            position: relative;
        }
    </style>
    <script>
        var ctx = "${ctx}";
        var uuid = query("uuid");
        function query(name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var param = window.location.search.substr(1).match(reg);
            if (param !== null) {
                return unescape(param[2]);
            } else {
                return '';
            }
        };


    </script>
</head>
<div id="client_approve" class="" v-cloak style="height: calc(100%)">
    <%--<gd-toolbar :config="toolbarConfig"></gd-toolbar>--%>
    <gd-table :config="tableConfig"></gd-table>
</div>
<script id="approve_wind" type="text/html">
    <div class="top">

    </div>
    <div class="flow">

    </div>
    <div class="opinion">
        <div class="wind-row">
            <label class="wind-label">审批意见</label>

            <label class="gd-radio">
                <input type="radio" value="1" checked name="approveIdea" id="agree">
                <i></i>
                通过
            </label>
            <label class="gd-radio">
                <input type="radio" value="0" name="approveIdea" id="reject">
                <i></i>
                拒绝
            </label>
        </div>
        <div class="wind-row">
            <label class="wind-label">备注</label>
            <textarea id="textarea" name="textarea" class="gd-textarea gd-textarea-lg"></textarea>
            <%--<textarea name="textarea" id="textarea" maxlength="140" rows="6" cols="50"></textarea>--%>
        </div>

    </div>
    <div class="table">
        <div class="wind-row" style="height:100%;">
            <label class="wind-label">审批历史</label>
            <div class="approveHistory">
                <%--<table id="approveHistoryTable" cellspacing="0" cellpadding="0" border="0" width="100%">
                    <thead>
                    <tr>
                        <th>审批人</th>
                        <th>审批结果</th>
                        <th>备注</th>
                        <th>审批时间</th>
                    </tr>
                    </thead>
                </table>--%>
                <gd-table :config="tableConfig"></gd-table>
            </div>

        </div>
    </div>

</script>
<script id="approve_tem_out_top" type="text/html">
    <div class="title">文件外发申请详情</div>
    <div class="content">
        <div class="left">
            <div class="wind-row">
                <label for="" class="dig">摘要：</label><a href="${ctx}/{{flowInfo.filePath}}"><i
                    class="iconfont icon-download"></i></a>
            </div>
            <div class="wind-row">
                <label for="">申请人：{{applicantName}}</label>
            </div>
            <div class="wind-row">
                <label for="">接收方信息：<span class="text-ellipsis w150 inline-block text-top"
                                          title="{{flowInfo.policyParam.recv}}">{{flowInfo.policyParam.recv}}</span></label>
                <!--<label for="">禁止截屏：{{if flowInfo.policyParam.forbidScreenShot == 1}} 是 {{else}} 否 {{/if}}</label>-->
            </div>
            <div class="wind-row">
                <label for="">外发原因：<span class="text-ellipsis w150 inline-block text-top"
                                         title="{{flowInfo.policyParam.reason}}">{{flowInfo.policyParam.reason}}</span></label>
            </div>
        </div>
        <div class="right">
            <div class="wind-row">
                <label for="">打开次数：{{if flowInfo.policyParam.openCount == 0}}不限{{else}}
                    {{flowInfo.policyParam.openCount}}
                    次{{/if}}</label>
            </div>
            <div class="wind-row">
                <label for="">机器码绑定：{{if flowInfo.policyParam.machineCode == ''}} 空 {{else}}
                    {{flowInfo.policyParam.machineCode}} {{/if}}</label>
            </div>
            <div class="wind-row">
                <label for="">有效日期：{{if flowInfo.policyParam.beginTime == ''}} 无 {{else}}
                    {{flowInfo.policyParam.beginTime}}
                    - {{flowInfo.policyParam.endTime}} {{/if}}</label></div>
        </div>
    </div>
</script>
<script id="approve_tem_export_top" type="text/html">
    <div class="title">文件导出申请详情</div>
    <div class="content">
        <div class="left">
            <div class="wind-row">
                <label for="" class="dig">摘要：</label><a href="${ctx}/{{flowInfo.filePath}}"><i
                    class="iconfont icon-download"></i></a>
            </div>
            <div class="wind-row">
                <label for="">接收方信息：<span class="text-ellipsis w300 inline-block text-top"
                                          title="{{flowInfo.policyParam.recv}}">{{flowInfo.policyParam.recv}}</span></label>
            </div>
            <div class="wind-row">
                <label for="">申请人：{{applicantName}}</label>
            </div>
            <div class="wind-row">
                <label for="">导出原因：<span class="text-ellipsis w300 inline-block text-top"
                                         title="{{flowInfo.policyParam.reason}}">{{flowInfo.policyParam.reason}}</span></label>
            </div>
        </div>
        <div class="right">
        </div>
    </div>
</script>
<script id="getNode_tem" type="text/html">
    <div class="wind-row">
        <label class="wind-label flowTitle">审批流程</label>
        <div class="flowContent">
            <label class="begin" for="">
                <div class="begin_bar">
                    <label for=""></label>
                    <span>开始</span>
                </div>
                <div class="flow_line">

                </div>
            </label>
            {{each data.detailList item}}
            <label class="default {{if item.pointId == data.pointId}}default-hover{{/if}}" for="">
                <div class="default_bar">
                    <label for=""></label>
                    <span class="text-ellipsis" title="{{item.name}}">{{item.name}}</span>
                </div>
                <div class="flow_line">

                </div>
            </label>
            {{/each}}
            <label class="end" for="">
                <div class="end_bar">
                    <label for=""></label>
                    <span>结束</span>
                </div>
            </label>
        </div>
    </div>
</script>
<%--<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>--%>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<%--<script src="${ctxJs}/public.js"></script>--%>
<script src="${ctxJs}/approve/client/index.js"></script>
