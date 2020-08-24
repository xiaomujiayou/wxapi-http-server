package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.entity.WcWxAccountEntity;

import java.util.List;

/**
 * 微信账号登录
 */
public interface WxAccountService {

    /**
     * 登录微信账号
     * @param machineId
     * @param wxid
     * @param name
     * @param headImg
     * @return
     */
    public WcWxAccountEntity login(Integer machineId, String wxid, String name, String headImg, String tcpId, String pId);

    /**
     * 退出账号
     * @param machineId
     * @param wxid
     */
    public void logout(Integer machineId, String wxid);

    /**
     *
     * @param machineId
     * @param accountId
     */
    public void logout(Integer machineId, Integer accountId);

    /**
     * 退出所有
     * @param machineId
     */
    public void logoutAll(Integer machineId);

    /**
     * 获取accountid
     * @param machineId
     * @param tcpId
     */
    public WcWxAccountEntity getAccountId(Integer machineId, String tcpId);

    /**
     * 获取
     * @param machineId
     * @param accountWxid
     * @return
     */
    public Integer getAccountIdByWxid(Integer machineId, String accountWxid);

    /**
     * 判断账号是否登录
     * @param machineId
     * @param wxid
     * @return
     */
    public WcWxAccountEntity getWxAccount(Integer machineId, String wxid);

    /**
     * 获取客户机登录的微信账号
     * @param machineId
     * @param state
     * @return
     */
    public List<WcWxAccountEntity> getAllList(Integer machineId, Integer state);

    /**
     * 客户机重连
     * @param onloginWxids
     */
    public void reConnect(Integer machineId,String onloginWxids);
}
