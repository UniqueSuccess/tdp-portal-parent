package cn.goldencis.tdp.core.service;

import java.util.List;
import java.util.Map;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.entity.UserDOCriteria;
import net.sf.json.JSONArray;

/**
 * 管理员service
 *
 * @author Administrator
 */
public interface IUserService extends BaseService<UserDO, UserDOCriteria> {

    List<UserDO> getUserList(UserDO user);

    List<UserDO> getAllUser();

    UserDO getLoginUser(String username);

    UserDO getLoginUserNocache(String username);

    UserDO getLoginUserNoCache(String username);

    Long countUserList(UserDO user);

    /**
     * 获取顶级部门
     *
     * @return
     */
    DepartmentDO getSuperDepartment();

    /**
     * 根据登录用户获取权限部门
     *
     * @param name
     * @return
     */
    List<DepartmentDO> getLoginDepartMent(String name);

    List<DepartmentDO> getUserRoleDepartmentByUserOptimised(String id);

    boolean addOrUpdateUser(UserDO user, String departmentListStr, String navigationListStr);

    /**
     * 删除用户，真实删除
     * @param user
     */
    void deleteUser(UserDO user);

    void deleteUserByDepartmentId(String departmentId, String userId);

    Long selectUserCountByPermission(String permissionId);

    /**
     * 通过账户名称获取账户对象
     * @param userName
     * @return
     */
    UserDO getUserByUserName(String userName);

    void updateUser(UserDO user);

    /**
     * 查询用户是否不想显示提示
     *
     * @param userId
     * @return
     */
    int queryRefusePromptUser(String userId);

    /**
     * 用户不想显示提示 保存
     *
     * @param userId
     * @return
     */
    int insertRefusePromptUser(String userId);

    int updateErrorLoginCount(String userId, boolean success) throws Exception;

    Map<String, Object> queryErrorLoginInfo(String userId);

	UserDO getLoginUserNoCacheByUserName(String userName);

	List<UserDO> getUserListByLoginUserRoleType(Integer roleType);

    /**
     * 根据登录用户的角色类型，获取相应类型的账户分页列表。
     * @param user 当前登录用户
     * @param start
     * @param length
     * @return
     */
    List<UserDO> getUserListByLoginUserRoleTypeInPages(UserDO user, int start, int length);

    /**
     * 根据登录用户的角色类型，获取相应类型的账户列表的总数
     * @param user 当前登录用户
     * @return
     */
    int countUserListByLoginUserRoleTypeInPages(UserDO user);

    /**
     * 检查账户名是否重复
     * @param user
     * @return 可用返回true
     */
    boolean checkUserNameDuplicate(UserDO user);

    /**
     * 根据账户id，查询账户对象
     * @param userId
     * @return
     */
    UserDO getUserByUserId(Integer userId);

    /**
     * 修改账户个人资料
     * @param user
     */
    void updateUserInfo(UserDO user);

    /**
     * 获取全部操作员的列表，即roleType为2的全部
     * @return
     */
    List<UserDO> getAllOperatorList();

    /**
     * 根据审批流程模型中的审批人，获取对应账户的姓名
     * @param approverIdList
     * @return
     */
    JSONArray getUserMapByIdList(List<String> approverIdList);

    /**
     *根据审批模型id，获取审批人guid字符串
     * @param approveModelId
     * @return
     */
    String getUserIdListByApproveModelId(Integer approveModelId);

    /**
     * 转化为审批界面回显审批人需要的JsonArray结构，包含guid和name，回显账户的check:true
     * @param userList
     * @param approvers
     * @return
     */
    JSONArray toApproveJsonArr(List<UserDO> userList, String approvers);
}
