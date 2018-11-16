package cn.goldencis.tdp.policy.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.entity.ClientUserDOCriteria;
import cn.goldencis.tdp.core.entity.DepartmentDO;

import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2017/12/22.
 */
public interface IClientUserService extends BaseService<ClientUserDO, ClientUserDOCriteria> {

    /**
     * 根据用户id查找用户
     * @param id
     * @return
     */
    ClientUserDO getClientUserById(Integer id);

    /**
     * 新建用户
     * @param clientUser
     * @param usbkeyid
     * @return
     */
    Map<String, Object> addClientUser(ClientUserDO clientUser);

    /**
     * 通过用户id删除用户
     * @param id
     */
    void deleteClientUserById(Integer id);

    /**
     * 通过用户名查找用户
     * @param clientUserName
     * @return
     */
    ClientUserDO getClientUserByName(String clientUserName);

    /**
     * 检查用户名是否重复
     * @return
     */
    boolean checkClientUserNameDuplicate(ClientUserDO clientUser);

    /**
     * 批量更新策略。批量更新id包含集合中的用户，将策略更新为新的策略id
     * @param idList 需要更新策略的用户id集合
     * @param policyid 需要更新的策略id
     */
    void batchUpdateClientUsersPolicy(List<Integer> idList, Integer policyid);

    /**
     * 为集合中的ClientUserDO对象设置策略名称
     * @param clientUsers
     */
    void setClientUserPolicyName(List<ClientUserDO> clientUsers);

    /**
     * 提取用户信息。返回JSON格式的数据信息
     * @param clientUser
     * @return
     */
    void loadClientUserInfo(ClientUserDO clientUser, Map<String, Object> resultMsg);

    /**
     * 根据传入的GUID，查询数据库中的用户信息
     * @param userguid
     * @return
     */
    ClientUserDO getClientUserByGuid(String userguid);

    /**
     * 客户端登出
     * @param userguid
     */
    void clientUserLogout(String userguid);

    /**
     * 修改密码
     * @param clientUser
     */
    void updateClientUserPassword(ClientUserDO clientUser);

    /**
     * 获取全部用户信息列表
     * @return
     */
    List<ClientUserDO> getAllClientUser();

    /**
     * 获取全部用户总数
     * @return
     */
    int countAllClientUser();

    /**
     * 获取全部在线用户总数
     * @return
     */
    int countAllOnlineClientUser();

    /**
     * 检查是否超过用户购买的最大点数
     * @param maxCustomerCnt
     * @return
     */
    boolean ckeckMaxCustomerCount(int maxCustomerCnt, String computerguid);

    /**
     * 校验上传的Excel文件的表格标题项是否正确，并将标题从列表中移除。
     * @param list
     * @return
     */
    boolean checkClientUserTempletTitle(List<List<String>> list);

    /**
     * 将列表中的用户信息，批量插入
     * @param list
     * @param allDepartment
     * @param resultMsg
     */
    boolean batchInsertClientUsersByListReadInExcel(List<List<String>> list, List<DepartmentDO> allDepartment, ResultMsg resultMsg);

    /**
     * 校验点数是否超过了，不进行插入，直接提示。
     *
     * @param list
     * @param maxCustomerCnt
     * @return
     */
    boolean checkClientUserMaxCount(List<List<String>> list, Integer maxCustomerCnt);

    /**
     * 校验Excel中的用户名是否重复，如果存在两个同名的用户名称，不进行插入，直接提示。
     * @return
     * @param list
     */
    boolean checkClientUserNameDuplicateInListFromExcel(List<List<String>> list);

    /**
     * 根据用户id的集合查询用户对象集合
     * @param idList 用户id的集合
     * @return 用户对象集合
     */
    List<ClientUserDO> getClientUserListByIdList(List<Integer> idList);

    /**
     * 为用户生成的屏幕隐式水印id和添加对应的水印日志
     * @param clientUser 用户对象
     */
    void addScrnwatermarkLog(ClientUserDO clientUser);

    /**
     * 终端查询接口
     * @param params
     * @return
     */
    int conutClientUserListByDepartmentId(Map<String, Object> params);
    /**
     * 终端查询接口
     * @param params
     * @return
     */
    List<ClientUserDO> getClientUserListByDepartmentId(Map<String, Object> params);
    /**
     * 更新终端部门和策略
     * @param params
     */
    void updateClientUser(Map<String, Object> params);
    /**
     * 心跳上报接口
     * @param computerguid
     */
    void updateHeartbeat(String usrunique);
    
    void updateClientUserState(String taskguid);

    /**
     * 根据用户唯一值查询 用户信息
     * @param usrunique
     * @return
     */
    Map<String, Object> queryDepartmentByUnique(String usrunique);

    void modifyClientUserStatus(Map<String, Object> params);
}
