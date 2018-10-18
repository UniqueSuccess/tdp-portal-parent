<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<c:set var="loginFlag" value="1"/>
<c:set var="serverName" value="${pageContext.request.serverName}"/>
<c:set var="serverPort" value="${pageContext.request.serverPort}"/>
<c:set var="outUrl" value="/logout"/>
<c:if test="${loginFlag == '1'}">
    <c:set var="pro" value="/tdp/logout"/>
    <c:if test="${!empty serverName}">
        <c:set var="outUrl" value="http://${serverName}${pro}"/>
    </c:if>
    <c:if test="${!empty serverPort}">
        <c:set var="outUrl" value="http://${serverName}:${serverPort}${pro}"/>
    </c:if>
</c:if>

<c:url value="${outUrl}" var="logoutUrl"/>
<form action="${logoutUrl}" method="post" id="logoutForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<div id="system_top">
    <gd-topbar :config="topbarConfig">
    </gd-topbar>
</div>
<%--<div id="wtop">
    <div id="wtright">
        <div class="nav-top">
            <!--navtestright-->
            <div class="nav-topright">
                <ul>
                    &lt;%&ndash;<li>
                        <a id="fullScreen" href="javascript:void(0)" class="top-full-box" title="消息"></a>
                    </li>&ndash;%&gt;
                    <li>
                        <a id="bell" href="javascript:void(0)" class="top-msg-box" title="消息"
                           onclick="alarm();alarmNoAuth();warningAlarm();">
                            <span id="top_msg" class="top-msg"></span>
                            <span id="top_alarm" class="top-alarm none"></span>
                            <audio id="alarm_audio">
                                <source src="${ctx}/resource/alarmsound/warningsound.mp3" type="audio/mpeg">
                                您的浏览器不支持 audio 元素。
                            </audio>
                        </a>

                    </li>
                    <li>
                        <a href="javascript:void(0)" class="top-user-info">
                            <span class="top-user-name" id="top_user_name" data-id="${userId}">
                                <sec:authentication property="name"/>
                                <span class="caret"></span>
                            </span>

                            <div class="user-info-hover-box">
                                <div class="user-info-hover-row j-top-edit-user">
                                    <i class="iconfont icon-menu-user1"></i>
                                    <span>用户信息</span>

                                </div>
                                <div class="user-info-hover-row" onclick="javascript:$('#logoutForm').submit()">
                                    <i class="iconfont icon-menu-exit"></i>
                                    <span>退出</span>
                                </div>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
            <!--navright END-->
            <div class="clear"></div>
        </div>
    </div>
    <div class="clear"></div>
</div>--%>

<div id="userinfo-html" style="display:none;">

    <div class="userinfo-box">
        <form id="userinfo-form">
            <div class="u-row">
                <div class="label-box"><label>用户名：</label></div>
                <input type="text" name="userName" readonly class="readonly-input" style="border:none !important;"
                />
            </div>
            <div class="u-row">
                <div class="label-box "><label class="j-label-role">账户角色：</label></div>
                <input type="text" name="roleTypeName" readonly class="readonly-input"
                       style="border:none !important;"/>
            </div>
            <div class="u-row">
                <div class="label-box"><label>原密码：</label></div>
                <input type="password" name="pwd" placeholder="不可超过24个字符"/>
            </div>
            <div class="u-row">
                <div class="label-box"><label>新密码：</label></div>
                <input type="password" name="newpwd" placeholder="不可超过24个字符"/>
            </div>
            <div class="u-row">
                <div class="label-box"><label>确认密码：</label></div>
                <input type="password" name="newpwd_x" placeholder="不可超过24个字符"/>
            </div>
            <div class="u-row u-row-first none">
                <div class="label-box"><label class="label-required">真实姓名：</label></div>
                <input type="text" name="first" placeholder="不可超过24个字符"
                />
            </div>
            <div class="u-row">
                <div class="label-box"><label>手机：</label></div>
                <input type="text" name="phone" placeholder="请输入11位正整数"/>
            </div>
            <input type="hidden" name="id" value=""/>
        </form>
    </div>
</div>
<input type="hidden" name="" class="messhid" value="">
<script src="${ctxJs}/top/top.js?v=${version}"></script>
<script type="text/javascript">
    var warningNum = 0;
    var ctx = '${ctx}';
    var host = window.location.host;
    var promptMsg = decodeURIComponent(getCookie("promptMsg"));
    var authmsg = "${authmsg}";
    if (promptMsg != 'null' && promptMsg != '') {
        $("#top_msg").show();
    }

    //较验授权信息维保到期，弹窗提示
    function alarm() {
        if (promptMsg != 'null' && promptMsg != '') {
//      $("#top_msg").show();
            layer.open({
                type: 1,
                title: '提示',
                content: '<div class="padding-sm">' + promptMsg + '</div>',
                area: ['300px', '200px'],
                btn: ['不再提示', '关闭'],
                yes: function (index, layero) {
                    delCookie("promptMsg");
                    layer.close(index);
                },
                success: function (layero, index) {
                    $(layero).find('.layui-layer-content').css({
                        "padding-left": '70px',
                        "height": '60px',
                        "line-height": "30px"
                    });
                },
                end: function () {
                }
            });
        } else {
            $("#top_msg").hide();
        }

    }

    //较验没有授权，弹窗提示
    function alarmNoAuth() {
        if (authmsg != 'null' && authmsg != '') {
            $("#top_msg").show();
            layer.open({
                type: 1,
                title: '提示',
                content: '<div class="padding-sm">' + authmsg + '</div>',
                area: ['300px', '200px'],
                btn: ['关闭'],
                yes: function (index, layero) {
                    layer.close(index);
                },
                success: function (layero, index) {
                    $(layero).find('.layui-layer-content').css({
                        "padding-left": '70px',
                        "height": '60px',
                        "line-height": "30px"
                    });
                },
                end: function () {
                }
            });
        } else {
            $("#top_msg").hide();
        }

    }

    //管理员报警信息提示
    if (getSearch("navId") == 1) {
        alarm();
        alarmNoAuth();
    }

    function getSearch(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
</script>