package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.core.utils.PathConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 下载相关的控制器
 */
@Controller
public class DownloadController {

    /**
     * 准入页面
     * @return
     */
    @RequestMapping(value = "/usr", method = RequestMethod.GET)
    public ModelAndView accessPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("download/index");
        return modelAndView;
    }

    /**
     * 准入页面下载VDP客户端接口
     * Created by limingchao on 2018/1/29.
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadVDPPackage(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletContext().getRealPath(PathConfig.PACKAGEUPLOADPATH + "/" + PathConfig.PACKAGEUPLOADFILENAME);
        File file = new File(path);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("请先上传客户端安装包");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int index = PathConfig.PACKAGEUPLOADFILENAME.lastIndexOf(".");
        String fileName = PathConfig.PACKAGEUPLOADFILENAME.substring(0, index) + "_" + PathConfig.ACCESS_SERVER_IP + "_" + PathConfig.PACKAGEUPLOADFILENAME.substring(index);
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, fileName);
    }

    /**
     * 为客户端下载升级资源文件开放接口
     * @param fileName
     * @param type
     * @param request
     * @param response
     */
    @RequestMapping(value = "/resource/clientupdate/{fileName}.{type}")
    public void downloadVDPClientupdateInResource(@PathVariable("fileName") String fileName, @PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletContext().getRealPath(PathConfig.CLIENTUPDATEPATH + "/" + fileName + "." + type);
        File file = new File(path);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("服务器没有该资源");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }

    /**
     * 浏览器下载页面
     * @return
     */
    @RequestMapping(value = "/downloadBrowserPage", method = RequestMethod.GET)
    public ModelAndView downloadBrowserPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("browser/index");
        return modelAndView;
    }

    /**
     * 为客户下载浏览器开放接口
     * @param browserType
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadBrowser/{browserName}.{browserType}")
    public void downloadBrowserInResource(@PathVariable("browserName") String browserName, @PathVariable("browserType") String browserType, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletContext().getRealPath(PathConfig.TEMPLET_PATH + "/" + browserName + "." + browserType);
        File file = new File(path);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("请先上传该类型浏览器资源");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }

    @RequestMapping(value = "/downloadFingerprintPlugins")
    public void downloadFingerprintPlugins(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletContext().getRealPath(PathConfig.PLUGINS_PATH + "/SetupFP.exe");
        File file = new File(path);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("未上传指纹录入插件");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }

    @RequestMapping(value = "/downloadUsbKeyPlugins")
    public void downloadUsbKeyPlugins(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletContext().getRealPath(PathConfig.PLUGINS_PATH + "/VdpConsoleEnvKit.exe");
        File file = new File(path);
        if (!file.exists()) {
            try {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("未上传UKey插件");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }
}
