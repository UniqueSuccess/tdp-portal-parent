package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.common.utils.ComputerInfoUtil;
import cn.goldencis.tdp.common.utils.ListUtils;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.XmlUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.*;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.JsonUtil;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.neiwang.vdpjni.IFConfig;

import org.dom4j.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by limingchao on 2018/1/5.
 */
@Controller
@RequestMapping(value = "/systemSetting")
public class SystemController {
    private final static Log LOG = LogFactory.getLog(SystemController.class);
    @Autowired
    private IOperationLogService logService;
    @Value("${golbalcfg.path}")
    private String golbalcfgPath;

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
            /**
             * 按照那么中的数字排序
             */
            Collections.sort(datalist, new Comparator<Map<String, Object>>() {

                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    int i1 = StringUtil.getNumber(String.valueOf(o1.get("name"))).intValue();
                    int i2 = StringUtil.getNumber(String.valueOf(o2.get("name"))).intValue();
                    if (i1 > i2) {
                        return 1;
                    } else if (i1 == i2) {
                        return 0;
                    }
                    return -1;
                }
                
            });
            LOG.info(JsonUtil.getObjectToString(datalist));
            modelAndView.addObject("data", datalist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("forbidwrite", queryForbidwrite());
        modelAndView.addObject("roleTypeList", JsonUtil.getObjectToString(ConstantsDto.ROLE_LIST));
        modelAndView.addObject("userId", GetLoginUser.getLoginUser().getId());
        modelAndView.setViewName("system/setting/index");
        return modelAndView;
    }

    private Object queryForbidwrite() {
        File file = new File(PathConfig.HOM_PATH + golbalcfgPath);
        if (!file.exists()) {
            return null;
        }
        Document document = XmlUtil.getDocument(PathConfig.HOM_PATH + golbalcfgPath);
        List<String> list = XmlUtil.getAttributeValueListByTree("/cfg/sandbox/@forbidwrite", document);
        if (!ListUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
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
            System.out.println(json);
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
                LOG.error("保存网络配置异常", e);
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

    /**
     * 更新全局配置文件只读
     * @param status 1 开启 0 关闭
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateForbidwrite", method = RequestMethod.POST)
    public synchronized ResultMsg updateForbidwrite(String status) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            File file = new File(PathConfig.HOM_PATH + golbalcfgPath);
            if (!file.exists()) {
                resultMsg.setResultMsg("全局控制文件不存在");
                return resultMsg;
            }
            Document document = XmlUtil.getDocument(PathConfig.HOM_PATH + golbalcfgPath);
            XmlUtil.updateAttributeByTree("/cfg/sandbox/@forbidwrite", status, document);
            XmlUtil.writeDocumentIntoFile(document, PathConfig.HOM_PATH + golbalcfgPath);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            LOG.error("更新全局配置文件只读开关异常", e);
            resultMsg.setResultMsg("更新全局配置文件只读开关异常！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }
        resultMsg.setData(queryForbidwrite());
        return resultMsg;
    }
}
