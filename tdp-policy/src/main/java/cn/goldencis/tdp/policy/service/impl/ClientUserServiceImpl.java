package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.CClientUserDOMapper;
import cn.goldencis.tdp.core.dao.DepartmentDOMapper;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.core.utils.AuthUtils;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.policy.dao.*;
import cn.goldencis.tdp.policy.entity.*;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.policy.service.IClientUserService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

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

    @Value("${golbalcfg.path}")
    public String golbalcfgpath;

    @Override
    protected BaseDao<ClientUserDO, ClientUserDOCriteria> getDao() {
        return mapper;
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
    public int addClientUser(ClientUserDO clientUser, Integer usbkeyid) {
        //设置用户guid
        UUID uuid = UUID.randomUUID();
        clientUser.setGuid(uuid.toString());

        //设置用户策略，没有则设置默认策略
        if (clientUser.getPolicyid() == null || clientUser.getPolicyid() == 0) {
            clientUser.setPolicyid(ConstantsDto.DEFAULT_POLICY_ID);
        }

        //设置usbKey，没有则未绑定，有则绑定，并且在UsbKey表中更新记录，传入用户id
        if (usbkeyid != null && usbkeyid != -1) {
            UsbKeyDOCriteria example = new UsbKeyDOCriteria();
            example.createCriteria().andIdEqualTo(usbkeyid);
            UsbKeyDO usbKey = new UsbKeyDO();
            usbKey.setUserguid(uuid.toString());
            usbKeyDOMapper.updateByExampleSelective(usbKey, example);
        }

        clientUser.setOnline("0");
        clientUser.setRegtime(new Date());

        return mapper.insertSelective(clientUser);
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
    public boolean ckeckMaxCustomerCount(int maxCustomerCnt) {
        int currentCustomerCnt = this.countAllClientUser();
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
        mapper.updateClientUser(params);
        
    }
}