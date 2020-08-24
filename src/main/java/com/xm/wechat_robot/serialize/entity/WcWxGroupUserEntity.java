package com.xm.wechat_robot.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_wx_group_user")
public class WcWxGroupUserEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属微信账户
	 */
	private Integer wxAccountId;

	/**
	 * 群ID
	 */
	private String groupWxid;

	/**
	 * 用户ID
	 */
	private String wxid;

	/**
	 * 用户昵称
	 */
	private String name;

	/**
	 * 群昵称
	 */
	private String groupName;

	/**
	 * 头像
	 */
	private String headImg;

	/**
	 * 状态(0:已删除,1:已登录,2:已下线)
	 */
	private Integer state;

	private java.util.Date createTime;
}
