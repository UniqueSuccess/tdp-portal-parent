package cn.goldencis.tdp.core.service;

import java.util.List;

import cn.goldencis.tdp.core.entity.PermissionDO;

public interface IPermissionService {

    boolean addOrUpdatePermission(PermissionDO record);

    List<PermissionDO> getPermissionList(int start, int length, String searchstr);

    Integer countPermission(String searchstr);

    void deletePermission(String id);

    List<PermissionDO> getPermissionListNoTable();
}
