package cn.goldencis.tdp.core.entity;

import cn.goldencis.tdp.common.annotation.MyFieldAnnotation;
import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class OperationLogDO extends BaseEntity implements Serializable {

    private Integer logId;

    @MyFieldAnnotation(order = 2, desc = "export")
    private Date time;

    @MyFieldAnnotation(order = 3, desc = "export")
    private String ip;

    @MyFieldAnnotation(order = 0, desc = "export")
    private String userName;

    private Integer logType;

    @MyFieldAnnotation(order = 1, desc = "export")
    private String logPage;

    private String logDesc;

    @MyFieldAnnotation(order = 4, desc = "export")
    private String logOperateParam;

    private static final long serialVersionUID = 1L;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogPage() {
        return logPage;
    }

    public void setLogPage(String logPage) {
        this.logPage = logPage == null ? null : logPage.trim();
    }

    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc == null ? null : logDesc.trim();
    }

    public String getLogOperateParam() {
        return logOperateParam;
    }

    public void setLogOperateParam(String logOperateParam) {
        this.logOperateParam = logOperateParam == null ? null : logOperateParam.trim();
    }

    @Override
    public String getPrimaryKey() {
        return getLogId().toString();
    }
}