package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.*;
import cn.goldencis.tdp.approve.entity.*;
import cn.goldencis.tdp.approve.service.IApproveFlowService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.UserDO;
import cn.goldencis.tdp.core.utils.GetLoginUser;
import cn.goldencis.tdp.core.websocket.IVdpWebSocketOnMessageService;
import cn.goldencis.tdp.core.websocket.VdpWebSocketHandler;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/16.
 */
@Service
public class ApproveFlowServiceImpl extends AbstractBaseServiceImpl<ApproveFlow, ApproveFlowCriteria> implements IApproveFlowService, IVdpWebSocketOnMessageService {

    @Autowired
    private ApproveFlowMapper mapper;

    @Autowired
    private CApproveMapper cMapper;

    @Autowired
    private ApproveDefinitionMapper definitionMapper;

    @Autowired
    private ApproveDetailMapper detailMapper;

    @Autowired
    private ApproveFlowInfoMapper infoMapper;

    @Autowired
    private VdpWebSocketHandler webSocketHandler;

    @Override
    protected BaseDao<ApproveFlow, ApproveFlowCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    public void init() {
        webSocketHandler.getOnMessageServiceMap().put("approveFlow", this);
    }

    /**
     * 发起审批流程
     *
     * @param approveFlow
     * @param modelList
     */
    @Override
    @Transactional
    public void submitApproveFlow(ApproveFlow approveFlow, List<ApproveModel> modelList) {

        ApproveDefinition approveDefinition = definitionMapper.selectByPrimaryKey(approveFlow.getFlowId());

        //补全流程属性，流程状态为0，进行中，流程环节id为第一个环节id
        approveFlow.setStatus(0);
        //modelList是已经完成排序的模型，直接将模型的第一个环节的id放入
        approveFlow.setPointId(modelList.get(0).getId());
        //设置流程名称
        approveFlow.setName(approveDefinition.getName());
        approveFlow.setApplyTime(new Date());

        //添加流程，回传流程id，即flowId，作为流程细节的flowId
        mapper.insertSelective(approveFlow);

        //根据流程模型，创建该流程的细节步骤
        ApproveDetail detail;
        List<ApproveDetail> detailList = new ArrayList<>();
        for (ApproveModel model : modelList) {
            //每条细节记录，只记载一个审批人的审批细节，所以当一个环节有多个审批人时，会有多条审批细节与之对应
            String[] approverArr = model.getApprovers().split(";");
            for (String approver : approverArr) {
                detail = new ApproveDetail();
                //审批细节的flowId，指的是本次提交的流程id
                detail.setFlowId(approveFlow.getId());
                //审批细节的名字，根据模型中环节的名字
                detail.setName(model.getName());
                //审批细节的pointId，根据模型中环节的id
                detail.setPointId(model.getId());
                //审批细节的seniorId，根据模型中环节的seniorId
                detail.setSeniorId(model.getSeniorId());
                //审批细节是否为标准环节，根据模型中环节是否为标准环节
                detail.setStandard(model.getStandard());
                //设置该细节步骤的审批人
                detail.setApprover(approver);
                //设置时间
                detail.setModifyTime(new Date());

                detailList.add(detail);
            }
        }

        //批量添加流程细节
        cMapper.batchInsertApproveDetailList(detailList);
    }

    /**
     * 获取审批流程。分页查询。
     *
     * @param start
     * @param length
     * @param submitDate
     * @param applicantOrType
     */
    @Override
    public List<ApproveFlow> getApproveFlowPageInPages(int start, int length, String submitDate, String applicantOrType) {
        RowBounds rowBounds = new RowBounds(start, length);
        ApproveFlowCriteria example = new ApproveFlowCriteria();
        ApproveFlowCriteria.Criteria criteria = example.createCriteria();

        //添加审批人活着类型条件
        if (!StringUtil.isEmpty(applicantOrType)) {
            criteria.andApplicantNameLike("%" + applicantOrType + "%");
            //ApproveFlowCriteria.Criteria applicantCriteria = example.createCriteria().andApplicantNameLike(applicantOrType);
            //ApproveFlowCriteria.Criteria typeCriteria = example.or();
        }

        //添加时间条件
        if (!StringUtil.isEmpty(submitDate)) {
            Date startDate = DateUtil.strToDate(submitDate, "yyyy-MM-dd");
            Date endDate = DateUtil.getNextDay(startDate);
            criteria.andApplyTimeBetween(startDate, endDate);
        }

        List<ApproveFlow> approveFlows = mapper.selectByExampleWithRowbounds(example, rowBounds);
        return approveFlows;
    }

