package cn.goldencis.tdp.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.DepartmentDOCriteria;

@SuppressWarnings("rawtypes")
public interface DepartmentDOMapper extends BaseDao {
    long countByExample(DepartmentDOCriteria example);

    int deleteByExample(DepartmentDOCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(DepartmentDO record);

    int insertSelective(DepartmentDO record);

    List<DepartmentDO> selectByExampleWithRowbounds(DepartmentDOCriteria example, RowBounds rowBounds);

    List<DepartmentDO> selectByExample(DepartmentDOCriteria example);

    DepartmentDO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DepartmentDO record, @Param("example") DepartmentDOCriteria example);

    int updateByExample(@Param("record") DepartmentDO record, @Param("example") DepartmentDOCriteria example);

    int updateByPrimaryKeySelective(DepartmentDO record);

    int updateByPrimaryKey(DepartmentDO record);

    List<Map<String, String>> queryPolicyNameById(@Param("idList")List<Integer> idList);

    Integer queryPolicyIdByDepartmentId(@Param("deptguid")Integer deptguid);

    List<String> queryClientUserList(@Param("id")Integer id, @Param("policyId")Integer oldPolicyId);
}