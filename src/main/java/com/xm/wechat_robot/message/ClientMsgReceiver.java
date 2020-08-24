package com.xm.wechat_robot.message;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xm.wechat_robot.config.ClientMsgMqConfig;
import com.xm.wechat_robot.config.WxMsgMqConfig;
import com.xm.wechat_robot.constance.ClientMsgTypeEnum;
import com.xm.wechat_robot.constance.RabbitMqConstant;
import com.xm.wechat_robot.serialize.bo.client.WxClientMsg;
import com.xm.wechat_robot.service.WxClientMsgService;
import com.xm.wechat_robot.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Deprecated
@Slf4j
//@Component
public class ClientMsgReceiver {

    @Autowired
    private WxClientMsgService wxClientMsgService;

    /**
     * 微信支付回调
     * @param wxClientMsg
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(ClientMsgMqConfig.EXCHANGE),
            key = ClientMsgMqConfig.KEY_CLIENT_MSG,
            value = @Queue(value = ClientMsgMqConfig.QUEUE_CLIENT_MSG,
                    arguments = {
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME,value = ClientMsgMqConfig.EXCHANGE),
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,value = ClientMsgMqConfig.KEY_CLIENT_MSG_FAIL)
                    })
    ))
    public void onNotifyMessage(JSONObject wxClientMsg, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try {
            ClientMsgTypeEnum clientMsgType = convertMsgType(wxClientMsg);
            WxClientMsg msg = wxClientMsg.toJavaObject(WxClientMsg.class);
            msg.setValue(wxClientMsg.getObject("value", clientMsgType.getType()));
            wxClientMsgService.processMsg(clientMsgType, msg);
        }catch (EnumConstantNotPresentException e){
            log.warn(e.getMessage());
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 设备号：{} 处理失败 error：{}",msgId,wxClientMsg.getString("machineId"),e);
        } finally {
            channel.basicAck(msgId,false);
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
