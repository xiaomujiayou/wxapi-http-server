package com.xm.wechat_robot.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.wechat_robot.mapper.WcWxAccountMapper;
import com.xm.wechat_robot.mapper.WcWxLoginQrcodeMapper;
import com.xm.wechat_robot.serialize.entity.WcWxAccountEntity;
import com.xm.wechat_robot.service.WxAccountService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service("wxAccountService")
public class WxAccountServiceImpl implements WxAccountService {

    @Autowired
    private WcWxAccountMapper wcWxAccountMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;

    @Override
    public WcWxAccountEntity login(Integer machineId, String wxid, String name, String headImg, String tcpId, String pId) {
        if(ObjectUtil.hasEmpty(machineId,wxid,name,headImg,tcpId,pId))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName()+"::"+machineId+"::"+wxid);
        return (WcWxAccountEntity) LockUtil.lock(lock, () -> {
            WcWxAccountEntity entity = new WcWxAccountEntity();
//            entity.setMachineId(machineId);
            entity.setWxid(wxid);
            WcWxAccountEntity one = wcWxAccountMapper.selectOne(entity);
            if(one != null){
                one.setMachineId(machineId);
                one.setName(name);
                one.setHeadImg(headImg);
                one.setPId(pId);
                one.setTcpId(tcpId);
                one.setState(1);
                one.setLastLogin(new Date());
                wcWxAccountMapper.updateByPrimaryKey(one);
            }else {
                entity.setMachineId(machineId);
                entity.setName(name);
                entity.setHeadImg(headImg);
                entity.setPId(pId);
                entity.setTcpId(tcpId);
                entity.setState(1);
                entity.setLastLogin(new Date());
                entity.setCreateTime(entity.getLastLogin());
                wcWxAccountMapper.insertSelective(entity);
            }
            return entity;
        }).get();
    }

    @Override
    public void logout(Integer machineId, String wxid) {
        if(machineId == null || StrUtil.isBlank(wxid))
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        unLine(machineId,wxid);
    }

    @Override
    public void logout(Integer machineId, Integer accountId) {
        if(machineId == null || accountId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        WcWxAccountEntity record = new WcWxAccountEntity();
        record.setId(accountId);
        record.setState(2);
        record.setLastLogin(new Date());
        wcWxAccountMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void logoutAll(Integer machineId) {
        if(machineId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        unLine(machineId,null);
    }

    private void unLine(Integer machineId, String wxid){
        Example example = new Example(WcWxAccountEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("machineId",machineId);
        if(StrUtil.isNotBlank(wxid))
            criteria.andEqualTo("wxid",wxid);
        WcWxAccountEntity entity = new WcWxAccountEntity();
        entity.setState(2);
        entity.setLastLogin(new Date());
        wcWxAccountMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public WcWxAccountEntity getAccountId(Integer machineId, String tcpId) {
        if(machineId == null || StrUtil.isBlank(tcpId))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        WcWxAccountEntity record = new WcWxAccountEntity();
        record.setMachineId(machineId);
        record.setTcpId(tcpId);
        record = wcWxAccountMapper.selectOne(record);
        if(record != null)
            return record;
        return null;
    }

    @Override
    public Integer getAccountIdByWxid(Integer machineId, String accountWxid) {
        if(machineId == null || StrUtil.isBlank(accountWxid))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        WcWxAccountEntity record = new WcWxAccountEntity();
        record.setMachineId(machineId);
        record.setWxid(accountWxid);
        record = wcWxAccountMapper.selectOne(record);
        if(record == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS,"accountWxid不存在");
        return record.getId();
    }

    @Override
    public WcWxAccountEntity getWxAccount(Integer machineId, String wxid) {
        WcWxAccountEntity entity = new WcWxAccountEntity();
        entity.setMachineId(machineId);
        entity.setWxid(wxid);
        return wcWxAccountMapper.selectOne(entity);
    }

    @Override
    public List<WcWxAccountEntity> getAllList(Integer machineId, Integer state) {
        WcWxAccountEntity record = new WcWxAccountEntity();
        record.setMachineId(machineId);
        record.setState(state);
        return wcWxAccountMapper.select(record);
    }


    @Override
    public void reConnect(Integer machineId,String onloginWxids) {
        if(StrUtil.isBlank(onloginWxids))
            return;
        String[] wxids = onloginWxids.split(",");
        List<String> wxidList = CollUtil.newArrayList(wxids);
        WcWxAccountEntity entity = new WcWxAccountEntity();
        entity.setState(1);
        entity.setMachineId(machineId);
        Example example = new Example(WcWxAccountEntity.class);
        example.createCriteria().andIn("wxid",wxidList);
        wcWxAccountMapper.updateByExampleSelective(entity,example);
    }
}
