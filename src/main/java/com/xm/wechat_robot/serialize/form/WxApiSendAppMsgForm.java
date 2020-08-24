package com.xm.wechat_robot.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxApiSendAppMsgForm extends WxApiSendMsgForm {

    /**
     * 小程序消息原始xml(可复制接收到的小程序消息)
     * @mock <msg>...</msg>
     */
    @NotBlank(message = "appMsgXml 不能为空！")
    private String appMsgXml;
}
