package cn.goldencis.tdp.core.dao;

import java.util.List;

import cn.goldencis.tdp.common.annotation.Mybatis;
import cn.goldencis.tdp.core.entity.DepartmentDO;

@Mybatis
public interface CDepartmentDOMapper {
    List<DepartmentDO> selectNoParentDepartment();
}