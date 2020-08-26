package com.xm.wechat_robot.constance;

/**
 * 微信好友类型
 */
public enum  UserTypeEnum {
    USER(1),
    GROUP(2),
    PUBLIC(3),
    OTHER(4);

    UserTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public static UserTypeEnum praseTypeByWxid(String wxid){
        if(wxid.startsWith("gh_")){
            return UserTypeEnum.PUBLIC;
        }else if(wxid.endsWith("@chatroom")){
            return UserTypeEnum.GROUP;
        }else {
            return UserTypeEnum.USER;
        }
    }
}
