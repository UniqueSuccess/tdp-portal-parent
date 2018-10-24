package cn.goldencis.tdp.approve.controller;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfo;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.service.IApproveDetailService;
import cn.goldencis.tdp.approve.service.IApproveFlowInfoService;
import cn.goldencis.tdp.approve.service.IApproveFlowService;
import cn.goldencis.tdp.approve.service.IApproveModelService;
import cn.goldencis.tdp.approve.service.impl.ApproveResultPublish4ClientRunnable;
import cn.goldencis.tdp.common.mqclient.MQClient;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by limingchao on 2018/1/16.
 */
@Controller
@PageLog(module = "审批请求")
@RequestMapping(value = "/approveFlow")
public class ApproveFlowController {

    @Autowired
    private IApproveFlowService approveFlowService;

    @Autowired
    private IApproveDetailService approveDetailService;

    @Autowired
    private IApproveModelService approveModelService;

    @Autowired
    private IApproveFlowInfoService approveFlowInfoService;

    @Autowired
    private MQClient publisher;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 审批流程执行界面跳转
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView flowIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("approve/flow/index");
        return modelAndView;
    }

    /**
     * 审批流程回显给客户端界面
     *
     * @return
     */
    @RequestMapping(value = "/clientFlowIndex", method = RequestMethod.GET)
    public ModelAndView clientFlowIndex(String applicantOrType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("applicantOrType", applicantOrType);
        modelAndView.setViewName("approve/client/index");
        return modelAndView;
    }


    /**
     * 审批中和已完成的列表界面切换
     *
     * @param statusType
     * @return
     */
    @RequestMapping(value = "/listPage")
    public ModelAndView listPage(String statusType) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("approve/config/" + statusType);
        return modelAndView;
    }

    /**
     * 客户端发起审批流程接口
     *
     * @param pId        审批定义id
     * @param rem        申请原因
     * @param uid        用户guid
     * @param uname      用户姓名
     * @param tranUnique 文件唯一标示
     * @param aType      审批类型
     * @param uploadFile 是否上传文件
     * @param argv       文件外发附加参数
     * @param approvfile 审批的文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitApproveFlow", method = RequestMethod.POST)
    public Map<String, Object> submitApproveFlow(Integer pId, String rem, String uid, String uname, String tranUnique, Integer aType, boolean uploadFile, String argv, MultipartFile approvfile, HttpServletRequest request) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //参数封装
            ApproveFlow approveFlow = new ApproveFlow();
            approveFlow.setFlowId(pId);
            approveFlow.setReason(rem);
            approveFlow.setApplicantId(uid);
            approveFlow.setApplicantName(uname);
            approveFlow.setTranUnique(tranUnique);
            approveFlow.setType(aType);

            ApproveFlowInfo flowInfo = new ApproveFlowInfo();
            flowInfo.setPolicyParam(argv);
            if (uploadFile) {
                //保存文件，并在流程信息中添加路径
                approveFlowInfoService.uploadApproveFile(flowInfo, approvfile);
            }


            //传入的审批流程对象中，flowId指的是，需要使用的审批流程定义id，根据此id，查询model 查询出几个节点
            List<ApproveModel> modelList = approveModelService.getApproveModelByDefinitionId(approveFlow.getFlowId());

            //发起审批流程，根据模型，生成本次流程的环节步骤
            approveFlowService.submitApproveFlow(approveFlow, modelList);

            //根据流程的回传id，设置flowId
            flowInfo.setFlowId(approveFlow.getId());
            //插入审批流程详细信息。
            approveFlowInfoService.addapproveFlowInfo(flowInfo);

            //通知审批流程当前环节的审批人
            approveFlowService.noticeApprover(approveFlow);

            resultMsg.put("state", ConstantsDto.APPROVE_SUBMIT_SUCCESS);
            resultMsg.put("flowId", approveFlow.getFlowId());
            resultMsg.put("reason", "");
        } catch (Exception e) {
            resultMsg.put("state", ConstantsDto.APPROVE_SUBMIT_FAILED);
            resultMsg.put("reason", "0");
        }

        return resultMsg;
    }

    /**
     * 控制台账户审批流程接口
     *
     * @return
     */
    @ResponseBody
    @PageLog(module = "进行审批操作", template = "审批细节id：%s，审批结果：%s，审批备注：%s", args = "0,1,2", type = LogType.UPDATE)
    @RequestMapping(value = "/approveFlow", method = RequestMethod.POST)
    public ResultMsg approveFlow(Integer approveDetailId, Integer result, String remark) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据id获取审批细节步骤
            ApproveDetail detail = approveDetailService.getApproveDetailById(approveDetailId);

            //校验审批步骤是否需要当前账户审批
            UserDO user = GetLoginUser.getLoginUser();
            if (!user.getGuid().equals(detail.getApprover())) {
                resultMsg.setResultMsg("当前账户不是该环节的审批人");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //补全当前审批细节步骤，封装审批结果和审批意见
            detail.setResult(result);
            detail.setRemark(remark);

            //处理当前细节步骤，无论通过和拒绝，更新细节步骤记录
            approveDetailService.dealWithCurrentDetail(detail);

            ApproveFlow approveFlow = null;

            //根据审批结果，判断审批调用方法
            if (result == ConstantsDto.APPROVE_DETAIL_RESULT_ADOPT) {
                //判断当前环节是否为标准环节
                if (detail.getStandard() == ConstantsDto.APPROVE_MODEL_STANDARD) {
                    //为标准环节，当前环节审批通过，进入下一环节，如果没有下一个环节，审批流程通过
                    approveFlow = approveFlowService.adoptCurrentPoint(detail);
                } else {
                    //不为标准环节，检测当前环节的其他审批细节是否通过，如果其他都通过，则当前环节审批通过，进入下一环节，如果没有下一个环节，审批流程通过
                    List<ApproveDetail> detailList = approveDetailService.getapproveDetailListCurrentPoint(detail.getFlowId(), detail.getPointId());
                    boolean flag = false;
                    for (ApproveDetail approveDetail : detailList) {
                        if (approveDetail.getResult() == ConstantsDto.APPROVE_DETAIL_RESULT_INQUEEN && approveDetail.getId() != detail.getId()) {
                            //当前环节中，存在没有审批的其他细节步骤，需要等待期审批完成进入下一环节
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        //其他细节步骤都已审批完成，进入下一环节
                        approveFlow = approveFlowService.adoptCurrentPoint(detail);
                    }
                }
            } else {
                //审批请求被拒绝，审批环节终止，审批流程终止，审批流程被拒绝
                approveFlow = approveFlowService.refuseApproveFlow(detail);
            }

            if (approveFlow != null) {
                if (approveFlow.getStatus() == ConstantsDto.APPROVE_FLOW_STATUS_APPROVING) {
                    //通知审批流程当前环节的审批人
                    approveFlowService.noticeApprover(approveFlow);
                } else {
                    //通知申请流程的客户端用户
                    ApproveFlowInfo flowInfo = approveFlowInfoService.getApproveFlowInfoByFlowId(approveFlow.getId());
                    ApproveResultPublish4ClientRunnable resultPublish4Client = new ApproveResultPublish4ClientRunnable();
                    resultPublish4Client.setFlow(approveFlow);
                    resultPublish4Client.setDetail(detail);
                    resultPublish4Client.setFlowInfo(flowInfo);
                    resultPublish4Client.setPublisher(publisher);
                    taskExecutor.execute(resultPublish4Client);
                }
            }


            //通知当前登录账户，更新待审批数量
            approveFlowService.noticeApproverByGuid(user.getGuid());

            resultMsg.setResultMsg("审批成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("审批错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 获取审批流程。分页查询。
     *
     * @param start
     * @param length
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveFlowPage", method = RequestMethod.GET)
    public ResultMsg getApproveFlowPage(@RequestParam("start") int start, @RequestParam("length") int length, Integer status, Integer needOnly, String applicantOrType) {
        ResultMsg resultMsg = new ResultMsg();
        //解析查询的开始时间和结束时间
        Map<String, Date> timeMap = DateUtil.analyzeQueryTime("all", "", "");
        try {

            int count = approveFlowService.countApproveFlowPage(status, needOnly, timeMap, applicantOrType);
            List<ApproveFlow> approveFlows = approveFlowService.getApproveFlowPage(start, length, status, needOnly, timeMap, applicantOrType);

            //根据是否需要当前登录账户审批
            approveFlowService.isNeedApprove(approveFlows);

            resultMsg.setRows(approveFlows);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setRecordsTotal(count);
            resultMsg.setExportstart(start);
            resultMsg.setExportlength(length);
            resultMsg.setResultMsg("获取审批流程列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取审批流程列表错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 根据流程id，获取流程详细信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveFlowInfoById", method = RequestMethod.GET)
    public ResultMsg getApproveFlowInfoById(Integer approveFlowId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据流程id，获取审批流程记录
            ApproveFlow approveFlow = approveFlowService.getApproveFlowInfoById(approveFlowId);

            //根据流程id，获取流程详细信息
            ApproveFlowInfo info = approveFlowInfoService.getApproveFlowInfoByFlowId(approveFlowId);
            approveFlow.setFlowInfo(JSONObject.fromObject(info));

            resultMsg.setData(approveFlow);
            resultMsg.setResultMsg("获取流程详细信息成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取流程详细信息错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 审批流程的删除接口
     *
     * @param approveFlowArr
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除审批流程", template = "审批流程id：%s", type = LogType.DELETE)
    @RequestMapping(value = "/deleteApproveFlow", method = RequestMethod.POST)
    public ResultMsg deleteApproveFlow(String approveFlowArr, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //校验该审批流程是否存在
            /*ApproveFlow approveFlow = approveFlowService.getApproveFlowInfoById(approveFlowId);
            if (approveFlow == null) {
                resultMsg.setResultMsg("该审批流程不存在，无法删除");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }*/

            //获取项目路径
            String ctp = request.getServletContext().getRealPath("");

            //删除审批流程
            approveFlowService.deleteApproveFlow(approveFlowArr, ctp);

            resultMsg.setResultMsg("删除审批流程成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除审批流程错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 首页，按照状态类型，统计当前账户审批请求数量
     *
     * @param status 审批请求的状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countApproveByState", method = RequestMethod.GET)
    public ResultMsg countApproveByState(Integer status) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //按照状态类型，统计当前审批请求数量
            long count = approveFlowService.countApproveByState(status);

            resultMsg.setData(count);
            resultMsg.setResultMsg("统计当前账户审批请求数量成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("统计当前账户审批请求数量错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
