package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.GeneratorCrc32Util;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.system.utils.ClientUpdateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by limingchao on 2018/1/10.
 */
@Controller
@PageLog(module = "系统设置-客户端")
@RequestMapping(value = "/systemSetting")
public class ServerController {


    @ResponseBody
    @PageLog(module = "上传客户端安装包文件", template = "", type = LogType.OTHER)
    @RequestMapping(value = "/uploadClientPackage")
    public ResultMsg uploadClientPackage(@RequestParam(value = "packageFile", required = false) MultipartFile packageFile) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            ServletContext servletContext = SysContext.getRequest().getServletContext();
            String dirPath = servletContext.getRealPath(PathConfig.PACKAGEUPLOADPATH);

            FileUpload fileUploader = new FileUpload();
            fileUploader.uploadFile(packageFile, dirPath, PathConfig.PACKAGEUPLOADFILENAME);

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
            ServletContext servletContext = SysContext.getRequest().getServletContext();
            String dirPath = servletContext.getRealPath(PathConfig.UPDATEUPLOADPATH);

            FileUpload fileUploader = new FileUpload();
            fileUploader.uploadFile(updateFile, dirPath, PathConfig.UPDATEUPLOADFILENAME);
            //在控制台上传升级包后，调用脚本解压升级包
            ClientUpdateUtil.executeClientUpdate(dirPath + "/" + PathConfig.UPDATEUPLOADFILENAME);
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
}
