/**
 * Created by chengl on 2018/1/5 0005.
 */
var accountTable = null;
var deptTree = null;
var navTree = null;
var depttreeobj = null;
var navtreeobj = null;
// var crc32_table = new Array();
$(function () {
    initEvent();
    getLegalTime();
    getlowerData(0, 20);
    getLogCollect();
    $(window).click(function () {
        $(".from-style input[type=text]").css("border", "1px solid #E9E9E9");
    });
    $("body #menus").resize(function () {
        accountTable.ajax.reload();
    });
});

function initEvent() {
    $("body")
    //tab页导航
        .on("click", ".titleTab li", function () {
            $(this).addClass("titleTabactive").siblings("li").removeClass("titleTabactive");
            var classcon = $(this).data("class");
            $("." + classcon + "con").show().siblings("div").hide();
            initdeptTable();
        })
        //删除用户
        .on('click', '.j-opt-hover-delete', function () {
            var id = $(this).attr('data-id');
            if (id == 1 || id == 2 || id == 3 || id == 4) {
                layer.msg("内置管理员不能删除！", {icon: 2});
                return
            }
            layer.confirm('确定要删除该账户吗？', {
                btn: ['确定', '取消']
            }, function () {
                var postData = {
                    userId: id
                };
                postAjax(ctx + '/systemSetting/user/deleteUser', postData, function (msg) {
                    if (msg.resultCode == 1) {
                        accountTable.ajax.reload(function () {
                        }, true);
                        layer.msg('删除成功！', {icon: 1});
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            });
        })
        //添加账户
        .on('click', '#bar_add_account', function () {
            layer.open({
                id: 'openWind',
                type: 1,
                title: '添加账户',
                content: $('#add_user_wind').html(),
                area: ['900px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-account-form").valid()) {
                        $($("#openWind input.error")[0]).focus();
                        return;
                    }
                    var deptnodes = $.grep(depttreeobj.getCheckedNodes(true), function (obj) {
                        return !obj.getCheckStatus().half
                    });
                    // var deptnodes = depttreeobj.getCheckedNodes(true);
                    var navnodes = navtreeobj.getCheckedNodes(true);
                    if (deptnodes.length == 0 || navnodes == 0) {
                        layer.msg('部门或者功能权限必选！', {icon: 2});
                        return;
                    }
                    var pass = $("#openWind input[name=password]").val();
                    $("input[name=password]").val(encrypt(pass).toUpperCase());

                    pass = $.trim(pass);
                    $("input[name=departmentListStr]").val(getnodesrt(deptnodes));
                    $("input[name=navigationListStr]").val(getnodesrt(navnodes));
                    var temp = $("#openWind form").serialize();
                    $.ajax({
                        type: 'post',
                        url: ctx + '/systemSetting/user/addOrUpdateUser',
                        data: $("#openWind form").serialize(),
                        success: function (msg) {
                            if (msg.resultCode == 1) {
                                layer.close(index);
                                accountTable.ajax.reload();
                                layer.msg('添加成功！', {icon: 1});
                            } else {
                                layer.msg('添加失败！' + (msg.resultMsg || ''), {icon: 2});
                            }
                        },
                        error: function () {
                            layer.msg('添加失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    })
                },
                success: function (layero, index) {
                    firstFocus();
                    initNavTree(1);
                    // 部门权限树
                    initDeptTree(0, 1);

                    // 校验
                    $('#openWind .j-add-account-form').validate({
                        rules: {
                            userName: {
                                required: true,
                                numchinese: true,
                            },
                            name: {
                                required: true,
                                numchinese: true,
                            },
                            password: {
                                required: true,
                                minlength: 6
                            },
                            repassword: {
                                equalTo: $('#openWind input[name=password]')
                            },
                            phone: {
                                isPhone: true
                            }
                        }
                    });
                }
            });
        })
        //编辑账户
        .on('click', '.j-opt-hover-edit', function () {
            var idx = $('.j-opt-hover-edit').index(this);
            // idx =idx+4;
            var id = $(this).attr('data-id');//这是账户的id
            var guid = $(this).attr('data-guid');//这是账户的id
            var only = $(this).attr('data-only');//这是账户的id
            if (id == 1 || id == 2 || id == 3 || id == 4) {
                layer.msg("内置管理员不能编辑", {icon: 2});
                return
            }
            layer.open({
                id: 'openWind',//这个地方会自动给弹出框添加一个id
                type: 1,
                title: '编辑账户',
                content: $('#add_user_wind').html(),
                area: ['900px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-account-form").valid()) {
                        $($("#openWind input.error")[0]).focus();
                        return;
                    }
                    if ($("#openWind #pass").val() != '' && $("#openWind #pass").val().length < 6) {
                        layer.msg('密码不能小于6位！', {icon: 2});
                        return false
                    }
                    var deptnodes = $.grep(depttreeobj.getCheckedNodes(true), function (obj) {
                        return !obj.getCheckStatus().half
                    });
                    // var deptnodes = depttreeobj.getCheckedNodes(true);
                    var navnodes = navtreeobj.getCheckedNodes(true);
                    if (deptnodes.length == 0 || navnodes == 0) {
                        layer.msg('部门或者功能权限必选！', {icon: 2});
                        return;
                    }
                    var pass = $("#openWind input[name=password]").val();
                    if (pass != '') {
                        $("input[name=password]").val(encrypt(pass).toUpperCase());
                        $("input[name=repassword]").val(encrypt(pass).toUpperCase());
                    }
                    $("#openWind input[name=departmentListStr]").val(getnodesrt(deptnodes));
                    $("#openWind input[name=navigationListStr]").val(getnodesrt(navnodes));

                    var temp = $("#openWind form").serialize();


                    $.ajax({
                        type: 'post',
                        url: ctx + '/systemSetting/user/addOrUpdateUser',
                        data: String(temp + '&id=' + id + '&guid=' + guid),
                        success: function (msg) {
                            if (msg.resultCode == 1) {
                                layer.close(index);
                                accountTable.ajax.reload();
                                layer.msg('修改成功！', {icon: 1});
                            } else {
                                layer.msg('修改失败！' + (msg.resultMsg || ''), {icon: 2});
                                layer.close(index);
                            }
                        },
                        error: function () {
                            layer.msg('修改错误！' + (msg.resultMsg || ''), {icon: 2});
                            layer.close(index);
                        }
                    })
                },
                success: function (layero, index) {
                    firstFocus();
                    initNavTree(1, guid);
                    $("#openWind select[name=readonly]").val(accountTable.ajax.json().data[idx].readonly);
                    $("#openWind select[name=roleType]").val(accountTable.ajax.json().data[idx].roleType);
                    // 部门权限树
                    initDeptTree(id);
                    $('#openWind input[name=userName]').val(accountTable.ajax.json().data[idx].userName);
                    $('#openWind input[name=name]').val(accountTable.ajax.json().data[idx].name);
                    $('#openWind input[name=phone]').val(accountTable.ajax.json().data[idx].phone);
                    // 校验
                    $('#openWind .j-add-account-form').validate({
                        rules: {
                            userName: {
                                required: true,
                                numchinese: true,
                            },
                            name: {
                                required: true,
                                numchinese: true,
                            },
                            password: {},
                            repassword: {
                                equalTo: $('#openWind input[name=password]')
                            },
                            phone: {
                                isPhone: true
                            }
                        }
                    });
                }
            });
        })
        //切换不同账户的导航权限
        .on('change', '#openWind #systemauthlist', function () {
            var value = $("#openWind #systemauthlist").find("option:selected").val();
            initNavTree(Number(value));
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
        //获取文件名放到text上
        .on('change', 'input[type=file]', function (e) {
            var file = $(this).val();
            var filenames = getfilename(file);
            $(this).siblings('input[type=text]').val(filenames);
        })
        //点击上传升级文件
        .on('click', '#updata', function () {
            if ($("input#clientUpdataPath").val() == '') {
                $("#clientUpdataTip").show();
                return false;
            }

        })
        //点击上传安装文件
        .on('click', '#install', function () {
            if ($("input#clientInstallPath").val() == '') {
                $("#clientInstallTip").show();
                return false;
            }
        })
        //双因子开关 ukey免密
        .on('change', '#factorSwitch', function () {
            var factorSwitch = true;
            if ($(this).is(":checked")) {
                factorSwitch = false;
            }
            postAjax(ctx + '/usbKey/updateUsbkeyPwdState', {isNeeded: factorSwitch}, function (msg) {
                if (msg.resultCode == 1) {
                }
                else {
                    layer.msg('配置失败！' + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
        //禁止截屏
        .on('change', '#screenshotSwitch', function () {
            var screenshotSwitch = 0;
            if ($(this).is(":checked")) {
                screenshotSwitch = 1;
            }
            postAjax(ctx + '/systemClient/updateForbidScreen', {enable: screenshotSwitch}, function (msg) {
                if (msg.resultCode == 1) {
                }
                else {
                    layer.msg('配置失败！' + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
        //点击上传水印图片
        .on('click', '#watermark', function () {
            if ($("input#client_watermark_path").val() == '') {
                layer.msg("请先上传水印图片", {icon: 2});
                $(".from-style input[type=text]").css("border", "1px solid red");
                return false;
            }
        })
        //保存临时进程
        .on('click', '#save', function () {
            var val = $('input[name=temporary_processs]').val();

            var reg = /^([\u4e00-\u9fa5a-zA-Z0-9]*(\.exe|EXE)?(;)?)+$/;
            if (val != '') {
                if (reg.test(val)) {

                } else {
                    $("#tmplocalprocTip").html("进程名称不合法");
                    return false;
                }
            }
        })
        //动态验证码
        .on('click', '#generateCode', function () {
            var val = $('input[name=authcode]').val();
            reg = /^\d{6}$/;
            if (reg.test(val)) {
                postAjax(ctx + '/systemSetting/generatorCrc32', {code: val}, function (msg) {
                    if (msg.resultCode == 1) {
                        layer.alert('<span class="error">' + msg.data + '</span>', {title: '卸载码'});
                    }
                })
            } else {
                layer.msg('请输入六位请求码', {icon: 7});
            }
        })
        // 日志清理
        .on('click', '.clearcon .wind-row input[type=button]', function () {
            var dateval = $(this).siblings('select').find('option:selected').val();
            var type = $(this).data('type');
            var title = $(this).data('title');
            layer.confirm('日志清理后数据无法恢复！<br/>确定要清理<span style="color: red">' + dateval + '</span>天以前的' + title + '吗？', {
                btn: ['确定', '取消'], title: '提示'
            }, function () {
                if (type == "app") {
                    postAjax(ctx + '/report/deleteVideoTransferLog', {clearDays: dateval}, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.msg("清理成功！", {icon: 1});
                        } else {
                            layer.msg("清理失败！" + (msg.resultMsg || ''), {icon: 2});
                        }
                    });
                } else {
                    postAjax(ctx + '/systemLog/deleteOperationLog', {
                        clearDays: dateval,
                        logType: type
                    }, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.msg("清理成功！", {icon: 1});
                        } else {
                            layer.msg("清理失败！" + (msg.resultMsg || ''), {icon: 2});
                        }
                    });

                }

            });
        })
        //点击水印解码增加颜色
        .on('click', '.dot-box .dot', function () {
            if ($(this).hasClass('dot-check')) {
                $(this).removeClass('dot-check');
            } else {
                $(this).addClass('dot-check');
            }
        })
        //清空选中的小点点
        .on('click', '#clear', function () {
            $('.decodecon-left .decodecon-content .dot').removeClass('dot-check');
            $('.decodecon-right .decodecon-content').html('');
        })
        //解码
        .on('click', '#decode', function () {
            var decodeId = 's';
            $(".decodecon-left .decodecon-content .dot").each(function (index, val) {
                if ($(this).hasClass('dot-check')) {
                    decodeId += '1';
                } else {
                    decodeId += '0';
                }
            });
            getAjax(ctx + '/scrnwatermark/getScrnwatermarkLogByLogId', {logId: decodeId}, function (msg) {
                if (msg.resultCode == 1) {
                    var applyInfo = JSON.parse(msg.data.applyInfo);
                    applyInfo.applyTime = msg.data.applyTime;
                    $('.decodecon-right .decodecon-content').html(template('show_tem', applyInfo));

                } else if (msg.resultCode == 0) {
                    layer.confirm('无此信息', {
                        btn: ['确定']
                    }, function (index) {
                        layer.close(index);
                    })
                } else {
                    layer.msg("查询错误" + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
        //合法登陆时间
        .on('click', '.j-login-time input[type=checkbox]', function () {
            if ($(".j-login-time-week input[type=checkbox]:checked").length == 0) {
                layer.msg('必须选择一天！', {icon: 2});
                $(this).prop("checked", "checked");
                return false;
            } else {
                saveLegalTime();
            }

        })
        //定时采集开关
        .on('change', '#timingSwitch', function () {
            var factorSwitch = 0;
            if ($(this).is(":checked")) {
                factorSwitch = 1;
            }
            putAjax(ctx + '/scheduledTask/turnScheduledTaskSwitch', {
                "id": 1,
                "taskSwitch": factorSwitch
            }, function (msg) {
                if (msg.resultCode == 1) {
                    layer.msg('更新定时采集成功！', {icon: 1});
                }
                else {
                    layer.msg('更新合法时间失败！' + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
        //定时采集开关
        .on('change', '#collectTime', function () {
            saveLogCollect();
        })
        // 下级单位添加
        .on("click", "#bar_lower_add", function () {
            var nameval = $.trim($("input[name=lowername]").val());
            var ipval = $.trim($("input[name=lowerip]").val());
            //校验
            $('body form#lowerform').validate({
                rules: {
                    lowername: {
                        required: true,
                        notAllSpace: true,
                    },
                    lowerip: {
                        required: true,
                        isIp: true,
                        notAllSpace: true,
                    }
                }
            });
            if (!$('body form#lowerform').valid()) {
                return;
            }
            if(ipval == '0.0.0.0'||ipval == '1.1.1.1'||ipval == '255.255.255.255'){
                layer.msg('IP地址不合理！', {icon: 7});
                return false;
            }
            var data = {nodeName: nameval, nodeIp: ipval};
            if (checkLowerValue(nameval) && checkLowerPath(ipval)) {
                if ($("body .lowerShow .lowerShowList").length != 12) {
                    postAjax(ctx + '/childNode/childNode', data, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.msg('添加成功！', {icon: 1});
                            $("input[name=lowername]").val('');
                            $("input[name=lowerip]").val('');
                            getlowerData();
                        } else {
                            layer.msg('添加失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    });
                } else {
                    layer.msg('最多只能添加12条！', {icon: 2});
                    return false;
                }
            } else {
                layer.msg('请勿重复添加！', {icon: 2});
                return false;
            }
        })
        // 下级单位删除
        .on("click", "#lowerClose", function () {
            var name = $(this).parents(".lowerShowList").data("name");
            var startPath = $(this).parents(".lowerShowList").attr("data-nodeIp");
            var id = $(this).parents(".lowerShowList").attr("data-id");
            var idx = $(this).parents(".lowerShowList").index();
            layer.confirm("是否确定删除？", {
                btn: ['确定', '取消']
            }, function () {
                deleteAjax(ctx + '/childNode/childNode/' + id, "", function (msg) {
                    if (msg.resultCode == 1) {
                        layer.msg('删除成功！', {icon: 1});
                        $(".lowerShow .lowerShowList").eq(idx).remove();
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            })
        })
        // 下级单位
        .on("mouseover", ".lowerShowList", function () {
            $(this).addClass("lowerShowList-hover");
        })
        .on("mouseleave", ".lowerShowList", function () {
            $(this).removeClass("lowerShowList-hover");
        })
    $('body .startTime').datetimepicker({
        format: 'hh:ii',
        autoclose: true,
        startView: 1,
        minView: 0,
        maxView: 0,
        minuteStep: 30
    }).on('changeDate', function (ev) {
        $('body .endTime').datetimepicker('show');
        saveLegalTime();
        //在这传入
    }).on('hide', function (ev) {


    });
    $('body .endTime').datetimepicker({
        format: 'hh:ii',
        autoclose: true,
        startView: 1,
        minView: 0,
        maxView: 0,
        minuteStep: 30
    }).on('show', function (ev) {


    }).on('hide', function (ev) {


    }).on('changeDate', function (ev) {
        saveLegalTime();

    });
    $('body .collectTimeDay').datetimepicker({
        format: 'hh:ii:00',
        autoclose: true,
        startView: 1,
        minView: 0,
        maxView: 0,
        minuteStep: 30
    }).on('show', function (ev) {


    }).on('hide', function (ev) {


    }).on('changeDate', function (ev) {
        saveLogCollect();
    });

}

//下级单位获取
function getlowerData() {
    getAjax(ctx + "/childNode/list", "", function (msg) {
        if (msg.resultCode == 1) {
            $(".lowerShow").html("");
            $(".lowerShow").html(template("lowerList", msg.data));
        }
    })
}

// 验证是否重复
function checkLowerValue(name) {
    var fl = true;
    $("body .lowerShow .lowerShowList>span").each(function () {
        // debugger
        if (name == $(this).text()) {
            fl = false;
            return
        }
    });
    return fl;
}

// 验证是否重复
function checkLowerPath(ip) {
    var fl = true;
    $("body .lowerShow .lowerShowList .con span").each(function () {
        // debugger
        if (ip == $(this).text()) {
            fl = false;
            return
        }
    });
    return fl;
}

//定时采集回显
function getLogCollect() {
    getAjax(ctx + '/scheduledTask/scheduledTask/1', '', function (msg) {
        if (msg.resultCode == 1) {
            $(".cascadecon #collectTime").val(msg.data.period);
            $(".cascadecon .collectTimeDay").val(msg.data.time);
            if (msg.data.taskSwitch == "1") {
                $($(".cascadecon input[name=timingSwitch]")).prop("checked", "checked");
            }
        }
        else {
            layer.msg('获取定时采集失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
}

//定时采集保存
function saveLogCollect() {

    var allData = new Object();
    var collectTime = $(".cascadecon .collectTimeDay").val();
    var collectPeriod = $(".cascadecon #collectTime option:selected").val();

    allData.collectTime = collectTime;
    allData.collectPeriod = collectPeriod;
    allData.id = 1;
    putAjax(ctx + '/scheduledTask/triggerTask', allData, function (msg) {
        if (msg.resultCode == 1) {
            layer.msg('更新定时采集成功！', {icon: 1});
        }
        else {
            layer.msg('更新合法时间失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
}

//客户端上传

$('#updataform').ajaxForm({
    uploadProgress: function (event, position, total, percentComplete) {
        var percentVal = percentComplete + '%';
        $('#clientUpdataTip').show();
        $('#clientUpdataTip').html(percentVal);
    },
    success: function (msg) {
        if (msg.resultCode == '1') {
            layer.msg("上传成功", {
                icon: 1,
                end: function () {
                }
            });
        } else {
            layer.msg("上传失败！" + (msg.resultMsg || ''), {icon: 2});
        }
    },
    error: function () {
        layer.msg("上传错误！", {icon: 2});
    },
    complete: function (xhr) {
        $('#clientUpdataTip').hide();
    }
});
//客户端上传
$('#installform').ajaxForm({
    uploadProgress: function (event, position, total, percentComplete) {
        var percentVal = percentComplete + '%';
        $('#clientInstallTip').show();
        $('#clientInstallTip').html(percentVal);
    },
    success: function (msg) {
        if (msg.resultCode == '1') {
            layer.msg("上传成功", {
                icon: 1,
                end: function () {
                }
            });
        } else {
            layer.msg("上传失败！" + (msg.resultMsg || ''), {icon: 2});
        }
    },
    error: function () {
        layer.msg("上传错误！", {icon: 2});
    },
    complete: function (xhr) {
        $('#clientInstallTip').hide();
    }
});
//水印上传
$('#watermarkform').ajaxForm({
    uploadProgress: function (event, position, total, percentComplete) {
        // var percentVal = percentComplete + '%';
        $('.screen-tip').show();
        // $('#clientUpdataTip').html(percentVal);
    },
    success: function (msg) {
        if (msg.resultCode == '1') {
            if (msg.data[0] == 's') {
                $(".decodecon-left .decodecon-content .dot").each(function (index, val) {
                    if (msg.data[index + 1] == 1) {
                        $(this).addClass('dot-check')
                    } else if (msg.data[index + 1] == 0) {
                        $(this).removeClass('dot-check')
                    }
                });
                getAjax(ctx + '/scrnwatermark/getScrnwatermarkLogByLogId', {logId: msg.data}, function (msg) {
                    if (msg.resultCode == 1) {
                        var applyInfo = JSON.parse(msg.data.applyInfo);
                        applyInfo.applyTime = msg.data.applyTime;
                        $('.decodecon-right .decodecon-content').html(template('show_tem', applyInfo));

                    } else if (msg.resultCode == 0) {
                        layer.confirm('无此信息', {
                            btn: ['确定']
                        }, function (index) {
                            layer.close(index);
                        })
                        $(".decodecon-left .decodecon-content .dot").each(function (index, val) {
                            $(this).removeClass('dot-check')
                        });
                        $('.decodecon-right .decodecon-content').html("");
                    } else {
                        layer.msg("查询错误" + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            } else {
                layer.msg(("分析失败！请更换图片清晰度"), {icon: 2});
            }
        } else {
            layer.msg("解析失败！" + (msg.resultMsg || ''), {icon: 2});
        }
    },
    error: function () {
        layer.msg("解析错误！", {icon: 2});
    },
    complete: function (xhr) {
        $('.screen-tip').hide();
    }
});

//账户用户表
function initdeptTable() {
    if (accountTable) {
        accountTable.ajax.reload();
        return;
    }
    accountTable = $('#accountTable').DataTable({ //表格初始化

        "ajax": {
            "beforeSend": function () {
            },
            "url": ctx + "/systemSetting/user/getUserPages",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.name, obj.userName, obj.roleType, obj.readonly, obj.phone || '--', {
                        id: obj.id,
                        guid: obj.guid,
                        only: obj.readonly
                    }];
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {},
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            "class": "text-ellipsis",

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
                    return '<span>内置管理员</span>';
                } else if (data == 1) {
                    return '<span>系统管理员</span>';
                } else if (data == 2) {
                    return '<span>系统操作员</span>';
                } else if (data == 3) {
                    return '<span>系统审计员</span>';
                }
            }
        }, {
            "targets": [3],
            "orderable": false,
            "class": "text-ellipsis",
            "render": function (data, type, full) {
                if (data == 0) {
                    return '<span>读写权限</span>';
                } else if (data == 1) {
                    return '<span>只读权限</span>';
                }
            }
        }, {
            "targets": [4],
            "orderable": false,
            "class": "text-ellipsis"
        }, {
            "targets": [5],
            "orderable": false,
            "class": "center-text",
            "width": "100px",
            "render": function (data, type, full) {
                return template('temp_opt_box', {id: data.id, guid: data.guid, only: data.only});
            }
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'accountTable';
            tableCallback(ele);

        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}

//获取所有部门表
function initDeptTree(id, isshow) {
    var setting1 = {
        view: {
            dblClickExpand: false,
            showLine: true,
            showIcon: false
        },
        check: {
            enable: true,
            nocheckInherit: false,
            chkboxType: {"Y": "ps", "N": "ps"}
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
            }
        }
    };
    getAjax(ctx + '/department/getDepartmentTreeByUserId', {userId: id}, function (msg) {
        if (msg.resultCode == 1) {
            var zNodes1 = eval(msg.data);
            if (deptTree) {
                deptTree.destroy();
            }
            deptTree = $.fn.zTree.init($("body #depttree"), setting1, zNodes1);
            depttreeobj = $.fn.zTree.getZTreeObj("depttree");
            depttreeobj.expandAll(true);
            if (isshow == 1) {
                depttreeobj.checkAllNodes(true);
            }

        }
        else {
            layer.msg('获取部门列表失败', {icon: 2});
        }
    });
}

//获取所有权限tree
function initNavTree(id, guid) {
    // 获取权限树
    var setting2 = {
        view: {
            dblClickExpand: false,
            showLine: true,
            showIcon: false
        },
        check: {
            enable: true,
            nocheckInherit: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
            }
        }
    };
    var guidid = guid ? guid : ''
    getAjax(ctx + '/system/navigation/getNavigationListByRoleType', {roleType: id, guid: guidid}, function (msg) {
        if (msg.resultCode == 1) {
            var navzNodes = eval(msg.data);
            if (navTree) {
                navTree.destroy();
            }
            navTree = $.fn.zTree.init($("body #navtree"), setting2, navzNodes);
            navtreeobj = $.fn.zTree.getZTreeObj("navtree");
            navtreeobj.expandAll(true);
        }
        else {
            layer.msg('获取权限失败', {icon: 2});
        }
    });

}

//合法时间段回显 usbkey 双因子登录  禁止截屏回显
function getLegalTime() {
    getAjax(ctx + '/systemClient/isForbidScreen', '', function (msg) {
        if (msg.resultCode == 1) {
            if (msg.data == 0) {

            } else {
                $("#screenshotSwitch").prop("checked", "checked");
            }
        }
        else {
            layer.msg('获取失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
    getAjax(ctx + '/usbKey/isUsbkeyPwdNeeded', '', function (msg) {
        if (msg.resultCode == 1) {
            if (msg.data) {

            } else {
                $("#factorSwitch").prop("checked", "checked");
            }
        }
        else {
            layer.msg('获取失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
    getAjax(ctx + '/systemClient/getClientLegalTime', '', function (msg) {
        if (msg.resultCode == 1) {
            $(".j-login-time .startTime").val(msg.data.begin);
            $(".j-login-time .endTime").val(msg.data.end);
            var array = msg.data.week.split(";");
            for (var i = 0; i < array.length; i++) {
                $($(".j-login-time input[type=checkbox]")[array[i]]).prop("checked", "checked");
            }
            if (msg.data.sound == "true") {
                $($(".j-login-time input[type=checkbox]")[0]).prop("checked", "checked");
            }
        }
        else {
            layer.msg('获取合法登陆时间失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
}

//合法时间段的保存
function saveLegalTime() {
    var allData = new Object();
    var week = new String();
    var begin = $(".j-login-time .startTime").val();
    var end = $(".j-login-time .endTime").val();
    if ((Number(begin.substring(0, 2)) - Number(end.substring(0, 2))) >= 0) {//这是小时
        if ((Number(begin.substring(3, 4)) - Number(end.substring(3, 4))) >= 0) {//这是分钟
            layer.msg('保存失败,结束时间晚于开始时间！', {icon: 2});
            return false;
        }
    }
    $(".j-login-time input[type=checkbox]").each(function (index, ele) {

        if (index == 0) {
            if ($(ele).is(":checked")) {
                allData.sound = "true";
            } else {
                allData.sound = "false";
            }
        } else {
            if ($(ele).is(":checked")) {

                week += $(ele).val() + ";";
            } else {

            }
        }

    });
    allData.week = week.substring(0, week.length - 1);
    allData.begin = begin;
    allData.end = end;
    postAjax(ctx + '/systemClient/updateClientLegalTime', allData, function (msg) {
        if (msg.resultCode == 1) {
            // layer.msg('成功！' , {icon: 1});
        }
        else {
            layer.msg('更新合法时间失败！' + (msg.resultMsg || ''), {icon: 2});
        }
    });
}

//获取tree的字符串字段
function getnodesrt(nodes) {
    var nodestr = '';
    for (var i = 0; i < nodes.length; i++) {
        if (i == nodes.length - 1) {
            nodestr += nodes[i].id
        } else {
            nodestr += nodes[i].id + ','
        }
    }
    return nodestr;
}

changeq("clientUpdataPath", "clientUpdata");
changeq("clientInstallPath", "clientInstall");

function changeq(a, b) {
    var copyFile = document.getElementById(a);
    var trueFile = document.getElementById(b);
    trueFile.onchange = function () {
        // 判断是不是火狐
        if (navigator.userAgent.indexOf('Firefox') >= 0) {
            copyFile.value = getFile(this);
        } else {
            copyFile.value = getFile(this).substring(12);
        }
    }

    function getFile(obj) {
        if (obj) {
            return obj.value;
        }
    }
}
