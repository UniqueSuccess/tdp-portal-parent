package cn.goldencis.tdp.access.entity;

import cn.goldencis.tdp.common.utils.ArrayUtils;
import cn.goldencis.tdp.common.utils.StringUtil;

import java.util.List;

/**
 * Created by limingchao on 2018/1/11.
 */
public class AccessConfig {

    private String nacServer;

    private boolean onoff;

    private String nacMac;

    private String nacUrl;

    private String[] ctrlAreas;

    private String[] httpPorts;

    private String[] legalIps;

    private String[] ilegalIps;

    private String[] legalPorts;

    private String[] ilegalPorts;

    public String getNacServer() {
        return nacServer;
    }

    public void setNacServer(String nacServer) {
        this.nacServer = nacServer;
    }

    public boolean isOnoff() {
        return onoff;
    }

    public void setOnoff(boolean onoff) {
        this.onoff = onoff;
    }

    public String getNacMac() {
        return nacMac;
    }

    public void setNacMac(String nacMac) {
        this.nacMac = nacMac;
    }

    public String getNacUrl() {
        return nacUrl;
    }

    public void setNacUrl(String nacUrl) {
        this.nacUrl = nacUrl;
    }

    public String[] getCtrlAreas() {
        return ctrlAreas;
    }

    public List<String> getCtrlAreasList() {
        return StringUtil.arrayToList(ctrlAreas);
    }

    public void setCtrlAreas(String[] ctrlAreas) {
        this.ctrlAreas = ctrlAreas;
    }

    public String[] getHttpPorts() {
        return httpPorts;
    }

    public List<String> getHttpPortsList() {
        return StringUtil.arrayToList(httpPorts);
    }

    public void setHttpPorts(String[] httpPorts) {
        this.httpPorts = httpPorts;
    }

    public String[] getLegalIps() {
        return legalIps;
    }

    public List<String> getLegalIpsList() {
        return StringUtil.arrayToList(legalIps);
    }

    public void setLegalIps(String[] legalIps) {
        this.legalIps = legalIps;
    }

    public String[] getIlegalIps() {
        return ilegalIps;
    }

    public List<String> getIlegalIpsList() {
        return StringUtil.arrayToList(ilegalIps);
    }

    public void setIlegalIps(String[] ilegalIps) {
        this.ilegalIps = ilegalIps;
    }

    public String[] getLegalPorts() {
        return legalPorts;
    }

    public List<String> getLegalPortsList() {
        return StringUtil.arrayToList(legalPorts);
    }

    public void setLegalPorts(String[] legalPorts) {
        this.legalPorts = legalPorts;
    }

    public String[] getIlegalPorts() {
        return ilegalPorts;
    }

    public List<String> getIlegalPortsList() {
        return StringUtil.arrayToList(ilegalPorts);
    }

    public void setIlegalPorts(String[] ilegalPorts) {
        this.ilegalPorts = ilegalPorts;
    }

    public String generateNacCongurationLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("nac服务器：").append(this.nacServer);
        sb.append("，启用状态：").append(this.onoff);
        sb.append("，mac地址：").append(this.nacMac);
        if (this.ctrlAreas != null) {
            sb.append("，跳转地址：").append(ArrayUtils.array2String(this.ctrlAreas));
        }
        if (this.httpPorts != null) {
            sb.append("，web重定向端口：").append(ArrayUtils.array2String(this.httpPorts));
        }
        if (this.legalIps != null) {
            sb.append("，白名单ip：").append(ArrayUtils.array2String(this.legalIps));
        }
        if (this.legalPorts != null) {
            sb.append("，白名单端口：").append(ArrayUtils.array2String(this.legalPorts));
        }
        return sb.toString();
    }
}
