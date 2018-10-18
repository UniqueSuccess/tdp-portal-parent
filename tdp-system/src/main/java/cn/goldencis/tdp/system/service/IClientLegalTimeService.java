package cn.goldencis.tdp.system.service;

import java.util.Map;

/**
 * Created by limingchao on 2018/5/14.
 */
public interface IClientLegalTimeService {

    /**
     * 查询客户端合法登陆时间
     * @return
     */
    Map<String,String> getClientLegalTime();

    /**
     * 修改客户端合法登陆时间的接口
     * @param week 每周几为合法登陆时间
     * @param begin 每天的开始时间
     * @param end 每天的结束时间
     * @param sound
     */
    void updateClientLegalTime(String week, String begin, String end, boolean sound);

    /**
     * 刷新禁止截屏的状态
     */
    boolean refreshEnableState();
    /**
     * 更新禁止截屏的开启状态
     * @param isNeeded 更新的状态
     */
    void updateForbidScreenState(String isNeeded);

    /**
     * 获取禁止截屏的状态
     * @return
     */
    String getForbidScreen();
}
