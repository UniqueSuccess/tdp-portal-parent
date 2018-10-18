package cn.goldencis.tdp.core.dao;

import java.util.List;

import cn.goldencis.tdp.common.annotation.Mybatis;
import cn.goldencis.tdp.core.entity.PermissionDO;

@Mybatis
public interface CPermissionDOMapper {

    Integer countPermission(String searchstr);

    List<PermissionDO> getPermissionList(int start, int length, String search);

    List<PermissionDO> selectList();

    PermissionDO selectByPrimaryKey(String sid);
}
