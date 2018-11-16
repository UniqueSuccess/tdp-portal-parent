package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.CClientUserDOMapper;
import cn.goldencis.tdp.core.dao.DepartmentDOMapper;
import cn.goldencis.tdp.policy.dao.ClientUserDOMapper;
import cn.goldencis.tdp.policy.dao.PolicyPotentialRiskDOMapper;
import cn.goldencis.tdp.policy.entity.*;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.policy.dao.PolicyDOMapper;
import cn.goldencis.tdp.policy.service.IPolicyService;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by limingchao on 2018/1/2.
 */
@Service
public class PolicyServiceImpl extends AbstractBaseServiceImpl<PolicyDO, PolicyDOCriteria> implements IPolicyService , ServletContextAware {

    @Autowired
    private PolicyDOMapper mapper;

    @Autowired
    private ClientUserDOMapper clientUserDOMapper;

    @Autowired
    private CClientUserDOMapper cClientUserDOMapper;

    @Autowired
    private PolicyPotentialRiskDOMapper riskDOMapper;

    @Autowired
    private DepartmentDOMapper departmentDOMapper;

    @Override
    protected BaseDao<PolicyDO, PolicyDOCriteria> getDao() {
        return this.mapper;
    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
    }

    /**
     * 通过策略id查询策略对象
     * @param id
     * @return
     */
    @Override
    public PolicyDO getByPrimaryKey(Integer id) {
        PolicyDO policy = mapper.selectByPrimaryKey(id);
        return policy;
    }

    /**
     * 获取全部的策略列表
     * @return
     */
    @Override
    public List<PolicyDO> getAllPolicyList() {
        PolicyDOCriteria example = new PolicyDOCriteria();
        List<PolicyDO> policyList = this.listBy(example);
        return policyList;
    }

    /**
     * 通过策略名称查询策略对象
     * @param name
     * @return
     */
    @Override
    public PolicyDO getPolicyByName(String name) {
        PolicyDOCriteria example = new PolicyDOCriteria();
        example.createCriteria().andNameEqualTo(name);
        List<PolicyDO> policyList = mapper.selectByExample(example);
        if (policyList != null && policyList.size() > 0) {
            return policyList.get(0);
        }
        return null;
    }

