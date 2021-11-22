# admin #
INSERT INTO `eoffice`.`eoffice_user` (`uid`, `email`, `phone`, `nickname`, `username`, `password`, `status`, `regip`,
                                      `create_at`, `create_by`, `update_at`, `update_by`, `auth_type`)
VALUES (1, 'admin@qq.com', '12345678901', '管理员', 'admin',
        '$2a$10$Ua0uQtOW2bJ1zk4MaWlFXeMVrEF4UMBm5XLmFLfhHZxEVRA2PJjPe', 1, '', '2021-10-31 15:58:55.0', 0, NULL, NULL,
        'DFAULT');

# config #

INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (16, 'pathmap', 'webp', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (15, 'pathmap', 'tiff', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (14, 'pathmap', 'tif', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (13, 'pathmap', 'svg', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (1, 'pathmap', 'png', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (11, 'pathmap', 'pjpeg', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (12, 'pathmap', 'pjp', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (17, 'pathmap', 'md', 'markdown', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (3, 'pathmap', 'jpg', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (2, 'pathmap', 'jpeg', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (10, 'pathmap', 'jfif', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (8, 'pathmap', 'ico', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (7, 'pathmap', 'gif', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (9, 'pathmap', 'cur', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (6, 'pathmap', 'bmp', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (5, 'pathmap', 'avif', 'image', NULL, NULL, NULL, NULL);
INSERT INTO `eoffice`.`eoffice_config` (`cid`, `module`, `ckey`, `cvalue`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES (4, 'pathmap', 'apng', 'image', NULL, NULL, NULL, NULL);
