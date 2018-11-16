package cn.goldencis.tdp.system.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;

import java.util.List;
import java.util.Map;

@Mybatis
public interface TClientPackageLogMapper {
    public void insert(Map<String, Object> params);
    public List<Map> queryLog();
}