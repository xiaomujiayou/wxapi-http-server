package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.entity.WcWxLoginQrcodeEntity;

public interface WxQrcodeService {

    /**
     * 生成一个登录二维码
     * @param machineId
     * @param pid
     * @param tcpId
     * @param qrcode
     */
    public void genQrcode(Integer machineId, String pid, String tcpId, String qrcode);

    /**
     * 使二维码无效（已登录，关闭）
     * @param machineId
     * @param pid
     * @param tcpId
     */
    public void invalid(Integer machineId, String pid, String tcpId);

    /**
     * 使二维码全部无效
     * @param machineId
     */
    public void invalidAll(Integer machineId);

    /**
     * 获取有效的登录二维码
     * @param machineId
     * @return
     */
    public WcWxLoginQrcodeEntity getValidQrcode(Integer machineId);
}
