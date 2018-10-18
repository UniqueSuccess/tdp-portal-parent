package cn.goldencis.tdp.approve.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class ApproveModel extends BaseEntity implements Serializable {
    private Integer id;

    private String name;

    private String approvers;

    private String[] approverArr;

    private Integer flowId;

    private Integer seniorId;

    private Integer standard;

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

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers == null ? null : approvers.trim();
        if (approvers != null) {
            this.approverArr = approvers.split(";");
        }
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getSeniorId() {
        return seniorId;
    }

    public void setSeniorId(Integer seniorId) {
        this.seniorId = seniorId;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
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

    public String[] getApproverArr() {
        return approverArr;
    }

    public void setApproverArr(String[] approverArr) {
        this.approverArr = approverArr;
    }
}