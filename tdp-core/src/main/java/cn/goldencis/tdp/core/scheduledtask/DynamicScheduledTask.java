package cn.goldencis.tdp.core.scheduledtask;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ScheduledTaskDO;
import cn.goldencis.tdp.core.entity.ScheduledTaskDOCriteria;
import cn.goldencis.tdp.core.service.IScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by limingchao on 2018/7/30.
 */
@Component
@EnableScheduling
public class DynamicScheduledTask implements SchedulingConfigurer {

    private static ScheduledTaskRegistrar scheduledTaskRegistrar;

    private TaskScheduler taskScheduler;

    private ScheduledExecutorService localExecutor;

    private final Set<ScheduledFuture<?>> scheduledFutures = new LinkedHashSet<ScheduledFuture<?>>();

    private List<ExecutableTask> executableTaskList = new ArrayList();

    private Map<String, String> cornMap = new HashMap<>();

    @Autowired
    private IScheduledTaskService scheduledTaskService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.localExecutor = Executors.newScheduledThreadPool(5);
        taskScheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
    }

    public boolean addCronTask(Integer index, String taskReference, String cron) {
        try {
            ArrayList<CronTask> cronTasks = new ArrayList<>();

            ExecutableTask executableTask = this.executableTaskList.get(index - 1);
            if (executableTask.getTaskReference().equals(taskReference)) {
                Runnable runnable = new ScheduledMethodRunnable(executableTask.getTarget(), executableTask.getMethod());

                String taskGuid = UUID.randomUUID().toString();
                cornMap.put(taskGuid, cron);

                cronTasks.add(new CronTask(runnable, cornMap.get(taskGuid)));

                scheduledTaskRegistrar.setCronTasksList(cronTasks);

                this.reflush();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addTriggerTask(String taskGuid, String taskReference, String cron) {
        try {
            ArrayList<TriggerTask> triggerTasks = new ArrayList<>();

            //获取可执行的任务并比对唯一标示任务引用
            //寻找匹配的可执行任务
            executableTaskList.stream().filter(executableTask -> executableTask.getTaskReference().equals(taskReference) ? true : false).findFirst().ifPresent(executableTask -> {
                Runnable runnable = new DynamicScheduledMethodRunnable(executableTask.getTarget(), executableTask.getMethod(), taskGuid);

                //将corn放入map中，可以动态的修改和调用。
                cornMap.put(taskGuid, cron);

                //创建动态触发器
                Trigger trigger = this.formatCronStrToTrigger(taskGuid);
                //创建触发任务
                TriggerTask triggerTask = new TriggerTask(runnable, trigger);
                //添加到触发任务列表中
                triggerTasks.add(triggerTask);
                scheduledTaskRegistrar.setTriggerTasksList(triggerTasks);

            });

            //注册器刷新设置，加载新的定时任务。
            this.reflush();
            return true;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addTriggerTaskList(List<Map<String, String>> taskMapList) {
        try {
            if (taskMapList != null && taskMapList.size() > 0) {
                ArrayList<TriggerTask> triggerTasks = new ArrayList<>();
                taskMapList.stream().forEach(taskMap -> {
                    String taskGuid = taskMap.get("taskGuid");
                    String taskReference = taskMap.get("taskReference");
                    String cron = taskMap.get("cron");

                    //寻找匹配的可执行任务
                    executableTaskList.stream().filter(executableTask -> executableTask.getTaskReference().equals(taskReference) ? true : false).findFirst().ifPresent(executableTask -> {
                        Runnable runnable = new DynamicScheduledMethodRunnable(executableTask.getTarget(), executableTask.getMethod(), taskGuid);

                        //将corn放入map中，可以动态的修改和调用。
                        cornMap.put(taskGuid, cron);

                        //创建动态触发器
                        Trigger trigger = this.formatCronStrToTrigger(taskGuid);
                        //创建触发任务
                        TriggerTask triggerTask = new TriggerTask(runnable, trigger);
                        //添加到触发任务列表中
                        triggerTasks.add(triggerTask);
                    });

                });

                scheduledTaskRegistrar.setTriggerTasksList(triggerTasks);
                //注册器刷新设置，加载新的定时任务。
                this.reflush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setScheduledTaskTrigger(String guid, String cron) {
        if (cornMap.containsKey(guid)) {
            cornMap.put(guid, cron);
            return true;
        }

        return false;
    }

    public ScheduledTaskRegistrar getScheduledTaskRegistrar() {
        return scheduledTaskRegistrar;
    }

    public void reflush() {
        this.scheduledTaskRegistrar.afterPropertiesSet();
    }

    public Trigger formatCronStrToTrigger(String taskGuid) {

        Trigger trigger = triggerContext -> {
            //动态调用的本质在于，每次设置时间，都回去map中获取最新的corn值
            CronTrigger cronTrigger = new CronTrigger(cornMap.get(taskGuid));
            Date nextExecDate = cronTrigger.nextExecutionTime(triggerContext);

            //完成本次定时任务后，会设定下一次的任务时间，同时将本次准确开始时间设置为任务对象的完成时间
            ScheduledTaskDOCriteria example = new ScheduledTaskDOCriteria();
            example.createCriteria().andGuidEqualTo(taskGuid);
            ScheduledTaskDO scheduledTask = scheduledTaskService.getBy(example);
            if (scheduledTask != null) {
                scheduledTask.setProcessing(ConstantsDto.CONST_FALSE);
                if (!StringUtils.isEmpty(triggerContext.lastActualExecutionTime())) {
                    scheduledTask.setLastExecutionTime(triggerContext.lastActualExecutionTime());
                }
                scheduledTaskService.update(scheduledTask);
            }
            return nextExecDate;
        };

        return trigger;
    }

    public void addExecutableTask(ExecutableTask executableTask) {
        executableTask.setTaskIndex(executableTaskList.size() + 1);
        this.executableTaskList.add(executableTask);
    }

    public int countExecutableTaskSize() {
        return executableTaskList.size();
    }

    /**
     * 销毁当前所有任务，再从数据库中重新加载所有持久化任务
     *
     * @param scheduledTaskList
     */
    public void reloadScheduledTask(List<Map<String, String>> scheduledTaskList) {
        scheduledTaskRegistrar.destroy();
        this.addTriggerTaskList(scheduledTaskList);
    }

    public static void destroy() {
        if (scheduledTaskRegistrar != null) {
            scheduledTaskRegistrar.destroy();
        }
    }
}
