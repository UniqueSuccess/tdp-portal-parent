<!--userlist.jsp -->
<!--
<%@ page language="java" pageEncoding="UTF-8" %>
-->
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>

<head>
    <title>金盾VDP</title>
    <link href="${ctxCss}/colpick/colpick.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxCss}/policy/index.css" rel="stylesheet" type="text/css"/>
</head>

<div class="main-right">
    <div class="top-bar">
        <div class="top-title">${policyName}</div>
        <c:if test="${policyId != 1}">
            <div class="bar-item-box policy-delete">
                <i class="iconfont icon-btn-delete"></i>
            </div>
        </c:if>
    </div>
</div>
<div class="policy-content content-box">

</div>
<div class="save">
    <div class="policy-save">
        <span>保存</span>
    </div>
</div>

<script id="policyContent" type="text/html">
    <form action="">
        <%--屏幕水印--%>
        <div class="screenWater">
            <div class="policy-title">
                <span>屏幕水印</span>
                <div class="beauty-switch">
                    <input id="screenSwitch" value="1" name="screenSwitch" type="checkbox" {{if
                           $data.sbscrnwatermark.enable==1}} checked {{/if}}>
                    <label for="screenSwitch" class="switch-icon"></label>
                </div>

            </div>
            <div class="policy-con">
                <%--<div class="policy-con-title">水印内容</div>--%>
                <div class="beauty-radio-policy fl margin-left-lg">
                    <input id="screenWaterShow" value="0" disabled class="beauty-radio-input" type="radio" name="screenWater"
                           {{if $data.sbscrnwatermark.content.isshow==0}} checked {{/if}} >
                    <label for="screenWaterShow" class="beauty-radio-label">显式水印</label>
                </div>
                <div class="beauty-radio-policy inline-block">
                    <input id="screenWaterHidden" value="1" disabled class="beauty-radio-input" type="radio"
                           name="screenWater" {{if $data.sbscrnwatermark.content.isshow==1}} checked {{/if}} >
                    <label for="screenWaterHidden" class="beauty-radio-label">隐式水印</label>
                </div>
                <div class="policy-content-con">
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="deptWater" name="deptWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.depname==1}} checked {{/if}} class="j-check-device-all"
                            value="1">
                            <label for="deptWater" class="checkbox-icon"></label>
                        </div>
                        <label for="deptWater">部门名称</label>
                    </label>
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="nameWater" name="nameWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.username==1}} checked {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="nameWater" class="checkbox-icon"></label>
                        </div>
                        <label for="nameWater">真实姓名</label>
                    </label>
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="ipWater" name="ipWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.ip==1}} checked {{/if}} class="j-check-device-all"
                            value="1">
                            <label for="ipWater" class="checkbox-icon"></label>
                        </div>
                        <label for="ipWater">IP地址</label>
                    </label>
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="MACWater" name="MACWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.macaddress==1}} checked {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="MACWater" class="checkbox-icon"></label>
                        </div>
                        <label for="MACWater">MAC地址</label>
                    </label>
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="computerWater" name="computerWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.computername==1}} checked {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="computerWater" class="checkbox-icon"></label>
                        </div>
                        <label for="computerWater">计算机名称</label>
                    </label>
                    <label for="" class="ms">
                        <div class="beauty-checkbox">
                            <input id="timeWater" name="timeWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.time==1}} checked {{/if}} class="j-check-device-all"
                            value="1">
                            <label for="timeWater" class="checkbox-icon"></label>
                        </div>
                        <label for="timeWater">终端时间</label>
                    </label>
                    <label for="" class="w300" class="ms">
                        <div class="beauty-checkbox">
                            <input id="diyWater" name="diyWater" type="checkbox" {{if
                                   $data.sbscrnwatermark.content.manual==1}} checked {{/if}} class="j-check-device-all"
                            value="1">
                            <label for="diyWater" class="checkbox-icon"></label>
                        </div>
                        <label for="diyWater">自定义水印</label>
                        <input type="text" disabled name="screendiyWaterContent" placeholder="自定义水印内容"
                               value="{{$data.sbscrnwatermark.content.manualtext}}"/>
                    </label>
                </div>
                <%--屏幕水印--%>
                <div class="policy-con-title margin-bottom-sm margin-left-lg">水印样式</div>
                <div class="policy-content-con">
                    <div class="policy-content-con-location">
                        <label for="" class="w50 inline-block">位置</label>
                        <div class="location inline-block text-top">
                            <label for="" class="block margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="location_tile" name="location" data-val="0" class="j-check-local" {{if
                                           $data.sbscrnwatermark.content.localtion==0}} checked {{/if}} type="radio" value="0">
                                    <label for="location_tile" class="beauty-radio-label"></label>
                                </div>
                                <label for="location_tile" class="">平铺</label>
                            </label>
                            <label for="" class="margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="location_diy" name="location" class="j-check-local" {{if
                                           $data.sbscrnwatermark.content.localtion!=0}} checked {{/if}} {{if $data.sbscrnwatermark.enable==0 }} disabled {{/if}}   type="radio" value="0">
                                    <label for="location_diy" class="beauty-radio-label"></label>
                                </div>
                                <label for="location_diy" class="">自定义</label>
                            </label>
                            <div class="j-location-content margin-bottom-sm margin-left-xxl {{if
                                           $data.sbscrnwatermark.content.localtion==0}} none {{/if}}">
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="top_left_corner" name="location" type="checkbox" class="j-check-local-all" value="1">
                                        <label for="top_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="top_left_corner">左上角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="top_right_corner" name="location" type="checkbox" class="j-check-local-all" value="2">
                                        <label for="top_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="top_right_corner">右上角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="bottom_left_corner" name="location" type="checkbox" class="j-check-local-all" value="8">
                                        <label for="bottom_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="bottom_left_corner">左下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="bottom_right_corner" name="location" type="checkbox" class="j-check-local-all" value="16">
                                        <label for="bottom_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="bottom_right_corner">右下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="center_corner" name="location" type="checkbox" class="j-check-local-all" value="4">
                                        <label for="center_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="center_corner">居中</label>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="policy-content-con-direction">

                        <div class="direction inline-block left-opt-item-box">
                            <label for="" class="w50 inline-block">颜色</label>
                            <div class="bar-item-box w60 margin-right60">
                                <div class="bar-item bar-item-dropdown {{if $data.sbscrnwatermark.enable==0}} select-disable {{else}} j-bar-item-dropdown {{/if}} ">
                                    <div class="color-content">
                                        <div class="color-content-show"></div>
                                        <div class="color-content-title none">{{if $data.sbscrnwatermark.content.color == '16711680'}} 红色 {{else if $data.sbscrnwatermark.content.color == '65280'}} 绿色 {{else if $data.sbscrnwatermark.content.color == '65535'}}青色{{else if $data.sbscrnwatermark.content.color == '16777215'}} 白色 {{else}} {{$data.sbscrnwatermark.content.tcolor}} {{/if}}</div>
                                    </div>
                                    <input id="color_content" name="color_content" class="dropdown-input" type="hidden" value="{{$data.sbscrnwatermark.content.color}}" readonly="">
                                    <ul id="" class="wind-content-input j-color-list none">
                                        <li data-color="ff0000"><span class="color-show color-red"></span></li>
                                        <li data-color="00ff00"><span class="color-show color-green"></span></li>
                                        <li data-color="00ffff"><span class="color-show color-cyan"></span></li>
                                        <li data-color="ffffff"><span class="color-show color-white"></span></li>
                                        <li id="diycolor"><span class="color-show color-other"></span></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="screen-is-show inline-block" style="{{if $data.sbscrnwatermark.content.isshow==1 }} display: none;{{/if}} vertical-align: middle">
                            <label for="" class="w65 inline-block">透明度</label>
                            <select name="opcity" id="opcity" class="left-opt-item-select w150 margin-right60 {{if
                           $data.sbscrnwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbscrnwatermark.enable==0}} disabled {{/if}}>
                            <option value="255" {{if
                                    $data.sbscrnwatermark.content.opacity==255}} selected {{/if}}>0%</option>
                            <option value="225" {{if
                                    $data.sbscrnwatermark.content.opacity==225}} selected {{/if}}>10%</option>
                            <option value="200" {{if
                                    $data.sbscrnwatermark.content.opacity==200}} selected {{/if}}>20%</option>
                            <option value="175" {{if
                                    $data.sbscrnwatermark.content.opacity==175}} selected {{/if}}>30%</option>
                            <option value="150" {{if
                                    $data.sbscrnwatermark.content.opacity==150}} selected {{/if}}>40%</option>
                            <option value="125" {{if
                                    $data.sbscrnwatermark.content.opacity==125}} selected {{/if}}>50%</option>
                            <option value="100" {{if
                                    $data.sbscrnwatermark.content.opacity==100}} selected {{/if}}>60%</option>
                            <option value="75" {{if
                                    $data.sbscrnwatermark.content.opacity==75}} selected {{/if}}>70%</option>
                            <option value="50" {{if
                                    $data.sbscrnwatermark.content.opacity==50}} selected {{/if}}>80%</option>
                            <option value="25" {{if
                                    $data.sbscrnwatermark.content.opacity==25}} selected {{/if}}>90%</option>
                            </select>
                            <label for="" class="w50 inline-block">方向</label>
                            <select name="direction" id="direction" class="left-opt-item-select w150 margin-right60 {{if
                           $data.sbscrnwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbscrnwatermark.enable==0}} disabled {{/if}}>
                                <option value="0" {{if
                                        $data.sbscrnwatermark.content.direction==0}} selected {{/if}}>左斜</option>
                                <option value="1" {{if
                                        $data.sbscrnwatermark.content.direction==1}} selected {{/if}}>右斜</option>
                                <option value="2" {{if
                                        $data.sbscrnwatermark.content.direction==2}} selected {{/if}}>横向</option>
                                <option value="3" {{if
                                        $data.sbscrnwatermark.content.direction==3}} selected {{/if}}>纵向</option>
                            </select>
                                <label for="" class="w60 inline-block">字体大小</label>
                                <select name="fontSize" id="fontSize" class="left-opt-item-select w60 margin-right60 {{if
                           $data.sbscrnwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbscrnwatermark.enable==0}} disabled {{/if}}>
                                <option value="14" {{if
                                        $data.sbscrnwatermark.content.fontsize==14}} selected {{/if}}>14</option>
                                <option value="16" {{if
                                        $data.sbscrnwatermark.content.fontsize==16}} selected {{/if}}>16</option>
                                <option value="18" {{if
                                        $data.sbscrnwatermark.content.fontsize==18}} selected {{/if}}>18</option>
                                <option value="20" {{if
                                        $data.sbscrnwatermark.content.fontsize==20}} selected {{/if}}>20</option>
                                <option value="22" {{if
                                        $data.sbscrnwatermark.content.fontsize==22}} selected {{/if}}>22</option>
                                <option value="24" {{if
                                        $data.sbscrnwatermark.content.fontsize==24}} selected {{/if}}>24</option>
                                <option value="26" {{if
                                        $data.sbscrnwatermark.content.fontsize==26}} selected {{/if}}>26</option>
                                <option value="28" {{if
                                        $data.sbscrnwatermark.content.fontsize==28}} selected {{/if}}>28</option>
                                <option value="30" {{if
                                        $data.sbscrnwatermark.content.fontsize==30}} selected {{/if}}>30</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--外发--%>
        <div class="fileOut">
            <div class="policy-title">
                <span>文件外发</span>
                <div class="beauty-switch">
                    <input id="fileOutSwitch" value="1" name="fileOutSwitch" type="checkbox" {{if
                           $data.sbfileoutcfg.enable==1}} checked {{/if}}>
                    <label for="fileOutSwitch" class="switch-icon"></label>
                </div>
            </div>
            <div class="policy-con outC">
                <label for="" class="out">
                    <div class="beauty-checkbox">
                        <input id="approveOut" name="approveOut" type="checkbox" {{if
                               $data.sbfileoutcfg.content.mode==2}} checked {{/if}} {{if $data.sbfileoutcfg.enable==0}} disabled {{/if}} class="j-check-device-all"
                        value="2">
                        <label for="approveOut" class="checkbox-icon"></label>
                    </div>
                    <label for="approveOut">审批外发</label>
                    <label id="{{if $data.sbfileoutcfg.content.mode==2}}fileOutApprove{{/if}}" class="approveButton fileOutApprove {{if $data.sbfileoutcfg.content.mode!=2}}color-grey{{/if}}">审批流程</label>
                </label>
                <label for="" class="out" style="display: none">
                    <div class="beauty-checkbox">
                        <input id="forbidScreen" name="forbidScreen" type="checkbox" {{if
                               $data.sbfileoutcfg.content.disablesc==1}} checked {{/if}} class="j-check-device-all"
                        value="1">
                        <label for="forbidScreen" class="checkbox-icon"></label>
                    </div>
                    <label for="forbidScreen">禁止截屏</label>
                </label>
                <label for="" class="out">
                    <div class="beauty-checkbox">
                        <input id="settingTime" name="settingTime" type="checkbox" {{if
                               $data.sbfileoutcfg.content.validtimecheck==1}} checked {{/if}} {{if $data.sbfileoutcfg.enable==0}} disabled {{/if}} class="j-check-device-all"
                        value="1">
                        <label for="settingTime" class="checkbox-icon"></label>
                    </div>
                    <label for="settingTime">有效时间</label>
                    <input type="text" name="settingTimes" disabled value="{{$data.sbfileoutcfg.content.validtime}}"
                           class="smallinput"/>天
                </label>
                <label for="" class="out">
                    <div class="beauty-checkbox">
                        <input id="passwordVerification" name="passwordVerification" type="checkbox" {{if
                               $data.sbfileoutcfg.content.pwdcheck==1}} checked {{/if}} {{if $data.sbfileoutcfg.enable==0}} disabled {{/if}} class="j-check-device-all"
                        value="1">
                        <label for="passwordVerification" class="checkbox-icon"></label>
                    </div>
                    <label for="passwordVerification">密码验证</label>
                    <input type="text" disabled name="passwordVerifications" value="{{$data.sbfileoutcfg.content.pwd}}"
                           class="smallinput"/>位
                </label>
                <label for="" class="out">
                    <div class="beauty-checkbox">
                        <input id="allowOpen" name="allowOpen" type="checkbox" {{if
                               $data.sbfileoutcfg.content.opencountcheck==1}} checked {{/if}} {{if $data.sbfileoutcfg.enable==0}} disabled {{/if}} class="j-check-device-all"
                        value="1">
                        <label for="allowOpen" class="checkbox-icon"></label>
                    </div>
                    <label for="allowOpen">允许打开</label>
                    <input type="text" disabled name="allowOpens" value="{{$data.sbfileoutcfg.content.opencount}}"
                           class="smallinput"/>次
                    <%--<div class="beauty-checkbox">
                        <input id="allowOpenDelete" disabled name="allowOpenDelete" type="checkbox" {{if
                               $data.sbfileoutcfg.content.autodelete==1}} checked {{/if}} class="j-check-device-all"
                        value="1">
                        <label for="allowOpenDelete" class="checkbox-icon"></label>
                    </div>
                    <span class="autoDelete">超出自动删除</span>--%>
                </label>
                <label for="" class="out">
                    <div class="beauty-checkbox">
                        <input id="isScreenWater" name="isScreenWater" type="checkbox" {{if
                               $data.sbfileoutcfg.content.scwatermark.enable==1}} checked {{/if}} {{if $data.sbfileoutcfg.enable==0}} disabled {{/if}} class="j-check-device-all" value="1">
                        <label for="isScreenWater" class="checkbox-icon"></label>
                    </div>
                    <label for="isScreenWater">水印</label>
                </label>
            </div>
            <div class="policy-con policy-list">
                <div class="beauty-radio-policy fl margin-left-lg">
                    <input id="outWaterShow" value="0" disabled class="beauty-radio-input" type="radio" name="outWater"
                           {{if $data.sbfileoutcfg.content.scwatermark.isshow==0}} checked {{/if}} >
                    <label for="outWaterShow" class="beauty-radio-label">显式水印</label>
                </div>
                <div class="beauty-radio-policy inline-block">
                    <input id="outWaterHidden" value="1" disabled class="beauty-radio-input" type="radio"
                           name="outWater" {{if $data.sbfileoutcfg.content.scwatermark.isshow==1}} checked {{/if}} >
                    <label for="outWaterHidden" class="beauty-radio-label">隐式水印</label>
                </div>
                <div class="waterShowContent">
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutDept" name="fileOutDept" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.depname==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutDept" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutDept">部门名称</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutName" name="fileOutName" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.username==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutName" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutName">真实姓名</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutIpWater" name="fileOutIpWater" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.ip==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutIpWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutIpWater">IP地址</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutMACWater" name="fileOutMACWater" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.macaddress==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutMACWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutMACWater">MAC地址</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutComputerWater" name="fileOutComputerWater" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.computername==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutComputerWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutComputerWater">计算机名称</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutTimeWater" name="fileOutTimeWater" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.time==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutTimeWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutTimeWater">终端时间</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileOutRecv" name="fileOutRecv" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.recv==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutRecv" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutRecv">接收方信息</label>
                    </label>
                    <label for="" class="w300">
                        <div class="beauty-checkbox">
                            <input id="fileOutDiyWater" name="fileOutDiyWater" type="checkbox" {{if
                                   $data.sbfileoutcfg.content.scwatermark.content.manual==1}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}
                            class="j-check-device-all" value="1">
                            <label for="fileOutDiyWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileOutDiyWater">自定义水印</label>
                        <input type="text" disabled
                               value="{{$data.sbfileoutcfg.content.scwatermark.content.manualtext}}"
                               name="outdiyWaterContent" placeholder="自定义水印内容"/>
                    </label>
                </div>
                <div class="policy-con-title margin-bottom-sm margin-left-lg">水印样式</div>
                <div class="policy-content-con margin-left-sm">
                    <div class="policy-content-con-location">
                        <label for="" class="w50 inline-block">位置</label>
                        <div class="location inline-block text-top">
                            <label for="" class="block margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="out_location_tile" name="outlocation" data-val="0" class="j-check-local" {{if
                                           $data.sbfileoutcfg.content.scwatermark.content.localtion==0}} checked {{/if}} type="radio" value="0">
                                    <label for="out_location_tile" class="beauty-radio-label"></label>
                                </div>
                                <label for="out_location_tile" class="">平铺</label>
                            </label>
                            <label for="" class="margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="out_location_diy" name="outlocation" class="j-check-local" {{if
                                           $data.sbfileoutcfg.content.scwatermark.content.localtion!=0}} checked {{/if}} {{if $data.sbfileoutcfg.content.scwatermark.enable==0 }} disabled {{/if}} type="radio" value="0">
                                    <label for="out_location_diy" class="beauty-radio-label"></label>
                                </div>
                                <label for="out_location_diy" class="">自定义</label>
                            </label>
                            <div class="j-location-content margin-bottom-sm margin-left-xxl {{if
                                           $data.sbfileoutcfg.content.scwatermark.content.localtion==0}} none {{/if}}">
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="out_top_left_corner" name="outlocation" type="checkbox" class="j-check-local-all-out" value="1">
                                        <label for="out_top_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="out_top_left_corner">左上角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="out_top_right_corner" name="outlocation" type="checkbox" class="j-check-local-all-out" value="2">
                                        <label for="out_top_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="out_top_right_corner">右上角</label>
                                </label>

                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="out_bottom_left_corner" name="outlocation" type="checkbox" class="j-check-local-all-out" value="8">
                                        <label for="out_bottom_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="out_bottom_left_corner">左下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="out_bottom_right_corner" name="outlocation" type="checkbox" class="j-check-local-all-out" value="16">
                                        <label for="out_bottom_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="out_bottom_right_corner">右下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="out_center_corner" name="outlocation" type="checkbox" class="j-check-local-all-out" value="4">
                                        <label for="out_center_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="out_center_corner">居中</label>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="policy-content-con-direction">

                        <div class="direction inline-block left-opt-item-box">
                            <label for="" class="w50 inline-block" style="vertical-align: middle">颜色</label>
                            <div class="bar-item-box w60 margin-right60">
                                <div class="bar-item bar-item-dropdown {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} select-disable {{else}} j-bar-item-dropdown_out{{/if}}">
                                    <div class="color-content">
                                        <div class="color-content-show"></div>
                                        <div class="color-content-title none">{{if $data.sbfileoutcfg.content.scwatermark.content.color == '16711680'}} 红色 {{else if $data.sbfileoutcfg.content.scwatermark.content.color == '65280'}} 绿色 {{else if $data.sbfileoutcfg.content.scwatermark.content.color == '65535'}}青色{{else if $data.sbfileoutcfg.content.scwatermark.content.color == '16777215'}} 白色 {{else}} {{$data.sbfileoutcfg.content.scwatermark.content.tcolor}} {{/if}}</div>
                                    </div>
                                    <input id="out_color_content" name="out_color_content" class="dropdown-input" type="hidden" value="{{$data.sbfileoutcfg.content.scwatermark.content.color}}" readonly="">
                                    <ul class="wind-content-input j-color-list_out none">
                                        <li data-color="ff0000"><span class="color-show color-red"></span></li>
                                        <li data-color="00ff00"><span class="color-show color-green"></span></li>
                                        <li data-color="00ffff"><span class="color-show color-cyan"></span></li>
                                        <li data-color="ffffff"><span class="color-show color-white"></span></li>
                                        <li id="out_diycolor"><span class="color-show color-other"></span></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="outer-is-show inline-block" style="{{if $data.sbfileoutcfg.content.scwatermark.isshow ==1}} display: none;{{/if}} vertical-align: middle">
                                <label for="" class="w65 inline-block">透明度</label>
                                <select name="out_opcity" id="out_opcity" class="left-opt-item-select w150 margin-right60 {{if
                                    $data.sbfileoutcfg.content.scwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}>
                                <option value="255" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==255}} selected {{/if}}>0%</option>
                                <option value="225" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==225}} selected {{/if}}>10%</option>
                                <option value="200" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==200}} selected {{/if}}>20%</option>
                                <option value="175" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==175}} selected {{/if}}>30%</option>
                                <option value="150" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==150}} selected {{/if}}>40%</option>
                                <option value="125" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==125}} selected {{/if}}>50%</option>
                                <option value="100" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==100}} selected {{/if}}>60%</option>
                                <option value="75" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==75}} selected {{/if}}>70%</option>
                                <option value="50" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==50}} selected {{/if}}>80%</option>
                                <option value="25" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.opacity==25}} selected {{/if}}>90%</option>
                                </select>
                            <label for="" class="w50 inline-block">方向</label>
                            <select name="out_direction" id="out_direction" class="left-opt-item-select w150 margin-right60  {{if
                                    $data.sbfileoutcfg.content.scwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbfileoutcfg.content.scwatermark.enable==0}} disabled {{/if}}>
                                <option value="0" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.direction==0}} selected {{/if}}>左斜</option>
                                <option value="1" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.direction==1}} selected {{/if}}>右斜</option>
                                <option value="2" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.direction==2}} selected {{/if}}>横向</option>
                                <option value="3" {{if
                                        $data.sbfileoutcfg.content.scwatermark.content.direction==3}} selected {{/if}}>纵向</option>
                            </select>

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="fileExport">
            <div class="policy-title">
                <span>文件导出</span>
                <div class="beauty-switch">
                    <input id="fileExportSwitch" name="fileExportSwitch" type="checkbox" value="1" {{if
                           $data.sbfileopt.enable==1}} checked {{/if}}>
                    <label for="fileExportSwitch" class="switch-icon"></label>
                </div>
            </div>
            <div class="policy-con export-c">
                <label for="" class="export">
                    <div class="beauty-checkbox">
                        <input id="approveExport" name="approveExport" type="checkbox" class="j-check-device-all"
                               value="3" {{if $data.sbfileopt.content.mode==3}} checked {{/if}} {{if $data.sbfileopt.enable==0}} disabled {{/if}}>
                        <label for="approveExport" class="checkbox-icon"></label>
                    </div>
                    <label for="approveExport">审批导出</label>
                    <label id="{{if $data.sbfileopt.content.mode==3}}fileExportApprove{{/if}}" class="approveButton fileExportApprove {{if $data.sbfileopt.content.mode!=3}}color-grey{{/if}}">审批流程</label>
                </label>
                <label for="" class="export">
                    <div class="beauty-checkbox">
                        <input id="isScreen" name="isScreen" type="checkbox" class="j-check-device-all" value="1" {{if
                               $data.sbfileopt.content.sbfileoptwatermark.enable==1}} checked {{/if}} {{if $data.sbfileopt.enable==0}} disabled {{/if}}>
                        <label for="isScreen" class="checkbox-icon"></label>
                    </div>
                    <label for="isScreen">水印</label>
                </label>
            </div>
            <div class="policy-con policy-list">

                <div class="beauty-radio-policy fl margin-left-lg">
                    <input id="waterShow" class="beauty-radio-input" disabled type="radio" name="exportWater" value="0"
                           {{if $data.sbfileopt.content.sbfileoptwatermark.isshow==0}} checked {{/if}}>
                    <label for="waterShow" class="beauty-radio-label">显式水印</label>
                </div>
                <div class="beauty-radio-policy inline-block">
                    <input id="waterHidden" class="beauty-radio-input" disabled type="radio" name="exportWater"
                           value="1" {{if $data.sbfileopt.content.sbfileoptwatermark.isshow==1}} checked {{/if}}>
                    <label for="waterHidden" class="beauty-radio-label">隐式水印</label>
                </div>
                <div class="waterShowContent">
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportDept" name="fileExportDept" type="checkbox" class="j-check-device-all"
                                   value="1" {{if $data.sbfileopt.content.sbfileoptwatermark.content.depname==1}}
                                   checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportDept" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportDept">部门名称</label>
                    </label>

                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportName" name="fileExportName" type="checkbox" class="j-check-device-all"
                                   value="1" {{if $data.sbfileopt.content.sbfileoptwatermark.content.username==1}}
                                   checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportName" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportName">真实姓名</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportIpWater" name="fileExportIpWater" type="checkbox"
                                   class="j-check-device-all" value="1" {{if
                                   $data.sbfileopt.content.sbfileoptwatermark.content.ip==1}} checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportIpWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportIpWater">IP地址</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportMACWater" name="fileExportMACWater" type="checkbox"
                                   class="j-check-device-all" value="1" {{if
                                   $data.sbfileopt.content.sbfileoptwatermark.content.macaddress==1}} checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportMACWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportMACWater">MAC地址</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportComputerWater" name="fileExportComputerWater" type="checkbox"
                                   class="j-check-device-all" value="1" {{if
                                   $data.sbfileopt.content.sbfileoptwatermark.content.computername==1}} checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportComputerWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportComputerWater">计算机名称</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportTimeWater" name="fileExportTimeWater" type="checkbox"
                                   class="j-check-device-all" value="1" {{if
                                   $data.sbfileopt.content.sbfileoptwatermark.content.time==1}} checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportTimeWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportTimeWater">终端时间</label>
                    </label>
                    <label class="ms" for="">
                        <div class="beauty-checkbox">
                            <input id="fileExportRecv" name="fileExportRecv" type="checkbox" class="j-check-device-all"
                                   value="1" {{if $data.sbfileopt.content.sbfileoptwatermark.content.recv==1}} checked
                                   {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportRecv" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportRecv">接收方信息</label>
                    </label>
                    <label for="" class="w300">
                        <div class="beauty-checkbox">
                            <input id="fileExportDiyWater" name="fileExportDiyWater" type="checkbox"
                                   class="j-check-device-all" value="1" {{if
                                   $data.sbfileopt.content.sbfileoptwatermark.content.manual==1}} checked {{/if}} {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                            <label for="fileExportDiyWater" class="checkbox-icon"></label>
                        </div>
                        <label for="fileExportDiyWater">自定义水印</label>
                        <input type="text" disabled
                               value="{{$data.sbfileopt.content.sbfileoptwatermark.content.manualtext}}"
                               name="exportdiyWaterContent" placeholder="自定义水印内容"/>
                    </label>
                </div>
                <%--文件导出--%>
                <div class="policy-con-title margin-bottom-sm margin-left-lg">水印样式</div>
                <div class="policy-content-con margin-left-sm">
                    <div class="policy-content-con-location">
                        <label for="" class="w50 inline-block">位置</label>
                        <div class="location inline-block text-top">
                            <label for="" class="block margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="export_location_tile" name="exportlocation" data-val="0" class="j-check-local" {{if
                                           $data.sbfileopt.content.sbfileoptwatermark.content.localtion==0}} checked {{/if}} type="radio" value="0">
                                    <label for="export_location_tile" class="beauty-radio-label"></label>
                                </div>
                                <label for="export_location_tile" class="">平铺</label>
                            </label>
                            <label for="" class="margin-bottom-sm">
                                <div class="beauty-radio">
                                    <input id="export_location_diy" name="exportlocation" class="j-check-local" {{if
                                           $data.sbfileopt.content.sbfileoptwatermark.content.localtion!=0}} checked {{/if}}  {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}} type="radio" value="0">
                                    <label for="export_location_diy" class="beauty-radio-label"></label>
                                </div>
                                <label for="export_location_diy" class="">自定义</label>
                            </label>
                            <div class="j-location-content margin-bottom-sm margin-left-xxl {{if
                                           $data.sbfileopt.content.sbfileoptwatermark.content.localtion==0}} none {{/if}}">
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="export_top_left_corner" name="exportlocation" type="checkbox" class="j-check-local-all-export" value="1">
                                        <label for="export_top_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="export_top_left_corner">左上角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="export_top_right_corner" name="exportlocation" type="checkbox" class="j-check-local-all-export" value="2">
                                        <label for="export_top_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="export_top_right_corner">右上角</label>
                                </label>

                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="export_bottom_left_corner" name="exportlocation" type="checkbox" class="j-check-local-all-export" value="8">
                                        <label for="export_bottom_left_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="export_bottom_left_corner">左下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="export_bottom_right_corner" name="exportlocation" type="checkbox" class="j-check-local-all-export" value="16">
                                        <label for="export_bottom_right_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="export_bottom_right_corner">右下角</label>
                                </label>
                                <label for="" class="margin-right-xxl">
                                    <div class="beauty-checkbox">
                                        <input id="export_center_corner" name="exportlocation" type="checkbox" class="j-check-local-all-export" value="4">
                                        <label for="export_center_corner" class="checkbox-icon"></label>
                                    </div>
                                    <label for="export_center_corner">居中</label>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="policy-content-con-direction">

                        <div class="direction inline-block left-opt-item-box">
                            <label for="" class="w50 inline-block" style="vertical-align: middle">颜色</label>
                            <div class="bar-item-box w60 margin-right60">
                                <div class="bar-item bar-item-dropdown {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} select-disable {{else}}  j-bar-item-dropdown_export  {{/if}}">
                                    <div class="color-content">
                                        <div class="color-content-show"></div>
                                        <div class="color-content-title none">{{if $data.sbfileopt.content.sbfileoptwatermark.content.color == '16711680'}} 红色 {{else if $data.sbfileopt.content.sbfileoptwatermark.content.color == '65280'}} 绿色 {{else if $data.sbfileopt.content.sbfileoptwatermark.content.color == '65535'}}青色{{else if $data.sbfileopt.content.sbfileoptwatermark.content.color == '16777215'}} 白色 {{else}} {{$data.sbfileopt.content.sbfileoptwatermark.content.tcolor}} {{/if}}</div>
                                    </div>
                                    <input id="export_color_content" name="export_color_content" class="dropdown-input" type="hidden" value="{{$data.sbfileopt.content.sbfileoptwatermark.content.color}}" readonly="">
                                    <ul class="wind-content-input j-color-list_export none">
                                        <li data-color="ff0000"><span class="color-show color-red"></span></li>
                                        <li data-color="00ff00"><span class="color-show color-green"></span></li>
                                        <li data-color="00ffff"><span class="color-show color-cyan"></span></li>
                                        <li data-color="ffffff"><span class="color-show color-white"></span></li>
                                        <li id="export_diycolor"><span class="color-show color-other"></span></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="export-is-show inline-block" style="{{if $data.sbfileopt.content.sbfileoptwatermark.isshow == 1}} display: none;{{/if}} vertical-align: middle">
                                <label for="" class="w65 inline-block">透明度</label>
                                <select name="export_opcity" id="export_opcity" class="left-opt-item-select w150 margin-right60 {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                                <option value="255" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==255}} selected {{/if}}>0%</option>
                                <option value="225" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==225}} selected {{/if}}>10%</option>
                                <option value="200" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==200}} selected {{/if}}>20%</option>
                                <option value="175" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==175}} selected {{/if}}>30%</option>
                                <option value="150" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==150}} selected {{/if}}>40%</option>
                                <option value="125" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==125}} selected {{/if}}>50%</option>
                                <option value="100" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==100}} selected {{/if}}>60%</option>
                                <option value="75" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==75}} selected {{/if}}>70%</option>
                                <option value="50" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==50}} selected {{/if}}>80%</option>
                                <option value="25" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.opacity==25}} selected {{/if}}>90%</option>
                                </select>
                            <label for="" class="w50 inline-block">方向</label>
                            <select name="export_direction" id="export_direction" class="left-opt-item-select w150 margin-right60  {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} select-disable {{/if}}" {{if $data.sbfileopt.content.sbfileoptwatermark.enable==0}} disabled {{/if}}>
                                <option value="0" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.direction==0}} selected {{/if}}>左斜</option>
                                <option value="1" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.direction==1}} selected {{/if}}>右斜</option>
                                <option value="2" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.direction==2}} selected {{/if}}>横向</option>
                                <option value="3" {{if
                                        $data.sbfileopt.content.sbfileoptwatermark.content.direction==3}} selected {{/if}}>纵向</option>
                            </select>

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--<div class="flowApprove">
            <div class="policy-title">
                <span>视频图片数据流转审计</span>
                <div class="beauty-switch">
                    <input id="videoApprove" name="videoApprove" type="checkbox" value="1" {{if
                           $data.videoappro.enable==1}} checked {{/if}}>
                    <label for="videoApprove" class="switch-icon"></label>
                </div>
            </div>
            <div class="policy-con"></div>
        </div>--%>
    </form>
</script>
<script src="${ctxJs}/plugins/template/template-web.js"></script>
<script src="${ctxJs}/plugins/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxJs}/plugins/colpick/colpick.js" type="text/javascript"></script>
<script src="${ctxJs}/policy/index.js"></script>
<script>
    var msg = ${resultMsg.data}
    var policyContent = template('policyContent', msg);
    $(".policy-content").html(policyContent);
    var policyId = ${policyId}

</script>