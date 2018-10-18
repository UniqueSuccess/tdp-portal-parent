/**
 *
 */
package cn.goldencis.tdp.core.utils;

import java.util.ResourceBundle;

/**
 * 读取配置文件
 * @author Administrator
 *
 */
public class PathConfig {
    private static ResourceBundle bundle;
    public final static String HOM_PATH;
    public final static String JEDIS_HOST;
    public final static Integer JEDIS_PORT;
    public final static String JEDIS_OPEN;
    public final static String ACCESS_SERVER_IP;//准入服务器IP
    public final static String DEVICE_UNIQUE_CMD; //设备唯一码查询命令
    public final static String VDP_AUTH_FILE_NAME; //TSA授权文件名
    public final static String VDP_URL; //VDP统一登录页面
    public final static String VDP_LOGIN_URL;//TSA单一登录页面
    public final static String READ_AUTH_PATH;//解密授权信息文件
    public final static String POLICY_BASECATALOG;//策略json文件的存放根目录
    public final static String POLICY_JSONFILENAME;//策略json文件的保存的文件名

    public final static String PACKAGEUPLOADPATH;//vdp安装包上传目录路径

    public final static String PACKAGEUPLOADFILENAME;//上传的vdp安装包的存放文件名

    public final static String UPDATEUPLOADPATH;//vdp升级包的上传目录路径

    public final static String UPDATEUPLOADFILENAME;//vdp升级包的存放文件名

    public final static String CLIENTUPDATEPATH;//客户端下载更新资源的路径，为客户端开放流使用

    public final static String GOLBALCFG_PATH;//全局配置文件路径golbalcfg.path

    public final static String APPROVE_FILEPATH;//审批文件存放路径

    public final static String VIDEOTRANSFERLOG_ROOTPATH;//视频流转日志摘要文件存放路径

    public final static String VIDEOTRANSFERLOG_DIRPATH;//视频流转日志摘要文件存放根目录路径

    public final static String EXPORT_PATH;//导出文件存放目录

    public final static String TEMPLET_PATH;//模板文件存放目录

    public final static String PLUGINS_PATH;//插件文件存放目录

    public final static String PRINTSCREEN_PATH;//模板文件存放目录

    public final static String IFCONFIGUTILS_LOADPATH;//ifconfig工具加载路径

    static {
        bundle = ResourceBundle.getBundle("tdp-common");

        HOM_PATH = bundle.getString("homepath");
        JEDIS_HOST = bundle.getString("jedis.host");
        JEDIS_PORT = Integer.parseInt(bundle.getString("jedis.port"));
        JEDIS_OPEN = bundle.getString("jedis.open");
        ACCESS_SERVER_IP = bundle.getString("accessServer.ip");
        DEVICE_UNIQUE_CMD = bundle.getString("device.unique.cmd");
        VDP_AUTH_FILE_NAME = bundle.getString("tdp_auth_file_name");
        VDP_URL = bundle.getString("tdp_url");
        VDP_LOGIN_URL = bundle.getString("tdp_login_url");
        READ_AUTH_PATH = bundle.getString("readauthpath");
        POLICY_BASECATALOG = bundle.getString("policy.BaseCatalog");
        POLICY_JSONFILENAME = bundle.getString("policy.JsonFileName");

        PACKAGEUPLOADPATH = bundle.getString("packageupload.path");
        PACKAGEUPLOADFILENAME = bundle.getString("packageupload.fileName");
        UPDATEUPLOADPATH = bundle.getString("updateupload.path");
        UPDATEUPLOADFILENAME = bundle.getString("updateupload.fileName");
        CLIENTUPDATEPATH = bundle.getString("clientupdate.path");
        GOLBALCFG_PATH = bundle.getString("golbalcfg.path");
        APPROVE_FILEPATH = bundle.getString("approve.filePath");
        VIDEOTRANSFERLOG_ROOTPATH = bundle.getString("videotransferlog.rootPath");
        VIDEOTRANSFERLOG_DIRPATH = bundle.getString("videotransferlog.dirPath");
        EXPORT_PATH = bundle.getString("export.path");
        TEMPLET_PATH = bundle.getString("templet.path");
        PLUGINS_PATH = bundle.getString("plugins.path");
        PRINTSCREEN_PATH = bundle.getString("printscreen.path");
        IFCONFIGUTILS_LOADPATH = bundle.getString("IFConfigUtilsLoadPath");
    }

}
