package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by limingchao on 2018/1/7.
 */
@Mybatis
public interface CUserNavigationDOMapper {

    /**
     * 为一个账户批量插入页面权限
     * @param userId
     * @param navigationIdList
     */
    void batchInsertByOneUserAndNavigationList(@Param("userId") String userId, @Param("navigationIdList") List<Integer> navigationIdList);

    /**
     * 删除账户页面权限关联表中关联该账户的记录
     * @param userId
     */
    void batchDeleteUserNavigationByUserId(String userId);

    /**
     * 通过用户的guid，查询所有关联的页面权限id集合
     * @param userGuid
     * @return
     */
    List<Integer> getNavigationListByUser(@Param(value = "userGuid") String userGuid);
}
