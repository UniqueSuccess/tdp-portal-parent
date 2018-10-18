<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/system/setting/index.css" rel="stylesheet" type="text/css"/>

    <!--<link href="${ctxCss}/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />-->
    <!--<link href="${ctxCss}/workorder/pending/index.css" rel="stylesheet" type="text/css" />-->
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="top-bar">
        <div class="top-title">系统设置</div>
    </div>
    <div class="content-box">
        <ul class="titleTab" id="titleTabul">
            <li class="netset" data-class="net">网络</li>
            <li class="accountset titleTabactive" data-class="account">账户</li>
            <li class="serverset" data-class="server">服务器</li>
        </ul>
        <div class="main-contentall">
            <div class="netcon none">
                <div class="aboutmain">
                    <form action="${ctx}/netconfig/savenetconfig" method="post" id="beanForm">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="boxnet">
                            <div class="netset col-md-12">
                                <c:forEach items="${data}" var="mo" varStatus="status">
                                    <div class="col-md-4 netsetleft">
                                        <ul>
                                            <li class="col-md-12 ethsa">${mo.name}</li>
                                            <li class="col-md-12" style="height:30px;margin-top:30px;">
                                                <div class="width-text-5 left">
                                                    <span class="right marginl-10">IP地址</span>
                                                </div>
                                                <input type="hidden" name="netConfigs[${status.index}].ethname" value="${mo.name}"/>
                                                <input class="col-md-6 marginl-20 isIp" type="text" style="height:30px;border:1px solid #eaedf1;" name="netConfigs[${status.index}].addr"
                                                       value="${mo.addr}" placeholder="xxx.xxx.xxx.xxx"/>
                                            </li>
                                            <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                <div class="width-text-5 left">
                                                    <span class="right">子网掩码</span>
                                                </div>
                                                <input class="col-md-6 marginl-20 ischildnum" name="netConfigs[${status.index}].mask" type="text" style="height:30px;border:1px solid #eaedf1;"
                                                       value="${mo.mask}" placeholder="xxx.xxx.xxx.xxx"/>
                                            </li>
                                            <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                <div class="width-text-5 left">
                                                    <span class="right">网关</span>
                                                </div>
                                                <input class="col-md-6 marginl-20 isnetwork" name="netConfigs[${status.index}].gateway" type="text" style="height:30px;border:1px solid #eaedf1;"
                                                       value="${mo.gateway}" placeholder="xxx.xxx.xxx.xxx"/>
                                            </li>
                                        </ul>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="padding-vertical-xxl">
                            <button type="button" class="btn-save" id="save_config">保存</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="accountcon">
                <div class="accounttop">
                    <a id="bar_add_account" class="bar-item bar-item-icon iconfont icon-btn-add" title="添加账户"></a>
                </div>
                <div class="accountshow">
                    <table id="accountTable" cellspacing="0" cellpadding="0" border="0" width="100%">
                        <thead>
                        <tr>
                            <th>真实姓名</th>
                            <th>账户名称</th>
                            <th>账户角色</th>
                            <th>策略控制</th>
                            <th>电话</th>
                            <th style="text-align:center;">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <div class="servercon none">33</div>
        </div>
    </div>
</div>
<div id="add_user_wind" class="none">
    <div class="wind-box">
        <form class="padding-normal j-add-account-form">
            <input type="hidden" name="departmentListStr" value=""/>
            <input type="hidden" name="navigationListStr" value=""/>
            <div class="info">
                <label for="">基本信息</label>
                <div class="infocon">
                    <div class="wind-row cf">
                        <label for="" class="wind-label label-required">账户名</label>
                        <input type="text" class="form-input wind-normal-input" name="userName" maxlength="20">
                    </div>
                    <div class="wind-row cf">
                        <label for="" class="wind-label label-required">真实姓名</label>
                        <input type="text" class="form-input wind-normal-input" name="name" maxlength="20">
                    </div>
                    <div class="wind-row cf">
                        <label for="" class="wind-label label-required">密码</label>
                        <input type="password" id="pass" class="form-input wind-normal-input" name="password" maxlength="20">
                    </div>
                    <div class="wind-row cf">
                        <label for="" class="wind-label label-required">确认密码</label>
                        <input type="password" class="form-input wind-normal-input" name="repassword" maxlength="20">
                    </div>
                    <div class="">
                        <label for="" class="wind-label">账户类别</label>
                        <select class="" name="roleType" id="systemauthlist">
                            <option value="1">系统管理员</option>
                            <option value="2">系统操作员</option>
                            <option value="3">系统审计员</option>
                        </select>
                    </div>
                    <div class="wind-row cf">
                        <label for="" class="wind-label label-required">电话</label>
                        <input type="text" class="form-input wind-normal-input" name="phone" maxlength="20">
                    </div>
                </div>
            </div>
            <div class="authset">
                <label for="">权限设置</label>
                <div class="authcon">
                    <div class="dept">
                        <div class="wind-row cf">
                            <label for="" class="wind-label">部门权限</label>
                            <div id="depttree" class="deptTree ztree"></div>
                        </div>
                    </div>
                    <div class="navcation">
                        <div class="wind-row cf">
                            <label for="" class="wind-label">功能权限</label>
                            <ul id="navtree" class="deptTree ztree"></ul>
                        </div>
                    </div>
                    <div class="">
                        <label for="" class="wind-label">操作权限</label>
                        <select class="" name="readonly" id="authreadonly">
                            <option value="1">只读权限</option>
                            <option value="0">读写权限</option>
                        </select>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script id="temp_opt_box" type="text/html">
    <div class="table-opt-box">
        <i class="iconfont icon-nav-system table-opt-icon"></i>
        <div class="opt-hover-box">
            <div class="opt-hover-row j-opt-hover-edit" data-id="{{id}}">
                <i class="iconfont icon-btn-edit text-sm"></i>
                <span class="text-sm margin-left-xs">编辑</span>
            </div>
            <div class="opt-hover-row j-opt-hover-delete" data-id="{{id}}">
                <i class="iconfont icon-btn-delete text-sm"></i>
                <span class="text-sm margin-left-xs">删除</span>
            </div>
        </div>
    </div>
</script>
<!--<script src="${ctxJs}/plugins/echarts/echarts.common.min.js"></script>-->
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<!--<script src="${ctxJs}/plugins/ueditor/ueditor.config.js"></script>-->
<!--<script src="${ctxJs}/plugins/ueditor/ueditor.all.js"></script>-->
<!--<script src="${ctxJs}/plugins/jquery.PrintArea/jquery.PrintArea.js"></script>-->
<script src="${ctxJs}/plugins/validate/jquery.validate.js"></script>
<script src="${ctxJs}/plugins/validate/messages_zh.js"></script>
<script src="${ctxJs}/validateExtent.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
<!--<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js" type="text/javascript"></script>-->
<!--<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>-->

<script src="${ctxJs}/system/setting/index.js"></script>