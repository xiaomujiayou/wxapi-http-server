package com.xm.wechat_robot.serialize.bo.action;

import lombok.Data;

/**
 * 客户端动态消息
 */
@Data
public class ActionMsg<T> {
    private String machineCode;
    private String clientId;
    private String type;
    private T action;
}
