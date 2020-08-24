package com.xm.wechat_robot.controller;

import com.xm.wechat_robot.serialize.form.*;
import com.xm.wechat_robot.service.WxApiService;
import com.xm.wechat_robot.util.Msg;
import com.xm.wechat_robot.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 命令API
 */
@RestController
@RequestMapping("/api")
public class WxApiOrderController {

    @Autowired
    private WxApiService wxApiService;

    /**
     * 启动一个微信客户端
     * @apiNote 等待扫码的微信客户端只会存在一个，不存在则打开，存在直接返回。
     */
    @PostMapping("/startClient")
    public Msg<Object> startClient(@Valid @RequestBody WxApiStartClientMsgForm wxApiStartClientMsgForm, BindingResult bindingResult){
        wxApiService.startClient(wxApiStartClientMsgForm.getMachineCode());
        return R.sucess();
    }

    /**
     * 关闭一个微信客户端
     * @param wxApiCloseClientMsgForm
     * @return
     */
    @PostMapping("/closeClient")
    public Msg<Object> closeClient(@Valid @RequestBody WxApiCloseClientMsgForm wxApiCloseClientMsgForm, BindingResult bindingResult){
        wxApiService.closeClient(wxApiCloseClientMsgForm.getMachineCode(),wxApiCloseClientMsgForm.getClientId());
        return R.sucess();
    }

    /**
     * 发送文本消息接口
     * @param wxApiSendTextMsgForm
     * @return
     */
    @PostMapping("/sendTextMsg")
    public Msg<Object> sendTextMsg(@Valid @RequestBody WxApiSendTextMsgForm wxApiSendTextMsgForm, BindingResult bindingResult){
        wxApiService.sendTextMsg(
                wxApiSendTextMsgForm.getMachineCode(),
                wxApiSendTextMsgForm.getAccountWxid(),
                wxApiSendTextMsgForm.getToUserWxid(),
                wxApiSendTextMsgForm.getMsg(),
                wxApiSendTextMsgForm.getAtWxid());
        return R.sucess();
    }

    /**
     * 发送图片消息接口
     * @param wxApiSendImgMsgForm
     * @return
     */
    @PostMapping("/sendImgMsg")
    public Msg<Object> sendImgMsg(@Valid @RequestBody WxApiSendImgMsgForm wxApiSendImgMsgForm, BindingResult bindingResult){
        wxApiService.sendImgMsg(
                wxApiSendImgMsgForm.getMachineCode(),
                wxApiSendImgMsgForm.getAccountWxid(),
                wxApiSendImgMsgForm.getToUserWxid(),
                wxApiSendImgMsgForm.getImgData());
        return R.sucess();
    }

    /**
     * 发送小程序消息接口
     * @param wxApiSendAppMsgForm
     * @return
     */
    @PostMapping("/sendAppMsg")
    public Msg<Object> sendAppMsg(@Valid @RequestBody WxApiSendAppMsgForm wxApiSendAppMsgForm, BindingResult bindingResult){
        wxApiService.sendAppMsg(
                wxApiSendAppMsgForm.getMachineCode(),
                wxApiSendAppMsgForm.getAccountWxid(),
                wxApiSendAppMsgForm.getToUserWxid(),
                wxApiSendAppMsgForm.getAppMsgXml());
        return R.sucess();
    }
}
