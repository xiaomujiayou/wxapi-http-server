package com.xm.wechat_robot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.wechat_robot.config.ClientMsgMqConfig;
import com.xm.wechat_robot.config.WxMsgMqConfig;
import com.xm.wechat_robot.message.ClientMsgQueue;
import com.xm.wechat_robot.message.WxMsgQueue;
import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;
import com.xm.wechat_robot.service.MechineCodeService;
import com.xm.wechat_robot.service.WxAccountService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.service.WxQrcodeService;
import com.xm.wechat_robot.util.MsgEnum;
import com.xm.wechat_robot.util.R;
import com.xm.wechat_robot.util.WsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 采集消息
 */
@Slf4j
@Component
@ServerEndpoint("/client/{machineCode}")
public class CollectWss {

    private static MechineCodeService mechineCodeService;
    //private static RabbitTemplate rabbitTemplate;
    private static WxAccountService wxAccountService;
    private static WxQrcodeService wxQrcodeService;

    @Autowired
    public void setMechineIdService(MechineCodeService mechineCodeService){ CollectWss.mechineCodeService = mechineCodeService; }
    //@Autowired
    //private void setRabbitTemplate(RabbitTemplate rabbitTemplate){
    //    CollectWss.rabbitTemplate = rabbitTemplate;
    //}
    @Autowired
    private void setWxAccountService(WxAccountService wxAccountService){
        CollectWss.wxAccountService = wxAccountService;
    }
    @Autowired
    private void setWxQrcodeService(WxQrcodeService wxQrcodeService){
        CollectWss.wxQrcodeService = wxQrcodeService;
    }

    //会话中心
    public static final Map<String,Session> MACHINE_SESSION_STORE = new ConcurrentHashMap();
    public static final Map<String, WcMachineCodeEntity> SESSION_MACHINE_STORE = new ConcurrentHashMap();

    @OnOpen
    public void onOpen(@PathParam("machineCode") String machineCode, Session session){
        System.out.println(machineCode);
        //校验machine
        if(StrUtil.isBlank(machineCode))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        machineCode = machineCode.toUpperCase();
        WcMachineCodeEntity code = mechineCodeService.saveOrUpdate(machineCode);

        MACHINE_SESSION_STORE.put(code.getMachineCode(),session);
        SESSION_MACHINE_STORE.put(session.getId(),code);
    }
    @OnClose
    public void onClose(Session session) throws IOException {
        WcMachineCodeEntity machineCodeEntity =  SESSION_MACHINE_STORE.get(session.getId());
        wxAccountService.logoutAll(machineCodeEntity.getId());
        wxQrcodeService.invalidAll(machineCodeEntity.getId());
        if(machineCodeEntity != null){
            MACHINE_SESSION_STORE.remove(SESSION_MACHINE_STORE.get(session.getId()).getMachineCode());
            SESSION_MACHINE_STORE.remove(session.getId());
        }
    }
    @OnMessage
    public void onMessage(Session session,String message) throws InterruptedException {
        log.debug("收到客户端消息：{}",message);
        JSONObject jsonMsg = JSON.parseObject(message);
        jsonMsg.put("machineId",SESSION_MACHINE_STORE.get(session.getId()).getId());
        if(jsonMsg.getString("space").equals("user_msg")){
        //    rabbitTemplate.convertAndSend(WxMsgMqConfig.EXCHANGE,WxMsgMqConfig.KEY_WX_MSG,jsonMsg);
            WxMsgQueue.WX_MSG_QUEUE.put(jsonMsg);
        }
        //rabbitTemplate.convertAndSend(ClientMsgMqConfig.EXCHANGE,ClientMsgMqConfig.KEY_CLIENT_MSG,jsonMsg);
        ClientMsgQueue.CLIENT_MSG_QUEUE.put(jsonMsg);
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
     * 从WS会话中获取code，减少数据库压力
     * @param machineCode
     * @param notEmpty      :是否必须存在,“是”不存在则抛异常，“否”不存在返回NULL
     * @param mustOnline    :必须在线
     * @return
     */
    public static WcMachineCodeEntity getValidMachineCode(String machineCode,Boolean notEmpty,Boolean mustOnline){
        Session session = MACHINE_SESSION_STORE.get(machineCode);
        if(session == null){
            if(mustOnline && notEmpty)
                throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"机器码错误或尚未连接!");
            WcMachineCodeEntity entity = mechineCodeService.getByMachineCode(machineCode);
            if(entity == null && notEmpty)
                throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"机器码错误或尚未连接!");
            if(entity != null)
                return entity;
            return null;
        }
        return SESSION_MACHINE_STORE.get(session.getId());
    }
}
