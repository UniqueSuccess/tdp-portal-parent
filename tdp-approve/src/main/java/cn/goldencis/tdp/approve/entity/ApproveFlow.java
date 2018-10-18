package cn.goldencis.tdp.approve.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class ApproveFlow extends BaseEntity implements Serializable {
    private Integer id;

    private String name;

    private Integer flowId;

    private Integer status;

    private Integer pointId;

    private String pointName;

    private String approvers;

    private Integer type;

    private String tranUnique;

    private String applicantId;

    private String applicantName;

    private String reason;

    private Date applyTime;

    private Date finishTime;

    private boolean checked;

    private JSONObject flowInfo;

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

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTranUnique() {
        return tranUnique;
    }

    public void setTranUnique(String tranUnique) {
        this.tranUnique = tranUnique == null ? null : tranUnique.trim();
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId == null ? null : applicantId.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public JSONObject getFlowInfo() {
        return flowInfo;
    }

    public void setFlowInfo(JSONObject flowInfo) {
        this.flowInfo = flowInfo;
    }
}