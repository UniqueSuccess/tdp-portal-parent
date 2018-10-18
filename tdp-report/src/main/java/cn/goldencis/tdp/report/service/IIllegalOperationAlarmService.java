package cn.goldencis.tdp.report.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDO;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDOCriteria;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by limingchao on 2018/5/23.
 */
public interface IIllegalOperationAlarmService extends BaseService<IllegalOperationAlarmDO, IllegalOperationAlarmDOCriteria> {

    /**
     * 解析参数，转化为非法登录告警对象
     * @param alarm 非法登录告警对象
     * @param argvJson 上报参数
     */
    void encapsulationBean(IllegalOperationAlarmDO alarm, JSONObject argvJson);

    /**
     * 添加非法告警
     * @param alarm 非法告警
     */
    void addIllegalOperationAlarm(IllegalOperationAlarmDO alarm);

    /**
     * 添加视频流转的告警信息
     * @param alarm 非法告警
     * @param argvJson 视频流转上报参数
     */
    void warningVideoTransfer(IllegalOperationAlarmDO alarm, JSONObject argvJson);

    /**
     * 查询全部告警日志数量
     * @param departmentList 部门集合
     * @param logType 日志类型
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    int IllegalOperationAlarm(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 获取告警日志列表,分页查询
     * @param start 开始
     * @param length 每页数量
     * @param departmentList 部门集合
     * @param logType 日志类型
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    List<IllegalOperationAlarmDO> getIllegalOperationAlarmInPage(Integer start, Integer length, List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 将id在集合中的告警状态，置为已读
     * @param idList 告警id集合
     */
    void readIllegalOperationAlarmByIdList(List<Integer> idList);

    /**
     * 统计未读告警的数量
     * @return 未读告警的数量
     */
    int countNotReadAlarm();

    /**
     * 通过socket给所有前端在线用户推送告警信息的数量
     */
    void noticeIllegalOperationAlarm();

    /**
     * 按条件获取告警日志列表，不分页
     * @param departmentList 部门集合
     * @param logType 日志类型
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    List<IllegalOperationAlarmDO> getIllegalOperationAlarm(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order);
}
