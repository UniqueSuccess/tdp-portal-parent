package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.ListUtils;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.CClientUserDOMapper;
import cn.goldencis.tdp.core.dao.DepartmentDOMapper;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.scheduledtask.DynamicScheduledTask;
import cn.goldencis.tdp.core.scheduledtask.ExecutableTask;
import cn.goldencis.tdp.core.service.IOperationLogService;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.policy.dao.*;
import cn.goldencis.tdp.policy.entity.*;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.policy.service.IClientUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

import javax.annotation.PostConstruct;

/**
 * Created by limingchao on 2017/12/22.
 */
@Service
public class ClientUserServiceImpl extends AbstractBaseServiceImpl<ClientUserDO, ClientUserDOCriteria> implements IClientUserService {

    @Autowired
    private ClientUserDOMapper mapper;

    @Autowired
    private CClientUserDOMapper cMapper;

    @Autowired
    private UsbKeyDOMapper usbKeyDOMapper;

    @Autowired
    private CUsbKeyDOMapper cUsbKeyDOMapper;

    @Autowired
    private PolicyDOMapper policyDOMapper;

    @Autowired
    private DepartmentDOMapper departmentDOMapper;

    @Autowired
    private CScrnwatermarkLogDoMapper cScrnwatermarkLogDoMapper;

    @Autowired
    private DynamicScheduledTask dynamicScheduledTask;

    @Autowired
    private PolicyPublishImpl policyPublish;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IOperationLogService logService;

    @Value("${golbalcfg.path}")
    public String golbalcfgpath;

    @Value("${updateClientUserState}")
    private String updateClientUserState;

