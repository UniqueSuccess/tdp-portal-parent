<!-- userlist.jsp -->

<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ page import="sun.misc.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <title></title>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <c:set var="ctxJs" value="${pageContext.request.contextPath}/js"/>
    <c:set var="ctxCss" value="${pageContext.request.contextPath}/skin/default/css"/>
    <c:set var="ctxImg" value="${pageContext.request.contextPath}/skin/default/images"/>
    <c:set var="version" value="1"/>
    <%--<link href="${ctxCss}/dataTables/dataTablesgray.css" rel="stylesheet" type="text/css"/>--%>
    <link rel="stylesheet" href="${ctxCss}/font_icon/iconfont.css?v=${version}"/>
    <link rel="icon" href="${ctxImg}/logo.ico" type="image/x-ico"/>
    <script src="${ctxJs}/jquery-2.2.1.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gd_iecheck.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/vue.min.js?v=${version}"></script>
    <script type="text/javascript" src="${ctxJs}/plugins/gdui/js/gdui.min.js?v=${version}"></script>
    <link href="${ctxCss}/approve/config/index.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctxJs}/plugins/gdui/css/gdui.min.css?v=${version}"/>
    <style>
        .gd-select .gd-select-drop{
            max-width: 380px;
            max-height: 210px !important;
        }
        .download-wrap{
            width: 380px;
            margin: 0 auto;
            padding: 200px 0 0;
        }
        .download-wrap img{
            margin-bottom: 35px;
        }
        .download-wrap .gd-select{
            height: 40px;
            line-height: 40px;
        }
        .download-wrap .gd-btn{
            width: 380px;
            height: 40px;
            line-height: 40px;
            background: -webkit-gradient(linear,left top,right top,from(#59aef0),color-stop(#50a9ef),color-stop(#59aef0),to(#50a9ef));
            background: linear-gradient(to right,#59aef0,#50a9ef,#59aef0,#50a9ef);
            font-size: 16px;
        }
        .container .row{
            margin: 20px 0;
        }
        .download-wrap .row{
            position: relative;
        }
        .download-wrap .gd-label-required{
            position: absolute;
            left: -10px;
            top: 12px;
        }
    </style>
    <script>
        var ctx = "${ctx}";
        var uuid = query("uuid");
        function query(name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var param = window.location.search.substr(1).match(reg);
            if (param !== null) {
                return unescape(param[2]);
            } else {
                return '';
            }
        };


    </script>
</head>
<body>
    <div id="app" class="container">
        <div class="download-wrap ">
            <div class="gd-text-center">
                <img src="${ctxImg}/download.png">
            </div>
            <div class="row">
                <label class="gd-label-required"></label>
                <gd-select class="gd-select gd-select-lg" v-model="selectValue" ref="customSelect" placeholder="选择部门">
                    <gd-tree :config='treeWindowConfig'></gd-tree>
                </gd-select>
            </div>

            <button class="gd-btn gd-btn-lg" @click="downloadClient()">下载</button>

        </div>
    </div>

    <script>
        var app = new Vue({
            el: '#app',
            data: {
                departmentId: null,
                selectValue: '', // 选择的部门名称
                treeWindowConfig: {
                    id: 'treeWindowDepartment',
                    simpleData: true,//简单模式，默认为true
                    data: [],
                    // linkable: false,
                    onSelect: function (node) {
                        app.selectValue = node.name;
                        app.departmentId = node.id;
                        app.$refs.customSelect.isDroped = false
                    },
                    onChange: function (nodes) {
                    },
                    ready: function (data) {
                    }
                }
            },
            mounted: function() {
                this.getTreeData();
            },
            methods: {
                getTreeData: function() { // 获取部门树
                    var _this = this;
                    gd.get(ctx + '/department/getManagerNodes', '', function(msg) {
                        if (msg.resultCode == 0) {
                            _this.treeWindowConfig.data = msg.data;
                        }
                    })
                },
                downloadClient: function() {
                    var _this = this;
                    if (_this.departmentId == null) {
                        gd.showWarning('请选择部门');
                        return;
                    }
                    gd.get(ctx + '/file/queryFileExists', {path: '/resource/package/Agent.exe'}, function(msg) {
                        if (msg.resultCode == 0) {
                            window.location.href = ctx + '/file/downloadAgent?departmentId=' + _this.departmentId;
                        } else {
                            gd.showWarning('客户端文件不存在');
                        }
                    });

                    return false;
                }
            }
        });
    </script>
</body>
