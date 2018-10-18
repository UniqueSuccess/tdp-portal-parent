package cn.goldencis.tdp.approve.controller;

import cn.goldencis.tdp.approve.entity.ApproveDefinition;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.service.IApproveDefinitionService;
import cn.goldencis.tdp.approve.service.IApproveModelService;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IUserService;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.service.IPolicyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批定义相关控制器
 * Created by limingchao on 2018/1/15.
 */
@Controller
@PageLog(module = "审批流程")
@RequestMapping(value = "/approveDefinition")
public class ApproveDefinitionController {

    @Autowired
    private IApproveDefinitionService approveDefinitionService;

    @Autowired
    private IApproveModelService approveModelService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPolicyService policyService;

    /**
     * 审批流程定义界面跳转
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView configIndex() {
        ModelAndView modelAndView = new ModelAndView();
        List<ApproveDefinition> allApproveDefinitionList = approveDefinitionService.getAllApproveDefinition();
        modelAndView.addObject("approveLsit", allApproveDefinitionList);
        modelAndView.setViewName("approve/config/index");
        return modelAndView;
    }

    /**
     * 审批流程定义界面，跳转审批流程具体的信息页面
     * @param approveDefinitionId
     * @return
     */
    @RequestMapping(value = "/approveDetailView", method = RequestMethod.GET)
    public ModelAndView approveDetailView(Integer approveDefinitionId) {
        ModelAndView modelAndView = new ModelAndView();

        //获取审批流程定义信息
        ApproveDefinition definition = approveDefinitionService.getByPrimaryKey(approveDefinitionId);

        //根据流程定义id，获取定义流程的环节信息，并以此构建审批流程模型
        List<ApproveModel> modelList = approveModelService.getApproveModelByDefinitionId(approveDefinitionId);

        //根据审批流程模型，获取其中审批人的guid集合
        List<String> approverIdList = approveModelService.getApproverIdListByModelList(modelList);

        JSONArray userJSONArray = null;
        if (approverIdList != null && approverIdList.size() > 0) {
            //根据审批流程模型中的审批人，获取对应账户的姓名
            userJSONArray = userService.getUserMapByIdList(approverIdList);
        }
        modelAndView.addObject("definition", JSONObject.fromObject(definition));
        modelAndView.addObject("modelList", JSONArray.fromObject(modelList));
        modelAndView.addObject("approvers", userJSONArray);
        modelAndView.setViewName("approve/flow/detail");
        return modelAndView;
    }

    /**
     * 审批流程定义界面，获取审批流程具体的信息接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveDefinitionModel", method = RequestMethod.GET)
    public ResultMsg getApproveDefinitionModel(Integer approveDefinitionId) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //获取审批流程定义信息
            ApproveDefinition definition = approveDefinitionService.getByPrimaryKey(approveDefinitionId);

            //根据流程定义id，获取定义流程的环节信息，并以此构建审批流程模型
            List<ApproveModel> modelList = approveModelService.getApproveModelByDefinitionId(approveDefinitionId);

            //根据审批流程模型，获取其中审批人的guid集合
            List<String> approverIdList = approveModelService.getApproverIdListByModelList(modelList);

            JSONArray userJSONArray = null;
            if (approverIdList != null && approverIdList.size() > 0) {
                //根据审批流程模型中的审批人，获取对应账户的姓名
                userJSONArray = userService.getUserMapByIdList(approverIdList);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("definition", definition);
            model.put("modelList", modelList);
            model.put("approvers", userJSONArray);

            resultMsg.setData(model);
            resultMsg.setResultMsg("获取审批流程模型成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取审批流程模型失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 审批流程定义界面，获取全部审批流程接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllApproveDefinition", method = RequestMethod.GET)
    public ResultMsg getAllApproveDefinition() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取全部审批流程列表
            List<ApproveDefinition> definitionList = approveDefinitionService.getAllApproveDefinition();

            resultMsg.setData(definitionList);
            resultMsg.setResultMsg("获取审批流程列表成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取审批流程列表失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 新定义审批流程，添加接口
     * @param approveDefinition
     * @param parentApproveId
     * @return
     */
    @ResponseBody
    @PageLog(module = "新建审批流程", template = "审批流程名称：%s，继承审批流程id：%s", args = "0.name,1", type = LogType.INSERT)
    @RequestMapping(value = "/addApproveDefinition")
    public ResultMsg addApproveDefinition(ApproveDefinition approveDefinition, Integer parentApproveId) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //检查审批流程名是否重复
            boolean flag = approveDefinitionService.checkApproveDefinitionNameDuplicate(approveDefinition);
            if (!flag) {
                resultMsg.setResultMsg("该审批流程名称已经存在");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //根据父类审批流程的id，查询审批环节模型
            List<ApproveModel> modelList = approveModelService.getApproveModelByDefinitionId(parentApproveId);

            //根据父类审批流程的id，创建审批流程，继承父类审批流程的环节
            approveDefinitionService.copyApproveDefinitionFromParent(approveDefinition, modelList);

            resultMsg.setResultMsg("添加审批流程成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("添加审批流程失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 根据id删除流程的定义，同时删除其对应的环节
     * @param approveDefinitionId
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除审批流程", template = "审批流程id：%s", type = LogType.DELETE)
    @RequestMapping(value = "/deleteApproveDefinition", method = RequestMethod.POST)
    public ResultMsg deleteApproveDefinition(Integer approveDefinitionId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取全部的策略集合
            List<PolicyDO> policyList = policyService.getAllPolicyList();
            if (policyList != null && policyList.size() > 0) {
                //检查正在使用的策略中，是否应用了该审批流程，若存在则不允许删除。
                boolean flag = policyService.checkApproveInUsing(policyList, approveDefinitionId);
                if (!flag) {
                    resultMsg.setResultMsg("该审批流程已被使用，无法删除！");
                    resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                    return resultMsg;
                }
            }

            //根据id删除流程的定义，同时删除其对应的环节
            approveDefinitionService.deleteApproveDefinition(approveDefinitionId);

            resultMsg.setResultMsg("删除审批流程成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除审批流程错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

}