    @Override
    protected BaseDao<ClientUserDO, ClientUserDOCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    public void init() {
        try {
            ExecutableTask executableTask = new ExecutableTask("终端状态更新", this, this.getClass().getMethod("updateClientUserState", String.class), "cn.goldencis.tdp.policy.service.impl.ClientUserServiceImpl.updateClientUserState");
            dynamicScheduledTask.addExecutableTask(executableTask);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照主键查询用户
     *
     * @param primaryKeyStr
     * @return
     */
    @Override
    public ClientUserDO getByPrimaryKey(String primaryKeyStr) {
        int primaryKey = Integer.parseInt(primaryKeyStr);
        return getDao().selectByPrimaryKey(primaryKey);
    }

    @Override
    public ClientUserDO getClientUserById(Integer id) {
        ClientUserDO clientUser = mapper.selectByPrimaryKey(id);
        return clientUser;
    }

    /**
     * 新建用户
     *
     * @param clientUser
     * @param usbkeyid
     * @return
     */
    @Transactional
    @Override
    public synchronized Map<String, Object> addClientUser(ClientUserDO clientUser) {
        Map<String, Object> result = new HashMap<String, Object>();
        clientUser.setOnline("0");
        Integer policyId = ConstantsDto.DEFAULT_POLICY_ID;
        
        if (clientUser.getGuid() != null) {
            /**
             * 这里对已经注册过 开机登录调用
             */
            //直接更新
            ClientUserDO cu = mapper.queryClientUserByGuId(clientUser.getGuid());
            if (cu == null) {
                result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
                result.put("resultMsg", "用户不存在");
                return result;
            }
            clientUser.setDeptguid(cu.getDeptguid());
            ClientUserDO updateCu = new ClientUserDO();
            updateCu.setIp(clientUser.getIp());
            updateCu.setMac(clientUser.getMac());
            updateCu.setOnlineTime(new Date());
            updateCu.setComputername(clientUser.getComputername());
            updateCu.setOnline("0");
            updateCu.setId(cu.getId());
            mapper.updateByPrimaryKeySelective(updateCu);
            result.put("usrunique", clientUser.getGuid());
            result.put("username", cu.getNickName());
            policyId = cu.getPolicyid();
        } else {
            /**
             * 未注册过
             * 1 校验部门id是否准确
             * 2 检查该终端是否在系统注册过
             *      如果注册过
             *              更新数据库
             *              返回之前策略id
             *      如果没有注册过
             *              根据部门找策略
             *                      如果找不到 使用默认策略
             *                      如果找到则使用查找到的策略
             */

            ClientUserDO cu = mapper.queryClientUserByComputerguid(clientUser.getComputerguid());
            if (cu == null) {
                if (!checkClientUserMax(clientUser)) {
                    result.put("resultCode", ConstantsDto.RESULT_CODE_FALSE);
                    result.put("resultMsg", "超过用户最大点数");
                    return result;
                }
                //根据部门查找对应策略
                if(clientUser.getDeptguid() != null) {
                    Integer departmnetPolicyId = departmentDOMapper.queryPolicyIdByDepartmentId(clientUser.getDeptguid());
                    if (departmnetPolicyId != null) {
                        policyId = departmnetPolicyId;
                    } else {
                        clientUser.setDeptguid(ConstantsDto.DEPARTMENT_UNKOWN_GROUP);
                    }
                } else {
                    clientUser.setDeptguid(ConstantsDto.DEPARTMENT_UNKOWN_GROUP);
                }
                //设置用户guid
                UUID uuid = UUID.randomUUID();
                clientUser.setGuid(uuid.toString());
                clientUser.setPolicyid(policyId);
                mapper.insertSelective(clientUser);
                result.put("usrunique", uuid);
            } else {
                //更新数据
                policyId = cu.getPolicyid();
                clientUser.setPolicyid(null);
                clientUser.setDeptguid(null);
                clientUser.setId(cu.getId());
                clientUser.setOnlineTime(new Date());
                mapper.updateByPrimaryKeySelective(clientUser);
                result.put("usrunique", cu.getGuid());
                result.put("username", cu.getNickName());
                clientUser.setDeptguid(cu.getDeptguid());
            }
            try {
                OperationLogDO log = new OperationLogDO();
                log.setIp(clientUser.getIp());
                String detail = String.format("【%s】终端注册成功", clientUser.getIp());
                log.setLogOperateParam(detail);
                log.setLogPage("终端注册");
                log.setTime(new Date());
                log.setUserName("");
                log.setLogType(LogType.INSERT.value());
                log.setLogDesc(String.format("终端注册成功"));
                logService.create(log);
            } catch (Exception e) {
            }
        }
        if (clientUser.getDeptguid() != null) {
            DepartmentDO department = departmentDOMapper.selectByPrimaryKey(String.valueOf(clientUser.getDeptguid()));
            if (department != null) {
                result.put("deptid", department.getId());
                result.put("deptname", department.getName());
            }
        }
        //根据策略id查询策略地址
        PolicyDO policy = policyDOMapper.selectByPrimaryKey(policyId);
        if (policy != null) {
            result.put("policy", policy.getPath());
            result.put("policytime", policy.getModifyTime().getTime() + "");
        }
        //设置全局配置信息
        result.put("golbalcfgpath", golbalcfgpath);

        String realPath = PathConfig.HOM_PATH + golbalcfgpath;
        File globalCfgFile = new File(realPath);

        if (globalCfgFile.exists()) {
            long globalcfgtime = globalCfgFile.lastModified();
            //为符合Python端的处理，截掉毫秒
            result.put("globalcfgtime", (globalcfgtime + "").substring(0,10));
        }
        if (result.get("username") == null) {
            result.put("username", "");
        }
        return result;
    }

    public boolean checkClientUserMax(ClientUserDO clientUser) {
      //检查是否超过用户购买的最大点数
        Integer maxCustomerCnt = ConstantsDto.VALIDATE_FLAG == 1 ? Integer.parseInt((String) SysContext.getRequest().getServletContext().getAttribute("maxCustomerCnt")) : Integer.MAX_VALUE;
        //排除重新安装的
        boolean flag = ckeckMaxCustomerCount(maxCustomerCnt, clientUser.getComputerguid());
        if (!flag) {
            return false;
        }
        return true;
    }

    /**
     * 通过用户id删除用户
     * 同时将绑定UsbKey设置为未绑定状态
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteClientUserById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过用户名获取用户对象
     *
     * @param clientUserName
     * @return
     */
    @Override
    public ClientUserDO getClientUserByName(String clientUserName) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andUsernameEqualTo(clientUserName);

        List<ClientUserDO> clientUserList = mapper.selectByExample(example);
        if (clientUserList != null && clientUserList.size() > 0) {
            return clientUserList.get(0);
        }
        return null;
    }

    /**
     * 检查用户名是否重复
     *
     * @return 可用返回true
     */
    @Override
    public boolean checkClientUserNameDuplicate(ClientUserDO clientUser) {

        //通过用户名获取数据库中用户记录
        ClientUserDO preClientUser = this.getClientUserByName(clientUser.getUsername());

        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preClientUser != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preClientUser.getId().equals(clientUser.getId())) {
                return true;
            }
            //若果不同，说明为两个用户，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

   

    /**
     * 批量更新策略。批量更新id包含集合中的用户，将策略更新为新的策略id
     *
     * @param idList   需要更新策略的用户id集合
     * @param policyid 需要更新的策略id
     */
    @Override
    @Transactional
    public void batchUpdateClientUsersPolicy(List<Integer> idList, Integer policyid) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andIdIn(idList);
        ClientUserDO clientUser = new ClientUserDO();
        clientUser.setPolicyid(policyid);
        mapper.updateByExampleSelective(clientUser, example);
    }

