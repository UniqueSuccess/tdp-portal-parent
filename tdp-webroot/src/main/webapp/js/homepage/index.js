var homepage = new Vue({
    el: "#homepage",
    data: {}
});


var potentRiskChart = ''//潜在风险图表
var potentRiskOpt = ''//潜在风险图表配置


var videoMoveChart = '';//文件流转
var videoMoveOpt = '';//文件流转配置
var submitDate = 'day';//时间
var startDate = "";
var endDate = "";
var reportData = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];
var dataX = ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];//图表数组


var personMoveChart = '';//人员文件流转
var personMoveOpt = '';//人员文件流转配置

var deptMoveChart = '';//部门文件流转分布
var deptMoveOpt = '';//部门文件流转分布


$(function () {
    //潜在风险
    potentRiskInfo();
    //文件流转
    videoMove();
    getRistInfo("day", "noall");
    //部门流转
    deptMove();
    //人员文件流转top10
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
    //文件流转图表切换
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
    //文件流转
    videoMoveChart.on('click', function (params) {

        if(params.seriesName == "文件导出"){
            location.href = ctx + '/report/index?video=OPT'
        }else if(params.seriesName == "文件外发"){
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
    //部门文件流转分布
    getAjax(ctx + '/report/countVideoTransferByDepartment', '', function (msg) {
        if (msg.resultCode == 0) {
            var legend = new Array();
            var tempData = new Array();
            for (var k = 0; k < msg.data.length - 1; k++) {//外层循环控制排序趟数
                for (var g = 0; g < msg.data.length - 1 - k; g++) {//内层循环控制每一趟排序多少次
                    if (msg.data[g].count < msg.data[g + 1].count) {
                        var temp = msg.data[g];
                        msg.data[g] = msg.data[g + 1];
                        msg.data[g + 1] = temp;
                    }
                }
            }
            for (var i = 0; i < 5; i++) {
                var name = null;
                var count = null;
                name = msg.data[i].name;
                count = msg.data[i].count;
                var deptData = new Object();
                if (name.toString().length > 8) {
                    name = name.substring(0, 7) + "... " + count;
                } else {
                    for (var j = 0; j < (8 - (msg.data[i].name.toString().length)); j++) {
                        name += "　";
                    }
                    name += count;
                }

                legend.push(name);
                deptData.value = count;
                deptData.name = name;
                deptData.id = msg.data[i].id;
                tempData.push(deptData);
            }
            if (getObjArrayCount(tempData) == 0) {
                $('#dept_video').addClass('vedio_export_empty');
                return;
            }
            deptMoveOpt.series[1].data[0].label.normal.formatter = getObjArrayCount(tempData).toString();
            deptMoveOpt.series[0].data = tempData;
            deptMoveOpt.legend.data = legend;
            deptMoveChart.setOption(deptMoveOpt);
        } else {
            gd.showError('部门文件流转分布连接服务器失败');
        }
    });

    //文件流转类型
    getAjax(ctx + "/report/getFileTypeCount", '', function (msg) {
        if (msg.resultCode == 0) {
            var fileType = ['WORD文档', 'PDF文档', '演示文稿', '电子表格']
            var legend = new Array();
            var tempData = new Array();
            var doc = msg.data.doc;
            var pdf = msg.data.pdf;
            var ppt = msg.data.ppt;
            var xls = msg.data.xls;
            legend.push(doc, pdf, ppt, xls);
            var legendMax = Math.max.apply(Math, legend);
            for (var i = 0; i < legend.length; i++) {
                tempData.push(legendMax * 2);
            }
            sort(legend, fileType);
            personMoveOpt.yAxis[0].data = fileType;
            personMoveOpt.series[0].data = legend;
            personMoveOpt.series[1].data = tempData;
            personMoveOpt.series[1].label.normal.formatter = function (params) {
                return legend[params.dataIndex] === 0 ? '-' : legend[params.dataIndex];
            };
            if (getArrayCount(legend) == 0) {
                $('#file_type').addClass('vedio_export_empty');
                return;
            }

            personMoveChart.setOption(personMoveOpt);
        } else {
            gd.showError('文件类型获取连接服务器失败');
        }
    });
}

function sort(arr, arr2) {
    for (var j = 0; j < arr.length - 1; j++) {
        //两两比较，如果前一个比后一个大，则交换位置。
        for (var i = 0; i < arr.length - 1 - j; i++) {
            if (arr[i] > arr[i + 1]) {
                var temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                var temp2 = arr2[i];
                arr2[i] = arr2[i + 1];
                arr2[i + 1] = temp2;

            }
        }
    }
}

function strlen(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
    }
    return len;
}

//文件流转方法
function getRistInfo(day, isAll) {

    videoMoveChart.clear();
    getAjax(ctx + '/report/getFileTransferLogInHours', {
        "departmentId": 1,
        "submitDate": day,
        "startDate": '',
        "endDate": '',
        "order": '',
        "logType": "OUTCFG"//外发
    }, function (msg) {
        if (msg.resultCode == 0) {
            getAjax(ctx + '/report/getFileTransferLogInHours', {
                "departmentId": 1,
                "submitDate": day,
                "startDate": '',
                "endDate": '',
                "order": '',
                "logType": "OPT"//导出
            }, function (msgIn) {
                if (msgIn.resultCode == 0) {
                    getAjax(ctx + '/report/getFileTransferLogInHours', {
                        "departmentId": 1,
                        "submitDate": day,
                        "startDate": '',
                        "endDate": '',
                        "order": '',
                        "logType": ''
                    }, function (msgAll) {
                        if (msgAll.resultCode == 0) {
                            videoMoveOpt.legend.data = ['外发', '导出'];
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
                            gd.showError('文件流转获取图表数据失败');
                        }
                    });
                } else {
                    gd.showError('文件流转获取图表数据失败');
                }
            });
        } else {
            gd.showError('文件流转获取图表数据失败');
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
        color: ['#039BE5'],
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
                borderColor: '#039BE5',
                borderWidth: 1,
                shadowBlur: 0
            }
        },

        backgroundStyle: {
            color: '#fff'
        },

        itemStyle: {
            opacity: 0.95,
            shadowBlur: 0,
            shadowColor: 'rgba(0, 0, 0, 0.4)'
        },

        label: {
            show: true,
            color: '#039BE5',
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

//文件流转
function videoMove() {
    videoMoveChart = echarts.init(document.getElementById('video_move'));

}

//文件流转
videoMoveOpt = {
    color: ['#FFBF2D'],
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        formatter: function (params) {
            var html = '';
            html += "<div style='display: inline-block;margin-right: 10px'>" + params[0].axisValueLabel + "</div>" + "<span style='font-size: 18px'>" + (params[0].data + params[1].data) + "</span>条</br>";
            html += "<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#37AAE3;\"></span>" + params[0].seriesName + "&nbsp;&nbsp;&nbsp;" + params[0].data + "</br>";
            html += "<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#6DCCF8;\"></span>" + params[1].seriesName + "&nbsp;&nbsp;&nbsp;" + params[1].data + "";
            return html;
        }
    },
    legend: {
        orient: "horizontal",
        data: ['外发', '导出'],
        itemWidth: 20,
        itemHeight: 12,
        right: 268,
        top: "-5",
        selectedMode: false,
        icon: 'circle',
        itemGap: 20,
        width: 200
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
                }
            }
        }
    ],
    series: [
        {
            name: '外发',
            type: 'bar',
            stack: "流转",
            barWidth: "auto",
            barMinHeight: 1,
            barGap: "10%",
            itemStyle: {
                normal: {
                    /*color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#37AAE3'},
                            {offset: 1, color: '#37AAE3'}
                        ]
                    )*/
                    color: "#37AAE3"
                }
            },
            data: []
        },
        {
            name: '导出',
            type: 'bar',
            stack: "流转",
            barWidth: "auto",
            barMinHeight: 1,
            barGap: "10%",
            itemStyle: {
                normal: {
                    /*color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#6DCCF8'},
                            {offset: 1, color: '#6DCCF8'}
                        ]
                    )*/
                    color: "#6DCCF8"
                }
            },
            data: []
        },
        /*{
            name: '总计',
            type: 'line',
            smooth: true,
            data: []
        }*/
    ]
};

//部门流转
function deptMove() {
    deptMoveChart = echarts.init(document.getElementById('dept_video'));

}

deptMoveOpt = {
    color: ['#BAE7FC', '#8DD8FA', '#3EBDF6', '#1BA4E7', '#1A84C3'],
    /*title:{
        text:158,
        subtext:"总数",
        x: '35%',
        y: 'center',
        textAlign: 'center',
        textStyle: {
            color: "#000",
            textAlign: 'center',
            fontSize: 38 * 0.5,
            fontWeight: 'bold'
        },
    },*/
    tooltip: {
        trigger: 'item',
        formatter: "{b} ({d}%)"
    },
    legend: {
        type: "scroll",
        orient: 'vertical',
        right: '8%',
        y: 'center',
        icon: 'circle',
        itemWidth: 12,
        itemHeight: 12,
        itemGap: 20,
        textStyle: {
            fontSize: 14
        },
        formatter: '{name}',
        data: ["直接访问", "邮件营销"]
    },
    series: [
        {
            name: '访问来源',
            type: 'pie',
            center: ['35%', '50%'],
            radius: ['38%', '50%'],
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
                {value: 335, name: '直接访问', id: 3},
                {value: 310, name: '邮件营销', id: 9}
            ]
        },
        {
            name: '内圈',
            type: 'pie',
            center: ['35%', '50%'],
            radius: [0, '38%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            cursor: "default",
            silent: true,
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: 0,
                itemStyle: {
                    normal: {
                        color: '#fff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        formatter: "182",
                        textStyle: {
                            color: '#232D3B',
                            fontSize: 28,
                            // fontWeight: 'bold'
                        }
                    }
                }
            }]
        }
    ]
};


