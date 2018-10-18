/**
 *
 */
package cn.goldencis.tdp.core.utils;

import java.util.List;

import javax.annotation.PostConstruct;

import cn.goldencis.tdp.core.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.service.IUserService;

/**
 * 获取当前登录用户信息
 * @author Administrator
 *
 */
@Component
public class GetLoginUser {
    @Autowired
    private IUserService userService;
    private static GetLoginUser getLoginUser;

    public void setUserInfo(IUserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        getLoginUser = this;
        getLoginUser.userService = this.userService;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public static UserDO getLoginUser() {
        String userName = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            userName = ((User) principal).getUsername();
        }
        UserDO user = getLoginUser.userService.getLoginUser(userName);
        return user;
    }

    /**
     * 关联用户部门表
     * 获取用户对应的部门权限
     * @return
     */
    public static List<DepartmentDO> getDepartmentListWithLoginUser() {

        String userName = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            userName = ((User) principal).getUsername();
        }
        List<DepartmentDO> roleDept = getLoginUser.userService.getLoginDepartMent(userName);
        return roleDept;

    }

    /**
     * 获取当前用户角色所管部门信息
     * @author mll
     * @return List<DepartmentDO>
     */
    public static List<DepartmentDO> getDepartmentByUser() {
        UserDO user = GetLoginUser.getLoginUser();
        return getLoginUser.userService.getUserRoleDepartmentByUserOptimised(user.getGuid());
    }

}
