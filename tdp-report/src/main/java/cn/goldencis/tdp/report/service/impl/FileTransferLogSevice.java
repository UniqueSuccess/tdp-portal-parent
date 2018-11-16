package cn.goldencis.tdp.report.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.ListUtils;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ChildNodeDOCriteria;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.report.entity.TFileTransferLogCriteria;
import cn.goldencis.tdp.report.entity.VideoTransferLogDOCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.goldencis.tdp.report.dao.TFileTransferLogMapper;
import cn.goldencis.tdp.report.entity.TFileTransferLog;
import cn.goldencis.tdp.report.service.IFileTransferLogSevice;

import org.springframework.util.StringUtils;

@Service
public class FileTransferLogSevice implements IFileTransferLogSevice {
    @Autowired
    private TFileTransferLogMapper mapper;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public List<TFileTransferLog> queryFileTransferLog(Map<String, Object> params) {
        return mapper.queryFileTransferLog(params);
    }

    @Override
    public Integer queryFileTransferLogCount(Map<String, Object> params) {
        return mapper.queryFileTransferLogCount(params);
    }

    @Override
    public Long getFileTypeCount(String type) {
        TFileTransferLogCriteria criteria = new TFileTransferLogCriteria();
        criteria.createCriteria().andFileTypeEqualTo(type);
        long l = mapper.countByExample(criteria);
        return l;
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
    public int countFileTransferLog(List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order) {
        TFileTransferLogCriteria example = this.generatorFileTransferLogExample(departmentList, nodeType, logType, submitDate, startDate, endDate, order);
        long count = mapper.countByExample(example);
        return (int) count;
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
    public List<TFileTransferLog> getFileTransferLogListByParams(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        //设置查询参数
        TFileTransferLogCriteria example = this.generatorFileTransferLogExample(departmentList, ConstantsDto.CURRENT_NODE, logType, submitDate, startDate, endDate, order);

        return mapper.selectByExample(example);
    }


    /**
     * 将获取视频流转日志列表转化为按时间分类的集合
     *
     * @return
     */
    @Override
    public List<Integer> getFileTransferLogInHours(List<TFileTransferLog> fileTransferLogList) {
        List<Integer> hoursCount = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursCount.add(0);
        }
        Calendar calendar = Calendar.getInstance();
        for (TFileTransferLog videoLog : fileTransferLogList) {
            calendar.setTime(videoLog.getTransferTime());
            int i = calendar.get(Calendar.HOUR_OF_DAY);
            hoursCount.set(i, hoursCount.get(i) + 1);
        }
        return hoursCount;
    }

    /**
     * 将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
     *
     * @param fileTransferLogList
     * @param startDate
     * @param endDate              @return
     */
    @Override
    public Map<String, Object> getFileTransferLogInDate(List<TFileTransferLog> fileTransferLogList, String startDate, String endDate) throws ParseException {
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
        for (TFileTransferLog fileTransferLog : fileTransferLogList) {
            Date transferTime = fileTransferLog.getTransferTime();
            String format = sdf.format(transferTime);
            index = dateList.indexOf(format);
            countList.set(index, countList.get(index) + 1);
        }

        resMap.put("dateList", dateList);
        resMap.put("countList", countList);
        return resMap;
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
    public TFileTransferLogCriteria generatorFileTransferLogExample(List<DepartmentDO> departmentList, String nodeType, String logType, String submitDate, String startDate, String endDate, String order) {
        TFileTransferLogCriteria example = new TFileTransferLogCriteria();
        //设置查询条件
        List<TFileTransferLogCriteria.Criteria> criteriaList = new ArrayList<>();
        if (!StringUtil.isEmpty(order)) {
            criteriaList.add(example.or().andFileNameLike("%" + order + "%"));
            criteriaList.add(example.or().andReceiverLikeInsensitive("%" + order + "%"));
        } else {
            criteriaList.add(example.createCriteria());
        }

        //设置部门查询条件
        if (departmentList != null && departmentList.size() > 0) {
            List<Integer> idList = new ArrayList<>();
            for (DepartmentDO department : departmentList) {
                idList.add(department.getId());
            }
            for (TFileTransferLogCriteria.Criteria criteria : criteriaList) {
                criteria.andDepartmentIdIn(idList);
            }
        }

        //添加流转类型查询
        if (!StringUtils.isEmpty(logType)) {
            List<Integer> fftypeList = null;

            if ((ConstantsDto.TRANSFER_TYPE_EXPORT.equals(logType))) {
                fftypeList = Arrays.asList(ConstantsDto.SECRET_EXPORT, ConstantsDto.APPROVE_EXPORT);
            } else if ((ConstantsDto.TRANSFER_TYPE_OUT.equals(logType))) {
                fftypeList = Arrays.asList(ConstantsDto.SELF_OUT, ConstantsDto.APPROVE_OUT);
            }

            if (fftypeList != null && fftypeList.size() > 0) {
                for (TFileTransferLogCriteria.Criteria criteria : criteriaList) {
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
                for (TFileTransferLogCriteria.Criteria criteria : criteriaList) {
                    criteria.andTransferTimeBetween(startDateTime, endDateTime);
                }
            }
        }

        example.setOrderByClause("transfer_time DESC");

        return example;
    }

    @Transactional
    @Override
    public void deleteFileTransferLog(Date clearDate) {
        TFileTransferLogCriteria example = new TFileTransferLogCriteria();
        example.createCriteria().andTransferTimeLessThan(clearDate);
        List<TFileTransferLog> list = mapper.selectByExample(example);
        mapper.deleteByExample(example);
        taskExecutor.execute(() -> {
            if (!ListUtils.isEmpty(list)) {
                list.stream().filter(log -> !StringUtil.isEmpty(log.getFilePath()) && log.getFilePath().startsWith(PathConfig.VIDEOTRANSFERLOG_ROOTPATH)).forEach(log -> {
                  //判断日志文件是否存在，存在则删除
                    File file = new File(log.getFilePath());
                    if (file.exists()) {
                        try {
                            file.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
