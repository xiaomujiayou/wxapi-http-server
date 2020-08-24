package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class WaitLoginCliInfo {
    private String pId;
    private String tcpId;
    private String qrcode;
}
