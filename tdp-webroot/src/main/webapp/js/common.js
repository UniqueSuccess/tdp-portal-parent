/**
 * 判断字符串是否为空
 */
String.prototype.isEmpty = function () {
    return this.trim() === '';
}
/**
 * 字符串格式化
 */
String.prototype.format = function () {
    if (arguments.length == 0) return this;
    for (var str = this, i = 0; i < arguments.length; i++)
        str = str.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
    return str;
};
/**
 * 取数组最后一个元素
 */
Array.prototype.last = function () {
    return this[this.length - 1];
}
//获取文件名
function getfilename(filePath) {
  var pos = filePath.lastIndexOf("\\");
  return filePath.substring(pos + 1);
}
/**
 * 获取url传参
 */
function query(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var param = window.location.search.substr(1).match(reg);
    if (param !== null) {
        return unescape(param[2]);
    } else {
        return '';
    }
}
/**
 * 日期格式化
 */
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/**
 * 表单序列化为JSON
 */
$.fn.serializeJSON = function () {
    var serializeObj = {};
    var array = this.serializeArray();
    var str = this.serialize();
    $(array).each(function () {
        if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
                serializeObj[this.name].push(this.value);
            } else {
                serializeObj[this.name] = [serializeObj[this.name], this.value];
            }
        } else {
            serializeObj[this.name] = this.value;
        }
    });
    return serializeObj;
};
/**
 * 数组与对象的深复制
 *
 */
function deepCopy(obj) {
    if (typeof obj == 'object') {
        return JSON.parse(JSON.stringify(obj));
    }
    else {
        return obj
    };
}
/**
 * checkbox美化
 */
function beaytyCheckbox(selector, callback) {
    $('body').off('click', selector).on('click', selector, function () {
        var target = $(this).find('input[type="checkbox"]').not('.disabled');
        if (target.length > 0) {
            $(target).prop('checked', !$(target).prop('checked')).removeClass('half');
            /**回调，方便更改选中状态后做一些事情 */
            if (callback) {
                callback($(this));
            }
        }
    })
}
/**
 * 通用Delete Ajax
 */
function deleteAjax(url, data, callback) {
    $.ajax({
        type: 'delete',
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
/**
 * 通用Put Ajax
 */
function putAjax(url, data, callback) {
    $.ajax({
        type: 'put',
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
/**
 * 通用Get Ajax
 */
function getAjax(url, data, callback) {
    $.ajax({
        type: 'get',
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
/**
 * 通用Post Ajax
 */
function postAjax(url, data, callback, isDebug) {
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        timeout:"100000",
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
// 字符串转数组，相当于php的strlen方法
function getsplit(val){
  return String(val).split('');
}
// dataTable的回调
function tableCallback(ele){

  var oTable = $("#"+ele).DataTable();
  //设置每一列的title
  $("table").find("tr td:not(:last-child)").each(function (index, obj) {
    $(obj).attr("title", $(obj).text());
  })
    //设置分页的title
  $(".dataTables_paginate .paginate_button").each(function (index, obj) {
      $(obj).attr("title", $(obj).text());
  })
  //添加跳转到指定页
  $(".dataTables_paginate").append("<input style='height:28px;line-height:28px;width:28px;margin: 0 5px' class='margin text-center' id='changePage' type='text'> <span style='font-size: 14px;'></span>  <a class='shiny' href='javascript:void(0);' id='dataTable-btn'>GO</a>");
  $('#dataTable-btn').click(function (e) {
    var redirectpage =0
    if ($("#changePage").val() && $("#changePage").val() > 0) {

      if(oTable.page.info().pages < $("#changePage").val()){
        redirectpage = oTable.page.info().pages-1
      } else {
        redirectpage = $("#changePage").val() - 1;
      }

    } else {
      redirectpage = 0;
    }
    oTable.page(redirectpage).draw( 'page' );
  });
  $('.dataTables_length').css('right',$('.dataTables_paginate').width()+20);

  //键盘事件  回车键 跳页
  $("#changePage").keydown(function () {
    var e = event || window.event;
    if (e && e.keyCode == 13) {
      var redirectpage =0
      if ($("#changePage").val() && $("#changePage").val() > 0) {
        if(oTable.page.info().pages < $("#changePage").val()){
          redirectpage = oTable.page.info().pages-1
        } else {
          redirectpage = $("#changePage").val() - 1;
        }
      } else {
        redirectpage = 0;
      }
      oTable.page(redirectpage).draw( 'page' );
    }
  })
  // 数字校验
  $('#changePage').bind("propertychange input",function(){
    this.value=this.value.replace(/\D/g,'');
  });
}
// 原生crc32生成
function GetCrc32(Instr) {
  if(typeof(window.Crc32Table)!="undefined")return;
  window.Crc32Table=new Array(256);
  var i,j;
  var Crc;
  for(i=0; i<256; i++)
  {
    Crc=i;
    for(j=0; j<8; j++)
    {
      if(Crc & 1)
        Crc=((Crc >> 1)& 0x7FFFFFFF) ^ 0xEDB88320;
      else
        Crc=((Crc >> 1)& 0x7FFFFFFF);
    }
    Crc32Table[i]=Crc;
  }
  if (typeof Instr != "string") Instr = "" + Instr;
  Crc=0xFFFFFFFF;
  for(i=0; i<Instr.length; i++)
    Crc=((Crc >> 8)&0x00FFFFFF) ^ Crc32Table[(Crc & 0xFF)^ Instr.charCodeAt(i)];
  Crc ^=0xFFFFFFFF;
  return Crc;
}