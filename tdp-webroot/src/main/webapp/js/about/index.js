var aboutVUE = new Vue({
    el: "#about",
    data: {
        version:"6.0.0",
        updateDate:"2018年10月26日 12:31:53"
        /*authDeviceNum:authInfo.authDeviceNum,//授权台数
        authmsg:authInfo.authmsg,//
        beginEndDate:authInfo.beginEndDate,//授权有效期
        company:authInfo.company,//公司名称
        deviceUnique:authInfo.deviceUnique,//授权码
        supportDate:authInfo.supportDate*/

    },
    methods: {
        // 点击选取文件
        getFile:function() {
            $('#importFile').click();
        },
        // 显示准备上传的文件名
        fillFileName:function(src, target) {
            $('.' + target).html($('#' + src)[0].files[0].name);
            // 当有文件准备上传时【导入】可用
            $('#authenticate').removeAttr('disabled');
        }
    }
})

$(function(){
    $(document)
        .on('click', '#authenticate', function(){
            var incCopyFileLinux = $(".fileNameShow").html();
            if (incCopyFileLinux == "") {
                gd.showWarning('请选择授权文件');
                return false;
            }
            $('#uploadTemplate').ajaxSubmit({
                type: "post",
                url: ctx + '/about/uploadfile',
                // data: flagSymbal,
                // beforeSubmit:function () {},
                success: function (msg) {
                    if (msg.resultCode == 1) {
                        gd.showSuccess('授权成功');
                        setTimeout(function(){
                            window.location.href = window.location.href;
                        },1000);
                    } else {
                        gd.showError('授权失败');
                    }
                },
            });

            /*var formData = new FormData($("#uploadTemplate")[0]);
            $.ajax({
                url: ctx + '/about/uploadfile',
                type: "post",
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                dataType: "json",
                data: formData,
                success: function (msg) {
                    if (msg.resultCode == 0) {
                        gd.showSuccess('授权成功');
                        setTimeout(function(){
                            window.location.href = window.location.href;
                        },1000);
                    } else {
                        gd.showError('授权失败')
                    }
                },
                error: function (res) {
                    gd.showError('授权失败');
                }
            });*/
        })
        .on('click','#save_authentication',function(){
            $.ajax({
                url: ctx + "/about/isFileExist",
                type: "post",
                dataType: "json",
                success: function (msg) {
                    if (msg == 'success') {
                        window.location = ctx + '/about/fileload'
                    } else {
                        gd.showWarning("授权文件不存在");
                    }
                },
                error: function (e) {
                    gd.showError("下载失败");
                }
            });
        })
})




/*
//drs选择文件
$('#btn_select_oms').click(function () {
    $(this).closest('li').find('.tip').hide();
    $('#incTrueFileLinux_oms').click();
});
//drs授权
$("#getauthority_oms").click(function () {
    var incCopyFileLinux = $("#incCopyFileLinux_oms").val();
    if (incCopyFileLinux == "") {
        $("#installTarLinuxTip_oms").html("请选择授权文件").show();
        return false;
    }
    var formData = new FormData($("#beanForm_oms")[0]);
    $.ajax({
        url: ctx + "/about/uploadfile",
        type: "post",
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        data: formData,
        success: function (msg) {
            if (msg.resultCode == '1') {
                layer.msg("导入成功", {
                    icon: 1,
                    end: function () {
                        window.location.reload();
                    }
                });
            } else {
                layer.msg("导入失败！" + (msg.resultMsg || ''), { icon: 2 });
            }
        },
        error: function (e) {
            layer.msg("导入失败", { icon: 2 });
        }
    });
})
//drs导出授权
function isFileExistOMS() {
    $.ajax({
        url: ctx + "/about/isFileExist",
        type: "post",
        dataType: "json",
        success: function (msg) {
            if (msg == 'success') {
                $("#downLoadFile_oms>p").trigger('click');
            } else {
                layer.msg("授权文件不存在", { icon: 7 });
            }
        },
        error: function (e) {
            layer.msg("下载失败", { icon: 2 });
        }
    });
}*/
