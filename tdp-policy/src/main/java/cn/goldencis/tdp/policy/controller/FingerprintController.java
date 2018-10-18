package cn.goldencis.tdp.policy.controller;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.policy.entity.FingerprintDO;
import cn.goldencis.tdp.policy.service.IFingerprintService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/5/22.
 */
@Controller
@RequestMapping(value = "/fingerprint")
public class FingerprintController {

    @Autowired
    private IFingerprintService fingerprintService;

    /**
     * 根据用户guid，查找对应指纹信息的接口
     * @param guid 用户guid
     * @return 返回指纹集合
     */
    @ResponseBody
    @RequestMapping(value = "/getFingerprintListByCilentUserGuid")
    public ResultMsg getFingerprintListByCilentUserGuid(String guid) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //根据用户guid，查找对应指纹信息
            List<FingerprintDO> fingerprintList = fingerprintService.getFingerprintListByCilentUserGuid(guid);

            resultMsg.setData(fingerprintList);
            resultMsg.setResultMsg("查询指纹信息集合成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("查询指纹信息集合错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 指纹比对接口
     * @param argv 指纹密码的json
     * @return json格式 {"result":0} 1:匹配成功 0:匹配失败 -1:出错
     */
    @ResponseBody
    @RequestMapping(value = "/verifyFingerprint")
    public Map<String, Object> verifyFingerprint(String argv) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            JSONObject argvJson = JSONObject.fromObject(argv);
            //进行指纹比对，比对成功，则方法返回指纹对应用户guid。
            String fptemp = argvJson.getString("fptemp");
            if (fingerprintService.verifyFingerprint(fptemp) != null) {
                resultMsg.put("result", 1);
            } else {
                resultMsg.put("result", 0);
            }

        } catch (Exception e) {
            resultMsg.put("result", -1);
        }

        return resultMsg;
    }
}
