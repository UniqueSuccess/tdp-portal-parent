package cn.goldencis.tdp.report.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDOCriteria;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/22.
 */
public interface IVideoTransferLogService extends BaseService<VideoTransferLogDO, VideoTransferLogDOCriteria> {

    /**
     * 添加视频流转日志
     * @param videoTransferLog
     */
    void addVideoTransferLog(VideoTransferLogDO videoTransferLog);

    /**
     * 解析参数，转化为视频流转日志对象
     * @param videoTransferLog
     * @param argvJson
     */
    void encapsulationBean(VideoTransferLogDO videoTransferLog, JSONObject argvJson);

    /**
     * 保存上传的文件，更新日志对象的文件地址
     * @param auditfile
     * @param videoTransferLog
     */
    void uploadAttatchFile(MultipartFile auditfile, VideoTransferLogDO videoTransferLog);

    /**
     * 查询全部视频流转日志数量
     * @return
     * @param departmentId
     * @param nodeType 节点类型
     * @param logType
     * @param submitDate
     * @param order
     */
    int countVideoTransferLog(List<DepartmentDO> departmentId, String nodeType, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 获取视频流转日志列表,分页查询，暂无查询条件
     * @param start
     * @param length
     * @param departmentId
     * @param nodeType 节点类型
     * @param logType
     * @param submitDate
     * @param order @return    */
    List<VideoTransferLogDO> getVideoTransferLogInPage(Integer start, Integer length, List<DepartmentDO> departmentId, String nodeType, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 将获取视频流转日志列表转化为按时间分类的集合
     * @param VideoTransferLogList
     * @return
     */
    List<Integer> getVideoTransferLogInHours(List<VideoTransferLogDO> VideoTransferLogList);

    /**
     * 将获取视频流转日志列表
     * @param departmentList 用户权限部门集合
     * @param logType 日志类型
     * @param submitDate 搜索类型
     * @param order 查询条件
     * @return
     */
    List<VideoTransferLogDO> getVideoTransferLogListByParams(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
     * @param videoTransferLogList
     * @param startDate
     *@param endDate @return
     */
    Map<String,Object> getVideoTransferLogInDate(List<VideoTransferLogDO> videoTransferLogList, String startDate, String endDate) throws ParseException;

    /**
     * 查询视频流转日志表中，统计记录最多用户的guid的前十名，返回真实姓名和统计数量
     * @return
     */
    List<Map<String,Object>> countVideoTransferTop5();

    /**
     * 清理视频流转日志
     * @param clearDate 需要清理日志的日期
     *
     */
    void deleteVideoTransferLogsByClearDate(Date clearDate);

    /**
     * 组装查询条件
     * @param departmentList 部门集合
     * @param nodeType 节点类型
     * @param logType 日志类型
     * @param submitDate 查询日期类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param order 查询条件
     * @return 查询条件实体
     */
    VideoTransferLogDOCriteria generatorVideoTransferLogExample(List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order);

    /**
     * 组装查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询条件实体
     */
    VideoTransferLogDOCriteria generatorVideoTransferLogExample(Date startTime, Date endTime);

    /**
     * 采集下级服务器的视频流转日志接口
     * @param start 开始时间
     * @param end 结束时间
     * @param childNodeList
     */
    Map<String, String> collectVideoTransferLogsWithAttachment(String start, String end, List<ChildNodeDO> childNodeList);

    /**
     * 采集下级服务器的视频流转日志接口的定时任务接口
     * @param taskguid 任务guid
     */
    void collectVideoTransferLogsWithAttachment(String taskguid);

    /**
     * 校验集合中的日志，是否已经存在数据库中，如果存在，将其从数据中剔除
     * @param videoTransferLogList 视频流转日志集合
     * @param nodeIp 节点IP
     */
    void removeExistvideoTransferLogsFromList(List<VideoTransferLogDO> videoTransferLogList, String nodeIp);

    /**
     * 上传日志之前，统计需要上传日志的数量
     * @param example 查询调剂
     * @return 日志的数量
     */
    int countVideoTransferLogsForCollecting(VideoTransferLogDOCriteria example);
}
