package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.wechat_robot.mapper.WcWxGroupUserMapper;
import com.xm.wechat_robot.serialize.bo.client.GroupUserList;
import com.xm.wechat_robot.serialize.entity.WcWxGroupUserEntity;
import com.xm.wechat_robot.service.WxGroupUserService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import com.xm.wechat_robot.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service("wxGroupUserService")
public class WxGroupUserServiceImpl implements WxGroupUserService {

    @Autowired
    private WcWxGroupUserMapper wcWxGroupUserMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;


    @Override
    public WcWxGroupUserEntity createOrUpdate(Integer accountId, GroupUserList groupUserList) {
        if(accountId == null || StrUtil.isBlank(groupUserList.getWxid()))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName()+"::"+accountId);
        return (WcWxGroupUserEntity) LockUtil.lock(lock, () -> {
            WcWxGroupUserEntity entity = new WcWxGroupUserEntity();
            entity.setWxAccountId(accountId);
            entity.setGroupWxid(groupUserList.getGroupWxid());
            entity.setWxid(groupUserList.getWxid());
            WcWxGroupUserEntity one = wcWxGroupUserMapper.selectOne(entity);
            if(one != null){
                one.setState(1);
                one.setHeadImg(groupUserList.getHeadImg());
                one.setName(groupUserList.getName());
                one.setGroupName(groupUserList.getGroupName());
                wcWxGroupUserMapper.updateByPrimaryKeySelective(one);
            }else {
                entity.setState(1);
                entity.setHeadImg(groupUserList.getHeadImg());
                entity.setName(groupUserList.getName());
                entity.setGroupName(groupUserList.getGroupName());
                entity.setCreateTime(new Date());
                wcWxGroupUserMapper.insertSelective(entity);
            }
            return entity;
        }).get();

    }

    @Override
    public void delUser(Integer accountId,String groupWxid, String wxid) {
        if(accountId == null || StrUtil.isBlank(wxid) || StrUtil.isBlank(groupWxid))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxGroupUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("groupWxid",groupWxid)
                .andEqualTo("wxid",wxid);
        WcWxGroupUserEntity entity = new WcWxGroupUserEntity();
        entity.setState(0);
        wcWxGroupUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public void setWaitCheck(Integer accountId) {
        if(accountId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxGroupUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("state",1);

        WcWxGroupUserEntity entity = new WcWxGroupUserEntity();
        entity.setState(2);
        wcWxGroupUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public void delInvalidUser(Integer accountId) {
        if(accountId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcWxGroupUserEntity.class);
        example.createCriteria()
                .andEqualTo("wxAccountId",accountId)
                .andEqualTo("state",2);
        WcWxGroupUserEntity entity = new WcWxGroupUserEntity();
        entity.setState(0);
        wcWxGroupUserMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public PageBean<WcWxGroupUserEntity> getGroupUserList(Integer accountId, String groupWxid, Integer pageNum, Integer pageSize) {
        WcWxGroupUserEntity accountRecord = new WcWxGroupUserEntity();
        accountRecord.setState(1);
        accountRecord.setId(accountId);
        int count = wcWxGroupUserMapper.selectCount(accountRecord);
        if(count != 1)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"accountId 不属于该设备");
        WcWxGroupUserEntity record = new WcWxGroupUserEntity();
        record.setWxAccountId(accountId);
        record.setState(1);
        PageHelper.startPage(pageNum,pageSize);
        List<WcWxGroupUserEntity> list = wcWxGroupUserMapper.select(record);
        PageBean<WcWxGroupUserEntity> pageBean = new PageBean<>(list);
        return pageBean;
    }
}
