/**
 * Created by chengl on 2018/1/16 0016.
 */
var humtree = null;
$(function () {
    getapp(objAll);
    initEvents();//初始化页面事件
});

function initEvents() {
    $('body')
    //流程列表点击切换
        .on('click', '.default_bar', function () {
            $(this).addClass('default_bar_hover');
            $(this).parents(".default").addClass("default_hover");
            $(this).parents(".default").siblings().find('.default_bar').removeClass('default_bar_hover');
            $(this).parents(".default").siblings("label").removeClass("default_hover");
            $(this).siblings(".approve_con").show();
            $(this).find("img").show();
            $(this).parents(".default").siblings().find(".approve_con").hide();
            $(this).parents(".default").siblings().find("img").hide();
        })
        //添加审批人
        .on('click', '.bar_add_hum', function () {
            var indexDOm = $(this);
            var zNodes = null;
            var setting = {
                check: {
                    enable: true,
                    chkStyle: "checkbox",
                    chkboxType: {"Y": "ps", "N": "ps"}
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                }
            };
            gd.showLayer({
                id: 'openWind',
                type: 1,
                title: '添加审批人',
                content: $('#treeLayer').html(),
                size: [470, 400],
                btn: [{
                    text: '确定',
                    enter: true,//响应回车
                    action: function (dom){
                        var apphum = '';

                        var nodes = humtree.getCheckedNodes(true);
                        nodes = $.grep(nodes, function (n, i) {
                            return n.id != 1;
                        });
                        for (var i = 0; i < nodes.length; i++) {
                            apphum += '<span class="wind-span text-ellipsis" data-guid="' + nodes[i].guid + '">' + nodes[i].name + '<i class="iconfont icon-close hum_del" data-guid="' + nodes[i].guid + '"></i></span>'
                        }
                        apphum += '<a class="bar-item bar-item-icon iconfont icon-add margin-right-sm bar_add_hum" title="添加"></a>'
                        indexDOm.parents(".approve_h").html(apphum);
                        dom.close();

                    }
                }, {
                    text: '取消',
                    action: function (dom) {
                        dom.close();
                    }
                }],
                success: function (dom) {
                    var postData = {};
                    if ($(indexDOm).closest("label.default").data("stepid") == '') {//说明是新增
                        gd.get(ctx + '/systemSetting/user/getAllOperatorList', '', function (msg) {
                            if (msg.resultCode == 0) {
                                zNodes = msg.data;
                                zNodes.push({"name": "全部", "id": 1});
                                for (var i = 0; i < zNodes.length; i++) {
                                    zNodes[i].pId = 1;
                                }
                                humtree = $.fn.zTree.init($("#openWind #treeview"), setting, zNodes);
                                humtree.expandAll(true);
                            }
                            else {
                                gd.showError('获取审批账户失败');
                            }
                        });
                    } else {//说明是保存 是回显
                        var id = $(indexDOm).closest("label.default").data("stepid");
                        postData.approveModelId = id;
                        gd.get(ctx + '/systemSetting/user/getAllOperatorList', postData, function (msg) {
                            if (msg.resultCode == 0) {
                                zNodes = msg.data;
                                zNodes.push({"name": "全部", "id": 1});
                                for (var i = 0; i < zNodes.length; i++) {
                                    zNodes[i].pId = 1;
                                }
                                humtree = $.fn.zTree.init($("#openWind #treeview"), setting, zNodes);
                                humtree.expandAll(true);
                            }
                            else {
                                gd.showError('获取审批账户失败！');
                            }
                        });

                    }


                }

            });
        })
        //删除流程
        .off("click", '.bar_delete_flow').on('click', '.bar_delete_flow', function () {
            var indexDom = $(this);
            if ($(".approve_start label.default").length == 1) {
                gd.showWarning('必须保留一个节点');
                return false;
            }

            if ($(this).parents('label.default').data('stepid') == '') {
                gd.showWarning('该流程还未保存，不能删除！');
                return false;
            } else {
                var stepid = $(this).data("stepid");
                var pid = $(this).parents('label.default').prev('label').data('stepid');

                gd.showConfirm({
                    id: 'deleteFlow',
                    content: '确定要删除该流程？',
                    btn: [{
                        text: '删除',
                        class: 'gd-btn-danger',//也可以自定义类
                        enter: true,//响应回车
                        action: function (dom) {

                            gd.post(ctx + '/approveModel/deleteApproveModel',{flowId: flowid, id: stepid, seniorId: pid},function(msg){
                                if (msg.resultCode == '0') {
                                    gd.showSuccess('删除成功！');
                                    $(indexDom).parents('label.default').fadeOut(300).remove();
                                } else {
                                    gd.showError('删除失败！');
                                }
                            })

                            dom.close();
                        }
                    }, {
                        text: '取消',
                        action: function (dom) {
                            dom.close();
                        }
                    }],
                    success: function (dom) {
                        // gd.showSuccess('窗口打开了');
                    },
                    end: function (dom) {
                        // gd.showSuccess('窗口关闭了');
                    }
                });

            }

        })
        //克隆流程
        .off("click", '.flow_add').on('click', '.flow_add', function () {
            var fl = true;
            $(".approve_start").find(".default").each(function () {
                if ($(this).data('stepid') == '') {
                    fl = false;
                }
            });
            if (fl) {
                if ($(this).closest("label.default").next("label").attr('class') == 'end') {
                    var a = $(this).closest("label").next("label.end");
                    var t = $("#default_template").find("label.default");
                    var b = t.clone(true);


                    b.insertBefore(a);

                    b.find(".default_bar_title").trigger('click');
                    b.find("#stand99").prop('checked', 'checked');
                } else {
                    var a = $(this).closest("label").next("label.default");
                    var t = $("#default_template").find("label.default");
                    var b = t.clone(true);


                    b.insertBefore(a);

                    b.find(".default_bar_title").trigger('click');
                    b.find("#stand99").prop('checked', 'checked');
                }
            } else {
                gd.showWarning("请保存新增节点");
                return;
            }
        })
        //删除审批人
        .on('click', '.hum_del', function () {
            $(this).closest('span').remove();
        })

}

