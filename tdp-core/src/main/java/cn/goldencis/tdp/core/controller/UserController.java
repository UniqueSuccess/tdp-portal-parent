package cn.goldencis.tdp.core.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.service.*;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;

import cn.goldencis.tdp.common.cache.manager.GuavaCacheManager;
import cn.goldencis.tdp.common.utils.CommonPoiXsl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.common.utils.HttpServletRequestUtils;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.JsonUtil;
import cn.goldencis.tdp.core.utils.PathConfig;

/**
 * 管理员管理controller
 *
 * @author Administrator
 */
@Controller
@PageLog(module = "系统设置-账户")
@RequestMapping("/systemSetting/user")
public class UserController implements ServletContextAware {

    @Autowired
    private IUserService userService;

    @Autowired
    private GuavaCacheManager cacheManager;

    private static final String FILE_NAME = "账户信息";

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 根据登录用户的角色类型，获取相应类型的账户列表。分页查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserPages")
    public ResultMsg getUserPages(HttpServletRequest request) {
        //获取入参
        Map<String, Object> params = HttpServletRequestUtils.getRequestParams(request);
        params = HttpServletRequestUtils.replaceStr2List(params, "roleType", "readonly");
        ResultMsg resultMsg = new ResultMsg();
        try {
            //根据登录用户的角色类型，获取相应类型的账户列表。分页查询
            List<UserDO> userList = userService.getUserListByLoginUserRoleTypeInPages(params);
            int total = userService.countUserListByLoginUserRoleTypeInPages(params);

            resultMsg.setRows(userList);
            resultMsg.setTotal(total);
            resultMsg.setResultMsg("用户列表获取成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("用户列表获取错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }
        return resultMsg;
    }

    /**
     * 新建和更新接口
     * @param user
     * @param departmentListStr
     * @param navigationListStr
     * @return
     */
    @ResponseBody
    @PageLog(module = "新建或更新账户", template = "账户名：%s", args = "0.userName", type = LogType.INSERT)
    @RequestMapping(value = "/addOrUpdateUser", method = RequestMethod.POST)
    public ResultMsg addOrUpdateUser(UserDO user, String departmentListStr, String navigationListStr) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            //检查账户名是否重复
            boolean flag = userService.checkUserNameDuplicate(user);

            if (!flag) {
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.setResultMsg("账户名重复！");
                return resultMsg;
            }

            if (StringUtil.isEmpty(user.getPassword())) {
                user.setPassword(null);
            }
            userService.addOrUpdateUser(user, departmentListStr, navigationListStr);

            resultMsg.setResultMsg("账户更新成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("账户更新错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 删除账户接口
     * @param userId
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除账户", template = "账户id为：%s", args = "0", type = LogType.DELETE)
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public ResultMsg deleteUser(Integer userId) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            UserDO user = userService.getUserByUserId(userId);
            if (user == null) {
                resultMsg.setResultMsg("不存在此账户！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            } else {
                userService.deleteUser(user);
                resultMsg.setResultMsg("账户删除成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            }
        } catch (Exception e) {
            resultMsg.setResultMsg("账户删除错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 根据当前登录账户的角色类型，获取同角色类型的所有账户并返回。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserListByLoginUserRoleType")
    public String getUserListByLoginUserRoleType() {

        UserDO user = GetLoginUser.getLoginUser();
        List<UserDO> userList;

        if ("1".equals(user.getGuid())) {
            userList = userService.getAllUser();
        } else {
            Integer roleType = user.getRoleType();
            userList = userService.getUserListByLoginUserRoleType(roleType);
        }


        String userListStr = JsonUtil.getObjectToString(userList);
        return userListStr;
    }

    /**
     * 账户信息导出接口
     * @param request
     * @param response
     */
    @ResponseBody
    @PageLog(module = "导出全部账户信息", template = "", args = "0", type = LogType.OTHER)
    @RequestMapping(value = "/exportxsl", method = RequestMethod.GET)
    public void exportXslAll(HttpServletRequest request, HttpServletResponse response) {
        List<UserDO> userList = userService.getAllUser();
        CommonPoiXsl<UserDO> poiXsl = new CommonPoiXsl<UserDO>();
        String path = "";
        try {
            String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            path = request.getServletContext().getRealPath(PathConfig.EXPORT_PATH);
            if (!(new File(path).isDirectory())) {
                new File(path).mkdirs();
            }
            path = path + "/" + FILE_NAME + key + ".xls";
            OutputStream out = new FileOutputStream(path);
            String[] headers = {"真实姓名", "账户名称", "账户角色", "策略控制", "电话"};
            poiXsl.exportExcel("账户信息", headers, userList, out, "yyyy-MM-dd");
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

    @ResponseBody
    @RequestMapping(value = "/insertRefusePromptUser", produces = "application/json", method = RequestMethod.POST)
    public ResultMsg insertRefusePromptUser(HttpServletRequest request, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            UserDO user = GetLoginUser.getLoginUser();
            userService.insertRefusePromptUser(user.getGuid());
            resultMsg.setResultCode(ConstantsDto.CONST_TRUE);
        } catch (Exception e) {
            resultMsg.setResultCode(ConstantsDto.CONST_FALSE);
        }
        return resultMsg;
    }

    /**
     * 根据账户id获取账户对象
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    public ResultMsg getUserById(Integer userId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据账户id获取账户对象
            UserDO user = userService.getUserByUserId(userId);

            resultMsg.setData(user);
            resultMsg.setResultMsg("获取账户信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取账户信息失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 针对账户所有人，开放修改账户个人资料的接口，密码、手机。
     * @return
     */
    @ResponseBody
    @PageLog(module = "更新账户个人信息", template = "账户名：%s", args = "0.userName", type = LogType.UPDATE)
    @RequestMapping(value = "/editPersonalUserInfo", method = RequestMethod.POST)
    public ResultMsg editPersonalUserInfo(UserDO user, String prePassword) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            UserDO preUser = userService.getUserByUserId(user.getId());
            if (preUser == null) {
                resultMsg.setResultMsg("该账户不存在");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }
            if (!preUser.getPassword().equals(prePassword)) {
                resultMsg.setResultMsg("原密码不正确");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //如果修改的密码为空，则认为不对密码进行修改
            if (StringUtil.isEmpty(user.getPassword())) {
                user.setPassword(null);
            }

            //修改账户个人资料
            userService.updateUserInfo(user);
            resultMsg.setResultMsg("修改账户信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("修改账户信息失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 获取全部操作员的列表，为审批定义界面,定义审批环节时提供审批人的选择列表，包含超级管理员,同时回显已勾选账户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllOperatorList", method = RequestMethod.GET)
    public ResultMsg getAllOperatorList(Integer approveModelId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取全部操作员的列表，即roleType为2的全部
            List<UserDO> userList = userService.getAllOperatorList();

            String approvers = null;
            if (approveModelId != null) {
                //根据审批模型id，获取账户id集合
                approvers = userService.getUserIdListByApproveModelId(approveModelId);
            }

            //转化为审批界面回显审批人需要的JsonArray结构，包含guid和name，回显账户的check:true
            JSONArray userJsonArr = userService.toApproveJsonArr(userList, approvers);


            resultMsg.setData(userJsonArr);
            resultMsg.setResultMsg("获取审计人列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取审计人列表失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
