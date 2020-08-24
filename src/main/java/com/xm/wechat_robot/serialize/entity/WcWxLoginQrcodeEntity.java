package com.xm.wechat_robot.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_wx_login_qrcode")
public class WcWxLoginQrcodeEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 机器码
	 */
	private Integer machineId;

	/**
	 * 进程ID
	 */
	private String pId;

	/**
	 * tcp通信ID
	 */
	private String tcpId;

	/**
	 * 登录二维码
	 */
	private String qrcode;

	/**
	 * 状态(0:已失效,1:有效)
	 */
	private Integer state;

	private java.util.Date createTime;
}
