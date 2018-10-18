/**
 * Created by chengl on 2018/1/3 0003.
 */
$(function () {
    //初始化事件
    initEvents();
    //定时刷新状态
    // refreshStatus();
});

/**
 * 初始化事件
 */
function initEvent() {
    $("#access-form").html(template('accessTem', accessObj));
    if ($("body #onoff").is(":checked")) {
        $("body input[name=ctrlAreas-x]").siblings('span.mustlabel').show();
    } else {
        $("body input[name=ctrlAreas-x]").siblings('span.mustlabel').hide();
    }
    $('#access-form form').validate({
        rules: {
            nacMac: {
                mac: true
            },
            nacUrl:{
                required:false,
                httpurl:true
            }

        },
        messages: {
            nacMac: {
                mac: "MAC格式不正确",
            },
            /*nacUrl:{
                httpurl:"地址格式不正确"
            }*/
        },
    });

}

function initEvents() {
    initEvent();
    $('body')
    //准入配置
        .on('click', '.config', function () {
            layer.open({
                id: 'openWind',
                type: 1,
                area: ['640px', '550px'],
                btn: ['确定', '取消'],
                title: '准入配置',
                zIndex: 100,
                content: $('.edit-server-wind').html(),
                yes: function (index, layero) {
                    //mac
                    if (!$('#openWind form').valid()) {
                        return;
                    }
                    //准入控制网段
                    var flag = true;
                    el = $(layero).find('input[name=ctrlAreas-x]');
                    if ($(".layui-layer-content").find(".switch-btn").hasClass("on")) {
                        if ($(layero).find('input[name=ctrlAreas-x]').length == 0) {
                            $(el).focus();
                            $(el).closest('.row').find('.error-text').text('至少要有一个准入控制网段').show();
                            flag = false;
                        }
                    } else {
                    }

                    if (!flag) {
                        return;
                    }
                    if ($('.layui-layer-btn0').attr('disabled') == 'true') {
                        return;
                    }
                    $('.layui-layer-btn0').attr('disabled', true);
                    $.ajax({
                        type: 'post',
                        url: ctx + '/access/editconfig',
                        data: $(layero).find('form').serialize(),
                        success: function (msg) {
                            $('.layui-layer-btn0').removeAttr('disabled');
                            if (msg === 'success') {
                                layer.close(index);
                                layer.msg('配置成功！', {icon: 1});
                            } else {
                                layer.msg('配置失败！', {icon: 2});
                            }
                        },
                        error: function (e) {
                            $('.layui-layer-btn0').removeAttr('disabled');
                            layer.msg('配置失败！', {icon: 2});
                        }
                    })
                },

                btn2: function (index, layero) {
                    return true;
                },
                success: function (layero, index) {
                    $('#openWind').addClass('layer-bottom-line');
                    if ($('#status').text() === '已开启') {
                        $('#status-switch .switch-btn').addClass('on');
                        $('#status-switch input[type=checkbox]').prop("checked", true);
                        //$(".macaddress").addClass("required");
                        $(".layui-layer-content").find(".mustinput").addClass("must");
                    } else {
                        $('#status-switch .switch-btn').removeClass('on');
                        $('#status-switch input[type=checkbox]').prop("checked", false);
                        //$(".macaddress").removeClass("required");
                        $(".layui-layer-content").find(".mustinput").removeClass("must");
                    }
                    $(".switch-btn").click(function () {
                        if ($(".layui-layer-content").find(".switch-btn").hasClass("on")) {
                            //$(".macaddress").removeClass("required");
                            $(".mustinput").removeClass("must");
                        } else {
                            //$(".macaddress").addClass("required");
                            $(".mustinput").addClass("must");
                        }
                    })
                    if ($.trim($('#nac_mac').text()) !== '未配置') {
                        $('#openWind input[name=nacMac]').val(nacMac)
                    }
                    if ($.trim($('#nac_url').text()) !== '未配置') {
                        $('#openWind input[name=nacUrl]').val(nacUrl)
                    }
                    for (var i = 0; i < ctrlAreas.length; i++) {
                        $('#openWind .ctrl-area').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + ctrlAreas[i] + '" type="text" name="ctrlAreas" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    for (var i = 0; i < legalIps.length; i++) {
                        $('#openWind .legal-ip').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + legalIps[i] + '" type="text" name="legalIps" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    for (var i = 0; i < legalPorts.length; i++) {
                        $('#openWind .legal-port').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + legalPorts[i] + '" type="text" name="legalPorts" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    for (var i = 0; i < ilegalIps.length; i++) {
                        $('#openWind .ilegal-ip').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + ilegalIps[i] + '" type="text" name="ilegalIps" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    for (var i = 0; i < ilegalPorts.length; i++) {
                        $('#openWind .ilegal-port').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + ilegalPorts[i] + '" type="text" name="ilegalPorts" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    for (var i = 0; i < httpPorts.length; i++) {
                        $('#openWind .http-port').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + httpPorts[i] + '" type="text" name="httpPorts" readonly><i class="iconfont icon-guanbi del-param"></i></div>');
                    }
                    $('#openWind input[name=nacServer]').val($.trim($('#ip').text()));
                    $('#openWind form').validate({
                        rules: {
                            nacMac: {
                                mac: true
                            },
                        },
                        messages: {
                            nacMac: {
                                mac: "MAC格式不正确",
                            },
                        },
                    });
                }
            });
        })
        //添加准入控制网段
        .on('click', '.add-ctrl-area', function () {
            var el = $('#access-form input[name=ctrlAreas-x]');
            var val = $.trim($(el).val());
            if (checkIp(val)) {
                $('#access-form .ctrl-area').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + val + '" type="text" name="ctrlAreas" readonly><i class="iconfont icon-btn-close del-param"></i></div>');
                $(el).val('').focus();
            } else {
                if (val !== '') {
                    $(el).focus();
                    $(el).siblings('span.error-text').text('IP地址格式不正确').show();
                } else {
                    $(el).siblings('span.error-text').text('不能为空').show();
                }
            }
        })
        //添加白名单IP
        .on('click', '.add-legal-ip', function () {
            var el = $('#access-form input[name=legalIps-x]')
            var val = $.trim($(el).val());
            if (checkIp(val) && val != '0.0.0.0' && val != '255.255.255.255') {
                $('#access-form .legal-ip').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + val + '" type="text" name="legalIps" readonly><i class="iconfont icon-btn-close del-param"></i></div>');
                $(el).val('').focus();
            } else {
                if (val !== '') {
                    $(el).focus();
                    if (val == '255.255.255.255' || val == '0.0.0.0') {
                        $(el).siblings('span.error-text').text('IP地址不能为边界值').show();
                    } else {
                        $(el).siblings('span.error-text').text('IP地址格式不正确').show();
                    }


                } else {
                    $(el).siblings('span.error-text').text('不能为空').show();
                }
            }
        })
        //添加白名单端口
        .on('click', '.add-legal-port', function () {
            var el = $('#access-form input[name=legalPorts-x]')
            var val = $.trim($(el).val());
            if (portValidate(val) || portAreasValidate(val)) {
                $('#access-form .legal-port').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + val + '" type="text" name="legalPorts" readonly><i class="iconfont icon-btn-close del-param"></i></div>');
                $(el).val('').focus();
            } else {
                if (val !== '') {
                    $(el).focus();
                    $(el).siblings('span.error-text').text('0~65535之间的整数').show();
                } else {
                    $(el).siblings('span.error-text').text('不能为空').show();
                }
            }
        })
        //添加HTTP端口
        .on('click', '.add-http-port', function () {
            var el = $('#access-form input[name=httpPorts-x]')
            var val = $.trim($(el).val());
            if (portValidate(val) || portAreasValidate(val)) {
                $('#access-form .http-port').append('<div class="append-param-box"><input class="add-param-val medium-input append" value="' + val + '" type="text" name="httpPorts" readonly><i class="iconfont icon-btn-close del-param"></i></div>');
                $(el).val('').focus();
            } else {
                if (val !== '') {
                    $(el).focus();
                    $(el).siblings('span.error-text').text('0~65535之间的整数').show();
                } else {
                    $(el).siblings('span.error-text').text('不能为空').show();
                }
            }
        })
        //删除参数
        .on('click', '.del-param', function () {
            $(this).closest('.append-param-box').remove();
        })
        //隐藏错误信息
        .on('focus', '#access-form input[type=text]', function () {
            $(this).siblings('span.error-text').hide();
        })
        //隐藏错误信息
        .on('input', '#access-form input[type=text]', function () {
            $(this).siblings('span.error-text').hide();
        })
        //准入状态开关
        .on('change', '#onoff', function () {
            if ($(this).is(":checked")) {
                $("body input[name=ctrlAreas-x]").siblings('span.mustlabel').show();
                $("#add_ctrl_area").addClass("add-ctrl-area").removeClass("color-grey");
                $("#add_legal_port").addClass("add-legal-port").removeClass("color-grey");
                $("#add_legal_ip").addClass("add-legal-ip").removeClass("color-grey");
                $("input[name='nacMac']").removeAttr("readonly");
                $("input[name='nacUrl']").removeAttr("readonly");
            } else {
                $("body input[name=ctrlAreas-x]").siblings('span.mustlabel').hide();
                $("#add_ctrl_area").removeClass("add-ctrl-area").addClass("color-grey");
                $("#add_legal_port").removeClass("addadd-legal-port").addClass("color-grey");
                $("#add_legal_ip").removeClass("add-legal-ip").addClass("color-grey");
                $(".error-text").hide();
                $("input[name='nacMac']").attr("readonly","readonly").val('');
                $("input[name='nacUrl']").attr("readonly","readonly").val('');
            }
        })
        //点击进行保存
        .on('click', '.access-save', function () {
            //mac
            if (!$('#access-form form').valid()) {
                return;
            }
            if ($("#onoff").is(":checked")) {
                var val = $("input[name=ctrlAreas-x]").val();
                if ($("input[name=ctrlAreas-x]").siblings(".append-box").find(".append-param-box").length == 0) {
                    $("input[name=ctrlAreas-x]").siblings('span.error-text').text('至少一个准入网段').show();
                    return
                }
            }
            //保存
            $.ajax({
                type: 'post',
                url: ctx + '/access/saveConfig',
                data: $("#access-form").find('form').serialize(),
                success: function (msg) {
                    if (msg.resultCode == '1') {
                        layer.msg('配置成功！', {icon: 1});
                    } else {
                        layer.msg('配置失败！', {icon: 2});
                    }
                },
                error: function (e) {
                    layer.msg('配置失败！', {icon: 2});
                }
            })


        })
}

