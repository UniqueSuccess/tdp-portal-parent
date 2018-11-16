package cn.goldencis.tdp.system.controller;

import java.util.HashMap;
import java.util.Map;

import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.GeneratorCrc32Util;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.system.service.ITClientPackageLogService;
import cn.goldencis.tdp.system.utils.ClientUpdateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by limingchao on 2018/1/10.
 */
@Controller
@PageLog(module = "系统设置-客户端")
@RequestMapping(value = "/systemSetting")
public class ServerController {

    @Autowired
    private ITClientPackageLogService tClientPackageLogService;

    @ResponseBody
    @PageLog(module = "上传客户端安装包文件", template = "", type = LogType.OTHER)
    @RequestMapping(value = "/uploadClientPackage")
    public ResultMsg uploadClientPackage(@RequestParam(value = "packageFile", required = false) MultipartFile packageFile) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //ServletContext servletContext = SysContext.getRequest().getServletContext();
            String dirPath = PathConfig.HOM_PATH + PathConfig.PACKAGEUPLOADPATH;//servletContext.getRealPath(PathConfig.PACKAGEUPLOADPATH);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("fileName", "安装包文件/" + packageFile.getOriginalFilename());
            params.put("fileSize", packageFile.getSize());
            params.put("user", GetLoginUser.getLoginUser().getGuid());
            params.put("fileType", "package");

            FileUpload fileUploader = new FileUpload();
            fileUploader.uploadFile(packageFile, dirPath, PathConfig.PACKAGEUPLOADFILENAME);

            tClientPackageLogService.saveLog(params);

            resultMsg.setResultMsg("上传文件成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("上传文件错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @PageLog(module = "上传客户端升级文件", template = "", type = LogType.OTHER)
    @RequestMapping(value = "/uploadClientUpdate")
    public ResultMsg uploadClientUpdate(@RequestParam(value = "updateFile", required = false) MultipartFile updateFile, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //ServletContext servletContext = SysContext.getRequest().getServletContext();
            String dirPath = PathConfig.HOM_PATH + PathConfig.UPDATEUPLOADPATH;//servletContext.getRealPath(PathConfig.UPDATEUPLOADPATH);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("fileName", "升级包文件/" + updateFile.getOriginalFilename());
            params.put("fileSize", updateFile.getSize());
            params.put("user", GetLoginUser.getLoginUser().getGuid());
            params.put("fileType", "update");

            FileUpload fileUploader = new FileUpload();
            fileUploader.uploadFile(updateFile, dirPath, PathConfig.UPDATEUPLOADFILENAME);
            //在控制台上传升级包后，调用脚本解压升级包
            ClientUpdateUtil.executeClientUpdate(dirPath + "/" + PathConfig.UPDATEUPLOADFILENAME);
            tClientPackageLogService.saveLog(params);
            resultMsg.setResultMsg("上传文件成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("上传文件错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/generatorCrc32", method = RequestMethod.POST)
    public ResultMsg generatorCrc32(String code) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            String resCrc32 = GeneratorCrc32Util.getCrc32Gd(code);

            resultMsg.setData(resCrc32);
            resultMsg.setResultMsg("生成动态验证码成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("生成动态验证码错误");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/querySubmitPackageInfo", method = RequestMethod.GET)
    public ResultMsg querySubmitPackageInfo(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            Map<String, Object> result = tClientPackageLogService.querySubmitPackageInfo();
            resultMsg.setData(result);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setResultMsg("获取上传安装文件信息");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
