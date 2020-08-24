package com.xm.wechat_robot.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxApiSendTextMsgForm extends WxApiSendMsgForm {

    /**
     * 文本消息
     * @mock hello
     */
    @NotBlank(message = "msg 不能为空！")
    private String msg;

    /**
     * At用户wxid
     * @mock wxid_95zo7g057d6z22
     */
    private String atWxid;
}
