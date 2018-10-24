<!--<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>-->
<head>
    <style>
        .main-content{
            width: 100%;
            position: absolute;
            top: 0;
            bottom: 0;
        }

        .container {
            height: calc(100% - 35px);
            width: calc(100% - 80px);
            margin: 15px 40px 0 40px;
        }
        .container .row {
            padding: 5px 0;
            overflow: auto;
            line-height: 32px;
        }
        .container .row label{
            display: inline-block;
            width: 70px;
            margin-right: 16px;
            text-align: right;
            font-size: 14px;
            color: #333333;
            letter-spacing: 0;
        }

        .client-state{
            display: inline-block;
            width: 42px;
            height: 22px;
            line-height: 22px;
            border-radius: 1px;
            text-align: center;
            color: #fff;
        }
        .client-state.online{
            background-color: #58a5e3;
        }
        .client-state.offline{
            background-color: #9e9e9e;
        }
        .gd-table-wrapper{
            margin: 0 30px;
        }
        .gd-layer-footer{
            border-top: none;
        }
    </style>
</head>

<body>
<div id="app" class="gd-right-content" v-cloak>
    <div class="main-content">
        <gd-toolbar :config="toolbarConfig"></gd-toolbar>
        <gd-table :config="tableConfig"></gd-table>
        <div style="visibility: hidden;position: fixed;z-index: -1;">
            <div id="tree_box" class="tree-box" style="width: 240px;max-height: 300px">
                <gd-tree :config='treeConfig'></gd-tree>
            </div>
        </div>
    </div>
</div>
<!--编辑弹框-->
<script id="edit_dept_temp" class="gd-none" type="text/html">
    <div class="container" id="add_dept" v-cloak>
        <form id="add_dept_form">
            <div class="row">
                <label>计算机名</label>
                <span id="edit_computername"></span>
            </div>
            <div class="row">
                <label>IP地址</label>
                <span id="edit_ip"></span>
            </div>
            <div class="row">
                <label>MAC地址</label>
                <span id="edit_mac"></span>
            </div>
            <div class="row">
                <label class="">部门</label>
                <gd-select placeholder="请选择" v-model="selectValue" ref="customSelect" name="deptguid" class="gd-select-lg">
                    <gd-tree :config='treeWindowConfig'></gd-tree>
                </gd-select>
            </div>
            <div class="row">
                <label class="">安全策略</label>
                <gd-select placeholder="请选择" v-model="selectPolicyValue" class="gd-select-lg" name="strategy">
                    <gd-option v-for="item in policyArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
        </form>
    </div>
</script>

<!--批量修改策略弹框-->
<script type="text/html" id="update_batch_temp">
    <div class="container" id="update_batch" v-cloak>
        <form id="add_dept_form">
            <div class="row">
                <label class="">安全策略</label>
                <gd-select placeholder="请选择" v-model="selectPolicyValue" class="gd-select-lg" name="strategy">
                    <gd-option v-for="item in policyArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
        </form>
    </div>
