$(function () {
    // 菜单////////////////////////////
    var menubtn = $("#menubtn"); // 菜单显示隐藏按钮
    var menus = $("#menus"); // 菜单

    var mli0s = $(".mli0"); // 一级菜单列表
    var mli1s = $("body .mli1"); // 二级菜单列表
    var mli2s = $(".mli2"); // 三级级菜单列表


    var mul1 = $(".mul1"); // 二级菜单容器
    var mul2 = $(".mul2"); // 三级菜单容器

    var wcontent = $("#wcontent"); // 右侧内容区域
    var leftMargin;

    // 加载状态
    var menushow = 0; // 菜单收起折叠 0 展开 1 收起
    if ($(window).width() > 1400) {
        if (typeof localStorage.menushow != "undefined") {
            menushow = localStorage.menushow;
        }
        showhidemenu();
    } else if(1300<$(window).width() < 1400){
        //在小屏下，必须收起来侧边栏，要不首页宽度太小
        if (typeof localStorage.menushow != "undefined") {
            menushow = localStorage.menushow;
        }else {
            menushow =1;
        }

        showhidemenu();
    }else{

        menushow = 0;
        $("#wlogo").css("background-position-x", "13px");
        $(".approveNum_first").addClass('approveNumLittle');
    }

    // 显示隐藏按钮方法
    function showhidemenu() {
        if (menushow == 0) {
            leftMargin = 200;
            // 展开
            menus.width(leftMargin);
            wcontent.css("left", leftMargin + "px");
            mul1.parent().addClass("havelist");
            menushow = 1;
            $("#wlogo").css("background-position-x", "center");
            $(".namehide").removeClass("namehide").addClass("nameshow");
          $(".approveNum_first").removeClass('approveNumLittle');
            localStorage.menushow = 0;
        } else {
            leftMargin = 52;
            // 隐藏
            menus.width(leftMargin);
            wcontent.css("left", leftMargin + "px");
            mul1.parent().removeClass("havelist");
            menushow = 0;
            $("#wlogo").css("background-position-x", "13px");
            $(".nameshow").removeClass("nameshow").addClass("namehide");
          $(".approveNum_first").addClass('approveNumLittle')
            localStorage.menushow = 1;
        }
    }
    mul2.parent().addClass("havelist"); // 加载所有二级菜单有子菜单的标志

    // 菜单切换按钮事件
    menubtn.click(function () {
        showhidemenu();
        if ($("#menus").width() == 200) {
          $("#VdpPolicyMenu").find('.policyul1').css({"left":"200px"});
          $(".approveNum_first").removeClass('approveNumLittle')
            // if ($(".nav_alarm").parent("a").hasClass("listclick")) {
            //     $("#deviceList").css({
            //         "width": $('.dataTables_scrollBody').width()
            //     })
            // } else {
            //     $("#deviceList").css({
            //         "width": "auto"
            //     })
            // }
            $("#wlogo").css("background-position-x", "center");
        }
        if ($("#menus").width() == 52) {
          $("#VdpPolicyMenu").find('.policyul1').css({"left":"52px"});

          $(".approveNum_first").addClass('approveNumLittle');
            // $("#deviceList").css({
            //     "width": "100%"
            // })
            $("#wlogo").css("background-position-x", "13px");
        }
        $('.dataTables_scrollHead table').css('width', $('.dataTables_scrollBody table').width());
        $('.dataTables_scrollBody').each(function (index, obj) {
            $(obj).find('tbody tr:first td').each(function (idx, val) {
                if (!$(val).hasClass('dataTables_empty')) {
                    $(val).closest('.dataTables_scrollBody').prev('.dataTables_scrollHead').find('th:eq(' + idx + ')').css('width', $(val).width())
                }
            });
        });
    });

    // 一级菜单单击
    mli0s.click(function () {

        //$(this).find(".ma0").addClass("listclick");
        //$(this).siblings().find(".ma0").removeClass("listclick");

    });

    // 没级菜单根据地址栏判断 当前栏目 并增加当前栏目在菜单上的标志
    $('.mul0 a').each(function (index, obj) {

      if($(obj).hasClass("policya1")){
        if($(obj).attr('href') == location.pathname+location.search){
          $(obj).parents('ul.policyul1').siblings('a').addClass('listclick');
          $(obj).addClass('listclick').css("background-image",'none');
        }
      }else if ($(obj).attr('href').indexOf(location.pathname) > -1) {
            $(obj).addClass('listclick');
            if ($(obj).hasClass('ma2')) {
                $(obj).closest('.mli1').find('.ma1').addClass('listclick').closest('.mli0').find('.ma0').addClass('listclick');
            } else if ($(obj).hasClass('ma1')) {
                $(obj).closest('.mli0').find('.ma0').addClass('listclick');
            }
            if ($(obj).next('ul').length == 0 && !$(obj).hasClass('ma0')) {
                $(obj).css('background-image', 'none');
            };
            return false;
        }

    });
    // $('body .policyListClick li.mli1 a.ma1').click(function(){
    //   $('body .policyListClick li.mli1 a.ma1').addClass('listclick').parents('li.mli1').siblings().find('a.ma1').removeClass('listclick')
    // });
    // 一级菜单悬停
    mli0s.hover(function () {
        if ($(window).height() - $(this).offset().top < $(this).find(".mul1").height()) {//这是在上面的
            if($(this).find(".mul1").height()>340){
                $(this).find(".mul1").css('top', 0 - $(this).find(".mul1").height() + 300).find('.listhover').css({ 'bottom': '24px', 'top': 'auto' });
            }else{
                $(this).find(".mul1").css('top', 0 - $(this).find(".mul1").height() + 60).find('.listhover').css({ 'bottom': '24px', 'top': 'auto' });
            }

        } else {
            $(this).find(".mul1").css('top', 0).find('.listhover').css({ 'top': '14px', 'bottom': 'auto' });
        }
        $(this).find(".mul1").show();
        $(this).addClass("li_on");
    }, function () {
        $(this).find(".mul1").hide();
        $(this).removeClass("li_on");

    });

    // 二级菜单悬停
    mli1s.hover(function () {
        if ($(window).height() - $(this).offset().top < $(this).find(".mul2").height()) {
            $(this).find(".mul2").css('top', 0 - $(this).find(".mul2").height() + 45).find('.listhover2').css({ 'bottom': '12px', 'top': 'auto' });
        }
        else {
            $(this).find(".mul2").css('top', 0).find('.listhover2').css({ 'top': '12px', 'bottom': 'auto' });
        }
        $(this).find(".mul2").show();
        $(this).addClass("li_on");

    }, function () {

        $(this).find(".mul2").hide();
        $(this).removeClass("li_on");

    });


    // 三级菜单悬停
    mli2s.hover(function () {

        $(this).addClass("li_on");

    }, function () {

        $(this).removeClass("li_on");

    });
    // 菜单 END

    //监听窗口缩放
    $(window).resize(function () {
        // showhidemenu();
        if ($(window).width() > 1300) {
            if ($('#menus').width() == 200) {
                $(".namehide").removeClass("namehide").addClass("nameshow");
              $(".approveNum_first").removeClass('approveNumLittle');
                mul1.parent().addClass("havelist");
                menushow = 1;
                localStorage.menushow = 0;
              $("#wlogo").css("background-position-x", "center");
            } else {
                menushow = 0;
              $(".approveNum_first").addClass('approveNumLittle');
                localStorage.menushow = 1;
            }
            // $("#wlogo").css("background-position-x", "center");
        } else {
            setTimeout(function () {
                if ($('#menus').width() < 200) {
                    $(".nameshow").removeClass("nameshow").addClass("namehide");
                    mul1.parent().removeClass("havelist");
                    $("#wlogo").css("background-position-x", "13px");
                  $(".approveNum_first").addClass('approveNumLittle');
                }
            }, 100);
            $('#menus').removeAttr("style");
            $("#wcontent").removeAttr("style");
            menushow = 0;
            localStorage.menushow = 1;
        }
    });

    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

        if (arr = document.cookie.match(reg))

            return unescape(arr[2]);
        else
            return null;
    }





});
