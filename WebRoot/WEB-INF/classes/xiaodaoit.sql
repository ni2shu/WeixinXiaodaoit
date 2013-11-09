
-- DROP DATABASE IF EXISTS `xiaodaoit`;
-- CREATE DATABASE `xiaodaoit` ;
USE `xiaodaoit`;

DROP TABLE IF EXISTS `tb_baike_temp`;
CREATE TABLE `tb_baike_temp` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(1024) DEFAULT NULL,
  `answer` text,
  `open_id` varchar(64) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=3661 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `tb_class_schedule`;
CREATE TABLE `tb_class_schedule` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `time` varchar(4) DEFAULT NULL COMMENT '时间',
  `weekly` varchar(255) DEFAULT NULL COMMENT '周次',
  `classroom` varchar(16) DEFAULT NULL COMMENT '教室',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=2528 DEFAULT CHARSET=utf8 COMMENT='课程表';

DROP TABLE IF EXISTS `tb_class_schedule19`;
CREATE TABLE `tb_class_schedule19` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `time` varchar(4) DEFAULT NULL COMMENT '时间',
  `weekly` varchar(255) DEFAULT NULL COMMENT '周次',
  `classroom` varchar(16) DEFAULT NULL COMMENT '教室',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=577 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='课程表';

DROP TABLE IF EXISTS `tb_getup_sign`;
CREATE TABLE `tb_getup_sign` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(64) DEFAULT NULL,
  `date` varchar(32) DEFAULT NULL,
  `time` varchar(32) DEFAULT NULL,
  `rank` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `is_request` varchar(2) NOT NULL DEFAULT '',
  `open_id` varchar(64) NOT NULL DEFAULT '' COMMENT '发送方帐号（一个OpenID）',
  `create_time` varchar(32) DEFAULT NULL COMMENT '消息创建时间 （整型）',
  `msg_type` varchar(16) DEFAULT NULL COMMENT '消息类型',
  `msg_id` varchar(128) DEFAULT NULL COMMENT '消息id，64位整型',
  `func_flag` varchar(32) DEFAULT NULL COMMENT '位0x0001被标志时，星标刚收到的消息。',
  `content` text COMMENT '文本消息内容',
  `article_count` varchar(16) DEFAULT NULL COMMENT '图文消息个数，限制为10条以内',
  `articles_xml` text COMMENT '多条图文消息信息的XML格式',
  `music_url` varchar(1024) DEFAULT NULL COMMENT '音乐链接',
  `hq_music_url` varchar(1024) DEFAULT NULL COMMENT '高质量音乐链接，WIFI环境优先使用该链接播放音乐',
  `location_x` varchar(255) DEFAULT NULL COMMENT '地理位置纬度',
  `location_y` varchar(255) DEFAULT NULL COMMENT '地理位置经度',
  `scale` varchar(255) DEFAULT NULL COMMENT '地图缩放大小',
  `label` varchar(512) DEFAULT NULL COMMENT '地理位置信息',
  `image_pic_url` varchar(1024) DEFAULT NULL COMMENT '图片链接',
  `event` varchar(64) DEFAULT NULL COMMENT '事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)',
  `event_key` varchar(255) DEFAULT NULL COMMENT '事件KEY值，与自定义菜单接口中KEY值对应',
  `link_title` varchar(255) DEFAULT NULL COMMENT '消息标题',
  `link_description` varchar(512) DEFAULT NULL COMMENT '消息描述',
  `link_url` varchar(1024) DEFAULT NULL COMMENT '消息链接',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=22718 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `tb_question`;
CREATE TABLE `tb_question` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(1024) DEFAULT NULL,
  `answer` text,
  `category` varchar(32) DEFAULT NULL,
  `open_id` varchar(64) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=2249 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `tb_show_love`;
CREATE TABLE `tb_show_love` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `from_open_id` varchar(64) DEFAULT NULL,
  `from_name` varchar(255) DEFAULT NULL,
  `to_name` varchar(255) DEFAULT NULL,
  `content` text,
  `date` varchar(32) DEFAULT NULL,
  `create_date_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_statistics_weixin`;
CREATE TABLE `tb_statistics_weixin` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(64) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(64) NOT NULL DEFAULT '',
  `student_num` varchar(64) DEFAULT NULL,
  `student_dept` varchar(255) DEFAULT NULL,
  `student_major` varchar(255) DEFAULT NULL,
  `student_info` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `student_pwd` varchar(1024) DEFAULT NULL,
  `user_log` text,
  `is_subscribe` varchar(2) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  `update_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1235 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS tb_msg_cache;
create table tb_msg_cache
(
   id                   integer primary key not null auto_increment,
   open_id         	 	varchar(64) comment 'OPEN_ID',
   weather             	varchar(128) comment '天气信息',
   express    			varchar(128) comment '快递信息',
   UNIQUE KEY `open_id` (`open_id`)
)default character set=utf8;
alter table tb_msg_cache comment '信息缓存表';
