package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xm.wechat_robot.constance.ClientMsgTypeEnum;
import com.xm.wechat_robot.constance.UserTypeEnum;
import com.xm.wechat_robot.serialize.bo.client.*;
import com.xm.wechat_robot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xm.wechat_robot.constance.ClientMsgTypeEnum.*;

@Service("wxClientMsgService")
public class WxClientMsgServiceImpl implements WxClientMsgService {

    @Autowired
    private MechineCodeService mechineCodeService;
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserMsgService wxUserMsgService;
    @Autowired
    private WxQrcodeService wxQrcodeService;
    @Autowired
    private WxGroupUserService wxGroupUserService;

    @Override
    public void processMsg(ClientMsgTypeEnum type, WxClientMsg msg) {

        if(type == QRCODE_CHANGE){
            WxClientMsg<LoginQrChange> qrMsg = msg;
            wxQrcodeService.genQrcode(msg.getMachineId(),msg.getPId(),msg.getTcpId(),qrMsg.getValue().getQrcode());
        }else if(type == ON_LOGIN){
            //登陆后二维码失效
            wxQrcodeService.invalid(msg.getMachineId(),msg.getPId(),msg.getTcpId());
        }else if(type == USER_INFO){
            WxClientMsg<UserInfo> lgMsg = msg;
            //登录一个微信账号
            wxAccountService.login(
                    lgMsg.getMachineId(),
                    lgMsg.getValue().getWxid(),
                    lgMsg.getValue().getName(),
                    lgMsg.getValue().getHeadImg(),
                    lgMsg.getTcpId(),
                    lgMsg.getPId());
        }else if(type == USER_LIST) {
            WxClientMsg<UserList> ulMsg = msg;
            //获取一个联系人信息
            Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();

            UserTypeEnum userType = UserTypeEnum.praseTypeByWxid(ulMsg.getValue().getWxid());
        //    if(userType == UserTypeEnum.GROUP)      //此处不处理群好友
        //        return;
            ulMsg.getValue().setType(userType.getType());
            wxUserService.createOrUpdate(accountId, ulMsg.getValue());
        }else if(type == GROUP_INFO){
            //完善群联系人信息
            WxClientMsg<GroupInfo> ulMsg = msg;
            Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();
            UserList userList = new UserList();
            userList.setType(UserTypeEnum.GROUP.getType());
            userList.setWxid(ulMsg.getValue().getGroupWxid());
            userList.setGroupWxids(ulMsg.getValue().getGroupUserList());
            userList.setGroupAdmin(ulMsg.getValue().getGroupAdmin());
            userList.setGroupCount(ulMsg.getValue().getGroupUserList().split(",").length);
            wxUserService.createOrUpdate(accountId, userList);
        }else if(type == GROUP_USER_LIST){
            WxClientMsg<GroupUserList> ulMsg = msg;
            //获取一个群成员详细信息
            Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();
            wxGroupUserService.createOrUpdate(accountId,ulMsg.getValue());
        }else if(type == USER_MSG){
            WxClientMsg<UserMsg> userMsg = msg;
            String accountWxid = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getWxid();
            wxUserMsgService.receiveMsg(accountWxid,userMsg);
        }else if(type == ON_LOGOUT){
            Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();
            //退出后二维码失效
            wxQrcodeService.invalid(msg.getMachineId(),msg.getPId(),msg.getTcpId());
            wxAccountService.logout(msg.getMachineId(),accountId);
            wxGroupUserService.setWaitCheck(accountId);
            wxUserService.setWaitCheck(accountId);
        }else if(type == CLI_EXIT){
            Integer accountId = wxAccountService.getAccountId(msg.getMachineId(), msg.getTcpId()).getId();
            if(accountId == null){
                //二维码界面状态 退出
                //微信关闭后二维码失效
                wxQrcodeService.invalid(msg.getMachineId(),msg.getPId(),msg.getTcpId());
            }else {
                //已登录状态下退出
                wxAccountService.logout(msg.getMachineId(),accountId);
                wxGroupUserService.setWaitCheck(accountId);
                wxUserService.setWaitCheck(accountId);
            }
        }else if(type == CLI_ON_LOGIN_WXIDS) {
            //服务器重连时存在已登录微信
            WxClientMsg<String> ulMsg = msg;
            String onloginWxids = ulMsg.getValue();
            if (StrUtil.isNotBlank(onloginWxids))
                wxAccountService.reConnect(ulMsg.getMachineId(), onloginWxids);
        }else if(type == CLI_WAIT_LOGIN_INFO){
            //服务器重连时存在等待登录的微信客户端
            WxClientMsg<WaitLoginCliInfo> ulMsg = msg;
            if(StrUtil.isNotBlank(ulMsg.getValue().getPId()))
                wxQrcodeService.genQrcode(
                        ulMsg.getMachineId(),
                        ulMsg.getValue().getPId(),
                        ulMsg.getValue().getTcpId(),
                        ulMsg.getValue().getQrcode());
        }else if(type == CLI_MSG_CALLBACK_URL){
            WxClientMsg<String> ulMsg = msg;
            mechineCodeService.setMsgCallBackUrl(ulMsg.getMachineId(),ulMsg.getValue());
        }
    }
}
