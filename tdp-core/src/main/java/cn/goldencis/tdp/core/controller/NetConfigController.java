package cn.goldencis.tdp.core.controller;

import cn.goldencis.tdp.core.entity.ResultMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.neiwang.vdpjni.IFConfig;

@Controller
@RequestMapping("/netconfig")
public class NetConfigController {

    @ResponseBody
    @RequestMapping(value = "/configTest")
    public ResultMsg configTest() {
        ResultMsg resultMsg = new ResultMsg();
        try {
            String ifConfig = IFConfig.getIFConfig();
            resultMsg.setData(ifConfig);
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.setData(e);
        }
        return resultMsg;
    }
}
