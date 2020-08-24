package com.xm.wechat_robot.serialize.bo.server;

import lombok.Data;

@Data
public class WxServerMsg<T> {
    private String tcpId;
    private String pId;
    private String accountWxid;
    private String space;
    private T msg;
}
