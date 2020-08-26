package com.xm.wechat_robot.constance;


import com.xm.wechat_robot.serialize.bo.client.*;

/**
 * 控制端消息类型
 */
public enum ClientMsgTypeEnum {
    QRCODE_CHANGE("login_qr_change","login_qr_code", LoginQrChange.class),
    ON_LOGIN("on_login","on_login", OnLogin.class),
    USER_INFO("user_info","account", UserInfo.class),
    USER_LIST("user_list","user_info", UserList.class),
    GROUP_USER_LIST("group_user_list","group_user_list", GroupUserList.class),
    GROUP_INFO("group_info","group_info", GroupInfo.class),
    USER_MSG("user_msg","msg", UserMsg.class),
    ON_LOGOUT("on_logout","on_logout", OnLogout.class),
    CLI_EXIT("cli_exit","client_close", OnLogout.class),
    CLI_ON_LOGIN_WXIDS("client","onLoginWxid", String.class),
    CLI_WAIT_LOGIN_INFO("client","waitLoginTcpIds", WaitLoginCliInfo.class),
    CLI_MSG_CALLBACK_URL("client","msgCallbackUrl", String.class),
    ;
    //命名空间
    private String space;
    //名称
    private String name;
    //值类型
    private Class type;

    ClientMsgTypeEnum(String space, String name, Class type) {
        this.space = space;
        this.name = name;
        this.type = type;
    }

    public String getSpace() {
        return space;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }
}
