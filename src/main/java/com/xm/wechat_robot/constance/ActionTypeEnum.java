package com.xm.wechat_robot.constance;

import com.xm.wechat_robot.serialize.bo.action.UserMsgActionMsg;

/**
 * 微信客户端消息动态
 */
public enum  ActionTypeEnum {
    ON_USER_MSG("on_user_msg","当收到消息时", UserMsgActionMsg.class);

    private String type;
    private String des;
    private Class bodyClz;

    ActionTypeEnum(String type, String des, Class bodyClz) {
        this.type = type;
        this.des = des;
        this.bodyClz = bodyClz;
    }

    public String getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    public Class getBodyClz() {
        return bodyClz;
    }
}
