package cn.goldencis.tdp.policy.controller;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.policy.service.IClientUserService;
import cn.goldencis.tdp.policy.entity.UsbKeyDO;
import cn.goldencis.tdp.policy.service.IUsbKeyService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/4.
 */
@Controller
@RequestMapping(value = "/usbKey")
public class UsbKeyController {

    @Autowired
    public IUsbKeyService usbKeyService;

    @Autowired
    public IClientUserService clientUserService;

    /**
     * UsbKey管理主界面
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("system/usbkey/index");
        return modelAndView;
    }

    /**
     * 按条件查询UsbKey接口，分页查询
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUsbKeyInPage")
    public ResultMsg getUsbKeyInPage(Integer start, Integer length, String order) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //查询UsbKey的总数量
            int count = usbKeyService.countAllUsbKey(order);
            //获取UsbKey列表，分页查询
            List<UsbKeyDO> usbKeyList = usbKeyService.getUsbKeyInPage(start, length, order);

            resultMsg.setExportlength(length);
            resultMsg.setExportstart(start);
            resultMsg.setRecordsFiltered(count);
            resultMsg.setRecordsTotal(count);
            resultMsg.setData(usbKeyList);
            resultMsg.setResultMsg("获取UKey列表成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("获取UKey列表错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * UsbKey的新建接口
     * @param usbKey 新建的UsbKey
     */
    @ResponseBody
    @RequestMapping(value = "/addUsbKey")
    public ResultMsg addUsbKey(UsbKeyDO usbKey) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //判断UsbKey名称是否重复
            boolean flag = usbKeyService.checkUsbKeyNameDuplicate(usbKey);
            if (!flag) {
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.setResultMsg("UKey名称重复！");
                return resultMsg;
            }

            //判断UsbKey是否重复录入
            flag = usbKeyService.checkUsbKeyNumDuplicate(usbKey);
            if (!flag) {
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.setResultMsg("该UKey已经注册！");
                return resultMsg;
            }

            //新增UsbKey对象
            usbKeyService.addUsbKey(usbKey);

