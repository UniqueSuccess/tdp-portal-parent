<!--userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<head>
    <title>设置</title>
    <link href="${ctxCss}/system/setting/index.css" rel="stylesheet" type="text/css"/>
    <style>
        .setting-content{
            width: 100%;
            padding: 20px;
            box-sizing: border-box;
            position: absolute;
            top: 0;
            bottom: 0;
        }
        .gd-toolbar{
            background-color: #fff;
            padding: 0 0 0 10px;
        }
        .gd-table-wrapper{
            height: calc(100% - 56px);
        }
        .wind-half{
            width: 310px;
            float: left;
        }
        .wind-half .gd-radio{
            width: auto !important;
        }
        .permission-wrap{
            width: 200px;
            height: 260px;
            overflow: auto;
            display: inline-block;
            vertical-align: top;
        }
        .permission-wrap .gd-tree{
            padding: 10px;
            border-color: #e1e1e1;
        }
        .client-item{
            margin: 10px 0;
            color: #333;
        }
        .client-title .icon-help{
            position: relative;
            top: 1px;
            left: 5px;
            color: #cfd6e1;
        }
        .client-item .row{
            margin:10px 0;
            padding-left: 20px;
        }
        .client-item .row label{
            padding-right: 16px;
        }
        .client-item .icon-file{
            margin: 0 10px 0 -30px;
            color: #58a5e3;
        }
        .client-item form{
            display: inline-block;
            position: relative;
            width: 600px;
        }
        .client-item .uploadFile{
            position: absolute;
            top: 0;
            left: 0;
            opacity: 0;
            cursor: pointer;
            width: 380px;
            height: 30px;
            overflow: hidden;
        }
        .copy-btn:hover, .copy-btn:focus{
            color: #333;
            background-color: #f5f5f5;
        }
        .code-value{
            padding: 16px 0;
            text-align: center;
            font-size: 28px;
        }
        .gd-layer .gd-layer-body .gd-layer-footer{
            border-top: none;
        }
        #generateWind .gd-layer-footer{
            text-align: center;
        }
        .remark{
            padding: 40px 20px 0;
            color: #999;
            line-height: 24px;
        }
        .gd-tab-horizontal ul.gd-tab-bar li.gd-tab-li>span{
            font-family: Arial,'PingFang SC','Microsoft YaHei','Segoe UI',Helvetica,FreeSans,Arimo,sans-serif;
        }
        .forbidwrite{
            padding-right: 0px!important;
            margin-left:15px;
        }
        .forbidwrite-row{
            padding-top: 10px!important;
        }
        .forbidwrite-row label:first-child{
            padding-left: 13px;
            padding-right: 0;
        }
    </style>
