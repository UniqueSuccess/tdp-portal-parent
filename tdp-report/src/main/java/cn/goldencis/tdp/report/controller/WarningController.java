package cn.goldencis.tdp.report.controller;

import cn.goldencis.tdp.common.utils.CommonPoiXsl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IDepartmentService;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDO;
import cn.goldencis.tdp.report.service.IIllegalOperationAlarmService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by limingchao on 2018/5/24.
 */
@Controller
@RequestMapping(value = "/warning")
public class WarningController {

    @Autowired
    private IIllegalOperationAlarmService illegalOperationAlarmService;

    @Autowired
    private IDepartmentService departmentService;

    private static final String FILE_NAME = "告警日志";

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("warning/index");
        return modelAndView;
    }

    /**
     * 非法登录告警上报接口
     * @param argv 上报参数json
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitIllegalLoginAlarm", method = RequestMethod.POST)
    public Map<String, Object> submitIllegalLoginAlarm(String argv) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //解析参数，转化为非法登录告警对象
            IllegalOperationAlarmDO alarm = new IllegalOperationAlarmDO();
            JSONObject argvJson = JSONObject.fromObject(argv);
            illegalOperationAlarmService.encapsulationBean(alarm, argvJson);
            alarm.setWarningType(ConstantsDto.ALARM_LOGIN);

            //添加非法告警
            illegalOperationAlarmService.addIllegalOperationAlarm(alarm);

            //通知所有在线的用户，有新的告警
            illegalOperationAlarmService.noticeIllegalOperationAlarm();

            resultMsg.put("status", "success");
        } catch (Exception e) {
            resultMsg.put("status", "failed");
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/getIllegalOperationAlarmInPage", method = RequestMethod.GET)
    public ResultMsg getIllegalOperationAlarmInPage(Integer start, Integer length, Integer departmentId, String logType, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            List<DepartmentDO> departmentList = null;
            if (departmentId != null && departmentId != 0) {
                //根据部门id，查询部门及其子类集合
                departmentList = departmentService.getDeptarMentListByParent(departmentId);
                //校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
                departmentService.checkLoginUserDepartment(departmentList);
            }

            //查询全部告警日志数量
            int count = illegalOperationAlarmService.IllegalOperationAlarm(departmentList, logType, submitDate, startDate, endDate, order);
            //获取告警日志列表,分页查询
            List<IllegalOperationAlarmDO> illegalOperationAlarmList = illegalOperationAlarmService.getIllegalOperationAlarmInPage(start, length, departmentList, logType, submitDate, startDate, endDate, order);

            if (illegalOperationAlarmList != null && illegalOperationAlarmList.size() > 0) {
                //将查询出的告警集合的id，放入集合中
                List<Integer> idList = new ArrayList<>();
//                illegalOperationAlarmList.stream().map(alarm -> idList.add(alarm.getId()));
                for (IllegalOperationAlarmDO alarm : illegalOperationAlarmList) {
                    idList.add(alarm.getId());
                }
                //将查询返回过得告警信息，在数据库中的状态置为已读，不修改本次返回的集合数据
                illegalOperationAlarmService.readIllegalOperationAlarmByIdList(idList);
            }

            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setRecordsTotal(count);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setData(illegalOperationAlarmList);
            resultMsg.setResultMsg("获取告警日志列表成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取告警日志列表失败！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 告警日志导出接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exportIllegalOperationAlarmByParams")
    public void exportIllegalOperationAlarmByParams(Integer departmentId, String logType, String submitDate, String startDate, String endDate , String order, HttpServletRequest request, HttpServletResponse response) {
        List<DepartmentDO> departmentList = null;
        if (departmentId != null && departmentId != 0) {
            //根据部门id，查询部门及其子类集合
            departmentList = departmentService.getDeptarMentListByParent(departmentId);
            //校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
            departmentService.checkLoginUserDepartment(departmentList);
        }

        //按条件获取告警日志列表，不分页
        List<IllegalOperationAlarmDO> illegalOperationAlarmList = illegalOperationAlarmService.getIllegalOperationAlarm(departmentList, logType, submitDate, startDate, endDate, order);

        CommonPoiXsl<IllegalOperationAlarmDO> poiXsl = new CommonPoiXsl<>();
        String path = "";
        try {
            String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            path = request.getServletContext().getRealPath(PathConfig.EXPORT_PATH);
            if (!(new File(path).isDirectory())) {
                new File(path).mkdirs();
            }
            path = path + "/" + FILE_NAME + key + ".xls";
            OutputStream out = new FileOutputStream(path);
            String[] headers = {"用户名", "真实姓名", "IP", "类型", "接收方ID", "流转文件", "操作时间"};
            poiXsl.exportExcel("告警日志", headers, illegalOperationAlarmList, out, "yyyy-MM-dd");
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

}
