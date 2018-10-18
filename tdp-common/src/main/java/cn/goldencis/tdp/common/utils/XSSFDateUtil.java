package cn.goldencis.tdp.common.utils;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;

/**
 * 自定义xssf日期工具类
 */
public class XSSFDateUtil extends DateUtil {

    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}
