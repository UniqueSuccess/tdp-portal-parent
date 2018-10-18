package cn.goldencis.tdp.core.scheduledtask;

import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by limingchao on 2018/8/6.
 */
public class DynamicScheduledMethodRunnable extends ScheduledMethodRunnable {

    private String taskguid;

    public DynamicScheduledMethodRunnable(Object target, Method method, String taskguid) {
        super(target, method);
        this.taskguid = taskguid;
    }

    public DynamicScheduledMethodRunnable(Object target, String methodName, String taskguid) throws NoSuchMethodException {
        super(target, methodName);
        this.taskguid = taskguid;
    }

    public String getTaskguid() {
        return taskguid;
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(super.getMethod());
            super.getMethod().invoke(getTarget(), taskguid);
        }
        catch (InvocationTargetException ex) {
            ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
        }
        catch (IllegalAccessException ex) {
            throw new UndeclaredThrowableException(ex);
        }
    }
}
