package cn.goldencis.tdp.core.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;

public class PermissionNavigationDO extends BaseEntity implements Serializable {
    private Integer permissionId;

    private Integer navigationId;

    private static final long serialVersionUID = 1L;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(Integer navigationId) {
        this.navigationId = navigationId;
    }

    public String getPrimaryKey(){
        return this.permissionId.toString();
    }
}