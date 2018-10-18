package cn.goldencis.tdp.system.controller;

import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDO;
import cn.goldencis.tdp.system.entity.VedioNetAccessDO;
import cn.goldencis.tdp.system.service.IClientLegalTimeService;
import cn.goldencis.tdp.system.service.IVedioLogonAccessService;
import cn.goldencis.tdp.system.service.IVedioNetAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/8.
 */
@Controller
@PageLog(module = "视频平台")
@RequestMapping(value = "/systemClient")
public class ClientController {

    @Autowired
    private IVedioNetAccessService vedioNetAccessService;

    @Autowired
    private IVedioLogonAccessService vedioLogonAccessService;

    @Autowired
    private IClientLegalTimeService clientLegalTimeService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("system/client/index");
        return modelAndView;
    }

    /**
     * 查询客户端页面，视频业务网络访问管控列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVedioNetAccessPages", method = RequestMethod.GET)
    public ResultMsg getVedioNetAccessPages() {
        ResultMsg resultMsg= new ResultMsg();
        try {

            //从全局配置的XML文件中，查询视频业务网络访问管控列表
            List<String> vedioLogonAccessList = vedioNetAccessService.getVedioNetAccessListFromCfgXml();

            resultMsg.setData(vedioLogonAccessList);
            resultMsg.setResultMsg("查询视频业务网络访问管控列表成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("查询视频业务网络访问管控列表错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 查询客户端页面，视频业务登录方式管控列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVedioLogonAccessPages", method = RequestMethod.GET)
    public ResultMsg getVedioLogonAccessPages() {
        ResultMsg resultMsg= new ResultMsg();
        try {

            //从全局配置的XML文件中，查询视频业务登录方式管控列表
            List<VedioLogonAccessDO> vedioLogonAccessList = vedioLogonAccessService.getVedioLogonAccessListFromCfgXml();

            resultMsg.setData(vedioLogonAccessList);
            resultMsg.setResultMsg("查询视频业务登录方式管控列表成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("查询视频业务登录方式管控列表错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 查询客户端合法登陆时间
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getClientLegalTime", method = RequestMethod.GET)
    public ResultMsg getClientLegalTime() {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //查询客户端合法登陆时间
            Map<String, String> legalTime  = clientLegalTimeService.getClientLegalTime();

            resultMsg.setData(legalTime);
            resultMsg.setResultMsg("查询客户端合法登陆时间成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("查询客户端合法登陆时间错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 视频业务网络访问 添加接口
     * @param vedioNetAccess
     * @return
     */
    @ResponseBody
    @PageLog(module = "新增视频业务网络访问", template = "新增IP/IP段：%s", args = "0.ip", type = LogType.INSERT)
    @RequestMapping(value = "/addVedioNetAccess", method = RequestMethod.POST)
    public ResultMsg addVedioNetAccess(VedioNetAccessDO vedioNetAccess) {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //添加视频业务网络访问
            vedioNetAccessService.addVedioNetAccessIntoXml(vedioNetAccess);

            resultMsg.setResultMsg("添加视频业务网络访问成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("添加视频业务网络访问错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 视频业务登录方式 添加接口
     * @param vedioLogonAccess
     * @return
     */
    @ResponseBody
    @PageLog(module = "新增视频业务登陆方式", template = "新增业务名称：%s，启动方式：%s", args = "0.name,0.startPath", type = LogType.INSERT)
    @RequestMapping(value = "/addVedioLogonAccess", method = RequestMethod.POST)
    public ResultMsg addVedioLogonAccess(VedioLogonAccessDO vedioLogonAccess) {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //添加视频业务登录方式
            vedioLogonAccessService.addVedioLogonAccessIntoXml(vedioLogonAccess);

            resultMsg.setResultMsg("添加视频业务登录方式成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("添加视频业务登录方式错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 修改客户端合法登陆时间的接口
     * @param week 每周几为合法登陆时间
     * @param begin 每天的开始时间
     * @param end 每天的结束时间
     * @return
     */
    @ResponseBody
    @PageLog(module = "更新客户端合法登陆时间", template = "合法登陆时间为：%s，限定每天时间为：%s - %s", args = "0,1,2", type = LogType.INSERT)
    @RequestMapping(value = "/updateClientLegalTime", method = RequestMethod.POST)
    public ResultMsg updateClientLegalTime(String week, String begin, String end,@RequestParam(value = "sound", defaultValue = "false") boolean sound) {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //与客户端确认，配置文件设置时，不能为空，否者客户端不予处理
            if (StringUtils.isEmpty(week) || StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) {
                //排除三项都为空的情况，即不设置合法时间
                if (!(StringUtils.isEmpty(week) && StringUtils.isEmpty(begin) && StringUtils.isEmpty(end))) {
                    resultMsg.setResultMsg("更新客户端合法登陆时间失败，设置不能为空!");
                    resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                    return resultMsg;
                }
            }

            //添加视频业务登录方式
            clientLegalTimeService.updateClientLegalTime(week, begin, end, sound);

            resultMsg.setResultMsg("更新客户端合法登陆时间成功!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("更新客户端合法登陆时间错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }


    /**
     * 删除视频业务网络访问 接口
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除视频业务网络访问", template = "删除IP/IP段：%s", args = "0.ip", type = LogType.DELETE)
    @RequestMapping(value = "/deleteVedioNetAccess", method = RequestMethod.POST)
    public ResultMsg deleteVedioNetAccess(VedioNetAccessDO vedioNetAccess) {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //删除视频业务网络访问
            boolean flag = vedioNetAccessService.deleteVedioNetAccessFromCfgXml(vedioNetAccess);

            if (flag) {
                resultMsg.setResultMsg("删除视频业务网络访问成功!");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("删除视频业务网络访问失败!");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除视频业务网络访问错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 删除视频业务登录方式 接口
     * @param vedioLogonAccess
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除视频业务登陆方式", template = "删除业务名称：%s，启动方式：%s", args = "0.name,0.startPath", type = LogType.DELETE)
    @RequestMapping(value = "/deleteVedioLogonAccess", method = RequestMethod.POST)
    public ResultMsg deleteVedioLogonAccess(VedioLogonAccessDO vedioLogonAccess) {
        ResultMsg resultMsg= new ResultMsg();
        try {
            //删除视频业务登录方式
            boolean flag = vedioLogonAccessService.deleteVedioLogonAccessFromCfgXml(vedioLogonAccess);

            if (flag) {
                resultMsg.setResultMsg("删除视频业务登录方式成功!");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("删除视频业务登录方式失败!");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("删除视频业务登录方式错误!");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
    /**
     * 更新禁止截屏开关
     * @param enable 更新的状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateForbidScreen", method = RequestMethod.POST)
    public ResultMsg updateForbidScreen(String enable){
        ResultMsg resultMsg = new ResultMsg();
        try {
            //更新usbkey双因子认证的开启状态
            clientLegalTimeService.updateForbidScreenState(enable);

            resultMsg.setResultMsg("更新禁止截屏功能开启状态成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("更新禁止截屏功能开启状态错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 获取是否开启禁止截屏的接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isForbidScreen", method = RequestMethod.GET)
    public ResultMsg isForbidScreen() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取usbkey双因子认证的开启状态
            String enable = clientLegalTimeService.getForbidScreen();

            resultMsg.setData(enable);
            resultMsg.setResultMsg("获取禁止截屏开启状态成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取禁止截屏开启状态错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
