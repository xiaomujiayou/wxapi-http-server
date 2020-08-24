package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class GroupInfo {
    private String groupAdmin;
    private String groupWxid;
    private String groupUserList; //“,”号间隔
}
