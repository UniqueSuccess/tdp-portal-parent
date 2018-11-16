package cn.goldencis.tdp.approve.controller;

import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.service.IApproveModelService;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by limingchao on 2018/1/16.
 */
@Controller
@PageLog(module = "审批流程")
@RequestMapping(value = "/approveModel")
public class ApproveModelController {

    @Autowired
    private IApproveModelService approveModelService;

    /**
     * 审批环节新建和更新接口
     *
     * @param approveModel
     * @return
     */
    @ResponseBody
    @PageLog(module = "新建和更新审批环节", template = "新建和更新审批环节名称：%s，所属审批流程id：%s", args = "0.name,0.flowId", type = LogType.INSERT)
    @RequestMapping(value = "/addOrUpdateApproveModel", method = RequestMethod.POST)
    public ResultMsg addOrUpdateApproveModel(ApproveModel approveModel) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //审批人参数转化
            if (StringUtil.isEmpty(approveModel.getApprovers()) || approveModel.getApprovers().split(";").length == 0) {
                resultMsg.setResultMsg("每个审批环节至少有一位审批人");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //校验在同一个流程中，是否有重名的环节名称
            boolean flag = approveModelService.checkApproveModelNameDuplicate(approveModel);
            if (!flag) {
                resultMsg.setResultMsg("审批环节名称重复");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //添加或新建审批环节，没有id为新建，有id为更新
            approveModelService.addOrUpdateApproveModel(approveModel);

            resultMsg.setResultMsg("添加或更新审批环节成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("添加或更新审批环节错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 审批环节删除接口
     *
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除审批环节", template = "删除审批环节id：%s，所属审批流程id：%s", args = "0.id,0.flowId", type = LogType.DELETE)
    @RequestMapping(value = "/deleteApproveModel", method = RequestMethod.POST)
    public ResultMsg deleteApproveModel(ApproveModel approveModel) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据环节id，校验该环节是否为流程中唯一的环节，
            boolean flag = approveModelService.checkApproveModelSingle(approveModel.getFlowId());
            if (flag) {
                resultMsg.setResultMsg("审批流程至少有一个审批环节");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //根据环节id，删除环节，并后一个环节与前一个环节连接。
            approveModelService.deleteApproveModel(approveModel);
            resultMsg.setResultMsg("审批环节删除成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("审批环节删除错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

}
