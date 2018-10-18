var homepage = new Vue({
    el: "#homepage",
    data: {
    }
});




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
    //潜在风险
    potentRiskInfo();
    //视频流转
    videoMove();
    getRistInfo("day", "noall");
    //部门流转
    deptMove();
    //人员视频流转top10
    personMove();
    getAllData();//获取用户数据先不用
    initEvents(); //初始化事件
});

/**
 * 初始化事件
 *
 */
function initEvents() {
    //窗口缩放调整图表
    $('body')
    //视频流转图表切换
        .on('click', '.data-select .right span', function () {
            $(this).addClass('color-white');
            $(this).siblings("span").removeClass('color-white')
        })
        .on('click', '.data-select .right .day', function () {
            if ($('.left .all').hasClass("color-white")) {
                getRistInfo("day", "all");
            } else {
                getRistInfo("day", "noall");
            }
        })
        .on('click', '.data-select .right .week', function () {
            if ($('.left .all').hasClass("color-white")) {
                getRistInfo("week", "all");
            } else {
                getRistInfo("week", "noall");
            }
        })
        .on('click', '.data-select .right .month', function () {
            if ($('.left .all').hasClass("color-white")) {
                getRistInfo("month", "all");
            } else {
                getRistInfo("month", "noall");
            }
        })
        /*//审批跳转
        .on('click', '.content-cell', function () {
            if ($('#menulist a[href="/tdp/approveDefinition/index"]').length == 0) {
                layer.msg('您没有查看权限！', {icon: 7});
                return;
            }
            var param = $(this).attr('data-value');
            location.href = ctx + '/approveDefinition/index?status=' + param;
        })*/
    $("body #menus").resize(function () {
        if (potentRiskChart) {
            potentRiskChart.resize();
        }
        if (videoMoveChart) {
            videoMoveChart.resize();
        }
        if (personMoveChart) {
            personMoveChart.resize();
        }
        if (deptMoveChart) {
            deptMoveChart.resize();
        }

    });
    $(window).resize(function () {
        if (potentRiskChart) {
            potentRiskChart.resize();
        }
        if (videoMoveChart) {
            videoMoveChart.resize();
        }
        if (personMoveChart) {
            personMoveChart.resize();
        }
        if (deptMoveChart) {
            deptMoveChart.resize();
        }

    });
    /*//人员流转跳转
    personMoveChart.on('click', function (params) {

        location.href = ctx + '/report/index?username=' + escape(params.name);
    });
    //部门流转跳转
    deptMoveChart.on('click', function (params) {

        location.href = ctx + '/report/index?deptid=' + escape(params.data.id)+'&deptname='+escape(params.data.name);
    });
    //视频流转
    videoMoveChart.on('click', function (params) {

        if(params.seriesName == "视频导出"){
            location.href = ctx + '/report/index?video=OPT'
        }else if(params.seriesName == "视频外发"){
            location.href = ctx + '/report/index?video=OUTCFG'
        }else{
            location.href = ctx + '/report/index'
        }
    });*/
}

// 获取所有的数据
function getAllData() {
    //用户总数
    var allConnt = 0;
    getAjax(ctx + '/clientUser/countAllClientUser', '', function (msg) {
        if (msg.resultCode == 0) {
            allConnt = msg.data.allConnt;
            //潜在风险统计
            getAjax(ctx + '/policy/countPolicyPotentialRisk', '', function (msg) {
                if (msg.resultCode == 0) {
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

                    gd.showError('潜在风险连接服务器失败');
                }
            });
        } else {
            gd.showError('用户总数连接服务器失败');
        }
    });

//视频流转


    //审批待审批
    getAjax(ctx + "/approveFlow/countApproveByState", {
        "status": 0
    }, function (msg) {
        if (msg.resultCode == 0) {
            $(".j-waiting").text(msg.data);
        } else {
            gd.showError('审批待审批连接服务器失败');
        }
    });
    //审批未通过
    getAjax(ctx + "/approveFlow/countApproveByState", {
        "status": -1
    }, function (msg) {
        if (msg.resultCode == 0) {
            $(".j-no").text(msg.data);
        } else {
            gd.showError('审批未通过连接服务器失败');
        }
    });
    //审批已通过
    getAjax(ctx + "/approveFlow/countApproveByState", {
        "status": 1
    }, function (msg) {
        if (msg.resultCode == 0) {
            $(".j-yes").text(msg.data);
        } else {
            gd.showError('审批已通过连接服务器失败');
        }
    });
    //审批已审批
    getAjax(ctx + "/approveFlow/countApproveByState", {
        "status": 2
    }, function (msg) {
        if (msg.resultCode == 0) {
            $(".j-all").text(msg.data);
        } else {
            gd.showError('审批已审批连接服务器失败');
        }
    });
    //部门视频流转分布
    getAjax(ctx + '/report/countVideoTransferByDepartment', '', function (msg) {

        if (msg.resultCode == 0) {
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
            gd.showError('部门视频流转分布连接服务器失败');
        }
    });

    //top5
    getAjax(ctx + '/report/countVideoTransferTop5', '', function (msg) {
        if (msg.resultCode == 0) {

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
            gd.showError('top5连接服务器失败');
        }
    });


}

