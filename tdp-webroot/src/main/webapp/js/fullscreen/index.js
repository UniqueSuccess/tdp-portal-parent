
var potentRiskChart = ''//潜在风险图表
var potentRiskOpt = ''//潜在风险图表配置


var videoMoveChart = '';//视频流转
var videoMoveOpt = '';//视频流转配置
var submitDate = 'day';//时间
var startDate = "";
var endDate = "";
var reportData = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];
var dataX = ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];//图表数组



var personMoveChart = '';//人员视频流转
var personMoveOpt = '';//人员视频流转配置
var deptMoveChart = '';//部门视频流转分布
var deptMoveOpt = '';//部门视频流转分布


$(function () {
    initEvents(); //初始化事件


    //潜在风险
    potentRiskInfo();
    //时间事件
    timeFun();
    //视频流转
    videoMove();
    getRistInfo("week");
    todayOutExport();
    videoMonitoring();
    //部门流转
    deptMove();
    //人员视频流转top10
    personMove();
    getAllData();//获取用户数据先不用

});

/**
 * 初始化事件
 *
 */
function initEvents() {
    $("body").on("click",".exit",function(){
        location.href = ctx + '/homepage/index';
    })



    //窗口缩放调整图表
    $("body #menus").resize(function () {
        if (personMoveChart) {
            personMoveChart.resize();
        }

    });
    $(window).resize(function () {

        if (personMoveChart) {
            personMoveChart.resize();
        }

    });

}
//时间事件
setInterval(timeFun,1000);
//时间事件
function timeFun(){
    var year = new Date().getFullYear();
    var month = (new Date().getMonth()+1) < 10? "0"+(new Date().getMonth()+1) : (new Date().getMonth()+1);
    var data = new Date().getDate() < 10? "0"+new Date().getDate() : new Date().getDate();
    var hours = new Date().getHours() < 10? "0"+new Date().getHours() : new Date().getHours();
    var minutes = new Date().getMinutes() < 10? "0"+new Date().getMinutes() : new Date().getMinutes();
    var seconds = new Date().getSeconds() < 10? "0"+new Date().getSeconds() : new Date().getSeconds();
    var day = new Date().getDay();
    $("body .year").html(year);
    $("body .month").html(month);
    $("body .day").html(data);
    $("body .time").html(hours+":"+minutes+":"+seconds);
    var weekArr = ["周天","周一","周二","周三","周四","周五","周六"];
    for(var i = 0;i<7;i++){
        if(day == i){
            $("body .week").html(weekArr[i]);
        }
    }


}
// 判断浏览器种类
function exitScreen(){
    var evt = $.Event('keydown', {keyCode: 122});
    $(document).trigger(evt);
}
// 获取所有的数据
function getAllData() {
    //用户总数
    //潜在风险统计
    var allConnt = 0;
    getAjax(ctx + '/clientUser/countAllClientUser', '', function (msg) {
        if (msg.resultCode == 1) {
            allConnt = msg.data.allConnt;
            $(".terminal-num").html(allConnt);
            //潜在风险统计
            getAjax(ctx + '/policy/countPolicyPotentialRisk', '', function (msg) {
                if (msg.resultCode == 1) {
                    var x = 0;
                    var y = 0;
                    for (var i = 1; i < 4; i++) {
                        x += Number(msg.data[i]);
                    }
                    y = allConnt * 3;
                    var num = ((Number(((1 - (x / y)).toFixed(2)) * 100)) / 100);
                    if (x == 0) {
                        potentRiskOpt.series[0].data = [1];
                    } else {
                        if (1 > num && num >= 0.9) {
                            potentRiskOpt.series[0].data = [num.toFixed(2)];
                            potentRiskOpt.series[0].color = ['#FFBF2D'];
                            potentRiskOpt.series[0].label.color = ['#FFBF2D'];
                            potentRiskOpt.series[0].outline.itemStyle.borderColor = ['#FFBF2D'];

                        } else {
                            potentRiskOpt.series[0].data = [num.toFixed(2)];
                            potentRiskOpt.series[0].color = ['#FF4706'];
                            potentRiskOpt.series[0].label.color = ['#FF4706'];
                            potentRiskOpt.series[0].outline.itemStyle.borderColor = ['#FF4706'];
                        }
                    }

                    $(".screen").text(msg.data[1]);
                    $(".out").text(msg.data[2]);
                    $(".export").text(msg.data[3]);
                    potentRiskChart.clear();
                    potentRiskChart.setOption(potentRiskOpt);

                } else {

                    layer.msg('潜在风险连接服务器失败', {icon: 2});
                }
            });
        } else {
            layer.msg('用户总数连接服务器失败', {icon: 2});
        }
    });

    //审批待审批
    getAjax(ctx + "/approveFlow/countApproveByState", {"status": 0}, function (msg) {
        if (msg.resultCode == 1) {
            $(".j-waiting").text(msg.data);
        } else {
            layer.msg('审批待审批连接服务器失败', {icon: 2});
        }
    });
    //审批未通过
    getAjax(ctx + "/approveFlow/countApproveByState", {"status": -1}, function (msg) {
        if (msg.resultCode == 1) {
            $(".j-no").text(msg.data);
        } else {
            layer.msg('审批未通过连接服务器失败', {icon: 2});
        }
    });
    //审批已通过
    getAjax(ctx + "/approveFlow/countApproveByState", {"status": 1}, function (msg) {
        if (msg.resultCode == 1) {
            $(".j-yes").text(msg.data);
        } else {
            layer.msg('审批已通过连接服务器失败', {icon: 2});
        }
    });
    //审批已审批
    getAjax(ctx + "/approveFlow/countApproveByState", {"status": 2}, function (msg) {
        if (msg.resultCode == 1) {
            $(".j-all").text(msg.data);
        } else {
            layer.msg('审批已审批连接服务器失败', {icon: 2});
        }
    });
    //部门视频流转分布
    getAjax(ctx + '/report/countVideoTransferByDepartment', '', function (msg) {

        if (msg.resultCode == 1) {
            var legend = new Array();
            var tempData = new Array();
            for (var i = 0; i < msg.data.length; i++) {
                var deptData = new Object();
                legend.push(msg.data[i].name);
                deptData.value = msg.data[i].count;
                deptData.name = msg.data[i].name;
                deptData.id = msg.data[i].id;
                tempData.push(deptData);
            }
            if (getObjArrayCount(tempData) == 0) {
                $('#dept_video').addClass('vedio_export_empty');
                return;
            }

            deptMoveOpt.series[0].data = tempData;
            deptMoveOpt.legend.data = legend;
            deptMoveChart.setOption(deptMoveOpt);
        } else {
            layer.msg('部门视频流转分布连接服务器失败', {icon: 2});
        }
    });

    //top5
    getAjax(ctx + '/report/countVideoTransferTop5', '', function (msg) {
        if (msg.resultCode == 1) {

            if (msg.data.length == 0) {

                $('#person_move').addClass('vedio_export_empty');
                $('.person-move').hide();
            } else {
                var temp;
                var count = new Array();
                var hunman = new Array();
                for (var i = 0; i < msg.data.length; i++) {
                    for (var j = i + 1; j < msg.data.length; j++) {
                        if (msg.data[i].countTransfer > msg.data[j].countTransfer) {
                            temp = msg.data[i];
                            msg.data[i] = msg.data[j];
                            msg.data[j] = temp;
                        }
                    }
                }
                for (var k = 0; k < msg.data.length; k++) {
                    count.push(msg.data[k].countTransfer);
                    hunman.push(msg.data[k].truename)
                }
                if (judgeNum(count, 1)) {
                    personMoveOpt.xAxis[0].splitNumber = 1;
                } else if (judgeNum(count, 2)) {
                    personMoveOpt.xAxis[0].splitNumber = 2;
                } else if (judgeNum(count, 3)) {
                    personMoveOpt.xAxis[0].splitNumber = 3;
                } else if (judgeNum(count, 4)) {
                    personMoveOpt.xAxis[0].splitNumber = 4;
                } else {
                    personMoveOpt.xAxis[0].splitNumber = 5;
                }
                personMoveOpt.yAxis[0].data = hunman;
                personMoveOpt.series[0].data = count;
                personMoveChart.setOption(personMoveOpt);
            }


        } else {
            layer.msg('top5连接服务器失败', {icon: 2});
        }
    });


}
//今日导出今日外发
function todayOutExport(){
    getAjax(ctx + '/report/getVideoTransferLogInHours', {
        "departmentId": 1,
        "submitDate": "day",
        "startDate": '',
        "endDate": '',
        "order": '',
        "logType": "OUTCFG"//外发
    }, function (msg) {
        if (msg.resultCode == 1) {
            var len = 0;
            for(var i = 0;i<msg.data.length;i++){
                len += msg.data[i]
            }
            $(".out-num").html(Number(len));
        }
    })
    getAjax(ctx + '/report/getVideoTransferLogInHours', {
        "departmentId": 1,
        "submitDate": "day",
        "startDate": '',
        "endDate": '',
        "order": '',
        "logType": "OPT"//外发
    }, function (msg) {
        if (msg.resultCode == 1) {
            var len = 0;
            for(var i = 0;i<msg.data.length;i++){
                len += msg.data[i]
            }
            $(".export-num").html(Number(len));

        }
    })
}
//视频流转方法
function getRistInfo(day) {

    videoMoveChart.clear();
    getAjax(ctx + '/report/getVideoTransferLogInHours', {
        "departmentId": 1,
        "submitDate": day,
        "startDate": '',
        "endDate": '',
        "order": '',
        "logType": "OUTCFG"//外发
    }, function (msg) {
        if (msg.resultCode == 1) {
            getAjax(ctx + '/report/getVideoTransferLogInHours', {
                "departmentId": 1,
                "submitDate": day,
                "startDate": '',
                "endDate": '',
                "order": '',
                "logType": "OPT"//导出
            }, function (msgIn) {
                if (msgIn.resultCode == 1) {
                    getAjax(ctx + '/report/getVideoTransferLogInHours', {
                        "departmentId": 1,
                        "submitDate": day,
                        "startDate": '',
                        "endDate": '',
                        "order": '',
                        "logType": ''
                    }, function (msgAll) {
                        if (msgAll.resultCode == 1) {
                            videoMoveOpt.legend.data = [{
                                name:"视频外发",
                                icon: 'image://../skin/default/images/fullScreen/len2.png',
                            },{
                                name:"视频导出",
                                icon: 'image://../skin/default/images/fullScreen/len1.png',
                            }]
                            if (day == 'day') {
                                if (judgeNum(msgAll.data, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msgAll.data, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msgAll.data, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msgAll.data, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }
                                // videoMoveOpt.series[2].data = msgAll.data;
                                videoMoveOpt.xAxis[0].data = ["01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];
                            } else if (day == 'week') {
                                if (judgeNum(msgAll.data.countList, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msgAll.data.countList, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msgAll.data.countList, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msgAll.data.countList, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }
                                // videoMoveOpt.series[2].data = msgAll.data.countList;
                                videoMoveOpt.xAxis[0].data = msgAll.data.dateList;
                            } else if (day == 'month') {
                                if (judgeNum(msgAll.data.countList, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msgAll.data.countList, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msgAll.data.countList, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msgAll.data.countList, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }
                                // videoMoveOpt.series[2].data = msgAll.data.countList;
                                videoMoveOpt.xAxis[0].data = msgAll.data.dateList;
                            }

                            if (day == 'day') {
                                //暂时保留
                                if ((getArrayCount(msg.data) + getArrayCount(msgIn.data)) == 0) {
                                    $('.no-data').show();
                                    $('.video-move').hide();
                                    return;
                                } else {
                                    $('.video-move').show();
                                    $('.no-data').hide();
                                }
                                if (judgeNum(msg.data, 1) && judgeNum(msgIn.data, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msg.data, 2) && judgeNum(msgIn.data, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msg.data, 3) && judgeNum(msgIn.data, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msg.data, 4) && judgeNum(msgIn.data, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }


                                videoMoveOpt.series[0].data = msg.data;
                                videoMoveOpt.series[1].data = msgIn.data;
                                videoMoveOpt.xAxis[0].data = ["01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];
                            } else if (day == 'week') {
                                if ((getArrayCount(msg.data.countList) + getArrayCount(msgIn.data.countList)) == 0) {
                                    $('.no-data').show();
                                    $('.video-move').hide();
                                    return;
                                } else {
                                    $('.video-move').show();
                                    $('.no-data').hide();
                                }
                                if (judgeNum(msg.data.countList, 1) && judgeNum(msgIn.data.countList, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msg.data.countList, 2) && judgeNum(msgIn.data.countList, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msg.data.countList, 3) && judgeNum(msgIn.data.countList, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msg.data.countList, 4) && judgeNum(msgIn.data.countList, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }
                                videoMoveOpt.series[0].data = msg.data.countList;
                                videoMoveOpt.series[1].data = msgIn.data.countList;
                                videoMoveOpt.xAxis[0].data = msg.data.dateList;
                            } else if (day == 'month') {
                                if ((getArrayCount(msg.data.countList) + getArrayCount(msgIn.data.countList)) == 0) {
                                    $('.no-data').show();
                                    $('.video-move').hide();
                                    return;
                                } else {
                                    $('.video-move').show();
                                    $('.no-data').hide();
                                }
                                if (judgeNum(msg.data.countList, 1) && judgeNum(msgIn.data.countList, 1)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 1;
                                } else if (judgeNum(msg.data.countList, 2) && judgeNum(msgIn.data.countList, 2)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 2;
                                } else if (judgeNum(msg.data.countList, 3) && judgeNum(msgIn.data.countList, 3)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 3;
                                } else if (judgeNum(msg.data.countList, 4) && judgeNum(msgIn.data.countList, 4)) {
                                    videoMoveOpt.yAxis[0].splitNumber = 4;
                                } else {
                                    videoMoveOpt.yAxis[0].splitNumber = 5;
                                }
                                videoMoveOpt.series[0].data = msg.data.countList;
                                videoMoveOpt.series[1].data = msgIn.data.countList;
                                videoMoveOpt.xAxis[0].data = msg.data.dateList;
                            }
                            videoMoveChart.setOption(videoMoveOpt);
                        } else {
                            layer.msg('视频流转获取图表数据失败！', {icon: 2});
                        }
                    });
                } else {
                    layer.msg('视频流转获取图表数据失败！', {icon: 2});
                }
            });
        } else {
            layer.msg('视频流转获取图表数据失败！', {icon: 2});
        }
    });

}

//判断数组中是否大于参数值
function judgeNum(arr, num) {
    var fl = true;
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] > num) {//里面没有大约num的
            fl = false;
        }

    }
    return fl;
}

// 纯数组总数判断
function getArrayCount(array) {
    var num = 0;
    for (var i = 0; i < array.length; i++) {
        num += array[i];
    }
    return num;
}

// obj数组判断
function getObjArrayCount(array) {
    var num = 0;
    for (var i = 0; i < array.length; i++) {
        num += array[i].value;
    }
    return num;
}



//视频流转
function videoMove() {
    videoMoveChart = echarts.init(document.getElementById('video_move'));
}

//视频流转
videoMoveOpt = {
    color: ['#FFBF2D'],
    tooltip: {
        trigger: 'axis',
        // formatter:'{a}: {c}条</br>{a1}: {c1}条</br>{b}'
    },
    legend: {
        orient: "horizontal",

        data: [],
        textStyle:{
            color:"#00ceda"
        },
        itemWidth: 12,
        itemHeight: 12,
        right: 0,
        top: 0,
        selectedMode: false
    },
    grid: {
        top: '40',
        left: '10',
        right: '0',
        bottom: '50',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            boundaryGap: true,
            minInterval: 0,
            splitNumber: 5,
            splitLine: {
                show: false,
                interval: 'auto',
                lineStyle: {
                    color: '#eee'
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#eee'
                }
            },
            axisLabel: {
                margin: 8,
                textStyle: {
                    fontSize: 12,
                    color: "#c3f9ff"
                }
            },
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        }
    ],
    yAxis: [
        {
            type: 'value',
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#eee',
                    opacity:0.3
                }
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#eee'
                }
            },
            axisLabel: {
                textStyle: {
                    color: "#c3f9ff"
                }
            },
            axisTick:{
                show: true,
                lineStyle: {
                    color: '#eee',
                    opacity:0.3
                }
            }
        }
    ],
    series: [
        {
            name: '视频外发',
            type: 'line',
            barWidth: "auto",
            barMinHeight: 1,
            barGap: "10%",
            smooth:true,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#0674c1'},
                            {offset: 1, color: '#0674c1'}
                        ]
                    )
                }
            },
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#0674c1'
                    }, {
                        offset: 1,
                        color: '#0674c1'
                    }]),
                    opacity:0.2
                }
            },
            data: []
        },
        {
            name: '视频导出',
            type: 'line',
            barWidth: "auto",
            barMinHeight: 1,
            barGap: "10%",
            smooth:true,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#12d7e1'},
                            {offset: 1, color: '#12d7e1'}
                        ]
                    )
                }
            },
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#12d7e1'
                    }, {
                        offset: 1,
                        color: '#12d7e1'
                    }]),
                    opacity:0.2
                }
            },
            data: []
        }
        /*,
        {
            name: '总计',
            type: 'line',
            smooth: true,
            data: []
        }*/
    ]
};

