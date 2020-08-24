package com.xm.wechat_robot.message;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.xm.wechat_robot.constance.ClientMsgTypeEnum;
import com.xm.wechat_robot.serialize.bo.client.WxClientMsg;
import com.xm.wechat_robot.service.WxClientMsgService;
import com.xm.wechat_robot.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 本地队列，取代RabbitMq
 */
@Slf4j
@Component
public class ClientMsgQueue implements Runnable{

    @Autowired
    private WxClientMsgService wxClientMsgService;

    //客户端消息队列
    public static final LinkedBlockingQueue<JSONObject> CLIENT_MSG_QUEUE = new LinkedBlockingQueue<>();


    @Override
    public void run() {

        while (true){
            JSONObject clientMsg = null;
            try {
                clientMsg = CLIENT_MSG_QUEUE.take();
                ClientMsgTypeEnum clientMsgType = convertMsgType(clientMsg);
                WxClientMsg msg = clientMsg.toJavaObject(WxClientMsg.class);
                msg.setValue(clientMsg.getObject("value", clientMsgType.getType()));
                wxClientMsgService.processMsg(clientMsgType, msg);
            }catch (EnumConstantNotPresentException e){
                log.warn(e.getMessage());
            }catch (Exception e) {
                log.error("消息：{} 设备号：{} 处理失败 error：{}", clientMsg.toJSONString(), clientMsg.getString("machineId"), e);
            }
        }
    }

    public static ClientMsgTypeEnum convertMsgType(JSONObject message) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String space = message.getString("space");
        String name = message.getString("name");
        return EnumUtils.getEnum(
                ClientMsgTypeEnum.class,
                MapUtil.builder(new HashMap<String,Object>())
                        .put("space",space)
                        .put("name",name).build());
    }
}