//视频流转方法
function getRistInfo(day, isAll) {

    videoMoveChart.clear();
    getAjax(ctx + '/report/getVideoTransferLogInHours', {
        "departmentId": 1,
        "submitDate": day,
        "startDate": '',
        "endDate": '',
        "order": '',
        "logType": "OUTCFG"//外发
    }, function (msg) {
        if (msg.resultCode == 0) {
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
                            videoMoveOpt.legend.data = ['视频外发', '视频导出',"总计"];
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
                                videoMoveOpt.series[2].data = msgAll.data;
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
                                videoMoveOpt.series[2].data = msgAll.data.countList;
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
                                videoMoveOpt.series[2].data = msgAll.data.countList;
                                videoMoveOpt.xAxis[0].data = msgAll.data.dateList;
                            }

                            if (day == 'day') {
                                //暂时保留
                                if ((getArrayCount(msg.data) + getArrayCount(msgIn.data)) == 0) {
                                    $('.no-data').show();
                                    $('.video-move').hide();
                                    return;
                                }else {
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
                                }else {
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
                                }else {
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
                            gd.showError('视频流转获取图表数据失败');
                        }
                    });
                } else {
                    gd.showError('视频流转获取图表数据失败');
                }
            });
        } else {
            gd.showError('视频流转获取图表数据失败');
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

//潜在风险
function potentRiskInfo() {
    potentRiskChart = echarts.init(document.getElementById('potent_risk'));
    // potentRiskChart.setOption(potentRiskOpt);
}

potentRiskOpt = {
    series: [{
        type: 'liquidFill',
        data: [0.8],
        color: ['#38D1BF'],
        center: ['50%', '48%'],
        radius: '70%',
        amplitude: '8%',
        waveLength: '80%',
        phase: 'auto',
        period: 'auto',
        direction: 'right',
        shape: 'circle',

        waveAnimation: true,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear',
        animationDuration: 2000,
        animationDurationUpdate: 1000,

        outline: {
            show: true,
            borderDistance: 4,
            itemStyle: {
                color: 'none',
                borderColor: '#38D1BF',
                borderWidth: 1,
                shadowBlur: 0
            }
        },

        backgroundStyle: {
            color: '#fff'
        },

        itemStyle: {
            opacity: 0.95,
            shadowBlur: 50,
            shadowColor: 'rgba(0, 0, 0, 0.4)'
        },

        label: {
            show: true,
            color: '#38D1BF',
            insideColor: '#fff',
            fontSize: 34,
            // fontWeight: 'bold',
            align: 'center',
            baseline: 'middle',
            position: 'inside',
            normal: {
                formatter: function (param) {
                    return parseInt(param.value * 100);
                }
            }

        },
    }]
}

//视频流转
function videoMove() {
    videoMoveChart = echarts.init(document.getElementById('video_move'));

}

//视频流转
videoMoveOpt = {
    color: ['#FFBF2D'],
    /*title: {
        text: '视频流转',
        textStyle: {
            color: '#333',
            fontSize: 14,
            fontWeight: 100
        },
        left: 6,
        top: 8
    },*/
    tooltip: {
        trigger: 'axis',
        // formatter:'{a}: {c}条</br>{a1}: {c1}条</br>{b}'
    },
    legend: {
        orient: "horizontal",
        icon: 'rect',
        data: ['视频外发', '视频导出', '总计'],
        itemWidth: 12,
        itemHeight: 12,
        right: 260,
        top: 0,
        selectedMode: false
    },
    grid: {
        top: '80',
        left: '25',
        right: '42',
        bottom: '3%',
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
                margin: 10,
                textStyle: {
                    fontSize: 14,
                    color: "#999"
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
                    color: '#eee'
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
                    color: "#999"
                },
                /*formatter:function(text){
                    console.log(text)
                    return text+'/条'
                }*/
            }
        }
    ],
    series: [
        {
            name: '视频外发',
            type: 'bar',
            barWidth: "auto",
            barMinHeight:1,
            barGap:"10%",
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#19C0F3'},
                            {offset: 1, color: '#73E1F7'}
                        ]
                    )
                }
            },
            data: []
        },
        {
            name: '视频导出',
            type: 'bar',
            barWidth: "auto",
            barMinHeight:1,
            barGap:"10%",
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#38D1BF'},
                            {offset: 1, color: '#69EAE1'}
                        ]
                    )
                }
            },
            data: []
        },
        {
            name: '总计',
            type: 'line',
            smooth: true,
            data: []
        }
    ]
};

