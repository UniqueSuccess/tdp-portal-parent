package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.policy.entity.PolicyPotentialRiskDO;
import cn.goldencis.tdp.policy.entity.PolicyPotentialRiskDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PolicyPotentialRiskDOMapper extends BaseDao {
    long countByExample(PolicyPotentialRiskDOCriteria example);

    int deleteByExample(PolicyPotentialRiskDOCriteria example);

    int deleteByPrimaryKey(@Param("riskId") Integer riskId, @Param("policyId") Integer policyId);

    int insert(PolicyPotentialRiskDO record);

    int insertSelective(PolicyPotentialRiskDO record);

    List<PolicyPotentialRiskDO> selectByExampleWithRowbounds(PolicyPotentialRiskDOCriteria example, RowBounds rowBounds);

    List<PolicyPotentialRiskDO> selectByExample(PolicyPotentialRiskDOCriteria example);

    int updateByExampleSelective(@Param("record") PolicyPotentialRiskDO record, @Param("example") PolicyPotentialRiskDOCriteria example);

    int updateByExample(@Param("record") PolicyPotentialRiskDO record, @Param("example") PolicyPotentialRiskDOCriteria example);
}