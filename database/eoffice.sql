/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MariaDB
 Source Server Version : 100421
 Source Host           : localhost:3306
 Source Schema         : eoffice

 Target Server Type    : MariaDB
 Target Server Version : 100421
 File Encoding         : 65001

 Date: 16/01/2022 22:33:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for eoffice_config
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_config`;
CREATE TABLE `eoffice_config`  (
  `cid` bigint(20) NOT NULL,
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块',
  `ckey` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置key',
  `cvalue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置value',
  `create_at` datetime NULL DEFAULT NULL,
  `create_by` bigint(20) NULL DEFAULT NULL,
  `update_at` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_content_storage
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_content_storage`;
CREATE TABLE `eoffice_content_storage`  (
  `sid` bigint(20) UNSIGNED NOT NULL COMMENT '存储ID',
  `content` longblob NULL COMMENT '内容',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '内容存储' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for eoffice_doc_template
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_doc_template`;
CREATE TABLE `eoffice_doc_template`  (
  `tid` bigint(20) NOT NULL COMMENT '模板ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模板名称',
  `sid` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '存储ID',
  `create_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_doc_template_category
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_doc_template_category`;
CREATE TABLE `eoffice_doc_template_category`  (
  `cid` bigint(20) NOT NULL COMMENT '模板分类ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `pcid` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '父分类ID',
  `create_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `extension` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '支持的文件类型 逗号 『,』 分割',
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_folder
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_folder`;
CREATE TABLE `eoffice_folder`  (
  `fid` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `pfid` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录名称',
  `display_order` smallint(6) UNSIGNED NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '目录图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `size` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '大小',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  `delete_at` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_group
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_group`;
CREATE TABLE `eoffice_group`  (
  `gid` bigint(20) NOT NULL COMMENT '分组ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `pgid` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '父分组ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '0 系统分组 1 用户自定义分组',
  `create_at` datetime NULL DEFAULT NULL,
  `create_by` bigint(20) NULL DEFAULT NULL,
  `update_at` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `extra_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '外部系统的ID',
  PRIMARY KEY (`gid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_perm
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_perm`;
CREATE TABLE `eoffice_perm`  (
  `pid` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `target_id` bigint(20) UNSIGNED NOT NULL COMMENT '目标ID',
  `target_type` tinyint(2) UNSIGNED NULL DEFAULT 0 COMMENT '目标类型 0 用户 1 群组',
  `rid` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '资源ID',
  `type` tinyint(2) UNSIGNED NOT NULL DEFAULT 10 COMMENT '类型',
  `download` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '下载',
  `edit` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '编辑',
  `print` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '打印',
  `view` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '查看',
  `comment` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '注释',
  `share` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分享',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_resource
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource`;
CREATE TABLE `eoffice_resource`  (
  `rid` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `fid` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '目录ID',
  `sid` bigint(20) NOT NULL DEFAULT 0 COMMENT '文件ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `extension` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '资源类型',
  `size` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '资源大小',
  `display_order` smallint(6) UNSIGNED NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '存储路径',
  `deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人ID',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  `delete_at` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源（文件）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_resource_attachment
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource_attachment`;
CREATE TABLE `eoffice_resource_attachment`  (
  `aid` bigint(20) UNSIGNED NOT NULL COMMENT '附件ID',
  `rid` bigint(20) NOT NULL COMMENT '资源ID',
  `sid` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '存储ID',
  PRIMARY KEY (`aid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档 附件表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for eoffice_resource_version
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_resource_version`;
CREATE TABLE `eoffice_resource_version`  (
  `vid` bigint(20) UNSIGNED NOT NULL COMMENT '版本ID',
  `rid` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '资源ID',
  `sid` bigint(20) NOT NULL DEFAULT 0 COMMENT '存储ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `size` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '资源大小',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源（文件）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_share
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_share`;
CREATE TABLE `eoffice_share`  (
  `share_id` bigint(20) UNSIGNED NOT NULL,
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `type` int(4) NULL DEFAULT NULL COMMENT '类型 0 资源 1 文件夹',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `auth_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '授权码， 空表示不需要授权码',
  `endtime` datetime NULL DEFAULT NULL COMMENT '截止日期， 0000-00-00 00:00:00 表示永久有效',
  `create_at` datetime NULL DEFAULT NULL,
  `update_at` datetime NULL DEFAULT NULL,
  `create_by` bigint(20) NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`share_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分享' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_share_range
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_share_range`;
CREATE TABLE `eoffice_share_range`  (
  `srid` bigint(20) UNSIGNED NOT NULL,
  `share_id` bigint(20) NULL DEFAULT NULL,
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `type` int(4) NULL DEFAULT NULL COMMENT '类型 0 用户 1 分组',
  PRIMARY KEY (`srid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分享范围' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_storage
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_storage`;
CREATE TABLE `eoffice_storage`  (
  `sid` bigint(20) UNSIGNED NOT NULL COMMENT '存储ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `size` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件大小',
  `md5` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件MD5',
  `extension` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件扩展名',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '存储相对路径',
  `create_at` datetime NULL DEFAULT current_timestamp() COMMENT '创建时间',
  PRIMARY KEY (`sid`) USING BTREE,
  INDEX `idx_extension`(`extension`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档存储基础信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for eoffice_user
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_user`;
CREATE TABLE `eoffice_user`  (
  `uid` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `email` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户手机',
  `nickname` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `username` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户状态 0 可用 1禁用',
  `regip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '注册的IP',
  `create_at` datetime(1) NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人ID',
  `update_at` datetime(1) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  `auth_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '认证方式',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for eoffice_user_group
-- ----------------------------
DROP TABLE IF EXISTS `eoffice_user_group`;
CREATE TABLE `eoffice_user_group`  (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `gid` bigint(20) NULL DEFAULT NULL COMMENT '分组ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户分组关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spring_session
-- ----------------------------
DROP TABLE IF EXISTS `spring_session`;
CREATE TABLE `spring_session`  (
  `PRIMARY_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SESSION_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
  UNIQUE INDEX `SPRING_SESSION_IX1`(`SESSION_ID`) USING BTREE,
  INDEX `SPRING_SESSION_IX2`(`EXPIRY_TIME`) USING BTREE,
  INDEX `SPRING_SESSION_IX3`(`PRINCIPAL_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spring_session_attributes
-- ----------------------------
DROP TABLE IF EXISTS `spring_session_attributes`;
CREATE TABLE `spring_session_attributes`  (
  `SESSION_PRIMARY_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`) USING BTREE,
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
