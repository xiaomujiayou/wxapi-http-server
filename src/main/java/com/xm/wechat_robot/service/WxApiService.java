package com.xm.wechat_robot.service;

public interface WxApiService {

    /**
     * 打开微信
     */
    public void startClient(String machineCode);

    /**
     * 关闭微信
     */
    public void closeClient(String machineCode, String clientId);

    /**
     * 发送文本消息
     * @param accountWxid
     * @param msg
     */
    public void sendTextMsg(String machineCode, String accountWxid, String toUserWxid, String msg, String atWxid);

    /**
     * 发送小程序消息
     * @param machineCode
     * @param accountWxid
     * @param toUserWxid
     * @param msg
     */
    public void sendAppMsg(String machineCode, String accountWxid, String toUserWxid, String msg);

    /**
     * 发送图片消息
     * @param accountWxid
     */
    public void sendImgMsg(String machineCode, String accountWxid, String toUserWxid, String imgData);
}
