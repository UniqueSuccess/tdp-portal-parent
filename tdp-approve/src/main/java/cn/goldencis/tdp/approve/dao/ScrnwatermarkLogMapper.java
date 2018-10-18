package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ScrnwatermarkLog;
import cn.goldencis.tdp.approve.entity.ScrnwatermarkLogCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ScrnwatermarkLogMapper extends BaseDao {
    long countByExample(ScrnwatermarkLogCriteria example);

    int deleteByExample(ScrnwatermarkLogCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScrnwatermarkLog record);

    int insertSelective(ScrnwatermarkLog record);

    List<ScrnwatermarkLog> selectByExampleWithRowbounds(ScrnwatermarkLogCriteria example, RowBounds rowBounds);

    List<ScrnwatermarkLog> selectByExample(ScrnwatermarkLogCriteria example);

    ScrnwatermarkLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScrnwatermarkLog record, @Param("example") ScrnwatermarkLogCriteria example);

    int updateByExample(@Param("record") ScrnwatermarkLog record, @Param("example") ScrnwatermarkLogCriteria example);

    int updateByPrimaryKeySelective(ScrnwatermarkLog record);

    int updateByPrimaryKey(ScrnwatermarkLog record);
}