package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xm.wechat_robot.mapper.WcMachineCodeMapper;
import com.xm.wechat_robot.serialize.entity.WcMachineCodeEntity;
import com.xm.wechat_robot.service.MechineCodeService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

@Service("mechineIdService")
public class MechineIdServiceImpl implements MechineCodeService {

    @Autowired
    private WcMachineCodeMapper wcMachineCodeMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;


    @Override
    public WcMachineCodeEntity create(Integer userId, Integer expire) {
        if (userId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        WcMachineCodeEntity entity = new WcMachineCodeEntity();
        entity.setUserId(userId);
        entity.setMachineCode(UUID.randomUUID().toString());
        entity.setState(1);
        entity.setExpire(expire == null ? null : new Date(System.currentTimeMillis() + expire));
        entity.setCreateTime(new Date());
        wcMachineCodeMapper.insertSelective(entity);
        return entity;
    }

    @Override
    public WcMachineCodeEntity saveOrUpdate(String machineCode) {
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName() + "::" + machineCode);
        return LockUtil.lock(lock, () -> {
            WcMachineCodeEntity record = new WcMachineCodeEntity();
            record.setMachineCode(machineCode);
            record = wcMachineCodeMapper.selectOne(record);
            if (record == null) {
                //save
                record = new WcMachineCodeEntity();
                record.setMachineCode(machineCode);
                record.setState(1);
                record.setCreateTime(new Date());
                wcMachineCodeMapper.insertSelective(record);
            } else {
                //update
                record.setState(1);
                wcMachineCodeMapper.updateByPrimaryKeySelective(record);
            }
            return record;
        }).get();
    }

    @Override
    public void del(Integer userId, String machineId) {
        if (userId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcMachineCodeEntity.class);
        example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("machineId", machineId);
        WcMachineCodeEntity entity = new WcMachineCodeEntity();
        entity.setState(0);
        wcMachineCodeMapper.updateByExampleSelective(entity, example);
    }

    @Override
    public void expire(Integer userId, String machineId) {
        if (userId == null)
            throw new GlobleException(MsgEnum.DATA_NOT_EXISTS);
        Example example = new Example(WcMachineCodeEntity.class);
        example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("machineId", machineId);
        WcMachineCodeEntity entity = new WcMachineCodeEntity();
        entity.setState(2);
        wcMachineCodeMapper.updateByExampleSelective(entity, example);
    }

    @Override
    public void setMsgCallBackUrl(Integer machineId, String msgCallbackUrl) {
        WcMachineCodeEntity record = wcMachineCodeMapper.selectByPrimaryKey(machineId);
        record.setMsgCallBackUrl(msgCallbackUrl);
        wcMachineCodeMapper.updateByPrimaryKey(record);
    }

    @Override
    public WcMachineCodeEntity getByMachineCode(String machineCode) {
        WcMachineCodeEntity record = new WcMachineCodeEntity();
        record.setMachineCode(machineCode);
        return wcMachineCodeMapper.selectOne(record);
    }
}
