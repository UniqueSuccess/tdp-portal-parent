package cn.goldencis.tdp.policy.entity;

import cn.goldencis.tdp.common.annotation.MyFieldAnnotation;
import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class ClientUserDO extends BaseEntity implements Serializable {
    private Integer id;

    private String guid;

    @MyFieldAnnotation(order = 0, desc = "export")
    private String username;

    private String password;

    @MyFieldAnnotation(order = 1, desc = "export")
    private String truename;

    private Integer deptguid;

    private String computerguid;

    private String computername;

    @MyFieldAnnotation(order = 3, desc = "export")
    private String ip;

    //策略名称
    @MyFieldAnnotation(order = 2, desc = "export")
    private String policyname;

    private String mac;

    private Date regtime;

    private Integer policyid;

    private String online;

    private Date onlineTime;

    private Date offlineTime;

    private String remark;

    private String departmentName;;
    private String policyName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename == null ? null : truename.trim();
    }

    public Integer getDeptguid() {
        return deptguid;
    }

    public void setDeptguid(Integer deptguid) {
        this.deptguid = deptguid;
    }

    public String getComputerguid() {
        return computerguid;
    }

    public void setComputerguid(String computerguid) {
        this.computerguid = computerguid == null ? null : computerguid.trim();
    }

    public String getComputername() {
        return computername;
    }

    public void setComputername(String computername) {
        this.computername = computername == null ? null : computername.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    public Integer getPolicyid() {
        return policyid;
    }

    public void setPolicyid(Integer policyid) {
        this.policyid = policyid;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online == null ? null : online.trim();
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPrimaryKey() {
        return null;
    }

    public String getPolicyname() {
        return policyname;
    }

    public void setPolicyname(String policyname) {
        this.policyname = policyname;
    }
}