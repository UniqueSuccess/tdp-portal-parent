<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/access/index.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main_right">
    <div class="top-bar">
        <div class="top-title">准入</div>
    </div>
    <div class="content-box">
        <div class="add-server-box" id="access-form">

        </div>
    </div>
</div>
<div class="save">
    <div class="access-save">
        <span>保存</span>
    </div>
</div>
<script id="accessTem" type="text/html">
    <form>
        <div class="item-row padding-xs">
            <label class="wind-label label-norequire">防护状态</label>
            <div class="beauty-switch">
                <input id="onoff" name="onoff" {{if $data.onoff==true}} checked {{/if}} type="checkbox" value="1">
                <label for="onoff" class="switch-icon onoff"></label>
            </div>
        </div>
        <div class="item-row padding-xs">
            <label class="wind-label">Mac地址</label>
            <input class="wind-input requierd" type="text" value="{{$data.nacMac}}" name="nacMac" placeholder="例如：FF:FF:FF:FF:FF:FF" {{if $data.onoff==true}}   {{else}} readonly="readonly" {{/if}}>
            <span class="error-text"></span>
        </div>
        <div class="item-row padding-xs">
            <label class="wind-label">跳转地址</label>
            <input class="wind-input requierd" type="text" value="{{$data.nacUrl}}" name="nacUrl" placeholder="例如：http://www.neiwang.cn" {{if $data.onoff==true}} {{else}} readonly="readonly"  {{/if}} >
            <span class="error-text"></span>
        </div>
        <div class="item-row padding-xs">
            <span class="mustlabel none">*</span>
            <label class="wind-label">准入控制网段</label>
            <input class="wind-input requierd" type="text" name="ctrlAreas-x" placeholder="例如：192.168.1.1或192.168.1.1-192.168.1.254">
            <i class="iconfont icon-btn-add {{if $data.onoff==true}}add-ctrl-area {{else}}color-grey {{/if}}" id="add_ctrl_area"></i>
            <span class="error-text"></span>
            <div class="append-box ctrl-area">
                {{each ctrlAreas item}}
                <div class="append-param-box"><input class="add-param-val medium-input append" value="{{item}}" type="text" name="ctrlAreas" readonly><i class="iconfont icon-btn-close del-param"></i></div>
                {{/each}}

            </div>
        </div>
        <div class="item-row padding-xs">
            <label class="wind-label">白名单端口</label>
            <input class="wind-input" type="text" name="legalPorts-x" placeholder="0~65535之间的整数">
            <i class="iconfont icon-btn-add {{if $data.onoff==true}}add-legal-port {{else}}color-grey {{/if}}" id="add_legal_port"></i>
            <span class="error-text"></span>
            <div class="append-box legal-port">
                {{each legalPorts item}}
                <div class="append-param-box"><input class="add-param-val medium-input append" value="{{item}}" type="text" name="legalPorts" readonly><i class="iconfont icon-btn-close del-param"></i></div>
                {{/each}}
            </div>
        </div>
        <div class="item-row padding-xs">
            <label class="wind-label ">白名单IP</label>
            <input class="wind-input" type="text" name="legalIps-x" placeholder="例如：192.168.1.1或192.168.1.1-192.168.1.254">
            <i class="iconfont icon-btn-add {{if $data.onoff==true}}add-legal-ip {{else}}color-grey {{/if}}" id="add_legal_ip"></i>
            <span class="error-text"></span>
            <div class="append-box legal-ip">
                {{each legalIps item}}
                <div class="append-param-box"><input class="add-param-val medium-input append" value="{{item}}" type="text" name="legalIps" readonly><i class="iconfont icon-btn-close del-param"></i></div>
                {{/each}}
            </div>
        </div>
        <!--<div class="item-row padding-xs">-->
            <!--<label class="wind-label">WEB重定向端口</label>-->
            <!--<input class="wind-input" type="text" name="httpPorts-x" placeholder="0~65535之间的整数">-->
            <!--<i class="iconfont icon-btn-add add-http-port"></i>-->
            <!--<span class="error-text"></span>-->
            <!--<div class="append-box http-port">-->
                <!--{{each httpPorts item}}-->
                <!--<div class="append-param-box"><input class="add-param-val medium-input append" value="{{item}}" type="text" name="httpPorts" readonly><i class="iconfont icon-btn-close del-param"></i></div>-->
                <!--{{/each}}-->
            <!--</div>-->
        <!--</div>-->
        <!--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
        <!--<input type="hidden" name="nacServer" value=""/>-->
    </form>
</script>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/plugins/validate/jquery.validate.js"></script>
<script src="${ctxJs}/plugins/validate/validateExtent.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/public.js"></script>
<script src="${ctxJs}/access/index.js"></script>
<script>
    var accessObj = JSON.parse('${config}');
</script>