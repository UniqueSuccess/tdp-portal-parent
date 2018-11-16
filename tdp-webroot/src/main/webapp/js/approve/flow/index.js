/**
 * Created by chengl on 2018/1/15 0015.
 */

var approveList = null;
var validateFlow = null;
var approveFlowIndex = new Vue({
    el: "#approveFlowIndex",
    mounted:function () {
        this.getApproveFlow('.left-list:first');
    },
    methods: {
        getApproveFlow:function(el){
            var _this = this;
            gd.get(ctx + '/approveDefinition/getAllApproveDefinition', '', function (msg) {
                if (msg.resultCode == 0) {
                    _this.flowArray = msg.data;
                    approveList = msg.data;
                    $("#flow_list_box").html(template('flow_list', approveList))
                }
                $(el).click();


            });
        },
        selectChange: function (data) {
            log(data)
        }
    },
    data: {
        flowArray:[],
        toolbarConfig: [
            {
                type: 'button',
                icon: 'icon-add',
                title: '添加',
                action: function (dom) {
                    var dom = gd.showLayer({
                        id: 'addWind',
                        title: '添加审批流程',
                        content: $('#new_flow').html(),
                        size: [590, 300],
                        btn: [{
                            text: '确定',
                            enter: true,//响应回车
                            action: function (dom){
                                if (!validateFlow.valid()) {
                                    return false;
                                }
                                var postData = $('#addWind #add_flow_form').serializeJSON();
                                gd.post(ctx + '/approveDefinition/addApproveDefinition',postData,function (msg) {
                                    if (msg.resultCode == '0') {
                                        gd.showSuccess('新建成功！');
                                        approveFlowIndex.getApproveFlow('.left-list:last');
                                    } else {
                                        gd.showError('新建失败');
                                    }
                                    dom.close();
                                })

                            }
                        }, {
                            text: '取消',
                            action: function (dom) {
                                dom.close();
                            }
                        }],
                        success: function (dom) {

                            var addWindow = new Vue({
                                el: '#add_approve',
                                data: {
                                    selectValue: '',
                                    selectFlowValue: '1',
                                    flowArray: approveFlowIndex.flowArray
                                },
                                mounted: function() {
                                    validateFlow = gd.validate('#add_flow_form', {
                                        autoPlaceholer: true,
                                    });
                                }
                            });
                        },
                        end: function (dom) {//参数为当前窗口dom对象
                        }
                    })
                }
            },
            {
                type: 'button',
                icon: 'icon-delete',
                title: '删除',
                disabled: true,
                action: function () {
                    gd.showConfirm({
                        id: 'deleteWind',
                        content: '确定要删除吗?',
                        btn: [{
                            text: '删除',
                            class: 'gd-btn',//也可以自定义类
                            enter: true,//响应回车
                            action: function (dom) {
                                var id = $("body #flow_list_box .list-active").data("id");
                                if (id == 1) {
                                    gd.showWarning('默认流程不能删除');
                                    dom.close();
                                    return false;
                                }
                                gd.post(ctx + '/approveDefinition/deleteApproveDefinition',{approveDefinitionId: id},function (msg) {
                                    if (msg.resultCode == '0') {
                                        gd.showSuccess('删除成功！');
                                        approveFlowIndex.getApproveFlow('.left-list:first');
                                    } else if (msg.resultCode == '1'){
                                        gd.showError('该审批流程已被使用，无法删除！');
                                    }else{
                                        gd.showError('删除错误！');
                                    }
                                    dom.close();
                                })
                            }
                        }, {
                            text: '取消',
                            action: function (dom) {
                                // gd.showSuccess('你点了取消');
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
            }
        ]
    }
});



$(function () {
    // getApprroveList('.left-list:first');//获取审批列表
    initEvents();//初始化页面事件
});

function initEvents() {
    $('body')
    //流程列表点击切换
        .on('click', '.left-list', function () {
            if (!$(this).hasClass('list-active')) {

                $('.list-active').removeClass('list-active');

                $(this).addClass('list-active');
                $("#approveFlow_box").html('').load(ctx + '/approveDefinition/approveDetailView?approveDefinitionId=' + $(this).attr('data-id'));
                // getRoleInfo($(this).attr('data-id'));
                approveFlowIndex.toolbarConfig[1].disabled = $(this).data("id") == 1 ? true : false; //批量删除是否禁用
            }
        })

}
