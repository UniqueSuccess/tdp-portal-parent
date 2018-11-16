package cn.goldencis.tdp.external.submit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.utils.JsonUtil;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.core.utils.PathConfig;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

@Controller
@RequestMapping("/file")
public class FileController  implements ServletContextAware {
    private final static Log LOG = LogFactory.getLog(FileController.class);
    private ServletContext servletContext;
    @Value("${videotransferlog.rootPath}")
    private String rootPath;

    private String servletRootPath = null;;
    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
        servletRootPath = servletContext.getRealPath("/");
    }

    /**
     * IO流读取文件
     * @param imgPath
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ioReadFile", method = RequestMethod.GET)
    public void ioReadFile(String path, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
        try {
            LOG.info("接收到ip：" + NetworkUtil.getIpAddress(request) + "请求文件消息, 文件路径:" + path);
            /**
             * 校验文件目录是否合法
             * 是否以videotransferlog.rootPath开头
             *  如果是
             *          则直接使用该路径查找
             *  如果不是 则取tomcat下 路径
             *  
             */ 
            if (StringUtil.isEmpty(path)) {
                result.put("resultMsg", "缺少文件路径");
                response.addHeader("result", JsonUtil.getObjectToString(result));
                LOG.info("接收到ip：" + NetworkUtil.getIpAddress(request) + "请求文件消息, 回参结果：" + JsonUtil.getObjectToString(result));
                return;
            }
            if ((path.startsWith(ConstantsDto.CLIENT_GLOBAL)) || (path.startsWith(PathConfig.PACKAGEUPLOADPATH)) || (path.startsWith(PathConfig.UPDATEUPLOADPATH)) 
                    || (path.startsWith(PathConfig.CLIENTUPDATEPATH)) || (path.startsWith(PathConfig.POLICY_BASECATALOG) 
                            && path.endsWith(PathConfig.POLICY_JSONFILENAME))) {
                path =  PathConfig.HOM_PATH + path;
            } else if(!path.startsWith(rootPath)) {
                path = getServletRootPath() + (path.startsWith("/") || path.startsWith("\\") ? path.substring(1) : path);
            }
            LOG.info("接收到ip：" + NetworkUtil.getIpAddress(request) + "请求文件消息, 文件真实路径:" + path);
            downLoad(path, response, result, request);
            response.addHeader("result", JsonUtil.getObjectToString(result));
            LOG.info("接收到ip：" + NetworkUtil.getIpAddress(request) + "请求文件消息, 回参结果：" + JsonUtil.getObjectToString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoad(String path, HttpServletResponse response, Map<String, Object> result, HttpServletRequest request) {
         InputStream fis = null;
        OutputStream toClient = null;
        FileInputStream fisa = null;
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            if (!file.exists()) {
                result.put("resultMsg", "文件不存在");
                return;
            }
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            fisa = new FileInputStream(path);
            fis = new BufferedInputStream(fisa);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            LOG.info("接收到ip：" + NetworkUtil.getIpAddress(request) + "请求文件消息,文件大小：" + file.length() + ", 文件修改时间:" + file.lastModified());
            result.put("resultCode", ConstantsDto.RESULT_CODE_TRUE);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fisa != null) {
                try {
                    fisa.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (toClient != null) {
                try {
                    toClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getServletRootPath() {
        if (StringUtil.isEmpty(servletRootPath)) {
            servletRootPath = servletContext.getRealPath("/");
        }
        return servletRootPath;
    }

    public void setServletRootPath(String servletRootPath) {
        this.servletRootPath = servletRootPath;
    }

    @RequestMapping(value = "/queryFileExists", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryFileExists(String path, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
        try {
            /**
             * 校验文件目录是否合法
             * 是否以videotransferlog.rootPath开头
             *  如果是
             *          则直接使用该路径查找
             *  如果不是 则取tomcat下 路径
             *  
             */ 
            if (StringUtil.isEmpty(path)) {
                result.put("resultMsg", "缺少文件路径");
                return result;
            }
            if ((path.startsWith(ConstantsDto.CLIENT_GLOBAL)) || (path.startsWith(PathConfig.PACKAGEUPLOADPATH)) || (path.startsWith(PathConfig.UPDATEUPLOADPATH)) || 
                    (path.startsWith(PathConfig.CLIENTUPDATEPATH)) || (path.startsWith(PathConfig.POLICY_BASECATALOG) && 
                            path.endsWith(PathConfig.POLICY_JSONFILENAME))) {
                path =  PathConfig.HOM_PATH + path;
            } else if(!path.startsWith(rootPath)) {
                path = getServletRootPath() + (path.startsWith("/") || path.startsWith("\\") ? path.substring(1) : path);
            }
            File file = new File(path);
            if (file.exists()) {
                result.put("resultCode", ConstantsDto.RESULT_CODE_TRUE);
            } else {
                result.put("resultMsg", "文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/downloadAgent", method = RequestMethod.GET)
    public void downloadAgent(String departmentId, HttpServletRequest request, HttpServletResponse response) {
        String path = PathConfig.HOM_PATH + PathConfig.PACKAGEUPLOADPATH + "/" + PathConfig.PACKAGEUPLOADFILENAME;//request.getServletContext().getRealPath();
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

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = "2";
        }
        request.setAttribute("fileType", "exe");
        int index = PathConfig.PACKAGEUPLOADFILENAME.lastIndexOf(".");
        String ip = PathConfig.ACCESS_SERVER_IP.replaceAll("\\.", "_");
        String fileName = PathConfig.PACKAGEUPLOADFILENAME.substring(0, index) + "_" + ip + "_" + departmentId + "_" + PathConfig.PACKAGEUPLOADFILENAME.substring(index);
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, fileName);
    }
}
