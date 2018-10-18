<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/system/setting/index.css" rel="stylesheet" type="text/css"/>

    <link href="${ctxCss}/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="top-bar">
        <div class="top-title">系统设置</div>
    </div>
    <div class="content-box">
        <ul class="titleTab" id="titleTabul">
            <li class="netset titleTabactive" data-class="net">网络</li>
            <li class="accountset" data-class="account">账户</li>
            <li class="serverset" data-class="server">客户端</li>
            <li class="clearset" data-class="clear">日志清理</li>
            <li class="decodeset" data-class="decode">水印解码</li>
            <li class="cascadeset" data-class="cascade">日志级联</li>
        </ul>
        <div class="main-contentall">
            <div class="netcon">
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
                                                    <span class="right">IP地址</span>
                                                </div>
                                                <input type="hidden" name="netConfigs[${status.index}].ethname"
                                                       value="${mo.name}"/>
                                                <input class="col-md-6 marginl-20 isIp" type="text"
                                                       style="height:30px;border:1px solid #eaedf1;"
                                                       name="netConfigs[${status.index}].addr"
                                                       value="${mo.addr}"/>
                                            </li>
                                            <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                <div class="width-text-5 left">
                                                    <span class="right">子网掩码</span>
                                                </div>
                                                <input class="col-md-6 marginl-20 ischildnum"
                                                       name="netConfigs[${status.index}].mask" type="text"
                                                       style="height:30px;border:1px solid #eaedf1;"
                                                       value="${mo.mask}"/>
                                            </li>
                                            <li class="col-md-12" style="height:30px;margin-top:20px;">
                                                <div class="width-text-5 left">
                                                    <span class="right">网关</span>
                                                </div>
                                                <input class="col-md-6 marginl-20 isnetwork"
                                                       name="netConfigs[${status.index}].gateway" type="text"
                                                       style="height:30px;border:1px solid #eaedf1;"
                                                       value="${mo.gateway}"/>
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
            <div class="accountcon none">
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
            <div class="servercon none">
                <div class="wind-bulk">
                    <label for="" class="wind-title">部署</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <span class="margin-right-sm">升级包</span>
                            <form id="updataform" action="${ctx}/systemSetting/uploadClientUpdate" method="post"
                                  enctype="multipart/form-data" target="serverUpdate">
                                <input type="hidden" name="MAX_FILE_SIZE" value="100000000">
                                <input type="text" id="clientUpdataPath" class="form-input wind-normal-input">
                                <a href="javascript:void(0);" class=""><i class="iconfont icon-menu-openfolder lookview"
                                                                          title="浏览"></i></a>
                                <input type="file" name="updateFile" id="clientUpdata" accept=".gz" class="uploadFile"
                                       value="浏览">
                                <input type="submit" class="sureButton" id="updata" value="上传">
                                <span id="clientUpdataTip" class="none">请选择文件</span>
                                <iframe id="serverUpdate" name="serverUpdate" style="display:none;"></iframe>
                            </form>
                        </div>
                        <div class="wind-row">
                            <span class="margin-right-sm">安装包</span>
                            <form id="installform" action="${ctx}/systemSetting/uploadClientPackage" method="post"
                                  enctype="multipart/form-data" target="clientUpdate" class="">
                                <input type="hidden" name="MAX_FILE_SIZE" value="100000000">
                                <input type="text" id="clientInstallPath" class="form-input wind-normal-input">
                                <a href="javascript:void(0);" class=""><i class="iconfont icon-menu-openfolder lookview"
                                                                          title="浏览"></i></a>
                                <input type="file" name="packageFile" id="clientInstall" accept=".exe"
                                       class="uploadFile" value="浏览">
                                <input type="submit" class="sureButton" id="install" value="上传">
                                <span id="clientInstallTip" class="none">请选择文件</span>
                                <iframe id="clientUpdate" name="clientUpdate" style="display:none;"></iframe>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="wind-bulk">
                    <label for="" class="wind-title">卸载</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <span class="margin-right-sm">请求码</span>
                            <input type="text" class="form-input wind-normal-input" placeholder="请输入6位数字"
                                   name="authcode" maxlength="6">
                            <input type="button" class="sureButton w100" id="generateCode" value="生成卸载码">
                        </div>
                    </div>
                </div>
                <div class="wind-bulk">
                    <label for="" class="wind-title">登录</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <span class="margin-right-sm">UKey免密</span>
                            <div class="beauty-switch">
                                <input id="factorSwitch" name="factorSwitch" type="checkbox" value="1">
                                <label for="factorSwitch" class="switch-icon text-middle"></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="wind-bulk">
                    <label for="" class="wind-title">截屏</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <span class="margin-right-sm">禁止截屏</span>
                            <div class="beauty-switch">
                                <input id="screenshotSwitch" name="screenshotSwitch" type="checkbox" value="0">
                                <label for="screenshotSwitch" class="switch-icon text-middle"></label>
                            </div>
                        </div>
                    </div>
                </div>
                <c:forEach items="${modules}" var="module">
                    <c:choose>
                        <c:when test="${module == 'legalTime'}">
                            <div class="wind-bulk j-login-time">
                                <label for="" class="wind-title">合法登陆时间</label>
                                <div class="wind-content">
                                    <div class="wind-row">
                                        <span class="margin-right-sm">声音报警</span>
                                        <div class="beauty-switch">
                                            <input id="voice" name="onoff" type="checkbox" value="1">
                                            <label for="voice" class="switch-icon text-middle"></label>
                                        </div>
                                    </div>
                                    <div class="wind-row j-login-time-week">
                                        <div class="beauty-checkbox">
                                            <input id="monday" name="monday" type="checkbox" class="" value="1">
                                            <label for="monday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="monday" class="text-middle margin-right-xl">周一</label>
                                        <div class="beauty-checkbox">
                                            <input id="tuesday" name="tuesday" type="checkbox" class="" value="2">
                                            <label for="tuesday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="tuesday" class="text-middle margin-right-xl">周二</label>
                                        <div class="beauty-checkbox">
                                            <input id="wednesday" name="wednesday" type="checkbox" class="" value="3">
                                            <label for="wednesday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="wednesday" class="text-middle margin-right-xl">周三</label>
                                        <div class="beauty-checkbox">
                                            <input id="thursday" name="thursday" type="checkbox" class="" value="4">
                                            <label for="thursday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="thursday" class="text-middle margin-right-xl">周四</label>
                                        <div class="beauty-checkbox">
                                            <input id="friday" name="friday" type="checkbox" class="" value="5">
                                            <label for="friday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="friday" class="text-middle margin-right-xl">周五</label>
                                        <div class="beauty-checkbox">
                                            <input id="saturday" name="saturday" type="checkbox" class="" value="6">
                                            <label for="saturday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="saturday" class="text-middle margin-right-xl">周六</label>
                                        <div class="beauty-checkbox">
                                            <input id="sunday" name="sunday" type="checkbox" class="" value="7">
                                            <label for="sunday" class="checkbox-icon"></label>
                                        </div>
                                        <label for="sunday" class="text-middle margin-right-xl">周日</label>
                                    </div>
                                    <div class="wind-row">
                                        <span>时间</span>
                                        <input type="text" class="startTime" readonly>
                                        <span>至</span>
                                        <input type="text" class="endTime" readonly>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                    </c:choose>
                </c:forEach>


            </div>
            <div class="clearcon none">
                <div class="wind-row">
                    <label for="" class="wind-label text-left w120">视频流转日志</label>
                    <select name="logoTable_length" class="approveLog w80">
                        <option value="30">30天</option>
                        <option value="90">90天</option>
                        <option value="180">180天</option>
                        <option value="365">365天</option>
                    </select>
                    <input type="button" class="sureButton" id="approveLog" value="清理" data-type="app"
                           data-title="视频流转日志">
                </div>
                <div class="wind-row">
                    <label for="" class="wind-label text-left w120">系统登录日志</label>
                    <select name="logoTable_length" class="logoLog w80">
                        <option value="30">30天</option>
                        <option value="90">90天</option>
                        <option value="180">180天</option>
                        <option value="365">365天</option>
                    </select>
                    <input type="button" class="sureButton" id="logoLog" value="清理" data-type="logonLog"
                           data-title="系统登录日志">
                </div>
                <div class="wind-row">
                    <label for="" class="wind-label text-left w120">系统操作日志</label>
                    <select name="logoTable_length" class="operLog w80">
                        <option value="30">30天</option>
                        <option value="90">90天</option>
                        <option value="180">180天</option>
                        <option value="365">365天</option>
                    </select>
                    <input type="button" class="sureButton" id="operLog" value="清理" data-type="operationLog"
                           data-title="系统操作日志">
                </div>
            </div>
            <div class="decodecon none">
                <div class="decodecon-top">
                    <div class="decodecon-title">方式一：上传水印图片</div>
                    <form id="watermarkform" action="${ctx}/scrnwatermark/getScrnwatermarkLogByPic"
                          method="post"
                          enctype="multipart/form-data" target="client_watermark" class="from-style">
                        <input type="hidden" name="MAX_FILE_SIZE" value="10000000000000">
                        <input type="text" id="client_watermark_path" class="form-input wind-normal-input">
                        <a href="javascript:void(0);" class="btn"><i
                                class="iconfont icon-menu-openfolder lookview"
                                title="浏览"></i></a>
                        <input type="file" name="pic" id="client_watermark_file" accept=".jpg"
                               class="uploadFile"
                               value="浏览">
                        <input type="submit" class="sureButton" id="watermark" value="解码">
                        <span id="client_watermark_tip" class="none">请选择文件</span>
                        <iframe id="client_watermark" name="client_watermark"
                                style="display:none;"></iframe>
                    </form>
                </div>
                <div class="screen-tip none">正在解析,请稍后...</div>
                <div class="decodecon-common decodecon-left">
                    <div class="decodecon-title">方式二：请输入隐式水印</div>
                    <div class="decodecon-content">

                    </div>
                </div>
                <div class="decodecon-center">
                    <div class="center-content">
                        <div class="save-btn margin-bottom-lg" id="decode"><span>解码</span></div>
                        <div class="save-btn" id="clear"><span>清空</span></div>
                    </div>
                </div>
                <div class="decodecon-common decodecon-right">
                    <div class="decodecon-title">水印内容</div>
                    <div class="decodecon-content col-sm-12 col-md-6 col-lg-6">

                    </div>
                </div>
            </div>
            <div class="cascadecon none">
                <div class="wind-bulk">
                    <label for="" class="wind-title">定时采集</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <span class="margin-right-sm">定时采集</span>
                            <div class="beauty-switch">
                                <input id="timingSwitch" name="timingSwitch" type="checkbox" value="1">
                                <label for="timingSwitch" class="switch-icon text-middle"></label>
                            </div>
                        </div>
                        <div class="wind-row">
                            <div class="wind-row">
                                <select name="collectTime" id="collectTime" class="w150 text-top margin-left-lg">
                                    <option value="day">每天</option>
                                    <option value="firstDayOfWeek">每周一</option>
                                    <option value="firstDayOfMonth">每月1号</option>
                                </select>
                                <input type="text" class="collectTimeDay" readonly>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="wind-bulk">
                    <label for="" class="wind-title">关联服务器</label>
                    <div class="wind-content">
                        <div class="wind-row">
                            <form id="lowerform" action="" novalidate="novalidate">
                                <div class="lowerAdd">
                                    <div class="wind-row inline-block">
                                        <label for="" class="wind-label text-left">下级单位</label>
                                        <input type="text" class="form-input wind-normal-input valid" name="lowername">
                                    </div>
                                    <div class="wind-row inline-block margin-left50">
                                        <label for="" class="wind-label">服务器IP</label>
                                        <input type="text" class="form-input wind-normal-input w300 valid" name="lowerip">
                                    </div>
                                    <a id="bar_lower_add" class="bar-item bar-item-icon iconfont icon-btn-add" title="添加下级单位"></a>
                                </div>
                            </form>
                        </div>
                        <div class="wind-row">
                            <div class="lowerShow">
                               <%-- <div class="lowerShowList inline-block" data-name="11" data-startpath="11">
                                    <span class="inline-block text-ellipsis" title="11">sadasdasdasdasdasdasdasdasdasdasdasd</span>
                                    <div class="con inline-block text-top">
                                        <span class="inline-block text-ellipsis text-top margin-left-normal" title="11">192.168.100.100</span>
                                        <i id="lowerClose" class="iconfont icon-btn-close"></i>
                                    </div>
                                </div>--%>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
                        <div class="wind-cell">
                            <label for="" class="wind-label label-required">账户名</label>
                            <input type="text" class="form-input wind-normal-input" name="userName" maxlength="20">
                        </div>
                        <div class="wind-cell">
                            <label for="" class="wind-label label-required">真实姓名</label>
                            <input type="text" class="form-input wind-normal-input" name="name" maxlength="20">
                        </div>
                    </div>
                    <div class="wind-row cf">
                        <div class="wind-cell">
                            <label for="" class="wind-label label-required">密码</label>
                            <input type="password" id="pass" class="form-input wind-normal-input" name="password"
                                   maxlength="20">
                        </div>
                        <div class="wind-cell">
                            <label for="" class="wind-label label-required">确认密码</label>
                            <input type="password" class="form-input wind-normal-input" name="repassword"
                                   maxlength="20">
                        </div>
                    </div>
                    <%--<div class="wind-row cf">

                    </div>
                    <div class="wind-row cf">

                    </div>--%>
                    <div class="wind-row cf">
                        <div class="wind-cell">
                            <label for="" class="wind-label">账户类别</label>
                            <select class="wind-normal-input" name="roleType" id="systemauthlist">
                                <option value="1">系统管理员</option>
                                <option value="2">系统操作员</option>
                                <option value="3">系统审计员</option>
                            </select>
                        </div>
                        <div class="wind-cell">

                            <label for="" class="wind-label">电话</label>
                            <input type="text" class="form-input wind-normal-input" name="phone" maxlength="20">
                        </div>

                    </div>
                    <div class="wind-row cf">
                        <label for="" class="wind-label">操作权限</label>
                        <select class="wind-normal-input" name="readonly" id="authreadonly">
                            <option value="1">只读权限</option>
                            <option value="0">读写权限</option>
                        </select>
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
                    <%--<div class="">
                        <label for="" class="wind-label">操作权限</label>
                        <select class="" name="readonly" id="authreadonly">
                            <option value="1">只读权限</option>
                            <option value="0">读写权限</option>
                        </select>
                    </div>--%>
                </div>
            </div>
        </form>
    </div>
