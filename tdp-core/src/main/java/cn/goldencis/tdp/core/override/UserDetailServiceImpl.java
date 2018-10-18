package cn.goldencis.tdp.core.override;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.dao.UserDOMapper;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.entity.UserDOCriteria;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.dao.CPermissionNavigationDOMapper;
import cn.goldencis.tdp.core.service.IUserService;

/**
 * 实现UserDetailsService
 *
 * @author 2016年7月14日 上午10:54:51
 */
@SuppressWarnings("rawtypes")
public class UserDetailServiceImpl extends AbstractBaseServiceImpl implements UserDetailsService {
    private Logger log = Logger.getLogger(UserDetailsService.class);

    @Autowired
    private CPermissionNavigationDOMapper cpermissionNavigationDOMapper;
    @Resource
    private IUserService userService;

    @Resource
    private UserDOMapper userDOMapper;

    @Autowired
    private IOperationLogService logService;
    private static final String USERPWDERROR="用户名或密码不正确";

    //@PageLog(module = "登录", template = "用户名：%s", args = "0",type= LogType.LOGIN)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //boolean isEnabled = true;
        HttpServletRequest request = SysContext.getRequest();

        UserDOCriteria userExample = new UserDOCriteria();
        userExample.createCriteria().andUserNameEqualTo(username).andStatusEqualTo(11);
        List<UserDO> userList = userDOMapper.selectByExample(userExample);
        if (userList.size() == 0) {
            request.getSession().setAttribute("LoginErrorMsg", USERPWDERROR);
            throw new UsernameNotFoundException(USERPWDERROR);
        }
        UserDO user = userList.get(0);
        //增加五次锁定逻辑判断
        //获取密码，已经在前台完成加密
        String password = request.getParameter("password");
        /*Map<String, Object> errorMap = userDOMapper.queryErrorLoginInfo(username);
        int errorCount = 0;
        if (errorMap != null) {
            errorCount = errorMap.get("error_login_count") == null ? 0 : Integer.valueOf(errorMap.get(
                    "error_login_count").toString());
        }

        if ("1".equals(request.getSession().getAttribute("FRISTLOGIN"))) {
            if (errorCount > 2 && errorCount < 5) {
                String secCode = (String) request.getSession().getAttribute(ConstantsDto.SEC_CODE);
                String newSecCode = request.getParameter(ConstantsDto.SEC_CODE_PARAMETER);
                if (!secCode.equalsIgnoreCase(newSecCode)) {
                    throw new UsernameNotFoundException("验证码错误");
                }
            }
        } else {
            request.getSession().setAttribute("FRISTLOGIN","1");
        }

        //锁定用户
        if (errorCount >= 5 && errorMap != null) {
            String lastLoginTimeStr = errorMap.get("error_login_last_time") == null ? null : errorMap.get("error_login_last_time").toString();
            Date nowDate = new Date();
            Date lastLoginTime = DateUtil.strToDate(lastLoginTimeStr, DateUtil.FMT_DATE);
            long time = nowDate.getTime() - lastLoginTime.getTime();
            if (time < 10 * 60 * 1000 || !user.getPassword().equalsIgnoreCase(password)) {
                try {
                    userService.updateErrorLoginCount(username, false);
                } catch (Exception e) {
                    throw new UsernameNotFoundException("该用户信息异常");
                }
                throw new UsernameNotFoundException("该用户已锁定,请稍后再试");
            }
        }*/

        //前台加密的密码直接与数据库中的进行比较
        if (!user.getPassword().equalsIgnoreCase(password)) {
            /*try {
                if (errorCount >= 2 && errorCount < 5) {
                    request.getSession().setAttribute(ConstantsDto.SEC_CODE_FLAG, true);
                }
                userService.updateErrorLoginCount(username, false);
            } catch (Exception e) {
                throw new UsernameNotFoundException("该用户信息异常");
            }*/
            request.getSession().setAttribute("LoginErrorMsg", USERPWDERROR);
            throw new UsernameNotFoundException(USERPWDERROR);
        }
        /*try {
            SysContext.getRequest().getSession().removeAttribute(ConstantsDto.SEC_CODE_FLAG);
            userService.updateErrorLoginCount(username, true);
        } catch (Exception e) {
            throw new UsernameNotFoundException("该用户信息异常");
        }*/

        //记录登录日志
        OperationLogDO log = new OperationLogDO();
        log.setIp(NetworkUtil.getIpAddress(request));
        String detail = String.format("【%s】在【账户登录】中执行【登录】操作成功：【账户名：%s】", user.getName(),user.getUserName());
        log.setLogOperateParam(detail);
        log.setLogPage("账户登录");
        log.setTime(new Date());
        log.setUserName(user.getUserName());
        log.setLogType(LogType.LOGIN.value());
        log.setLogDesc(String.format("UserDetailServiceImpl.loadUserByUsername(..) invoke"));
        logService.create(log);

        //为当前用户添加角色
        Collection<GrantedAuthority> auths = new ArrayList<>();
        GrantedAuthority sim = new SimpleGrantedAuthority("ROLE_USER");
        auths.add(sim);
        User ruser = new User(username, user.getPassword(), auths);
        return ruser;
    }

    @Override
    protected BaseDao getDao() {
        return this.userDOMapper;
    }
}
