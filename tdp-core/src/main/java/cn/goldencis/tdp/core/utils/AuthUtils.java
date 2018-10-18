package cn.goldencis.tdp.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;

import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;

public class AuthUtils {
    public static Map<String, Object> getAuthInfo(ServletContext servletContext) {
        Map<String, Object> authInfo = new HashMap<String, Object>();
        String deviceUnique = servletContext.getAttribute("deviceUnique") != null ? servletContext.getAttribute("deviceUnique").toString() : "";
        authInfo.put("deviceUnique", deviceUnique);
        // 读取授权文件
        String fileContent = "";
        File f = new File(PathConfig.HOM_PATH + "/" + ConstantsDto.AUTH_FILE_NAME);
        String authmsg = "";
        //增加个标识
        if (ConstantsDto.VALIDATE_FLAG == 1) {
            if (f.isFile() && f.exists()) {
                InputStreamReader read = null;
                BufferedReader reader = null;
                try {
                    read = new InputStreamReader(new FileInputStream(f), "UTF-8");
                    reader = new BufferedReader(read);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent += line;
                    }
                    JSONObject jasonObject;
                    String company = null;
                    String beginDate = null;
                    String endDate = null;
                    String supportDate = null;
                    Integer maxCustomerCnt = null;

                    Date nowDate = new Date();
                    String authContent = ReadOmsAuthUtil.readOmsAuth(fileContent);
                    if (authContent == null || "".equals(authContent)) {
                        authInfo.put("authmsg", "授权文件无效");
                        return authInfo;
                    }
                    jasonObject = JSONObject.parseObject(authContent);

                    company = jasonObject.get("company").toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //时间要么没字段 要么肯定有值
                    beginDate = !jasonObject.containsKey("startdate") ? "" : sdf.format(sdf.parse(jasonObject.get("startdate").toString()));
                    endDate = !jasonObject.containsKey("enddate") ? ConstantsDto.LONG_TIME_LIMIT : sdf.format(sdf.parse(jasonObject.get("enddate").toString()));
                    supportDate = !jasonObject.containsKey("maintaindate") ? ConstantsDto.LONG_TIME_LIMIT : sdf.format(sdf.parse(jasonObject.get("maintaindate").toString()));
                    //获取项目标识
                    String vdpFlag = jasonObject.get("product").toString();
                    //获取最大限制点数
                    maxCustomerCnt = (!jasonObject.containsKey("endpoints")) ? 0 : Integer.valueOf(jasonObject.get("endpoints").toString());

                    authInfo.put("company", company);
                    authInfo.put("beginEndDate", !ConstantsDto.LONG_TIME_LIMIT.equals(endDate) ? beginDate + " 到 " + endDate : endDate);
                    authInfo.put("supportDate", supportDate);
                    authInfo.put("deviceUnique", jasonObject.get("hwserial").toString());
                    authInfo.put("maxCustomerCnt", maxCustomerCnt);

                    if ("".equals(deviceUnique) || !deviceUnique.equals(jasonObject.get("hwserial").toString())) {
                        authmsg = "设备唯一标识码验证失败";
                    } else if (!ConstantsDto.LONG_TIME_LIMIT.equals(endDate) && nowDate.after(sdf.parse(endDate))) {//判断结束时间
                        authmsg = "不在授权期限内";
                    } else if (!ConstantsDto.PROCJECT_IDENTIFICATION.equalsIgnoreCase(vdpFlag)) {
                        authmsg = "项目标识不正确";
                    } else if (!"".equals(beginDate) && nowDate.before(sdf.parse(beginDate))) {//判断开始时间
                        authmsg = "不在授权期限内";
                    }

                    //提示信息
                    if (!ConstantsDto.LONG_TIME_LIMIT.equals(supportDate) && nowDate.after(DateUtil.strToDate(DateUtil.getDateAdd(-1, supportDate, "yyyy-MM-dd"), "yyyy-MM-dd"))) {
                        authInfo.put("promptMsg", "维保期限即将到期，请续保。");
                    }
                    //判断是否维保期限超期
                    if (!ConstantsDto.LONG_TIME_LIMIT.equals(supportDate) && nowDate.after(DateUtil.strToDate(DateUtil.getDateAddDay(1, supportDate, "yyyy-MM-dd"), "yyyy-MM-dd"))) {
                        authInfo.put("promptMsg", "维保期已到期，请续保。");
                    }

                } catch (Exception e) {
                    authmsg = "授权文件无效";
                } finally {
                    if (read != null) {
                        try {
                            read.close();
                        } catch (Exception e2) {
                        }
                    }

                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                        }
                    }
                }
            } else {
                authmsg = "授权文件不存在";
            }
        }
        authInfo.put("authmsg", authmsg);
//        authInfo.put("authmsg", "");
        return authInfo;
    }

    public static Map<String, Object> checkFileIsValidate(ServletContext servletContext, InputStream inStream) {
        Map<String, Object> authInfo = new HashMap<String, Object>();
        String authmsg = "";

        String deviceUnique = servletContext.getAttribute("deviceUnique") != null ? servletContext.getAttribute("deviceUnique").toString() : "";
        authInfo.put("deviceUnique", deviceUnique);
        if (ConstantsDto.VALIDATE_FLAG == 1) {
            try {
                String fileContent = IOUtils.toString(inStream, "UTF-8");
                JSONObject jasonObject;
                String authContent = ReadOmsAuthUtil.readOmsAuth(fileContent);
                if (authContent == null || "".equals(authContent)) {
                    authInfo.put("authmsg", "授权文件无效");
                    return authInfo;
                }

                jasonObject = JSONObject.parseObject(authContent);

                //以下做校验
                String vdpFlag = jasonObject.get("product").toString();

                if ("".equals(deviceUnique) || !deviceUnique.equals(jasonObject.get("hwserial").toString())) {
                    authmsg = "设备唯一标识码验证失败";
                } else if (!ConstantsDto.PROCJECT_IDENTIFICATION.equalsIgnoreCase(vdpFlag)) {
                    authmsg = "项目标识验证失败";
                } else if (!jasonObject.containsKey("company")) {
                    authmsg = "公司名称不存在";
                }

            } catch (Exception e) {
                //这里要求文件比较严格 上面若是有某个字段不正确 则抛出异常
                authmsg = "授权文件无效";
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //校验只需要校验格式
        authInfo.put("authmsg", !"".equals(authmsg) ? "授权文件无效" : authmsg);
        return authInfo;
    }

    public static Boolean checkFileExsits(String fileUrl, String fileName) {
        File file = new File(fileUrl, fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
