/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MariaDB
 Source Server Version : 100604
 Source Host           : localhost:3306
 Source Schema         : eoffice

 Target Server Type    : MariaDB
 Target Server Version : 100604
 File Encoding         : 65001

 Date: 06/01/2022 16:08:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for eoffice_config
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_config`;
CREATE TABLE `eoffice_config` (
  `cid` bigint(20) NOT NULL,
  `module` varchar(50) DEFAULT NULL COMMENT '模块',
  `ckey` varchar(50) DEFAULT NULL COMMENT '配置key',
  `cvalue` varchar(100) DEFAULT NULL COMMENT '配置value',
  `create_at` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='配置';

-- ----------------------------
-- Table structure for eoffice_content_storage
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_content_storage`;
CREATE TABLE `eoffice_content_storage` (
  `sid` bigint(20) unsigned NOT NULL COMMENT '存储ID',
  `content` longblob DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='内容存储';

-- ----------------------------
-- Table structure for eoffice_doc_template
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_doc_template`;
CREATE TABLE `eoffice_doc_template` (
  `tid` bigint(20) NOT NULL COMMENT '模板ID',
  `title` varchar(255) DEFAULT '' COMMENT '模板名称',
  `sid` bigint(20) unsigned DEFAULT NULL COMMENT '存储ID',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档模板';

-- ----------------------------
-- Table structure for eoffice_doc_template_category
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_doc_template_category`;
CREATE TABLE `eoffice_doc_template_category` (
  `cid` bigint(20) NOT NULL COMMENT '模板分类ID',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `pcid` bigint(20) unsigned DEFAULT 0 COMMENT '父分类ID',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `extension` varchar(255) DEFAULT '' COMMENT '支持的文件类型 逗号 『,』 分割',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板分类表';

-- ----------------------------
-- Table structure for eoffice_folder
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_folder`;
CREATE TABLE `eoffice_folder` (
  `fid` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `pfid` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '父ID',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '目录名称',
  `display_order` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `icon` varchar(20) NOT NULL DEFAULT '1' COMMENT '目录图标',
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT '路径',
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) unsigned NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='目录';

-- ----------------------------
-- Table structure for eoffice_perm
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_perm`;
CREATE TABLE `eoffice_perm` (
  `pid` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `target_id` bigint(20) unsigned NOT NULL COMMENT '目标ID',
  `uid` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `type` tinyint(2) unsigned NOT NULL DEFAULT 0 COMMENT '类型 0 资源 1 文件夹',
  `download` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '下载',
  `edit` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '编辑',
  `print` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '打印',
  `view` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '查看',
  `comment` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否删除',
  `share` tinyint(1) NOT NULL COMMENT '分享',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) unsigned NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源权限';

-- ----------------------------
-- Table structure for eoffice_resource
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource`;
CREATE TABLE `eoffice_resource` (
  `rid` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `fid` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '目录ID',
  `sid` bigint(20) NOT NULL DEFAULT 0 COMMENT '文件ID',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '资源名称',
  `extension` varchar(10) NOT NULL DEFAULT '0' COMMENT '资源类型',
  `size` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '资源大小',
  `display_order` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT '存储路径',
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) unsigned NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='资源（文件）';

-- ----------------------------
-- Table structure for eoffice_resource_attachment
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource_attachment`;
CREATE TABLE `eoffice_resource_attachment` (
  `aid` bigint(20) unsigned NOT NULL COMMENT '附件ID',
  `rid` bigint(20) NOT NULL COMMENT '资源ID',
  `sid` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '存储ID',
  PRIMARY KEY (`aid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='文档 附件表';

-- ----------------------------
-- Table structure for eoffice_resource_version
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource_version`;
CREATE TABLE `eoffice_resource_version` (
  `vid` bigint(20) unsigned NOT NULL COMMENT '版本ID',
  `rid` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '资源ID',
  `sid` bigint(20) NOT NULL DEFAULT 0 COMMENT '存储ID',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '资源名称',
  `size` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '资源大小',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) unsigned NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='资源（文件）';

-- ----------------------------
-- Table structure for eoffice_storage
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_storage`;
CREATE TABLE `eoffice_storage` (
  `sid` bigint(20) unsigned NOT NULL COMMENT '存储ID',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `size` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '文件大小',
  `md5` varchar(50) NOT NULL DEFAULT '' COMMENT '文件MD5',
  `extension` varchar(20) NOT NULL COMMENT '文件扩展名',
  `path` varchar(255) DEFAULT '' COMMENT '存储相对路径',
  `create_at` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  PRIMARY KEY (`sid`) USING BTREE,
  KEY `idx_extension` (`extension`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='文档存储基础信息表';

-- ----------------------------
-- Table structure for eoffice_user
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_user`;
CREATE TABLE `eoffice_user` (
  `uid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `email` char(40) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(255) NOT NULL DEFAULT '' COMMENT '用户手机',
  `nickname` char(30) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `username` char(30) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '用户密码',
  `status` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '用户状态 0 可用 1禁用',
  `regip` varchar(30) NOT NULL DEFAULT '' COMMENT '注册的IP',
  `create_at` datetime(1) NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) unsigned DEFAULT 0 COMMENT '创建人ID',
  `update_at` datetime(1) DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '更新人ID',
  `auth_type` varchar(10) DEFAULT '' COMMENT '认证方式',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE,
  KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Table structure for spring_session
-- ----------------------------
DROP TABLE IF EXISTS `spring_session`;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`) USING BTREE,
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`) USING BTREE,
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for spring_session_attributes
-- ----------------------------
DROP TABLE IF EXISTS `spring_session_attributes`;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`) USING BTREE,
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