    /**
     * 检查策略名称是否重复
     * @param policy
     * @return 可用返回True
     */
    @Override
    public boolean checkPolicyNameDuplicate(PolicyDO policy) {
        //通过策略名获取数据库中策略记录
        PolicyDO prePolicy = this.getPolicyByName(policy.getName());

        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (prePolicy != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (prePolicy.getId().equals(policy.getId())) {
                return true;
            }
            //若果不同，说明为两个策略，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    /**
     * 新建策略
     * @param policy
     * @param parentPolicy
     */
    @Override
    @Transactional
    public void addPolicy(PolicyDO policy, PolicyDO parentPolicy) throws IOException {
        //补全策略除path以外的信息。空置path。
        policy.setDefaultId(0);
        policy.setModifyTime(new Date());
        //插入策略并带回id
        mapper.insertSelective(policy);

        //继承父类策略，并根据当前策略id设置path
        this.copyPolicyFileFromParentpolicy(policy, parentPolicy);

        //更新新建策略的path
        mapper.updateByPrimaryKeySelective(policy);

        //继承父类策略的风险类型
        this.extendsPotentialRiskFromParentpolicy(policy.getId(), parentPolicy.getId());
    }

    /**
     * 继承父类策略，将父类策略的json文件拷贝到当前策略目录下
     * @param policy
     * @param parentPolicy
     * @throws IOException
     */
    @Override
    public void copyPolicyFileFromParentpolicy(PolicyDO policy, PolicyDO parentPolicy) throws IOException {
        //复制一份策略
        //获取父类策略的数据库路径
        String parentPolicyPath = parentPolicy.getPath();
        //获取父类策略的绝对路径
        //parentPolicyPath = servletContext.getRealPath(parentPolicyPath);
        //获取父类策略文件
        File parentPolicyFile = new File(PathConfig.HOM_PATH + parentPolicyPath);


        //获取新建策略的绝对路径
        String policyDirRealPath = PathConfig.HOM_PATH + PathConfig.POLICY_BASECATALOG + "/" + policy.getId();//servletContext.getRealPath(PathConfig.POLICY_BASECATALOG + "/" + policy.getId());

        //创建存放新建策略的文件夹
        File policyDir = new File(policyDirRealPath);
        if (!policyDir.exists()) {
            policyDir.mkdirs();
        }

        //获取并设置新建策略的相对路径
        policy.setPath(PathConfig.POLICY_BASECATALOG + "/" + policy.getId() + "/" + PathConfig.POLICY_JSONFILENAME);

        //将父类策略文件复制到新建策略的文件夹中
        File policyFile = new File(policyDirRealPath + "/" + PathConfig.POLICY_JSONFILENAME);
        FileUtils.copyFile(parentPolicyFile, policyFile);
    }

    /**
     * 读取策略JSON文件，将文件内容转换为JSON对象,以JSONOBJECT的形式返回
     * @param policy
     * @return
     */
    @Override
    public JSONObject readPolicyJsonFileById(PolicyDO policy) throws IOException {
        //ServletContext servletContext = SysContext.getRequest().getSession().getServletContext();
        JSONObject jsonObject = new JSONObject();

        String policyPath = policy.getPath();
        if (policyPath != null && !"".equals(policyPath)) {
            File jsonFile = new File(PathConfig.HOM_PATH + policyPath);
            String fileContentStr = FileUtils.readFileToString(jsonFile, "UTF-8");
            jsonObject = JSONObject.fromObject(fileContentStr);
        }

        return jsonObject;
    }

    /**
     * 根据策略id删除策略，并将使用该策略的用户策略设置为默认，（默认为1）。
     * @param policyId
     */
    @Override
    @Transactional
    public void deletePolicyById(Integer policyId) {

        PolicyDO policy = this.getByPrimaryKey(policyId);

        //根据策略id删除策略
        mapper.deleteByPrimaryKey(policyId);

        //将使用该策略的用户策略设置为默认1
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andPolicyidEqualTo(policyId);
        ClientUserDO clientUser = new ClientUserDO();
        clientUser.setPolicyid(1);
        clientUserDOMapper.updateByExampleSelective(clientUser, example);
        //将使用该策略的部门切换成默认策略
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oldPolicy", policyId);
        params.put("newPolicy", 1);
        departmentDOMapper.updateDepartmentPolicyByPolicyId(params);

        //删除策略关联的策略风险
        PolicyPotentialRiskDOCriteria riskExample = new PolicyPotentialRiskDOCriteria();
        riskExample.createCriteria().andPolicyIdEqualTo(policyId);
        riskDOMapper.deleteByExample(riskExample);

        //删除对应的策略文件
        //ServletContext servletContext = SysContext.getRequest().getSession().getServletContext();
        String realPath = PathConfig.HOM_PATH + policy.getPath();//servletContext.getRealPath(policy.getPath());
        File jsonFile = new File(realPath);
        if (jsonFile.exists()) {
            if (!jsonFile.delete()) {
                throw new RuntimeException("策略文件无法删除！");
            }
        }
    }

    /**
     * 更新策略的Json文件内容
     * @param jsonObject
     */
    @Override
    @Transactional
    public void updatePolicyJsonFile(JSONObject jsonObject) throws IOException {
        //从JSONObject中获取策略id
        Integer policyid = (Integer)jsonObject.get("policyid");

        if (policyid != null) {

            //查询策略对象
            PolicyDO policy = mapper.selectByPrimaryKey(policyid);
            //获取策略文件目录和文件，确保存在。
            String path = policy.getPath();
            //ServletContext servletContext = SysContext.getRequest().getSession().getServletContext();
            //String realPath = servletContext.getRealPath(path);
            File jsonFile = new File(PathConfig.HOM_PATH + path);
            if (!jsonFile.exists()) {

                int index = path.lastIndexOf("\\");
                String catalogPath = path.substring(0, index);
                File catalog = new File(catalogPath);

                if (!catalog.exists()) {
                    catalog.mkdirs();
                }

                if (!jsonFile.createNewFile()) {
                    throw new RuntimeException("策略文件未成功创建！");
                }
            }

            //从JSONObject中获取策略文件的内容
            String content = jsonObject.getString("content");
            //将内容写入策略文件
//            FileUtils.write(jsonFile,content,"UTF-8");
            FileUpload.writeStrInFile(jsonFile, content);

            //根据id更新策略更新时间
            policy.setModifyTime(new Date());
            PolicyDOCriteria example = new PolicyDOCriteria();
            example.createCriteria().andIdEqualTo(policyid);
            mapper.updateByExampleSelective(policy, example);

            //校验策略是否潜在风险
            this.checkPolicyPotentialRisk(policyid, content);
        } else {
            throw new RuntimeException("未获取到策略id参数");
        }
    }

    /**
     * 统计策略潜在风险
     * @return 存放统计策略风险的结果
     */
    @Override
    public Map<Integer, Integer> countPolicyPotentialRisk() {
        Map<Integer, Integer> countMap = new HashMap<>();

        //统计该风险类型对应的用户数量，并放入结果集
        this.countPotentialRiskClientUser(ConstantsDto.RISK_OF_SCRNWATERMARK, countMap);
        this.countPotentialRiskClientUser(ConstantsDto.RISK_OF_FILEOUTCFG, countMap);
        this.countPotentialRiskClientUser(ConstantsDto.RISK_OF_FILEOPT, countMap);
//        this.countPotentialRiskClientUser(ConstantsDto.RISK_OF_APPRO, countMap);

        return countMap;
    }

    /**
     * 检查正在使用的策略中，是否应用了该审批流程。
     * @param policyList 策略集合
     * @param approveDefinitionId
     * @return 若存在则返回false，不存在返回true
     */
    public boolean checkApproveInUsing(List<PolicyDO> policyList, Integer approveDefinitionId) {
        boolean flag = policyList.stream()
                  .map(PolicyDO::getPath)
                  .noneMatch( (path) -> {
                      //获取json文件
                      String filePath = servletContext.getRealPath(path);
                      File jsonFile = new File(filePath);
                      if (jsonFile.exists()) {
                          String fileContentStr;
                          try {
                              //获取策略json对象
                              fileContentStr = FileUtils.readFileToString(jsonFile, "UTF-8");
                              JSONObject jsonObject = JSONObject.fromObject(fileContentStr);
                              //检查文件导出配置项，外部配置项打开且才验证
                              JSONObject optCfg = jsonObject.getJSONObject("sbfileopt");
                              Integer enableOpt = (Integer) optCfg.get("enable");
                              if (enableOpt.equals(1)) {
                                  String flowId = (String) optCfg.getJSONObject("content").get("flowid");
                                  if (flowId.equals(approveDefinitionId.toString())) {
                                      return true;
                                  }
                              }
                              //检查文件外发配置项，外部配置项打开且才验证
                              JSONObject outcfg = jsonObject.getJSONObject("sbfileoutcfg");

                              Integer  enableOut = (Integer) outcfg.get("enable");
                              if (enableOut.equals(1)) {
                                  String flowId = (String) outcfg.getJSONObject("content").get("flowid");
                                  if (flowId.equals(approveDefinitionId.toString())) {
                                      return true;
                                  }
                              }
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                      return false;
                  });

        return flag;
    }

    /**
     * 统计该风险类型对应的用户数量，并放入结果集
     * @param riskType 风险类型id
     * @param countMap 结果集
     */
    private void countPotentialRiskClientUser(Integer riskType, Map<Integer, Integer> countMap) {
        PolicyPotentialRiskDOCriteria riskExample = new PolicyPotentialRiskDOCriteria();
        //查询屏幕无水印的风险
        riskExample.createCriteria().andRiskIdEqualTo(riskType);
        //查询风险类型对应策略集合
        List<PolicyPotentialRiskDO> riskList = riskDOMapper.selectByExample(riskExample);
        //获取策略id集合
        List<Integer> policyIdList = new ArrayList<>();
        for (PolicyPotentialRiskDO risk : riskList) {
            policyIdList.add(risk.getPolicyId());
        }

        if (policyIdList != null && policyIdList.size() > 0) {
            //统计该风险类型对应的用户数量，并放入结果集
            int count = cClientUserDOMapper.countPotentialRiskClientUser(policyIdList);
            countMap.put(riskType, count);
        } else {
            countMap.put(riskType, 0);
        }
    }

    /**
     * 校验策略是否潜在风险
     * @param policyId 策略id
     * @param policyContent 策略内容
     */
    private void checkPolicyPotentialRisk(Integer policyId, String policyContent) {
        //将策略的内容转化为json对象
        JSONObject policyJson = JSONObject.fromObject(policyContent);

        //校验是否存在屏幕无水印的风险
        Object sbscrnwatermarkEnable = policyJson.getJSONObject("sbscrnwatermark").get("enable");
        //检查屏幕水印开关，关闭则存在风险
        if (ConstantsDto.POLICYOPTIONUNENABLE.equals(sbscrnwatermarkEnable)) {
            //处理策略风险
            this.dealWithPolicyPotentialRisk(ConstantsDto.RISK_OF_SCRNWATERMARK, policyId);
        } else {
            //移除该策略风险的关联
            this.removePolicyPotentialRisk(ConstantsDto.RISK_OF_SCRNWATERMARK, policyId);
        }

        //校验是否存在外发无水印的风险
        Object sbfileoutcfgEnable = policyJson.getJSONObject("sbfileoutcfg").get("enable");
        Object outScwatermarkEnable = policyJson.getJSONObject("sbfileoutcfg").getJSONObject("content").getJSONObject("scwatermark").get("enable");
        //如果文件外发开关打开，内部屏幕水印开关关闭，则存在风险
        if (ConstantsDto.POLICYOPTIONENABLE.equals(sbfileoutcfgEnable) && ConstantsDto.POLICYOPTIONUNENABLE.equals(outScwatermarkEnable)) {
            //处理策略风险
            this.dealWithPolicyPotentialRisk(ConstantsDto.RISK_OF_FILEOUTCFG, policyId);
        } else {
            //移除该策略风险的关联
            this.removePolicyPotentialRisk(ConstantsDto.RISK_OF_FILEOUTCFG, policyId);
        }

        //校验是否存在导出无审批，无加密的风险
        Object sbfileoptEnable = policyJson.getJSONObject("sbfileopt").get("enable");

        Object optFileoptEncryEnable = policyJson.getJSONObject("sbfileopt").getJSONObject("content").get("encry");
        Object optFileoptApproveEnable = policyJson.getJSONObject("sbfileopt").getJSONObject("content").get("approve");
        //如果文件导出开关打开，内部水印开关关闭，则存在风险
        if (ConstantsDto.POLICYOPTIONENABLE.equals(sbfileoptEnable) && ConstantsDto.POLICYOPTIONUNENABLE.equals(optFileoptEncryEnable) && ConstantsDto.POLICYOPTIONUNENABLE.equals(optFileoptApproveEnable)) {
            //处理策略风险
            this.dealWithPolicyPotentialRisk(ConstantsDto.RISK_OF_FILEOPT, policyId);
        } else {
            //移除该策略风险的关联
            this.removePolicyPotentialRisk(ConstantsDto.RISK_OF_FILEOPT, policyId);
        }

        //校验是否存在无审批的风险
        Object outcfgApproEnable = policyJson.getJSONObject("sbfileoutcfg").getJSONObject("content").get("mode");
        Object optApproEnable = policyJson.getJSONObject("sbfileopt").getJSONObject("content").get("mode");
        //如果文件外发或者文件导出任何一个开关打开，其中的审批流程开关关闭，则存在风险.其中，审批流程开关:关闭为1，开启2，使用常量中的CONST_TRUE代替来判断
        if ((ConstantsDto.POLICYOPTIONENABLE.equals(sbfileoutcfgEnable) && ConstantsDto.CONST_TRUE.equals(outcfgApproEnable)) || (ConstantsDto.POLICYOPTIONENABLE.equals(sbfileoptEnable) && ConstantsDto.CONST_TRUE.equals(optApproEnable))) {
            //处理策略风险
            this.dealWithPolicyPotentialRisk(ConstantsDto.RISK_OF_APPRO, policyId);
        } else {
            //移除该策略风险的关联
            this.removePolicyPotentialRisk(ConstantsDto.RISK_OF_APPRO, policyId);
        }
    }

    /**
     * 移除该策略风险的关联
     * @param riskType 风险类型id
     * @param policyId
     */
    private void removePolicyPotentialRisk(Integer riskType, Integer policyId) {
        PolicyPotentialRiskDOCriteria riskExample = new PolicyPotentialRiskDOCriteria();
        riskExample.createCriteria().andRiskIdEqualTo(riskType).andPolicyIdEqualTo(policyId);
        riskDOMapper.deleteByExample(riskExample);
    }

    /**
     * 处理策略风险
     * @param riskType 风险类型id
     * @param policyId 策略id
     */
    private void dealWithPolicyPotentialRisk(Integer riskType, Integer policyId) {
        //存在风险，先校验数据库中是否已经存在该风险
        boolean isExists = this.checkRiskIsExists(riskType, policyId);
        if (!isExists) {
            //添加策略风险关联
            this.addPolicyPotentialRisk(riskType, policyId);
        }
    }

    /**
     * 校验数据库中是否已经存在该风险
     * @param riskType 风险类型id
     * @param policyId 策略id
     * @return
     */
    private boolean checkRiskIsExists(Integer riskType, Integer policyId) {
        PolicyPotentialRiskDOCriteria riskExample = new PolicyPotentialRiskDOCriteria();
        riskExample.createCriteria().andRiskIdEqualTo(riskType).andPolicyIdEqualTo(policyId);
        List<PolicyPotentialRiskDO> riskList = riskDOMapper.selectByExample(riskExample);
        if (riskList != null && riskList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加策略风险关联
     * @param riskType 风险类型id
     * @param policyId 策略id
     */
    private void addPolicyPotentialRisk(Integer riskType, Integer policyId) {
        PolicyPotentialRiskDO risk = new PolicyPotentialRiskDO();
        risk.setRiskId(riskType);
        risk.setPolicyId(policyId);
        riskDOMapper.insert(risk);
    }

    /**
     * 继承父类策略的风险类型
     * @param policyId
     * @param parentPolicyId
     */
    private void extendsPotentialRiskFromParentpolicy(Integer policyId, Integer parentPolicyId) {
        //查询父类的风险集合
        PolicyPotentialRiskDOCriteria riskExample = new PolicyPotentialRiskDOCriteria();
        riskExample.createCriteria().andPolicyIdEqualTo(parentPolicyId);
        List<PolicyPotentialRiskDO> riskList = riskDOMapper.selectByExample(riskExample);
        if (riskList != null && riskList.size() > 0) {
            for (PolicyPotentialRiskDO risk : riskList) {
                //将策略id替换为新策略id，并插入
                risk.setPolicyId(policyId);
                riskDOMapper.insert(risk);
            }
        }
    }

    @Override
    public void copyDefaultPolicy() {

    }
}