    /**
     * 为集合中的ClientUserDO对象设置策略名称
     * @param clientUsers ClientUserDO对象集合
     */
    @Override
    public void setClientUserPolicyName(List<ClientUserDO> clientUsers) {
        //遍历用户列表。获取需要查询名称的策略id集合
        List<Integer> idList = new ArrayList<>();
        for (ClientUserDO clientUser : clientUsers) {
            idList.add(clientUser.getPolicyid());
        }
        //查询策略
        PolicyDOCriteria example = new PolicyDOCriteria();
        example.createCriteria().andIdIn(idList);
        List<PolicyDO> policyList = policyDOMapper.selectByExample(example);
        //两此循环，为用户的策略名称属性赋值
        for (ClientUserDO clientUser : clientUsers) {
            for (PolicyDO policy : policyList) {
                if (clientUser.getPolicyid() == policy.getId()) {
                    clientUser.setPolicyname(policy.getName());
                    break;
                }
            }
        }
    }

    /**
     * 提取用户信息。返回JSON格式的数据信息
     *
     * @param clientUser
     * @return
     */
    @Override
    public void loadClientUserInfo(ClientUserDO clientUser, Map<String, Object> resultMsg) {

        //设置用户基本信息
        resultMsg.put("userguid", clientUser.getGuid());
        resultMsg.put("username", clientUser.getUsername());
        resultMsg.put("truename", clientUser.getTruename());


        //查询并设置部门名称
        DepartmentDO departmentDO = departmentDOMapper.selectByPrimaryKey(clientUser.getDeptguid().toString());
        if (departmentDO != null) {
            resultMsg.put("departname", departmentDO.getName());
            resultMsg.put("departid", departmentDO.getId());
        }
        //设置策略信息
        PolicyDO policy = policyDOMapper.selectByPrimaryKey(clientUser.getPolicyid());
        resultMsg.put("policyid", policy.getId());
        //为符合Python端的处理，截掉毫秒
        resultMsg.put("policytime", (policy.getModifyTime().getTime() + "").substring(0,10));
        resultMsg.put("policypath", policy.getPath());

        //设置用户对应的隐式屏幕水印id
        String scrnwatermarkId = cScrnwatermarkLogDoMapper.getScrnwatermarkIdByApplicantId(clientUser.getGuid());
        if (scrnwatermarkId != null) {
            resultMsg.put("scrnwatermark_id", scrnwatermarkId);
        }

        //设置ip地址
        String ip = NetworkUtil.getIpAddress(SysContext.getRequest());
        resultMsg.put("ip", ip);

        //设置全局配置信息
        resultMsg.put("golbalcfgpath", golbalcfgpath);

        String realPath = SysContext.getRequest().getSession().getServletContext().getRealPath(golbalcfgpath);
        File globalCfgFile = new File(realPath);

        if (globalCfgFile.exists()) {
            long globalcfgtime = globalCfgFile.lastModified();
            //为符合Python端的处理，截掉毫秒
            resultMsg.put("globalcfgtime", (globalcfgtime + "").substring(0,10));
        }

        //更新用户登录时间和登录状态，和ip、computername、mac
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andGuidEqualTo(clientUser.getGuid());

        ClientUserDO logonStatus = new ClientUserDO();
        logonStatus.setOnline("1");
        logonStatus.setOnlineTime(new Date());
        logonStatus.setIp(ip);
        if (!StringUtil.isEmpty(clientUser.getComputername())) {
            logonStatus.setComputername(clientUser.getComputername());
        }
        if (!StringUtil.isEmpty(clientUser.getComputerguid())) {
            logonStatus.setComputerguid(clientUser.getComputerguid());
        }
        if (!StringUtil.isEmpty(clientUser.getMac())) {
            logonStatus.setMac(clientUser.getMac());
        } else {
            logonStatus.setMac("");
        }

        mapper.updateByExampleSelective(logonStatus, example);
    }

