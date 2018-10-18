package cn.goldencis.tdp.core.service.impl;

import java.util.*;

import cn.goldencis.tdp.core.dao.*;
import cn.goldencis.tdp.core.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.service.IUserService;

/**
 * 管理员service实现类
 *
 * @author Administrator
 */
@Component("userService")
public class UserServiceImpl extends AbstractBaseServiceImpl<UserDO, UserDOCriteria> implements IUserService {

    @Autowired
    private UserDOMapper mapper;

    @Autowired
    private DepartmentDOMapper dmapper;

    @Autowired
    private CustomizedMapper cmapper;

    @Autowired
    private UserDepartmentDOMapper userDepartmentDOMapper;

    @Autowired
    private CUserDepartmentDOMapper cuserDepartmentDOMapper;

    @Autowired
    private CUserNavigationDOMapper cUserNavigationDOMapper;

    @SuppressWarnings("unchecked")
    @Override
    protected BaseDao<UserDO, UserDOCriteria> getDao() {
        return mapper;
    }

    @Override
    @Transactional
    public boolean addOrUpdateUser(UserDO user, String departmentListStr, String navigationListStr) {
        //设置账户状态，暂时无用
        user.setStatus(11);

        //没有部门则设置未分组，账户部门归属暂时无用，不代表部门权限
        if (user.getDepartment() == null) {
            user.setDepartment(0);
        }

        //解析部门权限，转化为集合。插入账户部门关联表。
        String[] departmentArr = departmentListStr.split(",");
        List<Integer> departmentIdList = new ArrayList<>();
        for (String departmentId : departmentArr) {
            departmentIdList.add(Integer.parseInt(departmentId));
        }

        //解析页面权限，转化为集合，插入账户权限表。
        String[] navigationArr = navigationListStr.split(",");
        List<Integer> navigationIdList = new ArrayList<>();
        for (String navigationId : navigationArr) {
            navigationIdList.add(Integer.parseInt(navigationId));
        }

        //判断是新建还是更新，没有即新建账户，补全对象属性。
        if (user.getId() == null) {

            user.setGuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            //创建账户,同时获取新建账户的id
            mapper.insertSelective(user);

        } else {
            //用户id存在，执行用户的选择性更新。
            mapper.updateByPrimaryKeySelective(user);

            //更新部门权限
            //先删除账户部门关联表中，该用户关联的部门权限
            cuserDepartmentDOMapper.batchDeleteUserDepartmentByUserId(user.getGuid());

            //更新页面权限
            //先删除账户页面关联表中，该用户关联的页面权限
            cUserNavigationDOMapper.batchDeleteUserNavigationByUserId(user.getGuid());
        }

        //为账户批量添加部门权限
        if (departmentIdList != null) {
            cuserDepartmentDOMapper.batchInsertByOneUserAndDepartmentList(user.getGuid(), departmentIdList);
        }

        //为账户添加页面权限
        if (navigationIdList != null) {
            cUserNavigationDOMapper.batchInsertByOneUserAndNavigationList(user.getGuid(),navigationIdList);
        }
        return true;
    }

    @Cacheable(value = "loginUser", key = "#username")
    public UserDO getLoginUser(String username) {
        UserDOCriteria userexample = new UserDOCriteria();
        userexample.createCriteria().andUserNameEqualTo(username).andStatusEqualTo(11);
        UserDO user = super.getBy(userexample);
        return user;
    }
    public UserDO getLoginUserNocache(String username) {
        UserDOCriteria userexample = new UserDOCriteria();
        userexample.createCriteria().andUserNameEqualTo(username).andStatusEqualTo(11);
        UserDO user = super.getBy(userexample);
        return user;
    }

    @Cacheable(value = "superDepartment")
    @Override
    public DepartmentDO getSuperDepartment() {
        return dmapper.selectByPrimaryKey(ConstantsDto.SUPER_DEPARTMENT_ID.toString());
    }

    @Override
    public List<DepartmentDO> getLoginDepartMent(String name) {
        UserDOCriteria userexample = new UserDOCriteria();
        userexample.createCriteria().andUserNameEqualTo(name).andStatusEqualTo(11);
        UserDO user = super.getBy(userexample);
        return cmapper.getUserDepartmentByUser(user.getGuid());
    }

    @Override
    public List<DepartmentDO> getUserRoleDepartmentByUserOptimised(String id) {
        List<DepartmentDO> list = cmapper.getUserDepartmentByUser(id);
        List<String> intList = new ArrayList<String>();
        for (DepartmentDO tmp : list) {
            intList.add(tmp.getId().toString());
        }
        return cmapper.getUserRoleDepartmentByParentIdOptimised(intList);
    }

