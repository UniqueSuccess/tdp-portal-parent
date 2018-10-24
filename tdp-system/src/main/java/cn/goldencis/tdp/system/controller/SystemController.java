package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.common.utils.ComputerInfoUtil;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.*;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.JsonUtil;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.neiwang.vdpjni.IFConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Created by limingchao on 2018/1/5.
 */
@Controller
@RequestMapping(value = "/systemSetting")
public class SystemController {

    @Autowired
    private IOperationLogService logService;

    /**
     * 系统设置主页面，同时查询网络配置
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            //获取网络配置
            JSONObject obj = new JSONObject(IFConfig.getIFConfig());
            List<Map<String, Object>> datalist = new ArrayList<>();
            if (obj != null) {
                Iterator<String> it = obj.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    Map<String, Object> eth = new HashMap<>();
                    JSONObject ethObj = (JSONObject) obj.get(key);
                    eth.put("name", key);
                    eth.put("addr", ethObj.get("addr"));
                    eth.put("gateway", ethObj.get("gateway"));
                    eth.put("mask", ethObj.get("mask"));
                    datalist.add(eth);
                }
            }
            Collections.reverse(datalist);
            modelAndView.addObject("data", datalist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("roleTypeList", JsonUtil.getObjectToString(ConstantsDto.ROLE_LIST));
        modelAndView.setViewName("system/setting/index");
        return modelAndView;
    }

    /**
     * 保存网络配置
     * @param netConfigsForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/savenetconfig", produces = "application/json", method = RequestMethod.POST)
    public String savenetconfig(NetConfigsForm netConfigsForm, HttpServletRequest request) {
        List<NetConfigDO> netConfigs = netConfigsForm.getNetConfigs();
        if (netConfigs != null && !netConfigs.isEmpty()) {
            Map<String, Object> configMap = new HashMap<>();
            if (netConfigs != null) {
                for (int i = 0; netConfigs.size() > i; i++) {
                    NetConfigDO netdo = netConfigs.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("addr", netdo.getAddr());
                    map.put("gateway", netdo.getGateway());
                    map.put("mask", netdo.getMask());
                    JSONObject json = new JSONObject(map);
                    configMap.put(netdo.getEthname(), json);
                }
            }
            JSONObject json = new JSONObject(configMap);
            /*"{\"eth0\":{\"addr\":\"192.168.3.90\",\"gateway\":\"192.168.3.1\",\"mask\":\"255.255.255.0\"}}"*/
            boolean flag = IFConfig.setIFConfig(json.toString());

            //自定义记录日志
            try {
                OperationLogDO log = new OperationLogDO();
                log.setIp(NetworkUtil.getIpAddress(request));
                UserDO currentUser = GetLoginUser.getLoginUser();
                String res = StringUtil.getResultStrByBoolean(flag);
                String detail = String.format("【%s】在【设置-网络】中执行【保存】操作" + res + "：【账户名：%s，网络配置信息为：%s】", currentUser.getName(),currentUser.getUserName(), json.toString());
                log.setLogOperateParam(detail);
                log.setLogPage("系统设置-网络");
                log.setTime(new Date());
                log.setUserName(currentUser.getUserName());
                log.setLogType(LogType.OTHER.value());
                log.setLogDesc(String.format("SystemController.savenetconfig(..) invoke"));
                logService.create(log);
            } catch (Exception e) {
            }
            if (flag) {
                return "success";
            } else {
                return "failed";
            }

        }
        return "nodata";
    }


    @ResponseBody
    @RequestMapping(value = "/getComputerInfo", method = RequestMethod.GET)
    public ResultMsg getComputerInfo() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            Map<String, Object> resMap = new HashMap<>();

            double cpu = ComputerInfoUtil.cpu();
            resMap.put("cpu", cpu);
            Map memory = ComputerInfoUtil.memory();
            resMap.put("memory", memory);
            Double mySQLUsage = ComputerInfoUtil.mySQLUsage();
            resMap.put("mySQLUsage", mySQLUsage);

            resultMsg.setData(resMap);
            resultMsg.setResultMsg("");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/getJavaLibraryPath", method = RequestMethod.GET)
    public ResultMsg getJavaLibraryPath() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            String property = System.getProperty("java.library.path");
            resultMsg.setData(property);
            resultMsg.setResultMsg("获取JavaLibrary的路径成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取JavaLibrary的路径失败！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
