package cn.goldencis.tdp.report.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.dao.ChildNodeDOMapper;
import cn.goldencis.tdp.core.dao.ScheduledTaskDOMapper;
import cn.goldencis.tdp.core.entity.*;
import cn.goldencis.tdp.core.scheduledtask.DynamicScheduledTask;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.HttpClientUtil;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.scheduledtask.ExecutableTask;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.report.dao.CVideoTransferLogDOMapper;
import cn.goldencis.tdp.report.dao.VideoTransferLogDOMapper;
import cn.goldencis.tdp.report.entity.VideoTransferLogDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDOCriteria;
import cn.goldencis.tdp.report.service.IVideoTransferLogService;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by limingchao on 2018/1/22.
 */
@Service
public class VideoTransferLogServiceImpl extends AbstractBaseServiceImpl<VideoTransferLogDO, VideoTransferLogDOCriteria> implements IVideoTransferLogService {

    private Logger logger = Logger.getLogger(VideoTransferLogServiceImpl.class);

    @Autowired
    private VideoTransferLogDOMapper mapper;

    @Autowired
    private CVideoTransferLogDOMapper cVideoTransferLogDOMapper;

    @Autowired
    private ChildNodeDOMapper childNodeDOMapper;

    @Autowired
    private ScheduledTaskDOMapper scheduledTaskDOMapper;

    @Autowired
    private DynamicScheduledTask dynamicScheduledTask;

    @Value("${uploadVideoTransferLog.url}")
    private String url;

    @Value("${uploadVideoTransferLog.counturl}")
    private String countUrl;

