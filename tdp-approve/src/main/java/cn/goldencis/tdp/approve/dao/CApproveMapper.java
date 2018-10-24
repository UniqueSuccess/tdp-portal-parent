package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.common.annotation.Mybatis;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by limingchao on 2018/1/17.
 */
@Mybatis
public interface CApproveMapper {

    /**
     * 批量添加审批流程细节步骤
     *
     * @param detailList
     */
    void batchInsertApproveDetailList(@Param(value = "detailList") List<ApproveDetail> detailList);

    /**
     * 根据查询条件，获取审批流程。分页查询
     *
     * @param start
     * @param length
     * @param status
     * @param userGuid
     * @param startDate
     * @param endDate
     * @param applicantOrType
     * @return
     */
    List<ApproveFlow> getApproveFlowPage(@Param("start") int start, @Param("length") int length, @Param("status") Integer status,
                                         @Param("userGuid") String userGuid, @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate, @Param("applicantOrType") String applicantOrType);

    /**
     * 根据查询条件，获取审批流程。分页查询
     *
     * @param start
     * @param length
     * @param status
     * @param userGuid
     * @param startDate
     * @param endDate
     * @return
     */
    List<ApproveFlow> getApproveFlowPageClient(@Param("start") int start, @Param("length") int length, @Param("status") Integer status,
                                         @Param("userGuid") String userGuid, @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate, @Param("uuid") String uuid);


    /**
     * 根据查询条件，获取审批流程的数量
     * @param status
     * @param userGuid
     * @param startDate
     * @param endDate
     * @param applicantOrType
     */
    long countApproveFlowPage(@Param("status") Integer status, @Param("userGuid") String userGuid, @Param("startDate") Date startDate,
                              @Param("endDate") Date endDate, @Param("applicantOrType") String applicantOrType);

    /**
     * 根据查询条件，获取审批流程的数量
     * @param status
     * @param userGuid
     * @param startDate
     * @param endDate
     * @param uuid
     */
    long countApproveFlowPageClient(@Param("status") Integer status, @Param("userGuid") String userGuid, @Param("startDate") Date startDate,
                              @Param("endDate") Date endDate, @Param("uuid") String uuid);

    /**
     * 按照guid统计对应账户还有多少流程正在审批中
     * @param userGuid
     * @return
     */
    long countNeedToApprove(@Param("userGuid") String userGuid);

    /**
     * 按照状态类型，统计当前审批请求数量
     * @param status 审批请求的状态
     * @return
     */
    long countApproveByState(@Param("userGuid") String guid, @Param("status") Integer status);
}
