package cn.goldencis.tdp.core.service;

import java.util.List;

import cn.goldencis.tdp.core.entity.NavigationDO;
import cn.goldencis.tdp.core.entity.UserDO;
import com.alibaba.fastjson.JSONArray;

public interface INavigationService {

    List<NavigationDO> getUserNavigation(UserDO user);

    String getNavigation();

    /**
     * 通过账户的角色类型，查询对应的页面集合
     * @param roleType
     * @return
     */
    JSONArray getNavigationListByRoleType(Integer roleType);

    /**
     * 通过账户的Guid，查询对应的页面集合
     * @param guid
     * @return
     */
    JSONArray getNavigationListByGuid(String guid);

    /**
     * 将关于权限加到权限集合中
     * @param list
     */
    void addAboutNavigationInList(List<NavigationDO> list);
}
