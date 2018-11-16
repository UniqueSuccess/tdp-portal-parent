package cn.goldencis.tdp.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String getMD5ByFile(String path) {
        String MD5Value = "";
        InputStream in  = null;
        try {
            in = new FileInputStream(path);
            MD5Value = DigestUtils.md5Hex(in);
        } catch (Exception e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return MD5Value;
    }
}
