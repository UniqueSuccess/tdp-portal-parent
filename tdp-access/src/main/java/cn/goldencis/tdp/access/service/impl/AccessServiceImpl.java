package cn.goldencis.tdp.access.service.impl;

import cn.goldencis.tdp.access.entity.AccessConfig;
import cn.goldencis.tdp.access.service.IAccessService;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.neiwang.nac.api.NacAPI;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/11.
 */
@Service
public class AccessServiceImpl implements IAccessService, ServletContextAware {

    @Value("${accessServer.ip}")
    private String accessServerIp;

    private static boolean hasInited = false;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        //初始化
        if (!hasInited) {
            this.updateAccessServerConfig();
            hasInited = true;
        }
    }

    /**
     * 读取准入服务器配置信息
     * @return
     */
    @Override
    public Map<String, Object> readAccessServerConfig(Map<String, Object> configMap) {
        //获取准入服务器状态和配置信息
        boolean accessStatus = (boolean)servletContext.getAttribute("accessStatus");
        String accessConfig = (String)servletContext.getAttribute("accessConfig");
        configMap.put("status", accessStatus);

        //读取配置信息
        if (accessStatus && !StringUtil.isEmpty(accessConfig)) {
            configMap.put("onoff", NacAPI.isNacEnabledWithGlobalConfig(accessConfig));
            configMap.put("nacMac", NacAPI.getNacMacWithGlobalConfig(accessConfig));
            configMap.put("nacUrl", NacAPI.getNacUrlWithGlobalConfig(accessConfig));
            configMap.put("ctrlAreas", NacAPI.getCtrlAreasWithGlobalConfig(accessConfig));
            configMap.put("httpPorts", NacAPI.getHttpPortsWithGlobalConfig(accessConfig));
            configMap.put("legalIps", NacAPI.getLegalIpsWithGlobalConfig(accessConfig));
            configMap.put("illegalIps", NacAPI.getIllegalIpsWithGlobalConfig(accessConfig));
            configMap.put("legalPorts", NacAPI.getLegalPortsWithGlobalConfig(accessConfig));
            configMap.put("illegalPorts", NacAPI.getIllegalPortsWithGlobalConfig(accessConfig));
        }

        return configMap;
    }

    /**
     * 保存准入服务器配置信息
     * @param accessConfig
     */
    @Override
    public void saveAccessServerConfig(AccessConfig accessConfig) {
        //保存准入配置时，默认设置0-65535，即所有端口都跳转
        List<String> portsList= new ArrayList<>();
        portsList.add("0-65535");
        //调用NacAPI，设置准入服务器的配置信息
        NacAPI.setNacGlobalConfig(accessServerIp, accessConfig.isOnoff(), accessConfig.getNacUrl(), accessConfig.getNacMac(),
                accessConfig.getCtrlAreasList(), portsList, accessConfig.getLegalIpsList(),
                accessConfig.getIlegalIpsList(), accessConfig.getLegalPortsList(), accessConfig.getIlegalPortsList());
        this.updateAccessServerConfig();
    }

    /**
     * 更新准入服务器状态和配置信息
     */
    @Override
    public void updateAccessServerConfig() {

        //获取准入服务器状态
        boolean accessStatus = NacAPI.isNacServerAlive(accessServerIp);

        //获取准入服务器配置信息
        String accessConfig = "";
        if (accessStatus) {
            accessConfig = NacAPI.getNacGlobalConfig(accessServerIp);
        }

        //更新准入服务器状态和配置信息
        servletContext.setAttribute("accessStatus", accessStatus);
        servletContext.setAttribute("accessConfig", accessConfig);
    }

    /**
     * 设置动态ip,放行和阻拦
     * @param trustIps
     * @param blockIps
     */
    @Override
    public boolean setDynamicRules(List<String> trustIps, List<String> blockIps) {
        return NacAPI.setDynamicRules(accessServerIp, trustIps, blockIps);
    }

    /**
     * 获取动态IP设置
     */
    @Override
    public List<String> getDynamicRules() {
        List<String> ips = NacAPI.getDynamicRules(accessServerIp);
        return ips;
    }

    @Override
    public void testNacAPI(ResultMsg resultMsg) {
        JSONObject json = new JSONObject();
        boolean alive = NacAPI.isNacServerAlive(accessServerIp);
        json.put("alive", alive);

        resultMsg.setData(json);
    }


}
