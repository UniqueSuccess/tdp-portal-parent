package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.entity.ApproveFlowCriteria;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.common.service.BaseService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/16.
 */
public interface IApproveFlowService extends BaseService<ApproveFlow, ApproveFlowCriteria> {

    /**
     * 发起审批流程
     * @param approveFlow
     * @param modelList
     */
    void submitApproveFlow(ApproveFlow approveFlow, List<ApproveModel> modelList);

    /**
     * 获取审批流程。分页查询。
     * @param start
     * @param length
     * @param submitDate
     * @param applicantOrType
     */
    List<ApproveFlow> getApproveFlowPageInPages(int start, int length, String submitDate, String applicantOrType);

    /**
     * 获取审批流程总数
     * @param submitDate
     * @param applicantOrType
     */
    int countApproveFlowInPages(String submitDate, String applicantOrType);

    /**
     * 当前环节审批通过，进入下一环节，如果没有下一个环节，审批流程通过
     * @param detail
     */
    ApproveFlow adoptCurrentPoint(ApproveDetail detail);

    /**
     * 拒绝审批请求，终止审批流程
     * @param detail
     */
    ApproveFlow refuseApproveFlow(ApproveDetail detail);

    /**
     * 根据查询条件，获取审批流程。分页查询
     * @param start
     * @param length
     * @param status
     * @param needOnly
     * @param timeMap
     * @param applicantOrType
     * @return
     */
    List<ApproveFlow> getApproveFlowPage(int start, int length, Integer status, Integer needOnly, Map<String, Date> timeMap, String applicantOrType);

    /**
     * 根据查询条件，获取审批流程的数量
     * @param status
     * @param needOnly
     * @param timeMap
     * @param applicantOrType
     * @return
     */
    int countApproveFlowPage(Integer status, Integer needOnly, Map<String, Date> timeMap, String applicantOrType);

    /**
     * 根据流程id，获取流程详细信息
     * @param approveFlowId
     * @return
     */
    ApproveFlow getApproveFlowInfoById(Integer approveFlowId);

    /**
     * 根据是否需要当前登录账户审批
     * @param approveFlows
     */
    void isNeedApprove(List<ApproveFlow> approveFlows);

    /**
     * 删除审批流程
     * @param approveFlowStr
     */
    void deleteApproveFlow(String approveFlowStr, String ctp);

    /**
     * 通知审批流程当前环节的审批人
     * @param approveFlow
     */
    void noticeApprover(ApproveFlow approveFlow);

    /**
     * 通知当前登录账户，更新待审批数量
     * @param userGuid
     */
    void noticeApproverByGuid(String userGuid);

    /**
     * 按照状态类型，统计当前审批请求数量
     * @param status 审批请求的状态
     * @return
     */
    long countApproveByState(Integer status);
}
