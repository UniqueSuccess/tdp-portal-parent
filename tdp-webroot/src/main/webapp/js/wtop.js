$(function () {


  var selectbluebtn = $(".selectbluebtn");

  selectbluebtn.hover(function () {
    $(this).addClass("bluebtnon");

    $(this).find(".btnselector").show();

  }, function () {

    $(this).removeClass("bluebtnon");

    $(this).find(".btnselector").hide();
  });
  $('.j-top-edit-user').click(editUserInfo);
  // getPendingMsg();
  // setInterval(function () {
  // getPendingMsg();
  // }, 1000 * 30);
});

function editUserInfo() {
  layer.open({
    title: '编辑账户信息',
    id: 'accountinfo',
    type: 1,
    content: $('#userinfo-html').html(),
    btn: ['确定', '取消'],
    area: ['540px', '570px'],
    yes: function (index) {
      if (!$("body #accountinfo form").valid()) {
        return;
      }
      var postData = $('#accountinfo form').serializeJSON();
      if (postData.newpwd && !postData.pwd) {
        layer.msg('若要修改密码，请输入现有密码！', {icon: 7});
        return;
      }
      if (postData.pwd) {
        postData.pwd = encrypt(postData.pwd).toUpperCase();
        if(postData.newpwd !=''){
          postData.newpwd = encrypt(postData.newpwd).toUpperCase();
        }
      }else {
        layer.msg('请输入现有密码！', {icon: 7});
        return;
      }
      postData.id = $('#top_user_name').attr('data-id');
      delete postData.newpwd_x;
      delete postData.roleTypeName;
      var account = {};
      account.id = postData.id;
      account.prePassword = postData.pwd;
      account.password = postData.newpwd;
      account.name = postData.first;
      account.phone = postData.phone;





      $.ajax({
        type: 'post',
        url: ctx + '/systemSetting/user/editPersonalUserInfo',
        data: account,
        success: function (msg) {
          if (msg.resultCode == 0) {
            layer.msg('现有密码不正确！', {icon: 7});
          } else if (msg.resultCode == 1) {
            layer.msg('修改成功', {icon: 1});
            layer.close(index);
          } else {
            layer.msg('修改失败', {icon: 2});
          }
        },
        error: function () {
          layer.msg('修改失败', {icon: 2});
        }
      })
    },
    success: function () {
      if ($('#top_user_name').text().trim() != 'system') {
        $('#accountinfo .u-row-first').removeClass('none');
      } else {
        $('#accountinfo .j-label-role').text('真实姓名：');
      }

      $.ajax({
        type: 'GET',
        url: ctx + '/systemSetting/user/getUserById?userId=' + $('#top_user_name').attr('data-id'),
        success: function (msg) {
          if (msg.resultCode == 1) {
            var roleName = '';
            if (msg.data.roleType == 0) {
              roleName = '超级管理员'
            } else if (msg.data.roleType == 1) {
              roleName = '管理员'
            } else if (msg.data.roleType == 2) {
              roleName = '操作员'
            } else if (msg.data.roleType == 3) {
              roleName = '审计员'
            }
            $('#accountinfo input[name=id]').val(msg.id);
            $('#accountinfo input[name=userName]').val(msg.data.userName);
            $('#accountinfo input[name=newpwd]').attr('id', 'userinfo-password');
            if ($('#top_user_name').text().trim() != 'system') {
              $('#accountinfo input[name=roleTypeName]').val(roleName || '');
            } else {
              $('#accountinfo input[name=roleTypeName]').val(roleName || '');
            }
            $('#accountinfo input[name=first]').val(msg.data.name);
            $('#accountinfo input[name=phone]').val(msg.data.phone);
            $('#accountinfo form').validate({
              rules: {
                pwd: {
                  twentyfourChar: true,
                  minSixChar: true
                },
                newpwd: {
                  twentyfourChar: true,
                  minSixChar: true
                },
                newpwd_x: {
                  equalTo: '#userinfo-password',
                },
                first: {
                  specialCharNew: true,
                  twentyfourChar: true,
                  required: true
                },
                phone: {
                  isPhone   : true,
                }
              },
              messages: {
                newpwd_x: {
                  equalTo: "两次密码不一致"
                }
              },
            });
          }

        },
        error: function (e) {
          layer.msg('获取用户信息失败', {icon: 2});
        }
      })
    }
  });
}

