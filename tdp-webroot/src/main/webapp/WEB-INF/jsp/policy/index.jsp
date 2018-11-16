<!--userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<head>
    <title>策略</title>
    <link rel="stylesheet" href="${ctxCss}/policy/iconfont/iconfont.css">
    <link rel="stylesheet" href="${ctxJs}/plugins/element-ui/element-ui-color.css">
    <style>
        .container{
            width: calc(100% - 60px);
            margin: 15px 30px 0 30px;
        }
        .container .row{
            padding: 10px 0;
        }
        .main-content{
            padding: 20px;
            box-sizing: border-box;
        }
        .main-content .gd-toolbar{
            background-color: #fff;
        }
        .main-content .gd-tab-vertical{
            border-top: 1px solid #eee;
        }
        .main-content .gd-tab-vertical>div.gd-tab-content{
            -webkit-box-shadow: none;
            box-shadow: none;
        }
        .policy-item{
            margin-top: 10px;
            color: #333;
        }
        .policy-item .policy-title{
            padding: 20px 0;
        }
        .policy-item .policy-content{
            margin-left: 20px;
        }
        .policy-item .policy-content .gd-checkbox{
            margin-right: 90px;
        }

        .policy-item .policy-content .gd-checkbox:nth-last-child(1){
            margin-right: 0;
        }
        .policy-item .policy-row{
            padding: 6px 0;
        }
        .policy-item .policy-row label{
            display: inline-block;
            width: 62px;
            margin-right: 10px;
            text-align: right;
        }
        .policy-item .policy-row .unit{
            margin-left: -24px;
        }
        .policy-item .icon-help{
            display: inline-block;
            vertical-align: top;
            position: relative;
            left: 5px;
            color: #cfd6e1;
        }
        .policy-item .hint-text {
            padding-left: 6px;
            color: #cfd6e1;
            position: relative;
            top: 1px;
        }
        .save-wrap{
            position: absolute;
            z-index: 99999;
            bottom: 0;
            left: 0;
            width: calc(100% - 100px);
            padding: 10px 40px;
            background-color: #fff;
        }
        .gd-tab-vertical{
            height: calc(100% - 56px);
        }
        .nature-wrap{
            margin: 20px;
            border: 1px solid #eee;
        }
        .nature-wrap .nature-tool-bar{
            line-height: 32px;
            padding: 8px;
        }
        .gd-table-wrapper .gd-table-body tr>th{
            text-align: left;
            font-weight: normal;
        }
        .container .row label.gd-radio{
            width: auto;
        }
        .remark-label{
            vertical-align: top;
            margin-top: 9px;
        }
        .gd-tab-vertical>div.gd-tab-content .gd-tab-item{
            padding: 20px 40px 70px 40px;
        }
        .gd-tab-vertical>div.gd-tab-content .gd-tab-item .policy-item:last-child{
            padding-bottom: 60px;
        }
        [class*=" el-icon-"], [class^=el-icon-]{
            font-family: iconfont;
            font-size: 16px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
        }
        .el-icon-arrow-down:before{
            content: "\e75e";
        }
        .file-color-picker{
            left: 456px !important;
        }
        .policy-item .policy-row .scan-type-radio{
            width: auto;
            margin-right: 30px;
        }
        .gd-toolbar>.gd-toolbar-text{
            margin-right: 24px;
        }
    </style>
