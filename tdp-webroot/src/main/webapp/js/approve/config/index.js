/**
 * Created by chengl on 2018/1/15 0015.
 */
// var processTable = null;
// var processDetailTable = null;
// var processOverTable = null;
// var needOnly = '';
// var submitDate = getDateStr(0);
// var applicantOrType = '';
// var submitDateOver = getDateStr(0);
// var applicantOrTypeOver = '';
$(function () {
  // initEvent();
  // initProcessTable(0);


});
function initEvent() {
  ininDateTimePicker();
  ininDateTimePickerOver();
  $("body")
  //tab页导航
  //   .on("click", ".titleTab li", function () {
  //     initProcessOverTable(1);
  //     initProcessTable(0);
  //     $(this).addClass("titleTabactive").siblings("li").removeClass("titleTabactive");
  //     var classcon = $(this).data("class");
  //     $("." + classcon + "con").show().siblings("div").hide();
  //   })
    //用户名回车搜索
    .on('keydown', '#bar_searchstr', function (e) {
      if (e.keyCode == 13) {
        $('#bar_searchstr_icon').click();
      }
    })

    //用户名点击搜索
    .on('click', '#bar_searchstr_icon', function () {
      processTable.settings()[0].ajax.data.applicantOrType = $('#bar_searchstr').val().trim();
      processTable.settings()[0].ajax.data.submitDate = $.trim($("#timechange").val());
      if ($("#onlyProcess").is(':checked')) {
        processTable.settings()[0].ajax.data.needOnly = 1;
        processTable.ajax.reload();

      } else {
        processTable.settings()[0].ajax.data.needOnly = 0;
        processTable.ajax.reload();
      }
    })

    //点击查看是不是要审批一下
    .on('click', '.approve-opt-icon', function () {
      var id = $(this).data('id');//他的id是多少
      var type = $(this).data('type');//类型是不是外发
      var is = $(this).data('is');//是不是到他审批了
      var detailId;
      layer.open({
        id: 'openWind',
        type: 1,
        title: '审批',
        content: $('#approve_wind').html(),
        area: ['900px', '650px'],
        btn: ['确定', '取消'],
        yes: function (index, layero) {

          if (is) {
            var result = $("#openWind input[name=approveIdea]:checked").val();
            var remark = $("#openWind textarea[name=textarea]").val();
            postAjax(ctx + '/approveFlow/approveFlow', {approveDetailId: detailId, result: result, remark: remark}, function (msg) {
              if (msg.resultCode == 1) {
                layer.msg('审批成功！', {icon: 1});

                layer.close(index);
                processTable.ajax.reload();
              } else {
                layer.msg('审批失败！' + (msg.resultMsg || ''), {icon: 2});
              }
            });
          } else {
            layer.close(index);
          }


        },
        success: function (layero, index) {
          // 获取详情
          getAjax(ctx + '/approveFlow/getApproveFlowInfoById', {approveFlowId: id}, function (msg) {
            if (msg.resultCode == 1) {
              msg.data.flowInfo.policyParam = JSON.parse(msg.data.flowInfo.policyParam);

              if (type == 2) {
                $("#openWind .top").html(template('approve_tem_out_top', msg.data));
              } else {
                $("#openWind .top").html(template('approve_tem_export_top', msg.data));
              }

            }
          });
          //获取环节
          getAjax(ctx + '/approveDetail/getApproveFlowModel', {approveFlowId: id}, function (msg) {
            if (msg.resultCode == 1) {
              detailId = msg.data.detailId;
              if (type == 2) {
                $("#openWind .flow").html(template('getNode_tem', msg));
              } else {
                $("#openWind .flow").html(template('getNode_tem', msg));
              }

            }
          });
          ProcessTable(id);
          if (is == true) {//到了当前人审批，需要审批
            $("#openWind .table").hide();
          } else {
            $("#openWind .opinion").hide();
          }
        }
      });

    })

    //只看需要审批的
    .on('change', '#onlyProcess', function () {
      processTable.settings()[0].ajax.data.searchstr = $('#bar_searchstr').val().trim();
      processTable.settings()[0].ajax.data.submitDate = $.trim($("#timechange").val());
      if ($(this).is(':checked')) {
        processTable.settings()[0].ajax.data.needOnly = 1;
        processTable.ajax.reload();

      } else {
        processTable.settings()[0].ajax.data.needOnly = '';
        processTable.ajax.reload();
      }
    })
    //用户全选或取消全选
    .on('change', '.j-check-process-all', function () {
      $('.j-check-process').prop('checked', $(this).prop('checked'));
    })
    //用户单个选择或取消选择
    .on('change', '.j-check-process', function () {
      $('.j-check-process-all').prop('checked', $('.j-check-process').not(':checked').length == 0);
    })
    //删除已完成的流程
    .on('click', '.j-opt-hover-delete,#bar_del_process', function () {
      var ids = [];
      if ($(this).is('#bar_del_process')) {
        if ($('.j-check-process:checked').length == 0) {
          layer.msg('请先选中流程！', { icon: 7 });
          return;
        }
        else {
          ids = $('.j-check-process:checked').map(function (index, obj) {
            return $(obj).attr('data-id');
          }).toArray();
        }
      }
      else {
        ids.push($(this).attr('data-id'));
      }
      layer.confirm(ids.length > 1 ? '确定要删除选中的流程吗？' : '确定要删除该流程吗？', {
        btn: ['确定', '取消']
      }, function () {
        var postData = {};
        postData.approveFlowArr = ids.join(',');
        $.ajax({
          type: 'post',
          url: ctx + '/approveFlow/deleteApproveFlow',
          data: postData,
          success: function (msg) {
            if (msg.resultCode == 1) {
              processOverTable.ajax.reload(function () { }, true);
              $('.j-check-process-all').prop('checked', false);
              layer.msg('删除成功！', { icon: 1 });
            } else {
              layer.msg('删除失败！', { icon: 2 });
            }
          },
          error: function () {
            layer.msg('删除失败！', { icon: 2 });
          }
        })
      });
    })
    //点击查看详情
    .on('click', '.j-opt-hover-detail', function () {
      var id = $(this).data('id');//他的id是多少
      var type = $(this).data('type');//类型是不是外发
      layer.open({
        id: 'openWind',//这个地方会自动给弹出框添加一个id
        type: 1,
        title: '审批详情',
        content: $('#approve_wind').html(),
        area: ['900px', '650px'],
        btn: ['确定', '取消'],
        yes: function (index, layero) {

        },
        success: function (layero, index) {
          // 获取详情
          getAjax(ctx + '/approveFlow/getApproveFlowInfoById', {approveFlowId: id}, function (msg) {
            msg.data.flowInfo.policyParam = JSON.parse(msg.data.flowInfo.policyParam);
            if (msg.resultCode == 1) {
              if (type == 2) {
                $("#openWind .top").html(template('approve_tem_out_top', msg.data));
              } else {
                $("#openWind .top").html(template('approve_tem_export_top', msg.data));
              }

            }
          });
          //获取环节
          getAjax(ctx + '/approveDetail/getApproveFlowModel', {approveFlowId: id}, function (msg) {
            if (msg.resultCode == 1) {
              if (type == 2) {
                $("#openWind .flow").html(template('getNode_tem', msg));
              } else {
                $("#openWind .flow").html(template('getNode_tem', msg));
              }

            }
          });
          ProcessTable(id);
          $("#openWind .opinion").hide();
        }

      });
    })

    //显示操作悬浮框
    .on('mouseover', '.table-opt-icon', function () {
      var offset = document.documentElement.clientHeight - $(this).offset().top;
      $(this).next().addClass('opt-hover-down');
      if (offset > $(this).next().outerHeight() + 70) {
        $(this).next().removeClass('opt-hover-down').addClass('opt-hover-up');
      }
    })

    //隐藏操作悬浮框
    .on('mouseleave', '.table-opt-box', function () {
      $(this).find('.opt-hover-box').removeClass('opt-hover-up opt-hover-down');
    })

}

