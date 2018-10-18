package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveDefinition;
import cn.goldencis.tdp.approve.entity.ApproveDefinitionCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ApproveDefinitionMapper extends BaseDao {
    long countByExample(ApproveDefinitionCriteria example);

    int deleteByExample(ApproveDefinitionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApproveDefinition record);

    int insertSelective(ApproveDefinition record);

    List<ApproveDefinition> selectByExampleWithRowbounds(ApproveDefinitionCriteria example, RowBounds rowBounds);

    List<ApproveDefinition> selectByExample(ApproveDefinitionCriteria example);

    ApproveDefinition selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApproveDefinition record, @Param("example") ApproveDefinitionCriteria example);

    int updateByExample(@Param("record") ApproveDefinition record, @Param("example") ApproveDefinitionCriteria example);

    int updateByPrimaryKeySelective(ApproveDefinition record);

    int updateByPrimaryKey(ApproveDefinition record);
}