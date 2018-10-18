package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveDetailCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ApproveDetailMapper extends BaseDao {
    long countByExample(ApproveDetailCriteria example);

    int deleteByExample(ApproveDetailCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApproveDetail record);

    int insertSelective(ApproveDetail record);

    List<ApproveDetail> selectByExampleWithRowbounds(ApproveDetailCriteria example, RowBounds rowBounds);

    List<ApproveDetail> selectByExample(ApproveDetailCriteria example);

    ApproveDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApproveDetail record, @Param("example") ApproveDetailCriteria example);

    int updateByExample(@Param("record") ApproveDetail record, @Param("example") ApproveDetailCriteria example);

    int updateByPrimaryKeySelective(ApproveDetail record);

    int updateByPrimaryKey(ApproveDetail record);
}