package cn.goldencis.tdp.common.utils;

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
}
