package com.xm.wechat_robot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.xm.wechat_robot.mapper.WcWxLoginQrcodeMapper;
import com.xm.wechat_robot.serialize.entity.WcWxLoginQrcodeEntity;
import com.xm.wechat_robot.service.WxQrcodeService;
import com.xm.wechat_robot.exception.GlobleException;
import com.xm.wechat_robot.util.LockUtil;
import com.xm.wechat_robot.util.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.concurrent.locks.Lock;

@Service("wxQrcodeService")
public class WxQrcodeServiceImpl implements WxQrcodeService {

    @Autowired
    private WcWxLoginQrcodeMapper wcWxLoginQrcodeMapper;
    @Autowired
    private DefaultLockRegistry defaultLockRegistry;

    @Override
    public void genQrcode(Integer machineId, String pid, String tcpId, String qrcode) {
        if(ObjectUtil.hasEmpty(machineId,pid,tcpId,qrcode))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        Lock lock = defaultLockRegistry.obtain(this.getClass().getSimpleName()+"::"+machineId);
        LockUtil.lock(lock, () -> {
            WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
            entity.setMachineId(machineId);
            WcWxLoginQrcodeEntity one = wcWxLoginQrcodeMapper.selectOne(entity);
            if(one == null){
                entity.setPId(pid);
                entity.setTcpId(tcpId);
                entity.setQrcode(qrcode);
                entity.setState(1);
                entity.setCreateTime(new Date());
                wcWxLoginQrcodeMapper.insertSelective(entity);
            }else {
                one.setPId(pid);
                one.setTcpId(tcpId);
                one.setQrcode(qrcode);
                one.setState(1);
                wcWxLoginQrcodeMapper.updateByPrimaryKeySelective(one);
            }
        });
    }

    @Override
    public void invalid(Integer machineId, String pid, String tcpId) {
        if(ObjectUtil.hasEmpty(machineId,pid,tcpId))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
        entity.setState(0);

        Example example = new Example(WcWxLoginQrcodeEntity.class);
        example.createCriteria()
                .andEqualTo("machineId",machineId)
                .andEqualTo("pId",pid)
                .andEqualTo("tcpId",tcpId);
        wcWxLoginQrcodeMapper.updateByExampleSelective(entity,example);
    }

    @Override
    public void invalidAll(Integer machineId) {
        if(machineId == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        WcWxLoginQrcodeEntity record = new WcWxLoginQrcodeEntity();
        record.setState(0);

        Example example = new Example(WcWxLoginQrcodeEntity.class);
        example.createCriteria()
                .andEqualTo("machineId",machineId);
        wcWxLoginQrcodeMapper.updateByExampleSelective(record,example);
    }

    @Override
    public WcWxLoginQrcodeEntity getValidQrcode(Integer machineId) {
        if(ObjectUtil.hasEmpty(machineId))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        WcWxLoginQrcodeEntity entity = new WcWxLoginQrcodeEntity();
        entity.setMachineId(machineId);
        entity.setState(1);
        return wcWxLoginQrcodeMapper.selectOne(entity);
    }
}
