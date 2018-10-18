package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.common.utils.CommonPoiXsl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.PathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by limingchao on 2018/1/23.
 */
@Controller
@RequestMapping(value = "/systemLog")
public class SystemLogController {

    private static final String FILE_NAME = "系统日志";

    @Autowired
    private IOperationLogService operationLogService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("system/log/index");
        return modelAndView;
    }

    @RequestMapping(value = "/listPage")
    public ModelAndView listPage(String logType) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("system/log/" + logType);
        return modelAndView;
    }

    /**
     * 查询系统登录日志列表的接口
     * @param start 开始位置
     * @param length 条数
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSystemLogonLogInPage")
    public ResultMsg getSystemLogonLogInPage(Integer start, Integer length, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //解析查询的开始时间和结束时间
            Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);

            //查询登录日志总数
            int count = operationLogService.countSystemLogListByParams(ConstantsDto.LOGON_LOG_TYPE, timeMap, order);

            //多条件分页查询登录日志
            List<OperationLogDO> operationLogList = operationLogService.getSystemLogListInPageByParams(start, length, ConstantsDto.LOGON_LOG_TYPE, timeMap, order);

            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setRecordsTotal(count);
            resultMsg.setData(operationLogList);
            resultMsg.setResultMsg("获取登录日志列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取登录日志列表错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 查询客户端登录日志列表的接口
     * @param start 开始位置
     * @param length 条数
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getClientLogonLogInPage")
    public ResultMsg getClientLogonLogInPage(Integer start, Integer length, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //解析查询的开始时间和结束时间
            Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);

            //查询登录日志总数
            int count = operationLogService.countSystemLogListByParams(ConstantsDto.CLIENT_LOGON_LOG_TYPE, timeMap, order);

            //多条件分页查询登录日志
            List<OperationLogDO> operationLogList = operationLogService.getSystemLogListInPageByParams(start, length, ConstantsDto.CLIENT_LOGON_LOG_TYPE, timeMap, order);

            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setRecordsTotal(count);
            resultMsg.setData(operationLogList);
            resultMsg.setResultMsg("获取客户端日志列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取客户端登录日志列表错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 查询系统操作日志列表的接口
     * @param start 开始位置
     * @param length 条数
     * @param submitDate 查询日期类型
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param order 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSystemOperationLogInPage")
    public ResultMsg getSystemOperationLogInPage(Integer start, Integer length, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //解析查询的开始时间和结束时间
            Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);

            //查询操作日志总数
            int count = operationLogService.countSystemLogListByParams(ConstantsDto.OPERATION_LOG_TYPE, timeMap, order);

            //多条件分页查询操作日志
            List<OperationLogDO> operationLogList = operationLogService.getSystemLogListInPageByParams(start, length, ConstantsDto.OPERATION_LOG_TYPE, timeMap, order);

            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setRecordsTotal(count);
            resultMsg.setData(operationLogList);
            resultMsg.setResultMsg("获取操作日志列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取操作日志列表错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 日志导出接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exportSystemLogsByParams")
    public void exportSystemLogsByParams(String logType, String submitDate, String startDate, String endDate , String order, HttpServletRequest request, HttpServletResponse response) {
        //解析查询的开始时间和结束时间
        Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);

        //按条件获取日志列表，不分页
        List<OperationLogDO> systemLogList = operationLogService.getSystemLogListByParams(logType, timeMap, order);

        CommonPoiXsl<OperationLogDO> poiXsl = new CommonPoiXsl<>();
        String path = "";
        try {
            String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            path = request.getServletContext().getRealPath(PathConfig.EXPORT_PATH);
            if (!(new File(path).isDirectory())) {
                new File(path).mkdirs();
            }
            path = path + "/" + FILE_NAME + key + ".xls";
            OutputStream out = new FileOutputStream(path);
            String[] headers = {"账户名称", "操作功能", "操作时间", "登录IP", "详细描述"};
            poiXsl.exportExcel("系统日志", headers, systemLogList, out, "yyyy-MM-dd");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }

    /**
     * 删除系统日志接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOperationLog")
    public ResultMsg deleteOperationLog(Integer clearDays, String logType) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取需要清理日志的日期
            Date clearDate = DateUtil.getCurrentDateAddDay(-clearDays);

            //清理系统日志
            operationLogService.deleteOperationLogsByClearDate(clearDate, logType);

            resultMsg.setResultMsg("删除系统日志成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除系统日志错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
