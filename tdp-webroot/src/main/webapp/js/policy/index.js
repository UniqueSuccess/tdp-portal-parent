/**
 * Created by chengl on 2018/1/2 0002.
 */
var colorFirst,colorSec,colorTh;
var screenColor;
var outColor;
var exportColor;
var approveList = null;
var export_file = 0;//审批流程的选择的id
var out_file = 0;//审批流程的选择的id
var obj = {
    "sbscrnwatermark": {
        "content": {
            "computername": 1,
            "depname": 1,
            "macaddress": 1,
            "ip": 1,
            "time": 1,
            "manualtext": "程磊测试",//自定义水印内容
            "mode": 0,
            "username": 1,
            "manual": 0,
            "direction": 0,
            "localtion": 0,
            "locaktiontemp": "[1,16]",
            "color": 16711680,
            "tcolor": '#ff0000',
            "opacity": 100,
            "fontsize":14,

        },
        "enable": 1,
    },
    "sbfileoutcfg": {//外发
        "content": {
            "validtimecheck": 0,//设置有效时间
            "autodelouttime": 0,
            "default": 1,
            "autodelete": 0,
            "opencountcheck": 0,//设置允许打开
            "disablemod": 0,
            "pwdcheck": 0,//设置密码验证
            "disablesc": 1,//禁止截屏
            "flowid": "1",//审批流程
            "mode": 1,//1自主外发 2是审批外发
            "opencount": 20,//允许打开多少次
            "printwatermark": 0,
            "ptwmcontent": "",
            "pwd": 10,//密码验证多少位
            "scwmcontent": "文件外发水印",
            "validtime": 1,//有效时间多少天
            "scwatermark": {
                "content": {
                    "computername": 1,
                    "depname": 1,
                    "macaddress": 1,
                    "ip": 1,
                    "time": 1,
                    "manualtext": "程磊测试",//自定义水印内容
                    "mode": 0,
                    "username": 1,
                    "manual": 0,
                    "recv": 0,
                    "direction": 1,
                    "localtion": 17,
                    "locaktiontemp": "[1,16]",
                    "color": 16711680,
                    "tcolor": '#ff0000',
                    "opacity": 255,
                },
                "enable": 0,
                "isshow": 0//0是显示1是隐式水印
            },//屏幕水印
        },
        "enable": 1,
    },
    "sbfileopt": {//导出
        "enable": 1,
        "content": {
            "flowid": "1",//审批流程
            "mode": 0,//1是明文 3是审批导出
            "sbfileoptwatermark": {
                "content": {
                    "computername": 1,
                    "depname": 1,
                    "macaddress": 1,
                    "ip": 1,
                    "time": 1,
                    "manualtext": "程磊测试",//自定义水印内容
                    "username": 1,
                    "manual": 0,
                    "recv": 0,
                    "direction": 0,
                    "localtion": 0,
                    "locaktiontemp": "[1,16]",
                    "color": 16711680,
                    "tcolor": '#ff0000',
                    "opacity": 100,
                },
                "enable": 0,
                "isshow": 0//0是显示1是隐式水印
            }
        },

    },
    "videoappro": {
        "enable": 0
    }//视频数据流转是否
};
var objAll = {};
$(function () {
    allChecked();
    getAllApprove();
    initEvents();
    $(".policy-content").scroll(function(){

        $($(screenColor)[0]).hide();
        $($(outColor)[0]).hide();
        $($(exportColor)[0]).hide();
        $(".j-color-list").slideUp("fast");
        $(".j-color-list_out").slideUp("fast");
        // $(".j-color-list_export").slideUp("fast");
    });
});
function initEvents() {
    $('body')
        //保存策略
        .on('click', '.policy-save', function () {
            var fl = false;
            $('.policy-content form').validate({
                rules: {
                    screendiyWaterContent: {
                        maxlength: 30,

                    },
                    settingTimes: {
                        digits: true,
                        min: 1
                    },
                    passwordVerifications: {
                        digits: true,
                        min: 1
                    },
                    allowOpens: {
                        digits: true,
                        min: 1
                    },
                    outdiyWaterContent: {
                        maxlength: 30,
                    },
                    exportdiyWaterContent: {
                        maxlength: 30,
                    },
                }
            });
            if (fl) {
                return false;
            }
            if ($("body input[name=diyWater]").is(":checked")) {
                if ($("body input[name=screendiyWaterContent]").val() != '') {
                    $("body input[name=screendiyWaterContent]").removeClass('border-error');
                } else {
                    layer.msg("请填写自定义水印内容", {icon: 2});
                    $("body input[name=screendiyWaterContent]").addClass('border-error');
                    $("body input[name=screendiyWaterContent]").focus();
                    return;
                }
            } else {
                $("body input[name=screendiyWaterContent]").removeClass('border-error');
            }
            if ($("body input[name=fileOutDiyWater]").is(":checked")) {
                if ($("body input[name=outdiyWaterContent]").val() != '') {
                    $("body input[name=outdiyWaterContent]").removeClass('border-error');
                } else {
                    layer.msg("请填写自定义水印内容", {icon: 2});
                    $("body input[name=outdiyWaterContent]").addClass('border-error');
                    $("body input[name=outdiyWaterContent]").focus();
                    return;
                }
            } else {
                $("body input[name=outdiyWaterContent]").removeClass('border-error');
            }
            if ($("body input[name=fileExportDiyWater]").is(":checked")) {
                if ($("body input[name=exportdiyWaterContent]").val() != '') {
                    $("body input[name=exportdiyWaterContent]").removeClass('border-error');
                } else {
                    layer.msg("请填写自定义水印内容", {icon: 2});
                    $("body input[name=exportdiyWaterContent]").addClass('border-error');
                    $("body input[name=exportdiyWaterContent]").focus();
                    return;
                }
            } else {
                $("body input[name=exportdiyWaterContent]").removeClass('border-error');
            }
            if (!$(".policy-content form").valid()) {
                $("input[type=text]").each(function(index,ele){
                    if($(this).hasClass("error")){
                        $(this).focus();
                    }
                });
                return;
            }
            var temp = $(".policy-content form").serializeJSON();
            //下面是关于审批的
            if (out_file == 0) {//说明进来没有点审批
                if (msg.sbfileoutcfg.content.flowid != 0) {
                    obj.sbfileoutcfg.content.flowid = msg.sbfileoutcfg.content.flowid;
                } else {
                    obj.sbfileoutcfg.content.flowid = out_file;
                }
            } else {//第一次进来点了审批，out_file有值了，不用管了
                obj.sbfileoutcfg.content.flowid = out_file;
            }
            if (export_file == 0) {
                if (msg.sbfileopt.content.flowid != 0) {
                    obj.sbfileopt.content.flowid = msg.sbfileopt.content.flowid;
                } else {
                    obj.sbfileopt.content.flowid = export_file;
                }
            } else {//第一次进来点了审批，export_file，不用管了
                obj.sbfileopt.content.flowid = export_file;
            }
            //关于隐式水印的，文件外发，如果点击了隐式水印，所有的下级都要被选中，但是serialize不能够获取disabled状态下的值，所以只能手动获取
            if ($("#screenWaterHidden").is(':checked')) {
                temp.deptWater = 1;
                temp.nameWater = 1;
                temp.ipWater = 1;
                temp.MACWater = 1;
                temp.computerWater = 1;
                temp.timeWater = 1;
                // temp.screenWater = 1;
            }
            if ($("#outWaterHidden").is(':checked')) {
                temp.fileOutDept = 1;
                temp.fileOutName = 1;
                temp.fileOutIpWater = 1;
                temp.fileOutMACWater = 1;
                temp.fileOutComputerWater = 1;
                temp.fileOutTimeWater = 1;
                temp.fileOutRecv = 1;
            }
            if ($("#waterHidden").is(':checked')) {
                temp.fileExportDept = 1;
                temp.fileExportName = 1;
                temp.fileExportIpWater = 1;
                temp.fileExportMACWater = 1;
                temp.fileExportComputerWater = 1;
                temp.fileExportTimeWater = 1;
                temp.fileExportRecv = 1;
            }
            if ($("#screenSwitch").is(":checked")) {
                if ($('.screenWater .policy-con input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项屏幕水印", {icon: 2});
                    return;
                }
            }

            if ($("#isScreenWater").is(":checked")) {
                if ($('.fileOut .waterShowContent input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项外发水印", {icon: 2});
                    return;
                }
            }

            if ($("#isScreen").is(":checked")) {
                if ($('.fileExport .waterShowContent input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项导出水印", {icon: 2});
                    return;
                }
            }
            // 屏幕水印位置必选
            if ($('#location_diy').is(':checked') && $('#screenSwitch').is(':checked')) {
                if ($('.screenWater .j-location-content input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项屏幕水印位置", {icon: 2});
                    return;
                }
            }
            // 外发水印位置必选
            if ($('#out_location_diy').is(':checked') && $('#isScreenWater').is(':checked')) {
                if ($('.fileOut .j-location-content input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项外发水印位置", {icon: 2});
                    return;
                }
            }
            // 导出水印位置必选
            if ($('#export_location_diy').is(':checked') && $('#isScreen').is(':checked')) {
                if ($('.fileExport .j-location-content input[type=checkbox]:checked').length == 0) {
                    layer.msg("请至少选择一项导出水印位置", {icon: 2});
                    return;
                }
            }

            //这是关于策略内容的
            //屏幕水印
            //todo 策略保存
            var localtion_temp = new Array();
            var localtion_totle = 0;
            temp.location = temp.location||0;
            for (var i = 0; i < temp.location.length; i++) {//暂时先改成外发的
                localtion_temp.push(temp.location[i]);
                localtion_totle = (localtion_totle + parseInt(temp.location[i]));
            }
            obj.sbscrnwatermark.enable = temp.screenSwitch ? Number(temp.screenSwitch) : 0;
            obj.sbscrnwatermark.content.isshow = temp.screenWater ? Number(temp.screenWater) : 0;
            obj.sbscrnwatermark.content.depname = temp.deptWater ? Number(temp.deptWater) : 0;
            obj.sbscrnwatermark.content.username = temp.nameWater ? Number(temp.nameWater) : 0;
            obj.sbscrnwatermark.content.ip = temp.ipWater ? Number(temp.ipWater) : 0;
            obj.sbscrnwatermark.content.macaddress = temp.MACWater ? Number(temp.MACWater) : 0;
            obj.sbscrnwatermark.content.computername = temp.computerWater ? Number(temp.computerWater) : 0;
            obj.sbscrnwatermark.content.time = temp.timeWater ? Number(temp.timeWater) : 0;
            obj.sbscrnwatermark.content.manual = temp.diyWater ? Number(temp.diyWater) : 0;
            obj.sbscrnwatermark.content.manualtext = temp.screendiyWaterContent || '';
            obj.sbscrnwatermark.content.direction = Number(temp.direction) || 0;//方向
            obj.sbscrnwatermark.content.localtion = Number(localtion_totle)||0;//位置
            obj.sbscrnwatermark.content.locaktiontemp = '[' + localtion_temp.toString() + ']' || '';
            obj.sbscrnwatermark.content.color = Number(temp.color_content) || 0;
            obj.sbscrnwatermark.content.tcolor = $('.screenWater .color-content-title').text();
            obj.sbscrnwatermark.content.opacity = Number(temp.opcity) || 125;
            obj.sbscrnwatermark.content.fontsize = Number(temp.fontSize) || 14;
            // 文件外发
            var out_localtion_temp = new Array();
            var out_localtion_totle = 0;
            temp.outlocation = temp.outlocation||0;
            for (var i = 0; i < temp.outlocation.length; i++) {
                out_localtion_temp.push(temp.outlocation[i]);
                out_localtion_totle = (out_localtion_totle + parseInt(temp.outlocation[i]));
            }
            obj.sbfileoutcfg.enable = temp.fileOutSwitch ? Number(temp.fileOutSwitch) : 0;
            obj.sbfileoutcfg.content.mode = temp.approveOut ? Number(temp.approveOut) : 1;
            // obj.sbfileoutcfg.content.disablesc = 1;//temp.forbidScreen?Number(temp.forbidScreen):0
            obj.sbfileoutcfg.content.validtimecheck = temp.settingTime ? Number(temp.settingTime) : 0;
            obj.sbfileoutcfg.content.validtime = temp.settingTimes ? Number(temp.settingTimes) : 7;
            obj.sbfileoutcfg.content.pwdcheck = temp.passwordVerification ? Number(temp.passwordVerification) : 0;
            obj.sbfileoutcfg.content.pwd = temp.passwordVerifications ? Number(temp.passwordVerifications) : 6;
            obj.sbfileoutcfg.content.opencountcheck = temp.allowOpen ? Number(temp.allowOpen) : 0;
            obj.sbfileoutcfg.content.opencount = temp.allowOpens ? Number(temp.allowOpens) : 1;
            obj.sbfileoutcfg.content.autodelete = temp.allowOpenDelete ? Number(temp.allowOpenDelete) : 1;//默认开启自动删除
            obj.sbfileoutcfg.content.scwatermark.enable = temp.isScreenWater ? Number(temp.isScreenWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.isshow = temp.outWater ? Number(temp.outWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.depname = temp.fileOutDept ? Number(temp.fileOutDept) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.username = temp.fileOutName ? Number(temp.fileOutName) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.ip = temp.fileOutIpWater ? Number(temp.fileOutIpWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.macaddress = temp.fileOutMACWater ? Number(temp.fileOutMACWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.computername = temp.fileOutComputerWater ? Number(temp.fileOutComputerWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.time = temp.fileOutTimeWater ? Number(temp.fileOutTimeWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.manual = temp.fileOutDiyWater ? Number(temp.fileOutDiyWater) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.recv = temp.fileOutRecv ? Number(temp.fileOutRecv) : 0;
            obj.sbfileoutcfg.content.scwatermark.content.manualtext = temp.outdiyWaterContent || '';
            obj.sbfileoutcfg.content.scwatermark.content.direction = Number(temp.out_direction)||0;
            obj.sbfileoutcfg.content.scwatermark.content.localtion = Number(out_localtion_totle)||0;
            obj.sbfileoutcfg.content.scwatermark.content.locaktiontemp = '[' + out_localtion_temp.toString() + ']';
            obj.sbfileoutcfg.content.scwatermark.content.color = Number(temp.out_color_content) || 0;
            obj.sbfileoutcfg.content.scwatermark.content.tcolor = $('.fileOut .color-content-title').text();
            obj.sbfileoutcfg.content.scwatermark.content.opacity = Number(temp.out_opcity)||125;
            //文件导出
            var export_localtion_temp = new Array();
            var export_localtion_totle = 0;
            temp.exportlocation = temp.exportlocation||0;
            for (var i = 0; i < temp.exportlocation.length; i++) {
                export_localtion_temp.push(temp.exportlocation[i]);
                export_localtion_totle = (export_localtion_totle + parseInt(temp.exportlocation[i]));
            }
            obj.sbfileopt.enable = temp.fileExportSwitch ? Number(temp.fileExportSwitch) : 0;
            obj.sbfileopt.content.mode = temp.approveExport ? Number(temp.approveExport) : 1;//1是明文3是审批
            obj.sbfileopt.content.sbfileoptwatermark.enable = temp.isScreen ? Number(temp.isScreen) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.isshow = temp.exportWater ? Number(temp.exportWater) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.depname = temp.fileExportDept ? Number(temp.fileExportDept) : 0;

            obj.sbfileopt.content.sbfileoptwatermark.content.username = temp.fileExportName ? Number(temp.fileExportName) : 0;

            obj.sbfileopt.content.sbfileoptwatermark.content.ip = temp.fileExportIpWater ? Number(temp.fileExportIpWater) : 0;

            obj.sbfileopt.content.sbfileoptwatermark.content.macaddress = temp.fileExportMACWater ? Number(temp.fileExportMACWater) : 0;

            obj.sbfileopt.content.sbfileoptwatermark.content.computername = temp.fileExportComputerWater ? Number(temp.fileExportComputerWater) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.time = temp.fileExportTimeWater ? Number(temp.fileExportTimeWater) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.manual = temp.fileExportDiyWater ? Number(temp.fileExportDiyWater) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.recv = temp.fileExportRecv ? Number(temp.fileExportRecv) : 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.manualtext = temp.exportdiyWaterContent || '';
            obj.sbfileopt.content.sbfileoptwatermark.content.direction = Number(temp.export_direction)||0;
            obj.sbfileopt.content.sbfileoptwatermark.content.localtion = Number(export_localtion_totle)||0;;
            obj.sbfileopt.content.sbfileoptwatermark.content.locaktiontemp = '[' + export_localtion_temp.toString() + ']';
            obj.sbfileopt.content.sbfileoptwatermark.content.color = Number(temp.export_color_content) || 0;
            obj.sbfileopt.content.sbfileoptwatermark.content.tcolor = $('.fileExport .color-content-title').text();
            obj.sbfileopt.content.sbfileoptwatermark.content.opacity = Number(temp.export_opcity) || 125;
            //图片审计
            obj.videoappro.enable = temp.videoApprove ? Number(temp.videoApprove) : 1;//默认开启，无论什么都是1
            objAll = {"content": obj, "policyid": policyId};
            $.ajax({
                type: 'post',
                url: ctx + '/policy/updatePolicyJsonFile',
                contentType: 'application/json;charset=utf-8',//指定为json类型
                //数据格式是json串
                data: JSON.stringify(objAll),
                success: function (msg) {//返回json结果
                    if (msg.resultCode == 1) {
                        layer.msg('保存成功！', {icon: 1});
                    }
                    else {
                        layer.msg('保存失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                },
                error: function (msg) {
                    layer.msg('保存错误！' + (msg.resultMsg || ''), {icon: 2});
                }
            });
        })
        //删除策略
        .on('click', '.policy-delete', function () {
            var postData = {};
            postData.policyId = policyId;
            layer.confirm('确定删除该策略吗？', {
                btn: ['确定', '取消']
            }, function () {
                postAjax(ctx + '/policy/deletePolicy', postData, function (msg) {
                    if (msg.resultCode == 1) {
                        // layer.msg('删除成功！', {icon: 1,end:function(){
                        window.location = ctx + '/policy/readPolicyJsonFileById?id=1';
                        // }});
                    }
                    else {
                        layer.msg('保存失败！' + (msg.resultMsg || ''), {icon: 2});
                    }
                });
            });

        })
        //todo 审批流程
        .on('click', '#fileExportApprove,#fileOutApprove', function () {
            if ($(this).is('#fileExportApprove')) {
                //这是导出
                layer.open({
                    id: 'exportOpenWind',
                    type: 1,
                    title: '选择审批流程',
                    content: '',
                    area: ['670px', '300px'],
                    btn: ['确定', '取消'],
                    yes: function (index, layero) {
                        export_file = $("#exportOpenWind input[name=exportSelect]:checked").val();
                        layer.close(index);
                    },
                    success: function (index, layero) {
                        if (export_file == 0) {//说明刚进来  没有点过审批选项
                            var approvehtml = '<div class="content">';
                            for (var i = 0; i < approveList.length; i++) {
                                var ischecked = '';
                                if (msg.sbfileopt.content.flowid == approveList[i].id) ischecked = "checked";
                                approvehtml += '<div class="beauty-radio margin-right-sm w200"><input ' + ischecked + ' id="approveListAll' + approveList[i].id + '" class="beauty-radio-input" type="radio" name="exportSelect" value="' + approveList[i].id + '"> <label for="approveListAll' + approveList[i].id + '" class="beauty-radio-label">' + approveList[i].name + '</label> </div>';
                            }
                            approvehtml += '</div>';
                            $("#exportOpenWind").html(approvehtml);
                        } else {
                            var approvehtml = '<div class="content">';
                            for (var i = 0; i < approveList.length; i++) {
                                var ischecked = '';
                                if (export_file == approveList[i].id) ischecked = "checked";
                                approvehtml += '<div class="beauty-radio margin-right-sm w200"><input ' + ischecked + ' id="approveListAll' + approveList[i].id + '" class="beauty-radio-input" type="radio" name="exportSelect" value="' + approveList[i].id + '"> <label for="approveListAll' + approveList[i].id + '" class="beauty-radio-label">' + approveList[i].name + '</label> </div>';
                            }
                            approvehtml += '</div>';
                            $("#exportOpenWind").html(approvehtml);
                        }

                    }
                })
            } else if ($(this).is('#fileOutApprove')) {
                //这是外发
                layer.open({
                    id: 'outOpenWind',
                    type: 1,
                    title: '选择审批流程',
                    content: '',
                    area: ['670px', '300px'],
                    btn: ['确定', '取消'],
                    yes: function (index, layero) {
                        out_file = $("#outOpenWind input[name=exportSelect]:checked").val();
                        layer.close(index);
                    },
                    success: function (index, layero) {
                        if (out_file == 0) {
                            var approvehtml = '<div class="content">';
                            for (var i = 0; i < approveList.length; i++) {
                                var ischecked = '';
                                if (msg.sbfileoutcfg.content.flowid == approveList[i].id) ischecked = "checked";
                                approvehtml += '<div class="beauty-radio margin-right-sm w200"> <input ' + ischecked + ' id="approveListAll' + approveList[i].id + '" class="beauty-radio-input" type="radio" name="exportSelect" value="' + approveList[i].id + '"> <label for="approveListAll' + approveList[i].id + '" class="beauty-radio-label">' + approveList[i].name + '</label> </div>';
                            }
                            approvehtml += '</div>';
                            $("#outOpenWind").html(approvehtml);
                        } else {
                            var approvehtml = '<div class="content">';
                            for (var i = 0; i < approveList.length; i++) {
                                var ischecked = '';
                                if (out_file == approveList[i].id) ischecked = "checked";
                                approvehtml += '<div class="beauty-radio margin-right-sm w200"> <input ' + ischecked + ' id="approveListAll' + approveList[i].id + '" class="beauty-radio-input" type="radio" name="exportSelect" value="' + approveList[i].id + '"> <label for="approveListAll' + approveList[i].id + '" class="beauty-radio-label">' + approveList[i].name + '</label> </div>';
                            }
                            approvehtml += '</div>';
                            $("#outOpenWind").html(approvehtml);
                        }

                    }
                })
            }
        })
        //点击input框解除disabled
        .on('click', '#diyWater,#settingTime,#passwordVerification,#allowOpen,#fileOutDiyWater,#fileExportDiyWater,#fileExportDiyWater', function () {
            if ($(this).is(":checked")) {
                $(this).parents(".beauty-checkbox").siblings("input[type=text]").prop("disabled", false);
                $(this).parents(".beauty-checkbox").siblings(".beauty-checkbox").find("input").prop("disabled", false);
            } else {
                $("body input[name=screendiyWaterContent]").removeClass('border-error');
                $("body input[name=outdiyWaterContent]").removeClass('border-error');
                $("body input[name=exportdiyWaterContent]").removeClass('border-error');
                $(this).parents(".beauty-checkbox").siblings("input[type=text]").prop("disabled", true);
                $(this).parents(".beauty-checkbox").siblings(".beauty-checkbox").find("input").prop("disabled", true);
            }
        })
        // 屏幕水印那块点选效果
        .on('click', 'input[name=screenWater]', function () {
            if ($("body #screenWaterHidden").is(":checked")) {
                $("body .screenWater .policy-content-con label.ms input").prop("checked", "checked");
                $("body .screenWater .policy-content-con label.ms input").prop("disabled", true);
                $("body .screenWater .policy-content-con label.w300 input[type=checkbox]").prop("disabled", true).prop("checked", false);
                $("body .screenWater .policy-content-con label.w300 input[type=text]").prop("disabled", true).prop("value", "");
                $("body .screenWater .policy-content-con label.w300").hide();
                $('.screen-is-show').hide();

            } else {
                $("body .screenWater .policy-content-con label.ms input").prop("checked", false);
                $("body .screenWater .policy-content-con input[type=checkbox]").prop("disabled", false);
                $("body .screenWater .policy-content-con label.w300 input[type=checkbox]").prop("disabled", false);
                $("body .screenWater .policy-content-con label.w300").show();
                $('.screen-is-show').show();

            }
        })
        // 文件外发水印那块点选效果
        .on('click', 'input[name=outWater]', function () {
            if ($("body #outWaterHidden").is(":checked")) {
                $("body .fileOut .waterShowContent label.ms input").prop("checked", "checked");
                $("body .fileOut .waterShowContent label.ms input").prop("disabled", true);
                $('.outer-is-show').hide();

            } else {
                $("body .fileOut .waterShowContent label.ms input").prop("checked", false);
                $("body .fileOut .waterShowContent input[type=checkbox]").prop("disabled", false);
                $('.outer-is-show').show();

            }
        })
        // 文件导出水印那块点选效果
        .on('click', 'input[name=exportWater]', function () {
            if ($("body #waterHidden").is(":checked")) {
                $("body .fileExport .waterShowContent label.ms input").prop("checked", "checked");
                $("body .fileExport .waterShowContent label.ms input").prop("disabled", true);
                $('.fileExport .bar-item-dropdown').removeClass('j-bar-item-dropdown_export');
                $(".fileExport .color-content-title").text('#ffffff');
                $(".fileExport .color-content-show").css('background-color', '#ffffff');
                $("#export_color_content").val(16777215);
                $('.export-is-show').hide();
            } else {
                $("body .fileExport .waterShowContent label.ms input").prop("checked", false);
                $("body .fileExport .waterShowContent input[type=checkbox]").prop("disabled", false);
                $('.fileExport .bar-item-dropdown').addClass('j-bar-item-dropdown_export');

                $('.export-is-show').show();
            }
        })
        // 屏幕disabled效果
        /*.on('click', 'input[name=screenSwitch]', function () {
            if ($("body #screenSwitch").is(":checked")) {
                $('.fileOut .policy-list input[type=checkbox],.fileOut input[type=radio],.fileOut select').prop("disabled", false);
                $('.fileOut .policy-list select').removeClass('select-disable');
                $('.fileOut .bar-item-dropdown').removeClass('select-disable');
                $('.fileOut .bar-item-dropdown').addClass('j-bar-item-dropdown_out');
                if ($("body #outWaterHidden").is(":checked")) {
                    $("body .fileOut .waterShowContent label.ms input").prop("checked", "checked");
                    $("body .fileOut .waterShowContent label.ms input").prop("disabled", true);
                }
            } else {
                $('.fileOut .policy-list input[type=checkbox],.fileOut input[type=radio],.fileOut select,.fileOut input[type=text]').prop("disabled", true);
                $('.fileOut .policy-list input[type=checkbox]').prop("checked", false);
                $('.fileOut .policy-list select').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').removeClass('j-bar-item-dropdown_out');
            }
        })*/
        // 文件外发disabled效果
        .on('click', 'input[name=isScreenWater]', function () {
            if ($("body #isScreenWater").is(":checked")) {
                $('.fileOut .policy-list input[type=checkbox],.fileOut input[type=radio],.fileOut select').prop("disabled", false);
                $('.fileOut .policy-list select').removeClass('select-disable');
                $('.fileOut .bar-item-dropdown').removeClass('select-disable');
                $('.fileOut .bar-item-dropdown').addClass('j-bar-item-dropdown_out');
                if ($("body #outWaterHidden").is(":checked")) {
                    $("body .fileOut .waterShowContent label.ms input").prop("checked", "checked");
                    $("body .fileOut .waterShowContent label.ms input").prop("disabled", true);
                }
            } else {
                $('.fileOut .policy-list input[type=checkbox],.fileOut input[type=radio],.fileOut select,.fileOut input[type=text]').prop("disabled", true);
                $('.fileOut .policy-list input[type=checkbox]').prop("checked", false);
                $('.fileOut .policy-list select').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').removeClass('j-bar-item-dropdown_out');
            }
        })
        // 文件导出disabled效果
        .on('click', 'input[name=isScreen]', function () {
            if ($("body #isScreen").is(":checked")) {
                $('.fileExport .policy-list input[type=checkbox],.fileExport input[type=radio],.fileExport select').prop("disabled", false);
                $('.fileExport .policy-list select').removeClass('select-disable');
                $('.fileExport .bar-item-dropdown').removeClass('select-disable');
                $('.fileExport .bar-item-dropdown').addClass('j-bar-item-dropdown_export');
                if ($("body #waterHidden").is(":checked")) {
                    $("body .fileExport .waterShowContent label.ms input").prop("checked", "checked");
                    $("body .fileExport .waterShowContent label.ms input").prop("disabled", true);
                }
            } else {
                $('.fileExport .policy-list input[type=checkbox],.fileExport input[type=radio],.fileExport select,.fileExport input[type=text]').prop("disabled", true);
                $('.fileExport .policy-list input[type=checkbox]').prop("checked", false);
                $('.fileExport .policy-list select').addClass('select-disable');
                $('.fileExport .bar-item-dropdown').addClass('select-disable');
                $('.fileExport .bar-item-dropdown').removeClass('j-bar-item-dropdown_export');

            }
        })
        //第一个屏幕
        .on('change', '#screenSwitch', function () {
            if ($(this).is(":checked")) {

                $('.screenWater .policy-con input[type=checkbox],.screenWater .policy-con input[type=radio],.screenWater .policy-con select').prop("disabled", false);
                $('.screenWater .policy-con select').removeClass('select-disable');
                $('.screenWater .bar-item-dropdown').addClass('j-bar-item-dropdown');
                $('.screenWater .policy-con .j-bar-item-dropdown').removeClass('select-disable');
                if($("#screenWaterHidden").is(":checked")){
                    $("body .screenWater .policy-content-con label.ms input").prop("checked", "checked");
                    $("body .screenWater .policy-content-con label.ms input").prop("disabled", true);
                    $("body .screenWater .policy-content-con label.w300 input[type=checkbox]").prop("disabled", true).prop("checked", false);
                    $("body .screenWater .policy-content-con label.w300 input[type=text]").prop("disabled", true).prop("value", "");
                    $("body .screenWater .policy-content-con label.w300").hide();
                    $('.screen-is-show').hide();
                }

            } else {
                $('.screenWater .policy-con input[type=checkbox],.screenWater .policy-con input[type=radio],.screenWater .policy-con select,.screenWater .policy-con input[type=text]').prop("disabled", true);
                $('.screenWater .policy-con input[type=checkbox]').prop("checked", false);
                $('.screenWater .policy-con select').addClass('select-disable');
                $('.screenWater .bar-item-dropdown').addClass('select-disable');
                $('.screenWater .bar-item-dropdown').removeClass('j-bar-item-dropdown');
            }
        })
        //第二个外发
        .on('change', '#fileOutSwitch', function () {
            if ($(this).is(":checked")) {
                $('.fileOut .outC input[type=checkbox],.fileOut .outC input[type=radio],.fileOut .outC select').prop("disabled", false);
                $('.fileOut .outC select').removeClass('select-disable');
            } else {
                $('.fileOut .policy-con input[type=checkbox],.fileOut .policy-con input[type=radio],.fileOut .policy-con select,.fileOut .policy-con input[type=text]').prop("disabled", true);
                $('.fileOut .policy-con input[type=checkbox]').prop("checked", false);
                $('.fileOut .policy-con select').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').addClass('select-disable');
                $('.fileOut .bar-item-dropdown').removeClass('j-bar-item-dropdown_out');
                $(".fileOutApprove").prop("id","temp");
                $(".fileOutApprove").addClass("color-grey");

            }
        })
        //第三个导出
        .on('change', '#fileExportSwitch', function () {
            if ($(this).is(":checked")) {
                $('.fileExport .export-c input[type=checkbox],.fileExport .export-c select').prop("disabled", false);
                $('.fileExport .export-c select').removeClass('select-disable');

            } else {
                $('.fileExport .policy-con input[type=checkbox],.fileExport .policy-con input[type=radio],.fileExport .policy-con select,.fileExport .policy-con input[type=text]').prop("disabled", true);
                $('.fileExport .policy-con input[type=checkbox]').prop("checked", false);
                $('.fileExport .policy-con select').addClass('select-disable');
                $('.fileExport .bar-item-dropdown').addClass('select-disable');
                $('.fileExport .bar-item-dropdown').removeClass('j-bar-item-dropdown_export');
                $(".fileExportApprove").prop("id","temp");
                $(".fileExportApprove").addClass("color-grey");

            }
        })
        // 下拉列表屏幕
        .on('click', '.j-bar-item-dropdown', function () {
            $(".j-color-list").slideToggle("fast");
        })
        // 下拉列表外发
        .on('click', '.j-bar-item-dropdown_out', function () {
            $(".j-color-list_out").slideToggle("fast");
        })
        // 下拉列表导出
        .on('click', '.j-bar-item-dropdown_export', function () {
            $(".j-color-list_export").slideToggle("fast");
        })
        // 外发审批勾选后自动点开流程
        .on('change', '#approveOut', function () {
            if ($(this).is(":checked")) {
                $(".fileOutApprove").prop("id","fileOutApprove");
                $(".fileOutApprove").removeClass("color-grey");
                $(this).closest('.beauty-checkbox').siblings('#fileOutApprove').click();
            }else {
                $(".fileOutApprove").prop("id","temp");
                $(".fileOutApprove").addClass("color-grey");
            }
        })
        // 导出审批勾选后自动点开流程
        .on('change', '#approveExport', function () {
            if ($(this).is(":checked")) {
                $(".fileExportApprove").prop("id","fileExportApprove");
                $(".fileExportApprove").removeClass("color-grey");
                $(this).closest('.beauty-checkbox').siblings('#fileExportApprove').click();
            }else {
                $(".fileExportApprove").prop("id","temp");
                $(".fileExportApprove").addClass("color-grey");
            }
        })
        //填充颜色屏幕
        .on('click', '.j-color-list li', function () {
            var color = $(this).data('color');
            colorFirst = color;
            var text = $(this).text();
            $(".screenWater .color-content-title").text(text);
            $(".screenWater .color-content-show").css('background-color', '#' + color);
            $("#color_content").val(parseInt(color, 16));

        })
        //填充颜色外发
        .on('click', '.j-color-list_out li', function () {
            var color = $(this).data('color');
            colorSec = color;
            var text = $(this).text();
            $(".fileOut .color-content-title").text(text);
            $(".fileOut .color-content-show").css('background-color', '#' + color);
            $("#out_color_content").val(parseInt(color, 16));

        })
        //填充颜色导出
        .on('click', '.j-color-list_export li', function () {
            var color = $(this).data('color');
            colorTh = color;
            var text = $(this).text();
            $(".fileExport .color-content-title").text(text);
            $(".fileExport .color-content-show").css('background-color', '#' + color);
            $("#export_color_content").val(parseInt(color, 16));

        })
        //水印样式交互
        .on('click', '.j-check-local', function () {
            if ($(this).data('val') == 0) {
                $(this).closest('label.margin-bottom-sm').siblings('.j-location-content').hide();
                $(this).closest('label').siblings('.j-location-content').find('input').prop("disabled", true);

            } else {
                $(this).closest('label').siblings('.j-location-content').show();
                $(this).closest('label').siblings('.j-location-content').find('input').prop("disabled", false);
            }
        })

    //屏幕
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.j-bar-item-dropdown').hasClass('j-bar-item-dropdown')) {
            if ($(e.target).parents('.colpick').css('display') == 'block') {

            } else {
                $('.j-color-list').slideUp("fast");
            }

        }
    });
    //外发
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.j-bar-item-dropdown_out').hasClass('j-bar-item-dropdown_out')) {
            if ($(e.target).parents('.colpick').css('display') == 'block') {

            } else {
                $('.j-color-list_out').slideUp("fast");
            }

        }
    });
    //导出
    $(document).on("click", function (e) {
        if (!$(e.target).closest('.j-bar-item-dropdown_export').hasClass('j-bar-item-dropdown_export')) {
            if ($(e.target).parents('.colpick').css('display') == 'block') {

            } else {
                $('.j-color-list_export').slideUp("fast");
            }

        }
    });
    //选择出现颜色屏幕
    $('#diycolor').colpick({
        colorScheme: 'light',
        layout: 'rgbhex',
        color: colorFirst,
        onShow: function (el) {
            screenColor = $(el);

        },
        onSubmit: function (hsb, hex, rgb, el) {
            //hex是颜色的色值
            $(".screenWater .color-content-title").text('#' + hex);
            $(".screenWater .color-content-show").css('background-color', '#' + hex);
            $("#color_content").val(parseInt(hex, 16));
            $(el).colpickHide();
            $(".j-color-list").slideToggle("fast");

        }
    })
    //选择出现颜色外发
    $('#out_diycolor').colpick({
        colorScheme: 'light',
        layout: 'rgbhex',
        color: colorSec,
        onShow: function (el) {
            outColor = $(el);
        },
        onSubmit: function (hsb, hex, rgb, el) {
            //hex是颜色的色值
            $(".fileOut .color-content-title").text('#' + hex);
            $(".fileOut .color-content-show").css('background-color', '#' + hex);
            $("#out_color_content").val(parseInt(hex, 16));
            $(el).colpickHide();
            $(".j-color-list_out").slideToggle("fast");

        }
    })
    //选择出现颜色导出
    $('#export_diycolor').colpick({
        colorScheme: 'light',
        layout: 'rgbhex',
        color: colorTh,
        onShow: function (el) {
            exportColor = $(el);
            var deviceWidth = window.innerWidth;
            if (deviceWidth < 1500) {
                var pickwidth = 448;
                if ($('#menus').css('width') == '52px') {
                    pickwidth = 301;
                }
                $(el).css('left', pickwidth)
            } else {
                var pickwidth = 449;
                if ($('#menus').css('width') == '52px') {
                    pickwidth = 301;
                }
                $(el).css('left', pickwidth)
            }

        },
        onSubmit: function (hsb, hex, rgb, el) {
            //hex是颜色的色值
            $(".fileExport .color-content-title").text('#' + hex);
            $(".fileExport .color-content-show").css('background-color', '#' + hex);
            $("#export_color_content").val(parseInt(hex, 16));
            $(el).colpickHide();
            $(".j-color-list_export").slideToggle("fast");

        }
    })
}

// 获取所有的审批流程
function getAllApprove() {

    getAjax(ctx + '/approveDefinition/getAllApproveDefinition', '', function (msg) {
        if (msg.resultCode == 1) {

            approveList = msg.data;
        }

    });

}

// 页面中的js交互
function allChecked() {
    //位置的交互

    var temp = msg.sbscrnwatermark.content.locaktiontemp;
    for (var i = 0; i < temp.length; i++) {
        if (temp[i] == 1) {
            $("#top_left_corner").prop('checked', 'checked');
        }
        if (temp[i] == 2) {
            $("#top_right_corner").prop('checked', 'checked');
        }
        if (temp[i] == 4) {
            $("#center_corner").prop('checked', 'checked');
        }
        if (temp[i] == 8) {
            $("#bottom_left_corner").prop('checked', 'checked');
        }
        if (temp[i] == 16) {
            $("#bottom_right_corner").prop('checked', 'checked');
        }
    }
    if ($('#location_tile').is(":checked")) {
        $('.j-check-local-all').prop("disabled", true);
    }
    var color = $('#color_content').val();
    switch (color) {
        case "16711680":
            $('.screenWater .color-content-show').css('background-color', '#FF0000');
            colorFirst = '#FF0000';
            break;
        case "16777215":
            $('.screenWater .color-content-show').css('background-color', '#ffffff');
            colorFirst = '#ffffff';
            break;
        case "65280":
            $('.screenWater .color-content-show').css('background-color', '#00ff00');
            colorFirst = '#00ff00';

            break;
        case "65535":
            $('.screenWater .color-content-show').css('background-color', '#00ffff');
            colorFirst = '#00ffff';
            break;
        case "0":
            $('.screenWater .color-content-show').css('background-color', '#000000');
            colorFirst = '000000';
            break;
        default:
            var tempColor = (Number(color)).toString(16);
            switch (tempColor.length){
                case 5 :
                    tempColor = '0'+tempColor;
                    break;
                case 4 :
                    tempColor = '00'+tempColor;
                    break;
                case 3 :
                    tempColor = '000'+tempColor;
                    break;
                case 2 :
                    tempColor = '0000'+tempColor;
                    break;
                case 1 :
                    tempColor = '00000'+tempColor;
                    break;
            }
            $('.screenWater .color-content-show').css('background-color', '#' + tempColor);
            colorFirst = tempColor;
            break;

    }
    //外发
    var temp_out = msg.sbfileoutcfg.content.scwatermark.content.locaktiontemp;
    for (var i = 0; i < temp_out.length; i++) {
        if (temp_out[i] == 1) {
            $("#out_top_left_corner").prop('checked', 'checked');
        }
        if (temp_out[i] == 2) {
            $("#out_top_right_corner").prop('checked', 'checked');
        }
        if (temp_out[i] == 4) {
            $("#out_center_corner").prop('checked', 'checked');
        }
        if (temp_out[i] == 8) {
            $("#out_bottom_left_corner").prop('checked', 'checked');
        }
        if (temp_out[i] == 16) {
            $("#out_bottom_right_corner").prop('checked', 'checked');
        }
    }
    if ($('#out_location_tile').is(":checked")) {
        $('.j-check-local-all-out').prop("disabled", true);
    } else {
        $('.j-check-local-all-out').prop("disabled", false);
    }
    var out_color = $('#out_color_content').val();
    switch (out_color) {
        case "16711680":
            $('.fileOut .color-content-show').css('background-color', '#FF0000');
            colorSec = '#FF0000';
            break;
        case "16777215":
            $('.fileOut .color-content-show').css('background-color', '#ffffff');
            colorSec = '#ffffff';
            break;
        case "65280":
            $('.fileOut .color-content-show').css('background-color', '#00ff00');
            colorSec = '#00ff00';
            break;
        case "65535":
            $('.fileOut .color-content-show').css('background-color', '#00ffff');
            colorSec = '#00ffff';
            break;
        case "0":
            $('.fileOut .color-content-show').css('background-color', '#000000');
            colorSec = '#000000';
            break;
        default:
            var tempColor = (Number(out_color)).toString(16);
            switch (tempColor.length){
                case 5 :
                    tempColor = '0'+tempColor;
                    break;
                case 4 :
                    tempColor = '00'+tempColor;
                    break;
                case 3 :
                    tempColor = '000'+tempColor;
                    break;
                case 2 :
                    tempColor = '0000'+tempColor;
                    break;
                case 1 :
                    tempColor = '00000'+tempColor;
                    break;
            }
            $('.fileOut .color-content-show').css('background-color', '#' + tempColor);
            colorSec = tempColor;
            break;

    }
    //导出
    var temp_export = msg.sbfileopt.content.sbfileoptwatermark.content.locaktiontemp;
    for (var i = 0; i < temp_export.length; i++) {
        if (temp_export[i] == 1) {
            $("#export_top_left_corner").prop('checked', 'checked');
        }
        if (temp_export[i] == 2) {
            $("#export_top_right_corner").prop('checked', 'checked');
        }
        if (temp_export[i] == 4) {
            $("#export_center_corner").prop('checked', 'checked');
        }
        if (temp_export[i] == 8) {
            $("#export_bottom_left_corner").prop('checked', 'checked');
        }
        if (temp_export[i] == 16) {
            $("#export_bottom_right_corner").prop('checked', 'checked');
        }
    }
    if ($('#export_location_tile').is(":checked")) {
        $('.j-check-local-all-export').prop("disabled", true);
    } else {
        $('.j-check-local-all-export').prop("disabled", false);
    }
    var export_color = $('#export_color_content').val();
    switch (export_color) {
        case "16711680":
            $('.fileExport .color-content-show').css('background-color', '#FF0000');
            colorTh = '#FF0000';
            break;
        case "16777215":
            $('.fileExport .color-content-show').css('background-color', '#ffffff');
            colorTh = '#ffffff';
            break;
        case "65280":
            $('.fileExport .color-content-show').css('background-color', '#00ff00');
            colorTh = '#00ff00';
            break;
        case "65535":
            $('.fileExport .color-content-show').css('background-color', '#00ffff');
            colorTh = '#00ffff';
            break;
        case "0":
            $('.fileExport .color-content-show').css('background-color', '#000000');
            colorTh = '#000000';
            break;
        default:
            var tempColor = (Number(export_color)).toString(16);
            switch (tempColor.length){
                case 5 :
                    tempColor = '0'+tempColor;
                    break;
                case 4 :
                    tempColor = '00'+tempColor;
                    break;
                case 3 :
                    tempColor = '000'+tempColor;
                    break;
                case 2 :
                    tempColor = '0000'+tempColor;
                    break;
                case 1 :
                    tempColor = '00000'+tempColor;
                    break;
            }
            $('.fileExport .color-content-show').css('background-color', '#' + tempColor);
            colorTh = tempColor;
            break;

    }
    //其他可用不可用的交互

    if ($("body #diyWater").is(":checked")) {
        $("body input[name=screendiyWaterContent]").removeAttr("disabled");
    }
    if ($("body #settingTime").is(":checked")) {
        $("body input[name=settingTimes]").removeAttr("disabled");
    }
    if ($("body #passwordVerification").is(":checked")) {
        $("body input[name=passwordVerifications]").removeAttr("disabled");
    }
    if ($("body #allowOpen").is(":checked")) {
        $("body input[name=allowOpens]").removeAttr("disabled");
        $("body input[name=allowOpenDelete]").removeAttr("disabled");
    }
    if ($("body #fileOutDiyWater").is(":checked")) {
        $("body input[name=outdiyWaterContent]").removeAttr("disabled");
    }
    if ($("body #fileExportDiyWater").is(":checked")) {
        $("body input[name=exportdiyWaterContent]").removeAttr("disabled");
    }
    if ($("body #screenWaterHidden").is(":checked")) {
        $("body .screenWater .policy-content-con label.ms input").prop("checked", "checked");
        $("body .screenWater .policy-content-con label.ms input").prop("disabled", true);
        $("body .screenWater .policy-content-con label.w300 input").prop("disabled", true);
        $("body .screenWater .policy-content-con label.w300").hide();
    }
    if ($("body #outWaterHidden").is(":checked")) {
        $("body .fileOut .waterShowContent label.ms input").prop("checked", "checked");
        $("body .fileOut .waterShowContent label.ms input").prop("disabled", true);
    }
    if ($("body #waterHidden").is(":checked")) {
        $("body .fileExport .waterShowContent label.ms input").prop("checked", "checked");
        $("body .fileExport .waterShowContent label.ms input").prop("disabled", true);
        $('.fileExport .bar-item-dropdown').removeClass('j-bar-item-dropdown_export');
    }
    if ($("body #screenSwitch").is(":checked")) {
        $("body input[name=screenWater]").removeAttr("disabled");
    }
    if ($("body #isScreenWater").is(":checked")) {
        $("body input[name=outWater]").removeAttr("disabled");
    }
    if ($("body #isScreen").is(":checked")) {
        $("body input[name=exportWater]").removeAttr("disabled");
    }
    //第一个
    if ($("body #screenSwitch").is(":checked")) {
        $('body .location_diy').prop("disabled", false);
    } else {
        $('.screenWater .policy-con input[type=checkbox]').prop("disabled", true);
    }
    //第二个
    if ($("body #fileOutSwitch").is(":checked")) {

    } else {
        $('.fileOut .outC input[type=checkbox]').prop("disabled", true);
        $('.fileOut .policy-list .waterShowContent input[type=checkbox]').prop("disabled", true);
    }
    //第三个
    if ($("body #fileExportSwitch").is(":checked")) {

    } else {
        $('.fileExport .export input[type=checkbox]').prop("disabled", true);
        $('.fileExport .waterShowContent input[type=checkbox]').prop("disabled", true);
    }

}