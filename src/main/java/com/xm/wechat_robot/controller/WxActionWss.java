package com.xm.wechat_robot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.wechat_robot.constance.ActionTypeEnum;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.message.ClientMsgQueue;
import com.xm.wechat_robot.message.WxMsgQueue;
import com.xm.wechat_robot.serialize.bo.action.ActionMsg;
import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;
import com.xm.wechat_robot.service.MechineCodeService;
import com.xm.wechat_robot.service.WxAccountService;
import com.xm.wechat_robot.service.WxQrcodeService;
import com.xm.wechat_robot.util.MsgEnum;
import com.xm.wechat_robot.util.R;
import com.xm.wechat_robot.util.WsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反馈微信动态到客户端
 * websocket方式
 */
@Slf4j
@Component
@ServerEndpoint("/action/callback/{machineCode}")
public class WxActionWss {

    private static MechineCodeService mechineCodeService;
    //private static RabbitTemplate rabbitTemplate;
    private static WxAccountService wxAccountService;
    private static WxQrcodeService wxQrcodeService;

    @Autowired
    public void setMechineIdService(MechineCodeService mechineCodeService){ WxActionWss.mechineCodeService = mechineCodeService; }
    @Autowired
    private void setWxAccountService(WxAccountService wxAccountService){
        WxActionWss.wxAccountService = wxAccountService;
    }
    @Autowired
    private void setWxQrcodeService(WxQrcodeService wxQrcodeService){
        WxActionWss.wxQrcodeService = wxQrcodeService;
    }

    //会话中心
    public static final Map<String,Session> MACHINE_SESSION_STORE = new ConcurrentHashMap();
    public static final Map<String, String> SESSION_MACHINE_STORE = new ConcurrentHashMap();

    @OnOpen
    public void onOpen(@PathParam("machineCode") String machineCode, Session session){
        System.out.println(machineCode);
        //校验machine
        if(StrUtil.isBlank(machineCode))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        machineCode = machineCode.toUpperCase();
        MACHINE_SESSION_STORE.put(machineCode,session);
        SESSION_MACHINE_STORE.put(session.getId(),machineCode);
    }
    @OnClose
    public void onClose(Session session) throws IOException {
        String machineCode =  SESSION_MACHINE_STORE.get(session.getId());
        MACHINE_SESSION_STORE.remove(machineCode);
        SESSION_MACHINE_STORE.remove(session.getId());
    }
    @OnMessage
    public void onMessage(Session session,String message) throws InterruptedException {
        log.debug("收到客户端消息：{}",message);
    }
    @OnError
    public void onError(Session session,Throwable e) throws IOException {
        e.printStackTrace();
        if(e instanceof GlobleException)
            WsUtil.sendMsgAsync(session, R.error(((GlobleException) e).getMsgEnum()));
        if(session.isOpen())
            session.close();
    }

    /**
     * 发送微信消息动态
     * @param actionMsg
     * @return
     */
    public static void sendActionMsg(ActionMsg actionMsg){
        Session session = MACHINE_SESSION_STORE.get(actionMsg.getMachineCode());
        if (session == null || !session.isOpen())
            return;
        WsUtil.sendMsgAsync(session,actionMsg);
    }
}
