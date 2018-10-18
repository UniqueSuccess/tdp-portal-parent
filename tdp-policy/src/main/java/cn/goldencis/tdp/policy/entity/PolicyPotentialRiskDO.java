package cn.goldencis.tdp.policy.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;

public class PolicyPotentialRiskDO extends BaseEntity implements Serializable {
    private Integer riskId;

    private Integer policyId;

    private static final long serialVersionUID = 1L;

    public Integer getRiskId() {
        return riskId;
    }

    public void setRiskId(Integer riskId) {
        this.riskId = riskId;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    @Override
    public String getPrimaryKey() {
        return riskId.toString();
    }
}