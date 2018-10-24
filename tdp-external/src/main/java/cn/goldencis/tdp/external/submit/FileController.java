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
import org.springframework.web.context.ServletContextAware;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.utils.JsonUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

@Controller
@RequestMapping("/file")
public class FileController  implements ServletContextAware {
    private final static Log LOG = LogFactory.getLog(FileController.class);
    private ServletContext servletContext;
    @Value("${videotransferlog.rootPath}")
    private String rootPath;
    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
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
                return;
            }
            if(!path.startsWith(rootPath)) {
                path = servletContext.getRealPath(path);
            }
            downLoad(path, response, result);
            response.addHeader("result", JsonUtil.getObjectToString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoad(String path, HttpServletResponse response, Map<String, Object> result) {
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
}
