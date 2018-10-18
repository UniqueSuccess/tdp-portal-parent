package cn.goldencis.tdp.core.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class ScheduledTaskDO extends BaseEntity implements Serializable {
    private Integer id;

    private String guid;

    private String taskReference;

    private String corn;

    private String period;

    private String time;

    private Integer processing;

    private Integer taskSwitch;

    private Date lastExecutionTime;

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

    public String getTaskReference() {
        return taskReference;
    }

    public void setTaskReference(String taskReference) {
        this.taskReference = taskReference == null ? null : taskReference.trim();
    }

    public String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        this.corn = corn == null ? null : corn.trim();
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getProcessing() {
        return processing;
    }

    public void setProcessing(Integer processing) {
        this.processing = processing;
    }

    public Integer getTaskSwitch() {
        return taskSwitch;
    }

    public void setTaskSwitch(Integer taskSwitch) {
        this.taskSwitch = taskSwitch;
    }

    public Date getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(Date lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
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
}