package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.bo.client.GroupUserList;
import com.xm.wechat_robot.serialize.entity.WcWxGroupUserEntity;
import com.xm.wechat_robot.util.PageBean;

/**
 * 微信好友管理
 */
public interface WxGroupUserService {

    /**
     * 捕获一个微信好友
     * @param accountId
     * @param groupUserList
     * @return
     */
    public WcWxGroupUserEntity createOrUpdate(Integer accountId, GroupUserList groupUserList);

    /**
     * 删除好友（假删除）
     * @param accountId
     * @param wxid
     */
    public void delUser(Integer accountId, String groupWxid, String wxid);

    /**
     * 设置好友为检测状态
     * @param accountId
     */
    public void setWaitCheck(Integer accountId);

    /**
     * 清除无效用户（假删除）
     * @param accountId
     */
    public void delInvalidUser(Integer accountId);

    /**
     * 获取
     * @return
     */
    public PageBean<WcWxGroupUserEntity> getGroupUserList(Integer accountId, String groupWxid, Integer pageNum, Integer pageSize);

}
