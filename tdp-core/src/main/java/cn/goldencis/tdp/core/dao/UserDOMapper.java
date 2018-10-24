package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.entity.UserDOCriteria;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserDOMapper extends BaseDao {
    long countByExample(UserDOCriteria example);

    int deleteByExample(UserDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    List<UserDO> selectByExampleWithRowbounds(UserDOCriteria example, RowBounds rowBounds);

    List<UserDO> selectByExample(UserDOCriteria example);

    UserDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserDO record, @Param("example") UserDOCriteria example);

    int updateByExample(@Param("record") UserDO record, @Param("example") UserDOCriteria example);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    //后添加的方法
    int queryRefusePromptUser(@Param("userId") String userId);

    int insertRefusePromptUser(@Param("userId") String userId);

    Map<String, Object> queryErrorLoginInfo(@Param("userId") String userId);

    void updateErrorLoginCount(@Param("userId") String userId, @Param("lastDate") String lastDate,
                               @Param("errorCount") int errorCount);

    List<UserDO> getUserListByLoginUserRoleTypeInPages(Map<String, Object> params);

    int countUserListByLoginUserRoleTypeInPages(Map<String, Object> params);
}