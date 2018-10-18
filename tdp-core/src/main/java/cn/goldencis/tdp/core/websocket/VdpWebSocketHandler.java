package cn.goldencis.tdp.core.websocket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by limingchao on 2018/2/1.
 */
@Component(value = "vdpWebSocketHandler")
public class VdpWebSocketHandler extends TextWebSocketHandler {

    private static CopyOnWriteArraySet<WebSocketSession> sessionSet = new CopyOnWriteArraySet<>();

    private WebSocketSession session;

    private Map<String, IVdpWebSocketOnMessageService> onMessageServiceMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            sessionSet.add(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            sessionSet.remove(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage message) throws Exception {
        synchronized (this) {
            this.session = session;
            Object result = null;
            //将参数转化为json
            JSONObject jsonMsg = JSONObject.parseObject((String) message.getPayload());
            //根据service名称，获取对应的处理消息的service
            IVdpWebSocketOnMessageService onMessageService = onMessageServiceMap.get(jsonMsg.get("service"));
            if (onMessageService != null) {
                //如果service存在，通过反射，调用方法传参，获取结果
                result = this.invokeWebSocketServiceMethod(onMessageService, jsonMsg.get("invoke"), jsonMsg.get("convert"), jsonMsg.get("params"));
            }

            //如果方法有返回值，则通过消息返回
            if (result != null) {
                WebSocketMessage<?> resultMessage = new TextMessage(result.toString());
                session.sendMessage(resultMessage);
            }
            this.session = null;
        }
    }

    private Object invokeWebSocketServiceMethod(IVdpWebSocketOnMessageService onMessageService, Object invoke, Object convert, Object params) {

        try {

            //获取处理消息Service的Class对象
            Class<? extends IVdpWebSocketOnMessageService> messageServiceClass = onMessageService.getClass();
            //获取其全部方法集合
            Method[] declaredMethods = messageServiceClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                //判断方法名称和调用名称是否一致
                if (method.getName().equals(invoke)) {
                    Object result = null;
                    if (Boolean.valueOf((boolean) convert)) {
                        //参数转化
                        Object[] paramsArr = onMessageService.convertParams(invoke, params);
                        //方法调用，将原service对象传入，参数传入
                        result = method.invoke(onMessageService, paramsArr);
                    } else {
                        //方法调用，将原service对象传入，参数传入
                        result = method.invoke(onMessageService);
                    }
                    return result;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    //接收文本消息，并发送出去
    /*@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //chatTextMessageHandler(message.getPayload());

        super.handleTextMessage(session, message);
    }*/

    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }

        sessionSet.remove(session);
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession session : sessionSet) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送给某个账户一个在线使用者
     * @param userGuid
     * @param message
     */
    public void sendMessageToUser(String userGuid, TextMessage message) {
        for (WebSocketSession session : sessionSet) {
            //比对session中的userguid，一样则发送
            if (userGuid.equalsIgnoreCase(session.getAttributes().get("userGuid").toString())) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //发送后中断
                break;
            }
        }
    }

    /**
     * 发送给某个账户所有在线的使用中
     * @param userGuid
     * @param message
     */
    public void sendMessageToUserOnline(String userGuid, TextMessage message) {
        for (WebSocketSession session : sessionSet) {
            //比对session中的userguid，一样则发送
            if (userGuid.equalsIgnoreCase(session.getAttributes().get("userGuid").toString())) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageToUsers(List<String> userGuidList, TextMessage message) {
        if (userGuidList != null && userGuidList.size() > 0) {
            for (String userGuid : userGuidList) {
                for (WebSocketSession session : sessionSet) {
                    //比对session中的userguid，一样则发送
                    if (userGuid.equals(session.getAttributes().get("userGuid").toString())) {
                        try {
                            if (session.isOpen()) {
                                session.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String getCurrentUserGuid() {
        return this.session.getAttributes().get("userGuid").toString();
    }

    public Map<String, IVdpWebSocketOnMessageService> getOnMessageServiceMap() {
        return onMessageServiceMap;
    }

    public void setOnMessageServiceMap(Map<String, IVdpWebSocketOnMessageService> onMessageServiceMap) {
        this.onMessageServiceMap = onMessageServiceMap;
    }

}
