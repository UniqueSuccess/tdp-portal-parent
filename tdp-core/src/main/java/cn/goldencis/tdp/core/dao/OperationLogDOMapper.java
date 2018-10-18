package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.OperationLogDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface OperationLogDOMapper extends BaseDao {
    long countByExample(OperationLogDOCriteria example);

    int deleteByExample(OperationLogDOCriteria example);

    int deleteByPrimaryKey(Integer logId);

    int insert(OperationLogDO record);

    int insertSelective(OperationLogDO record);

    List<OperationLogDO> selectByExampleWithBLOBsWithRowbounds(OperationLogDOCriteria example, RowBounds rowBounds);

    List<OperationLogDO> selectByExampleWithBLOBs(OperationLogDOCriteria example);

    List<OperationLogDO> selectByExampleWithRowbounds(OperationLogDOCriteria example, RowBounds rowBounds);

    List<OperationLogDO> selectByExample(OperationLogDOCriteria example);

    OperationLogDO selectByPrimaryKey(Integer logId);

    int updateByExampleSelective(@Param("record") OperationLogDO record, @Param("example") OperationLogDOCriteria example);

    int updateByExampleWithBLOBs(@Param("record") OperationLogDO record, @Param("example") OperationLogDOCriteria example);

    int updateByExample(@Param("record") OperationLogDO record, @Param("example") OperationLogDOCriteria example);

    int updateByPrimaryKeySelective(OperationLogDO record);

    int updateByPrimaryKeyWithBLOBs(OperationLogDO record);

    int updateByPrimaryKey(OperationLogDO record);
}