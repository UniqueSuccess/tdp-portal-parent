package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * Created by limingchao on 2018/6/22.
 */
@Mybatis
public interface CScrnwatermarkLogDoMapper {

    /**
     * 新建水印日志的接口
     * @param authId 设备唯一标示
     * @param userGuid 用户guid
     * @param truename 用户真实姓名
     * @param applyInfo 日志附加信息
     * @param appliTime 生成日志时间
     */
    int insert(@Param(value = "authId") String authId, @Param(value = "userGuid") String userGuid, @Param(value = "truename") String truename, @Param(value = "applyInfo") String applyInfo, @Param(value = "appliTime") Date appliTime);

    /**
     * 新建水印日志的接口
     * @param paramMap 包含param的Map
     * @return 插入数量
     */
    int insertByParamMap(Map<String, Object> paramMap);

    /**
     * 根据水印日志id，更新水印id
     * @param id 水印日志id
     * @param scrnwatermarkId 水印id
     */
    void updateScrnwatermarkId(@Param(value = "id") Integer id, @Param(value = "scrnwatermarkId") String scrnwatermarkId);

    /**
     * 通过申请人的guid，查询对应的用户屏幕隐式水印id
     * @param guid 申请人guid，即用户的guid
     * @return 用户屏幕隐式水印id
     */
    String getScrnwatermarkIdByApplicantId(@Param(value = "guid") String guid);
}
