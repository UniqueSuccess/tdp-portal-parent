package cn.goldencis.tdp.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangyi on 2018/9/26.
 */
public class ListUtils {
    public static boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    public static String covertStringByList(List<String> list, String space) {
        String split = ";";
        if (ListUtils.isEmpty(list)) {
            return "";
        }
        if (space != null) {
            split = space;
        }
        return String.join(split, list);
    }

    public static boolean contain(String type, String types) {
        List list = Arrays.asList(types.split(";"));
        if (list.contains(type)) {
            return true;
        }
        return false;
    }
}
