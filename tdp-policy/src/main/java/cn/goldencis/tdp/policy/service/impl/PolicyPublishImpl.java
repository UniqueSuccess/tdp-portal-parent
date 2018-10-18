package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.mqclient.MQClient;
import cn.goldencis.tdp.policy.dao.ClientUserDOMapper;
import cn.goldencis.tdp.policy.dao.PolicyDOMapper;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.entity.ClientUserDOCriteria;
import cn.goldencis.tdp.policy.entity.PolicyDO;
import cn.goldencis.tdp.policy.service.IPolicyPublish;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 审批流程结束，通知申请流程的客户端用户
 * Created by limingchao on 2018/1/8.
 */
@Component("policyPublish")
public class PolicyPublishImpl implements IPolicyPublish {

    @Autowired
    private PolicyDOMapper policyMapper;

    @Autowired
    private ClientUserDOMapper clientUserMapper;

    private ThreadLocal<Integer> policyId = new ThreadLocal<>();

    private AtomicInteger initPolicyId = new AtomicInteger();

    private ThreadLocal<Integer> clientUserId = new ThreadLocal<>();

    private AtomicInteger initClientUserId = new AtomicInteger();

    @Autowired
    private MQClient publisher;

    @Override
    public void run() {
        //如果主线程对初始化参数赋值，且不为0，则用该值对线程policyId进行初始化
        if (initPolicyId.get() != 0) {
            policyId.set(initPolicyId.getAndSet(0));
        }
        //如果主线程对初始化参数赋值，且不为0，则用该值对线程clientUserId进行初始化
        if (initClientUserId.get() != 0) {
            clientUserId.set(initClientUserId.getAndSet(0));
        }

        //保证策略id存在
        if (policyId.get() != null) {

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //获取策略对象
            PolicyDO policy = policyMapper.selectByPrimaryKey(policyId.get());

            List<ClientUserDO> clientUserList;

            if (clientUserId.get() == null) {
                //获取跟该策略id关联的用户集合
                ClientUserDOCriteria example = new ClientUserDOCriteria();
                example.createCriteria().andPolicyidEqualTo(policyId.get());
                clientUserList = clientUserMapper.selectByExample(example);
            } else {
                //单个用户更新策略的情况，通知该用户客户端
                clientUserList = new ArrayList<>();
                ClientUserDO clientUser = clientUserMapper.selectByPrimaryKey(clientUserId.get());
                clientUserList.add(clientUser);
            }

            if (clientUserList != null && clientUserList.size() > 0) {
                StringBuffer sb = new StringBuffer();

                //转化为用户id命名的频道字符串，以";"分开
                for (ClientUserDO clientUser : clientUserList) {
                    //
                    sb.append(clientUser.getGuid() + ";");
                }
                //去掉最后一个";"，完成发送频道拼接
                String clientUserIdsStr = sb.substring(0, sb.length() - 1);

                //设置消息为策略消息
                String message = "bdppolicy";

                //设置type为及时消息
                int type = MQClient.MSG_REALTIME;

                //组装消息内容
                JSONObject contentJson = new JSONObject();
                contentJson.put("url", policy.getPath());
                contentJson.put("time", (policy.getModifyTime().getTime() + "").substring(0,10));
                String content = contentJson.toJSONString();

                //使用消息缓存服务
                publisher.clientNotify(clientUserIdsStr ,message, content, type);
            }
        }
    }

    public ThreadLocal<Integer> getPolicyId() {
        return policyId;
    }

    public void setPolicyId(ThreadLocal<Integer> policyId) {
        this.policyId = policyId;
    }

    public AtomicInteger getInitPolicyId() {
        return initPolicyId;
    }

    public void setInitPolicyId(AtomicInteger initPolicyId) {
        this.initPolicyId = initPolicyId;
    }

    public ThreadLocal<Integer> getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(ThreadLocal<Integer> clientUserId) {
        this.clientUserId = clientUserId;
    }

    public AtomicInteger getInitClientUserId() {
        return initClientUserId;
    }

    public void setInitClientUserId(AtomicInteger initClientUserId) {
        this.initClientUserId = initClientUserId;
    }
}
