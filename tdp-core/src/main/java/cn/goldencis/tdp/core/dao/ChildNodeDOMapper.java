package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ChildNodeDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ChildNodeDOMapper extends BaseDao {
    long countByExample(ChildNodeDOCriteria example);

    int deleteByExample(ChildNodeDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChildNodeDO record);

    int insertSelective(ChildNodeDO record);

    List<ChildNodeDO> selectByExampleWithRowbounds(ChildNodeDOCriteria example, RowBounds rowBounds);

    List<ChildNodeDO> selectByExample(ChildNodeDOCriteria example);

    ChildNodeDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChildNodeDO record, @Param("example") ChildNodeDOCriteria example);

    int updateByExample(@Param("record") ChildNodeDO record, @Param("example") ChildNodeDOCriteria example);

    int updateByPrimaryKeySelective(ChildNodeDO record);

    int updateByPrimaryKey(ChildNodeDO record);
}