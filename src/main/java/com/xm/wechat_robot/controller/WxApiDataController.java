package com.xm.wechat_robot.controller;

import cn.hutool.core.util.ObjectUtil;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.mapper.WcMachineCodeMapper;
import com.xm.wechat_robot.mapper.WcWxAccountMapper;
import com.xm.wechat_robot.mapper.WcWxLoginQrcodeMapper;
import com.xm.wechat_robot.serialize.entity.*;
import com.xm.wechat_robot.serialize.form.*;
import com.xm.wechat_robot.serialize.vo.*;
import com.xm.wechat_robot.service.*;
import com.xm.wechat_robot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据API
 */
@RestController
@RequestMapping("/api")
public class WxApiDataController {

    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxGroupUserService wxGroupUserService;
    @Autowired
    private WcWxLoginQrcodeMapper wcWxLoginQrcodeMapper;
    @Autowired
    private WxUserMsgService wxUserMsgService;
    @Autowired
    private WcWxAccountMapper wcWxAccountMapper;

    /**
     * 获取登录二维码
     * @apiNote 使用前请先调用“启动微信客户端接口”
     * @param wxApiQrcodeForm
     * @return
     */
    @GetMapping("/login/qrcode")
    public Msg<WcWxQrcodeVo> getQrcode(@Valid WxApiQrcodeForm wxApiQrcodeForm, BindingResult bindingResult){
        WcMachineCodeEntity codeEntity = CollectWss.getValidMachineCode(wxApiQrcodeForm.getMachineCode(),true,false);
        WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
        entity.setMachineId(codeEntity.getId());
        entity.setState(1);
        entity = wcWxLoginQrcodeMapper.selectOne(entity);
        if(entity == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"请先使用启动微信接口");
        WcWxQrcodeVo wcWxQrcodeVo = new WcWxQrcodeVo();
        wcWxQrcodeVo.setQrCode(entity.getQrcode());
        wcWxQrcodeVo.setClientId(entity.getTcpId());
        return R.sucess(wcWxQrcodeVo);
    }

    /**
     * 获取客户端登录微信列表
     * @param wxApiAccountListForm
     * @return
     */
    @GetMapping("/account/list")
    public Msg<List<WcWxAccountVo>> accountList(@Valid WxApiAccountListForm wxApiAccountListForm, BindingResult bindingResult){
        WcMachineCodeEntity codeEntity = CollectWss.getValidMachineCode(wxApiAccountListForm.getMachineCode(),true,false);
        return R.sucess(wxAccountService.getAllList(codeEntity.getId(),null).stream().map(o -> {
            WcWxAccountVo vo = new WcWxAccountVo();
            vo.setWxid(o.getWxid());
            vo.setName(o.getName());
            vo.setHeadImg(o.getHeadImg());
            vo.setLastLogin(o.getLastLogin());
            vo.setClientId(o.getTcpId());
            vo.setCreateTime(o.getCreateTime());
            vo.setState(o.getState());
            return vo;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取登录微信用户列表
     * @param wxApiUserListForm
     * @return
     */
    @GetMapping("/user/list")
    public Msg<PageBean<WcWxUserVo>> userList(@Valid WxApiUserListForm wxApiUserListForm, BindingResult bindingResult){
        WcMachineCodeEntity codeEntity = CollectWss.getValidMachineCode(wxApiUserListForm.getMachineCode(),true,false);
        PageBean<WcWxUserEntity> pageBean = wxUserService.getAccountUserList(
                codeEntity.getId(),
                wxApiUserListForm.getAccountWxid(),
                wxApiUserListForm.getType(),
                wxApiUserListForm.getPageNum(),
                wxApiUserListForm.getPageSize());
        PageBean<WcWxUserVo> result = new PageBean<>(pageBean);
        result.setList( pageBean.getList().stream().map(o -> {
            WcWxUserVo vo = new WcWxUserVo();
            vo.setType(o.getType());
            vo.setWxid(o.getWxid());
            vo.setName(o.getName());
            vo.setHeadImg(o.getHeadImg());
            vo.setMark(o.getMark());
            vo.setGroupAdmin(o.getGroupAdmin());
            vo.setUserCount(o.getGroupCount());
            vo.setSex(o.getSex());
            vo.setSignStr(o.getSignStr());
            vo.setCountry(o.getCountry());
            vo.setProvince(o.getProvince());
            vo.setCity(o.getCity());
            vo.setWxAccountId(o.getWxAccountId());
            vo.setCreateTime(o.getCreateTime());
            return vo;
        }).collect(Collectors.toList()));
        return R.sucess(result);
    }

    /**
     * 获取微信群成员列表
     * @param wxApiGroupUserListForm
     * @return
     */
    @GetMapping("/group/user/list")
    public Msg<PageBean<WcWxGroupUserVo>> groupUserList(@Valid WxApiGroupUserListForm wxApiGroupUserListForm, BindingResult bindingResult){
        WcMachineCodeEntity codeEntity = CollectWss.getValidMachineCode(wxApiGroupUserListForm.getMachineCode(),true,false);
        Integer accountId = wxAccountService.getAccountIdByWxid(codeEntity.getId(), wxApiGroupUserListForm.getAccountWxid());
        PageBean<WcWxGroupUserEntity> pageBean = wxGroupUserService.getGroupUserList(
                accountId,
                wxApiGroupUserListForm.getGroupWxid(),
                wxApiGroupUserListForm.getPageNum(),
                wxApiGroupUserListForm.getPageSize());
        PageBean<WcWxGroupUserVo> result = new PageBean<>(pageBean);
        result.setList( pageBean.getList().stream().map(o -> {
            WcWxGroupUserVo vo = new WcWxGroupUserVo();
            vo.setWxid(o.getWxid());
            vo.setName(o.getName());
            vo.setHeadImg(o.getHeadImg());
            vo.setGroupName(o.getGroupName());
            vo.setState(o.getState());
            vo.setCreateTime(o.getCreateTime());
            return vo;
        }).collect(Collectors.toList()));
        return R.sucess(result);
    }


    /**
     * 获取用户聊天信息历史记录
     * @apiNote 获取实时消息请使用消息推送(需配置回调地址)
     * @param wxApiUserMsgListForm
     * @return
     */
    @GetMapping("/msg/list")
    public Msg<PageBean<WcWxMsgVo>> userMsgRecord(@Valid WxApiUserMsgListForm wxApiUserMsgListForm, BindingResult bindingResult){
        PageBean<WcWxMsgEntity> msgEntityList = wxUserMsgService.getUserMsg(
                wxApiUserMsgListForm.getAccountWxid(),
                wxApiUserMsgListForm.getGroupWxid(),
                wxApiUserMsgListForm.getTargetUserWxid(),
                wxApiUserMsgListForm.getMsgFrom(),
                wxApiUserMsgListForm.getPageNum(),
                wxApiUserMsgListForm.getPageSize());
        PageBean<WcWxMsgVo> result = new PageBean<>(msgEntityList);
        WcMachineCodeEntity machineCodeEntity = CollectWss.getValidMachineCode(wxApiUserMsgListForm.getMachineCode(),true,false);
        WcWxAccountEntity accountRecord = new WcWxAccountEntity();
        accountRecord.setWxid(wxApiUserMsgListForm.getAccountWxid());
        WcWxAccountEntity accountEntity = wcWxAccountMapper.selectOne(accountRecord);
        if(accountEntity == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR,"accountWxid 无效");
        result.setList(msgEntityList.getList().stream().map(o -> wxUserMsgService.entityToVo(machineCodeEntity,accountEntity,o)).collect(Collectors.toList()));
        return R.sucess(result);
    }
}
