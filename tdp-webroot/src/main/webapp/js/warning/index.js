/**
 * Created by chengl on 2018/5/23 0004.
 */
var warningTable = null;//表格
var departmentTree = null;//部门树
var zNodes = null;//部门数据
var departmentId = 1;//全局部门id
var submitDate = 'week';//时间
var startDate = "";
var endDate = "";
var order = $('#bar_searchstr').val().trim();//关键字
var logType = "";

$(function () {

    initwarningTable();
    getDepartmentData();
    initEvents(); //初始化事件
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.dropdown-tree-box').hasClass('dropdown-tree-box')) {
            $('#department_tree').hide();
        }
    });
    $("body #menus").resize(function () {
        warningTable.ajax.reload();
    });
    $("#bar_searchstr").focus();
});

/**
 * 初始化事件
 *
 */
function initEvents() {
    $('body')
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
            } else {
                submitDate = '';
            }
            order = $('#bar_searchstr').val().trim();
            refreshReportTable();

            getUserData();
        })
        //导出
        .on('click', '#bar_export', function () {
            window.location.href = ctx + '/warning/exportIllegalOperationAlarmByParams';
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
            }
            startDate = "";
            endDate = "";
            refreshReportTable();
        })
        .on('change', '#alltype',function(){
            logType = $(this).find('option:selected').val();
            refreshReportTable();
        });
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
        refreshReportTable();
    });
    //窗口缩放调整图表
    $(window).resize(function () {
        refreshReportTable();
    });


}

/**
 * 表格数据刷新
 */
function refreshReportTable() {
    warningTable.settings()[0].ajax.data.departmentId = departmentId;//部门id
    warningTable.settings()[0].ajax.data.submitDate = submitDate;//时间
    warningTable.settings()[0].ajax.data.startDate = startDate;//时间
    warningTable.settings()[0].ajax.data.endDate = endDate;//时间
    warningTable.settings()[0].ajax.data.order = $('#bar_searchstr').val().trim();//关键字
    warningTable.settings()[0].ajax.data.logType = logType;//关键字
    warningTable.ajax.reload();
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
                    } else {
                        submitDate = '';
                    }
                    refreshReportTable();
                    getUserData();
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
            /*console.log(departmentTree)
            $("#bar_dept").val(deptname);*/
            $('#' + departmentTree.getNodeByParam('id', deptid).tId + '_a').click();
        }
    })
}

//报表table
function initwarningTable() {
    warningTable = $('#exportListTable').DataTable({ //表格初始化
        "lengthMenu": [
            [10, 20, 30, 50, 100],
            ["10", "20", "30", "50", "100"]
        ],//定义在每页显示记录数的select中显示的选项
        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/warning/getIllegalOperationAlarmInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.username, obj.truename, obj.ip, obj.warningType, obj.receiver || "--",obj.fileName || "--", obj.warningTime, obj.hasRead]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "departmentId": departmentId,
                "submitDate": submitDate,
                "startDate": startDate,
                "endDate": endDate,
                "order": $('#bar_searchstr').val().trim(),
                "logType":logType
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
            "class": "text-ellipsis",
            "render": function (data, full) {
                if (data == 1) {
                    return '非法登陆';
                } else if (data == 2) {
                    return '非法导出';
                } else if (data == 3) {
                    return '非法外发';
                }
            }
        }, {
            "targets": [4],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [5],
            "orderable": false,
            "class": "text-ellipsis",

        }, {
            "targets": [6],
            "orderable": false,
            "class": "text-ellipsis",
        }, {
            "targets": [7],
            "orderable": false,
            "class": "center-text",
            "width": "100px",
            "render": function (data, full) {
                if (data == 0) {
                    return '<span class="common-span color-orange">未读</span>';
                } else if (data == 1) {
                    return '<span class="common-span color-deepgrey">已读</span>';
                }
            }
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'exportListTable';
            tableCallback(ele);

        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}

/**
 * 初始化时间控件
 * @return {[type]} [description]
 */