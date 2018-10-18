package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.entity.ApproveDetail;
import cn.goldencis.tdp.approve.entity.ApproveFlow;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfo;
import cn.goldencis.tdp.common.mqclient.MQClient;
import net.sf.json.JSONObject;

/**
 * Created by limingchao on 2018/1/21.
 */
public class ApproveResultPublish4ClientRunnable implements Runnable {

    private ApproveFlow flow;

    private ApproveDetail detail;

    private ApproveFlowInfo flowInfo;

    private MQClient publisher;

    @Override
    public void run() {
        String clientUserIdsStr = flow.getApplicantId();

        //设置消息为审批消息
        String message = "bdpapprove";

        //设置type为持久消息
        int type = MQClient.MSG_SERIALIZABLE;

        //组装消息内容
        JSONObject contentJson = new JSONObject();
        contentJson.put("tranunique", flow.getTranUnique());
        //状态参数转换为客户端的状态码
        Integer status = flow.getStatus();
        if (status.equals(-1)) {
            status = 1;
        } else if (status.equals(1)) {
            status = 2;
        }
        contentJson.put("reason", detail.getRemark());
        contentJson.put("state", status);
        contentJson.put("type", flow.getType());
        contentJson.put("content", flowInfo.getPolicyParam());
        String content = contentJson.toString();

        //使用消息缓存服务
        publisher.clientNotify(clientUserIdsStr ,message, content, type);
    }

    public ApproveFlow getFlow() {
        return flow;
    }

    public void setFlow(ApproveFlow flow) {
        this.flow = flow;
    }

    public ApproveFlowInfo getFlowInfo() {
        return flowInfo;
    }

    public void setFlowInfo(ApproveFlowInfo flowInfo) {
        this.flowInfo = flowInfo;
    }

    public ApproveDetail getDetail() {
        return detail;
    }

    public void setDetail(ApproveDetail detail) {
        this.detail = detail;
    }

    public MQClient getPublisher() {
        return publisher;
    }

    public void setPublisher(MQClient publisher) {
        this.publisher = publisher;
    }
}