</head>
<body>
<div id="app" class="gd-right-content" v-cloak>
    <div class="setting-content" >
        <gd-tab style="height: 100%" @change="tabChange">
            <gd-tab-item label="网口配置">
                <div class="netcon">
                    <div class="aboutmain">
                        <form action="${ctx}/netconfig/savenetconfig" method="post" id="beanForm">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <div class="boxnet">
                                <div class="netset col-md-12">
                                    <c:forEach items="${data}" varStatus="status">
                                    <c:set var="mo" value="${data[status.index]}"/>
                                        <div class="col-md-4 netsetleft">
                                            <ul>
                                                <li class="col-md-12 ethsa">${mo.name}</li>
                                                <li class="col-md-12" style="height:30px;margin-top:30px;">
                                                    <div class="width-text-5 left">
                                                        <span class="right">IP地址</span>
                                                    </div>
                                                    <input type="hidden" name="netConfigs[${status.index}].ethname"
                                                           value="${mo.name}"/>
                                                    <input class="gd-input gd-input-lg isIp" type="text"
                                                           style="height:30px;border:1px solid #eaedf1;"
                                                           name="netConfigs[${status.index}].addr"
                                                           value="${mo.addr}" placeholder="0.0.0.0" gd-validate="ip" />
                                                </li>
                                                <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                    <div class="width-text-5 left">
                                                        <span class="right">子网掩码</span>
                                                    </div>
                                                    <input class="gd-input gd-input-lg ischildnum"
                                                           name="netConfigs[${status.index}].mask" type="text"
                                                           style="height:30px;border:1px solid #eaedf1;"
                                                           value="${mo.mask}" placeholder="255.255.255.255" gd-validate="ip" gdv-msg="子网掩码格式不正确" />
                                                </li>
                                                <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                    <div class="width-text-5 left">
                                                        <span class="right">网关</span>
                                                    </div>
                                                    <input class="gd-input gd-input-lg isnetwork"
                                                           name="netConfigs[${status.index}].gateway" type="text"
                                                           style="height:30px;border:1px solid #eaedf1;"
                                                           value="${mo.gateway}" placeholder="0.0.0.0" gd-validate="ip" gdv-msg="网关格式不正确"/>
                                                </li>
                                            </ul>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="remark">
                                <div>备注：</div>
                                <ul>
                                    <li>1. 旁路方式下，eth0为管理口需要配置IP以及网关，单线模式使用eth1作为数据转发以及镜像口，双线模式eth2为数据镜像口，eth1为数据转发口</li>
                                    <li>2. 网桥模式下，eth1、eth2分别连接不同的交换机或路由器，eth3为双机热备心跳口。 802.1X模式下，eth0为管理口，需要配置IP以及网关。</li>
                                    <li>3. 策略路由模式下，eth0为管理口配置IP无需配置网关，eth1为数据转发口需要配置IP以及网关。</li>
                                </ul>
                            </div>
                            <div class="padding-vertical-xxl gd-margin-left-lg">
                                <button type="button" class="gd-btn gd-margin-top-lg" id="save_config" @click="doSave()">保存</button>
                            </div>
                        </form>
                    </div>
                </div>
            </gd-tab-item>
            <gd-tab-item label="用户" class="gd-padding-0">
                <gd-toolbar :config="toolbarConfig"></gd-toolbar>
                <gd-table :config="tableConfig"></gd-table>
            </gd-tab-item>
            <gd-tab-item label="客户端">
                <div class="client-item">
                    <div class="client-title">安装
                        <i class="gd-icon icon-help" tooltipnew="将安装包上传至服务器后，终端用户可通过下载链接下载客户端"></i>
                    </div>
                    <div class="wind-content gd-margin-v-md">
                        <div class="wind-row row">
                            <label>安装包</label>
                            <form id="installform" action="${ctx}/systemSetting/uploadClientPackage" method="post"
                                  enctype="multipart/form-data" target="clientUpdate" class="">
                                <input type="hidden" name="MAX_FILE_SIZE" value="100000000">
                                <input type="text" id="clientInstallPath" class="gd-input gd-input-lg">
                                <a href="javascript:void(0);" class=""><i class="gd-icon icon-file lookview"
                                                                          title="浏览"></i></a>
                                <input type="file" name="packageFile" id="clientInstall" accept=".exe"
                                       class="uploadFile gd-input gd-input-lg" value="浏览" gd-validate="required">
                                <%--<input type="submit" class="sureButton" id="install" value="上传">--%>
                                <button class="gd-btn-cancel" id="install" disabled>上传</button>
                                <a class="gd-btn-cancel copy-btn" id="copy_link" @click.stop="copyLink()" href="javascript:;">复制下载链接</a>
                                <textarea id="link_area" style="height:0;width:0;opacity: 0; position: absolute;"></textarea>
                                <span id="clientInstallTip" class="none">请选择文件</span>
                                <iframe id="clientUpdate" name="clientUpdate" style="display:none;"></iframe>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="client-item">
                    <div class="client-title">升级
                        <i class="gd-icon icon-help" id="update_hint" tooltipnew="上传成功后客户端自动升级"></i>
                    </div>
                    <div class="wind-content gd-margin-v-md">
                        <div class="wind-row row">
                            <label>升级包</label>
                            <form id="updataform" action="${ctx}/systemSetting/uploadClientUpdate" method="post"
                                  enctype="multipart/form-data" target="serverUpdate">
                                <input type="hidden" name="MAX_FILE_SIZE" value="100000000">
                                <input type="text" id="clientUpdataPath" class="gd-input gd-input-lg">
                                <a href="javascript:void(0);" class=""><i class="gd-icon icon-file lookview"
                                                                          title="浏览"></i></a>
                                <input type="file" name="updateFile" id="clientUpdata" accept=".gz" class="uploadFile gd-input gd-input-lg"
                                       value="浏览">
                                <%--<input type="submit" class="sureButton" id="updata" value="上传">--%>
                                <button class="gd-btn-cancel" id="updata" disabled>上传</button>
                                <span id="clientUpdataTip" class="none">请选择文件</span>
                                <iframe id="serverUpdate" name="serverUpdate" style="display:none;"></iframe>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="client-item">
                    <div class="client-title">卸载
                        <i class="gd-icon icon-help" tooltipnew="在客户端最小化或未开启的状态下按[Ctrl+Alt+Shift+0]生成请求码将其输入下方文本框即可"></i>
                    </div>
                    <div class="row">
                        <form id="code_form">
                            <label for="">请求码</label>
                            <input type="text" class="gd-input gd-input-lg" placeholder="客户端生成的六位数字" v-model="clientCode" gd-validate="required six" />
                            <button class="gd-btn-cancel" @click.prevent="generateCode()">生成卸载码</button>
                        </form>
                    </div>
                </div>
                <div class="client-item gd-padding-top-sm">
                    <div class="client-title">未保护文档设置
                        <i class="gd-icon icon-help" tooltipnew="开启只读模式后，未在TDP客户端保护下的文档将无法编辑；&#10;该功能设置需客户端重启电脑后生效"></i>
                    </div>
                    <div class="row forbidwrite-row">
                    	<label>只读</label>
                        <label class="gd-switch forbidwrite">
                             <input type="checkbox" v-model="forbidwrite" :true-value='1' :false-value='0' @change="updateForbidwrite()">
                             <i></i>
                         </label>
                    </div>
                </div>
            </gd-tab-item>
            <gd-tab-item label="日志清理">
                <div class="client-item">
                    <div class="row">
                        <label for="">流转日志</label>
                        <gd-select class="gd-select-lg" v-model="outClearDays">
                            <gd-option value="30">最近30天</gd-option>
                            <gd-option value="90">最近90天</gd-option>
                            <gd-option value="180">最近180天</gd-option>
                            <gd-option value="3665">最近365天</gd-option>
                        </gd-select>
                        <button class="gd-btn-cancel" @click="clearOutDays()">清理</button>
                    </div>
                </div>
                <div class="client-item">
                    <div class="row">
                        <label for="">操作日志</label>
                        <gd-select class="gd-select-lg" v-model="operClearDays">
                            <gd-option value="30">最近30天</gd-option>
                            <gd-option value="90">最近90天</gd-option>
                            <gd-option value="180">最近180天</gd-option>
                            <gd-option value="365">最近365天</gd-option>
                        </gd-select>
                        <button class="gd-btn-cancel gd-text-top" @click="clearOperDays()">清理</button>
                    </div>
                </div>
            </gd-tab-item>
        </gd-tab>
    </div>