//保存环节
function saveApprove(ele) {
    var id = $(ele).parents("label.default").data("stepid");
    var name = $(ele).parents("label.default").find('input[name=stepname]').val();
    name = $.trim(name);
    var approvers = '';
    $(ele).parents("label.default").find(".approve_h span").each(function (index) {
        if (index == $(this).parents("label.default").find(".approve_h span").length - 1) {
            approvers += $(this).data('guid').toString()
        } else {
            approvers += $(this).data('guid').toString() + ';';
        }

    });
    var senid = $(ele).parents("label.default").prev("label").data('stepid');
    if(senid === ""){
        gd.showWarning('请先保存上一节点！');
        return;
    }

    var postData = {};
    if ($(ele).closest("label.default").data("stepid") == '') {  //说明是新增
        var standid = $(ele).parents("label.default").find('input[name=mode99]:checked').val();
        if (name == '') {
            var ele = null;
            $(".approve_con").each(function(index){
                if($(this).css('display') == 'block'){
                    ele = $(this).find('input[name=stepname]');
                }

            });
            // singleValidate(ele,'环节名称不能为空！');
            gd.showWarning('环节名称不能为空！');
            return;
        }
        if (approvers == '') {
            var ele = null;
            $(".approve_con").each(function(index){
                if($(this).css('display') == 'block'){
                    ele = $(this).find('.bar_add_hum');
                }

            });
            // singleValidate(ele,'审批人不能为空！');
            gd.showWarning('审批人不能为空！');
            return;
        }

        postData.flowId = Number(flowid);
        postData.name = name.toString();
        postData.approvers = approvers;
        postData.seniorId = Number(senid);//必有
        postData.standard = Number(standid);//必有
        gd.post(ctx + '/approveModel/addOrUpdateApproveModel', postData, function (msg) {
            if (msg.resultCode == 0) {
                gd.showSuccess('保存成功！');
                gd.get(ctx + '/approveDefinition/getApproveDefinitionModel', {approveDefinitionId: flowid}, function (msg) {
                    if (msg.resultCode == 0) {
                        $(".approve_start").html(template('approveAllList', msg.data));
                    }
                });


            } else {
                gd.showError('保存失败！' + (msg.resultMsg || ''));
            }
        });
    } else {//说明是编辑
        var standid = $(ele).parents("label.default").find('input[name=mode' + id + ']:checked').val();
        if (name == '') {
            var ele = null;
            $(".approve_con").each(function(index){
                if($(this).css('display') == 'block'){
                    ele = $(this).find('input[name=stepname]');
                }

            });
            // singleValidate(ele,'环节名称不能为空！');
            gd.showWarning('环节名称不能为空！');
            return;
        }
        if (approvers == '') {
            var ele = null;
            $(".approve_con").each(function(index){
                if($(this).css('display') == 'block'){
                    ele = $(this).find('.bar_add_hum');
                }

            });
            // singleValidate(ele,'审批人不能为空！');
            gd.showWarning('审批人不能为空！');
            return;
        }

        postData.flowId = Number(flowid);
        postData.id = id;
        postData.name = name;
        postData.approvers = approvers;
        postData.seniorId = senid;
        postData.standard = standid;
        gd.post(ctx + '/approveModel/addOrUpdateApproveModel', postData, function (msg) {
            if (msg.resultCode == 0) {
                gd.showSuccess('保存成功！');
                gd.get(ctx + '/approveDefinition/getApproveDefinitionModel', {approveDefinitionId: flowid}, function (msg) {
                    if (msg.resultCode == 0) {
                        $(".approve_start").html(template('approveAllList', msg.data));
                    }
                });
            }
            else {
                gd.showError('保存失败！' + (msg.resultMsg || ''));
            }
        });
    }

}

//取消环节
function cancelApprove(ele) {
    if ($(ele).closest("label.default").data("stepid") == '') {//说明是新建
        $(ele).parents("label.default").fadeOut(300).remove();
    } else {//说明是编辑的
        $(ele).parents(".approve_con").hide();
    }
}

//重新获取所有审批流程
function getapp(objAll) {
    $(".approve_start").html(template('approveAllList', objAll));
}
function singleValidate(selector,content){
    $(selector).after('<span style="color: red;margin-left: 10px;vertical-align: middle" class="error-span">'+content+'</span>');
    setTimeout(function(){
        $('body .error-span').remove();
    },3000)

}
