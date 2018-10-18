/**
* @program: vdp-portal-parent
* @description:
    * @Author: chengl
* @create: 2018-10-16 10:43
**/// 主体部分
var topContent = new Vue({
    el: '#system_top',
    data: {
        gdata:{},
        oldpwd:'',
        pwd:'',
        confirmPwd:'',
        ukey:'',
        oldid:'',
        userName:'',
        topbarConfig: [{
            icon: 'icon-system', //图标，必选
            title: '文本', //鼠标放上去的title，可选
            text: '文本按钮', //图标旁边的文字，可选
            action: function (data) {//data返回当前条目的配置信息
                //点击图标执行的动作，可选
                gd.showSuccess('你点了文本按钮');
            }
        },
            {
                icon: 'icon-warning-hex',
                title: '报警',
                badge: '60', //如果定义的badge，将显示小红点
                action: function (data) {
                    gd.showWarning('你点了报警');
                }
            },
            {
                icon: 'icon-fullscreen-hex',
                title: '全屏',
                action: function (data) {
                    gd.toggleFullscreen();
                }
            },
            {
                icon: 'icon-user',
                text: 'system',
                dropItems: [
                    //如果定义了dropItems，将显示下拉框
                    {
                        icon: 'icon-user',//图标
                        text: '个人信息', //下拉框的文本
                        action: function (data) {
                            //下拉框的动作
                            gd.showSuccess('你点了个人信息');
                        }
                    },
                    {
                        text: '退出',
                        action: function (data) {
                            gd.showSuccess('你点了退出');
                        }
                    }
                ]
            }]
    }
});
