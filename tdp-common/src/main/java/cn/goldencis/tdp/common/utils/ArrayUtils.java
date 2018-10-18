package cn.goldencis.tdp.common.utils;

/**
 * Created by limingchao on 2018/1/26.
 */
public class ArrayUtils {

    public static String array2String(String[] array) {
        StringBuilder sb = new StringBuilder();
        if (array != null) {
            for (String s : array) {
                sb.append(s);
                sb.append(",");
            }
            int l = sb.length();
            if (l > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
        return "";
    }

}
