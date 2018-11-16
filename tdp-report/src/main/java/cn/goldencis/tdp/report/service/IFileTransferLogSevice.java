package cn.goldencis.tdp.report.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.report.entity.TFileTransferLog;

public interface IFileTransferLogSevice {

    List<TFileTransferLog> queryFileTransferLog(Map<String, Object> params);

    Integer queryFileTransferLogCount(Map<String, Object> params);

    /**
     * 根据文件 类型查询数量
     * @param type
     * @return
     */
    Long getFileTypeCount(String type);

    /**
     * 查询全部文件流转日志数量
     * @return
     * @param departmentId
     * @param nodeType 节点类型
     * @param logType
     * @param submitDate
     * @param order
     */
    int countFileTransferLog(List<DepartmentDO> departmentId, String nodeType, String logType, String submitDate, String startDate, String endDate, String order);
    /**
     * 将获取文件流转日志列表
     * @param departmentList 用户权限部门集合
     * @param logType 日志类型
     * @param submitDate 搜索类型
     * @param order 查询条件
     * @return
     */
    List<TFileTransferLog> getFileTransferLogListByParams(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 将获取视频流转日志列表转化为按时间分类的集合
     * @param fileTransferLogs
     * @return
     */
    List<Integer> getFileTransferLogInHours(List<TFileTransferLog> fileTransferLogs);


    /**
     * 将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
     * @param fileTransferLogList
     * @param startDate
     *@param endDate @return
     */
    Map<String,Object> getFileTransferLogInDate(List<TFileTransferLog> fileTransferLogList, String startDate, String endDate) throws ParseException;

    /**
     * 删除外发日志
     * @param clearDate
     */
    void deleteFileTransferLog(Date clearDate);
}
