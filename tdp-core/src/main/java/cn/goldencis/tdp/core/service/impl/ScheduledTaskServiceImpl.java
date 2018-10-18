package cn.goldencis.tdp.core.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.ScheduledTaskDOMapper;
import cn.goldencis.tdp.core.entity.ScheduledTaskDO;
import cn.goldencis.tdp.core.entity.ScheduledTaskDOCriteria;
import cn.goldencis.tdp.core.scheduledtask.DynamicScheduledTask;
import cn.goldencis.tdp.core.service.IScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by limingchao on 2018/8/2.
 */
@Service
public class ScheduledTaskServiceImpl extends AbstractBaseServiceImpl<ScheduledTaskDO, ScheduledTaskDOCriteria> implements IScheduledTaskService {

    @Autowired
    private ScheduledTaskDOMapper mapper;

    @Autowired
    private DynamicScheduledTask scheduledTask;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    protected BaseDao<ScheduledTaskDO, ScheduledTaskDOCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    public void init() {
        taskExecutor.execute(this::loadPersistentScheduledTask);
    }

    public void loadPersistentScheduledTask() {
        while (scheduledTask.countExecutableTaskSize() < 1 || scheduledTask.getScheduledTaskRegistrar() == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
            }
        }

        List<ScheduledTaskDO> scheduledTaskList = getDao().selectByExample(new ScheduledTaskDOCriteria());

        if (scheduledTaskList != null && scheduledTaskList.size() > 0) {
            List<Map<String, String>> taskMapList = this.formatScheduledTaskListToTashMap(scheduledTaskList);
            scheduledTask.addTriggerTaskList(taskMapList);
        }
    }

    @Override
    public ScheduledTaskDO addScheduledTask(String taskGuid, String taskReference, String cron) {
        //将该定时任务持久化
        ScheduledTaskDO scheduledTask = new ScheduledTaskDO();
        scheduledTask.setGuid(taskGuid);
        scheduledTask.setTaskReference(taskReference);
        scheduledTask.setCorn(cron);
        scheduledTask.setProcessing(ConstantsDto.CONST_FALSE);
        scheduledTask.setTaskSwitch(ConstantsDto.CONST_FALSE);
        scheduledTask.setLastExecutionTime(null);
        scheduledTask.setCreateTime(new Date());
        this.createSelective(scheduledTask);

        return scheduledTask;
    }

    @Override
    public List<Map<String, String>> formatScheduledTaskListToTashMap(List<ScheduledTaskDO> scheduledTaskList) {
        List<Map<String, String>> taskMapList = new ArrayList<>();

        //转化时，保留必要参数
        scheduledTaskList.stream().forEach(scheduledTaskDO -> {
            Map<String, String> taskMap = new HashMap<>();
            taskMap.put("taskGuid", scheduledTaskDO.getGuid());
            taskMap.put("taskReference", scheduledTaskDO.getTaskReference());
            taskMap.put("cron", scheduledTaskDO.getCorn());
            taskMapList.add(taskMap);
        });

        return taskMapList;
    }
}
