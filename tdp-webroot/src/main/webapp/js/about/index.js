changeq("incCopyFileLinux_oms", "incTrueFileLinux_oms");

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
}