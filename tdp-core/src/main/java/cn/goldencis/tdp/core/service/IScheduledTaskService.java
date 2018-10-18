package cn.goldencis.tdp.core.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.ScheduledTaskDO;
import cn.goldencis.tdp.core.entity.ScheduledTaskDOCriteria;

import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/8/2.
 */
public interface IScheduledTaskService extends BaseService<ScheduledTaskDO, ScheduledTaskDOCriteria> {

    /**
     * 新建定时任务
     * @param taskGuid 定时任务的guid
     * @param taskReference 方法引用
     * @param cron 表达式
     */
    ScheduledTaskDO addScheduledTask(String taskGuid, String taskReference, String cron);

    /**
     * 将定时任务的持久化集合，转化为任务参数的Map集合
     * @param scheduledTaskList 定时任务的持久化集合
     * @return 包含任务参数的Map集合
     */
    List<Map<String, String>> formatScheduledTaskListToTashMap(List<ScheduledTaskDO> scheduledTaskList);
}
