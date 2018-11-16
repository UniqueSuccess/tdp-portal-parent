package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ApproveFlowInfo;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfoCriteria;
import cn.goldencis.tdp.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by limingchao on 2018/1/20.
 */
public interface IApproveFlowInfoService extends BaseService<ApproveFlowInfo, ApproveFlowInfoCriteria> {

    /**
     * 插入审批流程详细信息
     *
     * @param flowInfo
     */
    void addapproveFlowInfo(ApproveFlowInfo flowInfo);

    /**
     * 根据流程id获取流程信息
     *
     * @param approveFlowInfoId
     * @return
     */
    ApproveFlowInfo getApproveFlowInfoByFlowId(Integer approveFlowInfoId);

    /**
     * 保存文件，并在流程信息中添加路径
     *
     * @param flowInfo
     * @param approvfile
     */
    void uploadApproveFile(ApproveFlowInfo flowInfo, MultipartFile approvfile);
}
