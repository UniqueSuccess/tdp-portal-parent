<!-- userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
-->


<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/layout/inpage.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/about/index.css" rel="stylesheet" type="text/css"/>
</head>

<div id="about" class="gd-right-content gd-padding-lg" v-cloak>
    <div id="about_subRoot">
        <div id="customer_info" class="details-styl"><h3 id="customer_name" class="customer-name title-styl">金盾</h3>
            <div id="customer_infos">
                <table id="customer_info_detail">
                    <tbody>
                    <tr>
                        <td class="common-detail-styl info-name">客户名称</td>
                        <td class="common-detail-styl info-content">${authInfo.company}</td>
                    </tr>
                    <tr>
                        <td class="common-detail-styl info-name">授权有效期</td>
                        <td class="common-detail-styl info-content">${authInfo.beginEndDate}</td>
                    </tr>
                    <tr>
                        <td class="common-detail-styl info-name">维保截止</td>
                        <td class="common-detail-styl info-content">${authInfo.supportDate}</td>
                    </tr>
                    <tr>
                        <td class="common-detail-styl info-name">授权数量</td>
                        <td class="common-detail-styl info-content">${authInfo.maxCustomerCnt}</td>
                    </tr>
                    <tr>
                        <td class="common-detail-styl info-name">授权码</td>
                        <td class="common-detail-styl info-content">${authInfo.deviceUnique}</td>
                    </tr>
                    <tr>
                        <td class="common-detail-styl info-name">授权文件</td>
                        <td class="common-detail-styl">
                            <form id="uploadTemplate" onsubmit="return false" action="${ctx}/about/fileupload"
                                  method="post" enctype="multipart/form-data" style="display: inline-block;">
                                <div class="filePickerBox"><input type="file" name="file" id="importFile" accept=".tdp"
                                                                  v-on:change="aboutVUE.fillFileName('importFile','fileNameShow')">
                                    <div class="fileNameShow"></div>
                                    <button id="filePicker" class="icon-file" v-on:click="getFile"></button>
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            </form>
                            <div class="btn_set">
                                <button id="authenticate" class="gd-btn" disabled>授权</button>
                                <button id="save_authentication" class="gd-btn-cancel">导出</button>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="products_info" class="details-styl">
            <div class="voc-logo-text"></div>
            <table id="products_info_detail">
                <tbody>
                <tr>
                    <td class="common-detail-styl info-name system-name">系统全称</td>
                    <td class="common-detail-styl info-content">TDP终端文档保护系统<br><br>Terminal Document  Protection System
                    </td>
                </tr>
                <tr>
                    <td class="common-detail-styl info-name">版本信息</td>
                    <td class="common-detail-styl info-content">{{version}}</td>
                </tr>
                <tr>
                    <td class="common-detail-styl info-name">版本更新日期</td>
                    <td class="common-detail-styl info-content">{{updateDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div id="copyright_info">
        <table id="copyright_info_detail" class="copy-table-styl">
            <tbody>
            <tr>
                <td class="partation-copy goldencis-logo"></td>
                <td class="partation-copy"></td>
                <td class="partation-copy"><span>Copyright © 2018 goldencis Inc. All rights reserved.</span></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script>
    var ctx = "${ctx}";
</script>
<script src="${ctxJs}/jquery.form.js"></script>
<script src="${ctxJs}/about/index.js"></script>
