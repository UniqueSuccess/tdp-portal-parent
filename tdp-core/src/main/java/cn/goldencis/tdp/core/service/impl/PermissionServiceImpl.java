package cn.goldencis.tdp.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.*;
import cn.goldencis.tdp.core.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.service.IPermissionService;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionDOMapper permissionDOMapper;

    @Autowired
    private PermissionNavigationDOMapper permissionNavigationDOMapper;

    @Autowired
    private CPermissionNavigationDOMapper cpermissionNavigationDOMapper;

    @Autowired
    private CPermissionDOMapper cpermissionDOMapper;

    @Autowired
    private NavigationDOMapper navigationDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;

    @Override
    public Integer countPermission(String searchstr) {
        return cpermissionDOMapper.countPermission(searchstr);
    }

    @Transactional
    @Override
    public boolean addOrUpdatePermission(PermissionDO record) {
        if (StringUtil.isEmpty(record.getId())) {
            permissionDOMapper.insertSelective(record);
            record.setId(record.getIntId().toString());
            batchInsert(record.getNav(), record);
        } else {
            permissionDOMapper.updateByPrimaryKey(record);
            List<PermissionNavigationDO> list = new ArrayList<PermissionNavigationDO>();
            PermissionNavigationDO temp = new PermissionNavigationDO();
            temp.setPermissionId(Integer.parseInt(record.getId()));
            list.add(temp);
            cpermissionNavigationDOMapper.deleteBatch(list, null);
            batchInsert(record.getNav(), record);
        }
        return true;
    }

    @Override
    public List<PermissionDO> getPermissionList(int start, int length, String searchstr) {
        return cpermissionDOMapper.getPermissionList(start, length, searchstr);
    }


    private void batchInsert(String nav, PermissionDO record) {
        if (!StringUtil.isEmpty(nav)) {
            String[] str = nav.split(",");
            List<PermissionNavigationDO> list = new ArrayList<PermissionNavigationDO>();
            PermissionNavigationDO temp = null;
            for (String tmp : str) {
                temp = new PermissionNavigationDO();
                temp.setPermissionId(Integer.parseInt(record.getId()));
                temp.setNavigationId(Integer.parseInt(tmp));
                list.add(temp);
            }
            cpermissionNavigationDOMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional
    public void deletePermission(String id) {
        permissionDOMapper.deleteByPrimaryKey(id);
        List<PermissionNavigationDO> list = new ArrayList<PermissionNavigationDO>();
        PermissionNavigationDO temp = new PermissionNavigationDO();
        temp.setPermissionId(Integer.parseInt(id));
        list.add(temp);
        cpermissionNavigationDOMapper.deleteBatch(list, null);
        UserDO user = new UserDO();
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andRoleTypeEqualTo(Integer.parseInt(id));
        user.setRoleType(Integer.parseInt(ConstantsDto.DEFAULT_PERMISSION));
        userDOMapper.updateByExampleSelective(user, example);
    }

    @Override
    public List<PermissionDO> getPermissionListNoTable() {
        return cpermissionDOMapper.selectList();
    }
}
