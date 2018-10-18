package cn.goldencis.tdp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.dao.PermissionNavigationDOMapper;
import cn.goldencis.tdp.core.entity.PermissionNavigationDO;
import cn.goldencis.tdp.core.entity.PermissionNavigationDOCriteria;
import cn.goldencis.tdp.core.service.IPermissionNavigationService;

/**
 * 角色对应权限service实现类
 * @author Administrator
 *
 */
@Component("permissionNavigationService")
public class PermissionNavigationServiceImpl extends
        AbstractBaseServiceImpl<PermissionNavigationDO, PermissionNavigationDOCriteria> implements
        IPermissionNavigationService {

    @Autowired
    private PermissionNavigationDOMapper mapper;

    @SuppressWarnings("unchecked")
    @Override
    protected BaseDao<PermissionNavigationDO, PermissionNavigationDOCriteria> getDao() {
        return mapper;
    }

}
