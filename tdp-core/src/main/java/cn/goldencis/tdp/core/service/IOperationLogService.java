package cn.goldencis.tdp.core.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.OperationLogDOCriteria;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/25.
 */
public interface IOperationLogService extends BaseService<OperationLogDO, OperationLogDOCriteria> {

    /**
     * 多条件分页查询登录日志
     * @param start
     * @param length
     * @param logType     是否为登录日志类型，
     * @param order
     * @return
     */
    List<OperationLogDO> getSystemLogListInPageByParams(Integer start, Integer length, String logType, Map<String, Date> timeMap, String search, String orderType);

    /**
     * 查询登录日志总数
     * @param logType
     * @param logType
     *@param order  @return
     */
    int countSystemLogListByParams(String logType, Map<String, Date> timeMap, String order);

    /**
     * 按条件获取日志列表，不分页
     * @param logType
     * @param timeMap
     * @param order
     */
    List<OperationLogDO> getSystemLogListByParams(String logType, Map<String, Date> timeMap, String order);

    /**
     * 清理系统日志
     * @param clearDate 需要清理日志的日期
     * @param logType
     */
    void deleteOperationLogsByClearDate(Date clearDate, String logType);
}
