var app = new Vue({
    el: '#approveOver',
    data: {
        batchDatas: [], // 批量修改的审批请求id
        //工具栏配置
        toolbarConfig: [
            {
                type: 'button',
                icon: 'icon-delete',
                title: '删除',
                action: function (dom) {
                    gd.showConfirm({
                        id: 'openWind',//可传一个id作为标识
                        content: '确定要删除选中的流程吗?',
                        btn: [
                            {
                                text: '删除',
                                class: 'gd-btn',//也可以自定义类
                                enter: true,//响应回车
                                action: function (dom) {

                                    var ids = app.batchDatas.map(function (item) {
                                        return item[0];
                                    });
                                    if (ids.length == 0) {
                                        gd.showWarning('请选择需要删除的流程');
                                        dom.close();
                                        return false;
                                    }
                                    var postData = {};
                                    postData.approveFlowArr = ids.join(',');
                                    gd.post(ctx + '/approveFlow/deleteApproveFlow',postData,function (msg) {
                                        if (msg.resultCode == 0) {
                                            gd.table('approveOverTable').reload(1);
                                            gd.showSuccess('删除成功！');
                                        } else {
                                            gd.showError('删除失败！');
                                        }
                                        dom.close();
                                    });
                                    return false;//阻止弹窗自动关闭
                                }
                            },
                            {
                                text: '取消',
                                action: function () {

                                }
                            }
                        ],
                        success: function (dom) {//参数为当前窗口dom对象
                        },
                        end: function (dom) {//参数为当前窗口dom对象
                            // gd.showSuccess('窗口关闭了');
                        }
                    });
                }
            },
            {
                type: 'searchbox',
                placeholder: "申请人",
                action: function (val) {
                    gd.table('approveOverTable').reload(1, {applicantOrType: val}, false);
                }
            }
        ],
        //表格配置
        detailId: '',
        tableConfig: {
            id: 'approveOverTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
            length: 50, //每页多少条,默认50，可选
            curPage: 1, //当前页码，默认1，可选
            lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
            enableJumpPage: false, //启用跳页，默认false，可选
            enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
            enablePaging: true,//启用分页,默认true，可选
            //orderColumn: 'ip',//排序列
            //orderType: 'desc',//排序规则，desc或asc,默认desc
            columnResize: true, //启用列宽调，默认true，可选
            //showFooter: false,//显示footer,默认为true
            //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
            //loading: true,//显示loading,默认为false
            ajax: {
                //其它ajax参数同jquery
                url: ctx + "/approveFlow/getApproveFlowPage",
                //改变从服务器返回的数据给table
                dataSrc: function (data) {
                    data.rows = data.rows.map(function (obj) {
                        return [
                            obj.flowId,
                            obj.status || '--',
                            obj.applicantName || '--',
                            obj.reason || '--',
                            obj.type || '--',
                            obj.name || '--',
                            obj.applyTime || '--',
                            obj.finishTime || '--',
                            obj.flowId
                        ]
                    });
                    return data;
                },
                //请求参数
                data: {
                    "applicantOrType": "",
                    "status": 2
                }
            },
            columns: [
                {
                    name: 'checkbox',
                    type: 'checkbox',
                    // single: true,//checkbox只允许选单个
                    width: '60', //列宽
                    //class: 'xxx',//加入自定义类
                    align: 'center',//对齐方式，默认left，与class不同，class只影响内容，align会影响内容和表头
                    change: function (data) {//复选框改变，触发事件，返回所有选中的列的数据
                        console.log(data);
                        app.batchDatas = data;
                    }
                },
                {
                    name: 'status',
                    head: '结果',
                    width: '120', //列宽
                    /*filterName: 'status',//高级查询字段名，不写为name
                    filters: [//设置检索条件
                        {
                            label: '已审批',
                            value: 2
                        }, {
                            label: '已通过',
                            value: 1
                        }, {
                            label: '已拒绝',
                            value: -1
                        }
                    ],*/
                    render: function (cell, row, raw) {//自定义表格内容
                        var html = '';
                        if (raw.status == 1) {
                            html = '<span class="client-state backlog">通过</span>';
                        } else {
                            html = '<span class="client-state refuse">拒绝</span>';
                        }
                        return html;
                    }
                },
                {
                    name: 'applicantName',
                    head: '申请人',
                    title: true
                },
                {
                    name: 'reason',
                    head: '申请原因',
                    title: true
                },
                {
                    name: 'type',
                    head: '类型',
                    render: function (cell, row, raw) {//自定义表格内容
                        var html = '';
                        if (raw.type == 10 || raw.type == 11) {
                            html = '<span class="">外发</span>';
                        } else {
                            html = '<span class="">导出</span>';
                        }
                        return html;
                    }
                },
                {
                    name: 'name',
                    head: '审批流程',
                    title: true
                },
                {
                    name: 'applyTime',
                    head: '申请时间',
                    title: true
                }, {
                    name: 'finishTime',
                    head: '完成时间',
                    title: true
                },
                {
                    name: 'operate',
                    head: '操作',
                    align: 'center',
                    width: 100,
                    operates: [
                        {
                            icon: 'icon-btn-document',
                            title: '查看详情',//设置图标title
                            action: function (cell, row, raw) {//动作函数,cell为本格数据，row为本行加工后的数据，raw为本行未加工的数据
                                var id = raw.id;//他的id是多少
                                var type = raw.type;//类型是不是外发
                                gd.showLayer({
                                    id: 'openWind',//可传一个id作为标识
                                    title: '审批',//窗口标题
                                    content: $('#approve_wind').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                    //url: './layer_content.html',//也可以传入url作为content,
                                    size: [900, 650],//窗口大小，直接传数字即可，也可以是['600px','400px']
                                    //autoFocus:true,//自动对输入框获取焦点，默认为ture
                                    btn: [
                                        {
                                            text: '确定',
                                            action: function (dom) {
                                            }
                                        },
                                        {
                                            text: '取消',
                                            action: function () {

                                            }
                                        }
                                    ],
                                    success: function (dom) {//参数为当前窗口dom对象
                                        // 获取详情
                                        gd.get(ctx + '/approveFlow/getApproveFlowInfoById', {approveFlowId: id}, function (msg) {
                                            console.log(msg)
                                            if (msg.resultCode == 0) {
                                                msg.data.flowInfo.policyParam = JSON.parse(msg.data.flowInfo.policyParam);

                                                if (type == 10 || type == 11) {
                                                    $("#openWind .top").html(template('approve_tem_out_top', msg.data));
                                                } else {
                                                    $("#openWind .top").html(template('approve_tem_export_top', msg.data));
                                                }

                                            }
                                        });
                                        //获取环节
                                        gd.get(ctx + '/approveDetail/getApproveFlowModel', {approveFlowId: id}, function (msg) {
                                            if (msg.resultCode == 0) {
                                                app.detailId = msg.data.detailId;
                                                if (type == 10 || type == 11) {
                                                    $("#openWind .flow").html(template('getNode_tem', msg));
                                                } else {
                                                    $("#openWind .flow").html(template('getNode_tem', msg));
                                                }

                                            }
                                        });
                                        ProcessTable(id);
                                        $("#openWind .opinion").hide();
                                    },
                                    end: function (dom) {//参数为当前窗口dom对象
                                        // gd.showSuccess('窗口关闭了');
                                    }
                                });
                            }
                        },
                        {
                            icon: 'icon-delete',
                            title: '删除',//设置图标title
                            action: function (cell, row, raw) {//动作函数,cell为本格数据，row为本行加工后的数据，raw为本行未加工的数据
                                gd.showConfirm({
                                    id: 'openWind',//可传一个id作为标识
                                    content: '确定要删除该流程吗?',
                                    btn: [
                                        {
                                            text: '删除',
                                            class: 'gd-btn',//也可以自定义类
                                            enter: true,//响应回车
                                            action: function (dom) {

                                                var ids = [];
                                                var postData = {};
                                                postData.approveFlowArr = ids.push(cell);
                                                gd.post(ctx + '/approveFlow/deleteApproveFlow', postData, function (msg) {
                                                    if (msg.resultCode == 0) {
                                                        gd.table('approveOverTable').reload(1);
                                                        gd.showSuccess('删除成功！');
                                                    } else {
                                                        gd.showError('删除失败！');
                                                    }
                                                });

                                            }
                                        },
                                        {
                                            text: '取消',
                                            action: function () {

                                            }
                                        }
                                    ],
                                    success: function (dom) {//参数为当前窗口dom对象
                                    },
                                    end: function (dom) {//参数为当前窗口dom对象
                                        // gd.showSuccess('窗口关闭了');
                                    }
                                });
                            }
                        }
                    ]
                }
            ]
        },
    },
    mounted: function () {
    },
    methods: {
        selectChange: function (data) {
            log(data)
        }
    }
});


