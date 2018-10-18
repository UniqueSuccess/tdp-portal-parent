package cn.goldencis.tdp.report.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.report.entity.VideoTransferLogDO;
import cn.goldencis.tdp.report.entity.VideoTransferLogDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface VideoTransferLogDOMapper extends BaseDao {
    long countByExample(VideoTransferLogDOCriteria example);

    int deleteByExample(VideoTransferLogDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(VideoTransferLogDO record);

    int insertSelective(VideoTransferLogDO record);

    List<VideoTransferLogDO> selectByExampleWithRowbounds(VideoTransferLogDOCriteria example, RowBounds rowBounds);

    List<VideoTransferLogDO> selectByExample(VideoTransferLogDOCriteria example);

    VideoTransferLogDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VideoTransferLogDO record, @Param("example") VideoTransferLogDOCriteria example);

    int updateByExample(@Param("record") VideoTransferLogDO record, @Param("example") VideoTransferLogDOCriteria example);

    int updateByPrimaryKeySelective(VideoTransferLogDO record);

    int updateByPrimaryKey(VideoTransferLogDO record);
}