package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.PermissionNavigationDO;
import cn.goldencis.tdp.core.entity.PermissionNavigationDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PermissionNavigationDOMapper extends BaseDao {
    long countByExample(PermissionNavigationDOCriteria example);

    int deleteByExample(PermissionNavigationDOCriteria example);

    int insert(PermissionNavigationDO record);

    int insertSelective(PermissionNavigationDO record);

    List<PermissionNavigationDO> selectByExampleWithRowbounds(PermissionNavigationDOCriteria example, RowBounds rowBounds);

    List<PermissionNavigationDO> selectByExample(PermissionNavigationDOCriteria example);

    int updateByExampleSelective(@Param("record") PermissionNavigationDO record, @Param("example") PermissionNavigationDOCriteria example);

    int updateByExample(@Param("record") PermissionNavigationDO record, @Param("example") PermissionNavigationDOCriteria example);
}