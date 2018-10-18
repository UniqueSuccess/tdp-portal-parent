var uKeyTable = null;//部门usbkey表
$(function () {
    initusbkeyTable()//usbkey表
    initEvents();//初始化页面事件
    $("body #menus").resize(function () {
        uKeyTable.ajax.reload();
    });
    $("#bar_searchstr").focus();
});

function initEvents() {
    $('body')
    //删除usbkey
        .on('click', '.j-opt-hover-delete,#bar_delete', function () {
            var ids = [];
            if ($(this).is('#bar_delete')) {
                if ($('.j-check-usbkey:checked').length == 0) {
                    layer.msg('请先选中UKey！', {icon: 7});
                    return;
                } else {
                    ids = $('.j-check-usbkey:checked').map(function (index, obj) {
                        return $(obj).attr('data-id');
                    }).toArray();
                }
            } else {
                ids.push($(this).attr('data-id'));
            }
            layer.confirm('确定要删除该UKey吗？', {
                btn: ['确定', '取消']
            }, function () {
                var postData = {};
                postData.ids = ids.join(',');
                postAjax(ctx + '/usbKey/deleteUsbKey', postData, function (msg) {
                    if (msg.resultCode == 1) {
                        uKeyTable.ajax.reload(function () {
                        }, true);
                        layer.msg('删除成功！', {icon: 1});
                        $('.j-check-usbkey-all').prop('checked', false);
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            });
        })
        //添加usbkey
        .on('click', '#bar_add_usbkey', function () {
            layer.open({
                id: 'openWind',
                type: 1,
                title: '添加UKey',
                content: $('#add_usbkey_wind').html(),
                area: ['670px', '300px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-user-form").valid()) {
                        $($("#openWind input.error")[0]).focus();
                        return;
                    }
                    if ($('#openWind input[name=sn]').val().trim() == '') {
                        layer.msg('请检索UKey设备！', {icon: 7});
                        return false;
                    }
                    var postData = {
                        name: $('#openWind input[name=name]').val().trim(),
                        keysn: $('#openWind input[name=sn]').val().trim(),
                        keynum: $('#openWind input[name=uuid]').val().trim(),
                    }
                    if ($(layero).find('.layui-layer-btn0').hasClass('btn-disabled')) {
                        return;
                    }
                    $(layero).find('.layui-layer-btn0').addClass('btn-disabled');
                    $.ajax({
                        type: 'post',
                        url: ctx + '/usbKey/addUsbKey',
                        data: postData,
                        success: function (msg) {
                            if (msg.resultCode == 1) {
                                uKeyTable.ajax.reload(function () {
                                }, true);
                                layer.close(index);
                                layer.msg('添加成功！', {icon: 1});

                            } else {
                                $(layero).find('.layui-layer-btn0').removeClass('btn-disabled');
                                layer.msg('添加失败！' + (msg.resultMsg || ''), {icon: 2});
                            }
                        },
                        error: function () {
                            $(layero).find('.layui-layer-btn0').removeClass('btn-disabled');
                            layer.msg('添加失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    })
                },
                success: function (layero, index) {
                    firstFocus();
                    checkDevice(false);
                    LayerTips();

                    //校验
                    $('#openWind .j-add-user-form').validate({
                        rules: {
                            name: {
                                required: true,
                                numchinese: true
                            }
                        }
                    });
                }
            });
        })
        //编辑usbkey
        .on('click', '.j-opt-hover-edit', function () {
            var id = Number($(this).attr('data-id'));//这是usbkey的id
            var name = $(this).attr('data-name');//这是usbkey的id
            var sn = $(this).attr('data-sn');//这是usbkey的id
            var uuid = $(this).attr('data-uuid');//这是usbkey的id
            layer.open({
                id: 'openWind',//这个地方会自动给弹出框添加一个id
                type: 1,
                title: '编辑UKey',
                content: $('#add_usbkey_wind').html(),
                area: ['670px', '300px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-user-form").valid()) {
                        $($("#openWind input.error")[0]).focus();
                        return;
                    }
                    var postData = {
                        id: id,
                        name: $('#openWind input[name=name]').val(),
                        keysn: $('#openWind input[name=sn]').val(),
                        keynum: $('#openWind input[name=uuid]').val(),
                    };
                    if ($(layero).find('.layui-layer-btn0').hasClass('btn-disabled')) {
                        return;
                    }
                    $(layero).find('.layui-layer-btn0').addClass('btn-disabled');
                    debugger
                    postAjax(ctx + '/usbKey/editUsbKey', postData, function (msg) {
                        if (msg.resultCode == 1) {
                            uKeyTable.ajax.reload(function () {
                            }, true);
                            layer.close(index);
                            layer.msg('编辑成功！', {icon: 1});
                        } else {
                            $(layero).find('.layui-layer-btn0').removeClass('btn-disabled');
                            layer.msg('编辑失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    });
                },
                success: function (layero, index) {
                    firstFocus();
                    checkDevice(false);
                    LayerTips();
                    $('#openWind input[name=name]').val(name);
                    $('#openWind input[name=sn]').val(sn);
                    $('#openWind input[name=uuid]').val(uuid);
                    //校验
                    $('#openWind .j-add-user-form').validate({
                        rules: {
                            name: {
                                required: true,
                                numchinese: true
                            }
                        }
                    });
                }
            });
        })
        //批量解绑ukey
        .on('click', '.j-opt-hover-unbind,#bar_unbind', function () {
            var idx = $('.j-opt-hover-unbind').index(this);
            var ids = [];
            if ($(this).is('#bar_unbind')) {
                if ($('.j-check-usbkey:checked').length == 0) {
                    layer.msg('请先选中UKey！', {icon: 7});
                    return;
                } else {
                    ids = $('.j-check-usbkey:checked').map(function (index, obj) {
                        return $(obj).attr('data-id');
                    }).toArray();
                }
            } else {
                if(uKeyTable.ajax.json().data[idx].userName == undefined){
                    layer.msg('该UKey未绑定用户！', {icon: 7});
                    return false;
                }
                ids.push($(this).attr('data-id'));
            }

            layer.confirm(ids.length > 1 ? '确定要批量解绑UKey吗？' : '确定要解绑该UKey吗？', {
                btn: ['确定', '取消']
            }, function () {
                var postData = {};
                postData.ids = ids.join(',');
                $.ajax({
                    type: 'post',
                    url: ctx + '/usbKey/unbindUsbKeyByUsbKeyId',//批量修改策略接口
                    data: postData,
                    success: function (msg) {
                        if (msg.resultCode == '1') {
                            uKeyTable.ajax.reload(function () {
                            }, true);
                            $('.j-check-usbkey-all').prop('checked', false);
                            layer.msg('解绑成功！', {icon: 1});
                        } else {
                            layer.msg('解绑失败！', {icon: 2});
                        }
                    },
                    error: function () {
                        layer.msg('解绑失败！', {icon: 2});
                    }
                })
            })
        })
        //usbkey全选或取消全选
        .on('change', '.j-check-usbkey-all', function () {
            $('.j-check-usbkey').prop('checked', $(this).prop('checked'));
        })
        //usbkey单个选择或取消选择
        .on('change', '.j-check-usbkey', function () {
            $('.j-check-usbkey-all').prop('checked', $('.j-check-usbkey').not(':checked').length == 0);
        })
        //usbkey名回车搜索
        .on('keydown', '#bar_searchstr', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon').click();
            }
        })
        //usbkey名点击搜索
        .on('click', '#bar_searchstr_icon', function () {
            uKeyTable.settings()[0].ajax.data.order = $('#bar_searchstr').val().trim();
            uKeyTable.ajax.reload();
        })
        //启动设备
        .off("click", '.j-start-device').on('click', '.j-start-device', function () {
        $("#openWind #open").attr("href", 'VdpUKeyHelp:///');
        $("#openWind #open").trigger("click");
        setTimeout(function () {
            checkDevice(true);
        }, 3000)
    })
    //检索ukey
        .off("click", '.j-check-device').on('click', '.j-check-device', function () {
        readDeviceInfo();
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

//
function readDeviceInfo() {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20023/msg',
        dataType: 'jsonp',
        data: "",
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        success: function (msg) {
            $("#openWind #sn").val(msg.ukey[0].deviceid);
            $("#openWind #uuid").val(msg.ukey[0].unqiueid);
        },
        error: function (msg) {
            layer.msg("读取设备失败", {icon: 2});
        }

    });

}

//页面进入检查设备和设备是否正常启动
function checkDevice(fl) {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20023/checklive',
        dataType: 'jsonp',
        data: '',
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        timeout: 1000,
        success: function (msg) {
            $("#openWind #start_device").removeClass("j-start-device").addClass("color-grey");
            if (fl) {
                layer.msg("启用成功", {icon: 1});
            }


        },
        error: function (msg) {
            if (fl) {
                layer.msg("启用设备失败", {icon: 2});
            }

        }

    });
}

//hover下出现提示
function LayerTips() {
    $("#openWind .tips").mouseenter(function () {
        var that = this;
        var msg = $(this).find('div').html();
        layer.tips(msg, that, {
            tips: [3, 'rgba(0,0,0,0.7)'],
            time: 0,
            area: ['250px', '60px']
        })
        var tipsLeft = $('.layui-layer-tips').css('left');
        var tipsTop = $('.layui-layer-tips').css('top');
        tipsLeft = tipsLeft.split('px');
        tipsTop = tipsTop.split('px');
        $('.layui-layer-tips').css('left', (tipsLeft[0] - 180) + 'px');
        $('.layui-layer-tips').css('top', (Number(tipsTop[0]) + 2) + 'px');

        $('.layui-layer-tips .layui-layer-TipsG').css({
            "border-right-color": "rgba(0, 0, 0, 0)"
        });
    })
    $("#openWind .tips span").mouseleave(function () {
        layer.close(layer.index);
    })

}

/**
 * 子部门列表
 *
 */
function initusbkeyTable() {
    if (uKeyTable) {
        uKeyTable.settings()[0].ajax.data.order = $('#bar_searchstr').val().trim();
        uKeyTable.ajax.reload();
        return;
    }
    uKeyTable = $('#usbkey_table').DataTable({ //表格初始化
        "searching": true,//关闭Datatables的搜索功能:
        "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
        "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
        "autoWidth": true,//自动计算列宽
        "processing": false,//是否显示正在处理的状态
        "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当usbkey重新刷新页面，表格的状态将会被设置为之前的设置。
        "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
        "scrollY": "auto",//控制表格的垂直滚动。
        "pagingType": "full_numbers",
        /*l - Length changing 改变每页显示多少条数据的控件
         f - Filtering input 即时搜索框控件
         t - The Table 表格本身
         i - Information 表格相关信息控件
         p - Pagination 分页控件
         r - pRocessing 加载等待显示信息*/
        "dom": 'rfrtilp',
        "oLanguage": {
            "sEmptyTable": "暂无数据",
        },
        "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
        },
        "lengthMenu": [
            [20, 30, 50, 100],
            ["20", "30", "50", "100"]
        ],//定义在每页显示记录数的select中显示的选项
        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/usbKey/getUsbKeyInPage",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.id, obj.name, obj.keysn || '--', obj.userName
                    || '--', {
                        id: obj.id,
                        name: obj.name,
                        sn: obj.keysn,
                        uuid: obj.keynum
                    }]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "order": $('#bar_searchstr').val().trim()
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            "width": "35px",
            "class": "text-center",
            "render": function (data, type, full) {
                return '<div class="beauty-checkbox">' +
                    '<input id="table_check_' + data + '" type="checkbox" class="j-check-usbkey" data-id="' + data + '">' +
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
            "class": "text-ellipsis"
        }, {
            "targets": [3],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [4],
            "orderable": false,
            "class": "center-text",
            "width": "100px",
            "render": function (data, type, full) {
                return template('temp_opt_box', {"id": data.id, "name": data.name, "sn": data.sn, "uuid": data.uuid});
            }
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'usbkey_table';
            tableCallback(ele);
        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}


