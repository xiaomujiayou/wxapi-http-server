package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class WxClientMsg<T> {
    //子链接ID
    private Integer machineId;
    private String tcpId;
    private String pId;
    private String space;
    private String name;
    private T value;
}
