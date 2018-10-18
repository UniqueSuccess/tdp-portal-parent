package cn.goldencis.tdp.approve.dao;

import cn.goldencis.tdp.approve.entity.ApproveModel;
import cn.goldencis.tdp.approve.entity.ApproveModelCriteria;
import cn.goldencis.tdp.common.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ApproveModelMapper extends BaseDao {
    long countByExample(ApproveModelCriteria example);

    int deleteByExample(ApproveModelCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApproveModel record);

    int insertSelective(ApproveModel record);

    List<ApproveModel> selectByExampleWithRowbounds(ApproveModelCriteria example, RowBounds rowBounds);

    List<ApproveModel> selectByExample(ApproveModelCriteria example);

    ApproveModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApproveModel record, @Param("example") ApproveModelCriteria example);

    int updateByExample(@Param("record") ApproveModel record, @Param("example") ApproveModelCriteria example);

    int updateByPrimaryKeySelective(ApproveModel record);

    int updateByPrimaryKey(ApproveModel record);
}