</head>
<body>
    <div id="app" class="gd-right-content" v-cloak>
        <div class="main-content">
            <gd-toolbar :config="toolbarConfig"></gd-toolbar>
            <gd-tab vertical>
                <gd-tab-item label="屏幕水印">
                    <div class="policy-item">
                        <div class="policy-title">启用状态</div>
                        <div class="policy-content">
                            <label class="gd-switch">
                                <input type="checkbox" v-model="sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                            </label>

                        </div>
                    </div>
                    <div class="policy-item">
                        <div class="policy-title">水印内容</div>
                        <div class="policy-content j-screen-water">
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="sbscrnwatermark.content.computername" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                计算机名
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="sbscrnwatermark.content.ip" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                IP地址
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="sbscrnwatermark.content.mac" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                MAC地址
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="sbscrnwatermark.content.depname" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                部门名称
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="sbscrnwatermark.content.pctime" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                终端时间
                            </label>
                            <div class="gd-margin-top-lg">
                                <label class="gd-checkbox gd-margin-right-sm">
                                    <input type="checkbox" v-model="sbscrnwatermark.content.manual" :disabled="!sbscrnwatermark.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    自定义
                                </label>
                                <input type="text" id="screen_manual_text" class="gd-input gd-input-lg" v-model="sbscrnwatermark.content.manualtext" :disabled="!sbscrnwatermark.content.manual || !sbscrnwatermark.enable">
                            </div>
                        </div>
                    </div>
                    <div class="policy-item">
                        <div class="policy-title">水印设置</div>
                        <div class="policy-content">
                            <div class="policy-row">
                                <label>颜色</label>
                                <el-color-picker v-model="sbscrnwatermark.content.mycolor" :disabled="!sbscrnwatermark.enable"></el-color-picker>
                            </div>
                            <div class="policy-row">
                                <label>透明度</label>
                                <gd-select class="gd-select-lg"  name="selectDemo" v-model="sbscrnwatermark.content.opacity" :disabled="!sbscrnwatermark.enable">
                                    <gd-option value="255">0%</gd-option>
                                    <gd-option value="225">10%</gd-option>
                                    <gd-option value="200">20%</gd-option>
                                    <gd-option value="175">30%</gd-option>
                                    <gd-option value="150">40%</gd-option>
                                    <gd-option value="125">50%</gd-option>
                                    <gd-option value="100">60%</gd-option>
                                    <gd-option value="75">70%</gd-option>
                                    <gd-option value="50">80%</gd-option>
                                    <gd-option value="25">90%</gd-option>
                                </gd-select>
                            </div>
                            <div class="policy-row">
                                <label>方向</label>
                                <gd-select class="gd-select-lg" v-model="sbscrnwatermark.content.mydirection" :disabled="!sbscrnwatermark.enable">
                                    <gd-option value="0">左斜</gd-option>
                                    <gd-option value="1">右斜</gd-option>
                                    <gd-option value="2">横向</gd-option>
                                    <gd-option value="3">纵向</gd-option>
                                </gd-select>
                            </div>
                            <div class="policy-row">
                                <label>字体大小</label>
                                <gd-select class="gd-select-lg" name="selectDemo" v-model="sbscrnwatermark.content.fontsize" :disabled="!sbscrnwatermark.enable">
                                    <gd-option value="14">14</gd-option>
                                    <gd-option value="16">16</gd-option>
                                    <gd-option value="18">18</gd-option>
                                    <gd-option value="20">20</gd-option>
                                    <gd-option value="22">22</gd-option>
                                    <gd-option value="24">24</gd-option>
                                    <gd-option value="26">26</gd-option>
                                    <gd-option value="28">28</gd-option>
                                    <gd-option value="30">30</gd-option>
                                </gd-select>
                            </div>
                        </div>
                    </div>
                </gd-tab-item>
                <gd-tab-item label="文件管控">
                    <div class="policy-item file-item">
                        <div class="policy-title">文件外发
                            <label class="gd-switch gd-margin-left-lg">
                                <input type="checkbox" v-model="sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                <i></i>
                            </label>
                            <i class="gd-icon icon-help"></i>
                            <span class="hint-text">将文件发送给企业外部人员</span>
                        </div>
                        <div class="policy-content">
                            <div class="policy-row">
                                <label class="gd-checkbox gd-margin-right-xxl">
                                    <input type="checkbox"  v-model="sbfileoutcfg.content.approve" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    审批外发
                                </label>
                                <gd-select class="gd-select-lg" name="selectDemo" v-model="sbfileoutcfg.content.flowid" :disabled="!sbfileoutcfg.content.approve || !sbfileoutcfg.enable">
                                    <gd-option v-for="item in approveArray" :value="item.id">{{item.name}}</gd-option>
                                </gd-select>
                            </div>
                            <div class="policy-row">
                                <label class="gd-checkbox gd-margin-right-xxl">
                                    <input type="checkbox"  v-model="sbfileoutcfg.content.isvalidtime" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0' gd-validate="minValue" gdv-minvalue="1" gdv-msg="只可输入大于0的整数">
                                    <i></i>
                                    有效期限
                                </label>
                                <input type="text" id="file_valid_time" class="gd-input gd-input-lg" v-model="sbfileoutcfg.content.validtime" :disabled="!sbfileoutcfg.content.isvalidtime || !sbfileoutcfg.enable">
                                <span class="unit">天</span>
                            </div>
                            <div class="policy-row">
                                <label class="gd-checkbox gd-margin-right-xxl">
                                    <input type="checkbox"  v-model="sbfileoutcfg.content.ispwd" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0' gd-validate="minValue" gdv-minvalue="1" gdv-msg="只可输入大于0的整数">
                                    <i></i>
                                    密码验证
                                </label>
                                <input type="text" id="file_psw" class="gd-input gd-input-lg" v-model="sbfileoutcfg.content.pwd" :disabled="!sbfileoutcfg.content.ispwd || !sbfileoutcfg.enable">
                                <span class="unit">位</span>
                            </div>
                            <div class="policy-row">
                                <label class="gd-checkbox gd-margin-right-xxl">
                                    <input type="checkbox" v-model="sbfileoutcfg.content.isopencount" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0' gd-validate="minValue" gdv-minvalue="1" gdv-msg="只可输入大于0的整数">
                                    <i></i>
                                    打开次数
                                </label>
                                <input type="text" id="file_open_count" class="gd-input gd-input-lg" v-model="sbfileoutcfg.content.opencount" :disabled="!sbfileoutcfg.content.isopencount || !sbfileoutcfg.enable">
                                <span class="unit">次</span>
                            </div>
                            <div class="policy-row">
                                <label class="gd-checkbox">
                                    <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.enable" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    屏幕水印
                                </label>
                                <div v-show="sbfileoutcfg.content.scwatermark.enable">
                                    <div class="policy-item gd-margin-left-xl">
                                        <div class="policy-title">水印内容</div>
                                        <div class="policy-content j-file-screen-water">
                                            <label class="gd-checkbox">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.computername" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                计算机名
                                            </label>
                                            <label class="gd-checkbox">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.ip" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                IP地址
                                            </label>
                                            <label class="gd-checkbox">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.mac" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                MAC地址
                                            </label>
                                            <label class="gd-checkbox">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.depname" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                部门名称
                                            </label>
                                            <label class="gd-checkbox">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.pctime" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                终端时间
                                            </label>
                                            <label class="gd-checkbox gd-margin-right-sm">
                                                <input type="checkbox" v-model="sbfileoutcfg.content.scwatermark.recv" :disabled="!sbfileoutcfg.enable" :true-value='1' :false-value='0'>
                                                <i></i>
                                                接收方信息
                                            </label>
                                        </div>
                                    </div>
                                    <div class="policy-item gd-margin-left-xl">
                                        <div class="policy-title">水印设置</div>
                                        <div class="policy-content">
                                            <div class="policy-row">
                                                <label>颜色</label>
                                                <el-color-picker :popper-class="popperClass" v-model="sbfileoutcfg.content.scwatermark.mycolor" :disabled="!sbfileoutcfg.enable"></el-color-picker>
                                            </div>
                                            <div class="policy-row">
                                                <label>透明度</label>
                                                <gd-select class="gd-select-lg"  name="selectDemo" v-model="sbfileoutcfg.content.scwatermark.opacity" :disabled="!sbfileoutcfg.enable">
                                                    <gd-option value="255">0%</gd-option>
                                                    <gd-option value="225">10%</gd-option>
                                                    <gd-option value="200">20%</gd-option>
                                                    <gd-option value="175">30%</gd-option>
                                                    <gd-option value="150">40%</gd-option>
                                                    <gd-option value="125">50%</gd-option>
                                                    <gd-option value="100">60%</gd-option>
                                                    <gd-option value="75">70%</gd-option>
                                                    <gd-option value="50">80%</gd-option>
                                                    <gd-option value="25">90%</gd-option>
                                                </gd-select>
                                            </div>
                                            <div class="policy-row">
                                                <label>方向</label>
                                                <gd-select class="gd-select-lg" name="selectDemo" v-model="sbfileoutcfg.content.scwatermark.mydirection" :disabled="!sbfileoutcfg.enable">
                                                    <gd-option value="0">左斜</gd-option>
                                                    <gd-option value="1">右斜</gd-option>
                                                    <gd-option value="2">横向</gd-option>
                                                    <gd-option value="3">纵向</gd-option>
                                                </gd-select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="policy-item file-item">
                        <div class="policy-title">文件导出
                            <label class="gd-switch gd-margin-left-lg">
                                <input type="checkbox" v-model="sbfileopt.enable" :true-value='1' :false-value='0'>
                                <i></i>
                            </label>
                            <i class="gd-icon icon-help"></i>
                            <span class="hint-text">企业内文件共享，以下两项不勾选为自主导出</span>
                        </div>
                        <div class="policy-content j-file-out">
                            <div class="policy-row">
                                <label class="gd-checkbox gd-margin-right-xxl">
                                    <input type="checkbox" v-model="sbfileopt.content.approve" :disabled="!sbfileopt.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    审批导出
                                </label>
                                <gd-select class="gd-select-lg" name="selectDemo" v-model="sbfileopt.content.flowid" :disabled="!sbfileopt.content.approve || !sbfileopt.enable">
                                    <gd-option v-for="item in approveArray" :value="item.id">{{item.name}}</gd-option>
                                </gd-select>
                            </div>
                            <div class="policy-row">
                                <label class="gd-checkbox">
                                    <input type="checkbox" v-model="sbfileopt.content.encry" :disabled="!sbfileopt.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    加密导出
                                </label>
                            </div>
                        </div>
                    </div>
                </gd-tab-item>
                <gd-tab-item label="文件扫描">
                    <div class="policy-item">
                        <div class="policy-title">启用状态</div>
                        <div class="policy-content">
                            <label class="gd-switch">
                                <input type="checkbox" v-model="filescan.enable" :true-value='1' :false-value='0'>
                                <i></i>
                            </label>
                        </div>
                    </div>
                    <div class="policy-item">
                        <div class="policy-title">识别方式</div>
                        <div class="policy-content">
                            <div class="policy-row gd-margin-bottom-md">
                                <label class="gd-radio scan-type-radio gd-margin-right-0">
                                    <input type="radio" value="0" name="scanType" v-model="filescan.content.type" :disabled="!filescan.enable">
                                    <i></i>
                                    全面扫描
                                </label>
                                <i class="gd-icon icon-help" style="top: 1px"></i>
                                <span class="hint-text" style="top: 2px">开启全面扫描会扫描全部格式为word文档、电子表格演示文稿、pdf文档的文件，请谨慎选择</span>
                            </div>
                            <div class="policy-row gd-margin-bottom-md">
                                <label class="gd-radio scan-type-radio">
                                    <input type="radio" value="1" name="scanType" v-model="filescan.content.type" :disabled="!filescan.enable">
                                    <i></i>
                                    涉密扫描
                                </label>
                            </div>
                            <div class="policy-content" v-show="filescan.content.type == 1">
                                <div class="policy-row">
                                    <label class="gd-checkbox gd-margin-right-xxl">
                                        <input type="checkbox" class="j-scan-checkbox" class="" v-model="filescan.content.datalist[0].iskeywords" :disabled="!filescan.enable" :true-value='1' :false-value='0'>
                                        <i></i>
                                        关键字扫描
                                    </label>
                                    <div class="gd-margin-left-xl gd-margin-top-lg">
                                        <input type="text" id="keywords_value" placeholder="关键字之间用分号隔开" class="gd-input gd-input-lg" v-model="filescan.content.datalist[0].keywords" :disabled = "!filescan.content.datalist[0].iskeywords || !filescan.enable"/>
                                    </div>
                                </div>
                                <div class="policy-row">
                                    <label class="gd-checkbox gd-margin-right-xxl">
                                        <input class="j-scan-checkbox" type="checkbox" v-model="filescan.content.datalist[1].isregexps" :disabled="!filescan.enable" :true-value='1' :false-value='0'>
                                        <i></i>
                                        自然语义
                                    </label>
                                    <div class="nature-wrap">
                                        <div class="nature-tool-bar">
                                            <i title="添加" class="gd-btn-icon icon-add" @click="addNature(!filescan.content.datalist[1].isregexps || !filescan.enable)" :disabled="!filescan.content.datalist[1].isregexps || !filescan.enable"></i>
                                            <i title="删除" class="gd-btn-icon icon-delete" @click="delNatureMore(!filescan.content.datalist[1].isregexps || !filescan.enable)" :disabled="!filescan.content.datalist[1].isregexps || !filescan.enable"></i>
                                        </div>
                                        <div class="gd-table-wrapper">
                                            <div class="gd-table-scroll-h">
                                                <div class="gd-table-body">
                                                        <table id="nature_table">
                                                            <tr>
                                                                <th width="40"><label class="gd-checkbox"><input class="j-check-all" type="checkbox" :checked="filescan.content.datalist[1].regexps.length === regexpsIds.length && regexpsIds.length!==0" :disabled="!filescan.content.datalist[1].isregexps || !filescan.enable"> <i></i></label></th>
                                                                <th><span class="gd-table-col-head">语义功能</span></th>
                                                                <th><span class="gd-table-col-head">匹配方式</span></th>
                                                                <th><span class="gd-table-col-head">扩展内容</span></th>
                                                                <th><span class="gd-table-col-head">备注</span></th>
                                                                <th><span class="gd-table-col-head">操作</span></th>
                                                            </tr>
                                                            <tr v-for="regexp in filescan.content.datalist[1].regexps" v-show="filescan.content.datalist[1].regexps !== 0">
                                                                <td><label class="gd-checkbox" ><input class="j-check-one" :data-id="regexp.id" type="checkbox" @click="checkedOne(regexp.id)" :checked="regexpsIds.indexOf(regexp.id)>=0" :disabled="!filescan.content.datalist[1].isregexps || !filescan.enable"> <i></i></label></td>
                                                                <td>{{regexp.rulename}}</td>
                                                                <td>{{regexp.extend_name}}</td>
                                                                <td>{{regexp.extend_content}}</td>
                                                                <td>{{regexp.remark}}</td>
                                                                <td>
                                                                    <div class="gd-table-operate">
                                                                        <button type="button" title="删除" class="gd-table-operate-item gd-btn-alone icon-delete" :data-id="regexp.id" @click="delNatureOne(regexp.id)" :disabled="!filescan.content.datalist[1].isregexps || !filescan.enable"></button>
                                                                    </div>
                                                                </td>
                                                            </tr>

                                                            <tr v-show="filescan.content.datalist[1].regexps == 0" >
                                                                <td colspan="6"><div class="gd-text-center">暂无数据</div></td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </gd-tab-item>
                <gd-tab-item label="打印管控">
                    <div class="policy-item">
                        <div class="policy-title">启用状态</div>
                        <div class="policy-content">
                            <label class="gd-switch">
                                <input type="checkbox" v-model="printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                            </label>
                        </div>
                    </div>
                    <div class="policy-item">
                        <div class="policy-title">水印内容</div>
                        <div class="policy-content j-print-screen">
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="printwatermark.content.computername" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                计算机名
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="printwatermark.content.ip" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                IP地址
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="printwatermark.content.mac" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                MAC地址
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="printwatermark.content.depname" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                部门名称
                            </label>
                            <label class="gd-checkbox">
                                <input type="checkbox" v-model="printwatermark.content.pctime" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                <i></i>
                                终端时间
                            </label>
                            <div class="gd-margin-top-lg">
                                <label class="gd-checkbox gd-margin-right-sm">
                                    <input type="checkbox" v-model="printwatermark.content.manual" :disabled="!printwatermark.enable" :true-value='1' :false-value='0'>
                                    <i></i>
                                    自定义
                                </label>
                                <input type="text" id="print_screen_manual_text" class="gd-input gd-input-lg" v-model="printwatermark.content.manualtext" :disabled="!printwatermark.content.manual || !printwatermark.enable">
                            </div>
                        </div>
                    </div>
                </gd-tab-item>
                <div class="save-wrap"><button class="gd-btn" @click.prevent="save()">保存</button></div>
            </gd-tab>
        </div>
    </div>
