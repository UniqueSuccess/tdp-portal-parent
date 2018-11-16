package cn.goldencis.tdp.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.goldencis.tdp.core.dao.*;
import cn.goldencis.tdp.core.entity.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.service.INavigationService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

/**
 * 菜单及顶部service实现类
 *
 * @author Administrator
 */
@Component("navigationService")
public class NavigationServiceImpl extends AbstractBaseServiceImpl<NavigationDO, NavigationDOCriteria> implements INavigationService, ServletContextAware {

    @Autowired
    private NavigationDOMapper mapper;

    @Autowired
    private PermissionNavigationDOMapper permissionNavigationDOMapper;

    @Autowired
    private CUserNavigationDOMapper cUserNavigationDOMapper;

    @Value("${customized.filePath}")
    private String customizedPath;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BaseDao<NavigationDO, NavigationDOCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    private void init() throws IOException {
        String jsonFilePath = servletContext.getRealPath(customizedPath);
        File customizedFile = new File(jsonFilePath);
        String content = FileUtils.readFileToString(customizedFile, "UTF-8");
        JSONObject customizedjson = JSONObject.parseObject(content);

        //读取定制化模块设定
        JSONArray modules = customizedjson.getJSONArray("modules");
        servletContext.setAttribute("modules", modules);
        //读取定制化页面设定
        JSONArray navigations = customizedjson.getJSONArray("navigations");
        servletContext.setAttribute("navigations", navigations);
        //读取定制化功能替换设定
        JSONArray replace = customizedjson.getJSONArray("replace");
        servletContext.setAttribute("replace", replace);
    }

    /**
     * 获取用户权限集合
     *
     * @param user
     * @return
     */
    public List<NavigationDO> getUserNavigation(UserDO user) {

        NavigationDOCriteria nexample = new NavigationDOCriteria();

        //如果是超级管理员，则获取全部权限
        if ("1".equals(user.getGuid())) {
            nexample.setOrderByClause("compositor asc");
            return mapper.selectByExample(nexample);
        }

        List<Integer> ids = cUserNavigationDOMapper.getNavigationListByUser(user.getGuid());

        //根据id列表查询对应权限列表
        NavigationDOCriteria.Criteria ncriteria = nexample.createCriteria();
        ncriteria.andIdIn(ids);
        nexample.setOrderByClause("compositor asc");
        List<NavigationDO> rList = mapper.selectByExample(nexample);
        return rList;
    }

    /*
     * (non-Javadoc)
     * @see cn.goldencis.tsa.system.service.INavigationService#getNavigation()
     */
    public String getNavigation() {
        NavigationDOCriteria nexample = new NavigationDOCriteria();
        nexample.setOrderByClause("compositor asc");
        List<NavigationDO> list = mapper.selectByExample(nexample);
        String nNodes = toTreeJson(list);
        return nNodes;
    }

    /**
     * 通过账户的角色类型，查询对应的页面集合
     *
     * @param roleType
     * @return
     */
    @Override
    public JSONArray getNavigationListByRoleType(Integer roleType) {
        //获取权限页面集合
        PermissionNavigationDOCriteria example = new PermissionNavigationDOCriteria();
        example.createCriteria().andPermissionIdEqualTo(roleType);
        List<PermissionNavigationDO> permissionNavigationList = permissionNavigationDOMapper.selectByExample(example);

        //转化为页面id集合
        List<Integer> navigationIdList = new ArrayList<>();
        for (PermissionNavigationDO permissionNavigation : permissionNavigationList) {
            navigationIdList.add(permissionNavigation.getNavigationId());
        }

        //获取全部页面集合
        NavigationDOCriteria navigationExample = new NavigationDOCriteria();
        List<NavigationDO> navigationList = mapper.selectByExample(navigationExample);

        //组装成需要返回的Json数组
        return toJson(navigationList, navigationIdList);

    }

    /**
     * 通过账户的Guid，查询对应的页面集合
     *
     * @param guid
     * @return
     */
    @Override
    public JSONArray getNavigationListByGuid(String guid) {
        //通过账户的Guid，查询对应的页面id集合
        List<Integer> navigationIdList = cUserNavigationDOMapper.getNavigationListByUser(guid);

        //获取全部页面集合
        NavigationDOCriteria navigationExample = new NavigationDOCriteria();
        List<NavigationDO> navigationList = mapper.selectByExample(navigationExample);

        //组装成需要返回的Json数组
        return toJson(navigationList, navigationIdList);
    }

    /**
     * @param navigations      全部权限集合
     * @param navigationIdList 有该页面权限，回显时打钩的，checked:true的。
     * @return
     */
    private JSONArray toJson(List<NavigationDO> navigations, List<Integer> navigationIdList) {
        JSONArray NavigationArray = new JSONArray();
        for (NavigationDO navigation : navigations) {
            JSONObject naviJson = new JSONObject();
            naviJson.put("id", navigation.getId());
            naviJson.put("pId", navigation.getParentId());
            naviJson.put("name", navigation.getTitle());
            naviJson.put("ParentNavigationId", navigation.getParentId());
            naviJson.put("level", navigation.getnLevel() - 1);
            if (navigationIdList.contains(navigation.getId())) {
                naviJson.put("checked", true);
            }
            NavigationArray.add(naviJson);
        }
        return NavigationArray;
    }

    /**
     * 转化成ztree json
     *
     * @param navigations
     * @return
     */
    private String toTreeJson(List<NavigationDO> navigations) {
        JSONArray array = new JSONArray();
        for (NavigationDO navigation : navigations) {
            JSONObject obj = new JSONObject();
            obj.put("id", navigation.getId());
            obj.put("pId", navigation.getParentId());
            obj.put("name", navigation.getTitle());
            obj.put("iconSkin", "tNavigation");
            obj.put("ParentNavigationId", navigation.getParentId());
            obj.put("open", true);
            array.add(obj);
        }
        return array.toJSONString();
    }

    /**
     * 将关于权限加到权限集合中
     *
     * @param list
     */
    public void addAboutNavigationInList(List<NavigationDO> list) {
        NavigationDO navigationDO = new NavigationDO();
        navigationDO.setIconUrl("nav_about");
        navigationDO.setTitle("关于");
        navigationDO.setCompositor(7);
        navigationDO.setUrl("/about/index?navId=1");
        navigationDO.setnLevel(1);
        navigationDO.setId(8);
        list.add(navigationDO);
    }

}