    /**
     * 获取审批流程总数
     *
     * @param submitDate
     * @param applicantOrType
     */
    @Override
    public int countApproveFlowInPages(String submitDate, String applicantOrType) {
        ApproveFlowCriteria example = new ApproveFlowCriteria();
        ApproveFlowCriteria.Criteria criteria = example.createCriteria();

        //添加审批人活着类型条件
        if (!StringUtil.isEmpty(applicantOrType)) {
            criteria.andApplicantNameLike("%" + applicantOrType + "%");
        }

        //添加时间条件
        if (!StringUtil.isEmpty(submitDate)) {
            Date startDate = DateUtil.strToDate(submitDate, "yyyy-MM-dd");
            Date endDate = DateUtil.getNextDay(startDate);
            criteria.andApplyTimeBetween(startDate, endDate);
        }

        long count = mapper.countByExample(example);
        return (int) count;
    }

    /**
     * 当前环节审批通过，进入下一环节，如果没有下一个环节，审批流程通过
     *
     * @param detail
     * @return
     */
    @Override
    @Transactional
    public ApproveFlow adoptCurrentPoint(ApproveDetail detail) {
        //查询下一个环节的细节列表
        ApproveDetailCriteria detailExample = new ApproveDetailCriteria();
        detailExample.createCriteria().andFlowIdEqualTo(detail.getFlowId()).andSeniorIdEqualTo(detail.getPointId());
        List<ApproveDetail> detailList = detailMapper.selectByExample(detailExample);

        ApproveFlow approveFlow = mapper.selectByPrimaryKey(detail.getFlowId());

        //存在后续环节，流程调到下一个环节，流程状态不变，依然为审批中
        if (detailList != null && detailList.size() > 0) {
            approveFlow.setPointId(detailList.get(0).getPointId());
        } else {
            //当前环节为最后一个环节，审批流程通过
            approveFlow.setStatus(ConstantsDto.APPROVE_FLOW_STATUS_ADOPT);
            approveFlow.setFinishTime(new Date());
        }
        //更新流程记录
        mapper.updateByPrimaryKey(approveFlow);
        return approveFlow;
    }

    /**
     * 拒绝审批请求，终止审批流程
     *
     * @param detail
     */
    @Override
    public ApproveFlow refuseApproveFlow(ApproveDetail detail) {
        ApproveFlow approveFlow = mapper.selectByPrimaryKey(detail.getFlowId());
        //设置流程状态
        approveFlow.setStatus(ConstantsDto.APPROVE_FLOW_STATUS_REFUSE);
        approveFlow.setFinishTime(new Date());
        //更新流程记录
        mapper.updateByPrimaryKey(approveFlow);
        return approveFlow;
    }

    /**
     * 根据查询条件，获取审批流程。分页查询
     *
     * @param start
     * @param length
     * @param status
     * @param needOnly
     * @param timeMap
     * @param applicantOrType
     * @return
     */
    @Override
    public List<ApproveFlow> getApproveFlowPage(Map<String, Object> params) {
        //添加审批人条件
        String userGuid = null;
        List<String> only = (List<String>) params.get("needOnly");
        if (only != null && only.size() == 1) {
            if (only != null && "1".equals(only.get(0))) {
                userGuid = GetLoginUser.getLoginUser().getGuid();
            }
        }
        params.put("userGuid", userGuid);
        List<ApproveFlow> approveFlowInPage = cMapper.getApproveFlowPage(params);
        for (ApproveFlow approveFlow : approveFlowInPage) {
            if (approveFlow.getSeniorId() == 0) {
                approveFlow.setModifyTime(null);
            } else {
                ApproveDetailCriteria criteria = new ApproveDetailCriteria();
                criteria.createCriteria().andPointIdEqualTo(approveFlow.getSeniorId()).andFlowIdEqualTo(approveFlow.getId());
                List<ApproveDetail> details = detailMapper.selectByExample(criteria);
                approveFlow.setModifyTime(details.get(0).getModifyTime());
            }

        }

        return approveFlowInPage;
    }

