package cn.goldencis.tdp.core.utils;

import cn.goldencis.tdp.common.utils.XmlUtil;
import org.dom4j.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by limingchao on 2018/5/23.
 * 校验合法时间的工具类
 */
public class LegalTimeUtil {

    //全局配置文件的读取路径，项目启动时需要加载
    private static String cfgPath;

    //是否开启合法时间校验
    private static boolean open = false;

    //每周合法时间的日期
    private static List<Integer> week;

    //工具中默认的时间格式化样式
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    //合法时间的开始时间
    private static Date startTime;

    //合法时间的结束时间
    private static Date endTime;

    //是否开启声音报警
    private static boolean sound;

    /**
     * 读取配置文件，刷新合法时间参数
     * @return 成功返回true，修改原有参数，失败返回false，不修改原有参数。
     */
    public static boolean refreshClientLegalTime() {
        List<Integer> weekTmp;
        Date startTimeTmp;
        Date endTimeTmp;
        boolean soundTmp;

        try {
            //查询xml中的标签
            Document document = XmlUtil.getDocument(cfgPath);
            //解析周几
            List<String> weekXml = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@week", document);
            //解析开始时间
            List<String> begin = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@begin", document);
            //解析结束时间
            List<String> end = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@end", document);
            //解析声音报警按钮
            List<String> soundXml = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@sound", document);

            if (weekXml != null && weekXml.size() > 0) {
                //按照XMl的格式拆分
                String[] whatDay = weekXml.get(0).split(";");

                if (whatDay != null && whatDay.length > 0) {
                    weekTmp = new ArrayList<>();
                    for (int i = 0; i < whatDay.length; i++) {
                        //转化为int
                        int day = Integer.parseInt(whatDay[i]);
                        //判断周几是否合法
                        if (day < 1 || day > 7) {
                            return false;
                        }
                        //并转为Calendar对应的星期几
                        if (day == 7) {
                            day = 1;
                        } else {
                            day += 1;
                        }
                        //添加到临时集合
                        weekTmp.add(day);
                    }

                    //排序
                    weekTmp.sort(Integer::compareTo);
                } else {
                    return false;
                }
            } else {
                return false;
            }

            //进行时间转化
            if (begin != null && begin.size() > 0) {
                startTimeTmp = formatTime(begin.get(0));
            } else {
                return false;
            }
            if (end != null && end.size() > 0) {
                endTimeTmp = formatTime(end.get(0));
            } else {
                return false;
            }

            //声音开关转化
            if (soundXml != null && soundXml.size() > 0) {
                soundTmp = Boolean.valueOf(soundXml.get(0));
            } else {
                soundTmp = false;
            }

            //解析成功，赋值
            week = weekTmp;
            startTime = startTimeTmp;
            endTime = endTimeTmp;
            sound = soundTmp;
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //按照指定格式化时间，pattern="HH:mm"的格式，日期统一为默认的1970.01.01，在该日期下，仅比较时间

    public static Date formatTime(String pattern) throws ParseException {
        return sdf.parse(pattern);
    }
    /**
     * 校验传入的日期时间是否为合法时间，是否在一周符合的日期中，是否在符合的时间段内
     * @param checkTime 需要检验的日期时间
     * @return 该日期时间合法则返回true，不合法返回false
     */
    public static boolean checkLegalTime(Date checkTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkTime);
        //首先校验日期，传入的日期，是否在合法日期集合中。
        boolean flag = week.contains(calendar.get(Calendar.DAY_OF_WEEK));
        //不在合法日期集合中,判断为非合法时间，返回false
        if (flag) {
            try {
                //校验时间是否合法
                String pattern = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date time = formatTime(pattern);

                //校验时间，在开始时间之后，并且在结束时间之前，为合法返回true
                if (time.after(startTime) && time.before(endTime)) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void setCfgPath(String cfgPath) {
        LegalTimeUtil.cfgPath = cfgPath;
    }

    public static boolean isSound() {
        return sound;
    }
}
