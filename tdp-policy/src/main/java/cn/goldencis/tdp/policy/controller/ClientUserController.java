package cn.goldencis.tdp.policy.controller;

import cn.goldencis.tdp.common.cache.manager.GuavaCacheManager;
import cn.goldencis.tdp.common.utils.*;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IDepartmentService;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.entity.UsbKeyDO;
import cn.goldencis.tdp.policy.service.IClientUserService;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.policy.service.IFingerprintService;
import cn.goldencis.tdp.policy.service.IUsbKeyService;
import cn.goldencis.tdp.policy.service.impl.PolicyPublishImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by limingchao on 2017/12/22.
 */
@Controller
@PageLog(module = "用户管理")
@RequestMapping(value = "/clientUser")
public class ClientUserController implements ServletContextAware {

    @Autowired
    private IClientUserService clientUserService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private PolicyPublishImpl policyPublish;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IFingerprintService fingerprintService;

    @Autowired
    private IUsbKeyService usbKeyService;

    @Autowired
    private IOperationLogService logService;

    @Autowired
    private GuavaCacheManager cacheManager;
    

    private static final String FILE_NAME = "用户信息";

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        Map<String, Object> authInfo = AuthUtils.getAuthInfo(servletContext);
        servletContext.setAttribute("maxCustomerCnt", authInfo.get("maxCustomerCnt") == null ? "0" : authInfo.get("maxCustomerCnt").toString());
        this.servletContext = servletContext;
    }

    /**
     * 用户管理主页面
     *
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/user/clientUser/index");

        //根据当前用户查询部门信息，放到模型中返回前端
        JSONArray zNodes = departmentService.getNodesByLogin();
        model.addObject("zNodes", zNodes);

        return model;
    }

    /**
     * 请求指定部门下的用户列表
     *
     * @param pid       指定部门id
     * @param start
     * @param length
     * @param ordercase 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getClientUserPages")
    public ResultMsg getClientUserPages(HttpServletRequest request) {
        //获取入参
        Map<String, Object> params = HttpServletRequestUtils.getRequestParams(request);
        params = HttpServletRequestUtils.replaceStr2List(params, "state", "department", "strategy");
        ResultMsg resultMsg = new ResultMsg();
        try {
            //根据部门参数不同查询部门集合，当部门id为顶级部门时，需要判断账户部门权限，
            List<DepartmentDO> departmentList = null;
            //重新查询账户的关联部门，查询其部门权限集合，如果包含顶级部门，则查询查询全部部门，否则查询该账户部门权限下的部门
            if (params.get("department") == null) {
                departmentList = GetLoginUser.getDepartmentListWithLoginUser();
                params.put("department", formatDepartmentDO(departmentList));
            }

            int count = clientUserService.conutClientUserListByDepartmentId(params);
            List<ClientUserDO> clientUsers = clientUserService.getClientUserListByDepartmentId(params);
            resultMsg.setRows(clientUsers);
            resultMsg.setTotal(count);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);

        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setResultMsg("查询用户出错！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    private List<Integer> formatDepartmentDO(List<DepartmentDO> departmentList) {
        List<Integer> list = new ArrayList<Integer>();
        for (DepartmentDO dto : departmentList) {
            list.add(dto.getId());
        }
        return list;
    }

    /**
     * 添加用户接口
     *
     * @param clientUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addClientUser", method = RequestMethod.POST)
    public ResultMsg addClientUser(@RequestBody net.sf.json.JSONObject jsonObject, HttpServletRequest request) {

        ResultMsg resultMsg = new ResultMsg();
        try {
            ClientUserDO clientUser = (ClientUserDO)net.sf.json.JSONObject.toBean(jsonObject, ClientUserDO.class);
            if (StringUtil.isEmpty(clientUser.getComputerguid()) && StringUtil.isEmpty(clientUser.getGuid())) {
                resultMsg.setResultMsg("缺失字段computerguid和guid");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }
            clientUser.setIp(NetworkUtil.getIpAddress(SysContext.getRequest()));
            //插入用户
            Map<String, Object> result = clientUserService.addClientUser(clientUser);
            if (result.containsKey("resultCode")) {
                resultMsg.setResultCode(Integer.valueOf(result.get("resultCode").toString()));
                if (result.containsKey("resultMsg")) {
                    resultMsg.setResultMsg(String.valueOf(result.get("resultMsg")));
                }
                return resultMsg;
            }
            result.put("ip", clientUser.getIp());
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.setData(result);
            resultMsg.setResultMsg("注册成功！");
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setResultMsg("注册失败");
        }
        return resultMsg;
    }

    /**
     * 更新用户接口
     *
     * @param clientUser
     * @return
     */
    @PageLog(module = "更新用户信息", template = "计算机guid：%s", args = "0.computerguid", type = LogType.UPDATE)
    @ResponseBody
    @RequestMapping(value = "/updateClientUser", method = RequestMethod.POST)
    public ResultMsg updateClientUser( HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();
        try{
            Map<String, Object> params = HttpServletRequestUtils.getRequestParams(request);
            params = HttpServletRequestUtils.replaceStr2List(params, "ids");
            clientUserService.updateClientUser(params);
            cacheManager.getCache("heartInfo").clear();
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        }catch (Exception e) {
            e.printStackTrace();
            resultMsg.setResultMsg("用户更新失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }
        return resultMsg;
    }

    /**
     * 删除用户接口
     *
     * @param id
     * @return
     */
    @PageLog(module = "删除用户", template = "用户id为：%s", args = "0", type = LogType.DELETE)
    @ResponseBody
    @RequestMapping(value = "/deleteClientUser", method = RequestMethod.POST)
    public ResultMsg deleteClientUser(Integer id, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            clientUserService.deleteClientUserById(id);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.setResultMsg("删除成功！");
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setResultMsg("删除错误！");
        }
        return resultMsg;
    }

    /**
     * 客户端用户登录接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clientUserLogin", method = RequestMethod.POST)
    public Map<String, Object> clientUserLogin(@RequestBody JSONObject clientUserLogin, HttpServletRequest request) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //记录登录日志
            OperationLogDO log = new OperationLogDO();

            //设置ip地址
            String ip = NetworkUtil.getIpAddress(SysContext.getRequest());
            log.setIp(ip);
            log.setLogPage("客户端登录");
            log.setTime(new Date());
            log.setLogType(LogType.CLIENT_LOGIN.value());
            log.setLogDesc(String.format("ClientUserController.clientUserLogin(..) invoke"));

            ClientUserDO clientUser = null;
            Integer logType = (Integer) clientUserLogin.get("logontype");
            if (logType.intValue() == ConstantsDto.CLIENT_LOGON_TYPE_PWD.intValue()) {
                log.setUserName((String) clientUserLogin.get("username"));
                //根据登录传入的用户名，查询数据库中的用户信息
                clientUser = clientUserService.getClientUserByName((String) clientUserLogin.get("username"));

                //用户为空不存在和校验密码
                if (clientUser == null || !clientUser.getPassword().equals(clientUserLogin.get("password"))) {
                    String detail = String.format("【用户名：%s】在【客户端登录】中执行【登录】操作失败：用户名或密码错误！", (String) clientUserLogin.get("username"));
                    log.setLogOperateParam(detail);
                    logService.create(log);
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "用户名或密码错误！");
                    return resultMsg;
                }

            } else if (logType.intValue() == ConstantsDto.CLIENT_LOGON_TYPE_USBKEY.intValue()) {
                //获取usbkey
                String keynum = clientUserLogin.getString("unqiueId");
                UsbKeyDO usbKey = usbKeyService.getUsbKeyByNum(keynum);

                //判断该UsbKey是否录入
                if (usbKey == null) {
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "该UKey未录入系统！");
                    return resultMsg;
                }

                //判断usbkey是否绑定用户
                if (StringUtils.isEmpty(usbKey.getUserguid())) {
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "该UKey未绑定用户！");
                    return resultMsg;
                }

                //获取用户信息
                clientUser = clientUserService.getClientUserByGuid(usbKey.getUserguid());

                //判断绑定用户是否存在
                if (clientUser == null) {
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "该UKey绑定用户不正确!");
                    return resultMsg;
                }

                log.setUserName(clientUser.getUsername());

                //校验usbkey的双因子认证的开关是否开启
                if (usbKeyService.getPwdNeeded()) {
                    //用户为空不存在和校验密码
                    if (!clientUser.getPassword().equals(clientUserLogin.get("password"))) {
                        String detail = String.format("【用户名：%s】在【客户端登录】中执行【登录】操作失败：用户名或密码错误！", clientUser.getUsername());
                        log.setLogOperateParam(detail);
                        logService.create(log);
                        resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                        resultMsg.put("resultmsg", "用户名或密码错误！");
                        return resultMsg;
                    }
                }
            } else if (logType.intValue() == ConstantsDto.CLIENT_LOGON_TYPE_FINGERPRINT.intValue()) {

                //判断定制化模板中替换选项内，是否包含指纹选项
                JSONArray replace = (JSONArray) request.getServletContext().getAttribute("replace");
                if (!replace.contains("fingerprint")) {
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "此系统为开放指纹功能！");
                    return resultMsg;
                }

                //获取登录信息中的指纹信息
                String fptemp = (String) clientUserLogin.get("fptemp");
                //进行指纹比对，比对成功，则正常执行,同时将指纹对应用户guid，赋值给入参的变量。
                String guid = fingerprintService.verifyFingerprint(fptemp);
                if (StringUtils.isEmpty(guid)) {
                    //指纹比对失败，返回提示。
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "指纹比对失败，请确认录入该指纹！");
                    return resultMsg;
                }

                if ((clientUser = clientUserService.getClientUserByGuid(guid)) != null) {
                    log.setUserName(clientUser.getUsername());
                } else {
                    //指纹比对失败，返回提示。
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "指纹比对失败，不存在对应用户信息！");
                    return resultMsg;
                }

            } else {
                resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("resultmsg", "客户端登录类型不匹配！");
                return resultMsg;
            }

            //校验单一用户登录，online字段为0直接登录，
            if ("1".equals(clientUser.getOnline())) {
                //为1时，比对computerguid和mac，任何一个匹配就可以登录
                if (!(clientUser.getComputerguid().equals(clientUserLogin.get("pcguid")) || clientUser.getMac().equals(clientUserLogin.get("mac")))) {
                    String detail = String.format("【用户名：%s】在【客户端登录】中执行【登录】操作失败：该用户已登录！", clientUser.getUsername());
                    log.setLogOperateParam(detail);
                    logService.create(log);
                    resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.put("resultmsg", "该用户已登录！");
                    return resultMsg;
                }
            }

            if (clientUserLogin.get("computername") != null) {
                clientUser.setComputername((String) clientUserLogin.get("computername"));
            }

            if (clientUserLogin.get("pcguid") != null) {
                clientUser.setComputerguid((String) clientUserLogin.get("pcguid"));
            }

            if (clientUserLogin.get("mac") != null) {
                clientUser.setMac((String) clientUserLogin.get("mac"));
            }

            //提取用户信息。组装返回的数据信息
            clientUserService.loadClientUserInfo(clientUser, resultMsg);

            //添加日志
            String detail = String.format("【%s】在【客户端登录】中执行【登录】操作成功：【用户名：%s】", clientUser.getTruename(), clientUser.getUsername());
            log.setLogOperateParam(detail);
            logService.create(log);

            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.put("resultmsg", "用户登录成功！");
        } catch (Exception e) {
            resultMsg.put("error", e);
            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.put("resultmsg", "用户登录错误！");
        }
        return resultMsg;
    }

    /**
     * 客户端登出接口
     *
     * @param userguid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clientUserLogout", method = RequestMethod.POST)
    public Map<String, Object> clientUserLogout(@RequestBody JSONObject userguid) {
        Map<String, Object> resultMsg = new HashMap<>();
        resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
        try {
            if (userguid == null || userguid.get("userguid") == null) {
                resultMsg.put("resultmsg", "缺少userguid");
                return resultMsg;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("guid", String.valueOf(userguid.get("userguid")));
            params.put("online", "1");
            params.put("onlineTime", DateUtil.getCurrentDate(DateUtil.DateTimeFormat));
            clientUserService.modifyClientUserStatus(params);
            //登出
            
            //记录登录日志
            //OperationLogDO log = new OperationLogDO();
            /*//设置ip地址
            String ip = NetworkUtil.getIpAddress(SysContext.getRequest());
            log.setIp(ip);
            log.setLogPage("客户端登出");
            log.setTime(new Date());
            log.setUserName(clientUser.getUsername());
            log.setLogType(LogType.CLIENT_LOGIN.value());
            log.setLogDesc(String.format("ClientUserController.clientUserLogout(..) invoke"));
            String detail = String.format("【%s】在【客户端登出】中执行【登出】操作成功：【用户名：%s】", clientUser.getTruename(), clientUser.getUsername());
            log.setLogOperateParam(detail);
            logService.create(log);*/

            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.put("resultmsg", "用户登出成功！");
        } catch (Exception e) {
            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.put("resultmsg", "用户登出错误！");
        }
        return resultMsg;
    }

    /**
     * 客户端修改密码接口
     *
     * @param clientUserInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Map<String, Object> changePassword(@RequestBody JSONObject clientUserInfo) {
        Map<String, Object> resultMsg = new HashMap<>();
        try {
            //根据传入的ID，查询数据库中的用户信息
            ClientUserDO clientUser = clientUserService.getClientUserByGuid((String) clientUserInfo.get("userguid"));

            //用户为空不存在
            if (clientUser == null) {
                resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("resultmsg", "用户名不存在！");
                return resultMsg;
            }

            if (!clientUser.getPassword().equals(clientUserInfo.get("prePassword"))) {
                resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("resultmsg", "原密码不正确！");
                return resultMsg;
            }

            //修改密码
            clientUser.setPassword((String) clientUserInfo.get("password"));
            clientUserService.updateClientUserPassword(clientUser);

            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.put("resultmsg", "修改用户密码成功！");
        } catch (Exception e) {
            resultMsg.put("error", e);
            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.put("resultmsg", "修改用户密码错误！");
        }
        return resultMsg;
    }

    /**
     * 用户信息导出接口
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @PageLog(module = "导出全部用户信息", template = "", type = LogType.OTHER)
    @RequestMapping(value = "/exportxsl", method = RequestMethod.GET)
    public void exportXslAll(HttpServletRequest request, HttpServletResponse response) {
        List<ClientUserDO> userList = clientUserService.getAllClientUser();
        clientUserService.setClientUserPolicyName(userList);
        CommonPoiXsl<ClientUserDO> poiXsl = new CommonPoiXsl<>();
        String path = "";
        try {
            String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            path = request.getServletContext().getRealPath(PathConfig.EXPORT_PATH);
            if (!(new File(path).isDirectory())) {
                new File(path).mkdirs();
            }
            path = path + "/" + FILE_NAME + key + ".xls";
            OutputStream out = new FileOutputStream(path);
            String[] headers = {"用户名", "真实姓名", "终端策略", "IP地址"};
            poiXsl.exportExcel("用户信息", headers, userList, out, "yyyy-MM-dd");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }

    /**
     * 获取全部用户总数和全部在线用户总数的接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countAllClientUser", method = RequestMethod.GET)
    public ResultMsg countAllClientUser() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            Map<String, Integer> countMap = new HashMap<>();

            //获取全部用户总数
            int allConnt = clientUserService.countAllClientUser();
            //获取全部在线用户总数
            int onlineConnt = clientUserService.countAllOnlineClientUser();

            countMap.put("allConnt", allConnt);
            countMap.put("onlineConnt", onlineConnt);
            resultMsg.setData(countMap);
            resultMsg.setResultMsg("获取用户总数成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取用户总数错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/downloadClientUserExcelTemplet")
    public void downloadClientUserExcelTemplet(HttpServletRequest request, HttpServletResponse response) {
        String templetPath = request.getServletContext().getRealPath(PathConfig.TEMPLET_PATH + "/" + ConstantsDto.TEMPLET_FILENAME_CLIENTUSER);
        File file = new File(templetPath);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("请先上传用户模板文件");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, templetPath, "导入用户信息模板.xls");
    }


    @ModelAttribute
    public void getModel(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            ClientUserDO clientUser = clientUserService.getClientUserById(id);
            map.put("clientUser", clientUser);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    public ResultMsg heartbeat(@RequestBody JSONObject json, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            if (!json.containsKey("usrunique")) {
                resultMsg.setResultMsg("缺少必要参数");
                return resultMsg;
            }
            //clientUserService.updateHeartbeat(json.getString("usrunique"));
            Map<String, Object> into = clientUserService.queryDepartmentByUnique(json.getString("usrunique"));
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            resultMsg.setData(into);
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setResultMsg("心跳接口异常");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

}
