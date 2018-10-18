package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.ApproveDefinitionMapper;
import cn.goldencis.tdp.approve.dao.ApproveModelMapper;
import cn.goldencis.tdp.approve.entity.ApproveDefinition;
import cn.goldencis.tdp.approve.entity.ApproveDefinitionCriteria;
import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.entity.ApproveModelCriteria;
import cn.goldencis.tdp.approve.service.IApproveDefinitionService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by limingchao on 2018/1/16.
 */
@Service
public class ApproveDefinitionServiceImpl extends AbstractBaseServiceImpl<ApproveDefinition, ApproveDefinitionCriteria> implements IApproveDefinitionService {

    @Autowired
    private ApproveDefinitionMapper mapper;

    @Autowired
    private ApproveModelMapper modelMapper;

    @Override
    protected BaseDao<ApproveDefinition, ApproveDefinitionCriteria> getDao() {
        return mapper;
    }

    /**
     * 根据流程定义主键id，获取流程定义对象
     * @param approveDefinitionId
     */
    @Override
    public ApproveDefinition getByPrimaryKey(Integer approveDefinitionId) {
        return mapper.selectByPrimaryKey(approveDefinitionId);
    }

    /**
     * 获取全部审批流程列表
     * @return
     */
    @Override
    public List<ApproveDefinition> getAllApproveDefinition() {
        ApproveDefinitionCriteria example = new ApproveDefinitionCriteria();
        List<ApproveDefinition> definitionList = mapper.selectByExample(example);
        return definitionList;
    }

    /**
     * 根据流程定义的名称，查询审批流程的对象
     * @param approveName
     * @return
     */
    @Override
    public ApproveDefinition getApproveDefinitionByName(String approveName) {
        ApproveDefinitionCriteria example = new ApproveDefinitionCriteria();
        example.createCriteria().andNameEqualTo(approveName);
        List<ApproveDefinition> approveList = mapper.selectByExample(example);

        if (approveList != null && approveList.size() > 0) {
            return approveList.get(0);
        }

        return null;
    }

    /**
     * 检查审批流程名是否重复
     * @param approveDefinition
     * @return
     */
    @Override
    public boolean checkApproveDefinitionNameDuplicate(ApproveDefinition approveDefinition) {
        //通过审批流程名获取数据库中的审批记录
        ApproveDefinition preApproveDefinition = this.getApproveDefinitionByName(approveDefinition.getName());


        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preApproveDefinition != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preApproveDefinition.getId().equals(approveDefinition.getId())) {
                return true;
            }
            //若果不同，说明为两个审批流程，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    /**
     * 根据父类审批流程的id，创建审批流程，继承父类审批流程的环节
     * @param approveDefinition
     * @param modelList
     */
    @Override
    @Transactional
    public void copyApproveDefinitionFromParent(ApproveDefinition approveDefinition, List<ApproveModel> modelList) {
        //创建审批流程，回传获取流程id
        approveDefinition.setIsDefault(0);
        approveDefinition.setModifyTime(new Date());
        mapper.insertSelective(approveDefinition);

        if (modelList != null) {
            int seniorId = 0;
            //继承父类审批流程的环节
            for (ApproveModel point : modelList) {
                //id自增生成
                point.setId(null);
                //继承的环节，需要归属于新的流程id
                point.setFlowId(approveDefinition.getId());
                point.setModifyTime(new Date());
                //设置上一环节id
                point.setSeniorId(seniorId);

                //创建审批环节，回传环节id，作为下一环节的父类id
                modelMapper.insertSelective(point);
                seniorId = point.getId();
            }
        }
    }

    /**
     * 根据id删除流程的定义，同时删除其对应的环节
     * @param approveDefinitionId
     */
    @Override
    @Transactional
    public void deleteApproveDefinition(Integer approveDefinitionId) {
        //删除其对应的环节
        ApproveModelCriteria modelExample = new ApproveModelCriteria();
        modelExample.createCriteria().andFlowIdEqualTo(approveDefinitionId);
        modelMapper.deleteByExample(modelExample);

        //根据id删除流程的定义
        mapper.deleteByPrimaryKey(approveDefinitionId);
    }
}
