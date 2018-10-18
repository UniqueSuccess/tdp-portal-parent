package cn.goldencis.tdp.core.entity;

import java.io.Serializable;
import java.util.List;

import cn.goldencis.tdp.common.annotation.MyFieldAnnotation;
import cn.goldencis.tdp.common.entity.BaseEntity;

public class DepartmentDO extends BaseEntity implements Serializable {
    private List<UserDO> userList;

    private Integer id;

    @MyFieldAnnotation(order = 0, desc = "export")
    private String name;

    private Integer parentId;

    @MyFieldAnnotation(order = 1, desc = "export")
    private String parentName;

    private String departmentRemark;

    @MyFieldAnnotation(order = 2, desc = "export")
    private String owner;

    @MyFieldAnnotation(order = 3, desc = "export")
    private String departmentTel;

    private String ipPart;

    private String treePath;

    private Integer status;

    private String remark;

    /**
     * 部门策略id
     */
    private Integer policyId;

    /**
     * 策略名称
     */
    private String policyName;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentRemark() {
        return departmentRemark;
    }

    public void setDepartmentRemark(String departmentRemark) {
        this.departmentRemark = departmentRemark == null ? null : departmentRemark.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getDepartmentTel() {
        return departmentTel;
    }

    public void setDepartmentTel(String departmentTel) {
        this.departmentTel = departmentTel == null ? null : departmentTel.trim();
    }

    public String getIpPart() {
        return ipPart;
    }

    public void setIpPart(String ipPart) {
        this.ipPart = ipPart == null ? null : ipPart.trim();
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath == null ? null : treePath.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPrimaryKey() {
        return this.id.toString();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<UserDO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDO> userList) {
        this.userList = userList;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
}