    /**
     * 根据查询条件，获取审批流程。分页查询
     *
     * @param start
     * @param length
     * @param status
     * @param timeMap
     * @param uuid
     * @return
     */
    @Override
    public List<ApproveFlow> getApproveFlowPageClient(int start, int length, Integer status, Map<String, Date> timeMap, String uuid) {
        //添加审批人或者类型条件

        Date startDate = null;
        Date endDate = null;
        if (timeMap != null && timeMap.size() > 1) {
            //添加时间条件
            startDate = timeMap.get("startDateTime");
            endDate = timeMap.get("endDateTime");
        }

        //添加审批人条件
        String userGuid = null;

        List<ApproveFlow> approveFlowInPage = cMapper.getApproveFlowPageClient(start, length, status, userGuid, startDate, endDate, uuid);
        return approveFlowInPage;
    }

    /**
     * 根据查询条件，获取审批流程的数量
     *
     * @param status
     * @param needOnly
     * @param timeMap
     * @param applicantOrType
     * @return
     */
    @Override
    public int countApproveFlowPage(Map<String, Object> params) {


        //添加审批人条件
        String userGuid = null;
        List<String> only = (List<String>) params.get("needOnly");
        if (only != null && only.size() == 1) {
            if (only != null && "1".equals(only.get(0))) {
                userGuid = GetLoginUser.getLoginUser().getGuid();
            }
        }
        params.put("userGuid", userGuid);
        long count = cMapper.countApproveFlowPage(params);
        return (int) count;
    }

    /**
     * 根据查询条件，获取审批流程的数量
     *
     * @param status
     * @param timeMap
     * @return
     */
    @Override
    public int countApproveFlowPageClient(Integer status, Map<String, Date> timeMap, String uuid) {
        Date startDate = null;
        Date endDate = null;
        if (timeMap != null && timeMap.size() > 1) {
            //添加时间条件
            startDate = timeMap.get("startDateTime");
            endDate = timeMap.get("endDateTime");
        }

        //添加审批人条件
        String userGuid = null;


        long count = cMapper.countApproveFlowPageClient(status, userGuid, startDate, endDate, uuid);
        return (int) count;
    }

    /**
     * 根据流程id，获取流程详细信息
     *
     * @param approveFlowId
     * @return
     */
    @Override
    public ApproveFlow getApproveFlowInfoById(Integer approveFlowId) {
        ApproveFlow approveFlow = mapper.selectByPrimaryKey(approveFlowId);
        return approveFlow;
    }

