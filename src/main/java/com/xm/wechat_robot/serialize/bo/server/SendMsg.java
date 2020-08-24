package com.xm.wechat_robot.serialize.bo.server;

import lombok.Data;

/**
 * 发送微信消息
 */
@Data
public class SendMsg {
    private String toUserWxid;
    private String atWxid;
    private String textMsg;
    private String imgData; //base64编码
    private String appMsgXml; //小程序消息 xml base64编码
}
