/**
 * Created by chengl on 2018/1/26 0026.
 */
/**
 * Created by chengl on 2018/1/20 0020.
 */
var clientTable = null;
var submitDateClient = 'day';
var startDateClient = "";
var endDateClient = "";
var orderClient = $('#bar_searchstr_client').val().trim();//关键字
$(function () {
    initEvent();
    initclientTable();
    $("body #menus").resize(function () {
        clientTable.ajax.reload();
    });
    $("#bar_searchstr_client").focus();
});

function initEvent() {
    $(".clientcon")
    //回车搜索
        .on('keydown', '#bar_searchstr_client', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon_client').click();
            }
        })
        //用户名点击搜索
        .on('click', '#bar_searchstr_icon_client', function () {
            if ($(".inputtime_client").val() == "今天") {
                submitDateClient = 'day'
            } else if ($(".inputtime_client").val() == "最近7天") {
                submitDateClient = 'week'
            } else {
                submitDateClient = '';
            }
            // orderClient = $('#bar_searchstr_client').val().trim();
            refreshReportTable();
        })
        // 时间控件
        .on('click', '.datatimechoose_client', function () {
            $(".datachange_client").slideToggle("fast");
        })
        .on('click', '.datachange_client li:not(:last-child)', function () {
            var typedata = $(this).attr("data-type");
            if (typedata == "day") {
                $(".inputtime_client").val("今天");
                submitDateClient = "day";
            } else if (typedata == "week") {
                $(".inputtime_client").val("最近7天");
                submitDateClient = "week";
            }
            startDateClient = "";
            endDateClient = "";
            refreshReportTable();
        })
        //导出
        .on('click', '#bar_export_client', function () {
            var order = $('#bar_searchstr_client').val().trim();
            window.location.href = ctx + '/systemLog/exportSystemLogsByParams?logType=clientLogonLog&submitDate=' + submitDateClient + '&startDate=' + startDateClient + '&endDate=' + endDateClient + '&order=' + order;
        })

    /**
     * 初始化时间控件
     * @return {[type]} [description]
     */

    $(document).on("click", function (e) {
        if (!$(e.target).closest('.datatimechoose_client').hasClass('datatimechoose_client')) {
            $('.datachange_client').hide();
        }
    })
    $('#reservation_client').daterangepicker({
        "locale": {
            format: 'YYYY-MM-DD',
            separator: ' ~ ',
            applyLabel: "确定",
            cancelLabel: "取消",
        },
        "parentEl": '.clientcon',

    }, function (start, end, label) {

        var starttime = $(".clientcon input#starttime").val();
        var endtime = $(".clientcon input#endtime").val();
        var alltime = starttime + " -- " + endtime;
        $(".inputtime_client").val(alltime);
        submitDateClient = "";
        startDateClient = starttime;
        endDateClient = endtime;
        refreshReportTable();
    })


}

/**
 * 表格数据刷新
 */
function refreshReportTable() {
    clientTable.settings()[0].ajax.data.submitDate = submitDateClient;//时间
    clientTable.settings()[0].ajax.data.startDate = startDateClient;//时间
    clientTable.settings()[0].ajax.data.endDate = endDateClient;//时间
    clientTable.settings()[0].ajax.data.order = $('#bar_searchstr_client').val().trim();//关键字
    clientTable.ajax.reload();
}

/**
 * 登陆日志
 *
 */
function initclientTable() {
    clientTable = $('.clientcon #clientTable').DataTable({ //表格初始化

        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/systemLog/getClientLogonLogInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {

                    return [obj.userName,obj.logOperateParam, obj.time || '--']
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "submitDate": submitDateClient,
                "startDate": startDateClient,
                "endDate": endDateClient,
                "order": $('#bar_searchstr_client').val().trim()
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            // "width":"80px",
            "class": "text-ellipsis",
        },  {
            "targets": [1],
            "orderable": false,
            "class": "text-ellipsis",
        }, {
            "targets": [2],
            "orderable": false,
            "class": "text-ellipsis"
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'clientTable';
            tableCallback(ele);

        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}