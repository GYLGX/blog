/*
Navicat MySQL Data Transfer

Source Server         : Linux
Source Server Version : 50711
Source Host           : 129.28.118.109:3306
Source Database       : guolitest

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2020-06-21 16:03:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bloginfo
-- ----------------------------
DROP TABLE IF EXISTS `bloginfo`;
CREATE TABLE `bloginfo` (
  `id` int(6) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(60) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT 'blog内容',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `upateTime` datetime DEFAULT NULL,
  `imgUrl` varchar(255) DEFAULT NULL,
  `lid` int(6) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否发布',
  `reading_volume` int(5) DEFAULT NULL,
  `comment_volume` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for carousel_img
-- ----------------------------
DROP TABLE IF EXISTS `carousel_img`;
CREATE TABLE `carousel_img` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `blogid` int(6) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `blogid` (`blogid`),
  CONSTRAINT `blogid` FOREIGN KEY (`blogid`) REFERENCES `bloginfo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for classificati
-- ----------------------------
DROP TABLE IF EXISTS `classificati`;
CREATE TABLE `classificati` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `groupame` varchar(60) DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pid` int(10) DEFAULT NULL,
  `blogid` int(10) DEFAULT NULL,
  `head` varchar(255) DEFAULT NULL,
  `content` longtext,
  `uid` int(10) DEFAULT NULL,
  `puid` int(10) DEFAULT NULL,
  `comment_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `uuid` (`uid`),
  KEY `blog_id` (`blogid`),
  CONSTRAINT `blog_id` FOREIGN KEY (`blogid`) REFERENCES `bloginfo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `uuid` FOREIGN KEY (`uid`) REFERENCES `ucenbter` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_backend_api_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_backend_api_table`;
CREATE TABLE `sys_backend_api_table` (
  `backend_api_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `backend_api_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'API名称',
  `backend_api_url` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'API请求地址',
  `backend_api_method` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'API请求方式：GET、POST、PUT、DELETE',
  `pid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '父ID',
  `backend_api_sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`backend_api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_frontend_menu_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_frontend_menu_table`;
CREATE TABLE `sys_frontend_menu_table` (
  `frontend_menu_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `frontend_menu_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '前端菜单名称',
  `frontend_menu_url` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '前端菜单访问HTML地址',
  `pid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '父ID',
  `frontend_menu_sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`frontend_menu_id`) USING BTREE,
  KEY `p_id` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_backend_api_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_backend_api_table`;
CREATE TABLE `sys_role_backend_api_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '角色ID',
  `backend_api_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT 'API管理表ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `rid` (`role_id`),
  CONSTRAINT `rid` FOREIGN KEY (`role_id`) REFERENCES `sys_role_table` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_frontend_menu_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_frontend_menu_table`;
CREATE TABLE `sys_role_frontend_menu_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '角色ID',
  `frontend_menu_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '前端菜单管理ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `r_id` (`role_id`),
  CONSTRAINT `r_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role_table` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=363 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_table`;
CREATE TABLE `sys_role_table` (
  `role_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `role_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '角色ID',
  `description` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `sys_role_table_pk` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_user_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user_table`;
CREATE TABLE `sys_role_user_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '角色ID',
  `user_id` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_table`;
CREATE TABLE `sys_user_table` (
  `user_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `user_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '用户名称',
  `pass_word` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '用户密码-BCR算法加密',
  `description` varchar(800) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `state` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ucenbter
-- ----------------------------
DROP TABLE IF EXISTS `ucenbter`;
CREATE TABLE `ucenbter` (
  `id` int(9) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `mobile` varchar(22) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `register_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `url` varchar(900) DEFAULT NULL COMMENT '头像',
  `sex` int(1) DEFAULT '1',
  `state` tinyint(1) DEFAULT '1' COMMENT '状态',
  `warning_content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2020106 DEFAULT CHARSET=utf8;
