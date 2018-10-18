package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.policy.entity.FingerprintDO;
import cn.goldencis.tdp.policy.entity.FingerprintDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface FingerprintDOMapper extends BaseDao {
    long countByExample(FingerprintDOCriteria example);

    int deleteByExample(FingerprintDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(FingerprintDO record);

    int insertSelective(FingerprintDO record);

    List<FingerprintDO> selectByExampleWithBLOBsWithRowbounds(FingerprintDOCriteria example, RowBounds rowBounds);

    List<FingerprintDO> selectByExampleWithBLOBs(FingerprintDOCriteria example);

    List<FingerprintDO> selectByExampleWithRowbounds(FingerprintDOCriteria example, RowBounds rowBounds);

    List<FingerprintDO> selectByExample(FingerprintDOCriteria example);

    FingerprintDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FingerprintDO record, @Param("example") FingerprintDOCriteria example);

    int updateByExampleWithBLOBs(@Param("record") FingerprintDO record, @Param("example") FingerprintDOCriteria example);

    int updateByExample(@Param("record") FingerprintDO record, @Param("example") FingerprintDOCriteria example);

    int updateByPrimaryKeySelective(FingerprintDO record);

    int updateByPrimaryKeyWithBLOBs(FingerprintDO record);

    int updateByPrimaryKey(FingerprintDO record);
}