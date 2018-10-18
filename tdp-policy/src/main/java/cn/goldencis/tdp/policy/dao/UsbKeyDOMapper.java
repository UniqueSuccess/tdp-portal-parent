package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.policy.entity.UsbKeyDO;
import cn.goldencis.tdp.policy.entity.UsbKeyDOCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UsbKeyDOMapper extends BaseDao {
    long countByExample(UsbKeyDOCriteria example);

    int deleteByExample(UsbKeyDOCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UsbKeyDO record);

    int insertSelective(UsbKeyDO record);

    List<UsbKeyDO> selectByExampleWithRowbounds(UsbKeyDOCriteria example, RowBounds rowBounds);

    List<UsbKeyDO> selectByExample(UsbKeyDOCriteria example);

    UsbKeyDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UsbKeyDO record, @Param("example") UsbKeyDOCriteria example);

    int updateByExample(@Param("record") UsbKeyDO record, @Param("example") UsbKeyDOCriteria example);

    int updateByPrimaryKeySelective(UsbKeyDO record);

    int updateByPrimaryKey(UsbKeyDO record);
}