    /**
     * 根据是否需要当前登录账户审批
     *
     * @param approveFlows
     */
    @Override
    public void isNeedApprove(List<ApproveFlow> approveFlows) {
        UserDO user = GetLoginUser.getLoginUser();
        for (ApproveFlow approveFlow : approveFlows) {
            if (approveFlow.getApprovers() != null) {
                String[] approverArr = approveFlow.getApprovers().split(",");
                for (String approver : approverArr) {
                    if (user.getGuid().equals(approver)) {
                        ApproveDetailCriteria criteria = new ApproveDetailCriteria();
                        criteria.createCriteria().andApproverEqualTo(approver).andFlowIdEqualTo(approveFlow.getId());
                        List<ApproveDetail> details = detailMapper.selectByExample(criteria);
                        if (details.get(0).getResult() != null) {
                        }else{
                            approveFlow.setChecked(true);
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteApproveFlow(String approveFlowStr, String ctp) {
        String realPath;
        File file;

        List<Integer> flowIdList = new ArrayList<>();
        String[] approveFlowArrStr = approveFlowStr.split(",");
        for (String approveFlowIdStr : approveFlowArrStr) {
            flowIdList.add(Integer.parseInt(approveFlowIdStr));
        }

        //删除审批流程对应细节步骤
        ApproveDetailCriteria detailExample = new ApproveDetailCriteria();
        detailExample.createCriteria().andFlowIdIn(flowIdList);
        detailMapper.deleteByExample(detailExample);

        //删除审批流程的详细信息
        ApproveFlowInfoCriteria infoExample = new ApproveFlowInfoCriteria();
        infoExample.createCriteria().andFlowIdIn(flowIdList);
        //首先查询等待删除的审批细节集合，删除其中路径下存在的文件
        List<ApproveFlowInfo> approveFlowInfoList = infoMapper.selectByExample(infoExample);
        for (ApproveFlowInfo approveFlowInfo : approveFlowInfoList) {
            //判断日志文件是否存在，存在则删除
            realPath = ctp + approveFlowInfo.getFilePath();
            file = new File(realPath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("删除审批摘要文件失败！");
                }
            }
        }
        infoMapper.deleteByExample(infoExample);

        //删除审批流程记录
        ApproveFlowCriteria example = new ApproveFlowCriteria();
        example.createCriteria().andIdIn(flowIdList);
        mapper.deleteByExample(example);
    }

    /**
     * 通知审批流程当前环节的审批人，参数为null时，通知当前登录账户
     *
     * @param approveFlow
     */
    @Override
    public void noticeApprover(ApproveFlow approveFlow) {
        if (approveFlow != null) {
            //根据审批流程的id和当前审批环节的id，获取当前审批环节的审批细节集合
            ApproveDetailCriteria example = new ApproveDetailCriteria();
            example.createCriteria().andFlowIdEqualTo(approveFlow.getId()).andPointIdEqualTo(approveFlow.getPointId());
            List<ApproveDetail> noticeDetailList = detailMapper.selectByExample(example);

            for (ApproveDetail detail : noticeDetailList) {
                String userGuid = detail.getApprover();
                //按照guid统计对应账户还有多少流程正在审批中
                Long count = cMapper.countNeedToApprove(userGuid);
                JSONObject res = new JSONObject();
                res.put("count", count);
                //设置socket消息类型
                res.put("messageType", "approveCount");
                //调用websocket，给账户发送信息
                TextMessage message = new TextMessage(res.toString());
                webSocketHandler.sendMessageToUserOnline(userGuid, message);
            }
        }
    }

    /**
     * 通知当前登录账户，更新待审批数量
     *
     * @param userGuid
     */
    @Override
    public void noticeApproverByGuid(String userGuid) {
        if (userGuid == null) {
            //如果传参的guid为null，则从webSocketHandler获取账户的guid
            userGuid = webSocketHandler.getCurrentUserGuid();
        }
        //按照guid统计对应账户还有多少流程正在审批中
        Long count = cMapper.countNeedToApprove(userGuid);

        JSONObject res = new JSONObject();
        res.put("count", count);
        //设置socket消息类型
        res.put("messageType", "approveCount");
        //调用websocket，给账户发送信息
        TextMessage message = new TextMessage(res.toString());
        webSocketHandler.sendMessageToUserOnline(userGuid, message);
    }

    @Override
    public long countApproveByState(Integer status) {

        String userGuid = null;
        if (status.intValue() == 0) {
            userGuid = GetLoginUser.getLoginUser().getGuid();
        }
        long count = cMapper.countApproveByState(userGuid, status);
        return count;
    }

    @Override
    public Object[] convertParams(Object invoke, Object params) {
        Object[] objs = null;
        if ("noticeApproverByGuid".equals(invoke)) {
            objs = new Object[1];
            //获取guid的参数
            String userGuid = webSocketHandler.getCurrentUserGuid();
            objs[0] = userGuid;
        }

        return objs;
    }
}