<%--新建自然语义--%>
<script type="text/html" id="add_nature_temp">
    <div class="container" id="add_nature">
        <form id="add_dept_form">
            <div class="row">
                <label class="">语义功能</label>
                <label class="gd-radio">
                    <input type="radio" value="邮箱:(\w){1,50}(\.\w{1,50}){0,50}@(\w){1,50}((\.\w{1,50}){1,50})" name="rulename" checked>
                    <i></i>
                    邮箱
                </label>
                <label class="gd-radio">
                    <input type="radio" value="手机:\b(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}\b" name="rulename">
                    <i></i>
                    手机
                </label>
                <label class="gd-radio">
                    <input type="radio" value="固话:((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))" name="rulename">
                    <i></i>
                    固话
                </label>
                <label class="gd-radio">
                    <input type="radio" value="身份证15位:[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}" name="rulename">
                    <i></i>
                    身份证15位
                </label>
                <label class="gd-radio">
                    <input type="radio" value="身份证18位:[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]" name="rulename">
                    <i></i>
                    身份证18位
                </label>
            </div>
            <div class="row">
                <label class="">匹配方式</label>
                <label class="gd-radio">
                    <input type="radio" value="不限:0" name="extend_type" checked>
                    <i></i>
                    不限
                </label>
                <label class="gd-radio">
                    <input type="radio" value="包含:3" name="extend_type">
                    <i></i>
                    包含
                </label>
                <label class="gd-radio">
                    <input type="radio" value="以此开头:1" name="extend_type">
                    <i></i>
                    以此开头
                </label>
                <label class="gd-radio">
                    <input type="radio" value="以此结尾:2" name="extend_type">
                    <i></i>
                    以此结尾
                </label>
            </div>
            <div class="row">
                <label>扩展内容</label>
                <input type="text" class="gd-input gd-input-lg" name="extend_content" />
            </div>
            <div class="row">
                <label class="remark-label">备注</label>
                <textarea class="gd-textarea gd-textarea-lg" name="remark"></textarea>
            </div>
        </form>
    </div>
