package cn.goldencis.tdp.core.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by limingchao on 2018/1/5.
 */
@Mybatis
public interface CClientUserDOMapper {

    /**
     * 传入需要修改的部门id，将该id部门下的用户，置为未分组
     * @param departmentId 部门id
     */
    void setClientUsersUngroup(@Param(value = "departmentId") Integer departmentId);

    /**
     * 统计用户存在策略潜在风险的数量
     * @param policyIdList 策略id集合
     * @return 统计存在风险的用户数量
     */
    Integer countPotentialRiskClientUser(@Param(value = "policyIdList") List<Integer> policyIdList);
}
