package cn.goldencis.tdp.core.controller;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.goldencis.tdp.common.utils.CommonPoiXsl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileDownLoad;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.*;
import cn.goldencis.tdp.core.utils.PathConfig;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.service.IDepartmentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 部门管理controller
 *
 * @author Administrator
 */
@Controller
@PageLog(module = "部门管理")
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    private static final String FILE_NAME = "部门信息";

    /**
     * 部门管理主页
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        JSONArray zNodes = departmentService.getNodesByLogin();
        model.addObject("zNodes", zNodes);
        model.setViewName("user/department/index");
        return model;
    }

    /*
     * 根据登录用户权限获取部门树json
     * @param ischeck 是否有未分组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDepartmentNodesByLoginUser")
    public JSONArray getDepartmentNodesByLoginUser() {
        JSONArray zNodes = departmentService.getNodesByLogin();
        return zNodes;
    }

    /**
     * 下级部门列表，分页查询
     *
     * @param pid
     * @param draw
     * @param length
     * @param start
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/datalist", produces = "application/json", method = RequestMethod.GET)
    public ResultMsg datalist(@RequestParam("pid") Integer pid, @RequestParam(value = "draw", required = false) String draw, @RequestParam("length") Integer length, @RequestParam("start") Integer start, String searchstr, HttpServletRequest request, HttpServletResponse response) {
        ResultMsg result = new ResultMsg();

        try {
          //设置查询条件
            String orderCase = null;

            //通过id查询父类部门
            DepartmentDO parentDept = departmentService.getByPrimaryKey(String.valueOf(pid));
            //获取父类部门树
            String treePath = parentDept.getTreePath() + parentDept.getId() + ",%";
            //查询子类部门数量
            long count = departmentService.getDeptarMentCountByParent(pid, treePath);
            //查询子类部门分页列表
            List<DepartmentDO> departmentList = departmentService.getDeptarMentListByParent(start, length, pid, treePath, orderCase);

            //为列表中的部门添加父类部门名称
            departmentService.setParentDepartmentNames(parentDept, departmentList);
            result.setRows(departmentList);
            result.setTotal((int) count);
            result.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResultMsg("查询部门异常");
        }
        return result;
    }

    /**
     * 获取全部部门树，如果有账户id，查询账户对应的部门权限，加上check:true
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDepartmentTreeByUserId", method = RequestMethod.GET)
    public ResultMsg getDepartmentTreeByUserId(Integer userId) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            JSONArray departmentArr = departmentService.getDepartmentTreeByUserId(userId);

            resultMsg.setData(departmentArr);
            resultMsg.setResultMsg("获取部门权限成功");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("获取部门权限失败");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }
        return resultMsg;
    }

    /**
     * 保存接口
     *
     * @param bean
     * @return
     */
    @ResponseBody
    @PageLog(module = "新建部门", template = "部门名称：%s", args = "0.name", type = LogType.INSERT)
    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public ResultMsg add(@ModelAttribute DepartmentDO bean) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            //检查新添加的部门名称是否重复
            List<DepartmentDO> list = departmentService.selectDepartmentByName(bean.getName());
            if (list.size() > 0) {
                resultMsg.setResultMsg("部门名称重复");
            } else {
                //查询父部门，如果没有父部门，则默认将父部门树设置为顶级部门树
                DepartmentDO pdept = departmentService.getByPrimaryKey(String.valueOf(bean.getParentId()));
                if (StringUtil.isEmpty(pdept.getTreePath())) {
                    pdept.setTreePath(",");
                }
                bean.setTreePath(pdept.getTreePath() + pdept.getId() + ",");

                departmentService.addDepartment(bean);
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            }
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setResultMsg("部门添加失败");
        }
        return resultMsg;
    }

    /**
     * 修改接口
     *
     * @param bean
     * @return
     */
    @ResponseBody
    @PageLog(module = "编辑部门", template = "部门名称：%s", args = "0.name", type = LogType.UPDATE)
    @RequestMapping(value = "/updateDept", produces = "application/json", method = RequestMethod.POST)
    public ResultMsg updateDept(@ModelAttribute DepartmentDO bean) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            //通过修改后传入的名称，查询相同名称的部门列表
            List<DepartmentDO> list = departmentService.selectDepartmentByName(bean.getName());
            //通过id，获取原部门
            DepartmentDO dep = departmentService.getDepartmentById(bean.getId());

            //存在该名称的部门，继续判断。
            if (list.size() > 0) {
                //传入部门和数据库中部门的名称一致，名称没有修改的情况，执行更新
                if (dep.getName().equals(bean.getName())) {
                    departmentService.updatedept(bean);
                    if (!dep.getPolicyId().equals(bean.getPolicyId())) {
                        departmentService.changePolicy(dep.getId(), bean.getPolicyId(), dep.getPolicyId());
                    }
                    resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
                } else {
                    //跟数据库中其他id部门的名称一致。重复。
                    resultMsg.setResultMsg("部门名称重复");
                }
            } else {
                //不存在该名称，不存在名称重复问题，直接修改。
                departmentService.updatedept(bean);
                if (!dep.getPolicyId().equals(bean.getPolicyId())) {
                    departmentService.changePolicy(dep.getId(), bean.getPolicyId(), dep.getPolicyId());
                }
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setResultMsg("部门添加失败");
        }
        return resultMsg;
    }

    /**
     * 删除接口
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除部门", template = "部门id：%s", args = "0.id", type = LogType.DELETE)
    @RequestMapping(value = "/delete", produces = "application/json", method = RequestMethod.POST)
    public ResultMsg delete(@RequestParam(value = "id") Integer id) {
        ResultMsg result = new ResultMsg();
        try {
            if (id == 2 || id == 1) {
                if (id == 1) {
                    result.setResultMsg("顶级部门不能删除");
                } else {
                    result.setResultMsg("未分组不能删除");
                }
            } else {
                DepartmentDOCriteria example = new DepartmentDOCriteria();
                example.createCriteria().andParentIdEqualTo(id);
                List<DepartmentDO> clist = departmentService.listBy(example);
                if (!CollectionUtils.isEmpty(clist)) {
                    result.setResultMsg("该部门有子部门不能删除");
                } else {
                    departmentService.deleteById(id);
                    result.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
                }
            }
        } catch (Exception ex) {
            result.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }
        return result;
    }
    /**
     * 部门信息导出接口
     * @param request
     * @param response
     */
    @ResponseBody
    @PageLog(module = "导出全部部门信息", template = "", type = LogType.OTHER)
    @RequestMapping(value = "/exportxsl", method = RequestMethod.GET)
    public void exportXslAll(HttpServletRequest request, HttpServletResponse response) {

        //获取全部部门
        List<DepartmentDO> departmentList = departmentService.getAllDepartment();
        //为列表中的部门添加父类部门名称
        departmentService.setParentDepartmentNames(null, departmentList);
        CommonPoiXsl<DepartmentDO> poiXsl = new CommonPoiXsl<>();
        String path = "";
        try {
            String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            path = request.getServletContext().getRealPath(PathConfig.EXPORT_PATH);
            if (!(new File(path).isDirectory())) {
                new File(path).mkdirs();
            }
            path = path + "/" + FILE_NAME + key + ".xls";
            OutputStream out = new FileOutputStream(path);
            String[] headers = {"部门名称", "上级部门", "部门负责人", "电话"};
            poiXsl.exportExcel("账户信息", headers, departmentList, out, "yyyy-MM-dd");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FileDownLoad filedownload = new FileDownLoad();
        filedownload.download(response, request, path, null);
    }
}
