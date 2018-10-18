var currentVisitChart = '';//当前正在访问的人数量
var currentVisitOpt = '';//当前正在访问的人数量配置

var cpuChart = '';//cpu图表
var cpuOpt = '';//cpu图表配置

var mysqlChart = ''//mysql图表
var mysqlOpt = ''//mysql图表配置

var memoryChart = ''//内存图表
var memoryOpt = ''//内存图表配置

var vedioDataExportChart = ''//视频数据导出图表
var vedioDataExportOpt = ''//视频数据导出配置

var potentRiskChart = ''//潜在风险图表
var potentRiskOpt = ''//潜在风险图表配置

var dataExportChart = ''//数据导出统计
var dataExportOpt = ''//数据导出配置
var mysqlImg = new Image();
mysqlImg.src = '../skin/default/images/pattern3.png';

var cpuImg = new Image();
cpuImg.src = '../skin/default/images/pattern2.png';

var grayImg = new Image();
grayImg.src = '../skin/default/images/pattern1.png';

var memImg = new Image();
memImg.src = '../skin/default/images/pattern4.png';
$(function () {
    // getCurrentVisitNumber();//当前访问人数信息
    getServerInfo();//服务器信息
    vedioDataExport();//视频导出
    potentRiskInfo();//潜在风险
    dataExport();//数据导出统计
    getAllData();//获取用户数据先不用
    initEvents(); //初始化事件
});

/**
 * 初始化事件
 *
 */
function initEvents() {
    //窗口缩放调整图表
    $(window).resize(function () {
        if (currentVisitChart) {
            currentVisitChart.resize();
        }
        if (cpuChart) {
            cpuChart.resize();
        }
        if (mysqlChart) {
            mysqlChart.resize();
        }
        if (memoryChart) {
            memoryChart.resize();
        }
        if (vedioDataExportChart) {
            vedioDataExportChart.resize();
        }
        if (potentRiskChart) {
            potentRiskChart.resize();
        }
        if (dataExportChart) {
            dataExportChart.resize();
        }
    });

}

// 获取所有的数据
function getAllData() {
    var allConnt = 0;
    //在线数和正在访问数
    getAjax(ctx + '/clientUser/countAllClientUser', '', function (msg) {
        if (msg.resultCode == 1) {
            // currentVisitOpt.grid.top = 100;
            allConnt = msg.data.allConnt;
        } else {
            layer.msg('连接服务器失败', {icon: 2});
        }
    });
    //服务器cpu信息
    getAjax(ctx + '/systemSetting/getComputerInfo', '', function (msg) {
        if (msg.resultCode == 1) {
            // 内存
            var sqlTotal = msg.data.memory.totalMem;
            var sqlUsed = msg.data.memory.usedMem;
            var isUse = parseInt((sqlUsed / sqlTotal).toFixed(2) * 100);
            memoryOpt.series[0].data[0].value = isUse;
            memoryOpt.series[0].data[1].value = 100-isUse;
            memoryOpt.title.text=isUse+'%';
            memoryChart.setOption(memoryOpt);
            // mysql
            var myIsUse = parseInt([Math.round(msg.data.mySQLUsage)]);
            mysqlOpt.series[0].data[0].value = myIsUse;
            mysqlOpt.series[0].data[1].value= 100-myIsUse;
            mysqlOpt.title.text=myIsUse+'%';
            mysqlChart.setOption(mysqlOpt);
            //cpu
            var cpu = parseInt(Number(Math.round(((msg.data.cpu).toFixed(2)) * 100)));
            cpuOpt.series[0].data[0].value = cpu;
            cpuOpt.series[0].data[1].value = 100 - cpu;
            cpuOpt.title.text=cpu+'%';
            cpuChart.setOption(cpuOpt);
        } else {
            layer.msg('连接服务器失败', {icon: 2});
        }
    });
    //潜在风险统计
    getAjax(ctx + '/policy/countPolicyPotentialRisk', '', function (msg) {
        if (msg.resultCode == 1) {
            potentRiskOpt.title.subtext = allConnt;
            for (var i = 1; i < 4; i++) {
                potentRiskOpt.series[0].data[i - 1].value = msg.data[i];
            }
            potentRiskChart.setOption(potentRiskOpt);
        } else {
            layer.msg('连接服务器失败', {icon: 2});
        }
    });
    //top10
    getAjax(ctx + '/report/countVideoTransferTop10', '', function (msg) {
        if (msg.resultCode == 1) {

            if (msg.data.length == 0) {
                vedioDataExportOpt.yAxis[0].data = '据数无';
                vedioDataExportOpt.series[0].data = 0;
                $('#vedio_export').addClass('vedio_export_empty')
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
                vedioDataExportOpt.yAxis[0].data = hunman;
                vedioDataExportOpt.series[0].data = count;
                vedioDataExportChart.setOption(vedioDataExportOpt);
            }


        } else {
            layer.msg('连接服务器失败', {icon: 2});
        }
    });
    //数据导出统计
    getAjax(ctx + '/report/getVideoTransferLogInWeek', '', function (msg) {
        if (msg.resultCode == 1) {
            dataExportOpt.xAxis.data = msg.data.dateList;
            dataExportOpt.series.data = msg.data.countList;
            dataExportChart.setOption(dataExportOpt);
        } else {
            layer.msg('连接服务器失败', {icon: 2});
        }
    });


}

