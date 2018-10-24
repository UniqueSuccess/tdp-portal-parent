package cn.goldencis.tdp.approve.controller;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.service.IApproveDetailService;
import cn.goldencis.tdp.approve.service.IApproveFlowService;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by limingchao on 2018/1/18.
 */
@Controller
@PageLog(module = "审批请求")
@RequestMapping(value = "/approveDetail")
public class ApproveDetailController {

    @Autowired
    private IApproveDetailService approveDetailService;

    @Autowired
    private IApproveFlowService approveFlowService;

    /**
     * 根据流程id，获取流程环节的模型
     *
     * @param approveFlowId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveFlowModel", method = RequestMethod.GET)
    public ResultMsg getApproveFlowModel(Integer approveFlowId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据流程id，获取该流程全部细节集合
            List<ApproveDetail> detailList = approveDetailService.getApproveDetailListByFlowId(approveFlowId);

            //获取当前流程对象
            ApproveFlow approveFlow = approveFlowService.getApproveFlowInfoById(approveFlowId);

            //获取当前账户待审批细节id
            Integer detailId = approveDetailService.getDetailNeedToApproveId(detailList, approveFlow.getPointId());

            //将流程细节集合转化为模型
            detailList = approveDetailService.convertDetailListToModel(detailList);

            JSONObject resJson = new JSONObject();
            resJson.put("detailList", detailList);
            resJson.put("pointId", approveFlow.getPointId());
            resJson.put("detailId", detailId);
            resultMsg.setData(resJson);
            resultMsg.setResultMsg("获取流程环节模型成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取流程环节模型错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }


    /**
     * 根据流程id，获取流程的全部细节
     * @param approveFlowId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveDetailsByFlowId", method = RequestMethod.GET)
    public ResultMsg getApproveDetailsByFlowId(Integer approveFlowId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据流程id，获取该流程已经审批完成的细节集合
            List<ApproveDetail> detailList = approveDetailService.getApproveDetailDoneListByFlowId(approveFlowId);

            //将流程细节集合转化为有序列表
            detailList = approveDetailService.convertToLinkedDetailList(detailList);

            //为流程细节设置审批人姓名
            approveDetailService.fillWithApproverName(detailList);

            resultMsg.setRows(detailList);
            resultMsg.setResultMsg("获取流程全部细节成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取流程全部细节错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
