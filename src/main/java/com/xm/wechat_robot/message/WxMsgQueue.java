package com.xm.wechat_robot.message;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.xm.wechat_robot.constance.ClientMsgTypeEnum;
import com.xm.wechat_robot.serialize.bo.client.UserMsg;
import com.xm.wechat_robot.serialize.bo.client.WxClientMsg;
import com.xm.wechat_robot.service.WxAccountService;
import com.xm.wechat_robot.service.WxClientMsgService;
import com.xm.wechat_robot.service.WxUserMsgService;
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
public class WxMsgQueue implements Runnable{

    @Autowired
    private WxUserMsgService wxUserMsgService;
    @Autowired
    private WxAccountService wxAccountService;

    //客户端消息队列
    public static final LinkedBlockingQueue<JSONObject> WX_MSG_QUEUE = new LinkedBlockingQueue<>();


    @Override
    public void run() {

        while (true){
            JSONObject wxMsg = null;
            try {
                wxMsg = WX_MSG_QUEUE.take();
                WxClientMsg msg = wxMsg.toJavaObject(WxClientMsg.class);
                msg.setValue(wxMsg.getObject("value", UserMsg.class));
                Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();
                wxUserMsgService.callBack(accountId,msg);
            }catch (EnumConstantNotPresentException e){
                log.warn(e.getMessage());
            }catch (Exception e){
                log.error("消息：{} 设备号：{} 处理失败 error：{}",wxMsg.toJSONString(),wxMsg.getString("machineId"),e);
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
