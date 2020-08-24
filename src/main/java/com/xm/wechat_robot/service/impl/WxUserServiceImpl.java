package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.wechat_robot.mapper.WcWxAccountMapper;
import com.xm.wechat_robot.mapper.WcWxUserMapper;
import com.xm.wechat_robot.serialize.bo.client.UserList;
import com.xm.wechat_robot.serialize.entity.WcWxAccountEntity;
import com.xm.wechat_robot.serialize.entity.WcWxUserEntity;
import com.xm.wechat_robot.service.WxUserService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import com.xm.wechat_robot.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service("wxUserService")
public class WxUserServiceImpl implements WxUserService {

    @Autowired
    private WcWxUserMapper wcWxUserMapper;
    @Autowired
    private WcWxAccountMapper wcWxAccountMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;

    @Override
    public WcWxUserEntity createOrUpdate(Integer accountId, UserList userList) {
        if(accountId == null || StrUtil.isBlank(userList.getWxid()))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName()+"::"+accountId);
        return (WcWxUserEntity) LockUtil.lock(lock, () -> {
            WcWxUserEntity entity = new WcWxUserEntity();
            entity.setWxAccountId(accountId);
            entity.setWxid(userList.getWxid());
            WcWxUserEntity one = wcWxUserMapper.selectOne(entity);
            if(one != null){
                one.setState(1);
                one.setName(userList.getName());
                one.setHeadImg(userList.getHeadImg());
                one.setMark(userList.getMark());
                one.setSignStr(userList.getSign());
                one.setSex(userList.getSex());
                one.setCountry(userList.getCountry());
                one.setProvince(userList.getProvince());
                one.setCity(userList.getCity());
                one.setType(userList.getType());
                one.setGroupAdmin(userList.getGroupAdmin());
                one.setGroupCount(userList.getGroupCount());
                one.setGroupWxids(userList.getGroupWxids());
                wcWxUserMapper.updateByPrimaryKeySelective(one);
            }else {
                entity.setState(1);
                entity.setName(userList.getName());
                entity.setHeadImg(userList.getHeadImg());
                entity.setMark(userList.getMark());
                entity.setSignStr(userList.getSign());
                entity.setSex(userList.getSex());
                entity.setCountry(userList.getCountry());
                entity.setProvince(userList.getProvince());
                entity.setCity(userList.getCity());
                entity.setType(userList.getType());
                entity.setGroupAdmin(userList.getGroupAdmin());
                entity.setGroupCount(userList.getGroupCount());
                entity.setGroupWxids(userList.getGroupWxids());
                entity.setCreateTime(new Date());
                wcWxUserMapper.insertSelective(entity);
            }
            return entity;
        }).get();

    }


    @Override
    public void delUser(Integer accountId, String wxid) {
        if(accountId == null || StrUtil.isBlank(wxid))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("wxid",wxid);
        WcWxUserEntity entity = new WcWxUserEntity();
        entity.setState(0);
        wcWxUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public void setWaitCheck(Integer accountId) {
        if(accountId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("state",1);

        WcWxUserEntity entity = new WcWxUserEntity();
        entity.setState(2);
        wcWxUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public void delInvalidUser(Integer accountId) {
        if(accountId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("state",2);
        WcWxUserEntity entity = new WcWxUserEntity();
        entity.setState(0);
        wcWxUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public PageBean<WcWxUserEntity> getAccountUserList(Integer machineId, String accountWxid, Integer type, Integer pageNum, Integer pageSize) {
        WcWxAccountEntity accountRecord = new WcWxAccountEntity();
        accountRecord.setMachineId(machineId);
        accountRecord.setState(1);
        accountRecord.setWxid(accountWxid);
        WcWxAccountEntity one = wcWxAccountMapper.selectOne(accountRecord);
        if(one == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"微信:"  + accountWxid +  "未在该设备登录！");
        WcWxUserEntity record = new WcWxUserEntity();
        record.setWxAccountId(one.getId());
        record.setType(type);
        record.setState(1);
        PageHelper.startPage(pageNum,pageSize);
        List<WcWxUserEntity> list = wcWxUserMapper.select(record);
        PageBean<WcWxUserEntity> pageBean = new PageBean<>(list);
        return pageBean;
    }
}
