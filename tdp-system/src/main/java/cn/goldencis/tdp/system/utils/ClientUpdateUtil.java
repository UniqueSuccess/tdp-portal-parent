package cn.goldencis.tdp.system.utils;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.utils.PathConfig;

import java.io.IOException;

/**
 * 在控制台上传升级包后，调用脚本解压升级包的工具类
 * Created by limingchao on 2018/2/2.
 */
public class ClientUpdateUtil {

    public static void executeClientUpdate(String realPath) {
        try {
            Runtime.getRuntime().exec(PathConfig.READ_AUTH_PATH + "/" + ConstantsDto.EXECUTE_CLIENT_UPDATE_FILE_NAME + " " + realPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
