package cn.goldencis.tdp.core.entity;

import cn.goldencis.tdp.common.annotation.MyFieldAnnotation;
import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class UserDO extends BaseEntity implements Serializable {
    private Integer id;

    private String guid;

    @MyFieldAnnotation(order = 1, desc = "export")
    private String userName;

    private String password;

    private Integer department;

    @MyFieldAnnotation(order = 0, desc = "export")
    private String name;

    private Integer sex;

    private Integer visible;

    private String email;

    @MyFieldAnnotation(order = 4, desc = "export")
    private String phone;

    private String address;

    private Integer status;

    private Integer roleType;

    @MyFieldAnnotation(order = 2, desc = "export")
    private String roleTypeName;

    private Integer readonly;

    @MyFieldAnnotation(order = 3, desc = "export")
    private String readonlyName;

    private String skin;

    private Integer errorLoginCount;

    private Date errorLoginLastTime;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getReadonly() {
        return readonly;
    }

    public void setReadonly(Integer readonly) {
        this.readonly = readonly;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin == null ? null : skin.trim();
    }

    public Integer getErrorLoginCount() {
        return errorLoginCount;
    }

    public void setErrorLoginCount(Integer errorLoginCount) {
        this.errorLoginCount = errorLoginCount;
    }

    public Date getErrorLoginLastTime() {
        return errorLoginLastTime;
    }

    public void setErrorLoginLastTime(Date errorLoginLastTime) {
        this.errorLoginLastTime = errorLoginLastTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }

    public String getRoleTypeName() {
        switch (this.getRoleType().intValue()) {
            case 0:
                return "超级管理员";

            case 1:
                return "管理员";

            case 2:
                return "操作员";

            case 3:
                return "审计员";

            default:
                return "";
        }
    }

    public String getReadonlyName() {
        switch (this.getReadonly().intValue()) {
            case 0:
                return "读写权限";

            case 1:
                return "只读权限";

            default:
                return "";
        }
    }

}