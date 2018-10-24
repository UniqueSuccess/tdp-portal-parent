package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.entity.ClientUserDOCriteria;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ClientUserDOMapper extends BaseDao {
    long countByExample(ClientUserDOCriteria example);

    int deleteByExample(ClientUserDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClientUserDO record);

    int insertSelective(ClientUserDO record);

    List<ClientUserDO> selectByExampleWithRowbounds(ClientUserDOCriteria example, RowBounds rowBounds);

    List<ClientUserDO> selectByExample(ClientUserDOCriteria example);

    ClientUserDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClientUserDO record, @Param("example") ClientUserDOCriteria example);

    int updateByExample(@Param("record") ClientUserDO record, @Param("example") ClientUserDOCriteria example);

    int updateByPrimaryKeySelective(ClientUserDO record);

    int updateByPrimaryKey(ClientUserDO record);

    //批量插入
    void batchInsertClientUsersByList(@Param("clientUserList") List<ClientUserDO> clientUserList);

    int conutClientUserListByDepartmentId(Map<String, Object> params);

    List<ClientUserDO> getClientUserListByDepartmentId(Map<String, Object> params);

    void updateClientUser(Map<String, Object> params);

    ClientUserDO queryClientUserByComputerguid(@Param("computerguid")String computerguid);
    ClientUserDO  queryClientUserByGuId(@Param("guid")String guid);

    int queryCurrentCustomerCntExclude(String computerguid);

    void updateHeartbeat(@Param("usrunique")String usrunique, @Param("onlineTime")String onlineTime);

    void updateClientUserOnline(@Param("date")String date);

    void updateClientUserOffline(@Param("date")String date);

    Map<String, Object> queryDepartmentByUnique(@Param("usrunique")String usrunique);

    List<String> queryChangeClient(Map<String, Object> params);
}