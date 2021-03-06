package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.UserDepartmentDO;
import cn.goldencis.tdp.core.entity.UserDepartmentDOCriteria;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@SuppressWarnings("rawtypes")
public interface UserDepartmentDOMapper extends BaseDao {
    long countByExample(UserDepartmentDOCriteria example);

    int deleteByExample(UserDepartmentDOCriteria example);

    int insert(UserDepartmentDO record);

    int insertSelective(UserDepartmentDO record);

    List<UserDepartmentDO> selectByExampleWithRowbounds(UserDepartmentDOCriteria example, RowBounds rowBounds);

    List<UserDepartmentDO> selectByExample(UserDepartmentDOCriteria example);

    int updateByExampleSelective(@Param("record") UserDepartmentDO record, @Param("example") UserDepartmentDOCriteria example);

    int updateByExample(@Param("record") UserDepartmentDO record, @Param("example") UserDepartmentDOCriteria example);
}