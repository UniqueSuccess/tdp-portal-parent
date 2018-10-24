package cn.goldencis.tdp.core.service.impl;

import java.util.*;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.*;
import cn.goldencis.tdp.core.entity.*;

import org.apache.ibatis.session.RowBounds;
import org.aspectj.weaver.ast.HasAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.mqclient.MQClient;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.ListUtils;
import cn.goldencis.tdp.common.utils.StringUtils;
import cn.goldencis.tdp.core.service.IDepartmentService;
import cn.goldencis.tdp.core.utils.GetLoginUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 部门管理service实现类
 * @author Administrator
 *
 */
@Component("departmentService")
public class DepartmentServiceImpl extends AbstractBaseServiceImpl<DepartmentDO, DepartmentDOCriteria> implements
        IDepartmentService {

    @Autowired
    private DepartmentDOMapper mapper;

    @Autowired
    private CustomizedMapper cmapper;

    @Autowired
    private UserDepartmentDOMapper udmapper;

    @Autowired
    private CClientUserDOMapper cClientUserDOMapper;

    @Autowired
    private UserDOMapper userMapper;

    @Autowired
    private MQClient publisher;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @SuppressWarnings("unchecked")
    @Override
    protected BaseDao<DepartmentDO, DepartmentDOCriteria> getDao() {
        return mapper;
    }

    /**
     * 管理员无权限限制，获取全部部门树json
     * @return
     */
    public JSONArray getManagerNodes() {
        JSONArray zNodes = toTreeJson(cmapper.getDeptarMentList(null, null, null, null, null), false);
        return zNodes;
    }

    /*
     * 根据登录用户权限获取部门树json
     * @param ischeck 是否有未分组
     * @return
     */
    public JSONArray getNodesByLogin() {

        JSONArray zNodes = new JSONArray();

        //获取当前登录账户
        UserDO loginUser = GetLoginUser.getLoginUser();
        String userGuid = loginUser.getGuid();

        //获取账户关联部门的关联对象集合
        UserDepartmentDOCriteria udexample = new UserDepartmentDOCriteria();
        udexample.createCriteria().andUserIdEqualTo(userGuid);
        List<UserDepartmentDO> userDepartmentList = udmapper.selectByExample(udexample);
        List<Integer> deptIdList = new ArrayList<>();
        //将集合转化为部门id的集合
        for (UserDepartmentDO userDepartment : userDepartmentList) {
            deptIdList.add(userDepartment.getDepartmentId());
        }

        //生成返回页面的部门树
        if (deptIdList.size() > 0) {
            //查询用户权限关联的部门对象集合
            DepartmentDOCriteria example = new DepartmentDOCriteria();
            example.createCriteria().andIdIn(deptIdList);
            List<DepartmentDO> departmentList = mapper.selectByExample(example);

            //将部门的部门树中包含的父系部门id放入Set中，利用Set去重，获取不重复的部门id集合，等待处理
            Set<String> parentIdSet = new HashSet();
            for (DepartmentDO department : departmentList) {
                String[] parentIdArr = department.getTreePath().split(",");
                for (String parentId : parentIdArr) {
                    parentIdSet.add(parentId);
                }
            }

            //去除空的字符串
            if (parentIdSet.contains(ConstantsDto.EMPTY_STR)) {
                parentIdSet.remove(ConstantsDto.EMPTY_STR);
            }

            //去除Set中，在部门集合中已经存在的id。
            for (DepartmentDO department : departmentList) {
                if (parentIdSet.contains(department.getId().toString())) {
                    //集合中已经存在，从Set中去除
                    parentIdSet.remove(department.getId().toString());
                }
            }

            //Set中剩余的部门id，是用来补全部门树的，需要再次查询
            if (parentIdSet.size() > 0 ) {
                deptIdList.clear();
                for (String parentId : parentIdSet) {
                    deptIdList.add(Integer.parseInt(parentId));
                }

                //查询需补全部门树的部门列表
                List<DepartmentDO> supplyList = mapper.selectByExample(example);
                //添加到权限部门集合中
                departmentList.addAll(supplyList);
            }

            zNodes = toTreeJson(departmentList, true);
        }

        return zNodes;
    }

    /**
     * 修改部门
     * @param bean
     * @return
     */
    @Transactional
    public boolean updatedept(DepartmentDO bean) {

        if (ConstantsDto.SUPER_DEPARTMENT_ID.equals(bean.getId().toString())) {
            mapper.updateByPrimaryKeySelective(bean);
        } else {
            DepartmentDO pdept = mapper.selectByPrimaryKey(String.valueOf(bean.getParentId()));

            //获取修改之前，部门的部门树+id，用来查询子部门。,1,44,45,
            String oldtreepath = bean.getTreePath() + bean.getId() + ",";
            //通过父类部门，获取修改后的部门树.,1,
            String newtreepath = pdept.getTreePath() + pdept.getId() + ",";

            //获取修改部门之前，该部门下的子类部门列表
            DepartmentDOCriteria example = new DepartmentDOCriteria();
            example.createCriteria().andTreePathLike(oldtreepath + "%");
            List<DepartmentDO> clist = mapper.selectByExample(example);

            //保存修改的部门id列表
            List<Integer> dids = new ArrayList<>();
            dids.add(bean.getId());

            //跟新子部门的部门树
            for (DepartmentDO cdept : clist) {
                //添加到修改部门id列表
                dids.add(cdept.getId());
                //替换子类部门的部门树
                String treePath = cdept.getTreePath();
                treePath = treePath.replace(bean.getTreePath(), newtreepath);
                cdept.setTreePath(treePath);
                //跟新子类部门
                mapper.updateByPrimaryKeySelective(cdept);
            }

            //更新本次修改部门的部门树
            bean.setTreePath(newtreepath);
            //bean.setStatus(1);
            //更新部门
            mapper.updateByPrimaryKeySelective(bean);

            //获取部门用户关联列表
            UserDepartmentDOCriteria udexample = new UserDepartmentDOCriteria();
            udexample.createCriteria().andDepartmentIdEqualTo(bean.getId());
            List<UserDepartmentDO> hlist = udmapper.selectByExample(udexample);

            //获取关联用户的id集合
            List<String> userids = new ArrayList<>();
            if (hlist != null) {
                hlist.forEach(tmp -> userids.add(tmp.getUserId()));
            }

            //获取修改部门后，有父类部门权限的用户中，没有当前部门权限的，用户部门关联表。需要为他们添加本次修改部门的权限
            udexample.clear();
            udexample.createCriteria().andDepartmentIdEqualTo(bean.getParentId()).andUserIdNotIn(userids);
            List<UserDepartmentDO> nlist = udmapper.selectByExample(udexample);
            //添加用户部门权限
            addRole(nlist, dids);
        }
        return true;
    }

    /**
     * 通过父类id和父类treePath查询子类集合，返回列表中包含父类本身。分页查询。
     * @param startNum
     * @param pageSize
     * @param pId 父类id
     * @param treePath 父类treePath
     * @param ordercase 查询条件，暂未实现
     * @return
     */
    @Override
    public List<DepartmentDO> getDeptarMentListByParent(Integer startNum, Integer pageSize, Integer pId, String treePath, String ordercase) {
        RowBounds rowBounds = new RowBounds(startNum, pageSize);

        //获取当前登录账户权限对应的部门id集合
        List<Integer> idList = this.getLoginUserDepartmentIdList();

        //通过父类id和父类treePath、和父类本身 查询子类集合
        DepartmentDOCriteria departmentDOCriteria = new DepartmentDOCriteria();
        departmentDOCriteria.createCriteria().andParentIdEqualTo(pId).andIdIn(idList);
        departmentDOCriteria.or().andTreePathLike(treePath).andIdIn(idList);
        //含父类本身，但是不包含顶级部门
        if (pId != 1 && idList.contains(pId)) {
            departmentDOCriteria.or().andIdEqualTo(pId);
        }

        List<DepartmentDO> departmentList = mapper.selectByExampleWithRowbounds(departmentDOCriteria, rowBounds);
        /**
         * 根据策略id查询策略名称
         */
        addPolicyName(departmentList);
        return departmentList;
    }

    private void addPolicyName(List<DepartmentDO> departmentList) {
        if (ListUtils.isEmpty(departmentList)) {
            return;
        }
        List<Integer> idList = new ArrayList<Integer>();
        for (DepartmentDO department : departmentList) {
            idList.add(department.getPolicyId());
        }
        if (ListUtils.isEmpty(idList)) {
            return;
        }
        List<Map<String, String>> policyNameList = mapper.queryPolicyNameById(idList);
        Map<String, String> policyNameMap = new HashMap<String, String>();
        for (Map<String, String> map : policyNameList) {
            policyNameMap.put(String.valueOf(map.get("id")), String.valueOf(map.get("name")));
        }
        for (DepartmentDO department : departmentList) {
            if (policyNameMap.containsKey(String.valueOf(department.getPolicyId()))) {
                department.setPolicyName(policyNameMap.get(String.valueOf(department.getPolicyId())));
            }
        }
    }

    /**
     * 根据父类id，查询该部门下所有部门的集合,如果要校验登录账户在部门权限，配合this.checkLoginUserDepartment方法使用
     * @param pId
     * @return
     */
    @Override
    public List<DepartmentDO> getDeptarMentListByParent(Integer pId) {
        DepartmentDO parentDept = mapper.selectByPrimaryKey(String.valueOf(pId));
        DepartmentDOCriteria departmentDOCriteria = new DepartmentDOCriteria();
        departmentDOCriteria.createCriteria().andParentIdEqualTo(pId);
        departmentDOCriteria.or().andTreePathLike(parentDept.getTreePath() + parentDept.getId() + ",%");
        List<DepartmentDO> departmentList = mapper.selectByExample(departmentDOCriteria);
        departmentList.add(parentDept);
        return departmentList;
    }

    /**
     * 通过父类id和父类treePath、和父类本身 查询子类集合的总数量
     * @param pId
     * @param treePath
     * @return
     */
    @Override
    public long getDeptarMentCountByParent(Integer pId, String treePath) {
        //获取当前登录账户权限对应的部门id集合
        List<Integer> idList = this.getLoginUserDepartmentIdList();

        //通过父类id和父类treePath、和父类本身 查询子类集合的总数量
        DepartmentDOCriteria departmentDOCriteria = new DepartmentDOCriteria();
        departmentDOCriteria.createCriteria().andParentIdEqualTo(pId).andIdIn(idList);
        departmentDOCriteria.or().andTreePathLike(treePath).andIdIn(idList);
        //含父类本身，但是不包含顶级部门
        if (pId != 1 && idList.contains(pId)) {
            departmentDOCriteria.or().andIdEqualTo(pId);
        }

        return mapper.countByExample(departmentDOCriteria);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteById(Integer id) {
        //根据部门id，查询该部门UserDepartmentDO的关联
        UserDepartmentDOCriteria udexample = new UserDepartmentDOCriteria();
        udexample.createCriteria().andDepartmentIdEqualTo(id);
        List<UserDepartmentDO> userList = udmapper.selectByExample(udexample);

        //遍历与当前部门关联的账户列表
        for (UserDepartmentDO temp : userList) {
            //查询当前账户关联的部门列表
            UserDepartmentDOCriteria tmpCriteria = new UserDepartmentDOCriteria();
            tmpCriteria.createCriteria().andUserIdEqualTo(temp.getUserId()).andDepartmentIdNotEqualTo(id);
            //如果当前账户没有其他关联部门，则创建一条与未分组关联的记录，插入数据库，用户分组调整为未分组。
            if (udmapper.selectByExample(tmpCriteria).size() == 0) {
                UserDepartmentDO record = new UserDepartmentDO();
                record.setDepartmentId(new Integer(ConstantsDto.DEPARTMENT_UNKOWN_GROUP));
                record.setUserId(temp.getUserId());
                udmapper.insert(record);
            }
        }
        //删除原有该部门UserDepartmentDO的关联
        udmapper.deleteByExample(udexample);

        //将用户中，属于该部门下的用户，归属到未分组
        cClientUserDOMapper.setClientUsersUngroup(id);

        //删除部门
        mapper.deleteByPrimaryKey(id.toString());
        return true;
    }

    /**
     * 转化成ztree json
     * @param departments
     * @param isedit
     * @return
     */
    private JSONArray toTreeJson(List<DepartmentDO> departments, boolean isedit) {
        JSONArray array = new JSONArray();
        JSONObject unknownGroup = new JSONObject();
        boolean hasUnknown = false;
        for (DepartmentDO department : departments) {
            JSONObject obj = new JSONObject();
            if (isedit && 1 == department.getId()) {
                obj.put("checkdisable", true);
            }
            if (2 == department.getId()) {
                unknownGroup.put("remark", department.getDepartmentRemark());
                unknownGroup.put("id", department.getId());
                unknownGroup.put("pId", department.getParentId() == -1 ? null : department.getParentId());
                unknownGroup.put("name", department.getName());
                unknownGroup.put("iconSkin", "tDepartment");
                unknownGroup.put("ParentDepartmentId", department.getParentId());
                unknownGroup.put("treePath", department.getTreePath());
                unknownGroup.put("open", true);
                unknownGroup.put("status", department.getStatus());
                unknownGroup.put("policyId", department.getPolicyId());
                unknownGroup.put("level", 1);
                hasUnknown = true;
            } else {
                obj.put("remark", department.getDepartmentRemark());
                obj.put("id", department.getId());
                obj.put("pId", department.getParentId() == -1 ? null : department.getParentId());
                obj.put("name", department.getName());
                obj.put("iconSkin", "tDepartment");
                obj.put("ParentDepartmentId", department.getParentId());
                obj.put("treePath",department.getTreePath());
                obj.put("open", true);
                obj.put("status", department.getStatus());
                obj.put("policyId", department.getPolicyId());
                obj.put("level", department.getId() == 1?0: StringUtils.levelNum(department.getTreePath()));
                array.add(obj);
            }
        }
        if (hasUnknown) {
            array.add(unknownGroup);
        }
        return array;
    }

    /**
     * 通过部门id获取对应部门对象
     * @param id
     * @return
     */
    @Override
    public DepartmentDO getDepartmentById(Integer id) {
        return mapper.selectByPrimaryKey(id.toString());
    }

    /**
     * 通过部门name获取对应部门对象
     * @param name
     * @return
     */
    @Override
    public List<DepartmentDO> selectDepartmentByName(String name) {
        DepartmentDOCriteria udexample = new DepartmentDOCriteria();
        udexample.createCriteria().andNameEqualTo(name);
        return mapper.selectByExample(udexample);
    }

    /**
     * 添加部门
     * @param bean
     * @return
     */
    @Override
    @Transactional
    public boolean addDepartment(DepartmentDO bean) {

        //插入当前传入的部门，注意：要在mybatis的配置文件中使用keyProperty将主键id传回。
        mapper.insertSelective(bean);

        //查询与父类部门相关联的账户集合
        UserDepartmentDOCriteria example = new UserDepartmentDOCriteria();
        example.createCriteria().andDepartmentIdEqualTo(bean.getParentId());
        List<UserDepartmentDO> list = udmapper.selectByExample(example);

        //传入插入sql时回传的部门id
        List<Integer> dids = new ArrayList<>();
        dids.add(bean.getId());

        //将新增部门和父类部门下的关联账户进行关联。
        cmapper.addUserDeparts(addRole(list, dids));
        return true;
    }

    /**
     * 为列表中的部门添加父类部门名称
     * @param parentDept 总父类
     * @param departmentList 需要添加父类部门名称的列表
     */
    @Override
    public void setParentDepartmentNames(DepartmentDO parentDept, List<DepartmentDO> departmentList) {

        //设置查询标记
        boolean selectFlag = false;

        //将父类部门和列表转化为Map
        Map<Integer, DepartmentDO> parentDepartmentMap = new HashMap<>();
        if (parentDept != null) {
            parentDepartmentMap.put(parentDept.getId(), parentDept);
        }
        for (DepartmentDO department : departmentList) {
            parentDepartmentMap.put(department.getId(), department);
        }

        //遍历列表，从Map中获取父类部门并赋值父类名称。
        List<Integer> parentDepartmentIds = new ArrayList<>();
        for (DepartmentDO department : departmentList) {
            if (parentDepartmentMap.containsKey(department.getParentId())) {
                department.setParentName(parentDepartmentMap.get(department.getParentId()).getName());
            } else {
                //Map中没有父类，添加到List中等待查询
                if (!parentDepartmentIds.contains(department.getParentId())) {
                    parentDepartmentIds.add(department.getParentId());
                }
                selectFlag = true;
            }
        }

        //查询父类的部门，给剩余部门的父类名称赋值
        if (selectFlag) {
            DepartmentDOCriteria example = new DepartmentDOCriteria();
            example.createCriteria().andIdIn(parentDepartmentIds);
            List<DepartmentDO> selectDepartments = mapper.selectByExample(example);
            for (DepartmentDO department : selectDepartments) {
                parentDepartmentMap.put(department.getId(),department);
            }
            for (DepartmentDO department : departmentList) {
                if ((department.getParentName() == null || "".equals(department.getParentName())) && department.getId() != 1) {
                    department.setParentName(parentDepartmentMap.get(department.getParentId()).getName());
                }
            }
        }
    }

    /**
     * 获取全部部门树，如果账户id，查询账户对应的部门权限，加上check:true
     * @param userId
     * @return
     */
    @Override
    public JSONArray getDepartmentTreeByUserId(Integer userId) {
        List<Integer> departmentIdList = null;

        //如果账户id不为0，查询账户的部门权限，如果为0，则是新建，不查询。
        if (userId != 0) {
            UserDO user = userMapper.selectByPrimaryKey(userId);

            UserDepartmentDOCriteria udExample = new UserDepartmentDOCriteria();
            udExample.createCriteria().andUserIdEqualTo(user.getGuid());
            List<UserDepartmentDO> userDepartmentList = udmapper.selectByExample(udExample);

            departmentIdList = new ArrayList<>();
            for (UserDepartmentDO userDepartment : userDepartmentList) {
                departmentIdList.add(userDepartment.getDepartmentId());
            }
        }

        //获取全部部门信息
        DepartmentDOCriteria example = new DepartmentDOCriteria();
        List<DepartmentDO> departmentList = mapper.selectByExample(example);

        //转化为前台需要的JSON格式，如果idList不为空，将对应id的部门check:true
        JSONArray jsonArray = new JSONArray();
        for (DepartmentDO department : departmentList) {
            JSONObject deptJson = new JSONObject();
            deptJson.put("id", department.getId());
            deptJson.put("pId", department.getParentId() == -1 ? null : department.getParentId());
            deptJson.put("ParentDepartmentId", department.getParentId());
            deptJson.put("name", department.getName());
            deptJson.put("treePath",department.getTreePath());
            deptJson.put("status", department.getStatus());
            deptJson.put("open", true);
            deptJson.put("iconSkin", "tDepartment");
            deptJson.put("remark", department.getDepartmentRemark());

            if (departmentIdList != null && departmentIdList.contains(department.getId())) {
                deptJson.put("checked",true);
            }
            jsonArray.add(deptJson);
        }

        return jsonArray;
    }

    /**
     * 获取全部部门
     * @return
     */
    @Override
    public List<DepartmentDO> getAllDepartment() {
        DepartmentDOCriteria example = new DepartmentDOCriteria();
        return mapper.selectByExample(example);
    }

    /**
     * 校验登录账户的权限，保留传入的部门集合中，拥有权限的部门
     * @param departmentList 需要校验权限的部门集合
     */
    @Override
    public void checkLoginUserDepartment(List<DepartmentDO> departmentList) {

        //获取当前登录账户权限对应的部门id集合
        List<Integer> idList = this.getLoginUserDepartmentIdList();

        DepartmentDO department;
        for (int i = departmentList.size() - 1; i >= 0; i--) {
            department = departmentList.get(i);
            //账户没有该部门权限，则从参数部门集合中剔除该集合
            if (!idList.contains(department.getId())) {
                departmentList.remove(i);
            }
        }

    }

    /**
     * 获取当前登录账户权限对应的部门id集合
     * @return
     */
    @Override
    public List<Integer> getLoginUserDepartmentIdList() {
        //获取登录账户的部门权限集合
        List<DepartmentDO> departmentByUser = GetLoginUser.getDepartmentListWithLoginUser();
        //将获取的部门集合转换为部门id集合
        List<Integer> idList = new ArrayList<>();
        for (DepartmentDO department : departmentByUser) {
            idList.add(department.getId());
        }
        return idList;
    }

    /**
     * 获取一级部门的集合
     * @return
     */
    @Override
    public List<DepartmentDO> getDepartmentLevelOne() {
        DepartmentDOCriteria example = new DepartmentDOCriteria();
        example.createCriteria().andParentIdEqualTo(ConstantsDto.SUPER_DEPARTMENT_ID);
        List<DepartmentDO> deptLvOneList = mapper.selectByExample(example);
        return deptLvOneList;
    }

    /**
     * 将用户id集合跟部门id，交叉组合，返回UserDepartmentDO关联表对象集合
     * @param list
     * @param dids
     * @return List<UserDepartmentDO>UserDepartmentDO关联表对象集合
     */
    public List<UserDepartmentDO> addRole(List<UserDepartmentDO> list, List<Integer> dids) {
        List<UserDepartmentDO> addlist = new ArrayList<>();
        if (list != null) {
            list.forEach(tmp -> {
                dids.forEach(dtmp -> {
                    UserDepartmentDO uddo = new UserDepartmentDO();
                    uddo.setDepartmentId(dtmp.intValue());
                    uddo.setUserId(tmp.getUserId());
                    addlist.add(uddo);
                });

            });
        }
        return addlist;
    }

    @Override
    public void changePolicy(Integer id, Integer newPolicyId, Integer oldPolicyId) {
        try {
            /**
             * 1 查询该部门下使用之前老策略的用户
             * 2 通知这些用户使用新策略
             */
            List<String> list = mapper.queryClientUserList(id, oldPolicyId);
            if (ListUtils.isEmpty(list)) {
                return;
            }
            List<Map<String, String>> policyList = mapper.queryPolicyNameById(Arrays.asList(id));
            if (ListUtils.isEmpty(policyList)) {
                return;
            }
            /**
             * 逻辑按照之前vdp 这里没法复用之前代码 会造成循环引用
             */
            taskExecutor.execute(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //设置消息为策略消息
                    String message = "bdppolicy";

                    //设置type为及时消息
                    int type = MQClient.MSG_REALTIME;

                    //组装消息内容
                    JSONObject contentJson = new JSONObject();
                    contentJson.put("url", policyList.get(0).get("path"));
                    contentJson.put("time", policyList.get(0).get("modify_time"));
                    String content = contentJson.toJSONString();

                    //使用消息缓存服务
                    publisher.clientNotify(ListUtils.covertStringByList(list, null) ,message, content, type);
                }
            });
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}