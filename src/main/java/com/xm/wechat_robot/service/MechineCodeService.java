package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;

import java.util.List;

public interface MechineCodeService {

    /**
     * 创建机器码
     * @param userId
     * @param expire    :毫秒
     * @return
     */
    public WcMachineCodeEntity create(Integer userId, Integer expire);

    /**
     * 查询
     * @param machineCode
     * @return
     */
    public WcMachineCodeEntity saveOrUpdate(String machineCode);

    /**
     * 删除（假）
     * @param userId
     * @param machineId
     */
    public void del(Integer userId, String machineId);

    /**
     * 机器码超时，失效
     * @param userId
     * @param machineId
     */
    public void expire(Integer userId, String machineId);

    /**
     * 设置消息回调连接
     * @param machineId
     * @param msgCallbackUrl
     */
    public void setMsgCallBackUrl(Integer machineId,String msgCallbackUrl);

    /**
     * 通过Code获取实体
     * @param machineCode
     * @return
     */
    WcMachineCodeEntity getByMachineCode(String machineCode);
}
