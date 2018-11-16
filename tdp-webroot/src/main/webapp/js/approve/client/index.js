var app = new Vue({
    el: '#client_approve',
    data: {

        //表格配置
        detailId:'',
        tableConfig: {
            id: 'clientApproveTableClient',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
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
                url: ctx + "/approveOpen/getApproveFlowPageClient",
                //改变从服务器返回的数据给table
                dataSrc: function (data) {
                    console.log(data);
                    data.rows = data.rows.map(function (obj) {
                        return [
                            obj.checked,
                            obj.applicantName || '--',
                            obj.reason || '--',
                            obj.type || '--',
                            obj.name || '--',
                            obj.pointName || '--',
                            obj.applyTime || '--',
                            obj.checked
                        ]
                    });
                    return data;
                },
                //请求参数
                data: {
                    "uuid": uuid,
                    "status": 0, //这个是未完成
                    "client":2 //这个是全部

                }
            },
            columns: [
                {
                    name: 'checked',
                    head: '状态',
                    width: '120', //列宽
                    render: function (cell, row, raw) {//自定义表格内容
                        console.log(raw);
                        var html = '';
                        if (raw.status == 0) {
                            html = '<span class="client-state onway">审批中</span>';
                        } else if (raw.status == 1){
                            html = '<span class="client-state backlog">通过</span>';

                        }else{
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
                        if (raw.type == 2) {
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
                    name: 'pointName',
                    head: '当前环节',
                    title: true
                },
                {
                    name: 'applyTime',
                    head: '申请时间',
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
                            title: '查看',//设置图标title
                            action: function (cell, row, raw) {//动作函数,cell为本格数据，row为本行加工后的数据，raw为本行未加工的数据
                                var id = raw.id;//他的id是多少
                                var type = raw.type;//类型是不是外发
                                var is = raw.checked;//是不是到他审批了
                                var detailId;
                                var domBacklog = gd.showLayer({
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
                                                dom.close();

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
                                            if (msg.resultCode == 0) {
                                                msg.data.flowInfo.policyParam = JSON.parse(msg.data.flowInfo.policyParam);

                                                if (type == 2) {
                                                    $("#openWind .top").html(template('approve_tem_out_top', msg.data));
                                                } else {
                                                    $("#openWind .top").html(template('approve_tem_export_top', msg.data));
                                                }

                                            }
                                        });
                                        //获取环节
                                        gd.get(ctx + '/approveOpen/getApproveFlowModelClient', {approveFlowId: id}, function (msg) {
                                            if (msg.resultCode == 0) {
                                                if (type == 2) {
                                                    $("#openWind .flow").html(template('getNode_tem', msg));
                                                } else {
                                                    $("#openWind .flow").html(template('getNode_tem', msg));
                                                }

                                            }
                                        });
                                        ProcessTable(id);
                                        if (is == true) {//到了当前人审批，需要审批
                                            // $("#openWind .table").hide();
                                        } else {
                                            $("#openWind .opinion").hide();
                                        }
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
    mounted: function() {
    },
    methods: {
        selectChange: function (data) {
            log(data)
        }
    }
});



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