</div>

<%--用户新增--%>
<script id="add_user_temp" class="gd-none" type="text/html">
    <div class="container" id="add_user" v-cloak>
        <form id="add_user_form">
            <div class="wind-half">
                <div class="row">
                    <label class="gd-label-required">用户名</label>
                    <input type="text" class="gd-input" gd-validate="required maxLength" gdv-maxLength="32" name="userName">
                </div>
            </div>
            <div class="wind-half">
                <div class="row">
                    <label class="gd-label-required">姓名</label>
                    <input type="text" class="gd-input" gd-validate="required maxLength" gdv-maxLength="32" name="name">
                </div>
            </div>
            <div class="wind-half">
                <div class="row">
                    <label class="gd-label-required">密码</label>
                    <input type="password" class="gd-input" name="password" id="psw" gd-validate="minLength" gdv-minlength="6">
                </div>
            </div>
            <div class="wind-half">
                <div class="row">
                    <label class="gd-label-required">确认密码</label>
                    <input type="password" class="gd-input" name="repassword" gd-validate="minLength equalTo" gdv-equal="psw"  gdv-minlength="6">
                </div>
            </div>
            <div class="wind-half">
                <div class="row">
                    <label class="">角色</label>
                    <gd-select placeholder="请选择" v-model="roleTypeValue" name="roleType" @change="roleTypeChange()">
                        <gd-option value="1">系统管理员</gd-option>
                        <gd-option value="2">系统操作员</gd-option>
                        <gd-option value="3">系统审计员</gd-option>
                    </gd-select>
                </div>
                <div class="row">
                    <label>电话</label>
                    <input type="text" class="gd-input" gd-validate="phone" name="phone">
                </div>
                <div class="row">
                    <label class="gd-margin-top-lg gd-label-required">部门权限</label>
                    <div class="permission-wrap">
                        <gd-tree :config='treeWindowConfig'></gd-tree>
                    </div>
                </div>
            </div>
            <div class="wind-half">                
                <div class="row" style="padding: 10px 0;">
                    <label class="">操作权限</label>
                    <label class="gd-radio">
                        <input type="radio" value="0" name="readonly" checked>
                        <i></i>
                        读写
                    </label>
                    <label class="gd-radio">
                        <input type="radio" value="1" name="readonly">
                        <i></i>
                        只读
                    </label>
                </div>
                <div class="row" style="margin-top: 45px;">
                    <label class="gd-margin-top-lg gd-label-required">功能权限</label>
                    <div class="permission-wrap">
                        <gd-tree :config='functionWindowConfig'></gd-tree>
                    </div>
                </div>
            </div>
        </form>
    </div>
