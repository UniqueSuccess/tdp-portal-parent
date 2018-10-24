package cn.goldencis.tdp.core.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> createMap(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return map;
    }
}
