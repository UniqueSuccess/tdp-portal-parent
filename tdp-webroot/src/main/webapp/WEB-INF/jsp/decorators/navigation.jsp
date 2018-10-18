<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!-- menu.jsp -->
<style>
    .policy-box{
        padding: 0 20px;
    }
    .gd-layer .gd-layer-body .gd-layer-header{
        border-bottom: none;
    }
    .policy-list li{
        width: 175px;
        height: 85px;
        line-height: 85px;
        float: left;
        margin: 8px 8px 0 0;
        border: 1px solid #e5e5e5;
        border-radius: 1px;
        text-align: center;
        position: relative;
        cursor: pointer;
    }
    .policy-list li:nth-of-type(4n) {
        margin-right: 0;
    }
    .policy-list li:hover{
        border-color: #58a5e3;
        color: #58a5e3;
    }
    .policy-list li:hover .icon-delete{
        display: inline-block;
    }
    .policy-list li .icon-delete{
        position: absolute;
        right: 8px;
        bottom: 8px;
        color: #c9ccd5;
        display: none;
        line-height: 14px;
    }

</style>

<div id="leftnav">
    <gd-leftmenu :config="leftMenuConfig"></gd-leftmenu>
</div>

<script type="text/html" id="policy_select_temp">
    <div class="policy-box" id="policy_select">
        <i title="添加" class="gd-btn-icon icon-add" @click="addPolicy()"></i>
        <ul class="policy-list">
            <li v-for="item in policyArray" :data-id="item.id" @click="goPage(item.id)">
                <span>{{item.name}}</span>
                <i title="删除" class="icon-delete" @click.stop="deletePolicy(item.id)"></i>
            </li>
        </ul>
    </div>
</script>

<script type="text/html" id="policy_add_temp">
    <div class="container" id="policy_add">
        <form id="add_dept_form">
            <div class="row">
                <label class="gd-label-required">策略名</label>
                <input type="text" class="gd-input gd-input-lg" gd-validate="required" name="name">
            </div>
            <div class="row">
                <label class="">继承自</label>
                <gd-select placeholder="请选择" v-model="selectPolicyValue" class="gd-select-lg" name="pid">
                    <gd-option v-for="item in policyArray" :value="item.id" :key="item.id">{{item.name}}</gd-option>
                </gd-select>
            </div>
        </form>
    </div>
</script>

<script>
    var app = new Vue({
        el: '#leftnav',
        data: {
            policyArray: [], // 策略
            leftMenuConfig: {
                api:ctx + '/system/navigation/getUserNavigation', //接口地址
                logo: ctxImg + '/leftmenu/logo.png', //logo地址
                apiCallback: function (data) {
                    return data;
                },
                logoStyle: {//设置logo的样式，可选
                    backgroundPosition: '0px'
                }
            }
        },
        mounted: function() {
            this.getPolicyList();
            this.initPolicyLayer();
        },
        methods: {
            getPolicyList: function() {
                var _this = this;
                gd.get(ctx + '/policy/getAllPolicys', '', function(msg) {
                    console.log(msg)
                    if (msg.resultCode == '0') {
                        _this.policyArray = msg.data;
                    }
                })
            },
            initPolicyLayer: function() {
                $('body').on('click', '.gd-left-menu-body li:nth-child(3)', function() {
                    gd.showLayer({
                        id: 'policyWind',//可传一个id作为标识
                        title: '选择策略',//窗口标题
                        content: $('#policy_select_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                        //url: './layer_content.html',//也可以传入url作为content,
                        size: [770, 475],//窗口大小，直接传数字即可，也可以是['600px','400px']
                        //autoFocus:true,//自动对输入框获取焦点，默认为ture
                        success: function(dom) {
                            console.log(app.policyArray);
                            var selectWindow = new Vue({
                                el: '#policy_select',
                                data: {
                                    policyArray: app.policyArray
                                },
                                methods: {
                                    addPolicy: function() {
                                        gd.showLayer({
                                            id: 'policyAddWind',//可传一个id作为标识
                                            title: '新建策略',//窗口标题
                                            content: $('#policy_add_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                            //url: './layer_content.html',//也可以传入url作为content,
                                            size: [670, 280],//窗口大小，直接传数字即可，也可以是['600px','400px']
//                                            autoFocus:true,//自动对输入框获取焦点，默认为ture
                                            btn: [
                                                {
                                                    text: '确定',
                                                    action: function (dom) {
                                                        var postData = $('#policyAddWind #add_dept_form').serializeJSON();
                                                        console.log(postData)
                                                        gd.post(ctx + '/policy/addPolicy', postData, function (msg) {
                                                            console.log(msg);
                                                            if (msg.resultCode == '0') {
                                                                gd.showSuccess('保存成功');
                                                                app.getPolicyList();
                                                                selectWindow.policyArray = app.policyArray;
                                                            } else {
                                                                gd.showError('保存失败');
                                                            }
                                                            dom.close();
                                                        })
                                                        return false;//阻止弹窗自动关闭
                                                    }
                                                },
                                                {
                                                    text: '取消',
                                                    action: function () {

                                                    }
                                                }
                                            ],
                                            success: function(dom) {
                                                var addWindow = new Vue({
                                                    el: '#policy_add',
                                                    data: {
                                                        policyArray: app.policyArray,
                                                        selectPolicyValue: '1'
                                                    }
                                                })
                                            },
                                            end: function (dom) {//参数为当前窗口dom对象
                                                // gd.showSuccess('窗口关闭了');
                                            }
                                        });
                                    },
                                    // 跳页
                                    goPage: function (id) {
                                        window.location.href = ctx + '/policy/index?id='+ id;
                                    },
                                    // 删除策略
                                    deletePolicy: function (id) {
                                        var _this = this;
                                        console.log(this.policyArray);
//                                        var flag = this.policyArray.map(function(item,index){
//                                            if (item.id == id) {
//                                                return index
//                                            }
//                                        });
//                                        console.log(flag);
//                                        return;
                                        gd.post(ctx + '/policy/deletePolicy', {policyId: id}, function(msg) {
                                            if (msg.resultCode == 0) {
                                                gd.showSuccess('删除成功');
                                                app.getPolicyList();
                                                selectWindow.policyArray = app.policyArray;
                                            } else {
                                                gd.showError('删除失败');
                                            }
                                        })
                                    }
                                }
                            })
                        },
                        end: function (dom) {//参数为当前窗口dom对象
                            // gd.showSuccess('窗口关闭了');
                        }
                    });
                });
            }
        }
    });

</script>