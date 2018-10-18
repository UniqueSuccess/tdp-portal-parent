package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveFlowInfo;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfoCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ApproveFlowInfoMapper extends BaseDao {
    long countByExample(ApproveFlowInfoCriteria example);

    int deleteByExample(ApproveFlowInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApproveFlowInfo record);

    int insertSelective(ApproveFlowInfo record);

    List<ApproveFlowInfo> selectByExampleWithRowbounds(ApproveFlowInfoCriteria example, RowBounds rowBounds);

    List<ApproveFlowInfo> selectByExample(ApproveFlowInfoCriteria example);

    ApproveFlowInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApproveFlowInfo record, @Param("example") ApproveFlowInfoCriteria example);

    int updateByExample(@Param("record") ApproveFlowInfo record, @Param("example") ApproveFlowInfoCriteria example);

    int updateByPrimaryKeySelective(ApproveFlowInfo record);

    int updateByPrimaryKey(ApproveFlowInfo record);
}