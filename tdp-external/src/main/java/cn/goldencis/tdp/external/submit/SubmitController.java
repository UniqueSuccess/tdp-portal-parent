package cn.goldencis.tdp.external.submit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.service.IExternalSubmitReport;

@Controller
@RequestMapping("/submit")
public class SubmitController implements ServletContextAware {
    private final static Log LOG = LogFactory.getLog(SubmitController.class);
    private ServletContext servletContext;
    @Autowired
    private List<IExternalSubmitReport> externalSubmitReport;

    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
    }

    /**
     * 外发日志提交接口
     * argv:
            {
                    "computername": "xielc-PC",
                    "dataType": "tGopLog",
                    "depid": "2",
                    "depname": "未分组",
                    "devunique": "909A61AD-4F7A-4843-B729-08D52A2F3643",
                    "extradata": {
                            "fileSize": 29696,
                            "reason": "lllllllllllllllllllllllllllll",
                            "receiver": "ooooooooooo"
                    },
                    "fftype": 2,
                    "fileName": "1537523043517242.xls",
                    "time": "2018-10-18 13:53:06",
                    "tranunique": "20181018133955",
                    "truename": "xielc",
                    "username": "xielc",
                    "usrunique": "74b52de3-4dc5-4806-87b2-3897d5fb4c6b"
            }
     */
    @ResponseBody
    @RequestMapping(value = "/submitReport", method = RequestMethod.POST)
    public Map<String, Object> submitReport(String argv, @RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            /**
             *
             * 首先判断文件是否有需要保存的文件
             *  如果有 
             *          判断文件大小   
             *          先保存文件获取地址
             *  将数据保存至数据库表中
             */
            JSONObject argvJson = JSONObject.fromObject(argv);
            if (argvJson.getString("dataType") == null) {
                result.put("resultMsg", "缺少数据类型【dataType】");
                return result;
            }
            if (file !=null && !file.isEmpty()) {
                //校验文件大小是否一致
                if (argvJson.containsKey("extradata") && argvJson.getJSONObject("extradata").containsKey("fileSize") && !(argvJson.getJSONObject("extradata").getLong("fileSize") == file.getSize())) {
                    result.put("resultMsg", "文件大小不一致");
                    return result;
                }
                params.put("file", file);
            }
            params.put("argvJson", argvJson);
            //externalSubmitReport.stream().filter(impl -> impl.support(argvJson.getString("dataType"))).findFirst().ifPresent(impl -> impl.action(params));
            boolean flag = false;
            for (IExternalSubmitReport impl : externalSubmitReport) {
                if (impl.support(argvJson.getString("dataType"))) {
                    flag = true;
                    Map<String, Object> resultMap = impl.action(params);
                    if (ConstantsDto.RESULT_CODE_TRUE == Integer.valueOf(String.valueOf(resultMap.get("resultCode")))) {
                        result.put("resultCode", ConstantsDto.RESULT_CODE_TRUE);
                    }
                    break;
                }
            }
            if (!flag) {
                result.put("resultMsg", "数据类型错误，未找到对应数据解析器");
            }
        } catch (Exception e) {
            LOG.error("外发日志提交接口:", e);
            result.put("resultCode", ConstantsDto.RESULT_CODE_ERROR);
            result.put("resultMsg", "外发日志提交接口异常");
        }
        return result;
    }
}
