package cn.goldencis.tdp.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.goldencis.tdp.core.constants.ConstantsDto;

public class ReadOmsAuthUtil {
    public static String readOmsAuth(String textString) {
        InputStream in = null;
        BufferedReader read = null;
        StringBuilder sb = new StringBuilder();
        try {
            Process pro = Runtime.getRuntime()
                    .exec(PathConfig.READ_AUTH_PATH + File.separator + ConstantsDto.READ_VDP_AUTH_FILE_NAME + " "
                            + textString);
            pro.waitFor();
            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("授权信息：" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }

            try {
                if (read != null) {
                    read.close();
                }
            } catch (Exception e2) {
            }
        }

        return sb.toString();
    }
}