            resultMsg.setResultMsg("新建UKey成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("新建UKey错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * UsbKey的编辑接口
     * @param usbKey 编辑的UsbKey
     */
    @ResponseBody
    @RequestMapping(value = "/editUsbKey")
    public ResultMsg editUsbKey(UsbKeyDO usbKey) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            //判断UsbKey名称是否重复
            boolean flag = usbKeyService.checkUsbKeyNameDuplicate(usbKey);
            if (!flag) {
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.setResultMsg("UKey名称重复！");
                return resultMsg;
            }

            //判断UsbKey是否重复录入
            flag = usbKeyService.checkUsbKeyNumDuplicate(usbKey);
            if (!flag) {
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.setResultMsg("该UKey已经注册！");
                return resultMsg;
            }


            //编辑UsbKey对象
            usbKeyService.editUsbKey(usbKey);

            resultMsg.setResultMsg("编辑UKey成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("编辑UKey错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 删除UsbKey的接口
     * @param ids 要删除的id的数组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteUsbKey")
    public ResultMsg deleteUsbKey(String ids) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            if (!StringUtils.isEmpty(ids)) {
                String[] idArr = ids.split(",");
                List<Integer> idList = new ArrayList<>();
                for (String id : idArr) {
                    idList.add(Integer.parseInt(id));
                }
                //判断UsbKey是否绑定用户
                boolean flag = usbKeyService.checkUsbKeyIsBinded(idList);
                if (flag) {
                    resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                    resultMsg.setResultMsg("请先解除UKey用户绑定！");
                    return resultMsg;
                }

                //删除UsbKey对象
                usbKeyService.deleteUsbKey(idList);
                resultMsg.setResultMsg("删除UKey成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("删除UKey失败！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setResultMsg("删除UKey错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 获取全部未绑定的UsbKey列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllUnbindUsbKey")
    public ResultMsg getAllUnbindUsbKey() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取全部未绑定的UsbKey列表
            List<UsbKeyDO> usbKeyList = usbKeyService.getAllUnbindUsbKey();
            resultMsg.setData(usbKeyList);
            resultMsg.setResultMsg("获取未绑定UKey列表成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("获取未绑定UKey列表错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 根据用户id解绑UsbKey
     * @param clientUserIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unbindUsbKeyByClientUserId",method = RequestMethod.POST)
    public ResultMsg unbindUsbKeyByClientUserId(String clientUserIds){
        ResultMsg resultMsg = new ResultMsg();
        try {
            if (!StringUtils.isEmpty(clientUserIds)) {
                //将UsbKeyId的数组字符串转化为待删除的id集合
                String[] idArr = clientUserIds.split(",");
                List<Integer> idList = new ArrayList<>();
                for (String id : idArr) {
                    idList.add(Integer.parseInt(id));
                }

                //查询需要解绑的用户集合
                List<ClientUserDO> clientUserList = clientUserService.getClientUserListByIdList(idList);
                //解除绑定
                usbKeyService.unbindUsbKeyByClientUser(clientUserList, idList);
                resultMsg.setResultMsg("解除绑定成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("解除绑定失败！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setResultMsg("解除绑定错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }
        return resultMsg;
    }

    /**
     * 根据UsbKeyId解绑UsbKey
     * @param ids UsbKeyId的数组字符串
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unbindUsbKeyByUsbKeyId",method = RequestMethod.POST)
    public ResultMsg unbindUsbKeyByUsbKeyId(String ids){
        ResultMsg resultMsg = new ResultMsg();
        try {
            if (!StringUtils.isEmpty(ids)) {
                //将UsbKeyId的数组字符串转化为待删除的id集合
                String[] idArr = ids.split(",");
                List<Integer> idList = new ArrayList<>();
                for (String id : idArr) {
                    idList.add(Integer.parseInt(id));
                }

                //通过usbkey的id集合解除绑定
                usbKeyService.unbindUsbKeyByUsbKeyIdList(idList);
                resultMsg.setResultMsg("解除绑定成功！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            } else {
                resultMsg.setResultMsg("解除绑定失败！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
            }
        } catch (Exception e) {
            resultMsg.setResultMsg("解除绑定错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }
        return resultMsg;
    }

    /**
     * 校验UsbKey是否合法的接口
     * @param params Json参数,UsbKey的唯一id
     * @return 是否合法，usbkey的名称，是否需要密码
     */
    @ResponseBody
    @RequestMapping(value = "/checkUsbkeyAvailableByNum", method = RequestMethod.POST)
    public Map<String, Object> checkUsbkeyAvailableByNum(@RequestBody JSONObject params) {
        Map<String, Object> resultMsg = new HashMap<>();

        try {
            String unqiueId = params.getString("unqiueId");
            UsbKeyDO usbKey = usbKeyService.getUsbKeyByNum(unqiueId);
            if (usbKey != null) {

                resultMsg.put("name", usbKey.getName());
                resultMsg.put("pwdNeeded", usbKeyService.getPwdNeeded());
                resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_TRUE);
                resultMsg.put("resultmsg", "验证UKey成功！");
            } else {
                resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_FALSE);
                resultMsg.put("resultmsg", "验证UKey失败！");
            }
        } catch (Exception e) {
            resultMsg.put("error", e);
            resultMsg.put("resultcode", ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.put("resultmsg", "验证UKey错误！");
        }

        return resultMsg;
    }

    /**
     * 获取是否开启usbkey双因子认证的接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isUsbkeyPwdNeeded", method = RequestMethod.GET)
    public ResultMsg isUsbkeyPwdNeeded() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //获取usbkey双因子认证的开启状态
            Boolean pwdNeeded = usbKeyService.getPwdNeeded();

            resultMsg.setData(pwdNeeded);
            resultMsg.setResultMsg("获取UKey双因子认证开启状态成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取UKey双因子认证开启状态错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 更新usbkey双因子认证的开启状态的接口
     * @param isNeeded 更新的状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateUsbkeyPwdState", method = RequestMethod.POST)
    public ResultMsg updateUsbkeyPwdState(boolean isNeeded) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //更新usbkey双因子认证的开启状态
            usbKeyService.updateUsbkeyPwdState(isNeeded);

            resultMsg.setResultMsg("更新UKey双因子认证的开启状态成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("更新UKey双因子认证的开启状态错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
