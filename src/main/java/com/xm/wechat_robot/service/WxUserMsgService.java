package com.xm.wechat_robot.service;


import com.xm.wechat_robot.serialize.bo.client.UserMsg;
import com.xm.wechat_robot.serialize.bo.client.WxClientMsg;
import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;
import com.xm.wechat_robot.serialize.entity.WcWxAccountEntity;
import com.xm.wechat_robot.serialize.entity.WcWxMsgEntity;
import com.xm.wechat_robot.serialize.vo.WcWxMsgVo;
import com.xm.wechat_robot.util.PageBean;

public interface WxUserMsgService {

    /**
     * 收到一条用户消息
     * @param accountWxid
     * @param userMsg
     */
    public void receiveMsg(String accountWxid, WxClientMsg<UserMsg> userMsg);

    /**
     * 微信消息推送、回调
     */
    public void callBack(Integer accountId,WxClientMsg<UserMsg> userMsg);

    /**
     * 获取消息
     */
    public PageBean<WcWxMsgEntity> getUserMsg(String accountWxid, String groupWxid, String targetUserWxid,Integer msgFrom,Integer pageNum,Integer pageSize);

    public WcWxMsgVo entityToVo(WcMachineCodeEntity machineCodeEntity,WcWxAccountEntity accountEntity, WcWxMsgEntity wcWxMsgEntity);
}
