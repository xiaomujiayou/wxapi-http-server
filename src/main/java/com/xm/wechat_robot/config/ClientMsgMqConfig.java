package com.xm.wechat_robot.config;

@Deprecated
public class ClientMsgMqConfig {
    public static final String EXCHANGE = "client";

    //客户端消息
    public static final String KEY_CLIENT_MSG = "msg";
    public static final String QUEUE_CLIENT_MSG = EXCHANGE  + "." + KEY_CLIENT_MSG + ".queue";
    public static final String KEY_CLIENT_MSG_FAIL = "msg.fail";
    public static final String QUEUE_CLIENT_MSG_FAIL = EXCHANGE  + "." + KEY_CLIENT_MSG_FAIL + ".queue";
}
