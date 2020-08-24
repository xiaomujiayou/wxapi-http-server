package com.xm.wechat_robot.util;

import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnumUtils {

    /**
     * 通过枚举某个属性获取其实例(这个属性应当是唯一的)
     *
     * @param clzz
     * @param keyName
     * @param key
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T extends Enum> T getEnum(Class<T> clzz, String keyName, Object key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] objects = clzz.getEnumConstants();
        String methodName = "get" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1, keyName.length());
        Method coinAddressCode = clzz.getMethod(methodName);
        for (Object object : objects) {
            if (coinAddressCode.invoke(object).equals(key)) {
                return (T) object;
            }
        }
        throw new EnumConstantNotPresentException(clzz, methodName + " 找不到所对应的枚举" + " " + key);
    }

    /**
     * 多个筛选条件（返回唯一结果）
     * @param clzz
     * @param fieldMap
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T extends Enum> T getEnum(Class<T> clzz, Map<String, Object> fieldMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (fieldMap == null || fieldMap.isEmpty())
            throw new NullPointerException("fieldMap 不存在或者为空");
        T[] objectArr = clzz.getEnumConstants();
        List<T> objects = CollUtil.toList(objectArr);
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            objects = filter(clzz, objects, entry.getKey(), entry.getValue());
        }
        if(objects.size() != 1)
            throw new EnumConstantNotPresentException(clzz,  "结果数量异常："+objects.size());
        return objects.get(0);
    }

    /**
     * 根据字段过滤
     *
     * @param clzz    :枚举类型
     * @param objects :需要过滤的实例
     * @param keyName :字段名称
     * @param key     :字段值
     * @return
     */
    private static <T extends Enum> List<T> filter(Class<T> clzz, List<T> objects, String keyName, Object key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<T> result = new ArrayList<>();
        String methodName = "get" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1, keyName.length());
        if (objects == null || objects.isEmpty())
            throw new EnumConstantNotPresentException(clzz, methodName + " 找不到所对应的枚举" + " " + key);
        Method coinAddressCode = clzz.getMethod(methodName);
        for (Object object : objects) {
            if (coinAddressCode.invoke(object).equals(key)) {
                result.add((T) object);
            }
        }
        return result;
    }
}
