package cn.goldencis.tdp.system.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class VedioLogonAccessDO extends BaseEntity implements Serializable {
    private Integer id;

    private String name;

    private String startMethod;

    private String startPath;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStartMethod() {
        return startMethod;
    }

    public void setStartMethod(String startMethod) {
        this.startMethod = startMethod == null ? null : startMethod.trim();
    }

    public String getStartPath() {
        return startPath;
    }

    public void setStartPath(String startPath) {
        this.startPath = startPath == null ? null : startPath.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}