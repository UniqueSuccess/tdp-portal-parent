package cn.goldencis.tdp.report.controller;

import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ChildNodeDOCriteria;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IChildNodeService;
import cn.goldencis.tdp.core.service.IDepartmentService;
import cn.goldencis.tdp.core.utils.LegalTimeUtil;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDOCriteria;
import cn.goldencis.tdp.report.service.IIllegalOperationAlarmService;
import cn.goldencis.tdp.report.service.IVideoTransferLogService;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by limingchao on 2018/1/4.
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private IVideoTransferLogService videoTransferLogService;

    @Autowired
    private IIllegalOperationAlarmService illegalOperationAlarmService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IChildNodeService childNodeService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("report/index");
        return modelAndView;
    }

    @RequestMapping(value = "/childIndex")
    public ModelAndView childIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cascade/index");
        return modelAndView;
    }

    /**
     * 视频流转日志上报
     * @param argv
     * @param auditfile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitVideoTransferLog", method = RequestMethod.POST)
    public Map<String, Object> submitVideoTransferLog(String argv, MultipartFile auditfile, HttpServletRequest request) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //解析参数，转化为视频流转日志对象
            VideoTransferLogDO videoTransferLog = new VideoTransferLogDO();
            JSONObject argvJson = JSONObject.fromObject(argv).getJSONObject("tGopLog");

            videoTransferLogService.encapsulationBean(videoTransferLog, argvJson);

            //保存上传文件，更新对象的文件地址
            videoTransferLogService.uploadAttatchFile(auditfile, videoTransferLog);
            //保存视频流转日志对象
            videoTransferLogService.addVideoTransferLog(videoTransferLog);

            //判断定制化模板中功能选项内，是否包含合法时间选项
            JSONArray modules = (JSONArray) request.getServletContext().getAttribute("modules");
            if (modules.contains("legalTime")) {
                //检查当前的操作时间是否合法，如果不合法，启动告警
                if (!LegalTimeUtil.checkLegalTime(new Date())) {
                    //解析参数，转化为非法登录告警对象
                    IllegalOperationAlarmDO alarm = new IllegalOperationAlarmDO();
                    illegalOperationAlarmService.warningVideoTransfer(alarm, argvJson);

                    //添加非法告警
                    illegalOperationAlarmService.addIllegalOperationAlarm(alarm);

                    //通知所有在线的用户，有新的告警
                    illegalOperationAlarmService.noticeIllegalOperationAlarm();
                }
            }

            resultMsg.put("status", "success");
        } catch (Exception e) {
            resultMsg.put("status", "failed");
        }

        return resultMsg;
    }

    /**
     * 获取视频流转日志列表接口，分页查询
     * @param start
     * @param length
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVideoTransferLogInPage", method = RequestMethod.GET)
    public ResultMsg getVideoTransferLogInPage(Integer start, Integer length, Integer departmentId, String nodeType, String logType, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            List<DepartmentDO> departmentList = null;
            if (departmentId != null && departmentId != 0) {
                //根据部门id，查询部门及其子类集合
                departmentList = departmentService.getDeptarMentListByParent(departmentId);
                //校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
                departmentService.checkLoginUserDepartment(departmentList);
            }

            //查询全部视频流转日志数量
            int count = videoTransferLogService.countVideoTransferLog(departmentList, nodeType, logType, submitDate, startDate, endDate, order);
            //获取视频流转日志列表,分页查询
            List<VideoTransferLogDO> videoTransferLogList = videoTransferLogService.getVideoTransferLogInPage(start, length, departmentList, nodeType, logType, submitDate, startDate, endDate, order);

            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setRecordsTotal(count);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setData(videoTransferLogList);
            resultMsg.setResultMsg("获取视频流转日志列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取视频流转日志列表失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 报表模块，图表接口，最初按小时查询，后来改为时间段可选。
     * @param departmentId
     * @param submitDate
     * @param startDate
     * @param endDate
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVideoTransferLogInHours", method = RequestMethod.GET)
    public ResultMsg getVideoTransferLogInHours(Integer departmentId, String logType, String submitDate, String startDate, String endDate , String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            List<DepartmentDO> departmentList = null;
            if (departmentId != null && departmentId != 0) {
                //根据部门id，查询部门及其子类集合
                departmentList = departmentService.getDeptarMentListByParent(departmentId);
                //校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
                departmentService.checkLoginUserDepartment(departmentList);
            }

            //将获取视频流转日志列表
            List<VideoTransferLogDO> videoTransferLogList = videoTransferLogService.getVideoTransferLogListByParams(departmentList, logType, submitDate, startDate, endDate, order);

            if ("day".equals(submitDate)) {
                //将获取视频流转日志列表转化为按小时分类的集合
                List<Integer> hoursCount = videoTransferLogService.getVideoTransferLogInHours(videoTransferLogList);
                resultMsg.setData(hoursCount);
            } else {
                //填充开始和结束时间
                if ("week".equals(submitDate)) {
                    startDate = DateUtil.getWeekEndStr();
                    endDate = DateUtil.getNowDateStr();
                } else if ("month".equals(submitDate)) {
                    startDate = DateUtil.getMonthEndStr();
                    endDate = DateUtil.getNowDateStr();
                }

                if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate)) {
                    //将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
                    Map<String, Object> videoTransferLogMap = videoTransferLogService.getVideoTransferLogInDate(videoTransferLogList, startDate, endDate);
                    resultMsg.setData(videoTransferLogMap);
                }
            }

            resultMsg.setResultMsg("获取视频流转日志图表信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取视频流转日志图表信息错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 首页中，数据导出统计接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVideoTransferLogInWeek", method = RequestMethod.GET)
    public ResultMsg getVideoTransferLogInWeek() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //将获取视频流转日志列表
            List<VideoTransferLogDO> videoTransferLogList = videoTransferLogService.getVideoTransferLogListByParams(null, null, "week", null, null, null);

            //填充开始和结束时间
            String startDate = DateUtil.getWeekEndStr();
            String endDate = DateUtil.getNowDateStr();
            //将获取视频流转日志列表转化为按日期分类的Map，存放两个List，一个存放日期集合，一个存放数量
            Map<String, Object> videoTransferLogMap = videoTransferLogService.getVideoTransferLogInDate(videoTransferLogList, startDate, endDate);
            resultMsg.setData(videoTransferLogMap);

            resultMsg.setResultMsg("获取首页数据导出统计信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取首页数据导出统计信息错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 首页中，视频数据导出Top5接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countVideoTransferTop5", method = RequestMethod.GET)
    public ResultMsg countVideoTransferTop5() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //查询视频流转日志表中，统计记录最多用户的guid的前五名，返回真实姓名和统计数量
            List<Map<String, Object>> countMap = videoTransferLogService.countVideoTransferTop5();

            resultMsg.setData(countMap);
            resultMsg.setResultMsg("获取首页视频数据导出Top10成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取首页视频数据导出Top10成功错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 删除视频流转日志接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteVideoTransferLog", method = RequestMethod.POST)
    public ResultMsg deleteVideoTransferLog(Integer clearDays) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取需要清理日志的日期
            Date clearDate = DateUtil.getCurrentDateAddDay(-clearDays);

            //清理视频流转日志及文件
            videoTransferLogService.deleteVideoTransferLogsByClearDate(clearDate);

            resultMsg.setResultMsg("删除视频流转日志成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除视频流转日志错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 按部门统计视频流转日志的接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countVideoTransferByDepartment", method = RequestMethod.GET)
    public ResultMsg countVideoTransferByDepartment() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取一级部门的集合
            List<DepartmentDO> deptLvOneList = departmentService.getDepartmentLevelOne();
            List<DepartmentDO> departmentList;
            List<JSONObject> countList = new ArrayList<>();
            //遍历一级部门，查出每个一级部门对应的
            for (DepartmentDO department : deptLvOneList) {
                //根据部门id，查询部门及其子类集合
                departmentList = departmentService.getDeptarMentListByParent(department.getId());
                //校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
                departmentService.checkLoginUserDepartment(departmentList);
                //查询全部视频流转日志数量
                int count = videoTransferLogService.countVideoTransferLog(departmentList, ConstantsDto.CURRENT_NODE, null, null, null, null, null);
                JSONObject dataJson = new JSONObject();
                dataJson.put("id", department.getId());
                dataJson.put("name", department.getName());
                dataJson.put("count", count);
                countList.add(dataJson);
            }

            resultMsg.setData(countList);
            resultMsg.setResultMsg("统计部门视频流转日志成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("统计部门视频流转日志错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 上传日志之前，统计需要上传日志的数量
     * @param start 采集的开始时间
     * @param end 采集的结束时间
     * @return 需要上传日志的数量
     */
    @ResponseBody
    @RequestMapping(value = "/countVideoTransferLogsForCollecting", method = RequestMethod.POST)
    public ResultMsg countVideoTransferLogsForCollecting(String start, String end) {
        try {
            //封装时间参数
            Date startTime = DateUtil.strToDate(start, DateUtil.FMT_DATE);
            Date endTime = null;
            if (!StringUtils.isEmpty(end)) {
                endTime = DateUtil.strToDate(end, DateUtil.FMT_DATE);
            }

            if (startTime == null || (!StringUtils.isEmpty(end) && endTime == null)) {
                return ResultMsg.build(ConstantsDto.RESULT_CODE_FALSE, "时间参数错误！");
            }

            VideoTransferLogDOCriteria example = videoTransferLogService.generatorVideoTransferLogExample(startTime, endTime);
            int count = videoTransferLogService.countVideoTransferLogsForCollecting(example);

            return ResultMsg.ok(count);
        } catch (Exception e) {
            return ResultMsg.build(ConstantsDto.RESULT_CODE_ERROR, "统计日志数量错误！", e);
        }
    }

    /**
     * 视频流转日志采集功能，上传日志接口
     * @param start 采集的开始时间
     * @param end 采集的结束时间
     * @return 日志集合
     */
    @ResponseBody
    @RequestMapping(value = "/uploadVideoTransferLogsWithAttachment", method = RequestMethod.POST)
    public ResultMsg uploadVideoTransferLogsWithAttachment(String start, String end, Integer page) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            System.out.println("进入上传日志接口");
            //封装时间参数
            Date startTime = DateUtil.strToDate(start, DateUtil.FMT_DATE);
            Date endTime = null;
            if (!StringUtils.isEmpty(end)) {
                endTime = DateUtil.strToDate(end, DateUtil.FMT_DATE);
            }

            if (startTime == null || (!StringUtils.isEmpty(end) && endTime == null)) {
                resultMsg.setResultMsg("时间参数错误！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            VideoTransferLogDOCriteria example = videoTransferLogService.generatorVideoTransferLogExample(startTime, endTime);
            List<VideoTransferLogDO> videoTransferLogDOList = videoTransferLogService.listPage(page, 10000, example);

            resultMsg.setData(videoTransferLogDOList);
            resultMsg.setResultMsg("上传视频流转日志成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("上传视频流转日志失败！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 视频流转日志采集功能，手动采集数据接口
     * @param start 采集开始时间
     * @param end 采集结束时间
     * @return 采集结果
     */
    @ResponseBody
    @RequestMapping(value = "/collectVideoTransferLogsWithAttachment", method = RequestMethod.POST)
    public ResultMsg collectVideoTransferLogsWithAttachment(String start, String end) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //封装时间参数
            Date startTime = DateUtil.strToDate(start, DateUtil.FMT_DATE);
            Date endTime = null;
            if (!StringUtils.isEmpty(end)) {
                endTime = DateUtil.strToDate(end, DateUtil.FMT_DATE);
            }

            if (startTime == null || (!StringUtils.isEmpty(end) && endTime == null)) {
                resultMsg.setResultMsg("时间参数错误！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //获取服务器节点信息
            List<ChildNodeDO> childNodeList = childNodeService.listBy(new ChildNodeDOCriteria());

            //调用接口，收集日志
            Map<String, String> resMap = null;
            if (childNodeList != null && childNodeList.size() > 0) {
                //调用接口，收集日志
                resMap = videoTransferLogService.collectVideoTransferLogsWithAttachment(start, end, childNodeList);
            } else {
                resultMsg.setResultMsg("请先输入服务器节点信息！");
            }

            resultMsg.setData(resMap);
            resultMsg.setResultMsg("采集下级日志成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setData(e);
            resultMsg.setResultMsg("采集下级日志错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
