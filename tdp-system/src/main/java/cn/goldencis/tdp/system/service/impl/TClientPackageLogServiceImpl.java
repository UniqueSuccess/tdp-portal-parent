package cn.goldencis.tdp.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.goldencis.tdp.system.dao.TClientPackageLogMapper;
import cn.goldencis.tdp.system.service.ITClientPackageLogService;
@Service
public class TClientPackageLogServiceImpl implements ITClientPackageLogService {

    @Autowired
    private TClientPackageLogMapper mapper;
    @Override
    public void saveLog(Map<String, Object> params) {
        mapper.insert(params);
    }
    @Override
    public Map<String, Object> querySubmitPackageInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("package", new HashMap<String, Object>());
        result.put("update", new HashMap<String, Object>());

        List<Map> list = mapper.queryLog();
        for(Map map : list) {
            result.put(String.valueOf(map.get("fileType")), map);
        }
        return result;
    }
}