</script>

<script src="${ctxJs}/jquery.form.js" type="text/javascript"></script>
<script>
    // 不同登录用户下的角色
    var roleTypeList = JSON.parse('${roleTypeList}');
    var forbidwrite = '${forbidwrite}';
    var filterArr = roleTypeList.map(function(item, index) {
        return {
            label: item[index + 1],
            value: index + 1
        }
    });
    filterArr.unshift({
        label: '超级管理员',
        value: 0
    });

    var userId = '${userId}';
    var roleTypeJson = {
        name: 'roleTypeName',
        head: '角色',
        title: true
    };
    if (userId == 1) {
        roleTypeJson = {
            name: 'roleTypeName',
            head: '角色',
            filterName: 'roleType',//高级查询字段名，不写为name
            filters: filterArr
        }
    }

    var validateUser = null; // 用户校验
    var codeValidate = null; // 卸载码校验
    var networkValidate = null;

    var app = new Vue({
        el: '#app',
        data: {
        	forbidwrite: forbidwrite,
            toolbarConfig: [
                {
                    type: 'button',
                    icon: 'icon-add',
                    title: "新建",
                    action: function (dom) {
                        gd.showLayer({
                            id: 'addWind',//可传一个id作为标识
                            title: '新建用户',//窗口标题
                            content: $('#add_user_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                            //url: './layer_content.html',//也可以传入url作为content,
                            size: [700, 600],//窗口大小，直接传数字即可，也可以是['600px','400px']
                            //autoFocus:true,//自动对输入框获取焦点，默认为ture
                            btn: [
                                {
                                    text: '确定',
                                    action: function (dom) {
                                        if (!validateUser.valid()) {
                                            return false;
                                        }

                                        var postData = $('#add_user_form').serializeJSON();
                                        postData.password = encrypt(postData.password).toUpperCase();
                                        postData.repassword = encrypt(postData.password).toUpperCase();
                                        var deptCheckedNodes = gd.tree('treeWindowDepartment').getCheckedNodes();
                                        var deptCheckedNodesId = deptCheckedNodes.map(function(item, index) {
                                            return item.id;
                                        });
                                        postData.departmentListStr = deptCheckedNodesId.join(',');

                                        var functionCheckedNodes = gd.tree('functionWindow').getCheckedNodes();
                                        var functionCheckedNodesId = functionCheckedNodes.map(function(item, index) {
                                            return item.id;
                                        });
                                        postData.navigationListStr = functionCheckedNodesId.join(',');

                                        if (postData.departmentListStr == '') {
                                            gd.showWarning('请选择部门权限');
                                            return false;
                                        }

                                        if (postData.navigationListStr == '') {
                                            gd.showWarning('请选择功能权限');
                                            return false;
                                        }

                                        gd.post(ctx + '/systemSetting/user/addOrUpdateUser', postData, function (msg) {
                                            if (msg.resultCode == '0') {
                                                gd.showSuccess('添加成功');
                                                gd.table('userTable').reload();
                                            } else {
                                                gd.showError('添加失败 ' + (msg.resultMsg || ''));
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
                                    el: '#add_user',
                                    data: {
                                        roleTypeArr: filterArr, // 角色列表
                                        roleTypeValue: '1', // 选中的角色值
                                        treeWindowConfig: { // 部门权限
                                            id: 'treeWindowDepartment',
                                            simpleData: true,//简单模式，默认为true
                                            showCheckBox: true,
                                            data: app.deptTreeData,
                                            // linkable: false,
                                            onSelect: function (node) {

                                            },
                                            onChange: function (nodes) {
                                            },
                                            ready: function (data) {

                                            }
                                        },
                                        functionWindowConfig: { // 功能权限
                                            id: 'functionWindow',
                                            simpleData: true,//简单模式，默认为true
                                            showCheckBox: true,
                                            data: [],
                                            // linkable: false,
                                            onSelect: function (node) {

                                            },
                                            onChange: function (nodes) {
                                            },
                                            ready: function (data) {

                                            }
                                        }
                                    },
                                    mounted: function() {
                                        validateUser = gd.validate('#add_user_form');
                                        this.getFunctionTreeData(1);
                                    },
                                    methods: {
                                        roleTypeChange: function() {
                                            this.getFunctionTreeData(this.roleTypeValue);
                                        },
                                        getFunctionTreeData: function(roleType) { // 功能树
                                            var _this = this;
                                            var postData = {
                                                roleType: roleType,
                                                guid: ''
                                            }
                                            // 获取部门树
                                            gd.get(ctx + '/system/navigation/getNavigationListByRoleType', postData, function(msg) {
                                                gd.tree('functionWindow').setData(msg.data);
                                            });
                                        }
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
                    placeholder: "用户名",
                    action: function (val) {
                        gd.table('userTable').reload(1, {searchStr: val}, false);
                    }
                }
            ],
            tableConfig: {
                id: 'userTable',//给table一个id,调用gd.tableReload('demoTable');可重新加载表格数据并保持当前页码，gd.tableReload('demoTable'，1)，第二个参数可在加载数据时指定页码
                length: 50, //每页多少条,默认50，可选
                curPage: 1, //当前页码，默认1，可选
                lengthMenu: [10, 30, 50, 100], //可选择每页多少条，默认[10, 30, 50, 100]，可选
                enableJumpPage: false, //启用跳页，默认false，可选
                enableLengthMenu: true, //启用可选择每页多少条，默认true，可选
                enablePaging: true,//启用分页,默认true，可选
                orderColumn: '',//排序列
                orderType: 'desc',//排序规则，desc或asc,默认desc
                columnResize: false, //启用列宽调，默认true，可选
                //showFooter: false,//显示footer,默认为true
                //lazy: true,//懒加载数据，调用gd.table('id').reload()对表格数据进行加载,默认为false
                //loading: true,//显示loading,默认为false
                ajax: {
                    //其它ajax参数同jquery
                    url: ctx + '/systemSetting/user/getUserPages',
                    //改变从服务器返回的数据给table
                    dataSrc: function (data) {
                        data.rows = data.rows.map(function (obj) {
                            return [
                                obj.userName || '--',
                                obj.name || '--',
                                obj.roleTypeName || '--',
                                obj.readonlyName || '--',
                                obj.phone || '--',
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
                        name: 'userName',
                        head: '用户名',
                        title: true
                    },
                    {
                        name: 'name',
                        head: '姓名',
                        title: true
                    },
                    roleTypeJson,
                    {
                        name: 'readonly',
                        head: '操作权限',
                        filterName: 'readonly',//高级查询字段名，不写为name
                        filters: [//设置检索条件
                            {
                                label: '读写',
                                value: '0',
                            }, {
                                label: '只读',
                                value: '1'
                            }
                        ],
                       // render: function (cell, row, raw) {//自定义表格内容

                       // }
                    },
                    {
                        name: 'phone',
                        head: '电话',
                        title: true
                    },
                    {
                        name: 'operate',
                        head: '操作',
                        align: 'center',
                        operates: [
                            {
                                icon: 'icon-edit',
                                title: '编辑',//设置图标title
                                disabled: function(cell, row, raw) {
                                    if (raw.guid == 1 || raw.guid == 2 || raw.guid == 3 || raw.guid == 4) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                },
                                action: function (cell, row, raw) {//动作函数,cell为本格数据，row为本行加工后的数据，raw为本行未加工的数据
                                    if (raw.guid == 1 || raw.guid == 2 || raw.guid == 3 || raw.guid == 4) {
                                        gd.showWarning('内置用户不能编辑');
                                        return;
                                    }

                                    gd.showLayer({
                                        id: 'editWind',//可传一个id作为标识
                                        title: '编辑用户',//窗口标题
                                        content: $('#add_user_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                        //url: './layer_content.html',//也可以传入url作为content,
                                        size: [700, 600],//窗口大小，直接传数字即可，也可以是['600px','400px']
                                        //autoFocus:true,//自动对输入框获取焦点，默认为ture
                                        btn: [
                                            {
                                                text: '确定',
                                                action: function (dom) {
                                                    if (!validateUser.valid()) {
                                                        return false;
                                                    }

                                                    var postData = $('#add_user_form').serializeJSON();
                                                    if (postData.password !== '') {
                                                        postData.password = encrypt(postData.password).toUpperCase();
                                                        postData.repassword = encrypt(postData.password).toUpperCase();
                                                    }
                                                    
                                                    var deptCheckedNodes = gd.tree('treeEditDepartment').getCheckedNodes();
                                                    var deptCheckedNodesId = deptCheckedNodes.map(function(item, index) {
                                                        return item.id;
                                                    });
                                                    postData.departmentListStr = deptCheckedNodesId.join(',');

                                                    var functionCheckedNodes = gd.tree('treeEditFunction').getCheckedNodes();
                                                    var functionCheckedNodesId = functionCheckedNodes.map(function(item, index) {
                                                        return item.id;
                                                    });
                                                    postData.navigationListStr = functionCheckedNodesId.join(',');

                                                    if (postData.departmentListStr == '') {
                                                        gd.showWarning('请选择部门权限');
                                                        return false;
                                                    }
                                                    if (postData.navigationListStr == '') {
                                                        gd.showWarning('请选择功能权限');
                                                        return false;
                                                    }

                                                    postData.id = raw.id;
                                                    postData.guid = raw.guid;
                                                    gd.post(ctx + '/systemSetting/user/addOrUpdateUser', postData, function (msg) {
                                                        if (msg.resultCode == '0') {
                                                            gd.showSuccess('修改成功');
                                                            gd.table('userTable').reload();
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
                                                el: '#add_user',
                                                data: {
                                                    roleTypeArr: filterArr, // 角色列表
                                                    roleTypeValue: '1', // 选中的角色值
                                                    treeWindowConfig: { // 部门权限
                                                        id: 'treeEditDepartment',
                                                        simpleData: true,//简单模式，默认为true
                                                        showCheckBox: true,
                                                        data: [],
                                                        // linkable: false,
                                                        onSelect: function (node) {

                                                        },
                                                        onChange: function (nodes) {
                                                        },
                                                        ready: function (data) {

                                                        }
                                                    },
                                                    functionWindowConfig: { // 功能权限
                                                        id: 'treeEditFunction',
                                                        simpleData: true,//简单模式，默认为true
                                                        showCheckBox: true,
                                                        data: [],
                                                        // linkable: false,
                                                        onSelect: function (node) {

                                                        },
                                                        onChange: function (nodes) {
                                                        },
                                                        ready: function (data) {

                                                        }
                                                    }
                                                },
                                                mounted: function() {
                                                    this.getTreeData();
                                                    this.getFunctionTreeData(raw.roleType, raw.guid);

                                                    $.each(raw, function (index, val) {
                                                        if (index !== 'readonly') {
                                                            $('#editWind input[name=' + index + ']').val(val);
                                                        }

                                                        if (index == 'roleType') {
                                                            setTimeout(function() {
                                                                editWindow.roleTypeValue = val;
                                                            }, 20)
                                                        }
                                                        if (index == 'password') {
                                                            $('#editWind input[name=password]').val('');
                                                        }
                                                        if (index == 'readonly') {
                                                            val == '0' ? $('#editWind input[name=readonly]').eq(0).click() : $('#editWind input[name=readonly]').eq(1).click()
                                                        }
                                                    });

                                                    validateUser = gd.validate('#add_user_form');
                                                },
                                                methods: {
                                                    roleTypeChange: function() {
                                                        var guid = this.roleTypeValue == raw.roleType ? raw.guid : '';
                                                        this.getFunctionTreeData(this.roleTypeValue, guid);
                                                    },
                                                    getTreeData: function () { // 部门树
                                                        // 获取部门树
                                                        gd.get(ctx + '/department/getDepartmentTreeByUserId', {userId: raw.id}, function(msg) {
                                                            gd.tree('treeEditDepartment').setData(msg.data);
                                                        });
                                                    },
                                                    getFunctionTreeData: function(roleType, guid) { // 功能树
                                                        var _this = this;
                                                        var postData = {
                                                            roleType: roleType,
                                                            guid: guid
                                                        }
                                                        // 获取部门树
                                                        gd.get(ctx + '/system/navigation/getNavigationListByRoleType', postData, function(msg) {
                                                            gd.tree('treeEditFunction').setData(msg.data);
                                                        });
                                                    }
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
                                icon: 'icon-delete',
                                text: '删除',
                                disabled: function(cell, row, raw) {
                                    if (raw.guid == 1 || raw.guid == 2 || raw.guid == 3 || raw.guid == 4) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                },
                                action: function (cell, row, raw) {
                                    if (raw.guid == 1 || raw.guid == 2 || raw.guid == 3 || raw.guid == 4) {
                                        gd.showWarning('内置用户不能删除');
                                        return;
                                    }
                                    gd.showConfirm({
                                        id: 'wind',
                                        content: '确定要删除吗?',
                                        btn: [{
                                            text: '确定',
                                            class: 'gd-btn-danger', //也可以自定义类
                                            action: function (dom) {
                                                gd.post(ctx + '/systemSetting/user/deleteUser', {userId: raw.id}, function(msg) {
                                                    if (msg.resultCode == 0) {
                                                        gd.showSuccess('删除成功');
                                                        gd.table('userTable').reload();
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
            deptTreeData: [], // 部门权限数据
            functionTreeData: [], // 功能权限数据,
            outClearDays: 30, // 外发日志默认清理时长
            operClearDays: 30, // 操作日志默认清理时长
            clientCode: '' // 客户端生成的六位数字
        },
        mounted: function() {
            this.getTreeData();
            this.getClientData();
        },
        methods: {
            tabChange: function() {
                $('.gd-tooltip').remove();
            },
            updateForbidwrite:function() {
            	var _this = this;
                var postData = {
                    status: _this.forbidwrite
                }
                gd.post(ctx + '/systemSetting/updateForbidwrite', postData, function (msg) {
                    if (msg.resultCode == '0') {
                        gd.showSuccess('配置成功');
                    } else {
                        gd.showError('配置失败 ' + (msg.resultMsg || ''));
                    }
                    _this.forbidwrite = msg.data
                })
                return false;//阻止弹窗自动关闭
            },
            doSave: function() { // 网口配置保存
                networkValidate = gd.validate('#beanForm');
                if (!networkValidate.valid()) {
                    return false;
                }
                gd.showConfirm({
                    id: 'wind',
                    content: '网络配置会自动重启服务器，确认提交?',
                    btn: [{
                        text: '确定',
                        class: 'gd-btn-danger', //也可以自定义类
                        action: function (dom) {
                            var postData = $('#beanForm').serializeJSON();
                            gd.post(ctx + '/systemSetting/savenetconfig', postData, function(msg) {
                                if (msg.resultCode == 0) {
                                    gd.showSuccess('网络配置成功');
                                } else {
                                    gd.showError('网络配置失败 ' + (msg.resultMsg || ''));
                                }
                            });
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
            },
            getTreeData: function () { // 部门树
                var _this = this;
                // 获取部门树
                gd.get(ctx + '/department/getDepartmentNodesByLoginUser', '', function(data) {
                    _this.deptTreeData = data;
                });
            },
            clearOutDays: function() { // 清理外发日志
                var _this = this;
                gd.showConfirm({
                    id: 'wind',
                    content: '日志清理后数据无法恢复！<br/>' + '确定要清理'+ this.outClearDays +'天以前的外发日志吗?',
                    btn: [{
                        text: '确定',
                        class: 'gd-btn-danger', //也可以自定义类
                        action: function (dom) {
                            gd.post(ctx + '/report/deleteFileTransferLog', {clearDays: _this.outClearDays}, function(msg) {
                                if (msg.resultCode == 0) {
                                    gd.showSuccess('清理成功');
                                } else {
                                    gd.showError('清理失败 ' + (msg.resultMsg || ''));
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
            },
            clearOperDays: function() { // 清理操作日志
                var _this = this;
                gd.showConfirm({
                    id: 'wind',
                    content: '日志清理后数据无法恢复！<br/>' + '确定要清理'+ this.operClearDays +'天以前的操作日志吗?',
                    btn: [{
                        text: '确定',
                        class: 'gd-btn-danger', //也可以自定义类
                        action: function (dom) {
                            gd.post(ctx + '/systemLog/deleteOperationLog', {clearDays: _this.operClearDays, logType: 'operationLog'}, function(msg) {
                                if (msg.resultCode == 0) {
                                    gd.showSuccess('清理成功');
                                } else {
                                    gd.showError('清理失败 ' + (msg.resultMsg || ''));
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
            },
            generateCode: function() { // 客户端生成卸载码
                codeValidate = gd.validate('#code_form', {
                    rules: [{//rules用于自定义校验
                        name: 'six',//规则名称，gd-validate="goldencis"
                        msg: '输入的值必须是六位数字',//不合法时的提示信息
                        valid: function (value, el) {//校验方法,第一个参数为输入框的值,第二个参数为输入框本身，返回true为通过
                            return value.length === 6
                        }
                    }]
                });

                if (!codeValidate.valid()) {
                    return false;
                }
                gd.post(ctx + '/systemSetting/generatorCrc32', {code: this.clientCode}, function(msg) {
                    if (msg.resultCode == '0') {
                        gd.showLayer({
                            id: 'generateWind',//可传一个id作为标识
                            title: '卸载码',//窗口标题
                            content: '<div class="code-value">'+ msg.data +'</div>',//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                            //url: './layer_content.html',//也可以传入url作为content,
                            size: [300, 200],//窗口大小，直接传数字即可，也可以是['600px','400px']
                            //autoFocus:true,//自动对输入框获取焦点，默认为ture
                            btn: [{
                                text: '复制',
                                action: function (dom) {//参数为当前窗口dom对象
                                    $('#link_area').val(msg.data);
                                    $('#link_area').select();
                                    document.execCommand("Copy");
                                    dom.close();//或gd.closeLayer(dom);
                                    return false;//阻止弹窗自动关闭
                                }
                            }]
                        });
                    } else {
                        gd.showError('生成卸载码失败');
                    }
                })
                return false;
            },
            getClientData: function() { // 获取客户端页面数据
                gd.get(ctx + '/systemSetting/querySubmitPackageInfo', '', function(msg) {
                    if (msg.resultCode == 0) {
                        if (msg.data.package.fileName) { // 安装包
                            $('#clientInstallPath').attr('placeholder', msg.data.package.fileName);
                            $('#install').attr('disabled', true);
                        }
                        if (msg.data.update.fileName) { // 升级包
                            $('#clientUpdataPath').attr('placeholder', msg.data.update.fileName);
                            $('#updata').attr('disabled', true);
                            $('#update_hint').attr('tooltipnew', '上次上传时间：'+ msg.data.update.updateTime +' 上传成功后客户端自动升级');
                        }
                    }
                })
            },
            copyLink: function(href) { // 客户端复制链接
                var domain = window.location.href;
                if(domain.indexOf("/",8) > -1 ){
                    domain = domain.substring(0,domain.indexOf("//")+2) + domain.substring(domain.indexOf("//")+2,domain.indexOf("/",8));
                }
                var content = domain + '/user';
                $('#link_area').val(content);
                $('#link_area').select();
                document.execCommand("Copy");
                gd.showSuccess('复制成功');
            }
        }
    });

    $('body').on('mouseenter', '[tooltipnew]', function() {
        var _this = $(this);
        var msg = $(this)
            .attr('tooltipnew')
            .trim();

        gd.showTip(this, msg, {
            id: 'tips',//如果传一个id，将关闭之前相同id的tip
            time: 0,//默认在3秒内关闭，可以自定义关闭时间，0为不自动关闭
            position: 'right'//设置位置，默认自动
        });
        $(this).one('mouseleave', function() {
            gd.closeTip(tips);
        });
    });

    // 安装包上传
    $('#installform').ajaxForm({
        uploadProgress: function (event, position, total, percentComplete) {
        },
        success: function (msg) {
            if (msg.resultCode == '0') {
                gd.showSuccess('上传成功');
            } else {
                gd.showError("上传失败 " + (msg.resultMsg || ''));
                $('#install').removeAttr('disabled');
            }
        },
        error: function () {
            gd.showError("上传错误！");
        },
        complete: function (xhr) {
            $('#clientInstallTip').hide();
        }
    });


   //    客户端上传
    $('#updataform').ajaxForm({
        uploadProgress: function (event, position, total, percentComplete) {
        },
        success: function (msg) {
            if (msg.resultCode == '0') {
                gd.showSuccess('上传成功');
            } else {
                gd.showError("上传失败 " + (msg.resultMsg || ''));
                $('#updata').removeAttr('disabled');
            }
        },
        error: function () {
            gd.showError("上传错误！");
        },
        complete: function (xhr) {
            $('#clientUpdataTip').hide();
        }
    });


    changeq("clientUpdataPath", "clientUpdata", '#updata');
    changeq("clientInstallPath", "clientInstall", '#install');
    // 上传
    function changeq(a, b, obj) {
        var copyFile = document.getElementById(a);
        var trueFile = document.getElementById(b);
        trueFile.onchange = function () {
            // 判断是不是火狐
            if (navigator.userAgent.indexOf('Firefox') >= 0) {
                copyFile.value = getFile(this);
            } else {
                copyFile.value = getFile(this).substring(12);
            }
            $(obj).removeAttr('disabled');
        }
//        trueFile.nextSibling.disabled = false;
        function getFile(obj) {
            if (obj) {
                return obj.value;
            }
        }
    }
</script>
</body>
