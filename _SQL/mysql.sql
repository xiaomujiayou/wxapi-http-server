/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - wechat_robot
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wechat_robot` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `wechat_robot`;

/*Table structure for table `wc_machine_code` */

DROP TABLE IF EXISTS `wc_machine_code`;

CREATE TABLE `wc_machine_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `machine_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '机器码',
  `expire` datetime DEFAULT NULL COMMENT '超时时间(不存在则表示永久)',
  `state` int(1) DEFAULT NULL COMMENT '状态(0:已删除,1:可用,2:已超时)',
  `msg_call_back_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息回调',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `wc_wx_account` */

DROP TABLE IF EXISTS `wc_wx_account`;

CREATE TABLE `wc_wx_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_id` int(11) DEFAULT NULL COMMENT '机器码id',
  `wxid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `head_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像链接',
  `tcp_id` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端子通信tcp链接id',
  `p_id` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端子进程id',
  `state` int(11) DEFAULT NULL COMMENT '状态(0:已删除,1:已登录,2:已下线)',
  `last_login` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `wc_wx_group_user` */

DROP TABLE IF EXISTS `wc_wx_group_user`;

CREATE TABLE `wc_wx_group_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wx_account_id` int(11) DEFAULT NULL COMMENT '所属微信账户',
  `group_wxid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群ID',
  `wxid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `group_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群昵称',
  `head_img` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `state` int(1) DEFAULT NULL COMMENT '状态(0:已删除,1:已登录,2:已下线)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=486 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `wc_wx_login_qrcode` */

DROP TABLE IF EXISTS `wc_wx_login_qrcode`;

CREATE TABLE `wc_wx_login_qrcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_id` int(11) DEFAULT NULL COMMENT '机器码',
  `p_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '进程ID',
  `tcp_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tcp通信ID',
  `qrcode` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录二维码',
  `state` int(1) DEFAULT NULL COMMENT '状态(0:已失效,1:有效)',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `wc_wx_msg` */

DROP TABLE IF EXISTS `wc_wx_msg`;

CREATE TABLE `wc_wx_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_wxid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属微信wxid',
  `chat_room` int(1) DEFAULT NULL COMMENT '是否为群消息(0:否,1:是)',
  `chat_room_wxid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群组ID',
  `target_user_wxid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '聊天用户wxid(为空则是自发消息)',
  `msg_from` int(1) DEFAULT NULL COMMENT '消息来源(0:对方->我,1:我->对方)',
  `type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息类型(0x01:文字消息,0x03:图片,0x22:语音,0x25:好友确认,0x2A:名片,0x2B:视频,0x2F:表情(石头剪刀布),0x30:位置,0x31:共享实时位置、文件、转账、链接,0x3E:小视频,0x2710:系统消息 红包,0x2712:撤回消息)',
  `msg` longtext COLLATE utf8mb4_unicode_ci COMMENT '消息内容(原始 xml)',
  `msg_resource` longtext COLLATE utf8mb4_unicode_ci COMMENT '消息资源(xml)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `wc_wx_user` */

DROP TABLE IF EXISTS `wc_wx_user`;

CREATE TABLE `wc_wx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wx_account_id` int(11) DEFAULT NULL COMMENT '所属登录微信',
  `type` int(1) DEFAULT NULL COMMENT '用户类型(1:普通用户,2:微信群,3:公众号,4:其他)',
  `wxid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `mark` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `head_img` varchar(225) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像链接',
  `sign_str` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `country` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `province` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `city` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `sex` int(1) DEFAULT NULL COMMENT '性别(1:男,2:女)',
  `group_admin` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群主wxid',
  `group_wxids` text COLLATE utf8mb4_unicode_ci COMMENT '微信群用户wxid ,","号间隔',
  `group_count` int(11) DEFAULT NULL COMMENT '群用户数量',
  `state` int(1) DEFAULT NULL COMMENT '状态(0:已删除,1:正常,2:待检测)',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=820 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
