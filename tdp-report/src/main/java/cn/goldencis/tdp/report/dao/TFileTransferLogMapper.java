package cn.goldencis.tdp.report.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.report.entity.TFileTransferLog;
import cn.goldencis.tdp.report.entity.TFileTransferLogCriteria;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TFileTransferLogMapper extends BaseDao {
    long countByExample(TFileTransferLogCriteria example);

    int deleteByExample(TFileTransferLogCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(TFileTransferLog record);

    int insertSelective(TFileTransferLog record);

    List<TFileTransferLog> selectByExampleWithRowbounds(TFileTransferLogCriteria example, RowBounds rowBounds);

    List<TFileTransferLog> selectByExample(TFileTransferLogCriteria example);

    TFileTransferLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TFileTransferLog record, @Param("example") TFileTransferLogCriteria example);

    int updateByExample(@Param("record") TFileTransferLog record, @Param("example") TFileTransferLogCriteria example);

    int updateByPrimaryKeySelective(TFileTransferLog record);

    int updateByPrimaryKey(TFileTransferLog record);

    String queryApproveFlowAttachment(@Param("usrunique")String usrunique, @Param("tranunique")String tranunique);

    List<TFileTransferLog> queryFileTransferLog(Map<String, Object> params);

    Integer queryFileTransferLogCount(Map<String, Object> params);
}