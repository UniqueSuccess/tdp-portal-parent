/**
 * Created by chengl on 2018/1/4 0004.
 */
var exportReportChart = '';//数据导出报表
var exportReportOpt = '';//当前正在访问的人数量配置
var reportTable = null;//表格
var departmentTree = null;//部门树
var zNodes = null;//部门数据
var departmentId = 1;//全局部门id
var submitDate = 'week';//时间
var startDate = "";
var endDate = "";
var order = $('#bar_searchstr').val().trim();//关键字
var logType = "";
var reportData = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];
var dataX = ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];//图表数组
var nodeType="childNode";
$(function () {

    getExportReport();//当前访问人数信息
    initreportTable();
    getDepartmentData();
    initEvents(); //初始化事件
    //getUserData();//获取用户数据
    otherEvent();//零散事件
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.dropdown-tree-box').hasClass('dropdown-tree-box')) {
            $('#department_tree').hide();
        }
    });
    var username = query("username");
    var video = query("video");
    if(username!=''){
        $('#bar_searchstr').val(username);
        $(".inputtime").val("全部");
        submitDate = 'all';
        refreshReportTable();
    }
    if(video!=''){
        $('#alltype').val(video);
        logType = video;
        refreshReportTable();
    }
    $("body #menus").resize(function () {
        reportTable.ajax.reload();
    });
    $("#bar_searchstr").focus();
});

/**
 * 初始化事件
 *
 */
