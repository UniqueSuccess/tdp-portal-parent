package cn.goldencis.tdp.core.annotation;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cn.goldencis.tdp.core.entity.ResultMsg;

/**
 * 
 * @author Administrator
 * 之前接口返回 datatable数据进行转换
 */
@Aspect
@Component
public class ResultMsgAspect {
    @Around("execution(cn.goldencis.tdp.core.entity.ResultMsg cn.goldencis.tdp.*.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result = pjp.proceed();
        if (result instanceof ResultMsg) {
            ResultMsg resultMsg = (ResultMsg)result;
            if ((resultMsg.getRecordsTotal() != null && resultMsg.getExportstart() != null && resultMsg.getExportlength() != null) && resultMsg.getData() != null) {
                //将之前接口返回数据进行转换
                resultMsg.setRows(resultMsg.getData());
                resultMsg.setTotal(resultMsg.getRecordsTotal());
                return resultMsg;
            }
        }
        return result;
    }

    @Around("execution(java.util.Map<String, Object> cn.goldencis.tdp.*.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundMethodMap(ProceedingJoinPoint pjp) throws Throwable{
        Object result = pjp.proceed();
        Map<String, Object> resultMsg = (Map<String, Object>)result;
        if ((resultMsg.get("recordsTotal") != null && resultMsg.get("exportlength") != null && resultMsg.get("exportstart") != null) && resultMsg.get("data") != null) {
            //将之前接口返回数据进行转换
            resultMsg.put("rows", resultMsg.get("data"));
            resultMsg.put("total", resultMsg.get("recordsTotal"));
            return resultMsg;
        }
        return result;
    }
}
