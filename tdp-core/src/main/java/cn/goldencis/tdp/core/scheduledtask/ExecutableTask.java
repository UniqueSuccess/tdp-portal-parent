package cn.goldencis.tdp.core.scheduledtask;

import java.lang.reflect.Method;

/**
 * 可执行的任务  实体类
 * Created by limingchao on 2018/8/1.
 */
public class ExecutableTask {

    //任务索引
    private Integer taskIndex;

    //任务名称
    private String taskName;

    //执行定时任务的bean实例，一般是service的实现类
    private Object target;

    //定时任务需要调用的方法
    private Method method;

    //任务具体执行方法的引用，可以作为唯一标示
    private String taskReference;

    public ExecutableTask() {
    }

    public ExecutableTask(String taskName, Object target, Method method, String taskReference) {
        this.taskName = taskName;
        this.target = target;
        this.method = method;
        this.taskReference = taskReference;
    }

    public Integer getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(Integer taskIndex) {
        this.taskIndex = taskIndex;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getTaskReference() {
        return taskReference;
    }

    public void setTaskReference(String taskReference) {
        this.taskReference = taskReference;
    }
}
