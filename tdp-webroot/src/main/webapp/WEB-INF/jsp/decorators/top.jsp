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
<style>
    /*编辑个人信息弹窗*/
    .edit-person {
        width: calc(100% - 80px);
        margin: 0 40px;
        height: 100%;
    }

    .edit-person li {
        margin-top: 12px;
        height: 32px;
        line-height: 32px;
    }

    .edit-person li label {
        display: inline-block;
        width: 60px;
        text-align: right;
        margin-right: 16px;
    }

    .edit-person li p {
        display: inline-block;
        margin: 0
    }

    #editPersonalWind .gd-layer-header {
        border-bottom: 0;
    }

    #editPersonalWind .gd-layer-footer {
        border-top: 0;
    }
</style>
<div id="system_top">
    <gd-topbar :config="topbarConfig">
    </gd-topbar>
</div>
<input type="hidden" name="" class="messhid" value="">
<script type="text/javascript">
    var alarmMsgContent = '';
    var ctx = '${ctx}';
    var promptMsg = decodeURIComponent(getCookie("promptMsg"));
    var authmsg = "${authmsg}";
    var endDate = '${endDate}';
    var maxCustomerCnt = '${maxCustomerCnt}';
    var userNum = '${userNum}';
    if (promptMsg != 'null' && promptMsg != '') {
        alarmMsgContent = "1";
    }

    //还能注册多少人
    function alarmRegisterNum() {
        if (maxCustomerCnt != 'null' && maxCustomerCnt != '' && maxCustomerCnt != 0) {
            var allowRegisterNum = (parseInt(maxCustomerCnt) - parseInt(userNum));
            if (0 < allowRegisterNum && allowRegisterNum < 10) {
                gd.showMsg('<div class="gd-gray1-color gd-margin-bottom-xs">授权提示</div><span class="gd-gray3-color">剩余可注册人数'+ allowRegisterNum + '人</span>', {
                    btn: [{
                        text: '关闭',//按钮文本
                        action: function () {//按钮点击事件
                        }
                    }]
                });
            }

        }
    }

    //校验授权马上到期或者已经超期提醒
    function alarmPastDue() {
        if (endDate != 'null' && endDate != '') {
            var timestamp = new Date().getTime();
            var endDateAuth = new Date(endDate).getTime();
            //计算出相差天数
            var days = Math.floor((endDateAuth - timestamp) / (24 * 3600 * 1000));
            if (0 < parseInt(days) && parseInt(days) < 10) {
                gd.showMsg('<div class="gd-gray1-color gd-margin-bottom-xs">授权提示</div><span class="gd-gray3-color">授权仅剩'+ days + '天</span>', {
                    btn: [{
                        text: '关闭',//按钮文本
                        action: function () {//按钮点击事件
                        }
                    }]
                });
            }
        }
    }

    function stringToDate(dateStr, separator) {
        if (!separator) {
            separator = "-";
        }
        var dateArr = dateStr.split(separator);
        var year = parseInt(dateArr[0]);
        var month;
        //处理月份为04这样的情况
        if (dateArr[1].indexOf("0") == 0) {
            month = parseInt(dateArr[1].substring(1));
        } else {
            month = parseInt(dateArr[1]);
        }
        var day = parseInt(dateArr[2]);
        var date = new Date(year, month - 1, day);
        return date;
    }


    //较验授权信息维保到期，弹窗提示
    function alarm() {

        if (promptMsg != 'null' && promptMsg != '') {
            alarmMsgContent = '1';
            gd.showMsg('<div class="gd-gray1-color gd-margin-bottom-xs">授权提示</div><span class="gd-gray3-color">'+authmsg+'</span>', {
                btn: [{
                    text: '不再提示',//按钮文本
                    action: function () {//按钮点击事件
                        delCookie("promptMsg");
                    }
                },{
                    text: '关闭',//按钮文本
                    action: function () {//按钮点击事件
                    }
                }]
            });

        } else {
            alarmMsgContent = '';
        }

    }

    //较验没有授权，弹窗提示
    function alarmNoAuth() {
        if (authmsg != 'null' && authmsg != '') {
            alarmMsgContent = '1';

            gd.showMsg('<div class="gd-gray1-color gd-margin-bottom-xs">授权提示</div><span class="gd-gray3-color">'+authmsg+'</span>', {
                btn: [{
                    text: '关闭',//按钮文本
                    action: function () {//按钮点击事件
                    }
                }]
            });
        } else {
            alarmMsgContent = '';
        }

    }

    //管理员报警信息提示
    if (getSearch("navId") == 1) {
        alarm();
        alarmNoAuth();
        alarmPastDue();
        alarmRegisterNum();
    }

    function getSearch(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
</script>
<script type="text/html" id="personal_information">
    <form id="editpersonal" onsubmit="return false">
        <ul class="edit-person">
            <li>
                <label class="">用户名</label>
                <p class="userName"></p>
            </li>
            <li>
                <label>重置密码</label>
                <input type="password" class="gd-input gd-input-lg" maxlength="20" gd-validate="required minLength"
                       gdv-minlength="6" name='password' id="set_psw">
                <p></p>
            </li>
            <li>
                <label>确认密码</label>
                <input type="password" class="gd-input gd-input-lg" maxlength="20"
                       gd-validate="required equalTo minLength" gdv-equal="set_psw" gdv-minlength="6"
                       name="prePassword">
                <p></p>
            </li>
            <li>
                <label class="">姓名</label>
                <p class="editname"></p>
            </li>
            <li>
                <label class="">角色</label>
                <p class="editrole"></p>
            </li>
            <li>
                <label class="">电话</label>
                <input type="text" class="gd-input gd-input-lg" name="phone" gd-validate="phone">
            </li>
        </ul>
    </form>
</script>

<script>
    var userId = '${userId}';
    var userName = '${userName}';
    var topContent = new Vue({
        el: '#system_top',
        data: {
            gdata: {},
            oldpwd: '',
            pwd: '',
            confirmPwd: '',
            ukey: '',
            oldid: '',
            userName: '', // 用户名
            topbarConfig: [
                {
                    icon: 'icon-warning-hex',
                    title: '报警',
                    badge: alarmMsgContent, //如果定义的badge，将显示小红点
                    action: function (data) {
                        alarm();
                        alarmNoAuth();
                        alarmPastDue();
                        alarmRegisterNum();
                    }
                },
                {
                    icon: 'icon-fullscreen-hex',
                    title: '全屏',
                    action: function (data) {
                        gd.toggleFullscreen();
                    }
                },
                {
                    icon: 'icon-account-hex',
                    text: userName,
                    dropItems: [
                        //如果定义了dropItems，将显示下拉框
                        {
                            text: '个人信息', //下拉框的文本
                            action: function (data) {
                                //下拉框的动作
                                gd.showLayer({
                                    id: 'editPersonalWind',//可传一个id作为标识
                                    title: '编辑个人信息',//窗口标题
                                    content: $("#personal_information").html(),
                                    size: [540, 460],
                                    btn: [{
                                        text: '确定',
                                        enter: true,//响应回车
                                        action: function (dom) {//参数为当前窗口dom对象
                                            if (!getEditValidate.valid()) {
                                                return false;
                                            }
                                            var resultData = $('form#editpersonal').serializeJSON();
                                            resultData.id = userId;
                                            if (resultData.password != "") {
                                                resultData.password = encrypt(resultData.password);
                                            }
                                            if (resultData.prePassword != "") {
                                                resultData.prePassword = encrypt(resultData.prePassword);
                                            }
                                            gd.post(ctx + "/systemSetting/user/editPersonalUserInfo", resultData, function (msg) {
                                                if (msg.resultCode == 0) {
                                                    gd.showSuccess("保存成功");
                                                    dom.close();
                                                } else {
                                                    gd.showError("保存失败 " + (msg.resultMsg || ''));
                                                }
                                            })
                                            return false;//阻止弹窗自动关闭
                                        }
                                    }, {
                                        text: '取消',
                                        action: function () {
                                        }
                                    }],
                                    success: function (dom) {//参数为当前窗口dom对象
                                        gd.get(ctx + "/systemSetting/user/getUserById", {userId: userId}, function (msg) {
                                            $(".userName").html(msg.data.userName);
                                            $(".editname").html(msg.data.name);
                                            $(".editrole").html(msg.data.roleTypeName);
                                            $("input[name=phone]").val(msg.data.phone);
                                        })
                                        getEditValidate = gd.validate('#editpersonal', {
                                            autoPlaceholer: false, //自动添加placeholder，默认为false
                                        })
                                    },
                                    end: function (dom) {//参数为当前窗口dom对象
                                    }
                                });
                            }
                        },
                        {
                            text: '退出',
                            action: function (data) {
                                $('#logoutForm').submit();
                            }
                        }
                    ]
                }]
        }
    });
</script>