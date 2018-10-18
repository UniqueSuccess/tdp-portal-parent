package cn.goldencis.tdp.core.utils;

import cn.goldencis.tdp.common.utils.SysContext;

import java.io.File;

/**
 * Created by limingchao on 2018/1/19.
 */
public class CommonFileUtil {

    public static String getGlobalcfgtime() {

        String realPath = SysContext.getRequest().getSession().getServletContext().getRealPath(PathConfig.GOLBALCFG_PATH);
        File globalCfgFile = new File(realPath);

        if (globalCfgFile.exists()) {
            long globalcfgtime = globalCfgFile.lastModified();
            //为符合Python端的处理，截掉毫秒
            return (globalcfgtime + "").substring(0,10);
        }
        return null;
    }
}
