package com.xm.wechat_robot.config;

@Deprecated
public class WxMsgMqConfig {
    public static final String EXCHANGE = "wx";
    //微信消息
    public static final String KEY_WX_MSG = "msg";
    public static final String QUEUE_WX_MSG = EXCHANGE  + "." + KEY_WX_MSG + ".queue";
    public static final String KEY_WX_MSG_FAIL = "msg.fail";
    public static final String QUEUE_WX_MSG_FAIL = EXCHANGE  + "." + KEY_WX_MSG_FAIL + ".queue";
}
