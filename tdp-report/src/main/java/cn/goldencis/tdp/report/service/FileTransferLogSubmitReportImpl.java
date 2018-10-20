package cn.goldencis.tdp.report.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.service.IExternalSubmitReport;
import cn.goldencis.tdp.report.dao.TFileTransferLogMapper;
import cn.goldencis.tdp.report.entity.TFileTransferLog;
@Component
public class FileTransferLogSubmitReportImpl implements IExternalSubmitReport {
    private final static Log LOG = LogFactory.getLog(FileTransferLogSubmitReportImpl.class);
    @Value("${videotransferlog.rootPath}${filetransferlog.dirPath}")
    private String filePath;

    @Autowired
    private TFileTransferLogMapper fileTransferLogMapper;
    @Override
    public boolean support(String type) {
        if(!StringUtil.isEmpty(type) && ConstantsDto.SUBMIT_REPORT_FILE_TRANSFER.equals(type)){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> action(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
        try {
            JSONObject argvJson = (JSONObject)(params.get("argvJson"));
            if (params.containsKey("file")) {
                MultipartFile file = (MultipartFile)params.get("file");
                String dirPath = filePath + "/" + DateUtil.getFormatDate("yyyyMMdd");
                long time = System.currentTimeMillis();
                FileUpload fileUpload = new FileUpload();
                String fileName = argvJson.getString("fileName");
                fileUpload.uploadFile(file, dirPath, time + "_" + fileName);
                //保存文件路径
                argvJson.put("filePath", dirPath + "/" + time + "_" + fileName);
            }
            //有审批流程的不上传文件 从流程中获取文件地址
            if (argvJson.containsKey("tranunique") && !StringUtil.isEmpty(argvJson.getString("tranunique"))) {
                String flowFilePath = fileTransferLogMapper.queryApproveFlowAttachment(argvJson.getString("usrunique"), argvJson.getString("tranunique"));
                if (!StringUtil.isEmpty(flowFilePath)) {
                    flowFilePath = flowFilePath.substring(1);
                    argvJson.put("filePath", SysContext.getRequest().getServletContext().getRealPath("/") + flowFilePath);
                }
            }
            //保存至数据库表
            fileTransferLogMapper.insert(convertLog(argvJson));
            result.put("resultCode", ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            LOG.error("外发日志上报异常", e);
        }
        return result;
    }
    public TFileTransferLog convertLog(JSONObject argvJson) throws ParseException {
        TFileTransferLog log = new TFileTransferLog();
        log.setApplicantId(argvJson.getString("usrunique"));
        log.setTranUnique(argvJson.getString("tranunique"));
        log.setDepartmentId(argvJson.containsKey("depid") ? (StringUtil.isEmpty(argvJson.getString("depid")) ? 0 : Integer.valueOf(argvJson.getString("depid"))) : 0);
        log.setFftype(argvJson.getInt("fftype"));
        log.setFileName(argvJson.getString("fileName"));
        log.setFileType(convertFileType(log.getFileName()));
        log.setIp(argvJson.containsKey("ip") ? argvJson.getString("ip") : "");
        log.setReceiver(argvJson.getJSONObject("extradata").getString("receiver"));
        log.setReason(argvJson.getJSONObject("extradata").containsKey("reason") ? argvJson.getJSONObject("extradata").getString("reason") : "");
        log.setTransferTime(DateUtil.getDateByStrWithFormat(argvJson.getString("time"), DateUtil.DateTimeFormat));
        log.setFilePath(argvJson.containsKey("filePath") ? argvJson.getString("filePath") : "");
        return log;
    }
    public String convertFileType(String fileName) {
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        return type.substring(0, 3);
    }
}
