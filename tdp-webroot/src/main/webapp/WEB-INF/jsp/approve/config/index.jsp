<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title></title>
    <%--<link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>--%>
    <%--<link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>--%>
</head>

<div id="app" class="gd-right-content gd-padding-lg" v-cloak>
    <gd-tab style="height: 100%;" @change="change">
        <gd-tab-item label="审批中">
            <div class="process" style="height:100%"></div>
        </gd-tab-item>
        <gd-tab-item label="已完成">
            <div class="processover" style="height: 100%"></div>
        </gd-tab-item>
    </gd-tab>
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
                <label for="">接收方：<span class="text-ellipsis w150 inline-block text-top"
                                          title="{{flowInfo.policyParam.receiver}}">{{flowInfo.policyParam.receiver}}</span></label>
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
                <label for="">接收方：<span class="text-ellipsis w300 inline-block text-top"
                                          title="{{flowInfo.policyParam.receiver}}">{{flowInfo.policyParam.receiver}}</span></label>
            </div>

        </div>
        <div class="right">
            <div class="wind-row">
                <label for="">申请人：{{applicantName}}</label>
            </div>
            <div class="wind-row">
                <label for="">导出原因：<span class="text-ellipsis w300 inline-block text-top"
                                         title="{{flowInfo.policyParam.reason}}">{{flowInfo.policyParam.reason}}</span></label>
            </div>
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
<script>
    var ctx = "${ctx}";
    var app = new Vue({
        el: '#app',
        mounted: function () {
            this.initLoad();
        },
        methods: {
            initLoad: function () {
                $(".process").load(ctx + '/approveFlow/listPage?statusType=process');
                $(".processover").load(ctx + '/approveFlow/listPage?statusType=processover');
            },
            change: function (data) {
                log(data)
                if (data.index == 0) {
                    $(".process").load(ctx + '/approveFlow/listPage?statusType=process');
                } else {
                    $(".processover").load(ctx + '/approveFlow/listPage?statusType=processover');
                }
            }
        },
        data: {},

    })

    //审批流程细节表
    function ProcessTable(id) {

        var appTable = new Vue({
            el: '.approveHistory',
            data: {
                //表格配置
                tableConfig: {
                    id: 'approveDetailTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                    length: 50, //每页多少条,默认50，可选
                    curPage: 1, //当前页码，默认1，可选
                    lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                    enableJumpPage: false, //启用跳页，默认false，可选
                    enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                    enablePaging: false,//启用分页,默认true，可选
                    //orderColumn: 'ip',//排序列
                    //orderType: 'desc',//排序规则，desc或asc,默认desc
                    columnResize: true, //启用列宽调，默认true，可选
                    //showFooter: false,//显示footer,默认为true
                    //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                    //loading: true,//显示loading,默认为false
                    ajax: {
                        //其它ajax参数同jquery
                        url: ctx + "/approveDetail/getApproveDetailsByFlowId",
                        //改变从服务器返回的数据给table
                        dataSrc: function (data) {
                            console.log(data);
                            data.rows = data.rows.map(function (obj) {
                                return [
                                    obj.approverName, obj.result, obj.remark || '--', obj.modifyTime
                                ]
                            });
                            return data;
                        },
                        //请求参数
                        data: {
                            "approveFlowId": id,
                        }
                    },
                    columns: [
                        {
                            name: 'approverName',
                            head: '审批人',
                            title: true
                        },
                        {
                            name: 'result',
                            head: '审批结果',
                            render: function (cell, row, raw) {//自定义表格内容
                                console.log(cell)
                                if (cell == 1) {
                                    return '<span>同意</span>'
                                } else {
                                    return '<span>拒绝</span>'
                                }
                            }
                        },
                        {
                            name: 'remark',
                            head: '备注',
                            title: true
                        },

                        {
                            name: 'modifyTime',
                            head: '审批时间',
                            title: true
                        }

                    ]
                },
            },
            methods: {
                selectChange: function (data) {
                    log(data)
                }
            }
        });


    }

</script>