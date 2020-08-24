package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.bo.client.UserList;
import com.xm.wechat_robot.serialize.entity.WcWxUserEntity;
import com.xm.wechat_robot.util.PageBean;

/**
 * 微信好友管理
 */
public interface WxUserService {

    /**
     * 捕获一个微信好友
     * @param accountId
     * @param userList
     * @return
     */
    public WcWxUserEntity createOrUpdate(Integer accountId, UserList userList);

    /**
     * 删除好友（假删除）
     * @param accountId
     * @param wxid
     */
    public void delUser(Integer accountId, String wxid);

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
    public PageBean<WcWxUserEntity> getAccountUserList(Integer machineId, String accountWxid, Integer type, Integer pageNum, Integer pageSize);

}
