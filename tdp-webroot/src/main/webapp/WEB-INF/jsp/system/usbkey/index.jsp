<!--
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
-->

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/system/usbkey/index.css" rel="stylesheet" type="text/css"/>
</head>
<div class="main-right">
    <div class="top-bar">
        <div class="top-title">UKey库</div>
        <div class="bar-item-box">
            <div class="bar-item bar-item-search">
                <input id="bar_searchstr" type="text" placeholder="UKey昵称">
                <i id="bar_searchstr_icon" class="iconfont icon-btn-search1 text-lg"></i>
            </div>
            <a id="bar_add_usbkey" class="bar-item bar-item-icon iconfont icon-btn-add" title="添加UKey"></a>
            <%--<a id="bar_edit_usbkey" class="bar-item bar-item-icon iconfont icon-btn-edit1" title="修改USBkey信息"></a>--%>
            <a id="bar_delete" class="bar-item bar-item-icon iconfont icon-btn-delete" title="删除"></a>
            <a id="bar_unbind" class="bar-item bar-item-icon iconfont icon-btn-unbind1" title="解绑"></a>
        </div>
    </div>
    <div class="content-box">
        <div class="cell-bottom">
            <table id="usbkey_table" cellspacing="0" cellpadding="0" border="0" width="100%">
                <thead>
                <tr>
                    <th class="text-center">
                        <div class="beauty-checkbox">
                            <input id="check_usbkey_all" type="checkbox" class="j-check-usbkey-all">
                            <label for="check_usbkey_all" class="checkbox-icon"></label>
                        </div>
                    </th>
                    <th>UKey昵称</th>
                    <th>UKey设备</th>
                    <th>绑定用户</th>
                    <th style="text-align:center;">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<!--添加用户弹窗-->
<div id="add_usbkey_wind" class="none">
    <div class="wind-box">
        <form class="padding-normal j-add-user-form">
            <div class="wind-row cf">
                <label for="" class="wind-label label-required w120">UKey昵称</label>
                <input type="text" class="form-input wind-normal-input" name="name" maxlength="20">
            </div>
            <div class="wind-row cf">
                <label for="" class="wind-label label-required w120">UKey设备</label>
                <input type="text" class="form-input wind-normal-input color-grey" id="sn" name="sn" maxlength="20" readonly="">
                <input type="hidden" class="form-input wind-normal-input color-grey" id="uuid" name="uuid" maxlength="20" readonly="">
                <div class="tips">
                    <span>?</span>
                    <div class="tips_con none">点击检索UKey按钮,获取UKey标识信息,每次只能插入一个UKey。</div>
                </div>
            </div>
            <div class="wind-row cf">
                <label for="" class="wind-label w120"></label>
                <div class="save-btn inline-block j-start-device" id="start_device"><span>启动工具</span></div>
                <div class="save-btn inline-block j-check-device" id="check_device"><span>检索UKey</span></div>
                <a href="${ctx}/downloadUsbKeyPlugins">下载UKey检索控件</a>
            </div>
            <a href="VDPFP://GdFingerprinted" class="" id="open" onclick="window.location.href=this.href"></a>
        </form>
    </div>
</div>
<!--添加用户弹窗结束-->
<!--用户表操作模板-->
<script id="temp_opt_box" type="text/html">
    <div class="table-opt-box">
        <div class="opt-hover-row j-opt-hover-edit" data-id="{{$data.id}}" data-name="{{$data.name}}" data-sn="{{$data.sn}}" data-uuid="{{$data.uuid}}" title="编辑">
            <i class="iconfont icon-btn-edit1 text-normal"></i>
        </div>
        <div class="opt-hover-row j-opt-hover-delete" data-id="{{$data.id}}" data-name="{{$data.name}}" data-sn="{{$data.sn}}" data-uuid="{{$data.uuid}}" title="删除">
            <i class="iconfont icon-btn-delete text-normal"></i>
        </div>
        <div class="opt-hover-row j-opt-hover-unbind" data-id="{{$data.id}}" data-name="{{$data.name}}" data-sn="{{$data.sn}}" data-uuid="{{$data.uuid}}" title="解绑">
            <i class="iconfont icon-btn-unbind1 text-normal"></i>
        </div>
    </div>
</script>
<!--部门表操作模板结束-->
<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/template/template-web.js" type="text/javascript"></script>
<script src="${ctxJs}/system/usbkey/index.js"></script>