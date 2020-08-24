package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class GroupUserList {
    private String groupWxid;
    private String wxid;
    private String name;        //昵称
    private String groupName;   //群昵称
    private String headImg;

}
