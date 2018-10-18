var deptTable = null;//部门用户表
var deptTree = null;//部门树
var policyList = null;//策略列表
var usbDeviceList = null;//没有用的usb设备列表
var fl = false;
var fingerNum = 1;
$(function () {
    initDeptTree(1);//初始化部门树,1是选中节点的id
    initEvents();//初始化页面事件
    getPolicyList();//获取所有的策略列表
    getEmUsbList();//获取空的usb设备
    cancekDevice();//让设备回到原始状态
    $("body #menus").resize(function () {
        deptTable.ajax.reload();
    });
    $("#bar_searchstr").focus();
});

function getPolicyList() {
    getAjax(ctx + '/policy/getAllPolicys', '', function (msg) {
        if (msg.resultCode == 1) {
            policyList = msg.data;
        }
        else {
            layer.msg('获取权限列表失败！', {icon: 2});
        }
    });
}

function getEmUsbList() {
    getAjax(ctx + '/usbKey/getAllUnbindUsbKey', '', function (msg) {
        if (msg.resultCode == 1) {
            usbDeviceList = msg.data;
        }
        else {
            layer.msg('获取usbKey失败！', {icon: 2});
        }
    });
}


function initEvents() {
    $('body')
    //删除用户
        .on('click', '.j-opt-hover-delete', function () {
            var id = $(this).attr('data-id');
            layer.confirm('确定要删除该用户吗？', {
                btn: ['确定', '取消']
            }, function () {
                var postData = {
                    id: id
                };
                postAjax(ctx + '/clientUser/deleteClientUser', postData, function (msg) {
                    if (msg.resultCode == 1) {
                        deptTable.ajax.reload(function () {
                        }, true);
                        layer.msg('删除成功！', {icon: 1});
                        $('.j-check-dept-all').prop('checked', false);
                        //更新部门树
                        getAjax(ctx + '/department/getDepartmentNodesByLoginUser', '', function (msg) {
                            zNodes = JSON.parse(msg);
                            var selectID = deptTree.getSelectedNodes()[0].id;
                            deptTree.destroy();
                            initDeptTree(selectID);
                            initdeptTable(selectID);
                        })
                    }
                    else {
                        layer.msg('删除失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            });
        })
        //添加用户
        .on('click', '#bar_add_user', function () {
            var parentDeptTree = null;//部门树
            var nameValidate = null;//较验
            layer.open({
                id: 'openWind',
                type: 1,
                title: '添加用户',
                content: $('#add_user_wind').html(),
                area: ['670px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-user-form").valid()) {
                        $($("#openWind input.error")[0]).focus();
                        return;
                    }
                    if ($("#openWind .add-finger .fingerShowList").length == 0) {
                        layer.msg('请至少录入一个指纹模板！', {icon: 2});
                        return;
                    }
                    var temp = $("#openWind form").serializeJSON();
                    var fingerTem = new Array();
                    $("#openWind .add-finger .fingerShowList").each(function (index, ele) {
                        var tem = new Object();
                        tem.id = '';
                        tem.fingerprintPwd = $(ele).data("finger");
                        fingerTem.push(tem)
                    });
                    var postData = {
                        username: $('#openWind input[name=username]').val().trim(),
                        truename: $('#openWind input[name=truename]').val().trim(),
                        policyid: temp.selectPolicy,
                        usbkeyid: temp.usbKeyList,
                        deptguid: $('#openWind input[name=parentdept]').attr('data-id'),
                        fingerprint: JSON.stringify(fingerTem)
                    }
                    if ($(layero).find('.layui-layer-btn0').hasClass('btn-disabled')) {
                        return;
                    }
                    $(layero).find('.layui-layer-btn0').addClass('btn-disabled');
                    $.ajax({
                        type: 'post',
                        url: ctx + '/clientUser/addClientUser',
                        data: postData,
                        success: function (msg) {
                            if (msg.resultCode == 1) {
                                layer.close(index);
                                layer.msg('添加成功！', {icon: 1});
                                //更新部门树
                                getAjax(ctx + '/department/getDepartmentNodesByLoginUser', '', function (msg) {
                                    zNodes = JSON.parse(msg);
                                    var selectID = deptTree.getSelectedNodes()[0].id;
                                    deptTree.destroy();
                                    initDeptTree(selectID);
                                    initdeptTable(selectID);
                                });
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
                btn2: function (index, layero) {
                    layer.close(layer.index);
                    cancekDevice();//让设备回到原始状态
                },
                success: function (layero, index) {
                    firstFocus();
                    checkDevice();
                    var htmlpolicy = '';
                    for (var i = 0; i < policyList.length; i++) {
                        htmlpolicy += '<option value=' + policyList[i].id + '>' + policyList[i].name + '</option>';
                    }
                    $('#openWind select[name=selectPolicy]').html(htmlpolicy);
                    var htmlusb = '';
                    htmlusb += '<option value="-1"></option>';
                    for (var i = 0; i < usbDeviceList.length; i++) {
                        htmlusb += '<option value=' + usbDeviceList[i].id + '>' + usbDeviceList[i].name + '</option>';
                    }
                    $('#openWind select[name=usbKeyList]').html(htmlusb);
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
                                $('#openWind .parent-dept').val(treeNode.name).attr('data-id', treeNode.id);
                                $('#openWind .parent-dept-tree-box').slideUp('fast');
                            }
                        }
                    };
                    //部门树
                    parentDeptTree = $.fn.zTree.init($("#openWind .j-parent-dept-tree"), setting, zNodes);

                    if (zNodes.length > 0) {
                        var node = parentDeptTree.getNodeByParam('id', deptTree.getSelectedNodes()[0].id == 1 ? 2 : deptTree.getSelectedNodes()[0].id);
                        //默认选中顶级部门
                        $('#' + node.tId + '_a').click();
                    }
                    //校验
                    nameValidate = $('#openWind .j-add-user-form').validate({
                        rules: {
                            username: {
                                required: true,
                                numchinese: true,
                            },
                            truename: {
                                required: true,
                                numchinese: true,
                            },
                            password: {
                                required: true,
                                minlength: 6
                            },
                            repassword: {
                                equalTo: $('#openWind input[name=password]'),
                                maxlength: 20
                            },
                            parentdept: {
                                required: true,
                            }
                        }
                    });
                },
                cancel: function (index, layero) {
                    layer.close(layer.index);
                    cancekDevice();//让设备回到原始状态
                }
            });
        })
        //编辑用户
        .on('click', '.j-opt-hover-edit', function () {
            getEmUsbList();
            var idx = $('.j-opt-hover-edit').index(this);
            var id = Number($(this).attr('data-id'));//这是用户的id
            var guid = String($(this).attr('data-guid'));//这是用户的id
            var parentDeptTree = null;//部门树
            layer.open({
                id: 'openWind',//这个地方会自动给弹出框添加一个id
                type: 1,
                title: '编辑用户',
                content: $('#add_user_wind').html(),
                area: ['670px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if (!$("#openWind .j-add-user-form").valid()) {
                        return;
                    }
                    if ($("#openWind .add-finger .fingerShowList").length == 0) {
                        layer.msg('请至少录入一个指纹模板！', {icon: 2});
                        return;
                    }
                    var temp = $("#openWind form").serializeJSON();
                    var fingerTem = new Array();
                    $("#openWind .add-finger .fingerShowList").each(function (index, ele) {
                        var tem = new Object();
                        tem.id = $(ele).data("id") == undefined ? '' : $(ele).data("id");
                        tem.fingerprintPwd = $(ele).data("finger");
                        fingerTem.push(tem)
                    });
                    var postData = {
                        id: id,
                        username: temp.username,
                        truename: temp.truename,
                        deptguid: $('#openWind input[name=parentdept]').attr('data-id'),
                        policyid: temp.selectPolicy,
                        usbkeyid: -1,
                        fingerprint: JSON.stringify(fingerTem)

                    };
                    if ($(layero).find('.layui-layer-btn0').hasClass('btn-disabled')) {
                        return;
                    }
                    $(layero).find('.layui-layer-btn0').addClass('btn-disabled');
                    postAjax(ctx + '/clientUser/updateClientUser', postData, function (msg) {
                        if (msg.resultCode == 1) {
                            layer.close(index);
                            layer.msg('编辑成功！', {icon: 1});
                            //更新部门树
                            getAjax(ctx + '/department/getDepartmentNodesByLoginUser', '', function (msg) {
                                zNodes = JSON.parse(msg);
                                var selectID = deptTree.getSelectedNodes()[0].id;
                                deptTree.destroy();
                                initDeptTree(selectID);
                                initdeptTable(selectID);
                            });
                        } else {
                            $(layero).find('.layui-layer-btn0').removeClass('btn-disabled');
                            layer.msg('编辑失败！' + (msg.resultMsg || ''), {icon: 2});
                        }
                    });
                },
                btn2: function (index, layero) {
                    layer.close(layer.index);
                    cancekDevice();//让设备回到原始状态
                },
                success: function (layero, index) {
                    firstFocus();
                    checkDevice();
                    var htmlpolicy = '';
                    for (var i = 0; i < policyList.length; i++) {
                        htmlpolicy += '<option value=' + policyList[i].id + '>' + policyList[i].name + '</option>';
                    }
                    $('#openWind select[name=selectPolicy]').html(htmlpolicy);
                    var htmlusb = '';
                    htmlusb += '<option value="-1"></option>';
                    for (var i = 0; i < usbDeviceList.length; i++) {
                        htmlusb += '<option value=' + usbDeviceList[i].id + '>' + usbDeviceList[i].name + '</option>';
                    }
                    $('#openWind select[name=usbKeyList]').html(htmlusb);
                    getAjax(ctx + '/fingerprint/getFingerprintListByCilentUserGuid', {guid: guid}, function (msg) {
                        if (msg.resultCode == 1) {
                            $("#openWind #bar_add_finger").before(template('tem_finger', msg.data));
                        }
                        else {
                            layer.msg('获取指纹列表失败！', {icon: 2});
                        }
                    });
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
                            onClick: function (event, treeId, treeNode) {
                                $('#openWind .parent-dept').val(treeNode.name).attr('data-id', treeNode.id);
                                $('#openWind .parent-dept-tree-box').slideUp('fast');
                            }
                        }
                    };
                    //部门树
                    // var zNodesBak = $.grep(zNodes, function (obj) {
                    //   return obj.treePath.indexOf(',' + id + ',') < 0 && obj.id !== id;
                    // });
                    parentDeptTree = $.fn.zTree.init($("#openWind .j-parent-dept-tree"), setting, zNodes);
                    if (zNodes.length > 0) {

                        var node = parentDeptTree.getNodeByParam('id', deptTree.getNodeByParam('id', deptTable.ajax.json().data[idx].deptguid).id);
                        if (node) {
                            $('#' + node.tId + '_a').click();
                        }
                    }
                    $('#openWind input[name=username]').val(deptTable.ajax.json().data[idx].username);
                    $('#openWind input[name=truename]').val(deptTable.ajax.json().data[idx].truename);
                    $('#openWind select[name=selectPolicy]').val(deptTable.ajax.json().data[idx].policyid);
                    //
                    // $('#openWind select[name=selectPolicy]').val(deptTable.ajax.json().data[idx].policyname).attr('value', deptTable.ajax.json().data[idx].policyid);
                    //校验
                    $('#openWind .j-add-user-form').validate({
                        rules: {
                            username: {
                                required: true,
                                numchinese: true,
                            },
                            truename: {
                                required: true,
                                numchinese: true,
                            },
                            password: {
                                maxlength: 20
                            },
                            repassword: {
                                equalTo: $('#openWind input[name=password]'),
                                maxlength: 20
                            },
                            parentdept: {
                                required: true,
                            },
                        }
                    });
                },
                cancel: function (index, layero) {
                    layer.close(layer.index);
                    cancekDevice();//让设备回到原始状态
                }
            });
        })
        //批量修改策略
        .on('click', '#bar_policy_user', function () {
            var ids = [];
            if ($('.j-check-user:checked').length == 0) {
                layer.msg('请先选中用户！', {icon: 7});
                return;
            } else {
                ids = $('.j-check-user:checked').map(function (index, obj) {
                    return $(obj).attr('data-id');
                }).toArray();
            }
            layer.open({
                id: 'openWind',
                type: 1,
                title: '批量修改策略',
                content: $('#temp_policy').html(),
                area: ['670px', '300px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    var postData = {};
                    postData.ids = ids.join(',');
                    postData.policyid = $('#openWind select[name=userSelectPolicy] option:selected').val();
                    $.ajax({
                        type: 'post',
                        url: ctx + '/policy/batchUpdateClientUsersPolicy',//批量修改策略接口
                        data: postData,
                        success: function (msg) {
                            if (msg.resultCode == '1') {
                                deptTable.ajax.reload(function () {
                                }, true);
                                $('.j-check-user-all').prop('checked', false);
                                layer.close(index);
                                layer.msg('修改成功！', {icon: 1});
                            } else {
                                layer.msg('修改失败！', {icon: 2});
                            }
                        },
                        error: function () {
                            layer.msg('删除失败！', {icon: 2});
                        }
                    })

                },
                success: function (index, layero) {
                    var htmlpolicy = '';
                    for (var i = 0; i < policyList.length; i++) {
                        htmlpolicy += '<option value=' + policyList[i].id + '>' + policyList[i].name + '</option>';
                    }
                    $('#openWind select[name=userSelectPolicy]').html(htmlpolicy);
                }
            });

        })
        //解绑usbkey
        .on('click', '.j-opt-hover-remove,#bar_relieve', function () {
            var ids = [];
            if ($(this).is('#bar_relieve')) {
                if ($('.j-check-user:checked').length == 0) {
                    layer.msg('请先选中用户！', {icon: 7});
                    return;
                }
                else {
                    ids = $('.j-check-user:checked').map(function (index, obj) {
                        return $(obj).attr('data-id');
                    }).toArray();
                }
            }
            else {
                ids.push($(this).attr('data-id'));
            }
            layer.confirm(ids.length > 1 ? '确定要解绑选中的用户吗？' : '确定要解绑该用户吗？', {
                btn: ['确定', '取消']
            }, function () {
                var postData = {};
                postData.clientUserId = ids.join(',');
                $.ajax({
                    type: 'post',
                    url: ctx + '/usbKey/unbindUsbKeyByClientUserId',
                    data: postData,
                    success: function (msg) {
                        if (msg.resultCode == '1') {
                            deptTable.ajax.reload(function () {
                            }, true);
                            $('.j-check-user-all').prop('checked', false);
                            layer.msg('解绑成功！', {icon: 1});
                        } else {
                            layer.msg('解绑失败！', {icon: 2});
                        }
                    },
                    error: function () {
                        layer.msg('解绑失败！', {icon: 2});
                    }
                })
            });

        })
        //导出
        .on('click', '#bar_export', function () {
            // var param = {
            //   searchstr: $('#bar_searchstr').val().trim(),//关键字
            //   pid: deptTree.getSelectedNodes()[0].id
            // }
            window.location.href = ctx + '/clientUser/exportxsl';
        })
        //导入用户
        .on('click', '#bar_import', function () {
            layer.open({
                id: 'openWind',
                type: 1,
                title: '导入用户',
                content: $('#tem_user_import').html(),
                area: ['670px', '400px'],
                btn: ['关闭'],
                yes: function (index, layero) {
                    if (fl) {
                        window.location.reload();
                    }
                    layer.closeAll();
                },
                success: function (layero, index) {

                }
            });
        })
        //点击上传升级文件
        .on('click', '#updata', function () {
            $('#clientUpdataTip').hide();
            if ($("input#clientUpdataPath").val() == '') {
                $("#clientUpdataTip").show();
                return false;
            }
            layer.confirm('导入将覆盖现有用户，确认导入？', {
                btn: ['确定', '取消']
            }, function () {
                var formData = new FormData($("#openWind #updataform")[0]);
                $.ajax({
                    url: ctx + "/clientUser/importClientUserWithExcel",
                    type: "post",
                    async: true,
                    cache: false,
                    contentType: false,
                    processData: false,
                    dataType: "json",
                    data: formData,
                    success: function (msg) {
                        if (msg.resultCode == '1') {
                            fl = true
                            layer.msg("导入成功", {
                                icon: 1,
                                end: function () {
                                    // window.location.reload();
                                }
                            });
                        } else {
                            layer.msg("导入失败！" + (msg.resultMsg || ''), {icon: 2});
                        }
                    },
                    error: function (e) {
                        layer.msg("导入失败", {icon: 2});
                    }
                });

            });

        })
        //获取文件名放到text上
        .on('change', 'input[type=file]', function (e) {
            var file = $(this).val();
            var filenames = getfilename(file);
            $(this).siblings('input[type=text]').val(filenames);
        })
        //显示与隐藏上级部门树
        .off("click", '.parent-dept').on('click', '.parent-dept', function () {
        if ($('#openWind .parent-dept-tree-box').css('display') == 'block') {
            $('#openWind .parent-dept-tree-box').slideUp('fast');
        }
        else {
            $('#openWind .parent-dept-tree-box').slideDown('fast');
        }
        return false;
    })
    //空白处点击收起上级部门树
        .on('click', '.parent-dept-tree-box', function () {
            return false;
        })
        //阻止收起上级部门树
        .on('click', '', function () {
            $('#openWind .parent-dept-tree-box').slideUp('fast');
        })
        //用户全选或取消全选
        .on('change', '.j-check-user-all', function () {
            $('.j-check-user').prop('checked', $(this).prop('checked'));
        })
        //用户单个选择或取消选择
        .on('change', '.j-check-user', function () {
            $('.j-check-user-all').prop('checked', $('.j-check-user').not(':checked').length == 0);
        })
        //用户名回车搜索
        .on('keydown', '#bar_searchstr', function (e) {
            if (e.keyCode == 13) {
                $('#bar_searchstr_icon').click();
            }
        })
        //用户名点击搜索
        .on('click', '#bar_searchstr_icon', function () {
            deptTable.settings()[0].ajax.data.ordercase = $('#bar_searchstr').val().trim();
            deptTable.ajax.reload();
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
        //密码强度验证
        .on('keyup', '#pass', function () {
            var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
            var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
            var enoughRegex = new RegExp("(?=.{6,}).*", "g");

            if (false == enoughRegex.test($(this).val())) {
                $('body #level').removeClass('pw-weak');
                $('body #level').removeClass('pw-medium');
                $('body #level').removeClass('pw-strong');
                $('body #level').addClass(' pw-defule');
                //密码小于六位的时候，密码强度图片都为灰色
            }
            else if (strongRegex.test($(this).val())) {
                $('body #level').removeClass('pw-weak');
                $('body #level').removeClass('pw-medium');
                $('body #level').removeClass('pw-strong');
                $('body #level').addClass(' pw-strong');
                //密码为八位及以上并且字母数字特殊字符三项都包括,强度最强
            }
            else if (mediumRegex.test($(this).val())) {
                $('body #level').removeClass('pw-weak');
                $('body #level').removeClass('pw-medium');
                $('body #level').removeClass('pw-strong');
                $('body #level').addClass(' pw-medium');
                //密码为七位及以上并且字母、数字、特殊字符三项中有两项，强度是中等
            }
            else {
                $('body #level').removeClass('pw-weak');
                $('body #level').removeClass('pw-medium');
                $('body #level').removeClass('pw-strong');
                $('body #level').addClass('pw-weak');
                //如果密码为6为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的
            }
            return true;
        })
        //启动设备
        .off("click", '.j-start-device').on('click', '.j-start-device', function () {
            $("#openWind #open").attr("href", 'VDPFP://GdFingerprinted');
            $("#openWind #open").trigger("click");
            setTimeout(function () {
                checkFingerDevice(true);
            }, 3000)
        })
        .off("click", '.j-bar-add-finger').on('click', '.j-bar-add-finger', function (event) {
        if ($("#openWind .add-finger .fingerShowList").length < 3) {
            checkFingerDevice();
        } else {
            layer.msg("最大录入三个指纹", {icon: 7});
        }
    })
    // 指纹
        .on("mouseover", ".fingerShowList", function () {
            $(this).addClass("fingerShowList-hover");
        })
        .on("mouseleave", ".fingerShowList", function () {
            $(this).removeClass("fingerShowList-hover");
        })
        .off("click", '.icon-btn-close').on("click", '.icon-btn-close', function () {
        _this = $(this);
        layer.confirm("是否确定删除？", {
            btn: ['确定', '取消']
        }, function (index) {
            _this.parents(".fingerShowList").remove();
            layer.close(index);
        })
    })
}

/*
    TODO 指纹录入方法，递归调用
    问题：1超时问题
    2 开始之后不能录入
 */
function startFingerFirstEntering() {
    layer.msg("手指按压传感器中央", {icon: 1, time: 10000000});
    $("#openWind #bar_add_finger").removeClass("j-bar-add-finger");
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20024/register',
        dataType: 'jsonp',
        data: {"serverip": window.location.host},
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        success: function (msg) {
            layer.close(layer.index);
            if (msg.result == 0 && msg.fptemplen == undefined) {
                fingerNum++;
                layer.msg("同一手指，按压第" + fingerNum + "次", {icon: 1, time: 10000000});
                startFingerSecondEntering();
            } else if (msg.result == 6 && msg.fptemplen == undefined) {
                layer.msg("请勿重复注册", {icon: 7});
                $("#openWind #bar_add_finger").addClass("j-bar-add-finger");
            } else if (msg.result == 17 && msg.fptemplen == undefined) {

            } else {
                layer.msg("录入失败，请重录", {icon: 2});
                setTimeout(startFingerFirstEntering(), 2000);
                $("#openWind #bar_add_finger").addClass("j-bar-add-finger");

            }

        },
        error: function (msg) {
            layer.close(layer.index);
            layer.msg("录入错误" + ("错误码" + msg.result), {icon: 2});
            $("#openWind #bar_add_finger").addClass("j-bar-add-finger");
        }

    });

}

//最终录入的指纹
function startFingerSecondEntering() {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20024/inputfingerprint',
        dataType: 'jsonp',
        data: '',
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        success: function (msg) {
            layer.close(layer.index);
            if (msg.result == 0 && msg.fptemplen == undefined) {
                fingerNum++;
                layer.msg("同一手指，按压第" + fingerNum + "次", {icon: 1, time: 10000000});
                startFingerSecondEntering();
            } else if (msg.result == 8) {
                layer.msg("录入失败,请重新录入", {icon: 7});
                fingerNum = 1;
                $("#openWind #bar_add_finger").addClass("j-bar-add-finger");
            } else if (msg.result == 17 && msg.fptemplen == undefined) {

            }else if (msg.result == 0 && msg.fptemplen != undefined) {
                layer.msg("录入完成", {icon: 1});
                fingerNum = 1;
                $("#openWind #bar_add_finger").addClass("j-bar-add-finger");
                var fingerName = "指纹1";
                if ($("#openWind .add-finger .fingerShowList").length > 0) {
                    var fl = true;
                    $("#openWind .add-finger .fingerShowList").each(function (index, ele) {
                        if ($(this).find("span.text-top").text() != ("指纹" + (index + 1))) {
                            fl = false;
                            fingerName = "指纹" + (index + 1);
                            return false;
                        }
                    })
                    if (fl) {
                        fingerName = "指纹" + ($("#openWind .add-finger .fingerShowList").length + 1)
                    }

                }
                var html = '<div class="fingerShowList inline-block" data-finger="' + msg.fptemp + '"><span class="inline-block"><i class="iconfont icon-icon-fingerprint"></i></span><div class="con inline-block text-top"><span class="inline-block text-top">' + fingerName + '</span><i class="iconfont icon-btn-close"></i></div></div>'
                if (fingerName == "指纹1") {
                    $("#openWind .before").after(html)
                } else if (fingerName == "指纹2") {
                    $("#openWind .add-finger .fingerShowList:first").after(html)
                } else if (fingerName == "指纹3") {
                    $("#openWind #bar_add_finger").before(html)
                }

            }

        },
        error: function (msg) {
            layer.close(layer.index);
            layer.msg("录入失败，请重录", {icon: 2});
            fingerNum = 1;
            $("#openWind #bar_add_finger").addClass("j-bar-add-finger");
        }

    });
}

/*
    检测指纹设备是否正常
 */
function checkFingerDevice(isStart) {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20024/serviceonline',
        dataType: 'jsonp',
        data: '',
        jsonp: 'callback',
        cache: true,
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        timeout: 1000,
        success: function (msg) {
            $("#openWind #start_device").removeClass("j-start-device").addClass("color-grey");
            if (msg.result == 0) {
                if (isStart) {
                    layer.msg("启用成功", {icon: 1});

                } else {
                    startFingerFirstEntering();
                }
            } else if (msg.result == 2) {
                layer.msg("未检测到设备", {icon: 7});
            } else {
                layer.msg("未检测到指纹录入控件，请下载并安装", {icon: 7});
            }
        },
        error: function (msg) {
            if (isStart) {
                return false;
            } else {
                layer.msg("未检测到指纹录入控件，请下载并安装", {icon: 7});

            }

        }

    });

}

