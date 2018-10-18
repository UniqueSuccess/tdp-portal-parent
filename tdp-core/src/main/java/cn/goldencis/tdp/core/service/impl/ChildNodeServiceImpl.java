package cn.goldencis.tdp.core.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.dao.ChildNodeDOMapper;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ChildNodeDOCriteria;
import cn.goldencis.tdp.core.service.IChildNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by limingchao on 2018/7/19.
 */
@Service
public class ChildNodeServiceImpl extends AbstractBaseServiceImpl<ChildNodeDO, ChildNodeDOCriteria> implements IChildNodeService {

    @Autowired
    private ChildNodeDOMapper mapper;

    @Override
    protected BaseDao<ChildNodeDO, ChildNodeDOCriteria> getDao() {
        return mapper;
    }


}
