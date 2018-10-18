<!--userlist.jsp -->
<!-- <%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/approve/flow/index.css" rel="stylesheet" type="text/css"/>
</head>
<div class="approve_start">

</div>
<div id="default_template" class="none">
    <label class="default" for="" data-stepid="">
        <div class="default_bar">
            <label for="" class="default_bar_title"></label>
            <span class="default_bar_name text-ellipsis">新增节点</span>
            <span class="default_bar_mode">标准模式</span>
            <img src="${ctx}/skin/default/images/approve/arrow-top.png" class="none" alt=""/>
        </div>
        <div class="flow_line">
            <i class="iconfont icon-add flow_add" title="添加节点"></i>
        </div>
        <div class="approve_con none">
            <div class="top-bar">
                <div class="top-title">新增节点</div>
                <div class="bar-item-box">
                    <a class="bar-item bar-item-icon iconfont icon-delete margin-right-sm bar_delete_flow" title="删除" data-stepid=""></a>
                </div>
            </div>
            <div class="content">
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">环节名称</label>
                    <input type="text" name="stepname" value="新增节点" class="gd-input gd-input-lg">
                    <%--<input type="text" class="form-input wind-normal-input-max" name="stepname" value="新增节点" maxlength="20">--%>
                </div>
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">审批模式</label>
                    <div class="mode">
                        <label class="gd-radio">
                            <input id="stand99" type="radio" checked="checked" name="mode99" value="1"/>
                            <i></i>
                            标准模式
                        </label>
                        <div class="tips">
                            <span tooltip="任何人审批通过，即可进入下一环节，若有一人拒绝，则该审批请求结束">?</span>
                        </div>
                    </div>
                    <div class="mode">
                        <label class="gd-radio">
                            <input id="strict99" type="radio" checked="checked" name="mode99" value="0"/>
                            <i></i>
                            严格模式
                        </label>
                        <div class="tips">
                            <span tooltip="所有人审批通过，即可进入下一环节，若有一人拒绝，则该审批请求结束">?</span>
                            <%--<div class="tips_con none">所有人审批通过，即可进入下一环节，若有一人拒绝，则该审批请求结束</div>--%>
                        </div>
                    </div>

                </div>
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">审批人</label>
                    <div class="approve_h">

                        <a class="bar-item bar-item-icon iconfont icon-add margin-right-sm bar_add_hum" title="添加"></a>
                    </div>
                </div>
                <div class="wind-row cf">
                    <button class="gd-btn" onclick="saveApprove(this)">保存</button>
                    <button class="gd-btn-cancel" onclick="cancelApprove(this)">取消</button>
                </div>
            </div>
        </div>
    </label>
</div>
<script id="treeLayer" type="text/html">
    <div id="treeview" class="ztree">

    </div>
</script>
<script id="approveAllList" type="text/html">
    <label class="begin" for="" data-stepid="0">
        <div class="begin_bar">
            <label for=""></label>
            <span>开始</span>
        </div>
        <div class="flow_line">
            <i class="iconfont icon-add flow_add flow_add_start" title="添加节点"></i>
        </div>
    </label>

    {{each modelList item}}
    <label class="default" for="" data-stepid="{{item.id}}">
        <div class="default_bar">
            <label for="" class="default_bar_title"></label>
            <span class="default_bar_name text-ellipsis" title="{{item.name}}">{{item.name}}</span>
            {{if item.standard == 1}}
            <span class="default_bar_mode">标准模式</span>
            {{else if item.standard == 0}}
            <span class="default_bar_mode">严格模式</span>
            {{/if}}
            <img src="${ctx}/skin/default/images/approve/arrow-top.png" class="none" alt=""/>
        </div>
        <div class="flow_line">
            <i class="iconfont icon-add flow_add" title="添加节点"></i>
        </div>
        <div class="approve_con none">
            <div class="top-bar">
                <div class="top-title">{{item.name}}</div>
                <div class="bar-item-box">
                    <a class="bar-item bar-item-icon iconfont icon-delete margin-right-sm bar_delete_flow" title="删除" data-stepid="{{item.id}}"></a>
                </div>
            </div>
            <div class="content">
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">环节名称</label>
                    <input type="text" name="stepname" value="{{item.name}}" class="gd-input gd-input-lg">
                    <%--<input type="text" class="form-input wind-normal-input-max" name="stepname" value="{{item.name}}" maxlength="20">--%>
                </div>
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">审批模式</label>
                    <div class="mode">

                        <label class="gd-radio">
                            <input id="stand{{item.id}}" type="radio" {{if item.standard == 1}} checked="checked" {{/if}} name="mode{{item.id}}" value="1"/>
                            <i></i>
                            标准模式
                        </label>
                        <div class="tips">
                            <span tooltip="任何人审批通过，即可进入下一环节，若有一人拒绝，则该审批请求结束">?</span>
                        </div>

                        <%--<input id="stand{{item.id}}" type="radio" {{if item.standard == 1}} checked="checked" {{/if}} name="mode{{item.id}}" value="1"/>--%>
                        <%--<label for="stand{{item.id}}">标准模式</label>--%>

                    </div>
                    <div class="mode">

                        <label class="gd-radio">
                            <input id="strict{{item.id}}" type="radio" {{if item.standard == 0}} checked="checked" {{/if}} name="mode{{item.id}}" value="0"/>
                            <i></i>
                            严格模式
                        </label>
                        <div class="tips">
                            <span tooltip="所有人审批通过，即可进入下一环节，若有一人拒绝，则该审批请求结束">?</span>
                        </div>
                    </div>

                </div>
                <div class="wind-row cf">
                    <label for="" class="wind-label-max label-required">审批人</label>
                    <div class="approve_h">
                        {{each item.approverArr approve}}
                            {{each approvers app}}
                                {{if app[approve] != undefined}}
                        <span class="wind-span text-ellipsis" data-guid="{{approve}}">{{app[approve]}}<i class="iconfont icon-close hum_del" data-guid="{{approve}}"></i></span>
                                {{/if}}
                            {{/each}}
                        {{/each}}
                        <a class="bar-item bar-item-icon iconfont icon-add margin-right-sm bar_add_hum" title="添加"></a>
                    </div>
                </div>
                <div class="wind-row cf">
                    <button class="gd-btn" onclick="saveApprove(this)">保存</button>
                    <button class="gd-btn-cancel" onclick="cancelApprove(this)">取消</button>
                </div>
            </div>
        </div>
    </label>
    {{/each}}
    <label class="end" for="" data-stepid="-1">
        <div class="end_bar">
            <label for=""></label>
            <span>结束</span>
        </div>
    </label>
</script>

<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/template/template-web.js" type="text/javascript"></script>
<script>
  var definition = JSON.parse('${definition}');
  var modelList = JSON.parse('${modelList}');
  var approvers = JSON.parse('${approvers}');
  var objAll = {
    definition:'',
    modelList:'',
    approvers:''
  };
  objAll.definition = definition;
  objAll.modelList = modelList;
  objAll.approvers = approvers;
//  console.log(objAll);
  var flowid = definition.id;
  var flowname = definition.name;





</script>
<script src="${ctxJs}/approve/flow/detail.js"></script>

