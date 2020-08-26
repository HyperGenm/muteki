/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : muteki

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 20/08/2020 16:09:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典标识',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字典创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary_value`;
CREATE TABLE `data_dictionary_value`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增',
  `dictionary_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典编号',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `num` int(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '可以作为排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dictionary_code`(`dictionary_code`) USING BTREE,
  CONSTRAINT `data_dictionary_value_ibfk_1` FOREIGN KEY (`dictionary_code`) REFERENCES `data_dictionary` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_error`;
CREATE TABLE `sys_error`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类名',
  `method_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '方法名',
  `line_number` smallint(5) UNSIGNED NOT NULL DEFAULT 0 COMMENT '第几行',
  `content` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详情',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统异常表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` smallint(5) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能路径',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `contain_api` json NULL COMMENT '当前功能对应的api列表，多个用,隔开',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能类型;1:菜单,2:按钮',
  `super_flag` tinyint(2) NOT NULL DEFAULT 1 COMMENT '超级管理员专属功能,1:普通,2:vip',
  `external_flag` tinyint(2) NOT NULL DEFAULT 1 COMMENT '外部链接,1:内部,2:外部超链接',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` tinyint(2) NOT NULL DEFAULT 77 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'system', 'system', '系统管理', '[]', 1, 1, 1, 'el-icon-s-tools', 88, '后台的管理', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (2, 1, 'sysFunction', 'sysFunction', '功能管理', '[\"/pc/sysFunction/getPageList\"]', 1, 2, 1, 'el-icon-s-tools', 9, '管理后台的菜单、按钮等。对应api可以将权限精确到接口', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (3, 1, 'sysRole', 'sysRole', '角色管理', NULL, 1, 1, 1, 'el-icon-user-solid\r\n', 2, '管理后台系统的各项角色，以及角色所拥有的功能等', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (4, 1, 'sysUser', 'sysUser', '系统用户管理', NULL, 1, 1, 1, 'el-icon-user', 1, '管理后台的用户', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (5, 29, 'sysUserLog', 'sysUserLog', '系统日志管理', '[\"/pc/sysUserLog/getPageList\"]', 1, 2, 1, 'el-icon-s-order', 4, '记录系统用户的操作', '2019-05-13 15:30:57');
INSERT INTO `sys_function` VALUES (6, 3, 'sysRole_add', 'add', '新增', '[\"/pc/sysRole/add\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (7, 3, 'sysRole_update', 'update', '修改', '[\"/pc/sysRole/update\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (8, 3, 'sysRole_delete', 'delete', '删除', '[\"/pc/sysRole/delete\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (9, 3, 'sysRole_save', 'save', '修改功能', '[\"/pc/sysRole/updateRoleFunction\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (10, 3, 'sysRole_status', 'status', '修改角色状态(启用/禁用)', '[\"/pc/sysRole/updateStatus\"]', 2, 1, 1, '', 4, '', '2019-05-10 15:58:15');
INSERT INTO `sys_function` VALUES (11, 4, 'sysUser_add', 'add', '新增', '[\"/pc/sysUser/add\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (12, 4, 'sysUser_delete', 'delete', '删除', '[\"/pc/sysUser/delete\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (13, 4, 'sysUser_role', 'role', '修改角色', '[\"/pc/sysUser/updateRole\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (14, 4, 'sysUser_resetPwd', 'resetPwd', '重置密码', '[\"/pc/sysUser/resetPwd\"]', 2, 1, 1, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (15, 0, 'tools', 'tools', '常用工具', NULL, 1, 2, 1, 'el-icon-s-promotion', 99, '进一步封装element-ui的常用组件，约定大于配置思想', '2019-08-24 16:41:28');
INSERT INTO `sys_function` VALUES (16, 15, 'toolsUpload', 'upload', '图片上传', NULL, 1, 2, 1, 'el-icon-picture', 0, '进一步封装了图片上传，方便调用', '2019-08-24 16:41:49');
INSERT INTO `sys_function` VALUES (17, 15, 'richText', 'richText', '富文本', NULL, 1, 2, 1, 'el-icon-s-release', 1, '进一步封装了富文本，方便调用', '2019-09-04 16:21:09');
INSERT INTO `sys_function` VALUES (18, 29, 'userLog', 'userLog', '用户日志', '[\"/pc/userLog/getPageList\"]', 1, 2, 1, 'el-icon-s-order', 5, '用户日志', '2020-02-28 20:44:20');
INSERT INTO `sys_function` VALUES (19, 1, 'sysError', 'sysError', '系统异常', '[\"/pc/sysError/getPageList\"]', 1, 2, 1, 'el-icon-s-release', 7, '系统异常', '2020-02-28 20:44:20');
INSERT INTO `sys_function` VALUES (20, 4, 'sysUser_updatePhone', 'sysUser_updatePhone', '修改手机号', '[\"/pc/sysUser/updatePhone\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-06-01 11:41:41');
INSERT INTO `sys_function` VALUES (21, 4, 'sysUser_get', 'sysUser_get', '查看', '[\"/pc/sysUser/getPageList\", \"/pc/sysRole/getList\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-06-01 11:43:09');
INSERT INTO `sys_function` VALUES (22, 4, 'sysUser_updateStatus', 'sysUser_updateStatus', '改变状态', '[\"/pc/sysUser/updateStatus\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-06-01 11:49:47');
INSERT INTO `sys_function` VALUES (23, 3, 'sysRole_get', 'sysRole_get', '查看', '[\"/pc/sysFunction/getTree\", \"/pc/sysRole/getFunctionList\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-06-01 14:10:22');
INSERT INTO `sys_function` VALUES (24, 2, 'sysFunction_add', 'sysFunction_add', '新增', '[\"/pc/sysFunction/add\"]', 2, 2, 1, 'el-icon-info', 77, '', '2020-06-05 10:08:38');
INSERT INTO `sys_function` VALUES (25, 2, 'sysFunction_update', 'sysFunction_update', '修改', '[\"/pc/sysFunction/update\"]', 2, 2, 1, 'el-icon-info', 77, '', '2020-06-05 10:09:32');
INSERT INTO `sys_function` VALUES (26, 2, 'sysFunction_delete', 'sysFunction_delete', '删除', '[\"/pc/sysFunction/delete\"]', 2, 2, 1, 'el-icon-info', 77, '', '2020-06-05 10:10:15');
INSERT INTO `sys_function` VALUES (27, 1, 'sysFile', 'sysFile', '系统文件', '[\"/pc/sysFile/getLogFile\", \"/pc/sysFile/downLogFile\"]', 1, 2, 1, 'el-icon-info', 77, '', '2020-06-05 10:20:48');
INSERT INTO `sys_function` VALUES (28, 29, 'sysUserLoginLog', 'sysUserLoginLog', '系统用户登录日志', '[\"/pc/sysUserLoginLog/getPageList\"]', 1, 1, 1, 'el-icon-info', 77, '', '2020-08-18 11:03:22');
INSERT INTO `sys_function` VALUES (29, 1, 'logManage', 'logManage', '日志管理', NULL, 1, 1, 1, 'el-icon-info', 77, '', '2020-08-20 10:24:28');
INSERT INTO `sys_function` VALUES (30, 29, 'userLoginLog', 'userLoginLog', '用户登录日志', '[\"/pc/userLoginLog/getPageList\"]', 1, 1, 1, 'el-icon-info', 77, '', '2020-08-20 10:37:37');
INSERT INTO `sys_function` VALUES (31, 1, 'druid', 'http://localhost:8080/druid', '阿里druid', NULL, 1, 2, 2, 'el-icon-info', 77, '', '2020-08-20 11:38:02');
INSERT INTO `sys_function` VALUES (32, 1, 'user', 'user', 'web用户管理', NULL, 1, 1, 1, 'el-icon-info', 77, '', '2020-08-21 15:23:12');
INSERT INTO `sys_function` VALUES (33, 32, 'user_get', 'user_get', '查看', '[\"/pc/user/getPageList\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-08-21 15:24:45');
INSERT INTO `sys_function` VALUES (34, 32, 'user_disable', 'user_disable', '禁用', '[\"/pc/user/disableUser\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-08-21 15:58:26');
INSERT INTO `sys_function` VALUES (35, 32, 'user_enable', 'user_enable', '启用', '[\"/pc/user/enableUser\"]', 2, 1, 1, 'el-icon-info', 77, '', '2020-08-21 15:58:45');


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:禁用',
  `sort` tinyint(2) NOT NULL DEFAULT 77 COMMENT '排序，数字越小越靠前',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL COMMENT '角色创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, 0, '超级管理员，拥有至高无上的权力', '2020-05-30 16:28:33');
INSERT INTO `sys_role` VALUES (101, '管理员', 1, 0, '管理员', '2020-05-30 16:28:35');
INSERT INTO `sys_role` VALUES (102, '测试', 1, 77, '测试', '2020-06-01 14:38:23');

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` smallint(5) UNSIGNED NOT NULL COMMENT '角色表id',
  `function_id` smallint(5) UNSIGNED NOT NULL COMMENT '功能表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `function_id`(`function_id`) USING BTREE,
  CONSTRAINT `sys_role_function_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_function_ibfk_2` FOREIGN KEY (`function_id`) REFERENCES `sys_function` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 318 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (256, 101, 1);