//部门流转
function deptMove() {
    deptMoveChart = echarts.init(document.getElementById('dept_move'));

}
placeHolderStyle = {
    normal: {
        label: {
            show: false,
            position: "center"
        },
        labelLine: {
            show: false
        },
        color: "#dedede",
        borderColor: "#dedede",
        borderWidth: 0
    },
    emphasis: {
        color: "#dedede",
        borderColor: "#dedede",
        borderWidth: 0
    }
};
deptMoveOpt = {
    color: ['#fc7a26', '#fff', '#ffa127', '#fff', "#ffcd26"],
    legend: [{
        orient: '',
        icon: 'circle',
        left: 'right',
        top: 'center',
        data: ['不喜欢', '喜欢', '跳过']
    }],
    series: [{
        name: '值',
        type: 'pie',
        clockWise: true, //顺时加载
        hoverAnimation: false, //鼠标移入变大
        radius: [199, 200],
        itemStyle: {
            normal: {
                label: {
                    show: true,
                    position: 'outside'
                },
                labelLine: {
                    show: true,
                    length: 100,
                    smooth: 0.5
                },
                borderWidth: 5,
                shadowBlur: 40,
                borderColor: "#fc7a26",
                shadowColor: 'rgba(0, 0, 0, 0)' //边框阴影
            }
        },
        data: [{
            value: 7,
            name: '70%'
        }, {
            value: 3,
            name: '',
            itemStyle: placeHolderStyle
        }]
    }, {
        name: '白',
        type: 'pie',
        clockWise: false,
        radius: [180, 180],
        hoverAnimation: false,
        data: [{
            value: 1
        }]
    }, {
        name: '值',
        type: 'pie',
        clockWise: true,
        hoverAnimation: false,
        radius: [159, 160],
        itemStyle: {
            normal: {
                label: {
                    show: true
                },
                labelLine: {
                    show: true,
                    length: 100,
                    smooth: 0.5
                },
                borderWidth: 5,
                shadowBlur: 40,
                borderColor: "#ffa127",
                shadowColor: 'rgba(0, 0, 0, 0)' //边框阴影
            }
        },
        data: [{
            value: 6,
            name: '60%'
        }, {
            value: 4,
            name: '',
            itemStyle: placeHolderStyle
        }]
    }, {
        name: '白',
        type: 'pie',
        clockWise: false,
        hoverAnimation: false,
        radius: [140, 140],
        data: [{
            value: 1
        }]
    }, {
        name: '值',
        type: 'pie',
        clockWise: true,
        hoverAnimation: false,
        radius: [119, 120],
        itemStyle: {
            normal: {
                label: {
                    show: true
                },
                labelLine: {
                    show: true,
                    length: 100,
                    smooth: 0.5
                },
                borderWidth: 5,
                shadowBlur: 40,
                borderColor: "#ffcd26",
                shadowColor: 'rgba(0, 0, 0, 0)' //边框阴影
            }
        },
        data: [{
            value: 4,
            name: '40%'
        }, {
            value: 6,
            name: '',
            itemStyle: placeHolderStyle
        }]
    }, {
        type: 'pie',
        color: ['#fc7a26', '#ffa127', "#ffcd26"],
        data: [{
            value: '',
            name: '不喜欢'
        }, {
            value: '',
            name: '喜欢'
        }, {
            value: '',
            name: '跳过'
        }]
    }, {
        name: '白',
        type: 'pie',
        clockWise: true,
        hoverAnimation: false,
        radius: [100, 100],
        label: {
            normal: {
                position: 'center'
            }
        },
        data: [{
            value: 1,
            label: {
                normal: {
                    formatter: '投票人数',
                    textStyle: {
                        color: '#666666',
                        fontSize: 26
                    }
                }
            }
        }, {
            tooltip: {
                show: false
            },
            label: {
                normal: {
                    formatter: '\n1200',
                    textStyle: {
                        color: '#666666',
                        fontSize: 26
                    }
                }
            }
        }]
    }]
};


