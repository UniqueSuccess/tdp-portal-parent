<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>
</head>
<div id="approveOver" class="" v-cloak style="height: calc(100% - 37px)">
    <gd-toolbar :config="toolbarConfig"></gd-toolbar>
    <gd-table :config="tableConfig"></gd-table>
</div>
<%--<div class="complatecon">
    <div class="processtop">
        <div class="left">
            <a id="bar_del_process" class="bar-item bar-item-icon iconfont icon-btn-delete" title="删除流程"></a>
        </div>
        <div class="right bar-item-box">
            <div class="bar-item bar-item-search">
                <select name="" id="allowType" class="w120">
                    <option value="2">已审批</option>
                    <option value="1">通过</option>
                    <option value="-1">拒绝</option>
                </select>
            </div>
            <div class="bar-item bar-item-search wind-content datatimechoose">
                <input type="text" class="inputtime wind-content-input wind-content-input-date" placeholder="请选择时间" value="全部">
                <ul class="datachange" style="display: none;">
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
        <table id="processTableOver" cellspacing="0" cellpadding="0" border="0" width="100%">
            <thead>
            <tr>
                <th class="text-center">
                    <div class="beauty-checkbox">
                        <input id="check_process_all" type="checkbox" class="j-check-process-all">
                        <label for="check_process_all" class="checkbox-icon"></label>
                    </div>
                </th>
                <th>流程名称</th>
                <th>审批类型</th>
                <th>提交人</th>
                <th>提交时间</th>
                <th>审批结果</th>
                <th>审批完成时间</th>
                <th style="text-align:center;">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>--%>
<%--<script id="approve_wind" type="text/html">
    <div class="top">

    </div>
    <div class="flow">

    </div>
    <div class="opinion">
        <div class="wind-row">
            <label class="wind-label">审批意见</label>
            <input id="agree" value="1" checked type="radio" name="approveIdea"/>
            <label for="agree" class="margin-right-xl">通过</label>
            <input id="reject" value="0" type="radio" name="approveIdea"/>
            <label for="reject">拒绝</label>
        </div>
        <div class="wind-row">
            <label class="wind-label">备注</label>
            <textarea name="textarea" id="textarea" maxlength="140" rows="6" cols="50"></textarea>
        </div>

    </div>
    <div class="table">
        <div class="wind-row">
            <label class="wind-label">审批历史</label>
            <div class="approveHistory">
                <table id="approveHistoryTable" cellspacing="0" cellpadding="0" border="0" width="100%">
                    <thead>
                    <tr>
                        <th>审批人</th>
                        <th>审批结果</th>
                        <th>备注</th>
                        <th>审批时间</th>
                    </tr>
                    </thead>
                </table>
            </div>

        </div>
    </div>
</script>
<script id="approve_tem_out_top" type="text/html">
    <div class="title">文件外发申请详情</div>
    <div class="content">
        <div class="left">
            <label for="" class="dig">摘要：</label><a href="${ctx}/{{flowInfo.filePath}}"><i class="iconfont icon-btn-review"></i></a>
            <label for="">申请人：{{applicantName}}</label>
            <label for="">接收方信息：<span class="text-ellipsis w150 inline-block text-top" title="{{flowInfo.policyParam.recv}}">{{flowInfo.policyParam.recv}}</span></label>
            <!--<label for="">禁止截屏：{{if flowInfo.policyParam.forbidScreenShot == 1}} 是 {{else}} 否 {{/if}}</label>-->
            <label for="">外发原因：<span class="text-ellipsis w150 inline-block text-top" title="{{flowInfo.policyParam.reason}}">{{flowInfo.policyParam.reason}}</span></label>
        </div>
        <div class="right">

            <label for="">打开次数：{{if flowInfo.policyParam.openCount == 0}}不限{{else}} {{flowInfo.policyParam.openCount}} 次{{/if}}</label>
            <label for="">机器码绑定：{{if flowInfo.policyParam.machineCode == ''}} 空 {{else}} {{flowInfo.policyParam.machineCode}} {{/if}}</label>
            <label for="">有效日期：{{if flowInfo.policyParam.beginTime == ''}} 无 {{else}} {{flowInfo.policyParam.beginTime}} - {{flowInfo.policyParam.endTime}} {{/if}}</label></div>
    </div>
</script>
<script id="approve_tem_export_top" type="text/html">
    <div class="title">文件导出申请详情</div>
    <div class="content">
        <div class="left">
            <label for="" class="dig">摘要：</label><a href="${ctx}/{{flowInfo.filePath}}"><i class="iconfont icon-btn-review"></i></a>
            <label for="">接收方信息：<span class="text-ellipsis w300 inline-block text-top" title="{{flowInfo.policyParam.recv}}">{{flowInfo.policyParam.recv}}</span></label>
            <label for="">申请人：{{applicantName}}</label>
            <label for="">导出原因：<span class="text-ellipsis w300 inline-block text-top" title="{{flowInfo.policyParam.reason}}">{{flowInfo.policyParam.reason}}</span></label>
        </div>
        <div class="right">
            <!--<label for="">申请人：XXXXXX</label>-->
            <!--<label for="">打开次数：4次，未启动自动删除</label>-->
            <!--<label for="">机器码绑定：XXXXXX</label>-->
            <!--<label for="">有效日期：20171.1-20171.2</label></div>-->
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
</script>--%>
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/approve/config/processover.js"></script>
<script>
  var ctx = "${ctx}";
</script>