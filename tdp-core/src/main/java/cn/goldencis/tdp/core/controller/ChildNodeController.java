package cn.goldencis.tdp.core.controller;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ChildNodeDOCriteria;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.service.IChildNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/7/19.
 */
@Controller
@RequestMapping(value = "/childNode")
public class ChildNodeController {

    @Autowired
    private IChildNodeService childNodeService;

    /**
     * 服务器字节点的新建接口
     * @param childNodeDO 子节点对象
     */
    @ResponseBody
    @RequestMapping(value = "/childNode", method = RequestMethod.POST)
    public ResultMsg save(ChildNodeDO childNodeDO) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            childNodeDO.setCreateTime(new Date());
            childNodeService.createSelective(childNodeDO);

            resultMsg.setResultMsg("子级服务器节点创建成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("子级服务器节点创建错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 服务器字节点的列表接口
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultMsg list() {
        ResultMsg resultMsg = new ResultMsg();

        try {

            List<ChildNodeDO> childNodeList = childNodeService.listBy(new ChildNodeDOCriteria());

            resultMsg.setData(childNodeList);
            resultMsg.setResultMsg("子级服务器节点创建成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("子级服务器节点创建错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 通过id获取服务器子节点的接口
     * @param id 服务器子节点的id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/childNode/{id}", method = RequestMethod.GET)
    public ResultMsg get(@PathVariable(value = "id") Integer id) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            ChildNodeDO childNode = childNodeService.getByPrimaryKey(id);

            resultMsg.setData(childNode);
            resultMsg.setResultMsg("服务器子节点获取成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("服务器子节点获取错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 通过id删除服务器子节点的接口
     * @param id 服务器子节点的id
     */
    @ResponseBody
    @RequestMapping(value = "/childNode/{id}", method = RequestMethod.DELETE)
    public ResultMsg delete(@PathVariable(value = "id") Integer id) {
        ResultMsg resultMsg = new ResultMsg();

        try {

            childNodeService.deleteByPrimaryKey(id);

            resultMsg.setResultMsg("服务器子节点删除成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("服务器子节点删除错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 服务器子节点的编辑接口
     * @param childNodeDO 服务器子节点对象
     */
    @ResponseBody
    @RequestMapping(value = "/childNode", method = RequestMethod.PUT)
    public ResultMsg edit(ChildNodeDO childNodeDO) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            childNodeService.update(childNodeDO);

            resultMsg.setResultMsg("服务器子节点更新成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("服务器子节点更新错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    @ModelAttribute
    public void getModel(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            ChildNodeDO childNodeDO = childNodeService.getByPrimaryKey(id);
            map.put("childNodeDO", childNodeDO);
        }
    }
}
