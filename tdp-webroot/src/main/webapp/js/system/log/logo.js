/**
 * Created by chengl on 2018/1/26 0026.
 */
/**
 * Created by chengl on 2018/1/20 0020.
 */
var logoTable = null;
var submitDateLogo = 'day';
var startDateLogo = "";
var endDateLogo = "";
var orderLogo = $('#bar_searchstr_logo').val().trim();//关键字
$(function () {
    initEvent();
    initlogoTable();
    $("body #menus").resize(function () {
        logoTable.ajax.reload();
    });
    $("#bar_searchstr_logo").focus();
});

function initEvent() {
    $(".logocon")
    //回车搜索
        .on('keydown', '#bar_searchstr_logo', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon_logo').click();
            }
        })
        //用户名点击搜索
        .on('click', '#bar_searchstr_icon_logo', function () {
            if ($(".inputtime_logo").val() == "今天") {
                submitDateLogo = 'day'
            } else if ($(".inputtime_logo").val() == "最近7天") {
                submitDateLogo = 'week'
            } else {
                submitDateLogo = '';
            }
            // orderLogo = $('#bar_searchstr_logo').val().trim();
            refreshReportTable();
        })
        // 时间控件
        .on('click', '.datatimechoose_logo', function () {
            $(".datachange_logo").slideToggle("fast");
        })
        .on('click', '.datachange_logo li:not(:last-child)', function () {
            var typedata = $(this).attr("data-type");
            if (typedata == "day") {
                $(".inputtime_logo").val("今天");
                submitDateLogo = "day";
            } else if (typedata == "week") {
                $(".inputtime_logo").val("最近7天");
                submitDateLogo = "week";
            }
            startDateLogo = "";
            endDateLogo = "";
            refreshReportTable();
        })
        //导出
        .on('click', '#bar_export_logo', function () {
            var order = $('#bar_searchstr_logo').val().trim();
            window.location.href = ctx + '/systemLog/exportSystemLogsByParams?logType=logonLog&submitDate=' + submitDateLogo + '&startDate=' + startDateLogo + '&endDate=' + endDateLogo + '&order=' + order;
        })

    /**
     * 初始化时间控件
     * @return {[type]} [description]
     */

    $(document).on("click", function (e) {
        if (!$(e.target).closest('.datatimechoose_logo').hasClass('datatimechoose_logo')) {
            $('.datachange_logo').hide();
        }
    })
    $('#reservation_logo').daterangepicker({
        "locale": {
            format: 'YYYY-MM-DD',
            separator: ' ~ ',
            applyLabel: "确定",
            cancelLabel: "取消",
        },
        "parentEl": '.logocon',

    }, function (start, end, label) {

        var starttime = $(".logocon input#starttime").val();
        var endtime = $(".logocon input#endtime").val();
        var alltime = starttime + " -- " + endtime;
        $(".inputtime_logo").val(alltime);
        submitDateLogo = "";
        startDateLogo = starttime;
        endDateLogo = endtime;
        refreshReportTable();
    })


}

/**
 * 表格数据刷新
 */
function refreshReportTable() {
    logoTable.settings()[0].ajax.data.submitDate = submitDateLogo;//时间
    logoTable.settings()[0].ajax.data.startDate = startDateLogo;//时间
    logoTable.settings()[0].ajax.data.endDate = endDateLogo;//时间
    logoTable.settings()[0].ajax.data.order = $('#bar_searchstr_logo').val().trim();//关键字
    logoTable.ajax.reload();
}

/**
 * 登陆日志
 *
 */
function initlogoTable() {
    logoTable = $('.logocon #logoTable').DataTable({ //表格初始化
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
          [20, 30, 50, 100],
          ["20条", "30条", "50条", "100条"]
        ],//定义在每页显示记录数的select中显示的选项*/
        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/systemLog/getSystemLogonLogInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.userName, obj.logPage, obj.time || '--', obj.ip || '--', obj.logOperateParam]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "submitDate": submitDateLogo,
                "startDate": startDateLogo,
                "endDate": endDateLogo,
                "order": $('#bar_searchstr_logo').val().trim(),
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            // "width":"80px",
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
            "width": "600px",
            "class": "text-ellipsis"
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'logoTable';
            tableCallback(ele);
            // var oTable = $(".logocon #logoTable").dataTable();
            // //设置每一列的title
            // $("table").find("tr td").each(function (index, obj) {
            //   $(obj).attr("title", $(obj).text());
            // })
            // //添加跳转到指定页
            // $(".logocon .dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePageLogo' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn_logo'>确认</a>");
            // $('.logocon #dataTable-btn_logo').click(function (e) {
            //   if ($(".logocon #changePageLogo").val() && $(".logocon #changePageLogo").val() > 0) {
            //     var redirectpage = $(".logocon #changePageLogo").val() - 1;
            //   } else {
            //     var redirectpage = 0;
            //   }
            //   oTable.fnPageChange(redirectpage);
            // });
            //
            // //键盘事件  回车键 跳页
            // $("#changePageLogo").keydown(function () {
            //   var e = event || window.event;
            //   if (e && e.keyCode == 13) {
            //     if ($("#changePageLogo").val() && $("#changePageLogo").val() > 0) {
            //       var redirectpage = $("#changePageLogo").val() - 1;
            //     } else {
            //       var redirectpage = 0;
            //     }
            //     oTable.fnPageChange(redirectpage);
            //   }
            // })
        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}