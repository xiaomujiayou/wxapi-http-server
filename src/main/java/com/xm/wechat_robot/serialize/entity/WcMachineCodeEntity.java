package com.xm.wechat_robot.serialize.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.Version;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_machine_code")
public class WcMachineCodeEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 机器码
	 */
	private String machineCode;

	/**
	 * 超时时间(不存在则表示永久)
	 */
	private java.util.Date expire;

	/**
	 * 状态(0:已删除,1:可用,2:已超时)
	 */
	private Integer state;

	/**
	 * 消息回调
	 */
	private String msgCallBackUrl;

	private java.util.Date createTime;
}