</script>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            departmentId: '1', // 新增弹框中用到此值
            deptTreeData: [], // 部门树
            policyArray: [], // 策略列表
            batchDatas: [], // 批量修改的策略id
            //工具栏配置
            toolbarConfig: [
                {
                    type: 'button',
                    icon: 'icon-strategy',
                    title: '策略',
                    action: function (dom) {
                        var ids = app.batchDatas.map(function(item) {
                            return item[0];
                        });
                        if (ids.length == 0) {
                            gd.showWarning('请选择需要修改策略的终端');
                            return;
                        }

                        gd.showLayer({
                            id: 'updateWind',//可传一个id作为标识
                            title: '策略',//窗口标题
                            content: $('#update_batch_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                            //url: './layer_content.html',//也可以传入url作为content,
                            size: [600, 220],//窗口大小，直接传数字即可，也可以是['600px','400px']
                            //autoFocus:true,//自动对输入框获取焦点，默认为ture
                            btn: [
                                {
                                    text: '确定',
                                    action: function (dom) {
                                        var postData = {
                                            ids: ids.join(';'),
                                            strategy: $('#updateWind input[name="strategy"]').val()
                                        }
                                        gd.post(ctx + '/clientUser/updateClientUser', postData, function (msg) {
                                            if (msg.resultCode == '0') {
                                                gd.showSuccess('批量修改策略成功');
                                                gd.table('clientUserTable').reload();
                                            } else {
                                                gd.showError('批量修改策略失败 ' + (msg.resultMsg || ''));
                                            }
                                            dom.close();
                                        })
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
                                var updateWindow = new Vue({
                                    el: '#update_batch',
                                    data: {
                                        selectPolicyValue: '1',
                                        policyArray: app.policyArray,
                                    }
                                });
                            },
                            end: function (dom) {//参数为当前窗口dom对象
                                // gd.showSuccess('窗口关闭了');
                            }
                        });
                    }
                },
                {
                    type: 'searchbox',
                    placeholder: "计算机名/IP",
                    action: function (val) {
                        gd.table('clientUserTable').reload(1, {searchStr: val}, false);
                    }
                }
            ],
            // 部门树配置
            treeConfig: {
                id: 'treeDepartment',
                showCheckBox: true,//默认是false;显示checkbox
                data: [],
                // linkable: false,
                onSelect: function (node) {

                },
                onChange: function (nodes) {
                    var ids = nodes.map(function (node) {
                        return node.id
                    });
                    gd.table('clientUserTable').setFilterValue('department', ids.join(';'))
                }
            },
            //表格配置
            tableConfig: {
                id: 'clientUserTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                length: 50, //每页多少条,默认50，可选
                curPage: 1, //当前页码，默认1，可选
                lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                enableJumpPage: false, //启用跳页，默认false，可选
                enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                enablePaging: true,//启用分页,默认true，可选
                orderColumn: 'loginTime',//排序列
                orderType: 'desc',//排序规则，desc或asc,默认desc
                columnResize: true, //启用列宽调，默认true，可选
                //showFooter: false,//显示footer,默认为true
                //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                //loading: true,//显示loading,默认为false
                ajax: {
                    //其它ajax参数同jquery
                    url: ctx + '/clientUser/getClientUserPages',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.computerguid,
                                obj.online,
                                obj.computername || '--',
                                obj.ip || '--',
                                obj.mac || '--',
                                obj.departmentName || '--',
                                obj.policyName || '--',
                                obj.onlineTime || '--',
                                obj.id
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
                        name: 'checkbox',
                        type: 'checkbox',
                        // single: true,//checkbox只允许选单个
                        width: '60', //列宽
                        //class: 'xxx',//加入自定义类
                        align: 'center',//对齐方式，默认left，与class不同，class只影响内容，align会影响内容和表头
                        change: function (data) {//复选框改变，触发事件，返回所有选中的列的数据
                            app.batchDatas = data;
                        }
                    },
                    {
                        name: 'online',
                        head: '状态',
                        width: '100', //列宽
                        filterName: 'state',//高级查询字段名，不写为name
                        filters: [//设置检索条件
                            {
                                label: '在线',
                                value: '1',
                            }, {
                                label: '离线',
                                value: '0'
                            }
                        ],
                        render: function (cell, row, raw) {//自定义表格内容
                            var html = '';
                            if (raw.online == '0') {
                                html = '<span class="client-state offline">离线</span>';
                            } else {
                                html = '<span class="client-state online">在线</span>';
                            }
                            return html;
                        }
                    },
                    {
                        name: 'computername',
                        head: '计算机名',
                        title: true
                    },
                    {
                        name: 'ip',
                        head: 'IP地址',
                        title: true
                    },
                    {
                        name: 'mac',
                        head: 'MAC地址',
                        title: true
                    },
                    {
                        name: 'departmentName',
                        head: '部门',
                        title: true,
                        filters: '#tree_box',//如果是自定义内容，这里只能传入一个选择器
                    },
                    {
                        name: 'policyName',
                        head: '安全策略',
                        title: true
                    },
                    {
                        name: 'loginTime',
                        head: '登录时间',
                        title: true,
                        orderable: true,
                    },
                    {
                        name: 'operate',
                        head: '操作',
                        align: 'center',
                        width: 120,
                        operates: [
                            {
                                icon: 'icon-edit',
                                title: '编辑',//设置图标title
                                action: function (cell, row, raw) {//动作函数,cell为本格数据，row为本行加工后的数据，raw为本行未加工的数据

                                    var domEdit = gd.showLayer({
                                        id: 'editWind',//可传一个id作为标识
                                        title: '编辑部门',//窗口标题
                                        content: $('#edit_dept_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                        //url: './layer_content.html',//也可以传入url作为content,
                                        size: [600, 400],//窗口大小，直接传数字即可，也可以是['600px','400px']
                                        //autoFocus:true,//自动对输入框获取焦点，默认为ture
                                        btn: [
                                            {
                                                text: '确定',
                                                action: function (dom) {
                                                    if (!validateDep.valid()) {
                                                        return false;
                                                    }

                                                    var postData = {};
                                                    postData.ids = raw.computerguid;
                                                    postData.department = app.departmentId;
                                                    postData.strategy = $('#editWind input[name="strategy"]').val();

                                                    gd.post(ctx + '/clientUser/updateClientUser', postData, function (msg) {
                                                        if (msg.resultCode == '0') {
                                                            gd.showSuccess('修改成功');
                                                            gd.table('clientUserTable').reload();
                                                        } else {
                                                            gd.showError('修改失败 ' + (msg.resultMsg || ''));
                                                        }
                                                        dom.close();
                                                    })
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
                                            var editWindow = new Vue({
                                                el: '#add_dept',
                                                data: {
                                                    selectValue: '',
                                                    selectPolicyValue: '',
                                                    policyArray: app.policyArray,
                                                    treeWindowConfig: {
                                                        id: 'treeWindowDepartment',
                                                        simpleData: true,//简单模式，默认为true
                                                        data: app.deptTreeData,
                                                        // linkable: false,
                                                        onSelect: function (node) {
                                                            editWindow.selectValue = node.name;
                                                            app.departmentId = node.id;
                                                            editWindow.$refs.customSelect.isDroped = false
                                                        },
                                                        onChange: function (nodes) {

                                                        },
                                                        ready: function (data) {
                                                        }
                                                    }
                                                },
                                                mounted: function() {
                                                    $.each(raw, function (index, val) {
                                                        $('#editWind span#edit_'+ index +'').html(val);
                                                        if (index == 'deptguid') {
                                                            setTimeout(function() {
                                                                $('#editWind #treeWindowDepartment span[data-id="'+ val +'"]').trigger("click");
                                                            }, 20)
                                                        }
                                                        if (index == 'policyid') {
                                                            setTimeout(function() {
                                                                editWindow.selectPolicyValue = val;
                                                            }, 20)
                                                        }
                                                    })

                                                    validateDep = gd.validate('#add_dept_form', {
                                                        autoPlaceholer: true,
                                                    });
                                                }
                                            })
                                        },
                                        end: function (dom) {//参数为当前窗口dom对象
                                            // gd.showSuccess('窗口关闭了');
                                        }
                                    });
                                }
                            },
                            {
                                icon: 'icon-delete',
                                text: '删除',
                                action: function (cell, row, raw) {
                                    gd.showConfirm({
                                        id: 'wind',
                                        content: '确定要删除吗?',
                                        btn: [{
                                            text: '确定',
                                            class: 'gd-btn-danger', //也可以自定义类
                                            action: function (dom) {
                                                gd.post(ctx + '/clientUser/deleteClientUser', {id: raw.id}, function(msg) {
                                                    if (msg.resultCode == 0) {
                                                        gd.showSuccess('删除成功');
                                                        gd.table('clientUserTable').reload();
                                                    } else {
                                                        gd.showError('删除失败 ' + (msg.resultMsg || ''));
                                                    }
                                                })
                                            }
                                        }, {
                                            text: '取消',
                                            action: function () {
                                            }
                                        }],
                                        success: function (dom) {
                                        },
                                        end: function (dom) {
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
            this.getPolicyList();
            this.getTreeData();
        },
        methods: {
            selectChange: function (data) {
                log(data)
            },
            getTreeData: function () {
                // 获取部门树
                gd.get(ctx + '/department/getDepartmentNodesByLoginUser', '', function(data) {
                    app.deptTreeData = data;
                    gd.tree('treeDepartment').setData(data);
                });
            },
            getPolicyList: function() {
                var _this = this;
                gd.get(ctx + '/policy/getAllPolicys', '', function(msg) {
                    if (msg.resultCode == '0') {
                        _this.policyArray = msg.data;
                    }
                })
            }
        }
    });
</script>
</body>
