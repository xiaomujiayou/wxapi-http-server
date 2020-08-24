package com.xm.wechat_robot.mybatis;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface SelectIdsMapper<T> {

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    @SelectProvider(type = SelectIdsProvider.class, method = "dynamicSQL")
    List<Integer> selectIds(T record);
}
