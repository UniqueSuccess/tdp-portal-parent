package cn.goldencis.tdp.access.service;

import cn.goldencis.tdp.access.entity.AccessConfig;
import cn.goldencis.tdp.core.entity.ResultMsg;

import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/11.
 */
public interface IAccessService {

    /**
     * 读取准入服务器配置信息
     * @return
     */
    Map<String, Object> readAccessServerConfig(Map<String, Object> configMap);

    /**
     * 保存准入服务器配置信息
     * @param accessConfig
     */
    void saveAccessServerConfig(AccessConfig accessConfig);

    /**
     * 更新准入服务器状态和配置信息
     */
    void updateAccessServerConfig();

    /**
     * 设置动态ip,放行和阻拦
     * @param trustIps
     * @param blockIps
     */
    boolean setDynamicRules(List<String> trustIps, List<String> blockIps);

    /**
     * 获取动态IP设置
     */
    List<String> getDynamicRules();

    void testNacAPI(ResultMsg resultMsg);
}