</div>
<script id="temp_opt_box" type="text/html">
    <div class="table-opt-box">
        <%--<i class="iconfont icon-nav-system table-opt-icon"></i>--%>
        <%--<div class="opt-hover-box">--%>
        <div class="opt-hover-row j-opt-hover-edit" data-id="{{id}}" data-guid="{{guid}}" data-only="{{only}}"
             title="编辑">
            <i class="iconfont icon-btn-edit1 text-sm"></i>
            <%--<span class="text-sm margin-left-xs">编辑</span>--%>
        </div>
        <div class="opt-hover-row j-opt-hover-delete" data-id="{{id}}" title="删除">
            <i class="iconfont icon-btn-delete text-sm"></i>
            <%--<span class="text-sm margin-left-xs">删除</span>--%>
        </div>
        <%--</div>--%>
    </div>
</script>
<script id="dot_tem" type="text/html">
    <div class="dot-box dot-box-start">
        <div class="dot-start"></div>
    </div>
    {{each site}}
    <div class="dot-box">
        <div class="dot"></div>
    </div>
    {{/each}}
</script>
<script id="show_tem" type="text/html">
    <div class="wind-row">
        <label for="" class="wind-label font-bold">{{type}}</label>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">部门名称:</label>
        <span>{{department}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">真实姓名:</label>
        <span>{{truename}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">计算机名称:</label>
        <span>{{computername}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">IP地址:</label>
        <span>{{ip}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">MAC地址:</label>
        <span>{{mac}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">终端时间:</label>
        <span>{{applyTime}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">接收方信息:</label>
        <span>{{receiver}}</span>
    </div>
    <div class="wind-row">
        <label for="" class="wind-label">自定义水印:</label>
        <span>{{manual}}</span>
    </div>
</script>
<script id="lowerList" type="text/html">
    {{each $data item}}
    <div class="lowerShowList inline-block" data-name="{{item.nodeName}}" data-nodeIp="{{item.nodeIp}}" data-id="{{item.id}}">
        <span class="inline-block text-ellipsis" title="{{item.nodeName}}">{{item.nodeName}}</span>
        <div class="con inline-block text-top">
            <span class="inline-block text-ellipsis text-top margin-left-normal" title="{{item.nodeIp}}">{{item.nodeIp}}</span>
            <i id="lowerClose" class="iconfont icon-btn-close"></i>
        </div>
    </div>
    {{/each}}
</script>
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/plugins/validate/jquery.validate.js"></script>
<script src="${ctxJs}/plugins/validate/messages_zh.js"></script>
<script src="${ctxJs}/plugins/validate/validateExtent.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js"
        type="text/javascript"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/system/setting/index.js"></script>
<script>
    var ctx = "${ctx}";
    $('#beanForm').validate({ //弹出后绑定校验
        rules: {
            ip: { //ip重复
                required: true
            },
            mask: {
                required: true
            },
            gateway: {
                required: true,
            }
        },
        messages: {
            ip: {
                required: "不可为空",
            },
            mask: {
                required: "不可为空",
            },
            gateway: {
                required: "不可为空",
            }
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }
    });

    function doSave() {
        if ($("#beanForm").valid()) {
            layer.confirm('网络配置会自动重启服务器，确认提交？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
                layer.closeAll();
                $.ajax({
                    type: "post",
                    url: ctx + "/systemSetting/savenetconfig",
                    dataType: "json",
                    data: $('#beanForm').serialize(),
                    success: function (msg) {
                        if (msg == 'success') {
                            layer.msg("网络配置成功", {icon: 1});
                        } else {
                            layer.msg("网络配置失败", {icon: 2});
                        }
                    },
                    error: function (e) {
                        //layer.msg("网络配置失败", { icon: 2 });//服务器重启的原因，这个地方不能加
                    }
                });
            })
        }
    }

    $("#save_config").click(function () {
        doSave()
    })

    var dotdata = {
        "site": []
    }
    for (var i = 0; i < 24; i++) {
        dotdata.site.push({"1": "1"})
    }
    $(".decodecon-left .decodecon-content").html(template('dot_tem', dotdata))


</script>