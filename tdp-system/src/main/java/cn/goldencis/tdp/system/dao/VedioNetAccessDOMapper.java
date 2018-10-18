package cn.goldencis.tdp.system.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.system.entity.VedioNetAccessDO;
import cn.goldencis.tdp.system.entity.VedioNetAccessDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface VedioNetAccessDOMapper extends BaseDao {
    long countByExample(VedioNetAccessDOCriteria example);

    int deleteByExample(VedioNetAccessDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(VedioNetAccessDO record);

    int insertSelective(VedioNetAccessDO record);

    List<VedioNetAccessDO> selectByExampleWithRowbounds(VedioNetAccessDOCriteria example, RowBounds rowBounds);

    List<VedioNetAccessDO> selectByExample(VedioNetAccessDOCriteria example);

    VedioNetAccessDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VedioNetAccessDO record, @Param("example") VedioNetAccessDOCriteria example);

    int updateByExample(@Param("record") VedioNetAccessDO record, @Param("example") VedioNetAccessDOCriteria example);

    int updateByPrimaryKeySelective(VedioNetAccessDO record);

    int updateByPrimaryKey(VedioNetAccessDO record);
}