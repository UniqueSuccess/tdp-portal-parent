<!-- userlist.jsp -->
<!-- <%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/meta.jsp"%> -->


<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/layout/inpage.css" rel="stylesheet" type="text/css" />
    <link href="${ctxCss}/about/index.css" rel="stylesheet" type="text/css" />
</head>

<div class="main_right">
    <div class="aboutmain">
        <!-- logo -->
        <div class="mainbox">
            <!-- 授权信息 -->
            <div class="auth-info">
                <div class="accreditbox">
                    <form action="${ctx}/about/fileupload" method="post" id="beanForm_oms" enctype="multipart/form-data">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="messagebox messagebox-oms">
                            <h4 class="messh4">VDP授权信息</h4>
                            <div class="messagebox">
                                <ul>
                                    <li class="w80">产品名称</li>
                                    <li>视频数据防护系统</li>
                                </ul>
                                <ul>
                                    <li class="w80">版本信息</li>
                                    <li>6.2.0</li>
                                </ul>
                                <ul>
                                    <li class="w80">客户名称</li>
                                    <li>${authInfo.company}</li>
                                </ul>
                                <%--<ul>
                                    <li class="w80">授权点数</li>
                                    <li>${authInfo.maxCustomerCnt}</li>
                                </ul>--%>
                                <ul>
                                    <li class="w80">授权期限</li>
                                    <li>${authInfo.beginEndDate}</li>
                                </ul>
                                <ul>
                                    <li class="w80">维保时间</li>
                                    <li>${authInfo.supportDate}</li>
                                </ul>
                                <ul>
                                    <li class="w80">授权码</li>
                                    <li>${authInfo.deviceUnique}</li>
                                </ul>
                                <ul>
                                    <li class="w80">授权文件</li>
                                    <li class="relative">
                                        <input type="hidden" name="MAX_FILE_SIZE" value="10000000000">
                                        <input type="text" id="incCopyFileLinux_oms" class="input-style inputup" readonly name="incCopyFileLinux" />
                                        <a href="javascript:void(0);" id="btn_select_oms" class="input-link btn btn"><i class="iconfont icon-menu-openfolder lookview" title="浏览"></i></a>
                                        <input type="file" name="file" accept=".vdp" id="incTrueFileLinux_oms" class="uploadFile btn" style="width:220px;top: 0px;"/>
                                        <input type="button" class="sureButton" value="授权" id="getauthority_oms" />
                                        <input type="hidden" name="type" value="oms" />
                                        <span id="installTarLinuxTip_oms" class="tip"></span>
                                        <iframe id="" name="installUpdateLinux" style="display:none;"></iframe>
                                    </li>
                                </ul>
                                <ul class="explort-box">
                                    <li class="w80"></li>
                                    <li class="outacc">
                                        <i class="iconfont icon-btn-export btn-parimy" onclick="isFileExistOMS();"></i>
                                        <a href="javascript:void(0);" style="text-decoration: none;color: #6B686A" onclick="isFileExistOMS();">导出&nbsp;&nbsp;</a>
                                        <a id="downLoadFile_oms" href="${ctx}/about/fileload" style="text-decoration: none;">
                                            <p></p>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <hr class="hline">
        <!-- 公司信息 -->
        <div class="goldenbox">
            <div class="goldlogo">
                <img src="${ctxImg}/logo-web.png" class="logogold"/>
            </div>
            <p class="copymess">Copyright &copy; 2018 goldencis Inc. All rights reserved.</p>
        </div>
    </div>
</div>
<script src="${ctxJs}/about/index.js"></script>