</script>
<script type="text/javascript" src="${ctxJs}/plugins/element-ui/element-ui.min.js"></script>
<script>
    $('.gd-topbar-name').html('策略');
    $('.icon-strategy').parent().addClass('gd-active');
    var msg = ${resultMsg.data}
    // // 转换屏幕水印方向的类型
    // if (msg.sbscrnwatermark.content.direction == 0) {
    //     var dir = msg.sbscrnwatermark.content.direction;
    //     msg.sbscrnwatermark.content.direction = dir.toString();
    // }
    // if (msg.sbfileoutcfg.content.scwatermark.direction == 0) {
    //     var dirA = msg.sbfileoutcfg.content.scwatermark.direction;
    //     msg.sbfileoutcfg.content.scwatermark.direction = dirA.toString();
    // }
    
    
    var policyId = ${policyId};
    var policyName = '${policyName}';

    var app = new Vue({
        el: '#app',
        data: {
            // 打印管控
            printwatermark: msg.printwatermark,
            // 屏幕水印
            sbscrnwatermark: msg.sbscrnwatermark,
            sbfileopt: msg.sbfileopt,
            sbfileoutcfg: msg.sbfileoutcfg,
            filescan: msg.filescan,
            regexpsIds: [], // 涉密扫描自然语义选中的id
            popperClass: 'file-color-picker',
            //工具栏配置
            toolbarConfig: [
                {
                    type: 'text',
                    text: policyName,
                },
                {
                    type: 'button',
                    icon: 'icon-celvezu',
                    title: '策略',
                    action: function (dom) {
                        gd.showLayer({
                            id: 'policyWind',//可传一个id作为标识
                            title: '选择策略',//窗口标题
                            content: $('#policy_select_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                            //url: './layer_content.html',//也可以传入url作为content,
                            size: [780, 475],//窗口大小，直接传数字即可，也可以是['600px','400px']
                            //autoFocus:true,//自动对输入框获取焦点，默认为ture
                            success: function(dom) {
                                var selectWindow = new Vue({
                                    el: '#policy_select',
                                    data: {
                                        policyArray: []
                                    },
                                    mounted: function() {
                                        this.getPolicyList();
                                    },
                                    methods: {
                                        addPolicy: function() {
                                            gd.showLayer({
                                                id: 'policyAddWind',//可传一个id作为标识
                                                title: '新建策略',//窗口标题
                                                content: $('#policy_add_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                                                //url: './layer_content.html',//也可以传入url作为content,
                                                size: [560, 280],//窗口大小，直接传数字即可，也可以是['600px','400px']
                                                autoFocus:true,//自动对输入框获取焦点，默认为ture
                                                btn: [
                                                    {
                                                        text: '确定',
                                                        action: function (dom) {
                                                            validateDep = gd.validate('#add_dept_form', {
                                                                autoPlaceholer: true,
                                                            });
                                                            if (!validateDep.valid()) {
                                                                return false;
                                                            }
                                                            
                                                            var postData = $('#policyAddWind #add_dept_form').serializeJSON();
                                                            gd.post(ctx + '/policy/addPolicy', postData, function (msg) {
                                                                if (msg.resultCode == '0') {
                                                                    // gd.showSuccess('保存成功');
                                                                    // selectWindow.getPolicyList();
                                                                    window.location.href = ctx + msg.data;
                                                                } else {
                                                                    gd.showError('保存失败 ' + (msg.resultMsg || ''));
                                                                }
                                                                dom.close();
                                                            })
                                                            return false;//阻止弹窗自动关闭
                                                        }
                                                    },
                                                    {
                                                        text: '取消',
                                                        action: function () {

                                                        }
                                                    }
                                                ],
                                                success: function(dom) {
                                                    var addWindow = new Vue({
                                                        el: '#policy_add',
                                                        data: {
                                                            policyArray: selectWindow.policyArray,
                                                            selectPolicyValue: '1'
                                                        }
                                                    })
                                                },
                                                end: function (dom) {//参数为当前窗口dom对象
                                                    // gd.showSuccess('窗口关闭了');
                                                }
                                            });
                                        },
                                        // 跳页
                                        goPage: function (id) {
                                            window.location.href = ctx + '/policy/readPolicyJsonFileById?id='+ id;
                                        },
                                        // 删除策略
                                        deletePolicy: function (id) {
                                            if (id == 1) {
                                                gd.showWarning('默认策略不可删除');
                                                return;
                                            }
                                            var domDelOne = gd.showConfirm({
                                                id: 'wind',
                                                content: '确定要删除吗?',
                                                btn: [{
                                                    text: '确定',
                                                    class: 'gd-btn-danger', //也可以自定义类
                                                    action: function (dom) {
                                                        gd.post(ctx + '/policy/deletePolicy', {policyId: id}, function(msg) {
                                                            if (msg.resultCode == 0) {
                                                                gd.showSuccess('删除成功');
                                                                selectWindow.getPolicyList();
                                                            } else {
                                                                gd.showError('删除失败 ' + (msg.resultMsg || ''));
                                                            }
                                                        })
                                                    }
                                                }, {
                                                    text: '取消',
                                                    action: function (dom) {
                                                        dom.close();
                                                    }
                                                }],
                                                success: function (dom) {
                                                },
                                                end: function (dom) {
                                                }
                                            });

                                        },
                                        // 获取策略列表
                                        getPolicyList: function() {
                                            var _this = this;
                                            gd.get(ctx + '/policy/getAllPolicys', '', function(msg) {
                                                if (msg.resultCode == '0') {
                                                    _this.policyArray = msg.data;
                                                }
                                            })
                                        },
                                    }
                                })
                            },
                            end: function (dom) {//参数为当前窗口dom对象
                                // gd.showSuccess('窗口关闭了');
                            }
                        });
                    }
                },
                {
                    type: 'button',
                    icon: 'icon-delete',
                    title: '删除',
                    disabled: policyId == 1 ? true : false,
                    action: function (dom) {
                        if (policyId == 1) {
                            gd.showWarning('默认策略不可删除');
                        } else {
                            var domDelPolicy = gd.showConfirm({
                                id: 'wind',
                                content: '确定要删除吗?',
                                btn: [{
                                    text: '确定',
                                    class: 'gd-btn-danger', //也可以自定义类
                                    action: function (dom) {
                                        gd.post(ctx + '/policy/deletePolicy', {policyId: policyId}, function(msg) {
                                            if (msg.resultCode == 0) {
                                                gd.showSuccess('删除成功');
                                                window.location.href = ctx + '/policy/readPolicyJsonFileById?id=1';
                                            } else {
                                                gd.showError('删除失败 ' + (msg.resultMsg || ''));
                                            }
                                        })
                                    }
                                }, {
                                    text: '取消',
                                    action: function () {
                                    }
                                }],
                                success: function (dom) {
                                },
                                end: function (dom) {
                                }
                            });
                        }
                    }
                }
            ],
            approveArray: [] // 审批流程
        },
        methods: {
            // 获取审批流程
            getAllApprove: function() {
                var _this = this;
                gd.get(ctx + '/approveDefinition/getAllApproveDefinition', '', function(msg){
                    if (msg.resultCode == 0) {
                        _this.approveArray = msg.data;
                    }
                })
            },
            // 涉密扫描新建自然语义
            addNature: function(tag) {
                if (tag) return; // disabled时不可点击
                gd.showLayer({
                    id: 'addWind',//可传一个id作为标识
                    title: '新建自然语义',//窗口标题
                    content: $('#add_nature_temp').html(),//可直接传内容，也可以是$('#xxx')，或是domcument.getElementById('xxx');
                    //url: './layer_content.html',//也可以传入url作为content,
                    size: [580, 400],//窗口大小，直接传数字即可，也可以是['600px','400px']
                    autoFocus:false,//自动对输入框获取焦点，默认为ture
                    btn: [
                        {
                            text: '确定',
                            action: function (dom) {
                                var postData = $('#addWind #add_dept_form').serializeJSON();
                                app.filescan.content.datalist[1].natureId ++; // id自增

                                var item = {
                                    id: app.filescan.content.datalist[1].natureId,
                                    rulename: postData.rulename.split(':')[0],
                                    regexps: postData.rulename.split(':')[1],
                                    extend_type: parseInt(postData.extend_type.split(':')[1]),
                                    extend_name: postData.extend_type.split(':')[0],
                                    extend_content: postData.extend_content == "" ? '--' : postData.extend_content,
                                    remark: postData.remark == "" ? '--' : postData.remark
                                };

                                app.filescan.content.datalist[1].regexps.push(item);

                                dom.close();
                                return false;//阻止弹窗自动关闭
                            }
                        },
                        {
                            text: '取消',
                            action: function () {

                            }
                        }
                    ],
                    success: function (dom) {//参数为当前窗口dom对象

                    },
                    end: function (dom) {//参数为当前窗口dom对象
                        // gd.showSuccess('窗口关闭了');
                    }
                });
            },
            //  涉密扫描自然语义删除单个
            delNatureOne: function(id) {
                var _this = this;
                var domDelSingle = gd.showConfirm({
                    id: 'wind',
                    content: '确定要删除吗?',
                    btn: [{
                        text: '确定',
                        class: 'gd-btn-danger', //也可以自定义类
                        action: function (dom) {
                            var regexpsArr = app.filescan.content.datalist[1].regexps;
                            app.filescan.content.datalist[1].regexps = regexpsArr.filter(function(item) {
                                return item.id != id;
                            });

                            var idIndex = _this.regexpsIds.indexOf(id);
                            if (idIndex >= 0) {
                                _this.regexpsIds.splice(idIndex, 1);
                            }
                        }
                    }, {
                        text: '取消',
                        action: function () {
                        }
                    }]
                });

            },
            //  涉密扫描自然语义删除多个
            delNatureMore: function(tag) {
                if (tag) return; // disabled时不可点击
                if (this.regexpsIds.length == 0){
                    gd.showWarning('请选择要删除的自然语义');
                    return;
                }
                var _this = this;
                var domDelMore = gd.showConfirm({
                    id: 'wind',
                    content: '确定要删除吗?',
                    btn: [{
                        text: '确定',
                        class: 'gd-btn-danger', //也可以自定义类
                        action: function (dom) {
                            for (var i = 0; i < _this.regexpsIds.length; i++) {
                                var regexpsArr = app.filescan.content.datalist[1].regexps;
                                app.filescan.content.datalist[1].regexps = regexpsArr.filter(function(item) {
                                    return item.id != _this.regexpsIds[i];
                                });
                            }
                            _this.regexpsIds = [];
                        }
                    }, {
                        text: '取消',
                        action: function () {
                        }
                    }]
                });

            },
            // 涉密扫描单选
            checkedOne: function(id) {
                var idIndex = this.regexpsIds.indexOf(id);
                idIndex >= 0 ? this.regexpsIds.splice(idIndex, 1) : this.regexpsIds.push(id);
            },
            // 保存
            save: function() {

                // 屏幕水印
                if (this.sbscrnwatermark.enable == 1 && $('.j-screen-water').find('input[type=checkbox]:checked').length == 0) {
                    $('.gd-tab-li').eq(0).click();
                    gd.showWarning('请至少选择一项屏幕水印');
                    return;
                }

                // 屏幕水印的自定义
                if (this.sbscrnwatermark.enable == 1 && this.sbscrnwatermark.content.manual == 1 && $('#screen_manual_text').val() == '') {
                    $('.gd-tab-li').eq(0).click();
                    gd.showWarning('请填写自定义水印内容');
                    $('#screen_manual_text').focus();
                    return;
                }

                // 文件管控 有效期限
                if (this.sbfileoutcfg.enable == 1 && this.sbfileoutcfg.content.isvalidtime == 1 && $('#file_valid_time').val() == '') {
                    $('.gd-tab-li').eq(1).click();
                    gd.showWarning('请填写有效期限');
                    $('#file_valid_time').focus();
                    return;
                } else {
                    var reg = /^[1-9]+[0-9]*/;
                    var valid = $('#file_valid_time').val();
                    if (!reg.test(valid)) {
                        if (this.sbfileoutcfg.enable == 1) {
                            gd.showWarning('只可输入大于0的整数');
                            $('#file_valid_time').focus();
                            $('.gd-tab-li').eq(1).click();
                            return;
                        } else {
                            this.sbfileoutcfg.content.validtime = 7;
                        }    
                    }
                }

                // 密码验证
                if (this.sbfileoutcfg.enable == 1 && this.sbfileoutcfg.content.ispwd == 1 && $('#file_psw').val() == '') {
                    $('.gd-tab-li').eq(1).click();
                    gd.showWarning('请填写密码验证');
                    $('#file_psw').focus();
                    return;
                } else {
                    var reg = /^[1-9]+[0-9]*/;
                    var pwd = $('#file_psw').val();
                    if (!reg.test(pwd)) {
                        if (this.sbfileoutcfg.enable == 1) {
                            gd.showWarning('只可输入大于0的整数');
                            $('#file_psw').focus();
                            $('.gd-tab-li').eq(1).click();
                            return;
                        } else {
                            this.sbfileoutcfg.content.pwd = 6;
                        }    
                    } 
                }

                // 打开次数
                if (this.sbfileoutcfg.enable == 1 && this.sbfileoutcfg.content.isopencount == 1 && $('#file_open_count').val() == '') {
                    $('.gd-tab-li').eq(1).click();
                    gd.showWarning('请填写打开次数');
                    $('#file_open_count').focus();
                    return;
                } else {
                    var reg = /^[1-9]+[0-9]*/;
                    var count = $('#file_open_count').val();
                    if (!reg.test(count)) {
                        if (this.sbfileoutcfg.enable == 1) {
                            gd.showWarning('只可输入大于0的整数');
                            $('#file_open_count').focus();
                            $('.gd-tab-li').eq(1).click();
                            return;
                        } else {
                            this.sbfileoutcfg.content.opencount = 1;
                        }  
                    }  
                }
                
                if (this.sbfileoutcfg.enable == 1 && this.sbfileoutcfg.content.scwatermark.enable == 1 && $('.j-file-screen-water').find('input[type=checkbox]:checked').length == 0) {
                    $('.gd-tab-li').eq(1).click();
                    gd.showWarning('请至少选择一项文件外发的水印内容');
                    return;
                }
                // 文件导出
                // if (this.sbfileopt.enable == 1 && $('.j-file-out').find('input[type=checkbox]:checked').length == 0) {
                //     $('.gd-tab-li').eq(1).click();
                //     gd.showWarning('请至少选择一项文件导出方式');
                //     return;
                // }

                // 涉密扫描
                if (this.filescan.enable == 1 && this.filescan.content.type == 1 && $('.j-scan-checkbox:checked').length == 0) {
                    $('.gd-tab-li').eq(2).click();
                    gd.showWarning('请至少选择一项识别方式');
                    return;
                }

                if (this.filescan.enable == 1 && this.filescan.content.datalist[0].iskeywords == 1 && $('#keywords_value').val() == '') {
                    $('.gd-tab-li').eq(2).click();
                    gd.showWarning('请填写关键字');
                    $('#keywords_value').focus();
                    return;
                }

                // 打印管控--屏幕水印
                if (this.printwatermark.enable == 1 && $('.j-print-screen').find('input[type=checkbox]:checked').length == 0) {
                    $('.gd-tab-li').eq(3).click();
                    gd.showWarning('请至少选择一项屏幕水印');
                    return;
                }

                // 屏幕水印的自定义
                if (this.printwatermark.enable == 1 && this.printwatermark.content.manual == 1 && $('#print_screen_manual_text').val() == '') {
                    $('.gd-tab-li').eq(3).click();
                    gd.showWarning('请填写自定义水印内容');
                    $('#print_screen_manual_text').focus();
                    return;
                }
                this.sbfileoutcfg.content.validtime = parseInt(this.sbfileoutcfg.content.validtime);
                this.sbfileoutcfg.content.pwd = parseInt(this.sbfileoutcfg.content.pwd);
                this.sbfileoutcfg.content.opencount = parseInt(this.sbfileoutcfg.content.opencount);
                this.sbfileoutcfg.content.scwatermark.opacity = parseInt(this.sbfileoutcfg.content.scwatermark.opacity);
                this.sbfileoutcfg.content.scwatermark.direction = parseInt(this.sbfileoutcfg.content.scwatermark.mydirection);

                this.sbscrnwatermark.content.direction = parseInt(this.sbscrnwatermark.content.mydirection);
                this.sbscrnwatermark.content.opacity = parseInt(this.sbscrnwatermark.content.opacity);
                this.sbscrnwatermark.content.fontsize = parseInt(this.sbscrnwatermark.content.fontsize);

                this.filescan.content.type = parseInt(this.filescan.content.type);

                var objAll = {
                    policyid: policyId,
                    content: {
                        sbscrnwatermark: this.sbscrnwatermark,
                        sbfileoutcfg: this.sbfileoutcfg,
                        sbfileopt: this.sbfileopt,
                        filescan: this.filescan,
                        printwatermark: this.printwatermark
                    }
                };

                $.ajax({
                    type: 'post',
                    url: ctx + '/policy/updatePolicyJsonFile',
                    contentType: 'application/json;charset=utf-8',//指定为json类型
                    //数据格式是json串
                    data: JSON.stringify(objAll),
                    success: function (msg) {//返回json结果
                        if (msg.resultCode == 0) {
                            gd.showSuccess('保存成功');
                        }
                        else {
                            gd.showError('保存失败 ' + (msg.resultMsg || ''));
                        }
                    },
                    error: function (msg) {
                        gd.showError('保存失败 ' + (msg.resultMsg || ''));
                    }
                });
            }

        },
        mounted: function() {
            this.getAllApprove();
        },
        watch:{
            sbscrnwatermark: {
                handler: function(newValue, oldValue) {
                    var colorVal = newValue.content.mycolor;
                    newValue.content.color = parseInt(colorVal.substring(1, colorVal.length), 16);
                },
                deep: true
            },
            sbfileoutcfg: {
                handler: function(newValue, oldValue) {
                    var colorVal = newValue.content.scwatermark.mycolor;
                    newValue.content.color = parseInt(colorVal.substring(1, colorVal.length), 16);
                },
                deep: true
            }
        }
    });

    $('body').
        // 自然语义全选框
        on('click', '.j-check-all', function() {
            var isChecked = $(this).is(':checked');
            $('.j-check-one').each(function() {
                if ($(this).is(':checked') !== isChecked) {
                    $(this).parent().click();
                }
            })
        })
        // 自然语义单选框
        .on('click', '.j-check-one', function() {
            var isChecked = $('.j-check-all').is(':checked'); // 全选是否选中
            var len = $('.j-check-one:not(:checked)').length; // 单选没有选中的个数
            if ((len == 0 && !isChecked) || (len !== 0 && isChecked)) {
                $('.j-check-all').parent().click();
            }
        })
        .on('mouseenter', '[tooltipnew]', function() {
            var _this = $(this);
            var msg = $(this)
                .attr('tooltipnew')
                .trim();

            gd.showTip(this, msg, {
                id: 'tips',//如果传一个id，将关闭之前相同id的tip
                time: 0,//默认在3秒内关闭，可以自定义关闭时间，0为不自动关闭
                position: 'right'//设置位置，默认自动
            });
            $(this).one('mouseleave', function() {
                gd.closeTip(tips);
            });
        });

</script>
</body>
