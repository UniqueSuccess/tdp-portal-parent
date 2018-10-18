package cn.goldencis.tdp.policy.controller;

import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.annotation.PageLog;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.policy.service.IClientUserService;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.service.IPolicyService;
import cn.goldencis.tdp.policy.service.impl.PolicyPublishImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/2.
 */
@Controller
@PageLog(module = "策略")
@RequestMapping(value = "/policy")
public class PolicyController implements ServletContextAware {

    @Autowired
    public IPolicyService policyService;

    @Autowired
    private IClientUserService clientUserService;

    @Autowired
    private PolicyPublishImpl policyPublish;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 策略管理主界面
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("policy/index");
        return modelAndView;
    }

    /**
     * 获取所有策略列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllPolicys", method = RequestMethod.GET)
    public ResultMsg getAllPolicys() {
        ResultMsg resultMsg = new ResultMsg();
        List<PolicyDO> allPolicyList = policyService.getAllPolicyList();
        resultMsg.setData(allPolicyList);
        resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        return resultMsg;
    }

    /**
     * 添加新的策略
     * @param policy 用于接受添加策略的名称
     * @param pid 添加策略继承的父类策略的id
     * @return
     */
    @ResponseBody
    @PageLog(module = "新建策略", template = "策略名称：%s", args = "0.name", type = LogType.INSERT)
    @RequestMapping(value = "/addPolicy", method = RequestMethod.POST)
    public ResultMsg addPolicy(PolicyDO policy, Integer pid) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //判断账户的读写权限
            UserDO loginUser = GetLoginUser.getLoginUser();
            if (loginUser.getReadonly() == 1) {
                resultMsg.setResultMsg("该账户没有写权限！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //检查策略名称是否重复
            boolean flag = policyService.checkPolicyNameDuplicate(policy);
            if (!flag) {
                resultMsg.setResultMsg("策略名称重复！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //查找继承的策略
            PolicyDO parentPolicy = policyService.getByPrimaryKey(pid);

            //添加策略
            policyService.addPolicy(policy, parentPolicy);
            //根据新建的策略id，拼接策略编辑页面跳转地址
            resultMsg.setData("/policy/readPolicyJsonFileById?id=" + policy.getId());
            resultMsg.setResultMsg("新建策略成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("新建策略错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 根据策略id删除策略
     * @return
     */
    @ResponseBody
    @PageLog(module = "删除策略", template = "策略id：%s", type = LogType.DELETE)
    @RequestMapping(value = "/deletePolicy", method = RequestMethod.POST)
    public ResultMsg deletePolicy(Integer policyId) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //判断账户的读写权限
            UserDO loginUser = GetLoginUser.getLoginUser();
            if (loginUser.getReadonly() == 1) {
                resultMsg.setResultMsg("该账户没有写权限！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //根据id删除对应策略，并将使用该策略的用户策略设置为默认（1）
            policyService.deletePolicyById(policyId);

            resultMsg.setResultMsg("删除策略成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("删除策略错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }

    /**
     * 根据策略id，读取策略的json文件，将文件内容转换为JSON对象，放在ResultMsg中返回。
     * 跳转到策略编辑页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/readPolicyJsonFileById", method = RequestMethod.GET)
    public ModelAndView readPolicyJsonFileById(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        ResultMsg resultMsg = new ResultMsg();
        PolicyDO policy = null;
        try {
            //查询需要读取的策略对象
            policy = policyService.getByPrimaryKey(id);
            //读取策略JSON文件，将文件内容转换为JSON对象,以JSONOBJECT的形式返回
            JSONObject jsonObject = policyService.readPolicyJsonFileById(policy);

            resultMsg.setData(jsonObject);
            resultMsg.setResultMsg("读取策略成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("读取策略错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        //跳转到页面编辑界面
        modelAndView.addObject("resultMsg", resultMsg);
        modelAndView.addObject("policyId", id);
        if (policy != null) {
            modelAndView.addObject("policyName", policy.getName());
        }
        modelAndView.setViewName("policy/index");
        return modelAndView;
    }

    /**
     * 更新策略接口
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PageLog(module = "编辑策略", template = "策略名称：%s", args = "0.name", type = LogType.UPDATE)
    @RequestMapping(value = "/updatePolicyJsonFile", method = RequestMethod.POST)
    public ResultMsg updatePolicyJsonFile(@RequestBody JSONObject jsonObject) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //判断账户的读写权限
            UserDO loginUser = GetLoginUser.getLoginUser();
            if (loginUser.getReadonly() == 1) {
                resultMsg.setResultMsg("该账户没有写权限！");
                resultMsg.setResultCode(ConstantsDto.RESULT_CODE_FALSE);
                return resultMsg;
            }

            //更新策略的Json文件内容
            policyService.updatePolicyJsonFile(jsonObject);

            //利用CAS算法，确保给初始化参数赋值的安全性，成功赋值后，会启动线程，子线程完成本地数据初始化后，会将初始化参数恢复默认值。
            //一个请求赋值后，其他多个请求由于CAS算法赋值不成功，仍然在循环中，但不阻塞，等赋值成功的线程完成初始化，则其他线程尝试进行赋值。
            while (true) {
                boolean flag = policyPublish.getInitPolicyId().compareAndSet(0, (Integer)jsonObject.get("policyid"));

                if (flag) {
                    //策略更新完成后，启动线程，通知客户端。
                    taskExecutor.execute(policyPublish);
                    break;
                }
            }

            resultMsg.setData(jsonObject);
            resultMsg.setResultMsg("更新策略成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("更新策略错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
            resultMsg.setData(e);
        }

        return resultMsg;
    }


    /**
     * 批量修改用户策略
     * @param ids 多个用户id的字符串，以"，"隔开。
     * @param policyid
     * @return
     */
    @ResponseBody
    @PageLog(module = "批量修改用户策略", template = "应用的策略id：%s，修改的用户id：%s", args = "0,1", type = LogType.UPDATE)
    @RequestMapping(value = "/batchUpdateClientUsersPolicy", method = RequestMethod.POST)
    public ResultMsg batchUpdateClientUsersPolicy(String ids, Integer policyid) {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //将接口参数参数中的多个用户id的字符串，转化为用户id集合
            String[] idArr = ids.split(",");
            List<Integer> idList = new ArrayList<>();
            for (String id : idArr) {
                idList.add(Integer.parseInt(id));
            }

            //批量更新id包含集合中的用户，将策略更新为新的策略id
            clientUserService.batchUpdateClientUsersPolicy(idList, policyid);
            resultMsg.setResultMsg("用户更新策略成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setResultMsg("用户更新策略失败！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }

    /**
     * 首页统计策略潜在风险接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countPolicyPotentialRisk", method = RequestMethod.GET)
    public ResultMsg countPolicyPotentialRisk() {
        ResultMsg resultMsg = new ResultMsg();

        try {
            //统计策略潜在风险
            Map<Integer, Integer> countMap = policyService.countPolicyPotentialRisk();

            resultMsg.setData(countMap);
            resultMsg.setResultMsg("统计策略潜在风险成功！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_TRUE);
        } catch (Exception e) {
            resultMsg.setData(e);
            resultMsg.setResultMsg("统计策略潜在风险错误！");
            resultMsg.setResultCode(ConstantsDto.RESULT_CODE_ERROR);
        }

        return resultMsg;
    }
}
