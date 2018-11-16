package cn.goldencis.tdp.common.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * String工具类. <br>
 *

 */
public class StringUtil {

    /**
     * 功能：检查这个字符串是不是空字符串。<br/>
     * 如果这个字符串为null或者trim后为空字符串则返回true，否则返回false。
     *
     * @param chkStr
     *            被检查的字符串
     * @return boolean
     */
    public static boolean isEmpty(String chkStr) {
        if (chkStr == null) {
            return true;
        } else {
            return "".equals(chkStr.trim()) ? true : false;
        }
    }

    /**
     * 
     * @param str
     * @return
     */
    public static Integer getNumber(String str) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher a1 = pattern.matcher(str);
        String[] arr = a1.replaceAll(" ").split("\\s+");
        StringBuffer sb = new StringBuffer();
        
        for(int i = 0;i<arr.length;i++){
            sb.append(arr[i]);
        }
        return Integer.valueOf(StringUtil.isEmpty(sb.toString()) ? "0" :sb.toString());
    }

    /**
     * 如果字符串没有超过最长显示长度返回原字符串，否则从开头截取指定长度并加...返回。
     *
     * @param str
     *            原字符串
     * @param length
     *            字符串最长显示的长度
     * @return 转换后的字符串
     */
    public static String trimString(String str, int length) {
        if (str == null) {
            return "";
        } else if (str.length() > length) {
            return str.substring(0, length - 3) + "...";
        } else {
            return str;
        }
    }

    /**
     * 根据绝对路径返回最后文件名称
     * @author mll
     * @param path
     * @return String
     */
    public static String subLastPath(String path) {
        int index = path.lastIndexOf("\\");
        char[] ch = path.toCharArray();
        String lastString = String.copyValueOf(ch, index + 1, ch.length - index - 1);
        return lastString;
    }

    /**
     * 将数组转化成字符
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串数组转list
     * @param arrayStr
     * @return
     */
    public static List<String> arrayToList(String[] arrayStr){
        if (arrayStr != null) {
            List<String> rlist = new ArrayList<>();
            for (String str : arrayStr) {
                rlist.add(str.trim());
            }
            return rlist;
        }
        return null;
    }

    public static String stringToHex(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int n_1, n_2, n_3, n_4;
            char cT;
            int nC;
            if (c > '9') {
                cT = 'a';
                nC = (c - cT) + 10;
            } else {
                cT = '0';
                nC = (c - cT);
            }
            n_1 = nC / 8;
            n_2 = (nC % 8) / 4;
            n_3 = (nC % 4) / 2;
            n_4 = nC % 2;

            sb.append("" + n_1 + n_2 + n_3 + n_4);
        }
        return sb.toString();
    }

    public static String generateScrnwatermarkId(Integer markRecordId) {
        StringBuffer sb = new StringBuffer();
        //第一位的标志位置为1，用于实际的奇偶校验
        sb.append("1");

        //转化为2进制，并返回为字符串
        String numStr = intToBytes(markRecordId);

        //如果转化后的字符串长度不足19位，则在前面补0，补齐19位
        int length = numStr.length();
        if (length < 19 ) {
            for (int i = 0; i < 19 - length; i++) {
                sb.append("0");
            }
        }

        //将20的字符串按照0-3,4-7,8-11,12-15,16-19分为五组，使用奇校验，在后面添加一位校验位。
        numStr = sb.append(numStr).toString();
        //清空buffer
        sb.setLength(0);
        for (int i = 0; i < 5; i++) {
            //每行数据的字符串
            String subNum = numStr.substring(4 * i, 4 * (i + 1));

            //获取第一位数字
            int res = Integer.parseInt(String.valueOf(subNum.charAt(0)));
            for (int j = 1; j < subNum.length(); j++) {
                int next = Integer.parseInt(String.valueOf(subNum.charAt(j)));
                res = res ^ next;
            }
            res = res == 0 ? 1 : 0;

            sb.append(subNum).append(res);
        }

        //将第一位标志位替换为s，用于记录。
        sb.replace(0, 1, "s");

        return sb.toString();
    }

    /**
     *
     * 将int转换为32位的二进制，保存在字符串中返回。
     * @param num
     * @return
     */
    public static String intToStr32(int num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 31; i >= 0; i--) {
            // &1 也可以改为num&0x01,表示取最地位数字.
            sb.insert(0, (byte) (num & 1));
            // 右移一位.
            num >>= 1;
        }
        return sb.toString();
    }

    @Test
    public void testChange() {
        String res1 = generateScrnwatermarkId(25366);
        System.out.println(res1);

    }

    public static String intToBytes(Integer num) {
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toBinaryString(num));
        return sb.toString();
    }

    public static String getResultStrByBoolean(boolean flag) {
        if (flag) {
            return "成功";
        } else {
            return "失败";
        }
    }

    public static String getResultStrByResultCode(Integer resultCode) {
        if (resultCode == 1) {
            return "成功";
        } else if (resultCode == 0){
            return "失败";
        } else {
            return "错误";
        }
    }
}