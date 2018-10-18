package cn.goldencis.tdp.policy.entity;

import cn.goldencis.tdp.common.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class UsbKeyDO extends BaseEntity implements Serializable {
    private Integer id;

    private String name;

    private String keysn;

    private String keynum;

    private Date regtime;

    private String userguid;

    private String userName;

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

    public String getKeysn() {
        return keysn;
    }

    public void setKeysn(String keysn) {
        this.keysn = keysn == null ? null : keysn.trim();
    }

    public String getKeynum() {
        return keynum;
    }

    public void setKeynum(String keynum) {
        this.keynum = keynum == null ? null : keynum.trim();
    }

    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid == null ? null : userguid.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrimaryKey() {
        return getId().toString();
    }
}