//页面进入检查设备
function checkDevice() {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20024/serviceonline',
        dataType: 'jsonp',
        data: '',
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        timeout: 1000,
        success: function (msg) {
            $("#openWind #start_device").removeClass("j-start-device").addClass("color-grey");
        },
        error: function (msg) {

        }

    });
}

//页面进入看上一次设备是否正常还有没有录入三次就结束的话终止进程
function cancekDevice() {
    $.ajax({
        type: "get",
        async: true,
        url: 'http://127.0.0.1:20024/cancellastregister',
        dataType: 'jsonp',
        data: "",
        cache: false,
        jsonp: 'callback',
        jsonpCallback: 'handleResponse', //设置回调函数名
        crossDomain: true,
        timeout: 1000,
        success: function (msg) {

        },
        error: function (msg) {

        }

    });
}

/**
 * 初始化部门树
 *
 */
function initDeptTree(selectID) {
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
                initdeptTable(treeNode.id);
                $('.j-check-dept-all').prop('checked', false);
            }
        }
    };
    //部门树
    deptTree = $.fn.zTree.init($("#dept_tree"), setting, zNodes);
    if (zNodes.length > 0) {
        //点击第一个节点
        var node = deptTree.getNodeByParam('id', selectID) || deptTree.getNodeByParam('id', 1);
        if (node) {
            $('#' + node.tId + '_a').click();
            $('.j-edit-dept').css('display', 'inline-block');
        }
    }
    else {
        $('.j-edit-dept').css('display', 'none');
        $('#user_table').DataTable();
    }
}

