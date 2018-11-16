package cn.goldencis.tdp.approve.controller;

import cn.goldencis.tdp.approve.entity.ScrnwatermarkLog;
import cn.goldencis.tdp.approve.service.IScrnwatermarkLogService;
import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IDepartmentService;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.service.IClientUserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * Created by limingchao on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/scrnwatermark")
public class ScrnwatermarkLogController {

    @Autowired
    private IScrnwatermarkLogService scrnwatermarkLogService;

    @Autowired
    private IClientUserService clientUserService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IOperationLogService logService;

    /**
     * 客户端获取49位屏幕水印随机id的接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/generateScrnwatermarkId", method = RequestMethod.POST)
    public Map<String, Object> generateScrnwatermarkId(String argv, HttpServletRequest request) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            //转化参数
            JSONObject paramJson = JSONObject.parseObject(argv);

            ScrnwatermarkLog watermark = new ScrnwatermarkLog();
            Map<String, Object> authInfo = AuthUtils.getAuthInfo(request.getServletContext());
            String authId = (String) authInfo.get("deviceUnique");
            watermark.setAuthId(authId);
            watermark.setApplicantId(paramJson.getString("userid"));
            watermark.setApplicantName(paramJson.getString("truename"));
            watermark.setApplyInfo(paramJson.toString());
            watermark.setApplyTime(new Date());

            //生成水印记录并回传记录唯一id
            scrnwatermarkLogService.addScrnwatermarkLog(watermark);

            //2的19次方为最大值
            if (watermark.getId() > 524287) {
                resultMsg.put("state", ConstantsDto.APPROVE_SUBMIT_FAILED);
                resultMsg.put("data", "生成水印id失败");
                return resultMsg;
            }

            //生成水印id
            String scrnwatermarkId = StringUtil.generateScrnwatermarkId(watermark.getId());
            watermark.setScrnwatermarkId(scrnwatermarkId);
            scrnwatermarkLogService.updateScrnwatermarkLogById(watermark);

            resultMsg.put("state", ConstantsDto.APPROVE_SUBMIT_SUCCESS);
            resultMsg.put("data", scrnwatermarkId);
        } catch (Exception e) {
            resultMsg.put("state", ConstantsDto.APPROVE_SUBMIT_FAILED);
            resultMsg.put("data", "生成水印id错误");
        }

        return resultMsg;
    }

    /**
     * 根据水印日志的id获取水印日志信息
     *
     * @param logId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getScrnwatermarkLogByLogId")
    public ResultMsg getScrnwatermarkLogByLogId(String logId) {

        ResultMsg resultMsg = new ResultMsg();

        try {

            //根据水印日志的id获取水印日志信息
            ScrnwatermarkLog watermark = scrnwatermarkLogService.getScrnwatermarkLogByLogId(logId);

            if (watermark != null) {
                //判断是否为用户的屏幕隐式水印
                JSONObject info = JSONObject.parseObject(watermark.getApplyInfo());
                if ("用户屏幕水印".equals(info.getString("type"))) {
                    //添加用户当前信息
                    ClientUserDO clientUser = clientUserService.getClientUserByGuid(watermark.getApplicantId());
                    DepartmentDO department = departmentService.getDepartmentById(clientUser.getDeptguid());
                    scrnwatermarkLogService.setClientUserIntoInfo(info, clientUser, department);
                    watermark.setApplyInfo(info.toJSONString());
                }

                resultMsg.setData(watermark);
                resultMsg.setResultMsg("获取水印日志成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("对应的水印日志不存在！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取水印日志错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/getScrnwatermarkLogByPic")
    public ResultMsg getScrnwatermarkLogByPic(MultipartFile pic, HttpServletRequest request) {

        ResultMsg resultMsg = new ResultMsg();
        File file = null;
        try {
            String fileName = UUID.randomUUID().toString();
            ServletContext servletContext = SysContext.getRequest().getServletContext();
            String dirPath = servletContext.getRealPath(PathConfig.PRINTSCREEN_PATH);
            String filePath = dirPath + "/" + fileName;
            file = new File(filePath);

            FileUpload fileUpload = new FileUpload();
            fileUpload.uploadFile(pic, dirPath, fileName);

            String logId = null;
            ScrnwatermarkLog log = null;

            //使用Python来识别图片的方法
            String[] logIdArr = scrnwatermarkLogService.scanPictureByPython(file.getAbsolutePath());

            if (logIdArr != null) {
                for (int i = 0; i < logIdArr.length; i++) {
                    System.out.println(logIdArr[i]);
                    log = scrnwatermarkLogService.getScrnwatermarkLogByLogId(logIdArr[i]);
                    if (log != null) {
                        logId = log.getScrnwatermarkId();
                        break;
                    }
                }
            }

            if (logId != null) {
                resultMsg.setData(logId);
                resultMsg.setResultMsg("通过图片获取水印id成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("对应的水印日志不存在！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取水印日志错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }

        return resultMsg;
    }

    /**
     * VDP6.0.0升级到VDP6.1.0版本，用户屏幕水印记录的升级接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/repairClientUserScrnwatermark", method = RequestMethod.GET)
    public ResultMsg repairClientUserScrnwatermark() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            List<ClientUserDO> allClientUser = clientUserService.getAllClientUser();
            for (ClientUserDO clientUser : allClientUser) {
                //判断数据库中是否存在该用户对应的屏幕隐式水印记录，没有就添加
                if (!scrnwatermarkLogService.isExistsClientUserScrnwatermark(clientUser.getGuid())) {
                    //为用户生成的屏幕隐式水印id和添加对应的水印日志
                    clientUserService.addScrnwatermarkLog(clientUser);
                }
            }

            resultMsg.setResultMsg("修复用户屏幕水印成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("修复用户屏幕水印错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
