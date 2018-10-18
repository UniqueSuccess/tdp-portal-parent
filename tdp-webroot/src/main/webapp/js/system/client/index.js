/**
 * Created by chengl on 2018/1/8 0008.
 */
$(function () {
    initEvent();
    getNetData(0, 20);
    getlogoData(0, 20);
});

// 初始化事件
function initEvent() {
    $("body")
    // 业务网络添加
        .on("click", "#bar_net_add", function () {
            var netval = $.trim($("input[name=netip]").val());
            //校验
            $('body form#netform').validate({
                rules: {
                    netip: {
                        required: true,
                        isIpRange: true,
                        notAllSpace: true,
                    },
                }
            });
            if (!$('body form#netform').valid()) {
                return;
            }
            if (checkValue(netval)) {
                if ($("body .netShow .netShowList").length != 12) {
                    postAjax(ctx + '/systemClient/addVedioNetAccess', {ip: netval}, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.msg('添加成功！', {icon: 1});
                            $("input[name=netip]").val('');
                            getNetData(0, 20);
                        } else {
                            layer.msg('保存失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    });
                } else {
                    layer.msg('最多只能添加12条！', {icon: 2});
                }

            } else {
                layer.msg('请勿重复添加！', {icon: 2});
                return;
            }
        })
        // 业务网络删除
        .on("click", "#netClose", function () {
            var ip = $(this).parents(".netShowList").data("ip");
            var idx = $(this).parents(".netShowList").index();
            layer.confirm("是否确定删除？", {
                btn: ['确定', '取消']
            }, function () {
                postAjax(ctx + '/systemClient/deleteVedioNetAccess', {ip: ip}, function (msg) {
                    if (msg.resultCode == 1) {
                        layer.msg('删除成功！', {icon: 1});
                        $(".netShow .netShowList").eq(idx).remove();
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            })
        })
        // 登陆方式添加
        .on("click", "#bar_logo_add", function () {
            var nameval = $.trim($("input[name=logoname]").val());
            var ipval = $.trim($("input[name=logoip]").val());
            // var idx = $(this).parents(".logoShowList").index();
            // var startmethod = $(this).parents(".logoShowList").data("method");
            // startmethod?startmethod:0;
            //校验
            $('body form#logoform').validate({
                rules: {
                    logoname: {
                        required: true,
                        notAllSpace: true,
                        // maxlength:20
                    },
                    logoip: {
                        required: true,
                        notAllSpace: true,
                    }
                }
            });
            if (!$('body form#logoform').valid()) {
                return;
            }
            var data = {name: nameval, startPath: ipval};
            if (checkLogoValue(nameval) && checkLogoPath(ipval)) {
                if ($("body .logoShow .logoShowList").length != 12) {
                    postAjax(ctx + '/systemClient/addVedioLogonAccess', data, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.msg('添加成功！', {icon: 1});
                            $("input[name=logoname]").val('');
                            $("input[name=logoip]").val('');
                            getlogoData(0, 20)
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
        // 登陆删除
        .on("click", "#logoClose", function () {
            var name = $(this).parents(".logoShowList").data("name");
            var startPath = $(this).parents(".logoShowList").attr("data-startPath");
            var idx = $(this).parents(".logoShowList").index();
            layer.confirm("是否确定删除？", {
                btn: ['确定', '取消']
            }, function () {
                postAjax(ctx + '/systemClient/deleteVedioLogonAccess', {
                    name: name,
                    startPath: startPath
                }, function (msg) {
                    if (msg.resultCode == 1) {
                        layer.msg('删除成功！', {icon: 1});
                        $(".logoShow .logoShowList").eq(idx).remove();
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            })
        })
        // 业务网络访问管控
        .on("mouseover", ".netShowList", function () {
            $(this).find("i").show();
            $(this).css("background", "#38D1BF");
            $(this).find('span').css("color", "#fff")
        })
        .on("mouseleave", ".netShowList", function () {
            $(this).find("i").hide();
            $(this).css("background", "#ededed")
            $(this).find('span').css("color", "#333")
        })
        // 视频登陆方式管控
        .on("mouseover", ".logoShowList", function () {
            $(this).addClass("logoShowList-hover");
        })
        .on("mouseleave", ".logoShowList", function () {
            $(this).removeClass("logoShowList-hover");
        })

}

function getNetData(start, length) {
    getAjax(ctx + "/systemClient/getVedioNetAccessPages", {start: start, length: length}, function (msg) {
        if (msg.resultCode == 1) {
            $(".netShow").html();
            $(".netShow").html(template("netList", msg.data));
        }
    })
}

function getlogoData(start, length) {
    getAjax(ctx + "/systemClient/getVedioLogonAccessPages", {start: start, length: length}, function (msg) {
        if (msg.resultCode == 1) {
            var reg1 = /\w*.exe$/;
            if(msg.data.length>0){
                for (var i = 0;i<msg.data.length;i++){
                    if(reg1.test(msg.data[i].startPath)){
                        msg.data[i].type = 1;
                    }else{
                        msg.data[i].type = 0;
                    }
                }
            }
            $(".logoShow").html("");
            $(".logoShow").html(template("logoList", msg.data));
        }
    })
}

function checkValue(ip) {
    var fl = true;
    $("body .netShow .netShowList span").each(function () {
        // debugger
        if (ip == $(this).text()) {
            fl = false;
            return
        }
    });
    return fl;
}

function checkLogoValue(name) {
    var fl = true;
    $("body .logoShow .logoShowList>span").each(function () {
        // debugger
        if (name == $(this).text()) {
            fl = false;
            return
        }
    });
    return fl;
}

function checkLogoPath(path) {
    var fl = true;
    $("body .logoShow .logoShowList .con span").each(function () {
        // debugger
        if (path == $(this).text()) {
            fl = false;
            return
        }
    });
    return fl;
}