//部门流转
function deptMove() {
    deptMoveChart = echarts.init(document.getElementById('dept_video'));

}

deptMoveOpt = {
    color: ['#F9C83F', '#25ABE4', '#4DC1E0', '#3C96AD', '#45D7C8', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    legend: {
        type: "scroll",
        orient: 'vertical',
        right: '8%',
        y: 'center',
        itemWidth: 12,
        itemHeight: 12,
        itemGap: 20,
        textStyle: {
            fontSize: 14,
        },
        formatter: function (parse) {
            if (parse.toString().length > 5) {
                parse = parse.substring(0, 4) + '...';
            }
            return parse;
        },
        data: ["直接访问", "邮件营销"]
    },
    series: [
        {
            name: '访问来源',
            type: 'pie',
            center: ['38%', '50%'],
            radius: ['35%', '60%'],
            avoidLabelOverlap: false,
            label: {
                normal: {
                    show: false,
                    position: 'center'
                },

            },
            itemStyle: {
                normal: {
                    borderWidth: 9,
                },
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [
                {value: 335, name: '直接访问',id:3},
                {value: 310, name: '邮件营销',id:9}
            ]
        }
    ]
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
        left: '3%',
        right: '30',
        bottom: '5%',
        containLabel: true
    },
    yAxis: [{
        type: 'category',
        data: ['杨利伟', '孙杨', '孙浩'],
        nameGap: 40,
        axisTick: {
            alignWithLabel: true,
        },
        axisLabel: {
            margin: 15,
            textStyle: {
                fontSize: 12,
                color: '#000'
            },
            formatter: function (pamar) {
                if (pamar.length > 5) {
                    pamar = pamar.substring(0, 4) + '...';
                }
                return pamar;
            }
        },
        axisLine: {
            lineStyle: {
                color: '#dbe0e6'
            }
        },

    }],
    xAxis: [{
        type: 'value',

        min: null,
        // max:function(value){
        //     alert(value);
        //     if(value<3){
        //         return value+3;
        //     }else{
        //         return value;
        //     }
        //
        // },
        // scale:true,
        minInterval: 0,
        splitNumber: 5,

        axisLabel: {
            margin: 5,
            textStyle: {
                fontSize: 12,
                color: '#94999f'
            }
        },
        axisLine: {
            lineStyle: {
                color: '#fff'
            }
        },
        splitLine: {
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
                position: 'insideRight',
                offset: [0, -1],
                textStyle: {
                    color: 'white', //color of value
                    size: 16,
                }
            }
        },
        itemStyle: {
            normal: {

                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                    offset: 0,
                    color: '#38D1BF' // 0% 处的颜色
                }, {
                    offset: 1,
                    color: '#10B0E1' // 100% 处的颜色
                }], false),
                barBorderRadius: [0, 15, 15, 0],
                shadowColor: 'rgba(0,0,0,0.1)',
                shadowBlur: 3,
                shadowOffsetY: 3
            }
        }
    }]
};


