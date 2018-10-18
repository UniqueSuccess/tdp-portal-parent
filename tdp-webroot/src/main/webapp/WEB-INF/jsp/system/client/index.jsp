<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>
    <!--<link href="${ctxCss}/ztree/ztree.css" rel="stylesheet" type="text/css" />-->
    <link href="${ctxCss}/system/client/index.css" rel="stylesheet" type="text/css"/>
</head>
<div class="main-right">
    <!--内容区头部-->
    <div class="top-bar">
        <div class="top-title">视频平台</div>
    </div>
    <!--内容区头部结束-->
    <!--内容区-表格-->
    <div class="content-box">
        <div class="videoNetCon">
            <div class="top-bar">
                <div class="top-title">视频业务网络访问管控</div>
            </div>
            <div class="netContent">
                <form id="netform" action="">
                    <div class="netAdd">
                        <div class="wind-row inline-block">
                            <label for="" class="wind-label text-left">IP/IP段</label>
                            <input type="text" class="form-input wind-normal-input w600" name="netip" placeholder="例如：192.168.1.1或192.168.1.1-192.168.1.254">
                        </div>
                        <a id="bar_net_add" class="bar-item bar-item-icon iconfont icon-btn-add" title="添加IP段"></a>
                    </div>
                </form>
                <div class="netShow">
                    <!--<div class="netShowList inline-block" data-id=""><span>192.168.111.111-192.168.222.222</span><i id="netClose" class="iconfont icon-btn-close none"></i></div>-->

                </div>
            </div>
        </div>
        <div class="videoLogoCon">
            <div class="top-bar">
                <div class="top-title">视频业务登陆方式管理</div>
            </div>
            <div class="netContent">
                <form id="logoform" action="">
                    <div class="logoAdd">
                        <div class="wind-row inline-block">
                            <label for="" class="wind-label text-left">业务名称</label>
                            <input type="text" class="form-input wind-normal-input" name="logoname">
                        </div>
                        <div class="wind-row inline-block margin-left50">
                            <label for="" class="wind-label">进程名</label>
                            <input type="text" class="form-input wind-normal-input w300" name="logoip">
                        </div>
                        <a id="bar_logo_add" class="bar-item bar-item-icon iconfont icon-btn-add" title="添加业务路径"></a>
                    </div>
                </form>
                <div class="logoShow">
                    <!--<div class="logoShowList inline-block">-->
                    <!--<span class="inline-block text-ellipsis">192.168.111.1111111111111111</span>-->
                    <!--<div class="con inline-block text-top">-->
                    <!--<i class="iconfont icon-nav-monitor"></i>-->
                    <!--<span class="inline-block text-ellipsis text-top">v:\1b69\De\download(1)\font_501303_4ftfb9s8g2kgwrk9\sfsdf.exe</span>-->
                    <!--<i id="logoClose" class="iconfont icon-btn-close"></i>-->
                    <!--</div>-->
                    <!--</div>-->
                </div>
            </div>
        </div>
    </div>
    <!--内容区-表格结束-->
</div>

<script id="netList" type="text/html">
    {{each $data item}}
    <div class="netShowList inline-block" data-ip="{{item}}"><span>{{item}}</span><i id="netClose" class="iconfont icon-btn-close none"></i></div>
    {{/each}}
</script>
<script id="logoList" type="text/html">
    {{each $data item}}
    <div class="logoShowList inline-block" data-name="{{item.name}}" data-startPath="{{item.startPath}}">
        <span class="inline-block text-ellipsis" title="{{item.name}}">{{item.name}}</span>
        <div class="con inline-block text-top">
            <i class="iconfont {{if item.type == 1}} icon-nav-monitor {{else}} icon-IE {{/if}}"></i>
            <span class="inline-block text-ellipsis text-top" title="{{item.startPath}}">{{item.startPath}}</span>
            <i id="logoClose" class="iconfont icon-btn-close"></i>
        </div>
    </div>
    {{/each}}
</script>
<!--<script src="${ctxJs}/plugins/dataTables/jquery.dataTables.min.js"></script>-->
<!--<script src="${ctxJs}/plugins/dataTables/ColReorderWithResize.min.js"></script>-->
<!--<script src="${ctxJs}/plugins/zTree/jquery.ztree.core-3.5.js" type="text/javascript"></script>-->
<!--<script src="${ctxJs}/plugins/zTree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>-->
<script src="${ctxJs}/plugins/template/template-web.js" type="text/javascript"></script>
<script src="${ctxJs}/system/client/index.js"></script>