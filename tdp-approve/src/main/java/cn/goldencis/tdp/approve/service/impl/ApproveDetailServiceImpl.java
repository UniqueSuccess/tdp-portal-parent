package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.ApproveDetailMapper;
import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveDetailCriteria;
import cn.goldencis.tdp.approve.service.IApproveDetailService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.dao.UserDOMapper;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.entity.UserDOCriteria;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by limingchao on 2018/1/16.
 */
@Service
public class ApproveDetailServiceImpl extends AbstractBaseServiceImpl<ApproveDetail, ApproveDetailCriteria> implements IApproveDetailService {

    @Autowired
    private ApproveDetailMapper mapper;

    @Autowired
    private UserDOMapper userMapper;

    @Override
    protected BaseDao<ApproveDetail, ApproveDetailCriteria> getDao() {
        return mapper;
    }

    /**
     * 仅批准当前细节步骤
     * @param detail
     */
    @Override
    @Transactional
    public void dealWithCurrentDetail(ApproveDetail detail) {
        detail.setModifyTime(new Date());
        mapper.updateByPrimaryKey(detail);
    }

    /**
     * 根据流程id和环节id，查询所属所有细节列表
     * @param flowId
     * @param pointId
     * @return
     */
    @Override
    public List<ApproveDetail> getapproveDetailListCurrentPoint(Integer flowId, Integer pointId) {
        ApproveDetailCriteria example = new ApproveDetailCriteria();
        example.createCriteria().andFlowIdEqualTo(flowId).andPointIdEqualTo(pointId);
        List<ApproveDetail> detailList = mapper.selectByExample(example);
        return detailList;
    }

    /**
     * 根据id获取审批细节步骤
     * @param approveDetailId
     * @return
     */
    @Override
    public ApproveDetail getApproveDetailById(Integer approveDetailId) {
        return mapper.selectByPrimaryKey(approveDetailId);
    }

    /**
     * 根据流程id，获取该流程全部细节集合
     * @param approveFlowId
     * @return
     */
    @Override
    public List<ApproveDetail> getApproveDetailListByFlowId(Integer approveFlowId) {
        ApproveDetailCriteria example = new ApproveDetailCriteria();
        example.createCriteria().andFlowIdEqualTo(approveFlowId);
        List<ApproveDetail> detailList = mapper.selectByExample(example);
        return detailList;
    }

    /**
     * 将流程细节集合转化为有序的细节名称，以json数组的形式返回。
     * @param detailList
     * @return
     */
    @Override
    public JSONArray convertDetailListToLinkedNameJsonArray(List<ApproveDetail> detailList) {
        Set<String> nameSet = new LinkedHashSet<>();
        for (ApproveDetail detail : detailList) {
            nameSet.add(detail.getName());
        }

        JSONArray nameArr = new JSONArray();
        for (String name : nameSet) {
            JSONObject nameJson = new JSONObject();
            nameJson.put("name", name);
            nameArr.add(nameJson);
        }
        return nameArr;
    }

    /**
     * 将流程细节集合转化为模型
     * @param detailList
     * @return
     */
    @Override
    public List<ApproveDetail> convertDetailListToModel(List<ApproveDetail> detailList) {
        Map<Integer, ApproveDetail> detailMap = new HashMap<>();
        int startPoint = 0;

        //去除重复的SeniorId
        for (ApproveDetail detail : detailList) {
            if (!detailMap.containsKey(detail.getSeniorId())) {
                detailMap.put(detail.getSeniorId(), detail);
            }
        }

        //构建顺序模型
        detailList = new ArrayList<>();
        for (int i = 0; i < detailMap.size(); i++) {
            detailList.add(detailMap.get(startPoint));
            startPoint = detailMap.get(startPoint).getPointId();
        }

        return detailList;
    }

    /**
     * 根据流程id，获取该流程已经审批完成的细节集合
     * @param approveFlowId
     * @return
     */
    @Override
    public List<ApproveDetail> getApproveDetailDoneListByFlowId(Integer approveFlowId) {
        ApproveDetailCriteria example = new ApproveDetailCriteria();
        example.createCriteria().andFlowIdEqualTo(approveFlowId).andResultIsNotNull();
        List<ApproveDetail> detailList = mapper.selectByExample(example);
        return detailList;
    }

    /**
     * 将流程细节集合转化为有序列表
     * @param detailList
     * @return
     */
    @Override
    public List<ApproveDetail> convertToLinkedDetailList(List<ApproveDetail> detailList) {

        Map<Integer, List<ApproveDetail>> detailMap = new HashMap<>();
        int startPoint = 0;

        //按照SeniorId归类
        outloop: for (ApproveDetail detail : detailList) {
            //按照SeniorId创建子集
            if (!detailMap.containsKey(detail.getSeniorId())) {
                List<ApproveDetail> subList = new ArrayList<>();
                subList.add(detail);
                detailMap.put(detail.getSeniorId(), subList);
                continue;
            }

            //完成同SeniorId的子集的排序
            List<ApproveDetail> subList = detailMap.get(detail.getSeniorId());
            for (int i = 0; i < subList.size(); i++) {
                if (detail.getModifyTime().before(subList.get(i).getModifyTime())) {
                    subList.add(i, detail);
                    continue outloop;
                }
            }

            //该细节的修改时间最晚，直接添加在最后
            subList.add(detail);
        }

        //构建顺序模型
        detailList = new ArrayList<>();
        for (int i = 0; i < detailMap.size(); i++) {
            List<ApproveDetail> subList = detailMap.get(startPoint);
            for (ApproveDetail detail : subList) {
                detailList.add(detail);
            }
            startPoint = subList.get(0).getPointId();
        }

        return detailList;
    }

    /**
     * 获取当前账户待审批细节id
     * @param detailList
     * @param pointId
     * @return
     */
    @Override
    public Integer getDetailNeedToApproveId(List<ApproveDetail> detailList, Integer pointId) {
        String guid = GetLoginUser.getLoginUser().getGuid();
        for (ApproveDetail detail : detailList) {
            if (pointId.equals(detail.getPointId())) {
                if (guid.equals(detail.getApprover())) {
                    return detail.getId();
                }
            }
        }
        return null;
    }

    /**
     * 为流程细节设置审批人姓名
     * @param detailList
     */
    @Override
    public void fillWithApproverName(List<ApproveDetail> detailList) {
        if (detailList != null && detailList.size() > 0) {
            List<String> approverGuidList = new ArrayList<>();
            for (ApproveDetail detail : detailList) {
                approverGuidList.add(detail.getApprover());
            }

            UserDOCriteria userExample = new UserDOCriteria();
            userExample.createCriteria().andGuidIn(approverGuidList);
            List<UserDO> userList = userMapper.selectByExample(userExample);

            for (ApproveDetail detail : detailList) {
                for (UserDO user : userList) {
                    if (user.getGuid().equals(detail.getApprover())) {
                        detail.setApproverName(user.getName());
                        break;
                    }
                }
            }
        }
    }
}
