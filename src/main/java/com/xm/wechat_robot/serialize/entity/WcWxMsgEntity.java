package com.xm.wechat_robot.serialize.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.Version;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "wc_wx_msg")
public class WcWxMsgEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属微信wxid
	 */
	private String accountWxid;

	/**
	 * 是否为群消息(0:否,1:是)
	 */
	private Integer chatRoom;

	/**
	 * 群组ID
	 */
	private String chatRoomWxid;

	/**
	 * 聊天用户wxid(为空则是自发消息)
	 */
	private String targetUserWxid;

	/**
	 * 消息来源(0:对方->我,1:我->对方)
	 */
	private Integer msgFrom;

	/**
	 * 消息类型(0x01:文字消息,0x03:图片,0x22:语音,0x25:好友确认,0x2A:名片,0x2B:视频,0x2F:表情(石头剪刀布),0x30:位置,0x31:共享实时位置、文件、转账、链接,0x3E:小视频,0x2710:系统消息 红包,0x2712:撤回消息)
	 */
	private String type;

	/**
	 * 消息内容(原始 xml)
	 */
	private String msg;

	/**
	 * 消息资源(xml)
	 */
	private String msgResource;

	private java.util.Date createTime;
}
