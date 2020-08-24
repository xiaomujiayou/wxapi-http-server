package com.xm.wechat_robot.serialize.vo;

import lombok.Data;

@Data
public class WcWxGroupUserVo {

    /**
     * 用户ID
     * @mock 微信id
     */
    private String wxid;

    /**
     * 用户昵称
     * @mock 小新
     */
    private String name;

    /**
     * 群昵称
     * @mock 。。。
     */
    private String groupName;

    /**
     * 头像
     * @mock http://wx.qlogo.cn/mmhead/ver_1/DffgKiaKNTJy4pGibIRInA2FF8u2QGuV5J6jGeLoU6oo0G4nbkhiaOOxj40FjF3vaicKRzKIsLuoYxbic5Zk80Il44Z94KRUFCR2NY6wib6I81tRQ/0
     */
    private String headImg;

    /**
     * 状态(0:已删除,1:已登录,2:已下线)
     * @mock 1
     */
    private Integer state;

    /**
     * 创建时间
     * @mock 2020-08-02 16:03:52
     */
    private java.util.Date createTime;
}
