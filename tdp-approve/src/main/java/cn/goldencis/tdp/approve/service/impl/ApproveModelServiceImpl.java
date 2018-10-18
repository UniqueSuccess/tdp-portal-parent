package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.ApproveModelMapper;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.entity.ApproveModelCriteria;
import cn.goldencis.tdp.approve.service.IApproveModelService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by limingchao on 2018/1/16.
 */
@Service
public class ApproveModelServiceImpl extends AbstractBaseServiceImpl<ApproveModel, ApproveModelCriteria> implements IApproveModelService{

    @Autowired
    private ApproveModelMapper mapper;

    @Override
    protected BaseDao<ApproveModel, ApproveModelCriteria> getDao() {
        return mapper;
    }

    /**
     * 根据流程定义id，获取定义流程的环节信息，并以此构建审批流程模型
     * @param approveDefinitionId
     * @return
     */
    @Override
    public List<ApproveModel> getApproveModelByDefinitionId(Integer approveDefinitionId) {
        //根据流程定义id，获取定义流程的所有环节信息
        ApproveModelCriteria example = new ApproveModelCriteria();
        example.createCriteria().andFlowIdEqualTo(approveDefinitionId);
        List<ApproveModel> approvePointList = mapper.selectByExample(example);

        //根据环节顺序，构建模型，默认第一个环节的seniorId为0
        int seniorId = 0;
        boolean isend = false;
        List<ApproveModel> approveModelList = new ArrayList<>();
        while (!isend) {
            for (ApproveModel point : approvePointList) {
                if (point.getSeniorId() == seniorId) {
                    approveModelList.add(point);
                    seniorId = point.getId();
                    isend = false;
                    break;
                }
                isend =true;
            }
        }
        return approveModelList;
    }

    /**
     * 通过审批环节名获取数据库中的审批环节
     * @param approveModelName
     * @return
     */
    @Override
    public ApproveModel getApproveModelByName(String approveModelName, Integer flowId) {
        ApproveModelCriteria example = new ApproveModelCriteria();
        example.createCriteria().andNameEqualTo(approveModelName);
        List<ApproveModel> approveModelList = mapper.selectByExample(example);

        if (approveModelList != null && approveModelList.size() > 0) {
            for (ApproveModel model : approveModelList) {
                if (flowId == model.getFlowId()) {
                    return model;
                }
            }
        }
        return null;
    }

    /**
     * 校验在同一个流程中，是否有重名的环节名称
     * @param approveModel
     * @return
     */
    @Override
    public boolean checkApproveModelNameDuplicate(ApproveModel approveModel) {
        //通过审批环节名获取数据库中的审批环节
        ApproveModel preApproveModel = this.getApproveModelByName(approveModel.getName(), approveModel.getFlowId());


        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preApproveModel != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preApproveModel.getId().equals(approveModel.getId())) {
                return true;
            }
            //若果不同，说明为两个审批环节，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    /**
     * 添加或新建审批环节，没有id为新建，有id为更新
     * @param approveModel
     */
    @Override
    @Transactional
    public void addOrUpdateApproveModel(ApproveModel approveModel) {

        //没有id为新建，打断原来的链路，新建环节的seniorId指向前一个环节，后一个环节的seniorId指向新建环节
        if (approveModel.getId() == null) {
            //根据审批流程定义id，插入一条新的环节，回传环节id
            mapper.insertSelective(approveModel);

            ApproveModelCriteria example = new ApproveModelCriteria();
            //根据新添加环节的seniorId，查询seniorId相同的环节
            example.createCriteria().andSeniorIdEqualTo(approveModel.getSeniorId()).andFlowIdEqualTo(approveModel.getFlowId()).andIdNotEqualTo(approveModel.getId());
            ApproveModel sufModel = new ApproveModel();
            //将该环节的seniorId设置为新建环节的回传id
            sufModel.setSeniorId(approveModel.getId());
            approveModel.setModifyTime(new Date());
            mapper.updateByExampleSelective(sufModel, example);
        } else {
            //有id为更新,直接更新即可
            approveModel.setModifyTime(new Date());
            mapper.updateByPrimaryKey(approveModel);
        }

    }

    /**
     * 根据审批流程模型，获取其中审批人的guid集合
     * @param modelList
     * @return
     */
    @Override
    public List<String> getApproverIdListByModelList(List<ApproveModel> modelList) {
        List<String> guidList = null;
        if (modelList != null && modelList.size() > 0) {
            guidList = new ArrayList<>();
            for (ApproveModel model : modelList) {
                String[] guidArr = model.getApprovers().split(";");
                if (guidArr != null && guidArr.length > 0) {
                    for (String guid : guidArr) {
                        guidList.add(guid);
                    }
                }
            }
        }
        return guidList;
    }

    /**
     * 根据环节id，校验该环节是否为流程中唯一的环节
     * @param flowId
     * @return
     */
    @Override
    public boolean checkApproveModelSingle(Integer flowId) {
        ApproveModelCriteria example = new ApproveModelCriteria();
        example.createCriteria().andFlowIdEqualTo(flowId);
        List<ApproveModel> modelList = mapper.selectByExample(example);

        if (modelList != null && modelList.size() > 1) {
            return false;
        }

        return true;
    }

    /**
     * 根据环节id，删除环节，并后一个环节与前一个环节连接。
     * @param approveModel
     */
    @Override
    @Transactional
    public void deleteApproveModel(ApproveModel approveModel) {
        //根据环节id,删除环节
        mapper.deleteByPrimaryKey(approveModel.getId());

        //后一个环节与前一个环节建立连接。
        ApproveModelCriteria example = new ApproveModelCriteria();
        example.createCriteria().andSeniorIdEqualTo(approveModel.getId());
        ApproveModel sufModel = new ApproveModel();
        sufModel.setSeniorId(approveModel.getSeniorId());
        sufModel.setModifyTime(new Date());
        mapper.updateByExampleSelective(sufModel, example);
    }
}
