package cn.goldencis.tdp.policy.controller;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.NavigationDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.service.INavigationService;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.service.IPolicyService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 左侧菜单及头部
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/system/navigation")
public class NavigationController implements ServletContextAware {

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private IPolicyService policyService;

    private ServletContext servletContext;

    /**
     * 左侧菜单
     *
     * @return
     */
    @RequestMapping(value = "/usernavigation", method = RequestMethod.GET)
    public ModelAndView usernavigation(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        UserDO user = GetLoginUser.getLoginUser();

        //获取当前账户导航栏权限
        Map<String, Object> authInfo = AuthUtils.getAuthInfo(servletContext);
        List<NavigationDO> list = new ArrayList<>();

        if (!"".equals(authInfo.get("authmsg"))) {
            //只允许system用户显示页面
            if (ConstantsDto.ADMIN_ID.equals(user.getId())) {
                //如果没有授权 或者 过期 只让system登录关于页面
                navigationService.addAboutNavigationInList(list);
                model.addObject("authmsg", authInfo.get("authmsg"));
            }
        } else {
            list = navigationService.getUserNavigation(user);
        }

        //获取策略集合
        List<PolicyDO> allPolicyList = policyService.getAllPolicyList();

        model.addObject("navigationlist", list);
        model.addObject("policyList", net.sf.json.JSONArray.fromObject(allPolicyList));
        model.setViewName("decorators/navigation");
        return model;
    }

    /**
     * 左侧菜单栏数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserNavigation", produces = "application/json", method = RequestMethod.GET)
    public ResultMsg getUserNavigation(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();
        UserDO user = GetLoginUser.getLoginUser();

        String path = request.getContextPath();
        //获取当前账户导航栏权限
        Map<String, Object> authInfo = AuthUtils.getAuthInfo(servletContext);
        List<NavigationDO> list = new ArrayList<>();

        if (!"".equals(authInfo.get("authmsg"))) {
            //只允许system用户显示页面
            if (ConstantsDto.ADMIN_ID.equals(user.getId())) {
                //如果没有授权 或者 过期 只让system登录关于页面
                navigationService.addAboutNavigationInList(list);
//                model.addObject("authmsg", authInfo.get("authmsg"));
            }
        } else {
            list = navigationService.getUserNavigation(user);
        }

        ArrayList<Map<String, Object>> newnavList = new ArrayList<>();


        this.covertNavigationBean(list,newnavList,path);
        resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        resultMsg.setData(newnavList);
        return resultMsg;
    }

    private void covertNavigationBean(List<NavigationDO> list, List<Map<String, Object>> newnavList,String path) {
        ArrayList<Map<String, Object>> navList = new ArrayList<>();
        ArrayList<Map<String, Object>> subnavList = new ArrayList<>();
        for (NavigationDO l : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", l.getTitle());
            jsonObject.put("icon", l.getIconUrl());
            if ("".equals(l.getUrl())) {
                jsonObject.put("href", l.getUrl());
            } else {
                jsonObject.put("href", path + l.getUrl());
            }
            jsonObject.put("id", l.getId());
            if (l.getnLevel() == 1) {
                navList.add(jsonObject);
            } else {
                jsonObject.put("parentid", l.getParentId());
                subnavList.add(jsonObject);
            }

        }
        for (Map<String, Object> nav : navList) {
            ArrayList<Map<String, Object>> objects = new ArrayList<>();
            for (Map<String, Object> sub : subnavList) {
                if (sub.get("parentid") == nav.get("id")) {
                    objects.add(sub);
                }
            }
            if ("".equals(nav.get("href")) && !"策略".equals(nav.get("name"))) {
                nav.put("sub", objects);
            }
            newnavList.add(nav);
        }

    }
    /**
     * 系统头部的获取
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ModelAndView getDecoratorTop() {
        ModelAndView model = new ModelAndView();
        model.setViewName("decorators/top");
        model.addObject("userId", GetLoginUser.getLoginUser().getId());
        // model.addObject("unreadalarm",alarmLogsService.countUnread(user.getUserName()));
        return model;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 查询全部页面集合，回显账户页面权限。
     * 1.如果有guid，是编辑的情况，通过账户的Guid，查询账户拥有的权限，使这些权限checked：true
     * 2.如果guid不存在。为新建账户的情况通过账户的角色类型，通过账户的角色类型，查询角色类型拥有的权限，使这些权限checked：true
     * 如果有该页面的权限，则checked：true
     *
     * @param roleType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNavigationListByRoleType")
    public ResultMsg getNavigationListByRoleType(Integer roleType, String guid) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            JSONArray navigationList;

            if (StringUtil.isEmpty(guid)) {
                //guid不存在。为新建账户的情况，通过roleType，查询权限
                if (!(roleType > 0 && roleType < 4)) {
                    resultMsg.setResultMsg("角色类型不正确！");
                    resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                    return resultMsg;
                }
                //通过账户的角色类型，查询对应的页面集合
                navigationList = navigationService.getNavigationListByRoleType(roleType);
            } else {
                //通过账户的Guid，查询对应的页面集合
                navigationList = navigationService.getNavigationListByGuid(guid);
            }


            resultMsg.setData(navigationList);
            resultMsg.setResultMsg("页面权限列表获取成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("页面权限列表获取错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }
        return resultMsg;
    }
}