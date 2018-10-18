package cn.goldencis.tdp.report.entity;

import cn.goldencis.tdp.common.annotation.MyFieldAnnotation;
import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class IllegalOperationAlarmDO extends BaseEntity implements Serializable {
    private Integer id;

    @MyFieldAnnotation(order = 1, desc = "export")
    private String truename;

    @MyFieldAnnotation(order = 0, desc = "export")
    private String username;

    private String userguid;

    private Integer departmentId;

    private String departmentName;

    private String devunique;

    private String extradata;

    @MyFieldAnnotation(order = 2, desc = "export")
    private String ip;

    @MyFieldAnnotation(order = 4, desc = "export")
    private String receiver;

    @MyFieldAnnotation(order = 5, desc = "export")
    private String fileName;

    private Integer hasRead;

    @MyFieldAnnotation(order = 3, desc = "export")
    private Integer warningType;

    @MyFieldAnnotation(order = 6, desc = "export")
    private Date warningTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename == null ? null : truename.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid == null ? null : userguid.trim();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getDevunique() {
        return devunique;
    }

    public void setDevunique(String devunique) {
        this.devunique = devunique == null ? null : devunique.trim();
    }

    public String getExtradata() {
        return extradata;
    }

    public void setExtradata(String extradata) {
        this.extradata = extradata == null ? null : extradata.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    public Integer getWarningType() {
        return warningType;
    }

    public void setWarningType(Integer warningType) {
        this.warningType = warningType;
    }

    public Date getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(Date warningTime) {
        this.warningTime = warningTime;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}