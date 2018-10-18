package cn.goldencis.tdp.access.service.impl;

import cn.goldencis.tdp.access.entity.AccessConfig;
import cn.goldencis.tdp.access.service.IAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by limingchao on 2018/1/21.
 */
@Component("saveAccessRunnable")
public class SaveAccessRunnable implements Runnable {

    @Autowired
    private IAccessService accessService;

    private AccessConfig accessConfig;

    @Override
    public void run() {
        accessService.saveAccessServerConfig(accessConfig);
    }

    public AccessConfig getAccessConfig() {
        return accessConfig;
    }

    public void setAccessConfig(AccessConfig accessConfig) {
        this.accessConfig = accessConfig;
    }
}
