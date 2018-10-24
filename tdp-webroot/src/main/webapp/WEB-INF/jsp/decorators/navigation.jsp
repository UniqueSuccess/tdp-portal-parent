<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!-- menu.jsp -->
<style>
    .policy-box{
        padding: 0 20px;
    }
    .gd-layer .gd-layer-body .gd-layer-header{
        border-bottom: none;
    }
    .clearfix:before{
        content: " ";
        display: table;
    }
    .clearfix:after{
        content: " ";
        display: table;
        clear: both;
    }
    .policy-list{
        margin-bottom: 10px;
    }
    .policy-list li{
        width: 175px;
        height: 85px;
        line-height: 85px;
        float: left;
        margin: 8px 8px 0 0;
        border: 1px solid #e5e5e5;
        border-radius: 1px;
        text-align: center;
        position: relative;
        cursor: pointer;
    }
    .policy-list li:nth-of-type(4n) {
        margin-right: 0;
    }
    .policy-list li:hover{
        border-color: #58a5e3;
        color: #58a5e3;
    }
    .policy-list li:hover .icon-delete{
        display: inline-block;
    }
    .policy-list li .icon-delete{
        position: absolute;
        right: 8px;
        bottom: 8px;
        color: #c9ccd5;
        display: none;
        line-height: 14px;
    }

</style>

<div id="leftnav">
    <gd-leftmenu :config="leftMenuConfig"></gd-leftmenu>
</div>

<script type="text/html" id="policy_select_temp">
    <div class="policy-box" id="policy_select">
        <i title="添加" class="gd-btn-icon icon-add" @click="addPolicy()"></i>
        <ul class="policy-list clearfix">
            <li v-for="item in policyArray" @click="goPage(item.id)">
                <span>{{item.name}}</span>
                <i title="删除" class="icon-delete" @click.stop="deletePolicy(item.id)"></i>
            </li>
        </ul>
    </div>
</script>

<script type="text/html" id="policy_add_temp">
    <div class="container" id="policy_add">
        <form id="add_dept_form">
            <div class="row">
                <label class="gd-label-required">策略名</label>
                <input type="text" class="gd-input gd-input-lg" gd-validate="required" name="name">
            </div>
            <div class="row">
                <label class="">继承自</label>
                <gd-select placeholder="请选择" v-model="selectPolicyValue" class="gd-select-lg" name="pid">
                    <gd-option v-for="item in policyArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
        </form>
    </div>
</script>

