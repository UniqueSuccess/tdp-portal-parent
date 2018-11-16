package cn.goldencis.tdp.system.service;

import java.util.Map;

public interface ITClientPackageLogService {

    void saveLog(Map<String, Object> params);

    Map<String, Object> querySubmitPackageInfo();

}
