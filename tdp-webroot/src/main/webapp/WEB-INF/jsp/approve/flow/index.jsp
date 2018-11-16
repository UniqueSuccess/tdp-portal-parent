<!--userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title></title>
    <link href="${ctxCss}/approve/flow/index.css" rel="stylesheet" type="text/css"/>
</head>
<div id="approveFlowIndex" v-cloak class="gd-right-content gd-padding-lg">
    <gd-toolbar :config="toolbarConfig">

    </gd-toolbar>
    <div class="left-bar">
        <div id="left_content_box" class="left-content-box">
            <ul id="flow_list_box" class="flow-list-box">

                <li class="left-list list-active" data-id="1">
                    <span class="list-name" title="默认">默认</span>

                </li>

            </ul>
        </div>
    </div>
    <!--角色列表结束-->
    <div class="right-content-box">
        <!--内容区头部结束-->
        <div class="right-content">
            <div id="approveFlow_box" class="approveFlow_box">

            </div>
        </div>
    </div>

</div>
<script id="new_flow" type="text/html" class="gd-none">
    <div class="container" id="add_approve" v-cloak>
        <form id="add_flow_form">
            <div class="row">
                <label class="gd-label-required">流程名</label>
                <input type="text" class="gd-input gd-input-lg" gd-validate="required" name="name">
            </div>
            <div class="row">
                <label class="">继承自</label>
                <gd-select placeholder="请选择" v-model="selectFlowValue" class="gd-select-lg" name="parentApproveId">
                    <gd-option v-for="item in flowArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
        </form>
    </div>


</script>

<script id="flow_list" type="text/html">
    {{each $data item}}
    <li class="left-list" data-id="{{item.id}}">
        <span class="list-name" title="{{item.name}}">{{item.name}}</span>
    </li>
    {{/each}}
</script>
<script src="${ctxJs}/plugins/template/template-web.js" type="text/javascript"></script>
<script src="${ctxJs}/approve/flow/index.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
</script>