//文件流转类型
function personMove() {
    personMoveChart = echarts.init(document.getElementById('file_type'));

}

// 文件流转类型
personMoveOpt = {
    tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(0,0,0,0.6)',
        // extraCssText: 'box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);',
        textStyle: {
            color: '#F0F0F0',
        },
        formatter: '{b0}: {c0}条',
        axisPointer: {
            lineStyle: {
                width: 0,
            }
        }

    },
    grid: {
        top: '0%',
        left: '5%',
        right: '8%',
        bottom: '5%',
        containLabel: true
    },
    yAxis: [
        {
            type: 'category',
            show: true,
            data: ['WORD文档', 'PDF文档', '演示文稿', '电子表格'],
            axisTick: {
                show: false
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#fff',
                }
            },
            axisLabel: {
                show: true,
                inside: false,
                textStyle: {
                    color: '#666666',
                    fontWeight: 'normal',
                    fontSize: '16',
                }
            }

        },
        /*{
            type: 'category',
            axisTick: {
                show: false
            },
            splitLine: {
                show: false,
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#000'
                }
            },
            axisLabel: {
                show: false,
                color: '#666',
                interval: '0',
                fontSize: '18',
            },
            position: 'right',
            offset: 0,
            data: [20, 30, 40, 50],
            zLevel: '3'
        }*/
    ],
    xAxis: [{
        type: 'value',
        // show: false,
        axisTick: {
            show: false
        },
        axisLine: {
            show: false,
            lineStyle: {
                color: '#2f3640',
            }
        },
        splitLine: {
            show: false,
            lineStyle: {
                color: '#2f3640 ',
            }
        },
        axisLabel: {
            show: false,
            inside: false,
            textStyle: {
                color: '#666666',
                fontWeight: 'normal',
                fontSize: '16',
            }
        },
        data: ['WORD文档', 'PDF文档', '演示文稿', '电子表格'],
    }],
    series: [
        {
            name: '',
            type: 'bar',
            barWidth: "20%",
            data: [20, 30, 50, 40],
            label: {
                normal: {
                    color: '#666666',
                    show: false,
                    // position: 'center',
                    position: ["50%", '-130%'],
                    textStyle: {
                        fontSize: 14,
                        fontWeight: 'bold',
                        fontFamily: 'PingFangSC'
                    },
                    formatter: function (a, b) {

                        return a.name;
                    }
                }
            },
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                        offset: 0,
                        color: '#81D4FA' // 0% 处的颜色
                    }, {
                        offset: 1,
                        color: '#039BE5' // 100% 处的颜色
                    }], false),
                    barBorderRadius: 20,
                    shadowColor: 'rgba(0,0,0,0.1)',
                    shadowBlur: 3,
                    shadowOffsetY: 3
                }
            }
        },
        {
            show: true,
            type: 'bar',
            barGap: '-100%',
            barWidth: '20%', //统计条宽度
            itemStyle: {
                normal: {
                    barBorderRadius: 20,
                    color: '#EBF0F2'
                },
                emphasis: {
                    barBorderRadius: 20,
                    color: '#EBF0F2'
                }
            },
            label: {
                normal: {
                    show: true,
                    position: 'right',
                    color: '#666',
                    interval: '0',
                    fontSize: '18',
                    formatter: function (params) {
                        return 0;
                        // return data1[params.dataIndex]===0?'-':data1[params.dataIndex];
                    }
                }
            },
            z: -12,
            data: [50, 50, 50, 50],
        },
    ]
};