//人员流转
function personMove() {
    personMoveChart = echarts.init(document.getElementById('person_move'));
}

// 人员视频数据导出信息top10
personMoveOpt = {
    tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(0,0,0,0.6)',
        // extraCssText: 'box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);',
        textStyle: {
            color: '#F0F0F0',
        },
        formatter: '{b0}: {c0}条'

    },
    grid: {
        top: '0%',
        left: '0%',
        right: '40',
        bottom: '0%',
        containLabel: true
    },
    yAxis: [{
        type: 'category',
        data: ['杨利伟', '孙杨', '孙浩'],
        nameGap: 40,
        axisTick: {
            show:false,
            alignWithLabel: true,
        },
        axisLabel: {
            margin: 15,
            textStyle: {
                fontSize: 12,
                color: '#c3f9ff'
            },
            formatter: function (pamar) {
                if (pamar.length > 5) {
                    pamar = pamar.substring(0, 4) + '...';
                }
                return pamar;
            }
        },
        axisLine: {
            show:false,
            lineStyle: {
                color: '#dbe0e6'
            }
        },

    }],
    xAxis: [{
        type: 'value',
/*
        min: null,
        minInterval: 0,
        splitNumber: 5,*/
        axisTick: {
            show:false,
            alignWithLabel: true,
        },
        axisLabel: {
            show:false,
            margin: 5,
            textStyle: {
                fontSize: 12,
                color: '#94999f'
            }
        },
        axisLine: {
            show:false,
            lineStyle: {
                color: '#fff'
            }
        },
        splitLine: {
            show:false,
            lineStyle: {
                color: '#dbe0e6'
            }
        }
    }],
    series: [{
        name: '',
        type: 'bar',
        barWidth: 15,
        data: [20, 30, 40],
        label: {
            normal: {
                show: true,
                position: 'right',
                // offset: [0, -1],
                textStyle: {
                    color: '#c3f9ff', //color of value
                    size: 12,
                }
            }
        },
        itemStyle: {
            normal: {

                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                    offset: 0,
                    color: '#1ddce7' // 0% 处的颜色
                }, {
                    offset: 1,
                    color: '#0bd4bd' // 100% 处的颜色
                }], false),
                barBorderRadius: [0, 15, 15, 0],
                shadowColor: 'rgba(0,0,0,0.1)',
                shadowBlur: 3,
                shadowOffsetY: 3
            }
        }
    }]
};

