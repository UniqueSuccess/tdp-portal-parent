<!--<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>-->
<head>
    <style>
        .gd-toolbar{
            padding: 0 16px;
        }
        .gd-table-wrapper{
            height: calc(100% - 56px);
        }

        .left-menu{
            position: fixed;
            left: 0;
            top: 0;
            bottom: 0;
            white-space: nowrap;
            width: 56px;
            background: #232D3B;
            user-select: none;
        }
        .main-content{
            width: 100%;
            position: absolute;
            top: 0;
            bottom: 0;
        }
        .left-box{
            width: 240px;
            height: 100%;
            float: left;
            overflow: auto;
        }
        .right-box{
            height: 100%;
            margin-left:240px;
            background-color: #fff;
        }

        .container {
            height: calc(100% - 35px);
            width: calc(100% - 80px);
            margin: 15px 40px 0 40px;
        }
        .container .row {
            padding: 5px 0;
            overflow: auto;
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


    </style>
</head>

<body>
<div id="app" class="gd-right-content" v-cloak>
    <div class="main-content">
        <div class="left-box">
            <gd-tree :config='treeConfig' class="gd-tree-gray"></gd-tree>
        </div>
        <div class="right-box">
            <gd-toolbar :config="toolbarConfig"></gd-toolbar>
            <gd-table :config="tableConfig"></gd-table>
        </div>
    </div>
</div>
<!--新增弹框-->
<script id="add_dept_temp" class="gd-none" type="text/html">
    <div class="container" id="add_dept" v-cloak>
        <form id="add_dept_form">
            <div class="row">
                <label class="gd-label-required">部门名称</label>
                <input type="text" class="gd-input gd-input-lg" gd-validate="required" name="name">
            </div>
            <div class="row">
                <label class="">上级部门</label>
                <gd-select placeholder="请选择" v-model="selectValue" ref="customSelect" name="parentId" class="gd-select-lg">
                    <gd-tree :config='treeWindowConfig'></gd-tree>
                </gd-select>
            </div>
            <div class="row">
                <label class="">安全策略</label>
                <gd-select placeholder="请选择" v-model="selectPolicyValue" class="gd-select-lg" name="policyId">
                    <gd-option v-for="item in policyArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
            <div class="row">
                <label class="">部门负责人</label>
                <input type="text" class="gd-input gd-input-lg" maxlength="20" gd-validate="exceptSpecialChar" name="owner">
            </div>
            <div class="row">
                <label class="">联系方式</label>
                <input type="text" class="gd-input gd-input-lg" gd-validate="phone" name="departmentTel">
            </div>
        </form>
    </div>
</script>

<script>
    var validateDep = null;
    var app = new Vue({
        el: '#app',
        data: {
            departmentId: '1', // 新增弹框中用到此值
            leftDeptId: '1', // 左侧部门树选中的部门id
            deptTreeData: [],
            policyArray: [], // 策略列表
            //工具栏配置
            toolbarConfig: [
                {
                    type: 'button',
                    icon: 'icon-add',
                    title: '添加',
                    action: function (dom) {
                        gd.showLayer({
                            id: 'addWind',//可传一个id作为标识
                            title: '新建部门',//窗口标题
                            content: $('#add_dept_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
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
                                        var postData = $('#addWind #add_dept_form').serializeJSON();
                                        postData.parentId = app.departmentId;
                                        gd.post(ctx + '/department/add', postData, function (msg) {
                                            if (msg.resultCode == '0') {
                                                gd.showSuccess('保存成功');
                                                app.getTreeData();
                                            } else {
                                                gd.showError('保存失败 ' + (msg.resultMsg || ''));
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
                                var addWindow = new Vue({
                                    el: '#add_dept',
                                    data: {
                                        selectValue: '',
                                        selectPolicyValue: '1',
                                        policyArray: app.policyArray,
                                        treeWindowConfig: {
                                            id: 'treeWindowDepartment',
                                            simpleData: true,//简单模式，默认为true
                                            data: app.deptTreeData,
                                            // linkable: false,
                                            onSelect: function (node) {
                                                addWindow.selectValue = node.name;
                                                app.departmentId = node.id;
                                                addWindow.$refs.customSelect.isDroped = false
                                            },
                                            onChange: function (nodes) {
                                            },
                                            ready: function (data) {
                                                $('#treeWindowDepartment span[data-id="'+ app.leftDeptId +'"').trigger("click");
                                            }
                                        }
                                    },
                                    mounted: function() {
                                        validateDep = gd.validate('#add_dept_form', {
                                            autoPlaceholer: true,
                                        });
                                    }
                                });
                            },
                            end: function (dom) {//参数为当前窗口dom对象
                                // gd.showSuccess('窗口关闭了');
                            }
                        });
                    }
                }
            ],
            treeConfig: {
                id: 'treeDepartment',
                simpleData: true,//简单模式，默认为true
                data: [],
                // linkable: false,
                onSelect: function (node) {
                    gd.table('departmentTable').reload(false, {pid: node.id});
                    app.leftDeptId = node.id;
                },
                onChange: function (nodes) {
                },
                ready: function (data) {
                }
            },
            //表格配置
            tableConfig: {
                id: 'departmentTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
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
                    url: ctx + '/department/datalist',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.name,
                                obj.parentName || '--',
                                obj.policyName || '--',
                                obj.owner || '--',
                                obj.departmentTel || '--',
                                obj.id
                            ]
                        });
                        return data;
                    },
                    //请求参数
                    data: {
                        pid: 1
                    }
                },
                columns: [
                    {
                        name: 'name',
                        head: '部门名称',
                        title: true
                    },
                    {
                        name: 'parentName',
                        head: '上级部门',
                        title: true
                    },
                    {
                        name: 'policyName',
                        head: '安全策略',
                        title: true
                    },
                    {
                        name: 'owner',
                        head: '负责人',
                        title: true
                    },
                    {
                        name: 'departmentTel',
                        head: '电话',
                        title: true
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
                                        content: $('#add_dept_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
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

                                                    var postData = $('#editWind #add_dept_form').serializeJSON();
                                                    postData.id = raw.id;
                                                    postData.parentId = app.departmentId;
                                                    gd.post(ctx + '/department/updateDept', postData, function (msg) {
                                                        if (msg.resultCode == '0') {
                                                            gd.showSuccess('修改成功');
                                                            app.getTreeData();
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
                                                        $('#editWind input[name=' + index + ']').val(val);
                                                        if (index == 'parentId') {
                                                            setTimeout(function() {
                                                                $('#editWind #treeWindowDepartment span[data-id="'+ val +'"]').trigger("click");
                                                            }, 20)
                                                        }
                                                        if (index == 'policyId') {
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
                                                gd.post(ctx + '/department/delete', {id: raw.id}, function(msg) {
                                                    if (msg.resultCode == 0) {
                                                        gd.showSuccess('删除成功');
                                                        app.getTreeData();
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
            this.getTreeData();
            this.getPolicyList();
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
                    setTimeout(function() {
                        $('#treeDepartment span[data-id="'+ app.leftDeptId +'"]').trigger("click");
                    }, 20)
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