INSERT INTO `sys_role_function` VALUES (257, 101, 4);
INSERT INTO `sys_role_function` VALUES (258, 101, 14);
INSERT INTO `sys_role_function` VALUES (259, 101, 13);
INSERT INTO `sys_role_function` VALUES (260, 101, 12);
INSERT INTO `sys_role_function` VALUES (261, 101, 11);
INSERT INTO `sys_role_function` VALUES (262, 101, 24);
INSERT INTO `sys_role_function` VALUES (263, 101, 23);
INSERT INTO `sys_role_function` VALUES (264, 101, 22);
INSERT INTO `sys_role_function` VALUES (265, 101, 3);
INSERT INTO `sys_role_function` VALUES (266, 101, 9);
INSERT INTO `sys_role_function` VALUES (267, 101, 8);
INSERT INTO `sys_role_function` VALUES (268, 101, 7);
INSERT INTO `sys_role_function` VALUES (269, 101, 6);
INSERT INTO `sys_role_function` VALUES (270, 101, 10);
INSERT INTO `sys_role_function` VALUES (271, 101, 25);
INSERT INTO `sys_role_function` VALUES (277, 101, 5);
INSERT INTO `sys_role_function` VALUES (278, 101, 20);
INSERT INTO `sys_role_function` VALUES (279, 101, 21);
INSERT INTO `sys_role_function` VALUES (280, 101, 2);
INSERT INTO `sys_role_function` VALUES (308, 102, 4);
INSERT INTO `sys_role_function` VALUES (309, 102, 14);
INSERT INTO `sys_role_function` VALUES (310, 102, 13);
INSERT INTO `sys_role_function` VALUES (311, 102, 12);
INSERT INTO `sys_role_function` VALUES (312, 102, 11);
INSERT INTO `sys_role_function` VALUES (313, 102, 24);
INSERT INTO `sys_role_function` VALUES (314, 102, 23);
INSERT INTO `sys_role_function` VALUES (315, 102, 22);
INSERT INTO `sys_role_function` VALUES (316, 102, 9);
INSERT INTO `sys_role_function` VALUES (317, 102, 1);
INSERT INTO `sys_role_function` VALUES (318, 102, 3);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `real_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `status` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:禁用',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '/logo.png' COMMENT '用户头像',
  `create_time` datetime(0) NOT NULL COMMENT '用户创建时间',
  `delete_time` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间,0:未删除，大于0:删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`, `delete_time`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `delete_time`(`delete_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superadmin', '2E46808F0FCF73680D81ACE81AB0DA01', 'superadmin', '18888888888', 1, '/user/icon/20200819/2DFA5F868735496880EFB26EEC9AD273.png', '2020-06-01 14:42:12', 0);
INSERT INTO `sys_user` VALUES (101, 'weiziplus', 'E07C3D1032C1C13CD618602EFA1E4903', 'weiziplus', '18888888888', 1, '/logo.png', '2020-06-01 11:40:01', 0);
INSERT INTO `sys_user` VALUES (102, 'wq', 'E5BD5EAD6928089FF2EF9223DE43736A', 'fgffffff', '18999898989', 2, '/logo.png', '2020-06-02 14:19:11', 20200817143226);
INSERT INTO `sys_user` VALUES (103, 'admin', 'E07C3D1032C1C13CD618602EFA1E4903', 'admin', '18888888888', 2, '/logo.png', '2020-06-03 14:44:38', 0);
INSERT INTO `sys_user` VALUES (104, 'test', '6A9966B0DC7FE2000D0735B65693B630', 'test', '15978983456', 2, '/logo.png', '2020-06-03 16:06:05', 0);
INSERT INTO `sys_user` VALUES (105, 'test1', '2E46808F0FCF73680D81ACE81AB0DA01', 'aa', '1', 1, '/logo.png', '2020-08-17 14:37:14', 0);
INSERT INTO `sys_user` VALUES (106, 'aa', '399B0034DA3514A8B048D7A3C48E0316', 'aa', 'a', 1, '/logo.png', '2020-08-18 09:15:22', 20200818091557);
-- ----------------------------
-- Table structure for sys_user_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_log`;
CREATE TABLE `sys_user_log`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户表id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方法名',
  `param` json NULL COMMENT '当前请求的参数',
  `type` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `result_code` smallint(5) NOT NULL DEFAULT 200 COMMENT '响应码',
  `result_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'success' COMMENT '提示信息',
  `time_consuming` mediumint(5) NOT NULL DEFAULT 0 COMMENT '请求耗时',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  CONSTRAINT `sys_user_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE `sys_user_login`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `login_province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登陆省份',
  `login_city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登陆城市',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `result_code` smallint(5) NOT NULL DEFAULT 0 COMMENT '响应码',
  `result_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应信息',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户表主键',
  `role_id` smallint(5) UNSIGNED NOT NULL COMMENT '角色表主键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (12, 101, 101);
INSERT INTO `sys_user_role` VALUES (13, 104, 101);
INSERT INTO `sys_user_role` VALUES (14, 104, 102);
INSERT INTO `sys_user_role` VALUES (15, 105, 101);
INSERT INTO `sys_user_role` VALUES (16, 106, 101);
INSERT INTO `sys_user_role` VALUES (20, 116, 101);
INSERT INTO `sys_user_role` VALUES (21, 116, 102);
INSERT INTO `sys_user_role` VALUES (22, 115, 102);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  `status` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:禁用,3:封号',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'web用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'weiziplus', 'B7966B0719AF24AD7CBE954DDFB32DC0', 'hyper muteki', 1, '2020-06-01 09:34:31');

-- ----------------------------
-- Table structure for t_user_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户表主键',
  `terminal` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '终端类型',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方法名',
  `param` json NULL COMMENT '当前请求的参数',
  `type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `result_code` smallint(5) NOT NULL DEFAULT 200 COMMENT '响应码',
  `result_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'success' COMMENT '提示信息',
  `time_consuming` mediumint(5) NOT NULL DEFAULT 0 COMMENT '请求耗时',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  CONSTRAINT `t_user_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'web用户日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user_login
-- ----------------------------
DROP TABLE IF EXISTS `t_user_login`;
CREATE TABLE `t_user_login`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `terminal` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '终端类型',
  `login_province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登陆省份',
  `login_city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登陆城市',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `result_code` smallint(5) NOT NULL DEFAULT 0 COMMENT '响应码',
  `result_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应信息',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
