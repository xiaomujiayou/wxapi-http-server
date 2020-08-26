package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.wechat_robot.controller.ClientWss;
import com.xm.wechat_robot.mapper.WcWxAccountMapper;
import com.xm.wechat_robot.mapper.WcWxLoginQrcodeMapper;
import com.xm.wechat_robot.serialize.bo.server.CloseClient;
import com.xm.wechat_robot.serialize.bo.server.SendMsg;
import com.xm.wechat_robot.serialize.bo.server.StartClient;
import com.xm.wechat_robot.serialize.bo.server.WxServerMsg;
import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;
import com.xm.wechat_robot.serialize.entity.WcWxAccountEntity;
import com.xm.wechat_robot.serialize.entity.WcWxLoginQrcodeEntity;
import com.xm.wechat_robot.service.WxAccountService;
import com.xm.wechat_robot.service.WxApiService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("wxApiService")
public class WxApiServiceImpl implements WxApiService {

    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WcWxAccountMapper wcWxAccountMapper;
    @Autowired
    private WcWxLoginQrcodeMapper wcWxLoginQrcodeMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;

    /**
     * 启动一个微信
     *
     * @param machineCode
     */
    @Cacheable(value = "wxApiService.startClient",key = "#machineCode")     //接口限频 3秒/次
    @Override
    public void startClient(String machineCode) {
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName() + "::" + machineCode);
        LockUtil.lock(lock,()->{
            WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
            entity.setMachineId(getMachineId(machineCode).getId());
            entity = wcWxLoginQrcodeMapper.selectOne(entity);
            if (entity != null && entity.getState() == 1)
                return;
            String space = "start_client";
            StartClient startClient = new StartClient();
            sendServerMsg(machineCode, null, space, startClient);
        });
    }

    @Override
    public void closeClient(String machineCode, String clientId) {
        Integer machineId = getMachineId(machineCode).getId();
        WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
        entity.setMachineId(machineId);
        entity.setTcpId(clientId);
        if (wcWxLoginQrcodeMapper.selectCount(entity) <= 0) {
            WcWxAccountEntity accountEntity = new WcWxAccountEntity();
            accountEntity.setMachineId(machineId);
            accountEntity.setTcpId(clientId);
            if (wcWxAccountMapper.selectCount(accountEntity) <= 0)
                throw new GlobleException(MsgEnum.DATA_INVALID_ERROR, "clientId 无效");
        }
        String space = "close_client";
        CloseClient closeClient = new CloseClient();
        closeClient.setTcpId(clientId);
        sendServerMsg(machineCode, null, space, closeClient);
    }

    @Override
    public void sendTextMsg(String machineCode, String accountWxid, String toUserWxid, String msgStr, String atWxid) {
        String space = "send_text_msg";
        SendMsg msg = new SendMsg();
        msg.setToUserWxid(toUserWxid);
        msg.setAtWxid(atWxid);
        msg.setTextMsg(msgStr);
        sendServerMsg(machineCode, accountWxid, space, msg);
    }

    @Override
    public void sendAppMsg(String machineCode, String accountWxid, String toUserWxid, String msgStr) {
        String space = "send_app_msg";
        SendMsg msg = new SendMsg();
        msg.setToUserWxid(toUserWxid);
        msg.setAppMsgXml(msgStr);
        sendServerMsg(machineCode, accountWxid, space, msg);
    }

    @Override
    public void sendImgMsg(String machineCode, String accountWxid, String toUserWxid, String imgData) {
        String space = "send_img_msg";
        SendMsg msg = new SendMsg();
        msg.setToUserWxid(toUserWxid);
        msg.setImgData(imgData);
        sendServerMsg(machineCode, accountWxid, space, msg);
    }

    private WcMachineCodeEntity getMachineId(String machineCode) {
        Session session = ClientWss.MACHINE_SESSION_STORE.get(machineCode);
        if (session == null)
            throw new GlobleException(MsgEnum.AUTH_CHILD_NOT_LOGIN, "客户端未连接或网络中断");
        WcMachineCodeEntity machineCodeEntity = ClientWss.SESSION_MACHINE_STORE.get(session.getId());
        return machineCodeEntity;
    }

    private void sendServerMsg(String machineCode, String accountWxid, String space, Object msg) {
        Session session = ClientWss.MACHINE_SESSION_STORE.get(machineCode);
        if (session == null)
            throw new GlobleException(MsgEnum.AUTH_CHILD_NOT_LOGIN, "客户端未连接或网络中断");
        if (!session.isOpen())
            throw new GlobleException(MsgEnum.NET_DISCONNECT_ERROR, "与客户端失去连接");
        WcMachineCodeEntity machineCodeEntity = ClientWss.SESSION_MACHINE_STORE.get(session.getId());
        WxServerMsg wxServerMsg = new WxServerMsg();
        if (StrUtil.isNotBlank(accountWxid)) {
            WcWxAccountEntity accountEntity = wxAccountService.getWxAccount(machineCodeEntity.getId(), accountWxid);
            if (accountEntity == null || accountEntity.getState() != 1)
                throw new GlobleException(MsgEnum.AUTH_CHILD_NOT_LOGIN, String.format("微信账号 %s 未登录", accountWxid));
            wxServerMsg.setAccountWxid(accountEntity.getWxid());
            wxServerMsg.setPId(accountEntity.getPId());
            wxServerMsg.setTcpId(accountEntity.getTcpId());
        }
        wxServerMsg.setSpace(space);
        wxServerMsg.setMsg(msg);
        session.getAsyncRemote().sendText(JSON.toJSONString(wxServerMsg));
    }
}
