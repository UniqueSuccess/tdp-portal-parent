package cn.goldencis.tdp.policy.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.entity.PolicyDOCriteria;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/2.
 */
public interface IPolicyService extends BaseService<PolicyDO, PolicyDOCriteria> {

    /**
     * 通过策略id查询策略对象
     * @param id
     * @return
     */
    PolicyDO getByPrimaryKey(Integer id);

    /**
     * 获取全部的策略列表
     * @return
     */
    List<PolicyDO> getAllPolicyList();

    /**
     * 通过策略名称查询策略对象
     * @param name
     * @return
     */
    PolicyDO getPolicyByName(String name);

    /**
     * 检查策略名称是否重复
     * @param policy
     * @return 可用返回True
     */
    boolean checkPolicyNameDuplicate(PolicyDO policy);

    /**
     * 新建策略
     * @param policy
     * @param parentPolicy
     * @throws IOException
     */
    void addPolicy(PolicyDO policy, PolicyDO parentPolicy) throws IOException;

    /**
     * 继承父类策略，将父类策略的json文件拷贝到当前策略目录下
     * @param policy
     * @param parentPolicy
     * @throws IOException
     */
    void copyPolicyFileFromParentpolicy(PolicyDO policy, PolicyDO parentPolicy) throws IOException;

    /**
     * 读取策略JSON文件，将文件内容转换为JSON对象,以JSONOBJECT的形式返回
     * @param policy
     * @return
     */
    JSONObject readPolicyJsonFileById(PolicyDO policy) throws IOException;

    /**
     * 根据策略id删除策略，并将使用该策略的用户策略设置为默认，（默认为1）。
     * @param policyId
     */
    void deletePolicyById(Integer policyId);

    /**
     * 更新策略的Json文件内容
     * @param jsonObject
     */
    void updatePolicyJsonFile(JSONObject jsonObject) throws IOException;

    /**
     * 统计策略潜在风险
     * @return
     */
    Map<Integer,Integer> countPolicyPotentialRisk();

    /**
     * 检查正在使用的策略中，是否应用了该审批流程。
     * @param policyList 策略集合
     * @param approveDefinitionId
     * @return 若存在则返回false，不存在返回true
     */
    boolean checkApproveInUsing(List<PolicyDO> policyList, Integer approveDefinitionId);

    void copyDefaultPolicy();
}