// 获取正在访问人数的信息
function getCurrentVisitNumber() {
    currentVisitChart = echarts.init(document.getElementById('curr_Visit_Chart'));
}

// 系统CPU内存等信息
function getServerInfo() {
    cpuChart = echarts.init(document.getElementById('serverinfo_cpu_chart'));
    mysqlChart = echarts.init(document.getElementById('serverinfo_mysql_chart'));
    memoryChart = echarts.init(document.getElementById('serverinfo_mem_chart'));
}

// 视频数据导出top10
function vedioDataExport() {
    vedioDataExportChart = echarts.init(document.getElementById('vedio_export'));
}

//潜在风险
function potentRiskInfo() {
    potentRiskChart = echarts.init(document.getElementById('potent_risk'));
}

//数据导出
function dataExport() {
    dataExportChart = echarts.init(document.getElementById('data_export'));
}

//当前正在访问的人数量配置
/*currentVisitOpt = {
    // tooltip: {
    //   formatter: "{a} <br/>{b} : {c}%"//饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
    // },
    grid: {
        // left: 0,
        y: 150,
        // right: 0,
        // bottom: 0
        y2: 0,
    },
    series: [{
        name: "当前访问的人数",
        type: "gauge",
        radius: "95%",
        startAngle: 200,
        endAngle: -20,
        splitNumber: 50,
        axisLine: {//仪表盘轴线相关配置
            "lineStyle": {
                "color": [[0.05, new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                    offset: 0,
                    color: '#38d1bf'
                }, {
                    offset: 1,
                    color: '#D3F546'
                }])], [1, "#cacaca"]],
                "width": 35,
            },

        },
        splitLine: {
            "show": false
        },
        axisTick: {//刻度样式。
            lineStyle: {
                color: '#fff',
                width: 2
            },
            length: 35,
            splitNumber: 1
        },
        pointer: {
            show: false
        },
        axisLabel: {//刻度标签
            show: false,
        },
        detail: {
            formatter: "{value}%",
            offsetCenter: [0, -10],
            textStyle: {
                fontSize: 32,
                fontWeight: 'bold',
                color: "#38d1bf"
            }
        },
        title: {
            offsetCenter: [0, "60%"]
        },
        data: [{
            name: "",
            value: 0
        }]
    }]
};*/
//cpu信息
cpuOpt = {
    title: {
        text: '58%',
        top: 'middle',
        left: '38',
        textStyle: {
            fontSize: 18,
            fontWeight: "700",
            color: "#333333"
        }
    },
    // color: ['#49BCAF', '#f2f2f2'],
    series: [
        {
            type: 'pie',
            silent: true,
            hoverAnimation: false,
            legendHoverLink: false,
            radius: [32, 47],
            center: ['30%', '50%'],
            x: '0%',
            label: '',
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 90,
                startAngle: 270,
                /*label: {
                    "normal": {
                        "show": true,
                        "formatter": "{d}%",
                        "textStyle": {
                            "color": "#666666",
                            "fontSize": 18,
                            "fontWeight": "700"
                        },
                        "position": "center"
                    }
                },*/
                labelLine: {
                    show: false
                },
                itemStyle: {
                    normal: {
                        type: 'linear',

                    }
                },
            },
                {
                    value: 95,
                    labelLine: {
                        show: false
                    },
                    itemStyle: {
                        normal: {
                            color: "#f2f2f2"
                        }
                    },
                }
            ],
            color: [new echarts.graphic.LinearGradient(0, 1, 1, 1, [{
                offset: 0,
                color: '#83c881'
            }, {
                offset: 1,
                color: '#56cbd3'
            }])]
        }
    ]
}
/*cpuOpt = {
    tooltip: {
        trigger: 'item',
        formatter: "{a}：{c}%"
    },
    grid: {
        left: 0,
        top: 0,
        right: 0,
        bottom: 0
    },
    xAxis: [{
        type: 'value',
        show: false,
    }],
    yAxis: [{
        type: 'category',
        show: false,
    }],
    series: [{
        type: 'bar',
        barWidth: 10,
        barGap: '-100%',
        silent: true,
        itemStyle: {
            normal: {
                color: {
                    image: grayImg,
                    repeat: 'repeat'
                }
            }
        },
        data: [100],
    }, {
        name: 'CPU使用率',
        type: 'bar',
        barWidth: 10,
        itemStyle: {
            normal: {
                color: {
                    image: cpuImg,
                    repeat: 'repeat'
                }
            }
        },
        data: []
    }
    ]
}*/
//mysql信息
mysqlOpt = {
    title: {
        text: '58%',
        top: 'middle',
        left: '38',
        textStyle: {
            fontSize: 18,
            fontWeight: "700",
            color: "#333333"
        }
    },
    // color: ['#49BCAF', '#f2f2f2'],
    series: [
        {
            type: 'pie',
            silent: true,
            hoverAnimation: false,
            legendHoverLink: false,
            radius: [32, 47],
            center: ['30%', '50%'],
            x: '0%',
            label: '',
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 90,
                startAngle: 270,
                /*label: {
                    "normal": {
                        "show": true,
                        "formatter": "{d}%",
                        "textStyle": {
                            "color": "#666666",
                            "fontSize": 18,
                            "fontWeight": "700"
                        },
                        "position": "center"
                    }
                },*/
                labelLine: {
                    show: false
                },
                itemStyle: {
                    normal: {
                        type: 'linear',

                    }
                },
            },
                {
                    value: 96,
                    labelLine: {
                        show: false
                    },
                    itemStyle: {
                        normal: {
                            color: "#f2f2f2"
                        }
                    },
                }
            ],
            color: [new echarts.graphic.LinearGradient(0, 1, 1, 1, [{
                offset: 0,
                color: '#83c881'
            }, {
                offset: 1,
                color: '#56cbd3'
            }])]
        }
    ]
}
/*mysqlOpt = {
    tooltip: {
        trigger: 'item',
        formatter: "{a}：{c}%"
    },
    grid: {
        left: 0,
        top: 0,
        right: 0,
        bottom: 0
    },
    xAxis: [{
        type: 'value',
        show: false,
    }],
    yAxis: [{
        type: 'category',
        show: false,
    }],
    series: [{
        type: 'bar',
        barWidth: 10,
        barGap: '-100%',
        silent: true,
        itemStyle: {
            normal: {
                color: {
                    image: grayImg,
                    repeat: 'repeat'
                }
            }
        },
        data: [100],
    }, {
        name: 'MYSQL使用率',
        type: 'bar',
        barWidth: 10,
        itemStyle: {
            normal: {
                color: {
                    image: mysqlImg,
                    repeat: 'repeat'
                }
            }
        },
        data: [10]
    }
    ]
}*/
//内存信息
memoryOpt = {
    title: {
        text: '58%',
        top: 'middle',
        left: '38',
        textStyle: {
            fontSize: 18,
            fontWeight: "700",
            color: "#333333"
        }
    },
    // color: ['#49BCAF', '#f2f2f2'],
    series: [
        {
            type: 'pie',
            silent: true,
            hoverAnimation: false,
            legendHoverLink: false,
            radius: [32, 47],
            center: ['30%', '50%'],
            x: '0%',
            label: '',
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 90,
                startAngle: 270,
                /*label: {
                    "normal": {
                        "show": true,
                        "formatter": "{d}%",
                        "textStyle": {
                            "color": "#666666",
                            "fontSize": 18,
                            "fontWeight": "700"
                        },
                        "position": "center"
                    }
                },*/
                labelLine: {
                    show: false
                },
                itemStyle: {
                    normal: {
                        type: 'linear',

                    }
                },
            },
                {
                    value: 55,
                    labelLine: {
                        show: false
                    },
                    itemStyle: {
                        normal: {
                            color: "#f2f2f2"
                        }
                    },
                }
            ],
            color: [new echarts.graphic.LinearGradient(0, 1, 1, 1, [{
                offset: 0,
                color: '#83c881'
            }, {
                offset: 1,
                color: '#56cbd3'
            }])]
        }
    ]
}
/*memoryOpt = {
    tooltip: {
        trigger: 'item',
        formatter: "{a}：{c}%"
    },
    grid: {
        left: 0,
        top: 0,
        right: 0,
        bottom: 0
    },
    xAxis: [{
        type: 'value',
        show: false,
    }],
    yAxis: [{
        type: 'category',
        show: false,
    }],
    series: [{
        type: 'bar',
        barWidth: 10,
        barGap: '-100%',
        silent: true,
        itemStyle: {
            normal: {
                color: {
                    image: grayImg,
                    repeat: 'repeat'
                }
            }
        },
        data: [100],
    }, {
        name: '内存使用率',
        type: 'bar',
        barWidth: 10,
        itemStyle: {
            normal: {
                color: {
                    image: memImg,
                    repeat: 'repeat'
                }
            }
        },
        data: []
    }
    ]
}*/
// 视频数据导出信息top10
vedioDataExportOpt = {
    //color: ['#3398DB'],
    tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(0,0,0,0.6)',
        extraCssText: 'box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);',
        textStyle: {
            color: '#F0F0F0',
        },

    },
    grid: {
        top: '0%',
        left: '3%',
        right: '4%',
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
        // min: 0,
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
        // minInterval: 1,
        // splitNumber:2,
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
        name: '导出数量',
        type: 'bar',
        barWidth: 15,
        data: [20, 30, 40],
        label: {
            normal: {
                show: true,
                position: 'insideRight',
                offset: [0, -3],
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
                    color: '#64bdc8' // 0% 处的颜色
                }, {
                    offset: 1,
                    color: '#00c484' // 100% 处的颜色
                }], false),
                barBorderRadius: [0, 15, 15, 0],
                shadowColor: 'rgba(0,0,0,0.1)',
                shadowBlur: 3,
                shadowOffsetY: 3
            }
        }
    }]
};
// 潜在风险配置
potentRiskOpt = {
    title: {
        text: "用户总数",
        textStyle: {
            fontSize: 14,
            color: "#9C9C9C"
        },
        subtext: "50",
        subtextStyle: {
            fontWeight: "bold",
            fontSize: 22,
            color: "#6BCFA1"
        },
        left: "center",
        top: "35%"
    },
    color: ['#B9E9D3', '#50C892', '#43A076', '#48ecf6', '#ffcd5a', '#16c092'],
    // 饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
    tooltip: {
        trigger: 'item',
        formatter: "{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'horizontal',
        bottom: '40px',
        itemGap: 10,
        itemWidth: 10,
        itemHeight: 10,
        data: ['屏幕无水印', '外发无水印', '导出无水印']
    },
    series: [
        {
            name: '',
            type: 'pie',
            radius: ['28%', '40%'],
            avoidLabelOverlap: true,
            center: ['50%', '40%'],
            hoverOffset: 80,
            data: [
                {value: 20, name: '屏幕无水印'},
                {value: 20, name: '外发无水印'},
                {value: 20, name: '导出无水印'},
                // {value: 20, name: '视频流转无审计'},
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
            labelLine: {
                normal: {
                    show: true,
                    length2: 10,
                    lineStyle: {
                        color: "#ededed",
                    }
                },
                emphasis: {
                    lineStyle: {
                        color: "#ededed",
                    }
                }
            },
            label: {
                normal: {
                    textStyle: {
                        color: '#000',
                        fontSize: 14
                    },
                    // "{c} \n {b}"
                    formatter: function (params) {
                        // console.log(params)
                        // return '<div style="font-size: 20px">'+params.data.value+'</div><div>'+params.data.name+'</div>';

                    }
                }
            }
        }
    ]
};

//数据导出统计
dataExportOpt = {
    title: {
        show: true,
        text: '数据导出统计',
        textStyle: {
            fontSize: 14,
            fontFamily: "Microsoft YaHei",
            fontWeight: 100,
            verticalAlign: "middle",
            lineHeight: 40,
            height: 40

        },
        padding: [
            15,  // 上
            0, // 右
            0,  // 下
            10 // 左
        ]
    },
    tooltip: {
        trigger: 'axis'
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
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
            lineStyle: {
                color: '#ccc'
            }
        },
        axisLabel: {
            margin: 10,
            textStyle: {
                fontSize: 14,
                color: "#666"
            }
        },
        data: ['2017-07-16', '2017-07-16', '2017-07-16', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15', '2017-07-15']
    },
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
                    color: '#ccc'
                }
            },
            axisLabel: {
                textStyle: {
                    color: "#666"
                }
            }
        }
    ],
    grid: {
        top: "70px",
        left: "80px",
        right: "80px",
        bottom: "12%"
    },
    visualMap: {
        top: 20,
        right: 5,
        orient: 'horizontal',
        pieces: [{
            gt: 0,
            lte: 100,
            color: '#68D7E5'
        }, {
            gt: 101,
            lte: 200,
            color: '#50C892'
        }, {
            gt: 201,
            lte: 300,
            color: '#FBAA59'
        }, {
            gt: 301,
            color: '#CC0A39'
        }],
        outOfRange: {
            color: '#68D7E5'
        }
    },
    series: {
        name: '视频导出次数',
        type: 'line',
        smooth: true,
        showSymbol: true,
        data: [10, 100, 30, 150, 250, 350, 295, 349, 1, 22, 33, 444, 55, 233, 432, 22, 11],
        /*areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: 'rgba(24,187,221,0.2)'
                }, {
                    offset: 1,
                    color: 'rgba(24,187,221,0)'
                }], false)
            }
        },*/
        itemStyle: {
            normal: {
                color: '#000000',
                // borderWidth:20
                opacity: 1
            }
        }
    }
};