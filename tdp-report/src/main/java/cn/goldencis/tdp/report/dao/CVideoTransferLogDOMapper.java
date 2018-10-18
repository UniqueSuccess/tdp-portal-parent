package cn.goldencis.tdp.report.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/29.
 */
@Mybatis
public interface CVideoTransferLogDOMapper {

    List<Map<String, Object>> countVideoTransferTop5();
}
