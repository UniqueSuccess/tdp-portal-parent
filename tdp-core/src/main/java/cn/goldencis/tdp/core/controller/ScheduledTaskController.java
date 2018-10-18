package cn.goldencis.tdp.core.controller;

import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.ScheduledTaskDO;
import cn.goldencis.tdp.core.entity.ScheduledTaskDOCriteria;
import cn.goldencis.tdp.core.scheduledtask.DynamicScheduledTask;
import cn.goldencis.tdp.core.service.IScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by limingchao on 2018/8/2.
 */
@Controller
@RequestMapping(value = "/scheduledTask")
public class ScheduledTaskController {

    @Autowired
    private DynamicScheduledTask register;

    @Autowired
    private IScheduledTaskService scheduledTaskService;

    /**
     * 根据任务id，获取定时任务的接口
     * @param id 任务id
     * @return 定时任务对象
     */
    @ResponseBody
    @RequestMapping(value = "/scheduledTask/{id}", method = RequestMethod.GET)
    public ResultMsg getScheduledTask(@PathVariable(value = "id") Integer id) {
        try {
            ScheduledTaskDO scheduledTask = scheduledTaskService.getByPrimaryKey(id);
            String period = DateUtil.formatCronPeriod(scheduledTask.getCorn());
            String time = DateUtil.formatCronTime(scheduledTask.getCorn());
            scheduledTask.setPeriod(period);
            scheduledTask.setTime(time);
            return ResultMsg.ok(scheduledTask);
        } catch (Exception e) {
            return ResultMsg.build(ConstantsDto.RESULT_CODE_ERROR, "获取定时任务错误！", e);
        }
    }

    /**
     * 添加定时任务接口
     * @param taskReference 任务引用
     * @param collectPeriod 采集周期
     * @param collectTime   采集具体时间
     */
    @ResponseBody
    @RequestMapping(value = "/triggerTask", method = RequestMethod.POST)
    public ResultMsg addTriggerTask(String taskReference, String collectPeriod, String collectTime) {
        try {
            //解析cron表达式
            String cron = DateUtil.parseCron(collectPeriod, collectTime);
            if (StringUtils.isEmpty(cron)) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "解析cron表达式失败！");
            }

            //创建定时任务的唯一标示guid
            String taskGuid = UUID.randomUUID().toString();

            //持久化定时任务
            scheduledTaskService.addScheduledTask(taskGuid, taskReference, cron);
        } catch (Exception e) {
            return ResultMsg.build(ConstantsDto.RESULT_CODE_ERROR, "添加触发类定时任务错误！", e);
        }

        return ResultMsg.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/turnScheduledTaskSwitch", method = RequestMethod.PUT)
    public ResultMsg turnScheduledTaskSwitch(Integer id, Integer taskSwitch) {
        try {
            if (taskSwitch != ConstantsDto.CONST_FALSE && taskSwitch != ConstantsDto.CONST_TRUE) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "定时任务的开关参数错误！");
            }
            ScheduledTaskDO scheduledTask = scheduledTaskService.getByPrimaryKey(id);
            if (scheduledTask == null) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "未找到对应的任务！");
            } else if (scheduledTask.getTaskSwitch().equals(taskSwitch)) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "该定时任务已经切换到" + (ConstantsDto.CONST_TRUE.equals(taskSwitch) ? "打开" : "关闭") + "状态！");
            }

            //开启或者关闭定时任务
            if (ConstantsDto.CONST_TRUE.equals(taskSwitch)) {
                //添加触发类定时任务
                boolean flag = register.addTriggerTask(scheduledTask.getGuid(), scheduledTask.getTaskReference(), scheduledTask.getCorn());
                if (!flag) {
                    return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "添加触发类定时任务失败！");
                }
                //更新定时任务的持久化对象
                scheduledTask.setTaskSwitch(taskSwitch);
                scheduledTaskService.update(scheduledTask);
            } else {
                //关闭定时任务
                //首先更新定时任务的持久化对象
                scheduledTask.setTaskSwitch(taskSwitch);
                scheduledTaskService.update(scheduledTask);

                //销毁当前所有定时任务，再从数据库中重新加载所有持久化任务
                ScheduledTaskDOCriteria example = new ScheduledTaskDOCriteria();
                example.createCriteria().andTaskSwitchEqualTo(ConstantsDto.CONST_TRUE);
                List<ScheduledTaskDO> scheduledTaskList = scheduledTaskService.listBy(example);
                if (scheduledTaskList != null && scheduledTaskList.size() > 0) {
                    List<Map<String, String>> taskMaps = scheduledTaskService.formatScheduledTaskListToTashMap(scheduledTaskList);
                    register.reloadScheduledTask(taskMaps);
                }
            }

            return ResultMsg.ok();
        } catch (Exception e) {
            return ResultMsg.build(ConstantsDto.RESULT_CODE_ERROR, "切换定时任务状态错误！", e);
        }
    }

    /**
     * 修改定时任务接口
     *
     * @param scheduledTaskDO 需要修改的定时任务，仅传id就可以
     * @param collectPeriod   采集周期
     * @param collectTime     采集具体时间
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/triggerTask", method = RequestMethod.PUT)
    public ResultMsg updateTriggerTask(ScheduledTaskDO scheduledTaskDO, String collectPeriod, String collectTime) {
        try {
            //解析cron表达式
            String cron = DateUtil.parseCron(collectPeriod, collectTime);
            if (StringUtils.isEmpty(cron)) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "解析cron表达式失败！");
            }

            scheduledTaskDO.setCorn(cron);
            scheduledTaskService.update(scheduledTaskDO);
//            register.setScheduledTaskTrigger(scheduledTaskDO.getGuid(), scheduledTaskDO.getCorn());

            //销毁当前所有定时任务，再从数据库中重新加载所有持久化任务
            ScheduledTaskDOCriteria example = new ScheduledTaskDOCriteria();
            example.createCriteria().andTaskSwitchEqualTo(ConstantsDto.CONST_TRUE);
            List<ScheduledTaskDO> scheduledTaskList = scheduledTaskService.listBy(example);
            if (scheduledTaskList != null && scheduledTaskList.size() > 0) {
                List<Map<String, String>> taskMaps = scheduledTaskService.formatScheduledTaskListToTashMap(scheduledTaskList);
                register.reloadScheduledTask(taskMaps);
            }
            return ResultMsg.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.build(ConstantsDto.RESULT_CODE_ERROR, "修改触发类定时任务错误！", e);
        }
    }

    @ModelAttribute
    public void getModel(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            ScheduledTaskDO scheduledTaskDO = scheduledTaskService.getByPrimaryKey(id);
            map.put("scheduledTaskDO", scheduledTaskDO);
        }
    }
}
