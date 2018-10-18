function splitComma(str) {
    var result = str.split(",");
    return result;
}

function _compare_ip(begin, end) {
    var temp1 = begin.split(".")
    var temp2 = end.split(".")

    for (var j = 0; j < 4; j++) {
        if (parseInt(temp1[j]) > parseInt(temp2[j])) {
            return 1;
        } else if (parseInt(temp1[j]) < parseInt(temp2[j])) {
            return -1;
        }
    }

    return 0;

}

function valid_ip(ip_range) {
    // var re = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/;
    var re = /^([1-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.([1-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-4])$/;
    var ips = ip_range.split("-")
    if (ips.length == 1) {
        if (re.test(ips[0])) {
            return true
        }
    } else if (ips.length == 2) {
        if (re.test(ips[0]) && re.test(ips[1])) {
            if (_compare_ip(ips[0], ips[1]) <= 0) {
                return true;
            }
        }
    }

    return false;
}

function checkIp(nameip) {
    var val = nameip;
    var array = val.split(";")
    for (var i = 0; i < array.length; i++) {
        if (!valid_ip(array[i])) {
            return false
        }
    }
    return true
}


//登陆超时重定向
$.ajaxSetup({
    complete: function (xhr, textStatus) {
        if (xhr.getResponseHeader('isRedirect') == 'yes') {
            location.href = "/tdp/login";
        }
    }
});

//获取cookie
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    }
    else {
        return null;
    }
}

//删除cookie
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) {
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ';path=/';
    }
}

var csrftoken_ = getCookie("XSRF-TOKEN");
var csrfHeader_ = getCookie("XSRF-HEADER-NAME");

if (csrftoken_ != null && csrfHeader_ != null) {
    $.ajaxSetup({
            beforeSend: function (xhr) {
                if (csrfHeader_ && csrftoken_) {
                    xhr.setRequestHeader(csrfHeader_, csrftoken_);
                }
            }
        }
    );
}
/*
//弹窗input回车点确定
$(document).on('keydown', '.layui-layer-page input:not([enter])', function (e) {
        if (e.keyCode == 13) {
            $(this).closest('.layui-layer-page').find('.layui-layer-btn0').click();
            return false;
        }
    })
//datatables 增加默认配置
$.extend(true, $.fn.dataTable.defaults, {
    "searching": true,//关闭Datatables的搜索功能:
    "destroy": true,//摧毁一个已经存在的Datatables，然后创建一个新的
    "retrieve": true, //检索已存在的Datatables实例,如果已经初始化了，则继续使用之前的Datatables实例
    "autoWidth": true,//自动计算列宽
    "processing": false,//是否显示正在处理的状态
    "stateSave": false, //开启或者禁用状态储存。当你开启了状态储存，Datatables会存储一个状态到浏览器上， 包含分页位置，每页显示的长度，过滤后的结果和排序。当用户重新刷新页面，表格的状态将会被设置为之前的设置。
    "serverSide": true,//服务器端处理模式——此模式下如：过滤、分页、排序的处理都放在服务器端进行。
    "scrollY": "auto",//控制表格的垂直滚动。
    "pagingType": "full_numbers",
    /!*l - Length changing 改变每页显示多少条数据的控件
     f - Filtering input 即时搜索框控件
     t - The Table 表格本身
     i - Information 表格相关信息控件
     p - Pagination 分页控件
     r - pRocessing 加载等待显示信息*!/
    "pageLength": 20,
    "dom": 'rlfrtip',
    "stateLoadParams": function (settings, data) { //状态加载完成之后，对数据处理的回调函数
    },
    "lengthMenu": [
        [20, 30, 50, 100],
        ["20", "30", "50", "100"]
    ],//定义在每页显示记录数的select中显示的选项
    "oLanguage": {
        // "sLengthMenu": "_MENU_",
        "sZeroRecords": "暂无数据",
        "sEmptyTable": "暂无数据",
    },
})
//在返回json与前台设置不能对应时屏蔽掉alert 而在控制台输出错误
// $.fn.dataTable.ext.errMode = 'throw'
// 单个验证
function singleValidate(selector,content){
    $(selector).after('<span style="color: red;margin-left: 10px;vertical-align: middle" class="error-span">'+content+'</span>');
    setTimeout(function(){
        $('body .error-span').remove();
    },3000)

}
function firstFocus(){
    $("body #openWind input[type=text]:first").focus();

}
*/