package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveDetailCriteria;
import cn.goldencis.tdp.common.service.BaseService;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * Created by limingchao on 2018/1/16.
 */
public interface IApproveDetailService extends BaseService<ApproveDetail, ApproveDetailCriteria> {

    /**
     * 仅批准当前细节步骤
     *
     * @param detail
     */
    void dealWithCurrentDetail(ApproveDetail detail);

    /**
     * 根据流程id和环节id，查询所属所有细节列表
     *
     * @param flowId
     * @param pointId
     * @return
     */
    List<ApproveDetail> getapproveDetailListCurrentPoint(Integer flowId, Integer pointId);

    /**
     * 根据id获取审批细节步骤
     *
     * @param approveDetailId
     * @return
     */
    ApproveDetail getApproveDetailById(Integer approveDetailId);

    /**
     * 根据流程id，获取该流程全部细节集合
     *
     * @param approveFlowId
     * @return
     */
    List<ApproveDetail> getApproveDetailListByFlowId(Integer approveFlowId);

    /**
     * 将流程细节集合转化为有序的细节名称，以json数组的形式返回。
     *
     * @param detailList
     * @return
     */
    JSONArray convertDetailListToLinkedNameJsonArray(List<ApproveDetail> detailList);

    /**
     * 将流程细节集合转化为模型
     *
     * @param detailList
     * @return
     */
    List<ApproveDetail> convertDetailListToModel(List<ApproveDetail> detailList);

    /**
     * 根据流程id，获取该流程已经审批完成的细节集合
     *
     * @param approveFlowId
     * @return
     */
    List<ApproveDetail> getApproveDetailDoneListByFlowId(Integer approveFlowId);

    /**
     * 将流程细节集合转化为有序列表
     *
     * @param detailList
     * @return
     */
    List<ApproveDetail> convertToLinkedDetailList(List<ApproveDetail> detailList);

    /**
     * 获取当前账户待审批细节id
     *
     * @param detailList
     * @param pointId
     * @return
     */
    Integer getDetailNeedToApproveId(List<ApproveDetail> detailList, Integer pointId);

    /**
     * 为流程细节设置审批人姓名
     *
     * @param detailList
     */
    void fillWithApproverName(List<ApproveDetail> detailList);
}
