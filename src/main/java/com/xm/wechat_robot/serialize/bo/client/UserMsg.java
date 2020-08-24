package com.xm.wechat_robot.serialize.bo.client;

import lombok.Data;

@Data
public class UserMsg {
    private String type;
    private Integer chatRoom;
    private String chatRoomWxid;
    private String targetUserWxid;
    private Integer msgFrom;
    private MsgInfo msgInfo;
}
