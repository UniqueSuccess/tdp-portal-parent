// JavaScript Document
//支持Enter键登录
document.onkeydown = function (e) {
    if ($(".bac").length == 0) {
        if (!e) e = window.event;
        if ((e.keyCode || e.which) == 13) {
            $('#submit_btn').click();
        }
    }
}
$(function () {
    $('#name').focus();
    $('#name').val(localStorage.name);
    $("#password").focus(function () {
        if(IEVersion() == "-1"){//说明刚不是ie
            capitalTip('password');
        }

    });
    //提交表单
    $('#submit_btn').click(function () {
        var name = $("#name").val();
        name = $.trim(name);
        var password = $("#password").val();
        password = $.trim(password);
        if (name == '' || password == '') {
            $("#errorMessage").html("用户名或密码不能为空");
            return;
        }

        postAjax('loginVal', {userName: name,password:encrypt(password).toUpperCase()}, function (msg) {
            if (msg.resultCode == 1) {
                localStorage.name = name;
                $("#password").val(encrypt(password).toUpperCase());
                $("#loginForm").submit();
            } else {
                $("#errorMessage").html("用户名或密码错误");
                return;
            }
        });

    });
    //更换验证码
    $('#img_code').click(function () {
        $(this).attr('src', $(this).attr('data-src') + '?' + Math.random());
    });
});
function capitalTip(id) {
    var capital = false; //聚焦初始化，防止刚聚焦时点击Caps按键提示信息显隐错误

    // 获取大写提示的标签，并提供大写提示显示隐藏的调用接口
    var capitalTip = {
        $elem: $('#capital_' + id),
        toggle: function (s) {
            if (s === 'none') {
                this.$elem.hide();
            } else if (s === 'block') {
                this.$elem.show();
            } else if (this.$elem.is(':hidden')) {
                this.$elem.show();
            } else {
                this.$elem.hide();
            }
        }
    }
    $('#' + id).on('keydown.caps', function (e) {
        if (e.keyCode === 20 && capital) { // 点击Caps大写提示显隐切换
            capitalTip.toggle();
        }
    }).on('focus.caps', function (e) {
        capital = false;
    }).on('keypress.caps', function (e) {
        capsLock(e)
    }).on('blur.caps', function (e) {

        //输入框失去焦点，提示隐藏
        capitalTip.toggle('none');
    });

    function capsLock(e) {
        var keyCode = e.keyCode || e.which;// 按键的keyCode
        var isShift = e.shiftKey || keyCode === 16 || false;// shift键是否按住
        if (keyCode === 9) {
            capitalTip.toggle('none');
        } else {
            //指定位置的字符的 Unicode 编码 , 通过与shift键对于的keycode，就可以判断capslock是否开启了
            // 90 Caps Lock 打开，且没有按住shift键
            if (((keyCode >= 65 && keyCode <= 90) && !isShift) || ((keyCode >= 97 && keyCode <= 122) && isShift)) {
                // 122 Caps Lock打开，且按住shift键
                capitalTip.toggle('block'); // 大写开启时弹出提示框
                capital = true;
            } else {
                capitalTip.toggle('none');
                capital = true;
            }
        }
    }
};
function postAjax(url, data, callback, isDebug) {
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        success: function (response, status, xhr) {
            callback(response, status, xhr);
        },
        error: function (xhr, errorText, errorStatus) {
            callback({
                resultCode: 0,
                resultMsg: errorText
            }, errorStatus, xhr);
        }
    })
}
