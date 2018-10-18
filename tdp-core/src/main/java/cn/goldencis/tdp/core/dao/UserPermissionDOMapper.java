package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.UserPermissionDO;
import cn.goldencis.tdp.core.entity.UserPermissionDOCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserPermissionDOMapper extends BaseDao {
    long countByExample(UserPermissionDOCriteria example);

    int deleteByExample(UserPermissionDOCriteria example);

    int insert(UserPermissionDO record);

    int insertSelective(UserPermissionDO record);

    List<UserPermissionDO> selectByExampleWithRowbounds(UserPermissionDOCriteria example, RowBounds rowBounds);

    List<UserPermissionDO> selectByExample(UserPermissionDOCriteria example);

    int updateByExampleSelective(@Param("record") UserPermissionDO record, @Param("example") UserPermissionDOCriteria example);

    int updateByExample(@Param("record") UserPermissionDO record, @Param("example") UserPermissionDOCriteria example);
}