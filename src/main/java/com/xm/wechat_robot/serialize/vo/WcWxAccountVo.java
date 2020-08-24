package com.xm.wechat_robot.serialize.vo;

import lombok.Data;

@Data
public class WcWxAccountVo {

    /**
     * 微信id
     * @mock wxid_95zo7g057d6z22
     */
    private String wxid;

    /**
     * 微信昵称
     * @mock 小新
     */
    private String name;

    /**
     * 头像链接
     * @mock http://wx.qlogo.cn/mmhead/ver_1/DBSEib7uUcGmNgY02eOyJeYLybKhgzujr2AzrE3FfibpSaoicM6FsRaSia0uyvRPopJARfiaOZNftHIx0MbSjBjNdcBMvfUWia2uqBWB28ibGXiaGuM/0
     */
    private String headImg;

    /**
     * 状态(0:已删除,1:已登录,2:已下线)
     * @mock 1
     */
    private Integer state;

    /**
     * 最后在线时间
     * @mock 2020-08-22 19:27:59
     */
    private java.util.Date lastLogin;

    /**
     * 微信客户端ID
     * @mock 1304
     */
    private String clientId;

    /**
     * 创建时间
     * @mock 2020-08-20 22:44:04
     */
    private java.util.Date createTime;
}
