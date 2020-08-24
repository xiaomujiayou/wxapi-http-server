package com.xm.wechat_robot.serialize.vo;

import lombok.Data;

@Data
public class WcWxUserVo {

    /**
     * 用户类型(1:普通用户,2:微信群,3:公众号,4:其他)
     * @mock 1
     */
    private Integer type;

    /**
     * 所属登录微信
     * @mock 2
     */
    private Integer wxAccountId;

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
     * 备注
     * @mock 朋友
     */
    private String mark;

    /**
     * 群主wxid
     * @mock wxid_95zo7g057d6z22
     */
    private String groupAdmin;

    /**
     * 群用户数量
     * @mock 99
     */
    private Integer userCount;

    /**
     * 头像链接
     * @mock http://wx.qlogo.cn/mmhead/ver_1/DBSEib7uUcGmNgY02eOyJeYLybKhgzujr2AzrE3FfibpSaoicM6FsRaSia0uyvRPopJARfiaOZNftHIx0MbSjBjNdcBMvfUWia2uqBWB28ibGXiaGuM/0
     */
    private String headImg;

    /**
     * 签名
     * @mock 哈哈哈
     */
    private String signStr;

    /**
     * 国家
     * @mock CN
     */
    private String country;

    /**
     * 省
     * @mock Guangdong
     */
    private String province;

    /**
     * 城市
     * @mock Shenzhen
     */
    private String city;

    /**
     * 性别(1:男,2:女)
     * @mock 1
     */
    private Integer sex;

    /**
     * 创建时间
     * @mock 2020-08-02 16:02:49
     */
    private java.util.Date createTime;
}
