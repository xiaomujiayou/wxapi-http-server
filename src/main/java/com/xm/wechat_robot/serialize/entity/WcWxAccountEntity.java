package com.xm.wechat_robot.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_wx_account")
public class WcWxAccountEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 机器码id
	 */
	private Integer machineId;

	/**
	 * 微信id
	 */
	private String wxid;

	/**
	 * 微信昵称
	 */
	private String name;

	/**
	 * 头像链接
	 */
	private String headImg;

	/**
	 * 客户端子通信tcp链接id
	 */
	private String tcpId;

	/**
	 * 客户端子进程id
	 */
	private String pId;

	/**
	 * 状态(0:已删除,1:已登录,2:已下线)
	 */
	private Integer state;

	private java.util.Date lastLogin;

	private java.util.Date createTime;
}
