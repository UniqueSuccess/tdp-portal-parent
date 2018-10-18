package cn.goldencis.tdp.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyi on 2018/10/11.
 */
public class HttpServletRequestUtils {
    public static Map<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
    public static Map<String, Object> replaceStr2List(Map<String, Object> params, String... strs) {
        for(String str : strs){
            if(params.get(str) != null && !StringUtils.isEmpty(params.get(str).toString())) {
                params.put(str, Arrays.asList(params.get(str).toString().split(";")));
            }
        }
        return params;
    }
}
