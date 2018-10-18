package cn.goldencis.tdp.core.annotation;

import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 日志AOP
 */
@Component
@Aspect
public class LogAopAdviseDefine {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IOperationLogService logService;

    @Pointcut("@annotation(cn.goldencis.tdp.core.annotation.PageLog)")
    public void pointcut() {
    }

    @AfterReturning(pointcut = "pointcut()", returning = "retVal")
    public void logMethodInvokeResult(JoinPoint joinPoint, Object retVal) {
        boolean success = true;
        String errorMsg = null;
        if (retVal instanceof ResultMsg) {
            ResultMsg resultMsg = (ResultMsg) retVal;
            if (ConstantsDto.RESULT_CODE_TRUE != resultMsg.getResultCode()) {
                success = false;
                errorMsg = resultMsg.getResultMsg();
            }
        }
        generatePageLog(joinPoint, success, errorMsg);
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "exception")
    public void logMethodInvokeException(JoinPoint joinPoint, Exception exception) {
        generatePageLog(joinPoint, false, exception.getMessage());
    }

    private void generatePageLog(JoinPoint joinPoint, boolean success, String errorMsg) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            PageLog pageLog = method.getAnnotation(PageLog.class);
            // 排除登录成功的情况，日志记录移植到登录业务内部
            if (success && pageLog.type() == LogType.LOGIN) {
                return;
            }
            String template = pageLog.template();
            String[] args = pageLog.args().split(",");
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                Object value = invokeValue(joinPoint, arg);
                args[i] = String.valueOf(value);
            }
            String userName = null;
            String userCname = null;
            if (pageLog.type() != LogType.LOGIN) {
                UserDO user = GetLoginUser.getLoginUser();
                userName = user.getUserName();
                userCname = user.getName();
            }
            HttpServletRequest request = SysContext.getRequest();
            String ip = NetworkUtil.getIpAddress(request);
            Date dateTime = new Date();
            String logOperateParam = String.format(template, args);
            String module;
            if (joinPoint.getTarget().getClass().getAnnotation(PageLog.class) != null) {
                module = joinPoint.getTarget().getClass().getAnnotation(PageLog.class).module();
            } else {
                module = pageLog.module();

            }
            String operation = pageLog.module();
            String detail;
            String userCnameStr = "";
            if (userCname != null) {
                userCnameStr = String.format("【%s】在", userCname);
            }
            if (success) {
                detail = String.format("%s【%s】中执行【%s】操作成功：【%s】", userCnameStr, module, operation, logOperateParam);
            } else {
                detail = String.format("%s【%s】中执行【%s】操作失败：【%s】，错误信息为【%s】", userCnameStr, module, operation,
                        logOperateParam, errorMsg);
            }
            OperationLogDO log = new OperationLogDO();
            log.setIp(ip);
            log.setLogOperateParam(detail);
            log.setLogPage(module);
            log.setTime(dateTime);
            log.setUserName(userName);
            log.setLogType(pageLog.type().value());
            log.setLogDesc(String.format("%s invoke", joinPoint.getSignature().toShortString()));
            logService.create(log);
            logger.info("{}--{}-{}---user is {}", module, operation, logOperateParam, userName);
        } catch (Exception ex) {
            logger.error("create pageLog error,message is {}", ex.getMessage());
        }
    }

    private Object invokeValue(JoinPoint joinPoint, String arg)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String[] parts = arg.split("\\.");
        int index = Integer.parseInt(parts[0]);
        Object value = joinPoint.getArgs()[index];
        if (parts.length > 1) {
            String field = parts[1];
            try{
                Method getMethod = value.getClass()
                        .getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                value = getMethod.invoke(value);
            }catch(NoSuchMethodException exception){
                Method getMethod = value.getClass().getMethod(field);
                value = getMethod.invoke(value);
            }
        }
        return value;
    }
}