//视频流转实时监控
/**
 * order:
 start: 0
 length: 10
 search[value]:
 search[regex]: false
 departmentId: 1
 submitDate: week
 startDate:
 endDate:
 logType:
 *
 */
function videoMonitoring(){
    getAjax(ctx + '/report/getVideoTransferLogInPage', {
        start:0,
        length:100,
        departmentId: 1,
        submitDate: "week",
        startDate:"",
        endDate:"",
        logType:""
    }, function (msg) {
        if (msg.resultCode == 1) {
          $("#real_time_monitor ul").html(template("carousel",msg.data));
            $(document).ready(function(){
                $("body #real_time_monitor").Scroll({line:1,speed:500,timer:3000});
            });
        console.log(msg);


        } else {
            layer.msg('视频流转监控数据获取失败', {icon: 2});
        }
    });
}
//安全检测
//潜在风险
function potentRiskInfo() {
    potentRiskChart = echarts.init(document.getElementById('safe_check'));
}
var arr = [];
var r = 100;
for (var i = 0; i < 1000; i++) {
    var rad = 2 * Math.PI / 1000 * i;
    var x = Math.cos(rad) * r + 500;
    var y = Math.sin(rad) * r + 500;

    arr.push([x, y]);
}

var arr2 = [];
for (var i = 500; i < 1000; i++) {
    var rad = 2 * Math.PI / 1000 * i;
    var x = Math.cos(rad) * r + 500;
    var y = Math.sin(rad) * r + 500;

    arr2.push([x, y]);
}
for (var i = 0; i < 500; i++) {
    var rad = 2 * Math.PI / 1000 * i;
    var x = Math.cos(rad) * r + 500;
    var y = Math.sin(rad) * r + 500;

    arr2.push([x, y]);
}


