package cn.goldencis.tdp.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.goldencis.tdp.common.annotation.Mybatis;
import cn.goldencis.tdp.core.entity.PermissionNavigationDO;

@Mybatis
public interface CPermissionNavigationDOMapper {

    void insertBatch(@Param("list") List<PermissionNavigationDO> list);

    List<PermissionNavigationDO> selectList(String permissionId);

    void deleteBatch(@Param("list") List<PermissionNavigationDO> list, @Param("listN") List<PermissionNavigationDO> listN);
}