    @Override
    protected BaseDao<VideoTransferLogDO, VideoTransferLogDOCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    public void init() {
        try {
            ExecutableTask executableTask = new ExecutableTask("日志采集", this, this.getClass().getMethod("collectVideoTransferLogsWithAttachment", String.class), "cn.goldencis.tdp.report.service.impl.VideoTransferLogServiceImpl.collectVideoTransferLogsWithAttachment");
            dynamicScheduledTask.addExecutableTask(executableTask);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加视频流转日志
     *
     * @param videoTransferLog
     */
    @Override
    public void addVideoTransferLog(VideoTransferLogDO videoTransferLog) {
        mapper.insertSelective(videoTransferLog);
    }

    /**
     * 解析参数，转化为视频流转日志对象
     *
     * @param videoTransferLog
     * @param argvJson
     */
    @Override
    public void encapsulationBean(VideoTransferLogDO videoTransferLog, JSONObject argvJson) {
        videoTransferLog.setGuid(UUID.randomUUID().toString());
        videoTransferLog.setTruename((String) argvJson.get("truename"));
        videoTransferLog.setUsername((String) argvJson.get("username"));
        videoTransferLog.setUserguid((String) argvJson.get("usrunique"));
        videoTransferLog.setDepartmentId(Integer.parseInt((String) argvJson.get("depid")));
        videoTransferLog.setDepartmentName((String) argvJson.get("depname"));
        videoTransferLog.setDevunique((String) argvJson.get("devunique"));
        videoTransferLog.setNodeLevel(ConstantsDto.CURRENT_NODE_LEVEL);

        //解析文件名
        String fileName = (String) argvJson.get("filepath");
        videoTransferLog.setFileName(fileName);

        //设置时间
        Date transferTime;
        try {
            transferTime = DateUtil.getDateByStrWithFormat((String) argvJson.get("time"), DateUtil.DateTimeFormat);
        } catch (ParseException e) {
            transferTime = new Date();
        }
        videoTransferLog.setTransferTime(transferTime);

        videoTransferLog.setTranunique((String) argvJson.get("tranunique"));
        videoTransferLog.setFftype((Integer) argvJson.get("fftype"));

        //从附加数据中获取接受者和水印模式
        JSONObject extradata = (JSONObject) argvJson.get("extradata");
        videoTransferLog.setReceiver((String) extradata.get("recv"));

        videoTransferLog.setExtradata(argvJson.toString());
    }

    /**
     * 保存上传的文件，更新日志对象的文件地址
     *
     * @param auditfile
     * @param videoTransferLog
     */
    @Override
    public void uploadAttatchFile(MultipartFile auditfile, VideoTransferLogDO videoTransferLog) {
        //获取视频流转日志上传的路径，该路径在配置文件中是绝对路径，直接使用。
        String dirPath = PathConfig.VIDEOTRANSFERLOG_DIRPATH + File.separator + ConstantsDto.CURRENT_NODE + File.separator + DateUtil.getCurrentDate("yyyyMM");

        String fileName = auditfile.getOriginalFilename();
        String filePath = dirPath + "/" + fileName;
        String realPath = PathConfig.VIDEOTRANSFERLOG_ROOTPATH + filePath;

        File file = new File(realPath);
        while (file.exists()) {
            fileName = UUID.randomUUID().toString() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
            filePath = dirPath + "/" + fileName;
            realPath = PathConfig.VIDEOTRANSFERLOG_ROOTPATH + filePath;
            file = new File(realPath);
        }

        FileUpload fileUploader = new FileUpload();
        fileUploader.uploadFile(auditfile, PathConfig.VIDEOTRANSFERLOG_ROOTPATH + dirPath, fileName);
        videoTransferLog.setFilePath(filePath);
    }

    /**
     * 查询全部视频流转日志数量
     *
     * @param departmentList
     * @param logType
     * @param submitDate
     * @param startDate
     * @param endDate
     * @param order
     * @return
     */
    @Override
    public int countVideoTransferLog(List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order) {
        VideoTransferLogDOCriteria example = this.generatorVideoTransferLogExample(departmentList, nodeType, logType, submitDate, startDate, endDate, order);
        long count = mapper.countByExample(example);
        return (int) count;
    }

    /**
     * 获取视频流转日志列表,分页查询，暂无查询条件
     *
     * @param start
     * @param length
     * @param departmentList
     * @param logType
     * @param submitDate
     * @param startDate
     * @param endDate
     * @param order
     * @return
     */
    @Override
    public List<VideoTransferLogDO> getVideoTransferLogInPage(Integer start, Integer length, List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order) {
        RowBounds rowBounds = new RowBounds(start, length);

        //设置查询参数
        VideoTransferLogDOCriteria example = this.generatorVideoTransferLogExample(departmentList, nodeType, logType, submitDate, startDate, endDate, order);

        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    /**
     * 将获取视频流转日志列表转化为按时间分类的集合
     *
     * @return
     */
    @Override
    public List<Integer> getVideoTransferLogInHours(List<VideoTransferLogDO> videoTransferLogList) {
        List<Integer> hoursCount = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursCount.add(0);
        }
        Calendar calendar = Calendar.getInstance();
        for (VideoTransferLogDO videoLog : videoTransferLogList) {
            calendar.setTime(videoLog.getTransferTime());
            int i = calendar.get(Calendar.HOUR_OF_DAY);
            hoursCount.set(i, hoursCount.get(i) + 1);
        }
        return hoursCount;
    }

    /**
     * 将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
     *
     * @param videoTransferLogList
     * @param startDate
     * @param endDate              @return
     */
    @Override
    public Map<String, Object> getVideoTransferLogInDate(List<VideoTransferLogDO> videoTransferLogList, String startDate, String endDate) throws ParseException {
        Map<String, Object> resMap = new HashMap<>();

        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DateFormatStr);
        Date startTime = sdf.parse(startDate);
        Date endTime = sdf.parse(endDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);

        while (calendar.getTime().before(endTime)) {
            String dataStr = sdf.format(calendar.getTime());
            dateList.add(dataStr);
            countList.add(0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        dateList.add(endDate);
        countList.add(0);

        int index;
        for (VideoTransferLogDO videoTransferLog : videoTransferLogList) {
            Date transferTime = videoTransferLog.getTransferTime();
            String format = sdf.format(transferTime);
            index = dateList.indexOf(format);
            countList.set(index, countList.get(index) + 1);
        }

        resMap.put("dateList", dateList);
        resMap.put("countList", countList);
        return resMap;
    }

    /**
     * 查询视频流转日志表中，统计记录最多用户的guid的前五名，返回真实姓名和统计数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> countVideoTransferTop5() {
        List<Map<String, Object>> countMaps = cVideoTransferLogDOMapper.countVideoTransferTop5();
        return countMaps;
    }

    /**
     * 清理视频流转日志
     *
     * @param clearDate 需要清理日志的日期
     */
    @Override
    @Transactional
    public void deleteVideoTransferLogsByClearDate(Date clearDate) {
        VideoTransferLogDOCriteria example = new VideoTransferLogDOCriteria();
        example.createCriteria().andTransferTimeLessThan(clearDate);

        String realPath;
        File file;
        List<VideoTransferLogDO> logList = mapper.selectByExample(example);
        for (VideoTransferLogDO log : logList) {
            //判断日志文件是否存在，存在则删除
            realPath = PathConfig.VIDEOTRANSFERLOG_ROOTPATH + log.getFilePath();
            file = new File(realPath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("视频流转文件无法删除！");
                }
            }
        }

        mapper.deleteByExample(example);
    }

    /**
     * 将获取视频流转日志列表
     *
     * @param departmentList
     * @param logType
     * @param submitDate
     * @param startDate
     * @param endDate
     * @param order          @return
     */
    @Override
    public List<VideoTransferLogDO> getVideoTransferLogListByParams(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        //设置查询参数
        VideoTransferLogDOCriteria example = this.generatorVideoTransferLogExample(departmentList, ConstantsDto.CURRENT_NODE, logType, submitDate, startDate, endDate, order);

        return mapper.selectByExample(example);
    }

    /**
     * 设置查询参数
     * * @param departmentList 用户权限部门集合
     * @param logType    日志类型
     * @param submitDate 搜索类型
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param order      查询条件
     * @return
     */
    public VideoTransferLogDOCriteria generatorVideoTransferLogExample(List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order) {
        VideoTransferLogDOCriteria example = new VideoTransferLogDOCriteria();
        //设置查询条件
        List<VideoTransferLogDOCriteria.Criteria> criteriaList = new ArrayList<>();
        if (!StringUtil.isEmpty(order)) {
            criteriaList.add(example.or().andTruenameLike("%" + order + "%"));
            criteriaList.add(example.or().andUsernameLike("%" + order + "%"));
            criteriaList.add(example.or().andFileNameLike("%" + order + "%"));
            criteriaList.add(example.or().andReceiverLikeInsensitive("%" + order + "%"));
        } else {
            criteriaList.add(example.createCriteria());
        }

        //设置当前节点层级，查询本地服务器日志，采用默认节点层级
        if (!StringUtils.isEmpty(nodeType)) {
            if (ConstantsDto.CURRENT_NODE.equals(nodeType)) {
                for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                    criteria.andNodeLevelEqualTo(ConstantsDto.CURRENT_NODE_LEVEL);
                }
            } else if (ConstantsDto.CHILD_NODE.equals(nodeType)) {
                for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                    criteria.andNodeLevelNotEqualTo(ConstantsDto.CURRENT_NODE_LEVEL);
                }
            } else {
                List<ChildNodeDO> childNodeList = childNodeDOMapper.selectByExample(new ChildNodeDOCriteria());
                if (childNodeList != null && childNodeList.size() > 0) {
                    List<String> ipList = childNodeList.stream().map(ChildNodeDO::getNodeIp).filter(nodeIp -> nodeIp.equals(nodeType)).collect(Collectors.toList());
                    if (ipList != null && ipList.size() == 1) {
                        for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                            criteria.andNodeLevelNotEqualTo(ConstantsDto.CURRENT_NODE_LEVEL).andNodeIpEqualTo(ipList.get(0));
                        }
                    } else if (ipList != null && ipList.size() > 1) {
                        for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                            criteria.andNodeLevelNotEqualTo(ConstantsDto.CURRENT_NODE_LEVEL).andNodeIpIn(ipList);
                        }
                    }
                }
            }
        }

        //设置部门查询条件
        if (departmentList != null && departmentList.size() > 0) {
            List<Integer> idList = new ArrayList<>();
            for (DepartmentDO department : departmentList) {
                idList.add(department.getId());
            }
            for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                criteria.andDepartmentIdIn(idList);
            }
        }

        //添加流转类型查询
        if (!StringUtils.isEmpty(logType)) {
            List<Integer> fftypeList = null;

            if ((ConstantsDto.TRANSFER_TYPE_EXPORT.equals(logType))) {
                fftypeList = Arrays.asList(ConstantsDto.SELF_EXPORT, ConstantsDto.APPROVE_EXPORT);
            } else if ((ConstantsDto.TRANSFER_TYPE_OUT.equals(logType))) {
                fftypeList = Arrays.asList(ConstantsDto.SELF_OUT, ConstantsDto.APPROVE_OUT);
            }

            if (fftypeList != null && fftypeList.size() > 0) {
                for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                    criteria.andFftypeIn(fftypeList);
                }
            }
        }

        //解析查询的开始时间和结束时间
        Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);
        if (timeMap != null && timeMap.size() > 1) {
            //使用通用的方法解析查询的开始时间和结束时间
            Date startDateTime = timeMap.get("startDateTime");
            Date endDateTime = timeMap.get("endDateTime");

            if (startDateTime != null && endDateTime != null) {
                for (VideoTransferLogDOCriteria.Criteria criteria : criteriaList) {
                    criteria.andTransferTimeBetween(startDateTime, endDateTime);
                }
            }
        }

        example.setOrderByClause("transfer_time DESC");

        return example;
    }

    @Override
    public VideoTransferLogDOCriteria generatorVideoTransferLogExample(Date startTime, Date endTime) {
        VideoTransferLogDOCriteria example = new VideoTransferLogDOCriteria();
        example.createCriteria().andTransferTimeBetween(startTime, endTime);
        return example;
    }

    @Override
    public Map<String, String> collectVideoTransferLogsWithAttachment(String start, String end, List<ChildNodeDO> childNodeList) {
        Map<String, String> result = new HashMap<>();
        childNodeList.stream().forEach(childNode -> {
            String nodeName = childNode.getNodeName();
            try {
                logger.info("检查服务器状态！");
                //封装请求路径和参数
                String nodeIp = childNode.getNodeIp();
                if (!HttpClientUtil.isConnect(nodeIp)) {
                    result.put(nodeName, "无法连接服务器！");
                    return;
                }
                logger.info("服务器状态可用！");

                Map<String, String> param = new HashMap<>();
                param.put("start", start);
                param.put("end", end);

                //查询带采集视频总数
                String countStr = HttpClientUtil.doPost(ConstantsDto.HTTP + nodeIp + countUrl, param);
                ResultMsg countRes = ResultMsg.format(countStr);
                if (countRes == null || countRes.getResultCode() != 1) {
                    result.put(nodeName, "数据解析失败或对应IP不存在日志服务！");
                    return;
                }

                //分批采集日志，每批采集1W条，重试次数为5
                Integer count = (Integer) countRes.getData();
                if (count > 0) {
                    for (int i = 0; i <= count / 10000; i++) {
                        //添加分页参数
                        param.put("page", String.valueOf(i + 1));

                        int retry = 0;
                        boolean res = false;
                        while (res == false && retry < 5) {
                            res = this.executeCollectLogs(nodeIp, nodeName, param);
                            retry++;
                        }

                        if (!res) {
                            logger.info("子级节点:" + nodeName + "第 " + (i + 1) + "次采集日志失败!");
                        }
                    }
                    result.put(nodeName, "完成子级节点日志采集!");
                } else {
                    result.put(nodeName, "完成子级节点日志采集!");
                }
                logger.info("子级节点:" + nodeName + "收集方法完成");
            } catch (Exception e) {
                result.put(nodeName, "子级节点日志采集错误!");
                logger.info("子级节点:" + nodeName + "收集方法错误！");
            }
        });

        logger.info("全部子级服务器采集日志完成！");
        return result;
    }

    protected boolean executeCollectLogs(String nodeIp, String nodeName, Map<String, String> param) {
        logger.info("发送POST请求！地址为：" + ConstantsDto.HTTP + nodeIp + url);
        //执行HTTP的POST请求，并获取返回值
        String resStr = HttpClientUtil.doPost(ConstantsDto.HTTP + nodeIp + url, param);
        logger.info("收到POST响应！");

        logger.info("开始解析数据");
        //将返回值转化为ResultMsg
        ResultMsg resultMsg = ResultMsg.formatToList(resStr, VideoTransferLogDO.class);
        if (resultMsg == null) {
            logger.info("数据解析失败或对应IP不存在日志服务！");
            return false;
        }
        logger.info("解析数据成功！");

        //返回值正常，则进行入库操作
        if (ConstantsDto.RESULT_CODE_TRUE == resultMsg.getResultCode().intValue()) {
            Object data = resultMsg.getData();
            if (data instanceof List) {
                List<VideoTransferLogDO> videoTransferLogList = (List<VideoTransferLogDO>) data;
                logger.info("总共解析" + videoTransferLogList.size() + "条数据，开始剔除数据！");
                //校验返回的数据，是否已经存在数据中，如果存在，将其从数据中剔除
                this.removeExistvideoTransferLogsFromList(videoTransferLogList, nodeIp);
                logger.info("完成数据剔除!剩余总共" + videoTransferLogList.size() + "条数据！");

                logger.info("开始更新日志!");
                //更新日志
                this.updateForCollectingVideoTransferLogs(nodeIp, nodeName, videoTransferLogList);
                logger.info("完成更新日志");

                if (videoTransferLogList != null && videoTransferLogList.size() > 0) {
                    logger.info("开始传输附件");
                    //采集日志对应的附件文件，无论附件是否上传成功，日志记录都应该上传。
                    videoTransferLogList.stream().forEach(videoTransferLogDO -> this.copyAttachmentForCollecting(nodeIp, videoTransferLogDO));
                    logger.info("完成附件传输");

                    //查询采集的日志记录
                    videoTransferLogList.stream().forEach(videoTransferLogDO -> mapper.insertSelective(videoTransferLogDO));
                }
                return true;
            }
        }
        return false;

    }

    protected void copyAttachmentForCollecting(String childNodeIp, VideoTransferLogDO videoTransferLogDO) {
        String source;
        String destination;
        try {
            //获取源文件路劲
            String filePath = videoTransferLogDO.getFilePath();
            if (StringUtils.isEmpty(filePath)) {
            }
            source = ConstantsDto.HTTP + childNodeIp + "/" + ConstantsDto.PROCJECT_IDENTIFICATION + filePath;

            //跟新附件的保存路径，并拼接新路径
            filePath = filePath.replace(ConstantsDto.CURRENT_NODE, childNodeIp);
            videoTransferLogDO.setFilePath(filePath);
            destination = PathConfig.VIDEOTRANSFERLOG_ROOTPATH + filePath;
            FileUtils.copyURLToFile(new URL(source), new File(destination), 5000, 200000);
        } catch (IOException e) {
            videoTransferLogDO.setFilePath(null);
        }
    }

    protected void updateForCollectingVideoTransferLogs(String nodeIp, String nodeName, List<VideoTransferLogDO> videoTransferLogList) {
        for (VideoTransferLogDO videoTransferLog : videoTransferLogList) {
            //置空id，避免重复
            videoTransferLog.setId(null);

            Integer nodeLevel = videoTransferLog.getNodeLevel();
            //递增日志层级
            videoTransferLog.setNodeLevel(nodeLevel + 1);
            //判断采集的日志为下级节点本身日志，则为其设置节点IP和名称
            if (ConstantsDto.CURRENT_NODE_LEVEL.equals(nodeLevel)) {
                videoTransferLog.setNodeIp(nodeIp);
                videoTransferLog.setNodeName(nodeName);
            }
        }
    }

    @Override
    public void collectVideoTransferLogsWithAttachment(String taskguid) {
        //开始日志采集定时任务
        ScheduledTaskDOCriteria taskExample = new ScheduledTaskDOCriteria();
        taskExample.createCriteria().andGuidEqualTo(taskguid);
        List<ScheduledTaskDO> scheduledTaskDOS = scheduledTaskDOMapper.selectByExample(taskExample);
        if (scheduledTaskDOS == null || scheduledTaskDOS.size() == 0) {
            logger.info("任务号为：" + taskguid + ",该任务不存在！");
            return;
        }
        ScheduledTaskDO scheduledTask = scheduledTaskDOS.get(0);
        //更新任务的执行状态
        scheduledTask.setProcessing(ConstantsDto.CONST_TRUE);
        scheduledTaskDOMapper.updateByPrimaryKey(scheduledTask);

        Date lastExecutionTime = scheduledTask.getLastExecutionTime();
        if (lastExecutionTime == null) {
            //若定时任务第一次启动，采集其实时间设为默认其实时间1970年
            lastExecutionTime = new Date(0);
        }

        Map<String, String> result;
        //确定采集的时间范围，以上次定时任务时间执行为开始，以当前时间为结束。
        String start;
        try {
            start = DateUtil.format(lastExecutionTime, DateUtil.FMT_DATE);
            String end = DateUtil.getFormatDate();

            //获取服务器节点信息
            ChildNodeDOCriteria childNodeExample = new ChildNodeDOCriteria();
            List<ChildNodeDO> childNodeList = childNodeDOMapper.selectByExample(childNodeExample);

            if (childNodeList == null || childNodeList.size() == 0) {
                logger.info("任务号为：" + taskguid + ",日志采集节点为空！");
                return;
            }
            logger.info("开始时间：" + start);
            logger.info("结束时间：" + end);
            for (ChildNodeDO nodeDO : childNodeList) {
                logger.info("节点信息：" + nodeDO);
            }

            result = this.collectVideoTransferLogsWithAttachment(start, end, childNodeList);

        } catch (ParseException e) {
            logger.info("任务号为：" + taskguid + ",设定开始时间失败！");
            e.printStackTrace();
            return;
        } catch (Exception e) {
            logger.info("任务号为：" + taskguid + ",任务执行失败！");
            e.printStackTrace();
            return;
        }

        if (result != null && result.size() > 0) {
            logger.info("定时任务执行结果为：");
            result.entrySet().stream().forEach(entry -> logger.info("子节点名称：" + entry.getKey() + "，执行结果：" + entry.getValue()));

        }
    }

    @Override
    public void removeExistvideoTransferLogsFromList(List<VideoTransferLogDO> videoTransferLogList, String nodeIp) {
        //当前集合日志的guid集合
        List<String> guidList = videoTransferLogList.stream().map(VideoTransferLogDO::getGuid).collect(Collectors.toList());
        VideoTransferLogDOCriteria example = new VideoTransferLogDOCriteria();
        VideoTransferLogDOCriteria.Criteria criteria = example.createCriteria();
        criteria.andGuidIn(guidList);

        //设置节点添加，为空或null则不设置
//        if (!StringUtils.isEmpty(nodeIp)) {
//            criteria.andNodeIpEqualTo(nodeIp);
//        }

        //根据guid，将已存在的日志从集合总剔除
        List<VideoTransferLogDO> existList = this.listBy(example);
        if (existList != null && existList.size() > 0) {
            List<String> existGuidList = existList.stream().map(VideoTransferLogDO::getGuid).collect(Collectors.toList());
            videoTransferLogList.removeIf(videoTransferLogDO -> existGuidList.contains(videoTransferLogDO.getGuid()));
        }
    }

    @Override
    public int countVideoTransferLogsForCollecting(VideoTransferLogDOCriteria example) {
        return (int) mapper.countByExample(example);
    }
}