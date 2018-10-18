package cn.goldencis.tdp.approve.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;

public class ApproveFlowInfo extends BaseEntity implements Serializable {
    private Integer id;

    private Integer flowId;

    private String policyParam;

    private String filePath;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public String getPolicyParam() {
        return policyParam;
    }

    public void setPolicyParam(String policyParam) {
        this.policyParam = policyParam == null ? null : policyParam.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}