    /**
     * 根据传入的GUID，查询数据库中的用户信息
     * @param userguid
     * @return
     */
    @Override
    public ClientUserDO getClientUserByGuid(String userguid) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andGuidEqualTo(userguid);
        List<ClientUserDO> clientUserList = mapper.selectByExample(example);

        if (clientUserList != null && clientUserList.size() > 0) {
            return clientUserList.get(0);
        }
        return null;
    }

    /**
     * 客户端登出
     * @param userguid
     */
    @Override
    public void clientUserLogout(String userguid) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andGuidEqualTo(userguid);

        ClientUserDO logonStatus = new ClientUserDO();
        logonStatus.setOnline("0");
        logonStatus.setOfflineTime(new Date());

        mapper.updateByExampleSelective(logonStatus, example);
    }

    /**
     * 修改密码
     * @param clientUser
     */
    @Override
    public void updateClientUserPassword(ClientUserDO clientUser) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andGuidEqualTo(clientUser.getGuid());

        ClientUserDO updateClientUser = new ClientUserDO();
        updateClientUser.setPassword(clientUser.getPassword());

        mapper.updateByExampleSelective(updateClientUser, example);
    }

    /**
     * 获取全部用户信息列表
     * @return
     */
    @Override
    public List<ClientUserDO> getAllClientUser() {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        return mapper.selectByExample(example);
    }

    /**
     * 获取全部用户总数
     * @return
     */
    @Override
    public int countAllClientUser() {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        long count = mapper.countByExample(example);
        return (int) count;
    }

    /**
     * 获取全部在线用户总数
     * @return
     */
    @Override
    public int countAllOnlineClientUser() {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andOnlineEqualTo("1");
        long count = mapper.countByExample(example);
        return (int) count;
    }

    /**
     * 检查是否超过用户购买的最大点数
     * @param maxCustomerCnt
     * @return
     */
    @Override
    public boolean ckeckMaxCustomerCount(int maxCustomerCnt, String computerguid) {
        
        int currentCustomerCnt = mapper.queryCurrentCustomerCntExclude(computerguid);
        if (currentCustomerCnt < maxCustomerCnt) {
            return true;
        }
        return false;
    }

    /**
     * 校验上传的Excel文件的表格标题项是否正确，并将标题从列表中移除。
     * @param list
     * @return
     */
    @Override
    public boolean checkClientUserTempletTitle(List<List<String>> list) {
        List<String> titleRow = list.get(0);
        //匹配模板格式
        if (titleRow.size() > 2 && "用户名".equals(titleRow.get(0)) && "真实姓名".equals(titleRow.get(1)) && "所属部门".equals(titleRow.get(2))) {
            //移除表格title
            list.remove(0);
            return true;
        }
        return false;
    }

    /**
     * 校验点数是否超过了，不进行插入，直接提示。
     *
     * @param list
     * @param maxCustomerCnt
     * @return
     */
    @Override
    public boolean checkClientUserMaxCount(List<List<String>> list, Integer maxCustomerCnt) {
        //从列表最后开始遍历，剔除无用的空行数，检查存在数据的行数，从后往前，第一个不为空的行开始
        int insertCount;
        for (insertCount = list.size() - 1; insertCount >= 0; insertCount--) {
            List<String> clientUserfieldList = list.get(insertCount);
            //从后往前，前三个不为空的行保留
            if (clientUserfieldList.size() > 2 ) {
                if (!StringUtil.isEmpty(clientUserfieldList.get(0)) || !StringUtil.isEmpty(clientUserfieldList.get(1)) || !StringUtil.isEmpty(clientUserfieldList.get(2))){
                    continue;
                }
            }
            //剔除无用行数
            list.remove(insertCount);
        }

        //获取全部导入用户信息的名称
        List<String> clientUserNameList = new ArrayList<>();
        for (List<String> clientUserfieldList : list) {
            if (clientUserfieldList.size() > 0) {
                clientUserNameList.add(clientUserfieldList.get(0));
            }
        }

        //获取当前数据库中已有的用户名称和准备导入的Excel中重名的用户名数量，交集
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andUsernameIn(clientUserNameList);
        int existsCount = (int) mapper.countByExample(example);

        //清理完成后，此时集合长度等于有效行数
        insertCount = list.size() ;

        //目前数据库中的用户数量
        int currentCount = this.countAllClientUser();

        //校验是否超过最大点数，未超过，
        if (currentCount + insertCount - existsCount < maxCustomerCnt) {
            return true;
        }
        return false;
    }

    /**
     * 校验Excel中的用户名是否重复，如果存在两个同名的用户名称，不进行插入，直接提示。
     * @return
     * @param list
     */
    @Override
    public boolean checkClientUserNameDuplicateInListFromExcel(List<List<String>> list) {
        //将所有list中的名称（索引为0）取出放进Set中，利用Set去重。
        Set set = new HashSet();
        for (List<String> clientUserfieldList : list) {
            if (clientUserfieldList.size() > 0) {
                set.add(clientUserfieldList.get(0));
            }
        }

        //通过判断Set和List的大小是否相等，判断是否存在重名的
        if (set.size() == list.size()) {
            return true;
        }
        return false;
    }

    @Override
    public List<ClientUserDO> getClientUserListByIdList(List<Integer> idList) {
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andIdIn(idList);
        List<ClientUserDO> clientUserList = mapper.selectByExample(example);
        return clientUserList;
    }

    @Override
    public void addScrnwatermarkLog(ClientUserDO clientUser) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", null);

        //获取服务器设备标识
        Map<String, Object> authInfo = AuthUtils.getAuthInfo(SysContext.getRequest().getServletContext());
        String authId = (String) authInfo.get("deviceUnique");
        paramMap.put("authId", authId);

        String userGuid = clientUser.getGuid();
        paramMap.put("userGuid", userGuid);
        String truename = clientUser.getTruename();
        paramMap.put("truename", truename);

        //设置日志中的水印类型
        String applyInfo = "{\"type\":\"用户屏幕水印\"}";
        paramMap.put("applyInfo", applyInfo);

        //设置水印生成时间
        Date appliTime = new Date();
        paramMap.put("appliTime", appliTime);

        //生成水印记录并回传记录唯一id
        cScrnwatermarkLogDoMapper.insertByParamMap(paramMap);

        //生成水印id
        long id = (long) paramMap.get("id");
        String scrnwatermarkId = StringUtil.generateScrnwatermarkId((int) id);

        //更新水印id
        cScrnwatermarkLogDoMapper.updateScrnwatermarkId((int) id, scrnwatermarkId);

    }

    @Override
    public boolean batchInsertClientUsersByListReadInExcel(List<List<String>> list, List<DepartmentDO> allDepartment,
            ResultMsg resultMsg) {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * 终端查询接口
     */
    @Override
    public int conutClientUserListByDepartmentId(Map<String, Object> params) {
        
        return mapper.conutClientUserListByDepartmentId(params);
    }
    /**
     * 终端查询接口
     */
    @Override
    public List<ClientUserDO> getClientUserListByDepartmentId(Map<String, Object> params) {
        
        return mapper.getClientUserListByDepartmentId(params);
    }

    @Override
    public void updateClientUser(Map<String, Object> params) {
        List<String> list = queryChangeClient(params);
        mapper.updateClientUser(params);
        /**
         * 查询哪些用户策略有变化 下发策略
         */
        if (!ListUtils.isEmpty(list)) {
            /*while (true) {
                boolean flag = policyPublish.getInitPolicyId().compareAndSet(0, Integer.valueOf(String.valueOf(params.get("strategy"))))
                        && policyPublish.getInitClientUserList().compareAndSet(null, ListUtils.covertStringByList(list, null));
                if (flag) {
                    //策略更新完成后，启动线程，通知客户端。
                    taskExecutor.execute(policyPublish);
                    break;
                }
            }*/
            policyPublish.send(Integer.valueOf(String.valueOf(params.get("strategy"))), null, ListUtils.covertStringByList(list, null));
        }
    }

    private List<String> queryChangeClient(Map<String, Object> params) {
        return mapper.queryChangeClient(params);
    }

    @Override
    public void updateHeartbeat(String usrunique) {
        mapper.updateHeartbeat(usrunique, DateUtil.getCurrentDate(DateUtil.DateTimeFormat));
    }

    @Transactional
    @Override
    public void updateClientUserState(String taskguid) {
        /**
         * 心跳由python实现
         */
        //String date = DateUtil.getFormatDate(DateUtil.getCurrentDateAddMinute(Integer.valueOf(updateClientUserState)), DateUtil.DateTimeFormat);
        //mapper.updateClientUserOnline(date);
        //mapper.updateClientUserOffline(date);
    }

    @Override
    @Cacheable(value = "heartInfo", key = "#usrunique")
    public Map<String, Object> queryDepartmentByUnique(String usrunique) {
        return mapper.queryDepartmentByUnique(usrunique);
    }

    @Override
    public void modifyClientUserStatus(Map<String, Object> params) {
        mapper.modifyClientUserStatus(params);
    }
}