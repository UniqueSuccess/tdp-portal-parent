package cn.goldencis.tdp.approve.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class ScrnwatermarkLog extends BaseEntity implements Serializable {
    private Integer id;

    private String scrnwatermarkId;

    private String authId;

    private String applicantId;

    private String applicantName;

    private String applyInfo;

    private Date applyTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScrnwatermarkId() {
        return scrnwatermarkId;
    }

    public void setScrnwatermarkId(String scrnwatermarkId) {
        this.scrnwatermarkId = scrnwatermarkId == null ? null : scrnwatermarkId.trim();
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
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

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo == null ? null : applyInfo.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}