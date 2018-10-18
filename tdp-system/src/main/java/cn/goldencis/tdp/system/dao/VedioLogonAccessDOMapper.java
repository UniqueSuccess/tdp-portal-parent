package cn.goldencis.tdp.system.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDO;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface VedioLogonAccessDOMapper extends BaseDao {
    long countByExample(VedioLogonAccessDOCriteria example);

    int deleteByExample(VedioLogonAccessDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(VedioLogonAccessDO record);

    int insertSelective(VedioLogonAccessDO record);

    List<VedioLogonAccessDO> selectByExampleWithRowbounds(VedioLogonAccessDOCriteria example, RowBounds rowBounds);

    List<VedioLogonAccessDO> selectByExample(VedioLogonAccessDOCriteria example);

    VedioLogonAccessDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VedioLogonAccessDO record, @Param("example") VedioLogonAccessDOCriteria example);

    int updateByExample(@Param("record") VedioLogonAccessDO record, @Param("example") VedioLogonAccessDOCriteria example);

    int updateByPrimaryKeySelective(VedioLogonAccessDO record);

    int updateByPrimaryKey(VedioLogonAccessDO record);
}