dataBJ = [

    [134, 96, 165, 41],

];
indicatorData = [{
    name: '屏幕无水印',
    max: 400
}, {
    name: '无审批',
    max: 400
}, {
    name: '外发无水印',
    max: 300
}, {
    name: '导出无水印',
    max: 300
}];

var rotate = 45;
var width = 4;
potentRiskOpt = {
    title: {
        text: '436',
        x: 'center',
        y: 'center',
        textStyle: {
            fontWeight: 'bold',
            color: "#fff",
            fontSize: 80
        },
        zlevel: 21
    },
    legend: {
        bottom: 0,
        orient: 'horizontal',
        itemWidth: 30,
        itemHeight: 20,

        data: [{
            name: '19-35岁',
            icon: 'circle',
            textStyle: {
                color: "#fc20ff"
            }
        }]
    },
    tooltip: {},
    xAxis: {

        max: 1000,
        min: 0,
        interval: 100,
        show: false,
        silent: true

    },
    yAxis: {

        max: 1000,
        min: 0,
        interval: 100,
        show: false,
        silent: true
    },
    radar: {
        center: ['50%', '50%'],
        indicator: indicatorData,
        radius: '65%',
        splitNumber: 1,
        shape: 'circle',
        name: {
            textStyle: {
                color: '#fff',
                fontSize: 18,
                fontFamily: "Microsoft YaHei"
            },

            padding: [8, 8, 8, 8]
        },
        splitLine: {

            lineStyle: {
                color: '#4f8bbe',
                opacity: 0.5,
                type: 'dotted'
            }
        },
        splitArea: {
            show: true,
            areaStyle: {
                color: '#0d6dba',
                opacity: 0
            }
        },
        axisLine: {
            show: true,
            lineStyle: {
                color: '#4f8bbe',
                opacity: 0.5,
                type: 'dotted'
            }
        },
        axisTick: {
            show: true
        },
        axisLabel: {
            show: true,
            formatter: function(value, index) {

                return value;
            }
        },
        zlevel: 20
    },
    series: [

        {
            name: '左下红弧',
            type: 'gauge',
            radius: '99%',
            startAngle: -160,
            endAngle: -170,
            zlevel: 22,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.2, '#d70029'],
                        [1, '#d70029']
                    ],
                    width: 4,
                    shadowColor: '#d70029',
                    shadowOffsetX: 0,
                    shadowOffsetY: -12,
                    shadowBlur: 120,
                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        },


        {
            name: '右上红弧',
            type: 'gauge',
            radius: '99%',
            startAngle: -340,
            endAngle: -350,
            zlevel: 22,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.2, '#d70029'],
                        [1, '#d70029']
                    ],
                    width: 4,
                    shadowColor: '#d70029',
                    shadowOffsetX: 0,
                    shadowOffsetY: -12,
                    shadowBlur: 120,
                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        },


        {
            name: '右外圈',
            type: 'gauge',
            radius: '99%',
            startAngle: -320,
            endAngle: -400,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.2, '#0e313f'],
                        [0.8, '#0e313f'],
                        [1, '#0e313f']
                    ],
                    width: 4,
                    // shadowColor: '#d70029',
                    shadowOffsetX: 0,
                    shadowOffsetY: -12,
                    shadowBlur: 120,
                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        },

        {
            name: '左外圈',
            type: 'gauge',
            radius: '99%',
            startAngle: -140,
            endAngle: -220,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.2, '#0e313f'],
                        [0.8, '#0e313f'],
                        [1, '#0e313f']
                    ],
                    width: 4,
                    // shadowColor: '#d70029',
                    shadowOffsetX: 0,
                    shadowOffsetY: -12,
                    shadowBlur: 120,
                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        },

        {
            name: '蓝色外圈1',
            type: 'gauge',
            radius: '99%',
            startAngle: -85 + rotate,
            endAngle: -115 + rotate,
            splitNumber: 4,
            axisLine: {
                lineStyle: {
                    color: [
                        [1, '#1e5a67']
                    ],
                    width: width,


                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        }, {
            name: '蓝色外圈2',
            type: 'gauge',
            radius: '99%',
            startAngle: -155 + rotate,
            endAngle: 175 + rotate,
            splitNumber: 4,
            axisLine: {
                lineStyle: {
                    color: [
                        [1, '#1e5a67']
                    ],
                    width: width,


                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        }, {
            name: '蓝色外圈3',
            type: 'gauge',
            radius: '99%',
            startAngle: 25 + rotate,
            endAngle: -5 + rotate,
            splitNumber: 4,
            axisLine: {
                lineStyle: {
                    color: [
                        [1, '#1e5a67']
                    ],
                    width: width,


                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        }, {
            name: '蓝色外圈4',
            type: 'gauge',
            radius: '99%',
            startAngle: 95 + rotate,
            endAngle: 65 + rotate,
            splitNumber: 4,
            axisLine: {

                lineStyle: {
                    color: [
                        [1, '#1e5a67']
                    ],
                    width: width,
                    opacity: 1,
                }

            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            detail: {
                show: false
            }
        },

        {
            type: 'pie',

            radius: ['72%', '92%'],
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: 1000,
                tooltip: {
                    show: false
                },
                itemStyle: {
                    normal: {
                        color: '#4dfaff',
                        opacity: 0.2
                    }
                }
            }]
        },

        {
            name: '雷达线ALL',
            type: 'radar',
            silent: true,
            lineStyle: {
                normal: {
                    type: 'dotted',
                    color: "#355862",
                    width: 2,
                    opacity: 1,

                }
            },
            data: [
                [300, 300, 300, 300, 300]
                // { name:'A',value:300,label:{ normal:{ show:true,zlevel:30 } } },
                // { name:'B',value:300,label:{ normal:{ show:true,zlevel:30 } } },
                // { name:'C',value:300,label:{ normal:{ show:true,zlevel:30 } } },
                // { name:'D',value:300,label:{ normal:{ show:true,zlevel:30 } } },
                // { name:'E',value:300,label:{ normal:{ show:true,zlevel:30 } } },
            ],

            label: {
                normal: {
                    show: true
                }
            },

            itemStyle: {
                normal: {
                    opacity: 0

                }
            },
            areaStyle: {
                normal: {
                    color: '#0d6dba',
                    opacity: 0
                }
            }
        },

        {
            name: '雷达线2',
            type: 'radar',
            silent: true,
            lineStyle: {
                normal: {
                    type: 'dotted',
                    color: "#355862",
                    width: 2,
                    opacity: 0.8,

                }
            },
            data: [
                // [250, 250, 250, 250, 250]

                {
                    name: 'B',
                    value:  [250, 250, 250, 250, 250],
                    symbol:'circle',
                    symbolSize:1,
                    label: {
                        normal: {
                            show: true
                        }
                    },
                    itemStyle:{
                        normal:{
                            opacity:0.85
                        }
                    }
                }
            ],

            itemStyle: {
                normal: {
                    opacity: 0

                }
            },
            areaStyle: {
                normal: {
                    color: 'rgba(0,0,0,0)',
                    opacity: 0
                }
            }
        },

        {
            name: '雷达线3',
            type: 'radar',
            silent: true,
            lineStyle: {
                normal: {
                    type: 'dotted',
                    color: "#355862",
                    width: 2,
                    opacity: 0.6,

                }
            },
            data: [
                // [200, 200, 200, 200, 200]

                {
                    name: 'A',
                    value: [200, 200, 200, 200, 200],
                    symbol:'circle',
                    symbolSize:1,
                    label: {
                        normal: {
                            show: true
                        }
                    },
                    itemStyle:{
                        normal:{
                            opacity:0.85
                        }
                    }
                }
            ],

            itemStyle: {
                normal: {
                    opacity: 0

                }
            },
            areaStyle: {
                normal: {
                    color: 'rgba(0,0,0,0)',
                    opacity: 0
                }
            }
        },

        {
            name: '雷达线4',
            type: 'radar',
            silent: true,
            lineStyle: {
                normal: {
                    type: 'dotted',
                    color: "#355862",
                    width: 2,
                    opacity: 0.4,

                }
            },
            data: [
                // [150, 150, 150, 150, 150]
                {
                    name: 'B',
                    value:  [150, 150, 150, 150, 150],
                    symbol:'circle',
                    symbolSize:1,
                    label: {
                        normal: {
                            show: true
                        }
                    },
                    itemStyle:{
                        normal:{
                            opacity:1
                        }
                    }
                }
            ],

            itemStyle: {
                normal: {
                    opacity: 0

                }
            },
            areaStyle: {
                normal: {
                    color: 'rgba(0,0,0,0)',
                    opacity: 0
                }
            }
        },

        {
            name: '雷达线5',
            type: 'radar',
            silent: true,
            lineStyle: {
                normal: {
                    type: 'dotted',
                    color: "#355862",
                    width: 2,
                    opacity: 0.2,

                },

            },
            data: [
                // [100, 100, 100, 100, 100]

                {
                    name: 'B',
                    value:  [100, 100, 100, 100, 100],
                    symbol:'circle',
                    symbolSize:1,
                    label: {
                        normal: {
                            show: true
                        }
                    },
                    itemStyle:{
                        normal:{
                            opacity:1
                        }
                    }
                }
            ],

            itemStyle: {
                normal: {
                    opacity: 0

                }
            },
            areaStyle: {
                normal: {
                    color: 'rgba(0,0,0,0)',
                    opacity: 0
                }
            }
        },


        {
            name: '数据显示',
            type: 'radar',
            lineStyle: {
                normal: {
                    width: 0.1,
                    opacity: 0.1
                }
            },
            data: dataBJ,
            symbolSize: 0,
            itemStyle: {
                normal: {
                    borderColor: '#32565f',
                    borderWidth: 4,
                }
            },
            areaStyle: {
                normal: {
                    color: '#ca4a49',
                    opacity: 0.85
                }
            },
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
            zlevel: 21
        },




        {
            name: "仪盘表",
            type: "gauge",
            // min: 0,
            // max: 360,
            startAngle: 0,
            endAngle: 15,
            splitNumber: 5,
            radius: '92%',
            // radius: ['72%', '92%'],
            zlevel: 22,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.1, "#d70029"],
                        [1, "#0d2534"]
                    ],
                    // width: 90,
                    opacity: 0
                },
            },
            axisTick: {
                lineStyle: {
                    // color: '#4dfdfe',
                    color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [{
                        color: '#4dfdfe',
                        offset: 0.9,
                    }, {
                        color: '#143a49',
                        offset: 0.2
                    }]),
                    width: 2,
                    opacity: [
                        [0.1, 1],
                        [0.5, 0.5],
                        [1, 0.1]
                    ]
                },

                length: '22%',
                splitNumber: 2
            },
            pointer: {
                shadowColor: '#fff',
                shadowBlur: 5,
                show: false
            },
            axisLabel: {
                distance: 10,
                textStyle: {
                    color: "#fff"
                },
                show: false,
            },
            splitLine: {
                "show": false
            },
            itemStyle: {
                normal: {
                    color: "#494f50"
                }
            },
            detail: {

                show: false
            }
        },


        {
            name: "仪盘表",
            type: "gauge",
            // min: 0,
            // max: 360,
            startAngle: 180,
            endAngle: 195,
            splitNumber: 5,
            radius: '92%',
            // radius: ['72%', '92%'],
            zlevel: 22,
            axisLine: {
                lineStyle: {
                    color: [
                        [0.1, "#d70029"],
                        [1, "#0d2534"]
                    ],
                    // width: 90,
                    opacity: 0
                },
            },
            axisTick: {
                lineStyle: {
                    // color: '#4dfdfe',
                    color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [{
                        color: '#4dfdfe',
                        offset: 0.9,
                    }, {
                        color: '#143a49',
                        offset: 0.2
                    }]),
                    width: 2,
                    opacity: [
                        [0.1, 1],
                        [0.5, 0.5],
                        [1, 0.1]
                    ]
                },

                length: '22%',
                splitNumber: 2
            },
            pointer: {
                shadowColor: '#fff',
                shadowBlur: 5,
                show: false
            },
            axisLabel: {
                distance: 10,
                textStyle: {
                    color: "#fff"
                },
                show: false,
            },
            splitLine: {
                "show": false
            },
            itemStyle: {
                normal: {
                    color: "#494f50"
                }
            },
            detail: {

                show: false
            }
        }

    ]
};
potentRiskChart.setOption(potentRiskOpt);

function animate () {

    var series1 = potentRiskOpt.series[potentRiskOpt.series.length - 2];
    // series1.startAngle += 1;
    series1.startAngle = Date.now() * 0.01;
    series1.endAngle = series1.startAngle + 15;

    var series2 = potentRiskOpt.series[potentRiskOpt.series.length - 1];
    // series2.startAngle += 1;
    series2.startAngle = Date.now() * 0.01 + 180;
    series2.endAngle = series2.startAngle + 15;


    potentRiskChart.setOption(potentRiskOpt);

    requestAnimationFrame(animate)
}

setTimeout(animate, 500);





