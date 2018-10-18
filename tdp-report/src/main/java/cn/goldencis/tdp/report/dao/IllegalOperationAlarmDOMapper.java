package cn.goldencis.tdp.report.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDO;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface IllegalOperationAlarmDOMapper extends BaseDao {
    long countByExample(IllegalOperationAlarmDOCriteria example);

    int deleteByExample(IllegalOperationAlarmDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IllegalOperationAlarmDO record);

    int insertSelective(IllegalOperationAlarmDO record);

    List<IllegalOperationAlarmDO> selectByExampleWithRowbounds(IllegalOperationAlarmDOCriteria example, RowBounds rowBounds);

    List<IllegalOperationAlarmDO> selectByExample(IllegalOperationAlarmDOCriteria example);

    IllegalOperationAlarmDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IllegalOperationAlarmDO record, @Param("example") IllegalOperationAlarmDOCriteria example);

    int updateByExample(@Param("record") IllegalOperationAlarmDO record, @Param("example") IllegalOperationAlarmDOCriteria example);

    int updateByPrimaryKeySelective(IllegalOperationAlarmDO record);

    int updateByPrimaryKey(IllegalOperationAlarmDO record);
}