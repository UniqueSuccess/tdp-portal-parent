package cn.goldencis.tdp.core.service;

import java.util.List;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.entity.DepartmentDOCriteria;

import com.alibaba.fastjson.JSONArray;

/**
 * 部门管理service
 * @author Administrator
 *
 */
public interface IDepartmentService extends BaseService<DepartmentDO, DepartmentDOCriteria> {

    /**
     * 根据查询条件获取列表，分页查询
     * @param startNum
     * @param pageSize
     * @param pId
     * @param treePath
     * @param ordercase
     * @return
     */
    List<DepartmentDO> getDeptarMentListByParent(Integer startNum, Integer pageSize, Integer pId, String treePath, String ordercase, String searchstr);

    /**
     * 根据查询条件获取全部列表
     * @param pId
     * @return
     */
    List<DepartmentDO> getDeptarMentListByParent(Integer pId);

    /**
     * 根据条件查询总数
     * @param pId
     * @param treePath
     * @return
     */
    long getDeptarMentCountByParent(Integer pId, String treePath, String searchstr);

    /**
     * 管理员无权限限制，获取全部部门树json
     * @return
     */
    JSONArray getManagerNodes();

    /**
     * 根据登录用户权限获取部门树json
     * @return
     */
    JSONArray getNodesByLogin();

    /**
     * 修改部门
     * @param bean
     * @return
     */
    boolean updatedept(DepartmentDO bean);

    /**
     * 删除部门
     * @param id
     * @return
     */
    boolean deleteById(Integer id);

    /**
     * 通过部门id获取对应部门对象
     * @param id
     * @return
     */
    DepartmentDO getDepartmentById(Integer id);

    /**
     * 根据部门名字查找部门
     * @param name
     * @return
     */
    List<DepartmentDO> selectDepartmentByName(String name);

    /**
     * 添加部门
     *
     * @param bean
     * @return
     */
    boolean addDepartment(DepartmentDO bean);

    /**
     * 为列表中的部门添加父类部门名称
     * @param parentDept 总父类
     * @param departmentList 需要添加父类部门名称的列表
     */
    void setParentDepartmentNames(DepartmentDO parentDept, List<DepartmentDO> departmentList);

    /**
     * 获取全部部门树，如果账户id，查询账户对应的部门权限，加上check:true
     * @param userId
     * @return
     */
    JSONArray getDepartmentTreeByUserId(Integer userId);

    /**
     * 获取全部部门
     * @return
     */
    List<DepartmentDO> getAllDepartment();

    /**
     * 校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
     * @param departmentList 需要校验权限的部门集合
     */
    void checkLoginUserDepartment(List<DepartmentDO> departmentList);

    /**
     * 获取当前登录账户权限对应的部门id集合
     * @return 部门id集合
     */
    List<Integer> getLoginUserDepartmentIdList();

    /**
     * 获取一级部门的集合
     * @return
     */
    List<DepartmentDO> getDepartmentLevelOne();

    /**
     * 通知客户端 改变策略
     * @param id
     * @param policyId
     */
    void changePolicy(Integer id, Integer newPolicyId, Integer oldPolicyId);
}
