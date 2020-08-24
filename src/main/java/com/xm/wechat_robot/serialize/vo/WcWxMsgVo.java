package com.xm.wechat_robot.serialize.vo;

import lombok.Data;

@Data
public class WcWxMsgVo {

    /**
     * 机器码
     * @mock 303b4699-191a-422f-9bbe-399e8124fb70
     */
    private String machineCode;

    /**
     * 微信客户端ID
     * @mock 1019
     */
    private String clientId;

    /**
     * 消息所属微信号wxid
     * @mock wxid_95zo7g057d6z22
     */
    private String accountWxid;

    /**
     * 是否为群发消息
     * @mock false
     */
    private Boolean isGroupMsg;

    /**
     * 所属群wxid
     * @mock 21861977728@chatroom
     */
    private String groupWxid;

    /**
     * 聊天用户wxid
     * @mock wxid_95zo7g057d6z22
     */
    private String targetUserWxid;

    /**
     * 消息类型 16进制（具体含义可以百度）
     * @mock 2F
     */
    private String msgType;

    /**
     * At的用户wxid列表“,”号间隔
     * @mock wxid_9b9f4jowb10722,wxid_9b9f4jowb10721
     */
    private String atUserListWxids;

    /**
     * 原始消息体base64编码
     * @mock
     */
    private String msgBody;
}
