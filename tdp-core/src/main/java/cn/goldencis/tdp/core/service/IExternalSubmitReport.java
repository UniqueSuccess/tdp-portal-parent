package cn.goldencis.tdp.core.service;

import java.util.Map;

public interface IExternalSubmitReport {
    public boolean support(String type);
    public Map<String, Object> action(Map<String, Object> params);
}
