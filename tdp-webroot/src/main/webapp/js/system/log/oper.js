/**
 * Created by chengl on 2018/1/26 0026.
 */
/**
 * Created by chengl on 2018/1/20 0020.
 */
var operTable = null;
var submitDateOper = 'day';
var startDateOper = "";
var endDateOper = "";
var orderoper = $('#bar_searchstr_oper').val().trim();//关键字
$(function () {
    initEvent();
    initoperTable();
    $("body #menus").resize(function () {
        operTable.ajax.reload();
    });
    $("#bar_searchstr_oper").focus();
});

function initEvent() {
    $(".opercon")
    //回车搜索
        .on('keydown', '#bar_searchstr_oper', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon_oper').click();
            }
        })
        /**
         * 初始化时间控件
         * @return {[type]} [description]
         */

        //用户名点击搜索
        .on('click', '#bar_searchstr_icon_oper', function () {
            if ($(".inputtime_oper").val() == "今天") {
                submitDateOper = 'day'
            } else if ($(".inputtime_oper").val() == "最近7天") {
                submitDateOper = 'week'
            } else {
                submitDateOper = '';
            }
            // orderLogo = $('#bar_searchstr_logo').val().trim();
            refreshReportTable();
        })
        // 时间控件
        .on('click', '.datatimechoose_oper', function () {
            $(".datachange_oper").slideToggle("fast");
        })
        .on('click', '.datachange_oper li:not(:last-child)', function () {
            var typedata = $(this).attr("data-type");
            if (typedata == "day") {
                $(".inputtime_oper").val("今天");
                submitDateOper = "day";
            } else if (typedata == "week") {
                $(".inputtime_oper").val("最近7天");
                submitDateOper = "week";
            }
            startDateOper = "";
            endDateOper = "";
            refreshReportTable();
        })
        //导出
        .on('click', '#bar_export_oper', function () {
            var order = $('#bar_searchstr_oper').val().trim();
            window.location.href = ctx + '/systemLog/exportSystemLogsByParams?logType=operationLog&submitDate=' + submitDateOper + '&startDate=' + startDateOper + '&endDate=' + endDateOper + '&order=' + order;
        })
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.datatimechoose_oper').hasClass('datatimechoose_oper')) {
            $('.datachange_oper').hide();
        }
    });
    /**
     * 初始化时间控件
     * @return {[type]} [description]
     */
    $('#reservation_oper').daterangepicker({
        "locale": {
            format: 'YYYY-MM-DD',
            separator: ' ~ ',
            applyLabel: "确定",
            cancelLabel: "取消",
        },
        "parentEl": '.opercon',

    }, function (start, end, label) {
        var starttime = $(".opercon input#starttime").val();
        var endtime = $(".opercon input#endtime").val();
        var alltime = starttime + " -- " + endtime;
        $(".inputtime_oper").val(alltime);
        submitDateOper = "";
        startDateOper = starttime;
        endDateOper = endtime;
        refreshReportTable();
    })
}

/**
 * 表格数据刷新
 */
function refreshReportTable() {
    operTable.settings()[0].ajax.data.submitDate = submitDateOper;//时间
    operTable.settings()[0].ajax.data.startDate = startDateOper;//时间
    operTable.settings()[0].ajax.data.endDate = endDateOper;//时间
    operTable.settings()[0].ajax.data.order = $('#bar_searchstr_oper').val().trim();//关键字
    operTable.ajax.reload();
}

/**
 * 操作日志
 *
 */
function initoperTable() {
    operTable = $('#operTable').DataTable({ //表格初始化
        /*"searching": true,//关闭Datatables的搜索功能:
        "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
        "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
        "autoWidth": true,//自动计算列宽
        "processing": false,//是否显示正在处理的状态
        "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当用户重新刷新页面，表格的状态将会被设置为之前的设置。
        "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
        "scrollY": "auto",//控制表格的垂直滚动。
        "pagingType": "full_numbers",
        /!*l - Length changing 改变每页显示多少条数据的控件
         f - Filtering input 即时搜索框控件
         t - The Table 表格本身
         i - Information 表格相关信息控件
         p - Pagination 分页控件
         r - pRocessing 加载等待显示信息*!/
        "dom": 'rlfrtip',
        "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
        },
        "lengthMenu": [
          [16, 30, 50, 100],
          ["16条", "30条", "50条", "100条"]
        ],//定义在每页显示记录数的select中显示的选项*/
        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/systemLog/getSystemOperationLogInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.userName, obj.logPage, obj.time || '--', obj.logOperateParam]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "submitDate": submitDateOper,
                "startDate": startDateOper,
                "endDate": endDateOper,
                "order": $('#bar_searchstr_oper').val().trim(),
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
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
            "width": "600px",
            "class": "text-ellipsis"
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'operTable';
            tableCallback(ele);
            /*var oTable = $("#operTable").dataTable();
            //设置每一列的title
            $("table").find("tr td").each(function (index, obj) {
              $(obj).attr("title", $(obj).text());
            })
            //添加跳转到指定页
            $(".opercon .dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePage' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn'>确认</a>");
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