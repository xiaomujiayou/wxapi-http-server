package com.xm.wechat_robot.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxApiSendMsgForm extends BaseForm {

    /**
     * 机器码
     * @mock 303b4699-191a-422f-9bbe-399e8124fb70
     */
    @NotBlank(message = "machineCode 不能为空！")
    private String machineCode;

    /**
     * 所属微信wxid
     * @mock wxid_95zo7g057d6z22
     */
    @NotBlank(message = "accountWxid 不能为空！")
    private String accountWxid;

    /**
     * 收信人wxid
     * @mock wxid_95zo7g057d6z22
     */
    @NotBlank(message = "toUserWxid 不能为空！")
    private String toUserWxid;

}
