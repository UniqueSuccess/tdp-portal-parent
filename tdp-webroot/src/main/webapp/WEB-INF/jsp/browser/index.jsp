<!-- userlist.jsp -->

<!--<%@ page language="java" pageEncoding="UTF-8"%>-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%>
<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="${ctxImg}/logo.ico" type="image/x-ico" />
    <link href="${ctxCss}/browser/index.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="download_page">
    <div class="content">
        <div class="logo"></div>
        <div class="down">
            <label>您正在使用的浏览器版本过低，请升级浏览器或者安装谷歌浏览器进行访问</label>
            <div class="left">
                <a href="${ctx}/downloadBrowser/IE11x86.exe">
                    <img src="${ctxImg}/ie.png" alt=""/>
                </a>
                <span class="type font-bold">升级IE浏览器</span>
                <span class="title">win7x64</span>
            </div>
            <div class="left">
                <a href="${ctx}/downloadBrowser/IE11.exe">
                    <img src="${ctxImg}/ie.png" alt=""/>
                </a>
                <span class="type font-bold">升级IE浏览器</span>
                <span class="title">XP系统/win7x86</span>
            </div>
            <div class="right">
                <a href="${ctx}/downloadBrowser/Chrome.exe">
                    <img src="${ctxImg}/google.png" alt=""/>
                </a>
                <span class="type font-bold">使用Google Chrome浏览器</span>
                <span class="title">XP/Win7系统</span>
            </div>
        </div>
    </div>
</div>
</body>
<script>
  var ctx = "${ctx}";
</script>