/**
 * 子部门列表
 *
 */
function initdeptTable(pid) {
    if (deptTable) {
        deptTable.settings()[0].ajax.data.pid = pid;
        deptTable.settings()[0].ajax.data.ordercase = $('#bar_searchstr').val().trim();
        deptTable.ajax.reload();
        return;
    }
    deptTable = $('#user_table').DataTable({ //表格初始化
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
            "url": ctx + "/clientUser/getClientUserPageByDepartmentId",
            //改变从服务器返回的数据给Datatable
            "dataSrc": function (json) {
                return json.data.map(function (obj) {
                    return [obj.id, {
                        username: obj.username,
                        isusb: obj.isbindedUsbkey
                    }, obj.truename, obj.policyname || '--', obj.ip || '--', obj.online, {id: obj.id, guid: obj.guid}]
                });
            },
            //将额外的参数添加到请求或修改需要被提交的数据对象
            "data": {
                "pid": pid,
                "ordercase": $('#bar_searchstr').val().trim()
            },
        },
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            "width": "35px",
            "class": "text-center",
            "render": function (data, type, full) {
                return '<div class="beauty-checkbox">' +
                    '<input id="table_check_' + data + '" type="checkbox" class="j-check-user" data-id="' + data + '">' +
                    '<label for="table_check_' + data + '" class="checkbox-icon"></label>' +
                    '</div>';
            }
        }, {
            "targets": [1],
            "orderable": false,
            "class": "text-ellipsis",
            "render": function (data, type, full) {
                if (data.isusb == 1) {
                    return '<i class="iconfont icon-list-USB" style="position: absolute;left: 40px;"></i><span>' + data.username + '</span>';
                } else {
                    return '<span>' + data.username + '</span>';
                }

            }
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
            "class": "text-ellipsis",
            "width": "80px",
            "render": function (data, type, full) {
                if (data == 0) {//不在线
                    return "<i class='iconfont icon-menu-user1 greycol' title='离线'></i>";
                } else {
                    return "<i class='iconfont icon-menu-user1 syscol' title='在线'></i>";
                }
            }
        }, {
            "targets": [6],
            "orderable": false,
            "class": "center-text",
            "width": "100px",
            "render": function (data, type, full) {
                return template('temp_opt_box', {id: data.id, guid: data.guid});
            }
        }],
        //当每次表格重绘的时候触发一个操作，比如更新数据后或者创建新的元素
        "drawCallback": function (oTable) {
            var ele = 'user_table';
            tableCallback(ele);
        }
    }).on('xhr.dt', function (e, settings, json, xhr) {
        //登陆超时重定向
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    });
}