/**
 * 初始化时间控件
 * @return {[type]} [description]
 */
function ininDateTimePicker() {
  $('#timechange').val(new Date().Format('yyyy-MM-dd')).datetimepicker({
    language: 'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    minView: 2,
    todayBtn: true,
    endDate: new Date()
  })
    .on('changeDate', function (ev) {
      processTable.settings()[0].ajax.data.applicantOrType = $('#bar_searchstr').val().trim();
      processTable.settings()[0].ajax.data.submitDate = $.trim($("#timechange").val());
      if ($("#onlyProcess").is(':checked')) {
        processTable.settings()[0].ajax.data.needOnly = 1;
        processTable.ajax.reload();

      } else {
        processTable.settings()[0].ajax.data.needOnly = '';
        processTable.ajax.reload();
      }
    }).on('hide', function () {
    setTimeout(function () {
      $('#timechange').blur();
    }, 50);
  });
}
/**
 * 初始化时间控件
 * @return {[type]} [description]
 */
function ininDateTimePickerOver() {
  $('#timechangeOver').val(new Date().Format('yyyy-MM-dd')).datetimepicker({
    language: 'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    minView: 2,
    todayBtn: true,
    endDate: new Date()
  })
    .on('changeDate', function (ev) {
      processOverTable.settings()[0].ajax.data.applicantOrType = $('#bar_searchstrOver').val().trim();
      processOverTable.settings()[0].ajax.data.submitDate = $.trim($("#timechangeOver").val());
      processOverTable.ajax.reload();
    }).on('hide', function () {
    setTimeout(function () {
      $('#timechangeOver').blur();
    }, 50);
  });
}
//未完成审批表
function initProcessTable(status) {
  if (processTable) {
    processTable.ajax.reload();
    return;
  }
  processTable = $('#processTable').DataTable({ //表格初始化
    "searching": true,//关闭Datatables的搜索功能:
    "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
    "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
    "autoWidth": true,//自动计算列宽
    "processing": false,//是否显示正在处理的状态
    "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当用户重新刷新页面，表格的状态将会被设置为之前的设置。
    "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
    "scrollY": "auto",//控制表格的垂直滚动。
    "pagingType": "full_numbers",
    /*l - Length changing 改变每页显示多少条数据的控件
     f - Filtering input 即时搜索框控件
     t - The Table 表格本身
     i - Information 表格相关信息控件
     p - Pagination 分页控件
     r - pRocessing 加载等待显示信息*/
    "dom": 'rlfrtip',
    "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
    },
    "lengthMenu": [
      [20, 30, 50, 100],
      ["20条", "30条", "50条", "100条"]
    ],//定义在每页显示记录数的select中显示的选项
    "ajax": {
      "beforeSend": function () {
      },
      "url": ctx + "/approveFlow/getApproveFlowPage",
      //改变从服务器返回的数据给Datatable
      "dataSrc": function (json) {
        return json.data.map(function (obj) {
          return [obj.name, obj.type, obj.applicantName, obj.pointName, obj.applyTime || '--', {id: obj.id, type: obj.type, isa: obj.checked}];//是不是到了当前人
        });
      },
      //将额外的参数添加到请求或修改需要被提交的数据对象
      "data": {
        "needOnly": needOnly,
        "applicantOrType": applicantOrType,
        "submitDate": submitDate,
        "status": status,
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
      "render": function (data, type, full) {
        if (data == 2) {
          return '<span>外发</span>'
        } else {
          return '<span>导出</span>'
        }
      }
    }, {
      "targets": [2],
      "orderable": false,
      "class": "text-ellipsis",
    }, {
      "targets": [3],
      "orderable": false,
      "class": "text-ellipsis",
    }, {
      "targets": [4],
      "orderable": false,
      "class": "text-ellipsis"
    }, {
      "targets": [5],
      "orderable": false,
      "class": "center-text",
      "width": "80px",
      "render": function (data, type, full) {

        return template('temp_approve', {id: data.id, type: data.type, isa: data.isa});

      }
    }],
    //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
    "drawCallback": function (oTable) {
      var oTable = $("#processTable").dataTable();
      //设置每一列的title
      $("table").find("tr td:not(:last-child)").each(function (index, obj) {
        $(obj).attr("title", $(obj).text());
      })
      //添加跳转到指定页
      $(".processcon .dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePage' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn'>确认</a>");
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
      })
    }
  }).on('xhr.dt', function (e, settings, json, xhr) {
    //登陆超时重定向
    if (xhr.getResponseHeader('isRedirect') == 'yes') {
      location.href = "/tdp/login";
    }
  });
}
//完成审批表
function initProcessOverTable(status) {
  if (processOverTable) {
    processOverTable.ajax.reload();
    return;
  }
  processOverTable = $('#processTableOver').DataTable({ //表格初始化
    "searching": true,//关闭Datatables的搜索功能:
    "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
    "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
    "autoWidth": true,//自动计算列宽
    "processing": false,//是否显示正在处理的状态
    "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当用户重新刷新页面，表格的状态将会被设置为之前的设置。
    "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
    "scrollY": "auto",//控制表格的垂直滚动。
    "pagingType": "full_numbers",
    /*l - Length changing 改变每页显示多少条数据的控件
     f - Filtering input 即时搜索框控件
     t - The Table 表格本身
     i - Information 表格相关信息控件
     p - Pagination 分页控件
     r - pRocessing 加载等待显示信息*/
    "dom": 'rlfrtip',
    "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
    },
    "lengthMenu": [
      [20, 30, 50, 100],
      ["20条", "30条", "50条", "100条"]
    ],//定义在每页显示记录数的select中显示的选项
    "ajax": {
      "beforeSend": function () {
      },
      "url": ctx + "/approveFlow/getApproveFlowPage",
      //改变从服务器返回的数据给Datatable
      "dataSrc": function (json) {
        return json.data.map(function (obj) {
          return [obj.id, obj.name, obj.type, obj.applicantName, obj.applyTime, obj.status, obj.finishTime || '--', {id: obj.id, type: obj.type}];//是不是到了当前人
        });
      },
      //将额外的参数添加到请求或修改需要被提交的数据对象
      "data": {
        "applicantOrType":applicantOrTypeOver,
        "submitDate":submitDateOver,
        "status": status
      },
    },
    "columnDefs": [{
      "targets": [0],
      "orderable": false,
      "width": "35px",
      "class": "text-center",
      "render": function (data, type, full) {
        return '<div class="beauty-checkbox">' +
          '<input id="table_check_' + data + '" type="checkbox" class="j-check-process" data-id="' + data + '">' +
          '<label for="table_check_' + data + '" class="checkbox-icon"></label>' +
          '</div>';
      }

    }, {
      "targets": [1],
      "orderable": false,
      "class": "text-ellipsis"
    }, {
      "targets": [2],
      "orderable": false,
      "class": "text-ellipsis",
      "render": function (data, type, full) {
        if (data == 0) {
          return '<span>外发</span>'
        } else {
          return '<span>导出</span>'
        }
      }
    }, {
      "targets": [3],
      "orderable": false,
      "class": "text-ellipsis",
    }, {
      "targets": [4],
      "orderable": false,
      "class": "text-ellipsis"
    }, {
      "targets": [5],
      "orderable": false,
      "class": "text-ellipsis",
      "render": function (data, type, full) {
        if (data == -1) {
          return '<span class="refuse">拒绝</span>'
        } else {
          return '<span class="pass">通过</span>'
        }
      }
    }, {
      "targets": [6],
      "orderable": false,
      "class": "text-ellipsis"
    }, {
      "targets": [7],
      "orderable": false,
      "class": "center-text",
      "width": "80px",
      "render": function (data, type, full) {

        return template('temp_opt_box', {id: data.id, type: data.type});

      }
    }],
    //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
    "drawCallback": function (oTable) {
      var oTable = $("#processTableOver").dataTable();
      //设置每一列的title
      $("table").find("tr td:not(:last-child)").each(function (index, obj) {
        $(obj).attr("title", $(obj).text());
      })
      //添加跳转到指定页
      $(".complatecon .dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePageOver' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btnOver'>确认</a>");
      $('#dataTable-btnOver').click(function (e) {
        if ($("#changePageOver").val() && $("#changePageOver").val() > 0) {
          var redirectpage = $("#changePageOver").val() - 1;
        } else {
          var redirectpage = 0;
        }
        oTable.fnPageChange(redirectpage);
      });

      //键盘事件  回车键 跳页
      $("#changePageOver").keydown(function () {
        var e = event || window.event;
        if (e && e.keyCode == 13) {
          if ($("#changePageOver").val() && $("#changePageOver").val() > 0) {
            var redirectpage = $("#changePageOver").val() - 1;
          } else {
            var redirectpage = 0;
          }
          oTable.fnPageChange(redirectpage);
        }
      })
    }
  }).on('xhr.dt', function (e, settings, json, xhr) {
    //登陆超时重定向
    if (xhr.getResponseHeader('isRedirect') == 'yes') {
      location.href = "/tdp/login";
    }
  });
}
//审批流程细节表
function ProcessTable(id) {

  processDetailTable = $('#openWind #approveHistoryTable').DataTable({ //表格初始化
    "paging": false,//禁用分页
    "info": false,//禁止显示信息
    "searching": true,//关闭Datatables的搜索功能:
    "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
    // "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
    "autoWidth": true,//自动计算列宽
    "processing": false,//是否显示正在处理的状态
    "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当用户重新刷新页面，表格的状态将会被设置为之前的设置。
    "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
    "scrollY": "auto",//控制表格的垂直滚动。
    "pagingType": "full_numbers",
    /*l - Length changing 改变每页显示多少条数据的控件
     f - Filtering input 即时搜索框控件
     t - The Table 表格本身
     i - Information 表格相关信息控件
     p - Pagination 分页控件
     r - pRocessing 加载等待显示信息*/
    // "dom": 'rlfrtip',
    "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
    },
    // "lengthMenu": [
    // [20, 30, 50, 100],
    // ["20条", "30条", "50条", "100条"]
    // ],//定义在每页显示记录数的select中显示的选项
    "ajax": {
      "beforeSend": function () {
      },
      "url": ctx + "/approveDetail/getApproveDetailsByFlowId",
      //改变从服务器返回的数据给Datatable
      "dataSrc": function (json) {
        return json.data.map(function (obj) {
          return [obj.approverName, obj.result, obj.remark || '--', obj.modifyTime];//是不是到了当前人
        });
      },
      //将额外的参数添加到请求或修改需要被提交的数据对象
      "data": {
        "approveFlowId": id,
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
      "render": function (data, type, full) {
        if (data == 1) {
          return '<span>同意</span>'
        } else {
          return '<span>拒绝</span>'
        }
      }

    }, {
      "targets": [2],
      "orderable": false,
      "class": "text-ellipsis",
    }, {
      "targets": [3],
      "orderable": false,
      "class": "text-ellipsis",
    }],
    //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
    // "drawCallback": function (oTable) {
    //   var oTable = $("#openWind #approveHistoryTable").dataTable();
    //   //设置每一列的title
    //   $("table").find("tr td:not(:last-child)").each(function (index, obj) {
    //     $(obj).attr("title", $(obj).text());
    //   })
    //   //添加跳转到指定页
    //   $(".dataTables_paginate").append("<span style='margin-left: 10px;font-size: 14px;'>到第 </span><input style='height:20px;line-height:28px;width:28px;margin-top: 5px;' class='margin text-center' id='changePage' type='text'> <span style='font-size: 14px;'>页</span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn'>确认</a>");
    //   $('#dataTable-btn').click(function (e) {
    //     if ($("#changePage").val() && $("#changePage").val() > 0) {
    //       var redirectpage = $("#changePage").val() - 1;
    //     } else {
    //       var redirectpage = 0;
    //     }
    //     oTable.fnPageChange(redirectpage);
    //   });
    //
    //   //键盘事件  回车键 跳页
    //   $("#changePage").keydown(function () {
    //     var e = event || window.event;
    //     if (e && e.keyCode == 13) {
    //       if ($("#changePage").val() && $("#changePage").val() > 0) {
    //         var redirectpage = $("#changePage").val() - 1;
    //       } else {
    //         var redirectpage = 0;
    //       }
    //       oTable.fnPageChange(redirectpage);
    //     }
    //   })
    // }
  })
  //   .on('xhr.dt', function (e, settings, json, xhr) {
  //   //登陆超时重定向
  //   if (xhr.getResponseHeader('isRedirect') == 'yes') {
  //     location.href = "/tdp/login";
  //   }
  // });
}

/**
 *  获取前几天或后几天的日期 格式  例：2016-11-15格式不能乱
 * @param {*2016-11-15} AddDayCount
 */
function getDateStr(AddDayCount) {
  var dd = new Date();
  dd.setDate(dd.getDate() + AddDayCount); //获取AddDayCount天后的日期
  var y = dd.getFullYear();
  var m = (dd.getMonth() + 1) < 10 ? "0" + (dd.getMonth() + 1) : (dd.getMonth() + 1); //获取当前月份的日期，不足10补0
  var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate(); //获取当前几号，不足10补0
  return y + "-" + m + "-" + d;
}