    public List<UserDO> getUserListByLoginUserRoleType(Integer roleType) {
        UserDOCriteria userDOCriteria = new UserDOCriteria();
        userDOCriteria.createCriteria().andRoleTypeEqualTo(roleType);
        List<UserDO> userList = mapper.selectByExample(userDOCriteria);
        return userList;
    }

    @Override
    public Long countUserList(UserDO user) {
        return cmapper.countUserList(user);
    }

    @Override
    public List<UserDO> getUserList(UserDO user) {
        return cmapper.getUserList(user);
    }

    @Override
    public List<UserDO> getAllUser() {
        UserDOCriteria userDOCriteria = new UserDOCriteria();
        return mapper.selectByExample(userDOCriteria);
    }

    /**
     * 根据登录用户的角色类型，获取相应类型的账户分页列表。
     * @param user 当前登录用户
     * @param start
     * @param length
     * @return
     */
    @Override
    public List<UserDO> getUserListByLoginUserRoleTypeInPages(UserDO user, int start, int length) {
        RowBounds rowBounds = new RowBounds(start, length);

        UserDOCriteria example = new UserDOCriteria();
        //如果是超级管理员，返回查询所有用户,如果是其他用户，根据角色类型，查询相同类型的用户
        if (!"1".equals(user.getGuid())) {
            example.createCriteria().andRoleTypeEqualTo(user.getRoleType());
        }
        List<UserDO> userList = mapper.selectByExampleWithRowbounds(example, rowBounds);

        return userList;
    }

    /**
     * 根据登录用户的角色类型，获取相应类型的账户列表的总数
     * @param user 当前登录用户
     * @return
     */
    @Override
    public int countUserListByLoginUserRoleTypeInPages(UserDO user) {
        UserDOCriteria example = new UserDOCriteria();
        //如果是超级管理员，返回查询所有用户,如果是其他用户，根据角色类型，查询相同类型的用户
        if (!"1".equals(user.getGuid())) {
            example.createCriteria().andRoleTypeEqualTo(user.getRoleType());
        }

        return (int)mapper.countByExample(example);
    }

    /**
     * 检查账户名是否重复
     * @param user
     * @return 可用返回true
     */
    @Override
    public boolean checkUserNameDuplicate(UserDO user) {
        UserDO preUser = this.getUserByUserName(user.getUserName());

        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preUser != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preUser.getId().equals(user.getId())) {
                return true;
            }
            //若果不同，说明为两个用户，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    /**
     * 根据账户id，查询账户对象
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public UserDO getUserByUserId(Integer userId) {
        return mapper.selectByPrimaryKey(userId);
    }

    /**
     * 修改账户个人资料
     * @param user
     */
    @Override
    public void updateUserInfo(UserDO user) {
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andIdEqualTo(user.getId());
        mapper.updateByExampleSelective(user, example);
    }

    /**
     * 获取全部操作员的列表，即roleType为2的全部，不包含超级管理员
     * @return
     */
    @Override
    public List<UserDO> getAllOperatorList() {
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andRoleTypeEqualTo(2);
        List<UserDO> userList = mapper.selectByExample(example);
        return userList;
    }

    /**
     * 根据审批流程模型中的审批人，获取对应账户的姓名
     * @param approverIdList
     * @return
     */
    @Override
    public JSONArray getUserMapByIdList(List<String> approverIdList) {
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andGuidIn(approverIdList);
        List<UserDO> userList = mapper.selectByExample(example);
        JSONArray jsonArray = null;
        if (userList != null && userList.size() > 0) {
            jsonArray = new JSONArray();
            for (UserDO user : userList) {
                JSONObject userJson = new JSONObject();
                userJson.put(user.getGuid(), user.getName());
                jsonArray.add(userJson);
            }
        }
        return jsonArray;
    }

    /**
     * 根据审批模型id，获取审批人guid字符串
     * @param approveModelId
     * @return
     */
    @Override
    public String getUserIdListByApproveModelId(Integer approveModelId) {
        return cmapper.getUserIdListByApproveModelId(approveModelId);
    }

    /**
     * 转化为审批界面回显审批人需要的JsonArray结构，包含guid和name，回显账户的check:true
     * @param userList
     * @param approvers
     * @return
     */
    @Override
    public JSONArray toApproveJsonArr(List<UserDO> userList, String approvers) {
        String[] approverArr = null;
        if (approvers != null) {
            approverArr = approvers.split(";");
        }
        JSONArray userArr = new JSONArray();
        for (UserDO user : userList) {
            JSONObject userJson = new JSONObject();
            userJson.put("guid", user.getGuid());
            userJson.put("name", user.getName());
            if (approverArr != null) {
                for (String approve : approverArr) {
                    if (user.getGuid().equals(approve)) {
                        userJson.put("checked", true);
                    }
                }
            }
            userArr.add(userJson);
        }
        return userArr;
    }

