package com.xm.wechat_robot.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;

@Slf4j
public class WsUtil {
    public static void sendMsg(Session session, Object msg) throws IOException {
        if(!session.isOpen()){
            log.error("session 已关闭 消息发送失败！");
            return;
        }
        session.getBasicRemote().sendText(JSON.toJSONString(msg));
    }
    public static void sendMsgAsync(Session session,Object msg){
        if(!session.isOpen()){
            log.error("session 已关闭 消息发送失败！");
            return;
        }
        session.getAsyncRemote().sendText(JSON.toJSONString(msg));
    }
}
