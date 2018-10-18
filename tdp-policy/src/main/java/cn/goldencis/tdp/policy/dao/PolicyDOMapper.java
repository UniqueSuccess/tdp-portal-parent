package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.entity.PolicyDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PolicyDOMapper extends BaseDao {
    long countByExample(PolicyDOCriteria example);

    int deleteByExample(PolicyDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PolicyDO record);

    int insertSelective(PolicyDO record);

    List<PolicyDO> selectByExampleWithRowbounds(PolicyDOCriteria example, RowBounds rowBounds);

    List<PolicyDO> selectByExample(PolicyDOCriteria example);

    PolicyDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PolicyDO record, @Param("example") PolicyDOCriteria example);

    int updateByExample(@Param("record") PolicyDO record, @Param("example") PolicyDOCriteria example);

    int updateByPrimaryKeySelective(PolicyDO record);

    int updateByPrimaryKey(PolicyDO record);
}