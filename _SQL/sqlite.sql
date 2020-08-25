-- import to SQLite by running: sqlite3.exe db.sqlite3 -init sqlite.sql

PRAGMA journal_mode = MEMORY;
PRAGMA synchronous = OFF;
PRAGMA foreign_keys = OFF;
PRAGMA ignore_check_constraints = OFF;
PRAGMA auto_vacuum = NONE;
PRAGMA secure_delete = OFF;
BEGIN TRANSACTION;

DROP TABLE IF EXISTS `wc_machine_code`;

CREATE TABLE `wc_machine_code` (
`id` INTEGER NOT NULL ,
`user_id` INTEGER DEFAULT NULL ,
`machine_code` TEXT  DEFAULT NULL ,
`expire` datetime DEFAULT NULL ,
`state` INTEGER DEFAULT NULL ,
`msg_call_back_url` TEXT  DEFAULT NULL ,
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wc_wx_account`;

CREATE TABLE `wc_wx_account` (
`id` INTEGER NOT NULL ,
`machine_id` INTEGER DEFAULT NULL ,
`wxid` TEXT  DEFAULT NULL ,
`name` TEXT  DEFAULT NULL ,
`head_img` TEXT  DEFAULT NULL ,
`tcp_id` TEXT  DEFAULT NULL ,
`p_id` TEXT  DEFAULT NULL ,
`state` INTEGER DEFAULT NULL ,
`last_login` datetime DEFAULT NULL,
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wc_wx_group_user`;

CREATE TABLE `wc_wx_group_user` (
`id` INTEGER NOT NULL ,
`wx_account_id` INTEGER DEFAULT NULL ,
`group_wxid` TEXT  DEFAULT NULL ,
`wxid` TEXT  DEFAULT NULL ,
`name` TEXT  DEFAULT NULL ,
`group_name` TEXT  DEFAULT NULL ,
`head_img` TEXT  DEFAULT NULL ,
`state` INTEGER DEFAULT NULL ,
`create_time` datetime NOT NULL,
PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wc_wx_login_qrcode`;

CREATE TABLE `wc_wx_login_qrcode` (
`id` INTEGER NOT NULL ,
`machine_id` INTEGER DEFAULT NULL ,
`p_id` TEXT  DEFAULT NULL ,
`tcp_id` TEXT  DEFAULT NULL ,
`qrcode` TEXT  DEFAULT NULL ,
`state` INTEGER DEFAULT NULL ,
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wc_wx_msg`;

CREATE TABLE `wc_wx_msg` (
`id` INTEGER NOT NULL ,
`account_wxid` TEXT  DEFAULT NULL ,
`chat_room` INTEGER DEFAULT NULL ,
`chat_room_wxid` TEXT  DEFAULT NULL ,
`target_user_wxid` TEXT  DEFAULT NULL ,
`msg_from` INTEGER DEFAULT NULL ,
`type` TEXT  DEFAULT NULL ,
`msg` TEXT  ,
`msg_resource` TEXT  ,
`create_time` datetime NOT NULL,
PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wc_wx_user`;

CREATE TABLE `wc_wx_user` (
`id` INTEGER NOT NULL ,
`wx_account_id` INTEGER DEFAULT NULL ,
`type` INTEGER DEFAULT NULL ,
`wxid` TEXT  DEFAULT NULL ,
`name` TEXT  DEFAULT NULL ,
`mark` TEXT  DEFAULT NULL ,
`head_img` TEXT  DEFAULT NULL ,
`sign_str` TEXT  DEFAULT NULL ,
`country` TEXT  DEFAULT NULL ,
`province` TEXT  DEFAULT NULL ,
`city` TEXT  DEFAULT NULL ,
`sex` INTEGER DEFAULT NULL ,
`group_admin` TEXT  DEFAULT NULL ,
`group_wxids` text  ,
`group_count` INTEGER DEFAULT NULL ,
`state` INTEGER DEFAULT NULL ,
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
);





COMMIT;
PRAGMA ignore_check_constraints = ON;
PRAGMA foreign_keys = ON;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;