function initEvents() {
    $('body')
    //模态框点击消失
        .on('click', '.magnify-modal', function (e) {
            e.stopPropagation();
            if ($('.magnify-button-close').length > 0) {
                $('.magnify-button-close').click();
            }
        })
        //显示部门树
        .on('click', '.dropdown-input', function () {
            $(this).closest('.dropdown-tree-box').find('.dropdown-tree').toggle();
        })
        //回车搜索
        .on('keydown', '#bar_searchstr', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon').click();
            }
        })
        //点击搜索
        .on('click', '#bar_searchstr_icon', function () {
            if ($(".inputtime").val() == "今天") {
                submitDate = 'day'
            } else if ($(".inputtime").val() == "最近7天") {
                submitDate = 'week'
            }else if ($(".inputtime").val() == "全部") {
                submitDate = 'all'
            } else {
                submitDate = '';
            }
            order = $('#bar_searchstr').val().trim();
            refreshReportTable();

            //getUserData();
        })
        // 时间控件
        .on('click', '.datatimechoose', function () {
            $(".datachange").slideToggle("fast");
        })
        .on('click', '.datachange li:not(:last-child)', function () {
            var typedata = $(this).attr("data-type");
            if (typedata == "day") {
                $(".inputtime").val("今天");
                submitDate = "day";
            } else if (typedata == "week") {
                $(".inputtime").val("最近7天");
                submitDate = "week";
            }else if (typedata == "all") {
                $(".inputtime").val("全部");
                submitDate = "all";
            }
            startDate = "";
            endDate = "";
            //getUserData();
            refreshReportTable();
        })
        .on('change', '#alltype',function(){
            nodeType = $(this).find('option:selected').val();
            refreshReportTable();
        })
        .on('click','#bar_pull_log',function(){
            var index = layer.open({
                type: 3,
                 //数组第二项即吸附元素选择器或者DOM
            });
            postAjax(ctx + '/report/collectVideoTransferLogsWithAttachment', {"start":"1970-01-01 00:00:00","end":new Date().Format("yyyy-MM-dd HH:mm:ss")}, function (msg) {
                if (msg.resultCode == 1) {
                    reportTable.ajax.reload();
                    layer.close(index);
                    layer.msg('获取成功！', {icon: 1});

                }
                else {
                    layer.close(index);
                    layer.msg('获取下级日志失败！' + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.datatimechoose').hasClass('datatimechoose')) {
            $('.datachange').hide();
        }
    });
    /**
     * 初始化时间控件
     * @return {[type]} [description]
     */
    $('#reservation').daterangepicker({
        "locale": {
            format: 'YYYY-MM-DD',
            separator: ' ~ ',
            applyLabel: "确定",
            cancelLabel: "取消",
        }

    }, function (start, end, label) {

        var starttime = $("input#starttime").val();
        var endtime = $("input#endtime").val();
        var alltime = starttime + " -- " + endtime;
        $(".inputtime").val(alltime);
        submitDate = "";
        startDate = starttime;
        endDate = endtime;
        //getUserData();
        refreshReportTable();
    });
    //窗口缩放调整图表
    $(window).resize(function () {
        if (exportReportChart) {
            exportReportChart.resize();
        }
        refreshReportTable();
    });


}
function otherEvent(){
    getAjax(ctx + '/childNode/list', '', function (msg) {
        if (msg.resultCode == 1) {
            $("body #alltype").html(template("childNode",msg.data));
        }
    });
}
/**
 * 表格数据刷新
 */
function refreshReportTable() {
    reportTable.settings()[0].ajax.data.departmentId = departmentId;//部门id
    reportTable.settings()[0].ajax.data.submitDate = submitDate;//时间
    reportTable.settings()[0].ajax.data.startDate = startDate;//时间
    reportTable.settings()[0].ajax.data.endDate = endDate;//时间
    reportTable.settings()[0].ajax.data.order = $('#bar_searchstr').val().trim();//关键字
    reportTable.settings()[0].ajax.data.logType = logType;//关键字
    reportTable.settings()[0].ajax.data.nodeType = nodeType;//关键字
    reportTable.ajax.reload();
}

/**
 * 部门树
 */
function getDepartmentData() {
    getAjax(ctx + '/department/getDepartmentNodesByLoginUser', '', function (msg) {
        zNodes = JSON.parse(msg);
        var setting = {
            view: {
                dblClickExpand: false,
                showLine: true,
                showIcon: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: function (event, treeId, treeNode, clickFlag) {
                    $(event.target).closest('.ztree').hide();
                    $(event.target).closest('.dropdown-tree-box').find('.dropdown-input').val(treeNode.name).attr('data-id', treeNode.id);
                    departmentId = treeNode.id;
                    if ($(".inputtime").val() == "今天") {
                        submitDate = 'day'
                    } else if ($(".inputtime").val() == "最近7天") {
                        submitDate = 'week'
                    }else if ($(".inputtime").val() == "全部") {
                        submitDate = 'all'
                    } else {
                        submitDate = '';
                    }
                    refreshReportTable();
                    //getUserData();
                }
            }
        };
        //部门树
        departmentTree = $.fn.zTree.init($('#department_tree'), setting, zNodes);
        var nodes = departmentTree.getNodes();
        var firstNode = nodes[0];
        $("#bar_dept").val(firstNode.name);
        if (zNodes.length > 0) {
            var node = departmentTree.getNodeByParam('id', 1);
            if (node) {
                departmentTree.selectNode(node);
            }
        }
        var deptid = query("deptid");
        // var deptname = query("deptname");
        if(deptid!=''){
            $(".inputtime").val("全部");
            $('#' + departmentTree.getNodeByParam('id', deptid).tId + '_a').click();
        }

    })
}

//获取用户数据
function getUserData() {
    // 总览趋势图表
    getAjax(ctx + '/report/getVideoTransferLogInHours', {
        "departmentId": departmentId,
        "submitDate": submitDate,
        "startDate": startDate,
        "endDate": endDate,
        "order": $('#bar_searchstr').val().trim()
    }, function (msg) {
        if (msg.resultCode == 1) {
            if (submitDate == 'day') {
                reportData = msg.data;
                dataX = ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"];
            } else {
                reportData = msg.data.countList;
                dataX = msg.data.dateList;
            }
            exportReportOpt.xAxis[0].data = dataX;
            exportReportOpt.series[0].data = reportData;
            exportReportChart.setOption(exportReportOpt);
        } else {
            layer.msg('获取图表数据失败！', {icon: 2});
        }
    });
}

//报表table
function initreportTable() {
    reportTable = $('#exportListTable').DataTable({ //表格初始化
        "lengthMenu": [
            [10, 20, 30, 50, 100],
            ["10", "20", "30", "50", "100"]
        ],//定义在每页显示记录数的select中显示的选项
        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/report/getVideoTransferLogInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.nodeName, obj.nodeIp, obj.username, obj.truename, obj.fileName || '--',obj.receiver || '--', obj.fftype, obj.transferTime, obj.filePath,]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "departmentId": departmentId,
                "submitDate": submitDate,
                "startDate": startDate,
                "endDate": endDate,
                "order": $('#bar_searchstr').val().trim(),
                "logType":logType,
                "nodeType":nodeType
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            // "width": "35px",
            "class": "text-ellipsis",
        }, {
            "targets": [1],
            "orderable": false,
            "class": "text-ellipsis",
        }, {
            "targets": [2],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [3],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [4],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [5],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [6],
            "orderable": false,
            "class": "text-ellipsis",
            "render": function (data, full) {
                if (data == 1) {
                    return '自主导出';
                } else if (data == 3) {
                    return '审批导出';
                } else if (data == 10) {
                    return '自主外发';
                } else if (data == 11) {
                    return '审批外发';
                }
            }
        }, {
            "targets": [7],
            "orderable": false,
            "class": "text-ellipsis",
        }, {
            "targets": [8],
            "orderable": false,
            "class": "center-text",
            "width": "80px",
            "render": function (data, type, full) {
                return '<a style="position: relative" href="#" data-magnify="gallery" class="photoCheck" data-caption="查看缩略图" data-src="' + ctx + data + '"><i class="iconfont icon-btn-photos"></i><img src="' + ctx + data + '" alt="" class="hover_img" width="240px" height="160px" style="position: absolute;right: 20px;top:1px;display: none;border: 5px solid #fff;box-shadow: #666 0px 0px 10px;z-index: 1000000;"/></a>';
            }
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var width = $("body").width();
            var height = $("#wcontent").height();
            //图片配置
            $("body [data-magnify = gallery]").magnify({

                draggable: false,
                keyboard: false,
                fixedModalSize: true,
                modalWidth: width,
                modalHeight: height,
                footToolbar: [
                    'rotateRight'
                ],
                "icons": {
                    "close": "iconfont icon-btn-close"
                },
                headToolbar: ['close'],
                footToolbar: [],
            });
            $("body .icon-btn-photos").hover(function () {
                var offset = document.documentElement.clientHeight - $(this).offset().top;
                if (offset > $(this).next().outerHeight() + 50) {

                } else {
                    $(this).siblings('img').css("top", "-120px")
                }
                $(this).siblings('img').css("display", "block")
            }, function () {
                $(this).siblings('img').css("display", "none")
            })
            var ele = 'exportListTable';
            tableCallback(ele);
            /*var oTable = $("#exportListTable").dataTable();
            设置每一列的title
            $("table").find("tr td:not(:last-child)").each(function (index, obj) {
              $(obj).attr("title", $(obj).text());
            })
            //添加跳转到指定页
            $(".dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePage' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn'>确认</a>");
            $('#dataTable-btn').click(function (e) {
              if ($("#changePage").val() && $("#changePage").val() > 0) {
                var redirectpage = $("#changePage").val() - 1;
              } else {
                var redirectpage = 0;
              }
              oTable.fnPageChange(redirectpage);
            });

            //键盘事件  回车键 跳页
            $("#changePage").keydown(function () {
              var e = event || window.event;
              if (e && e.keyCode == 13) {
                if ($("#changePage").val() && $("#changePage").val() > 0) {
                  var redirectpage = $("#changePage").val() - 1;
                } else {
                  var redirectpage = 0;
                }
                oTable.fnPageChange(redirectpage);
              }
            })*/
        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}

// 获取正在访问人数的信息
function getExportReport() {
    exportReportChart = echarts.init(document.getElementById('exportReport'));

    // exportReportChart.setOption(exportReportOpt);
}

exportReportOpt = {
    color: ['#3398DB'],
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            data: dataX,
            axisTick: {
                alignWithLabel: true
            }
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '流转类型总和',
            type: 'bar',
            barWidth: '60%',
            data: reportData
        }
    ]
};

/**
 * 初始化时间控件
 * @return {[type]} [description]
 */