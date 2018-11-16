<!--userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<head>
    <title>日志</title>
    <style>
        .main-content{
            padding: 20px;
            box-sizing: border-box;
        }
        .gd-tab-horizontal>div.gd-tab-content .gd-tab-item{
            padding: 0;
        }
        .gd-toolbar{
            background-color: #fff;
            padding: 0;
        }
        .gd-table-wrapper{
            height: calc(100% - 56px);
        }
        .gd-tab-horizontal{
            color: #333;
        }
    </style>
</head>
<body>
<div id="app" class="gd-right-content" v-cloak>
    <div class="main-content" >
        <gd-tab style="height: 100%">
            <gd-tab-item label="流转日志">
                <gd-toolbar :config="outToolbarConfig"></gd-toolbar>
                <gd-table :config="outTableConfig"></gd-table>
            </gd-tab-item>
            <gd-tab-item label="操作日志">
                <gd-toolbar :config="operToolbarConfig"></gd-toolbar>
                <gd-table :config="operTableConfig"></gd-table>
            </gd-tab-item>
            <%--写完了控制台登录日志，先不显示--%>
            <%--<gd-tab-item label="登录日志">
                <gd-toolbar :config="logoToolbarConfig"></gd-toolbar>
                <gd-table :config="logoTableConfig"></gd-table>
            </gd-tab-item>--%>
        </gd-tab>
    </div>
</div>

<script>
    var app = new Vue({
        el: '#app',
        data: {
            // 外发日志工具栏
            outToolbarConfig: [
                {
                    type: 'searchbox',
                    placeholder: "IP/文件名",
                    action: function (val) {
                        gd.table('outTable').reload(1, {searchStr: val}, false);
                    }
                }
            ],
            // 外发日志表格配置
            outTableConfig: {
                id: 'outTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                length: 50, //每页多少条,默认50，可选
                curPage: 1, //当前页码，默认1，可选
                lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                enableJumpPage: false, //启用跳页，默认false，可选
                enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                enablePaging: true,//启用分页,默认true，可选
                orderColumn: 'transferTime',//排序列
                orderType: 'desc',//排序规则，desc或asc,默认desc
                columnResize: true, //启用列宽调，默认true，可选
                //showFooter: false,//显示footer,默认为true
                //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                //loading: true,//显示loading,默认为false
                ajax: {
                    //其它ajax参数同jquery
                    url: ctx + '/report/queryFileTransferLog',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.ip || '--',
                                obj.fileName || '--',
                                obj.receiver || '--',
                                obj.fileType ,
                                obj.transferTime || '--'
                            ]
                        });
                        return data;
                    },
                    //请求参数
                    data: {
                        searchStr: ''
                    }
                },
                columns: [
                    {
                        name: 'ip',
                        head: 'IP地址',
                        // width: '300', //列宽
                        title: true
                    },
                    {
                        name: 'fileName',
                        head: '文件名',
                        title: true
                    },
                    {
                        name: 'receiver',
                        head: '接收方',
                        title: true
                    },
                    {
                        name: 'fftype',
                        head: '流转类型',
                        filterName: 'fftype',//高级查询字段名，不写为name
                        filters: [//设置检索条件
                            {
                                label: '密文导出',
                                value: '2',
                            }, {
                                label: '审批导出',
                                value: '3'
                            },
                            {
                                label: '自主外发',
                                value: '10',
                            }, {
                                label: '审批外发',
                                value: '11'
                            }
                        ],
                        render: function (cell, row, raw) {//自定义表格内容
                            var html = '';
                            switch(raw.fftype) {
                                case 2:
                                    html = '密文导出';
                                    break;
                                case 3:
                                    html = '审批导出';
                                    break;
                                case 10:
                                    html = '自主外发';
                                    break;
                                case 11:
                                    html = '审批外发';
                                    break;
                                default:
                                    html = '未知';
                            }
                            return html;
                        }
                    },
                    {
                        name: 'transferTime',
                        head: '时间',
                        title: true,
                        orderable: true
                    }
                ]
            },
            // 操作日志工具栏配置
            operToolbarConfig: [
                {
                    type: 'searchbox',
                    placeholder: "操作人/操作详情/IP",
                    action: function (val) {
                        gd.table('operTable').reload(1, {searchStr: val}, false);
                    }
                }
            ],
            // 操作日志表格配置
            operTableConfig: {
                id: 'operTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                length: 50, //每页多少条,默认50，可选
                curPage: 1, //当前页码，默认1，可选
                lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                enableJumpPage: false, //启用跳页，默认false，可选
                enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                enablePaging: true,//启用分页,默认true，可选
                orderColumn: 'time',//排序列
                orderType: 'desc',//排序规则，desc或asc,默认desc
                columnResize: true, //启用列宽调，默认true，可选
                //showFooter: false,//显示footer,默认为true
                //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                //loading: true,//显示loading,默认为false
                ajax: {
                    //其它ajax参数同jquery
                    url: ctx + '/systemLog/getSystemOperationLogInPage',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.userName || '--',
                                obj.logPage || '--',
                                obj.logOperateParam || '--',
                                obj.ip || '--',
                                obj.time || '--'
                            ]
                        });
                        return data;
                    },
                    //请求参数
                    data: {
                        search: ''
                    }
                },
                columns: [
                    {
                        name: 'userName',
                        head: '操作人',
                        title: true,
                        width: '12%'
                    },
                    {
                        name: 'logPage',
                        head: '模块',
                        width: '120',
                        title: true
                    },
                    {
                        name: 'logOperateParam',
                        head: '操作详情',
                        title: true,
                        width: '40%'
                    },
                    {
                        name: 'ip',
                        head: 'IP地址',
                        title: true,
                        width: '10%'
                    },
                    {
                        name: 'time',
                        head: '时间',
                        title: true,
                        width: '20%',
                        orderable: true
                    }
                ]
            },
            // 登录日志工具栏配置
            logoToolbarConfig: [
                {
                    type: 'searchbox',
                    placeholder: "用户名/IP",
                    action: function (val) {
                        gd.table('logoTable').reload(1, {order: val}, false);
                    }
                }
            ],
            // 登录表格配置
            logoTableConfig: {
                id: 'logoTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                length: 50, //每页多少条,默认50，可选
                curPage: 1, //当前页码，默认1，可选
                lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                enableJumpPage: false, //启用跳页，默认false，可选
                enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                enablePaging: true,//启用分页,默认true，可选
                orderColumn: 'time',//排序列
                orderType: 'desc',//排序规则，desc或asc,默认desc
                columnResize: true, //启用列宽调，默认true，可选
                //showFooter: false,//显示footer,默认为true
                //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                //loading: true,//显示loading,默认为false
                ajax: {
                    //其它ajax参数同jquery
                    url: ctx + '/systemLog/getSystemLogonLogInPage',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.userName || '--',
                                obj.logOperateParam || '--',
                                obj.ip || '--',
                                obj.time || '--'
                            ]
                        });
                        return data;
                    },
                    //请求参数
                    data: {
                        order: ''
                    }
                },
                columns: [
                    {
                        name: 'userName',
                        head: '操作人',
                        title: true,
                        width: '12%'
                    },
                    {
                        name: 'logOperateParam',
                        head: '操作详情',
                        title: true,
                        width: '40%'
                    },
                    {
                        name: 'ip',
                        head: 'IP地址',
                        title: true,
                        width: '10%'
                    },
                    {
                        name: 'time',
                        head: '时间',
                        title: true,
                        width: '20%',
                        orderable: true
                    }
                ]
            }
        },
        methods: {


        }
    });
</script>
</body>