    /**
     * 删除用户，真实删除
     * @param user
     */
    @Override
    @Transactional
    public void deleteUser(UserDO user) {
        //删除账户部门关联表中关联该账户的记录
        cuserDepartmentDOMapper.batchDeleteUserDepartmentByUserId(user.getGuid());

        //删除账户页面权限关联表中关联该账户的记录
        cUserNavigationDOMapper.batchDeleteUserNavigationByUserId(user.getGuid());

        //删除账户
        mapper.deleteByPrimaryKey(user.getId());
    }

    @Override
    public void deleteUserByDepartmentId(String departmentId, String userId) {
        List<String> list = new ArrayList<String>();
        String arrStr[] = userId.split(",");
        for (String temp : arrStr) {
            list.add(temp);
        }
        cuserDepartmentDOMapper.deleteBatch(list, departmentId);
        for (String temp : list) {
            UserDepartmentDOCriteria criteria = new UserDepartmentDOCriteria();
            criteria.createCriteria().andUserIdEqualTo(temp);
            List<UserDepartmentDO> tempList = userDepartmentDOMapper.selectByExample(criteria);
            if (tempList.size() == 0) {
                UserDepartmentDO record = new UserDepartmentDO();
                record.setDepartmentId(new Integer(ConstantsDto.DEPARTMENT_UNKOWN_GROUP));
                record.setUserId(temp);
                userDepartmentDOMapper.insert(record);
            }
        }
    }

    @Override
    public Long selectUserCountByPermission(String permissionId) {
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andRoleTypeEqualTo(Integer.parseInt(permissionId));
        return mapper.countByExample(example);
    }

    /**
     * 通过账户名称获取账户对象
     * @param userName
     * @return
     */
    @Override
    public UserDO getUserByUserName(String userName) {
        UserDOCriteria example = new UserDOCriteria();
        example.createCriteria().andUserNameEqualTo(userName);

        List<UserDO> users = mapper.selectByExample(example);
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public UserDO getLoginUserNoCache(String username) {
        UserDOCriteria userexample = new UserDOCriteria();
        userexample.createCriteria().andUserNameEqualTo(username);
        UserDO user = super.getBy(userexample);
        return user;
    }

    @Override
    public UserDO getLoginUserNoCacheByUserName(String username) {
        UserDOCriteria userexample = new UserDOCriteria();
        userexample.createCriteria().andUserNameEqualTo(username);
        UserDO user = super.getBy(userexample);
        return user;
    }


    @Override
    public void updateUser(UserDO user) {
        mapper.updateByPrimaryKeySelective(user);
    }

   /* @Override
    public List<UserDO> queryUserExclude(String id) {
        return mapper.queryUserExclude(id);
    }*/

    @Override
    public int queryRefusePromptUser(String userId) {
        return mapper.queryRefusePromptUser(userId);
    }

    @Override
    public int insertRefusePromptUser(String userId) {
        return mapper.insertRefusePromptUser(userId);
    }

    @Override
    public synchronized int updateErrorLoginCount(String userId, boolean success) throws Exception {
        int flag = 0;
        //登录成功 清除错误记录
        if (success) {
            mapper.updateErrorLoginCount(userId, DateUtil.format(new Date(), DateUtil.FMT_DATE), flag);
            return flag;
        }
        Map<String, Object> map = mapper.queryErrorLoginInfo(userId);
        if (map != null) {
            int count = map.get("error_login_count") == null ? 0 : Integer.valueOf(map.get("error_login_count").toString());
            String lastLoginTimeStr = map.get("error_login_last_time") == null ? null : map.get("error_login_last_time").toString();
            if (count < 5) {
                flag = count + 1;
                mapper.updateErrorLoginCount(userId, DateUtil.format(new Date(), DateUtil.FMT_DATE), flag);
            } else {
                Date nowDate = new Date();
                Date lastLoginTime = DateUtil.strToDate(lastLoginTimeStr, DateUtil.FMT_DATE);
                flag = count + 1;
                if ((nowDate.getTime() - lastLoginTime.getTime()) > 10 * 60 * 1000) {
                    mapper.updateErrorLoginCount(userId, DateUtil.format(new Date(), DateUtil.FMT_DATE), flag);
                } else {
                    mapper.updateErrorLoginCount(userId, lastLoginTimeStr, flag);
                }
            }
        }
        return flag;
    }

    @Override
    public Map<String, Object> queryErrorLoginInfo(String userId) {
        return mapper.queryErrorLoginInfo(userId);
    }
}
