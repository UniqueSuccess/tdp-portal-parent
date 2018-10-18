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
            if (data == 0) {
                $(".approveNum").hide();
            } else {
                $(".approveNum").show();
                $(".approveNum").addClass('approTips');
                $(".approveNum").html(data);
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