<script>
    var app = new Vue({
        el: '#leftnav',
        data: {
            policyArray: [], // 策略
            leftMenuConfig: {
                api:ctx + '/system/navigation/getUserNavigation', //接口地址
                logo: ctxImg + '/leftmenu/logo.png', //logo地址
                apiCallback: function (data) {
                    return data;
                },
                logoStyle: {//设置logo的样式，可选
                    backgroundPosition: '0px'
                }
            }
        },
        mounted: function() {
            this.getPolicyList();
            this.initPolicyLayer();
        },
        methods: {
            // 获取策略列表
            getPolicyList: function() {
                var _this = this;
                gd.get(ctx + '/policy/getAllPolicys', '', function(msg) {
                    if (msg.resultCode == '0') {
                        _this.policyArray = msg.data;
                    }
                })
            },
            // 添加策略的点击事件
            initPolicyLayer: function() {

            }
        }
    });

    // 策略li的点击事件
    $('body').on('click', '.gd-left-menu-body li', function() {
        if($(this).find('.icon-strategy').length !== 0) {
            gd.showLayer({
                id: 'policyWind',//可传一个id作为标识
                title: '选择策略',//窗口标题
                content: $('#policy_select_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                //url: './layer_content.html',//也可以传入url作为content,
                size: [780, 475],//窗口大小，直接传数字即可，也可以是['600px','400px']
                //autoFocus:true,//自动对输入框获取焦点，默认为ture
                success: function(dom) {
                    var selectWindow = new Vue({
                        el: '#policy_select',
                        data: {
                            policyArray: []
                        },
                        mounted: function() {
                            this.getPolicyList();
                        },
                        methods: {
                            addPolicy: function() {
                                gd.showLayer({
                                    id: 'policyAddWind',//可传一个id作为标识
                                    title: '新建策略',//窗口标题
                                    content: $('#policy_add_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                    //url: './layer_content.html',//也可以传入url作为content,
                                    size: [670, 280],//窗口大小，直接传数字即可，也可以是['600px','400px']
//                                            autoFocus:true,//自动对输入框获取焦点，默认为ture
                                    btn: [
                                        {
                                            text: '确定',
                                            action: function (dom) {
                                                var postData = $('#policyAddWind #add_dept_form').serializeJSON();
                                                console.log(postData)
                                                gd.post(ctx + '/policy/addPolicy', postData, function (msg) {
                                                    console.log(msg);
                                                    if (msg.resultCode == '0') {
                                                        gd.showSuccess('保存成功');
                                                        selectWindow.getPolicyList();
                                                    } else {
                                                        gd.showError('保存失败 ' + (msg.resultMsg || ''));
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
                                    success: function(dom) {
                                        var addWindow = new Vue({
                                            el: '#policy_add',
                                            data: {
                                                policyArray: selectWindow.policyArray,
                                                selectPolicyValue: '1'
                                            }
                                        })
                                    },
                                    end: function (dom) {//参数为当前窗口dom对象
                                        // gd.showSuccess('窗口关闭了');
                                    }
                                });
                            },
                            // 跳页
                            goPage: function (id) {
                                window.location.href = ctx + '/policy/readPolicyJsonFileById?id='+ id;
                            },
                            // 删除策略
                            deletePolicy: function (id) {
                                if (id == 1) {
                                    gd.showWarning('默认策略不可删除');
                                    return;
                                }
                                gd.showConfirm({
                                    id: 'wind',
                                    content: '确定要删除吗?',
                                    btn: [{
                                        text: '确定',
                                        class: 'gd-btn-danger', //也可以自定义类
                                        action: function (dom) {
                                            gd.post(ctx + '/policy/deletePolicy', {policyId: id}, function(msg) {
                                                if (msg.resultCode == 0) {
                                                    gd.showSuccess('删除成功');
                                                    selectWindow.getPolicyList();
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

                            },
                            // 获取策略列表
                            getPolicyList: function() {
                                var _this = this;
                                gd.get(ctx + '/policy/getAllPolicys', '', function(msg) {
                                    if (msg.resultCode == '0') {
                                        _this.policyArray = msg.data;
                                    }
                                })
                            },
                        }
                    })
                },
                end: function (dom) {//参数为当前窗口dom对象
                    // gd.showSuccess('窗口关闭了');
                }
            });
        }
        return false;
    });

    /**
     * Created by chengl on 2018/1/31 0031.
     */
    var websocket;
    var host = window.location.host;

    function initSocket(option) {
        //服务器地址
        if ('WebSocket' in window) {
            //websocket =  new WebSocket("ws://" + host + "/tdp/vdpWebSocketServer");
            var protocolStr = document.location.protocol;
            if(protocolStr == "http:")
            {
                websocket = new ReconnectingWebSocket("ws://" +
                    host + "/tdp/vdpWebSocketServer", null, {
                    debug: true,
                    maxReconnectAttempts: 2,
                    timeoutInterval: 100000
                });
            }
            else if(protocolStr == "https:")
            {
                websocket = new ReconnectingWebSocket("wss://" +
                    host + "/tdp/vdpWebSocketServer", null, {
                    debug: true,
                    maxReconnectAttempts: 2,
                    timeoutInterval: 100000
                });
            }
        } else if ('MozWebSocket' in window) {
            var protocolStr = document.location.protocol;
            if(protocolStr == "http:")
            {
                websocket = new MozWebSocket("ws://" + host +
                    "/tdp/vdpWebSocketServer");
            }
            else if(protocolStr == "https:")
            {
                websocket = new MozWebSocket("wss://" + host +
                    "/tdp/vdpWebSocketServer");
            }
        } else {
            websocket = new SockJS("http://" + host +
                "/tdp/sockjs/webSocketIMServer");
        }
        // var url = "wss://echo.websocket.org";
        //回调函数审批
        var approveCallback = option.approveCallback;
        if (typeof approveCallback !== "function") {
            return false;
        }
        //回调函数告警
        var warningCallback = option.warningCallback;
        if (typeof warningCallback !== "function") {
            return false;
        }
        //一些对浏览器的兼容已经在插件里面完成
        // websocket = new ReconnectingWebSocket(url);
        //var websocket = new WebSocket(url);

        //连接发生错误的回调方法


        //连接成功建立的回调方法
        websocket.onopen = function (event) {
            //成功建立之后需要给后台地址发送的数据
            var approveParam = {
                service: "approveFlow",
                invoke: "noticeApproverByGuid",
                convert: true,
                params: null
            };
            var warningParam = {
                service: "illegalOperationAlarm",
                invoke: "noticeIllegalOperationAlarm",
                convert: false,
                params: null
            };
            websocket.send(JSON.stringify(approveParam));
            websocket.send(JSON.stringify(warningParam));
            window.setInterval(function () { //每隔5秒钟发送一次心跳，避免websocket连接因超时而自动断开
                websocket.send(JSON.stringify(approveParam));
            }, 50000);

        };
        websocket.onerror = function (e) {
            console.log(e);
        };
        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            if(JSON.parse(event.data).messageType == "approveCount"){
                approveCallback(JSON.parse(event.data).count)
            }
            if(JSON.parse(event.data).messageType == "alarmCount"){
                warningCallback(JSON.parse(event.data).count,JSON.parse(event.data).sound)
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            websocket.close();
        };
        return websocket;
    }

    $(function () {
        var option = {
            approveCallback: function (data) {
                //处理业务逻辑多加一些判断
               /* if (data == 0) {
                    $(".approveNum").hide();
                } else {
                    $(".approveNum").show();
                    $(".approveNum").addClass('approTips');
                    $(".approveNum").html(data);
                }
*/
               if(data != 0){
                   gd.menu('审批请求').addTip();//添加红点提示
               }


            },
            warningCallback: function (data,fl) {
                warningNum = data;
                //处理业务逻辑多加一些判断
                if (data == 0) {
                    $("#top_alarm").hide();
                } else {
                    $("#top_alarm").show();
                    $("#top_alarm").html(data);
                    if(fl == true){
                        var alarm_audio = document.getElementById("alarm_audio");
                        alarm_audio.play();
                    }

                }
            }
        };
        var socket = initSocket(option);
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function (){
            socket.close();
        }
    })

</script>