package com.xm.wechat_robot.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxApiUserMsgListForm extends PageForm {

    /**
     * 机器码
     * @mock 303b4699-191a-422f-9bbe-399e8124fb70
     */
    @NotBlank(message = "machineCode 不能为空")
    private String machineCode;

    /**
     * 所属微信wxid
     * @mock wxid_95zo7g057d6z22
     */
    @NotBlank(message = "accountWxid 不能为空")
    private String accountWxid;

    /**
     * 要获取的群wxid
     * @mock 22566385278@chatroom
     */
    private String groupWxid;

    /**
     * 聊天用户微信id
     * @mock wxid_95zo7g057d6z22
     */
    private String targetUserWxid;

    /**
     * 消息来源(0:对方->我,1:我->对方)
     * @mock 0
     */
    private Integer msgFrom;

}
