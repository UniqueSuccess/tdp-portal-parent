package cn.goldencis.tdp.core.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.NavigationDO;
import cn.goldencis.tdp.core.service.INavigationService;
import cn.goldencis.tdp.core.service.IUserService;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.GetLoginUser;

/**
 * 登录
 *
 * @author Administrator
 */
@Controller
public class LoginController implements ServletContextAware {

    private ModelAndView model = null;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private IUserService userService;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request, HttpServletResponse response) {
        if("ture".equals(error)){
            try {
                RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                redirectStrategy.sendRedirect(request, response, "/loginError");
                return null;
            } catch (IOException e) {
                 e.printStackTrace();
            }
        }
        model = new ModelAndView();
        response.setHeader("isRedirect", "yes");
        model.setViewName("../../index");
        return model;
    }

    @RequestMapping(value = "/loginError", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg loginError(HttpServletRequest request, HttpServletResponse response){
        String error = (String)request.getSession().getAttribute("LoginErrorMsg");
        return new ResultMsg(ConstantsDto.RESULT_CODE_FALSE, error, null);
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/loginsuccess", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg loginsuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultMsg result = new ResultMsg();
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        //获取当前登录用户
        UserDO user = GetLoginUser.getLoginUser();
        //获取当前登录用户对应权限的对应导航栏
        List<NavigationDO> list = navigationService.getUserNavigation(user);

//        int size = userService.getAllUser().size();

        //获取授权文件的信息
        Map<String, Object> authInfo = AuthUtils.getAuthInfo(servletContext);

        if (authInfo.get("promptMsg") != null && !"".equals(authInfo.get("promptMsg")) && userService.queryRefusePromptUser(user.getGuid()) == 0) {
            //request.getSession().setAttribute("promptMsg", authInfo.get("promptMsg"));
            Cookie cookie = new Cookie("promptMsg", URLEncoder.encode(URLEncoder.encode(authInfo.get("promptMsg").toString(), "utf-8")));
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        request.getSession().setAttribute("maxCustomerCnt", authInfo.get("maxCustomerCnt") == null ? "0" : authInfo.get("maxCustomerCnt").toString());
        request.getSession().setAttribute("endDate", "永久".equals(authInfo.get("endDate")) ? "" : authInfo.get("endDate"));
//        request.getSession().setAttribute("userNum", size);

        if (!"".equals(authInfo.get("authmsg"))) {
            request.getSession().setAttribute("unauthority", "1");
            request.getSession().setAttribute("logintime", new Date());
            request.getSession().setAttribute("authmsg", authInfo.get("authmsg"));

            /*//时间限制 客户增加用户数
            request.getSession().setAttribute("maxCustomerCnt", "0");*/

            //超期或者有其他问题则不让普通用户登录
            if (!ConstantsDto.ADMIN_ID.equals(user.getId())) {
                //redirectStrategy.sendRedirect(request, response, "/login?logout");
                result.setResultMsg("授权超期");
                return result;
            } else {
                //如果没有授权 或者 过期 只让system登录关于页面
                list = new ArrayList<>();
                navigationService.addAboutNavigationInList(list);
            }
        }

        result.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        return result;
    }

    @RequestMapping(value = "/pathDispatch", method = RequestMethod.GET)
    public void pathDispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

//获取当前登录用户
        UserDO user = GetLoginUser.getLoginUser();
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

        String url = "";
        //登录第一个url
        if (list != null && list.size() > 0) {
            for (NavigationDO nd : list) {
                if (nd.getnLevel() == 1 && !StringUtil.isEmpty(nd.getUrl())) {
                    url = nd.getUrl();
                } else if (nd.getnLevel() == 1) {
                    for (NavigationDO nd1 : list) {
                        if (!StringUtil.isEmpty(nd.getId().toString()) && Integer.valueOf(nd.getId()) == nd1.getParentId()) {
                            url = nd1.getUrl();
                            break;
                        }
                    }
                }
                if (!StringUtil.isEmpty(url)) {
                    break;
                }
            }
        }

        //空白页面
        if (StringUtil.isEmpty(url)) {
            url = "/blank/index";
        }

        redirectStrategy.sendRedirect(request, response, url);
    }


}