package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.ScheduledTaskDO;
import cn.goldencis.tdp.core.entity.ScheduledTaskDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ScheduledTaskDOMapper extends BaseDao {
    long countByExample(ScheduledTaskDOCriteria example);

    int deleteByExample(ScheduledTaskDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScheduledTaskDO record);

    int insertSelective(ScheduledTaskDO record);

    List<ScheduledTaskDO> selectByExampleWithRowbounds(ScheduledTaskDOCriteria example, RowBounds rowBounds);

    List<ScheduledTaskDO> selectByExample(ScheduledTaskDOCriteria example);

    ScheduledTaskDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScheduledTaskDO record, @Param("example") ScheduledTaskDOCriteria example);

    int updateByExample(@Param("record") ScheduledTaskDO record, @Param("example") ScheduledTaskDOCriteria example);

    int updateByPrimaryKeySelective(ScheduledTaskDO record);

    int updateByPrimaryKey(ScheduledTaskDO record);
}