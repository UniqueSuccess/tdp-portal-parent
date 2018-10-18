package cn.goldencis.tdp.policy.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class FingerprintDO extends BaseEntity implements Serializable {
    private Integer id;

    private String clientUserGuid;

    private Date createTime;

    private String fingerprintPwd;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientUserGuid() {
        return clientUserGuid;
    }

    public void setClientUserGuid(String clientUserGuid) {
        this.clientUserGuid = clientUserGuid == null ? null : clientUserGuid.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFingerprintPwd() {
        return fingerprintPwd;
    }

    public void setFingerprintPwd(String fingerprintPwd) {
        this.fingerprintPwd = fingerprintPwd == null ? null : fingerprintPwd.trim();
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}