package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.entity.ApproveModelCriteria;
import cn.goldencis.tdp.common.service.BaseService;

import java.util.List;

/**
 * Created by limingchao on 2018/1/16.
 */
public interface IApproveModelService extends BaseService<ApproveModel, ApproveModelCriteria> {

    /**
     * 根据流程定义id，获取定义流程的环节信息，并以此构建审批流程模型
     *
     * @param approveDefinitionId
     * @return
     */
    List<ApproveModel> getApproveModelByDefinitionId(Integer approveDefinitionId);

    /**
     * 通过审批环节名获取数据库中的审批环节
     *
     * @param approveModelName
     * @return
     */
    ApproveModel getApproveModelByName(String approveModelName, Integer flowId);

    /**
     * 校验在同一个流程中，是否有重名的环节名称
     *
     * @param approveModel
     * @return
     */
    boolean checkApproveModelNameDuplicate(ApproveModel approveModel);

    /**
     * 添加或新建审批环节，没有id为新建，有id为更新
     *
     * @param approveModel
     */
    void addOrUpdateApproveModel(ApproveModel approveModel);

    /**
     * 根据审批流程模型，获取其中审批人的guid集合
     *
     * @param modelList
     * @return
     */
    List<String> getApproverIdListByModelList(List<ApproveModel> modelList);

    /**
     * 根据环节id，校验该环节是否为流程中唯一的环节
     *
     * @param flowId
     * @return
     */
    boolean checkApproveModelSingle(Integer flowId);

    /**
     * 根据环节id，删除环节，并后一个环节与前一个环节连接。
     *
     * @param approveModelId
     */
    void deleteApproveModel(ApproveModel approveModelId);
}
