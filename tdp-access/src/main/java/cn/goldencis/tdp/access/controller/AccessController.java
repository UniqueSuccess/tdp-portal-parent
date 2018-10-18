package cn.goldencis.tdp.access.controller;

import cn.goldencis.tdp.access.entity.AccessConfig;
import cn.goldencis.tdp.access.service.IAccessService;
import cn.goldencis.tdp.access.service.impl.SaveAccessRunnable;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.CommonFileUtil;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by limingchao on 2018/1/3.
 */
@Controller
@RequestMapping(value = "/access")
public class AccessController{

    @Autowired
    private IAccessService accessService;

    @Value("${accessServer.ip}")
    private String accessServerIp;

    @Autowired
    private SaveAccessRunnable saveAccessRunnable;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IOperationLogService logService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        //读取配置信息
        Map<String, Object> configMap = new HashMap<>();
        accessService.readAccessServerConfig(configMap);
        JSONObject configJson = JSONObject.fromObject(configMap);
        modelAndView.addObject("config",configJson);

        modelAndView.setViewName("access/index");
        return modelAndView;
    }

    /**
     * 保存准入服务器配置信息，完成后刷新准入服务器状态
     * @param accessConfig
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveConfig", produces = "application/json", method = RequestMethod.POST)
    public ResultMsg saveConfig(AccessConfig accessConfig, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            if (accessConfig != null) {
                saveAccessRunnable.setAccessConfig(accessConfig);
                taskExecutor.execute(saveAccessRunnable);
                resultMsg.setResultMsg("保存配置信息成功");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("保存配置信息失败，配置信息不可以为空");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("保存配置信息错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        //自定义记录日志
        try {
            OperationLogDO log = new OperationLogDO();
            log.setIp(NetworkUtil.getIpAddress(request));
            UserDO currentUser = GetLoginUser.getLoginUser();
            String res = StringUtil.getResultStrByResultCode(resultMsg.getResultCode());
            String congurationLog = "";
            if (accessConfig != null) {
                congurationLog = accessConfig.generateNacCongurationLog();
            }
            String detail = String.format("【%s】在【准入】中执行【保存】操作" + res + "：【账户名：%s，准入服务器配置成功，配置参数为：%s】", currentUser.getName(),currentUser.getUserName(), congurationLog);
            log.setLogOperateParam(detail);
            log.setLogPage("准入");
            log.setTime(new Date());
            log.setUserName(currentUser.getUserName());
            log.setLogType(LogType.OTHER.value());
            log.setLogDesc(String.format("AccessController.saveConfig(..) invoke"));
            logService.create(log);
        } catch (Exception e) {
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/readAccessServerConfig", method = RequestMethod.GET)
    public ResultMsg readAccessServerConfig() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //读取配置信息
            Map<String, Object> configMap = new HashMap<>();
            accessService.readAccessServerConfig(configMap);

            resultMsg.setData(configMap);
            resultMsg.setResultMsg("读取配置信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("读取配置信息错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 设置动态IP http的接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/applyClientAccess", method = RequestMethod.POST)
    public Map<String, Object> applyClientAccess(HttpServletRequest request) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //获取ip地址
            String ip = NetworkUtil.getIpAddress(request);
            List<String> ips = new ArrayList<>();
            ips.add(ip);
            resultMsg.put("ip", ip);

            //获取配置文件时间
            String globalcfgtime = CommonFileUtil.getGlobalcfgtime();
            resultMsg.put("globalcfgtime", globalcfgtime);

            //设置动态ip
            boolean flag = accessService.setDynamicRules(ips, null);

            if (flag) {
                resultMsg.put("result", ConstantsDto.RESULT_CODE_TRUE);
                resultMsg.put("errmsg", "success");
            } else {
                resultMsg.put("result", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("errmsg", "动态IP设置错误");
            }
        } catch (Exception e) {
            resultMsg.put("errmsg", e);
            resultMsg.put("result", ConstantsDto.RESULT_CODE_FALSE);
        }

        return resultMsg;
    }

    /**
     * 移除动态IP http的接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/removeClientAccess", method = RequestMethod.POST)
    public Map<String, Object> removeClientAccess(HttpServletRequest request, @RequestBody JSONObject param) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            String ip;
            //获取ip地址
            if (!StringUtil.isEmpty((String) param.get("ip"))) {
                ip = (String) param.get("ip");
            } else {
                ip = NetworkUtil.getIpAddress(request);
            }
            List<String> ips = new ArrayList<>();
            ips.add(ip);

            //移除动态ip
            boolean flag = accessService.setDynamicRules( null, ips);

            if (flag) {
                resultMsg.put("result", ConstantsDto.RESULT_CODE_TRUE);
                resultMsg.put("errmsg", "success");
            } else {
                resultMsg.put("result", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("errmsg", "移除IP设置错误");
            }
        } catch (Exception e) {
            resultMsg.put("errmsg", e);
            resultMsg.put("result", ConstantsDto.RESULT_CODE_FALSE);
        }

        return resultMsg;
    }

    /**
     * 检查当前准入的动态IP设置
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkClientAccessList", method = RequestMethod.GET)
    public Map<String, Object> checkClientAccessList() {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //获取动态IP设置
            List<String> ips = accessService.getDynamicRules();

            resultMsg.put("ips", ips);
            resultMsg.put("result", ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.put("errmsg", "success");
        } catch (Exception e) {
            resultMsg.put("errmsg", e);
            resultMsg.put("result", ConstantsDto.RESULT_CODE_FALSE);
        }
        return resultMsg;
    }
}