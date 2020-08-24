package com.xm.wechat_robot.service;


import com.xm.wechat_robot.constance.ClientMsgTypeEnum;
import com.xm.wechat_robot.serialize.bo.client.WxClientMsg;

public interface WxClientMsgService {

    /**
     * 处理客户端消息
     * @param type
     * @param msg
     */
    public void processMsg(ClientMsgTypeEnum type, WxClientMsg msg);
}
