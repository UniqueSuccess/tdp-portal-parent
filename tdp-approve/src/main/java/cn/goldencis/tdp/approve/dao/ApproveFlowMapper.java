package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.entity.ApproveFlowCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ApproveFlowMapper extends BaseDao {
    long countByExample(ApproveFlowCriteria example);

    int deleteByExample(ApproveFlowCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApproveFlow record);

    int insertSelective(ApproveFlow record);

    List<ApproveFlow> selectByExampleWithRowbounds(ApproveFlowCriteria example, RowBounds rowBounds);

    List<ApproveFlow> selectByExample(ApproveFlowCriteria example);

    ApproveFlow selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApproveFlow record, @Param("example") ApproveFlowCriteria example);

    int updateByExample(@Param("record") ApproveFlow record, @Param("example") ApproveFlowCriteria example);

    int updateByPrimaryKeySelective(ApproveFlow record);

    int updateByPrimaryKey(ApproveFlow record);
}