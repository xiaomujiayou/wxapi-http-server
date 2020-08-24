package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class UserList extends UserInfo {
    private String mark;
    private String sign;
    private String country;
    private String province;
    private String city;
    private Integer sex;
    private Integer type;
    private String groupAdmin;
    private String groupWxids;
    private Integer groupCount;
}


