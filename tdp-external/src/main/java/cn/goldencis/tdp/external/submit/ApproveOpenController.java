package cn.goldencis.tdp.external.submit;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.service.IApproveDetailService;
import cn.goldencis.tdp.approve.service.IApproveFlowInfoService;
import cn.goldencis.tdp.approve.service.IApproveFlowService;
import cn.goldencis.tdp.approve.service.IApproveModelService;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: tdp-portal-parent
 * @description:
 * @Author: chengl
 * @create: 2018-10-22 16:21
 **/
@Controller
@RequestMapping(value = "/approveOpen")
public class ApproveOpenController {
    @Autowired
    private IApproveFlowService approveFlowService;

    @Autowired
    private IApproveDetailService approveDetailService;

    @Autowired
    private IApproveModelService approveModelService;

    @Autowired
    private IApproveFlowInfoService approveFlowInfoService;

    /**
     * 客户端获取审批流程。分页查询。
     * @param start
     * @param length
     * @param status
     * @param uuid
     * @param client
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveFlowPageClient", method = RequestMethod.GET)
    public ResultMsg getApproveFlowPageClient(@RequestParam("start") int start, @RequestParam("length") int length, Integer status, String uuid, Integer client) {
        ResultMsg resultMsg = new ResultMsg();
        //解析查询的开始时间和结束时间
        Map<String, Date> timeMap = DateUtil.analyzeQueryTime("all", "", "");
        try {

            int count = approveFlowService.countApproveFlowPageClient(status, timeMap, uuid);
            List<ApproveFlow> approveFlows = approveFlowService.getApproveFlowPageClient(start, length, status, timeMap, uuid);

            count += approveFlowService.countApproveFlowPageClient(client, timeMap, uuid);
            List<ApproveFlow> approveFlowPage = approveFlowService.getApproveFlowPageClient(start, length, client, timeMap, uuid);
            approveFlows.addAll(approveFlowPage);
            //根据是否需要当前登录账户审批
//                approveFlowService.isNeedApprove(approveFlows);

            resultMsg.setRows(approveFlows);
            resultMsg.setTotal(count);
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
     * 根据流程id，获取流程环节的模型
     *
     * @param approveFlowId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApproveFlowModelClient", method = RequestMethod.GET)
    public ResultMsg getApproveFlowModelClient(Integer approveFlowId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据流程id，获取该流程全部细节集合
            List<ApproveDetail> detailList = approveDetailService.getApproveDetailListByFlowId(approveFlowId);

            //获取当前流程对象
            ApproveFlow approveFlow = approveFlowService.getApproveFlowInfoById(approveFlowId);

            //将流程细节集合转化为模型
            detailList = approveDetailService.convertDetailListToModel(detailList);

            JSONObject resJson = new JSONObject();
            resJson.put("detailList", detailList);
            resJson.put("pointId", approveFlow.getPointId());
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

}
