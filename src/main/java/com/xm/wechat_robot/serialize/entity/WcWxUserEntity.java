package com.xm.wechat_robot.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_wx_user")
public class WcWxUserEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属登录微信
	 */
	private Integer wxAccountId;

	/**
	 * 用户类型(1:普通用户,2:微信群,3:公众号,4:其他)
	 */
	private Integer type;

	/**
	 * 微信id
	 */
	private String wxid;

	/**
	 * 微信昵称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String mark;

	/**
	 * 头像链接
	 */
	private String headImg;

	/**
	 * 签名
	 */
	private String signStr;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 性别(1:男,2:女)
	 */
	private Integer sex;

	/**
	 * 群主wxid
	 */
	private String groupAdmin;

	/**
	 * 微信群用户wxid ,","号间隔
	 */
	private String groupWxids;

	/**
	 * 群用户数量
	 */
	private Integer groupCount;

	/**
	 * 状态(0:已删除,1:正常,2:待检测)
	 */
	private Integer state;

	private java.util.Date createTime;
}