/**
 * 较验端口
 */
function portValidate(val) {
    //端口正则
    var portReg = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
    return portReg.test(val);
}

/**
 * 较验端口段
 */
function portAreasValidate(val) {
    //端口段正则
    var portAreasReg = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])\-([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
    if (portAreasReg.test(val)) {
        var temp = val.split('-');
        return Number(temp[0]) < Number(temp[1])
    }
    return false;
}

/**
 * 定时刷新状态
 */
function refreshStatus() {
    setInterval(getStatus, 1000)
}

/**
 * 获取服务器状态
 */
function getStatus() {
    if (typeof status != 'undefined') {
        $.ajax({
            type: 'get',
            url: ctx + '/access/readAccessServerConfig',
            success: function (data) {
                $('.list-item').each(function (i, el) {
                    $.each(data, function (j, obj) {
                        if ($(el).attr('data-ip') == obj.ip) {
                            if ($(el).hasClass('online') !== obj.status) {
                                if (obj.status) {
                                    $(el).addClass('online')
                                } else {
                                    $(el).removeClass('online')
                                }
                            }
                            return false;
                        }
                    });
                });
                $.each(data, function (index, obj) {
                    if ($('.list-item.active').attr('data-ip') == obj.ip) {
                        if (obj.config) {
                            var config = JSON.parse(obj.config);
                            if (config.nac_open) {
                                if (config.nac_open === '1') {
                                    status = true;
                                } else {
                                    status = false;
                                }
                            }
                            if (config.nac_mac) {
                                nacMac = config.nac_mac;
                            } else {
                                nacMac = '';
                            }
                            if (config.url) {
                                nacUrl = config.url;
                            } else {
                                nacUrl = '';
                            }
                            if (config.ctrl_area) {
                                ctrlAreas = [];
                                for (var key in config.ctrl_area) {
                                    ctrlAreas.push(key)
                                }
                            } else {
                                ctrlAreas = [];
                            }
                            if (config.legal_ip) {
                                legalIps = [];
                                for (var key in config.legal_ip) {
                                    legalIps.push(key)
                                }
                            } else {
                                legalIps = [];
                            }
                            if (config.legal_port) {
                                legalPorts = [];
                                for (var key in config.legal_port) {
                                    legalPorts.push(key)
                                }
                            } else {
                                legalPorts = [];
                            }
                            if (config.ilegal_ip) {
                                ilegalIps = [];
                                for (var key in config.ilegal_ip) {
                                    ilegalIps.push(key)
                                }
                            } else {
                                ilegalIps = [];
                            }
                            if (config.ilegal_port) {
                                ilegalPorts = [];
                                for (var key in config.ilegal_port) {
                                    ilegalPorts.push(key)
                                }
                            } else {
                                ilegalPorts = [];
                            }
                            if (config.http_port) {
                                httpPorts = [];
                                for (var key in config.http_port) {
                                    httpPorts.push(key)
                                }
                            } else {
                                httpPorts = [];
                            }
                            refreshPage();
                        }
                    }

                });
            },
            error: function (e) {
            }
        })
    }
    ;
}
