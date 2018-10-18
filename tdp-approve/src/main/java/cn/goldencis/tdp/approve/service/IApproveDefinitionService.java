package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ApproveDefinition;
import cn.goldencis.tdp.approve.entity.ApproveDefinitionCriteria;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.common.service.BaseService;

import java.util.List;

/**
 * Created by limingchao on 2018/1/16.
 */
public interface IApproveDefinitionService extends BaseService<ApproveDefinition, ApproveDefinitionCriteria> {

    /**
     * 根据流程定义主键id，获取流程定义对象
     * @param approveDefinitionId
     */
    ApproveDefinition getByPrimaryKey(Integer approveDefinitionId);

    /**
     * 获取全部审批流程列表
     * @return
     */
    List<ApproveDefinition> getAllApproveDefinition();

    /**
     * 根据流程定义的名称，查询审批流程的对象
     * @param approveName
     * @return
     */
    ApproveDefinition getApproveDefinitionByName(String approveName);

    /**
     * 检查审批流程名是否重复
     * @param approveDefinition
     * @return
     */
    boolean checkApproveDefinitionNameDuplicate(ApproveDefinition approveDefinition);

    /**
     * 根据父类审批流程的id，创建审批流程，继承父类审批流程的环节
     * @param approveDefinition
     * @param modelList
     */
    void copyApproveDefinitionFromParent(ApproveDefinition approveDefinition, List<ApproveModel> modelList);

    /**
     * 根据id删除流程的定义，同时删除其对应的环节
     * @param approveDefinitionId
     */
    void deleteApproveDefinition(Integer approveDefinitionId);
}
