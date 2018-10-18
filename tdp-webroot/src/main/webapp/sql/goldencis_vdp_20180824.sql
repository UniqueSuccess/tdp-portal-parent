/*
Navicat MySQL Data Transfer

Source Server         : 140
Source Server Version : 50713
Source Host           : 10.10.16.140:3306
Source Database       : goldencis_vdp

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2018-08-24 11:10:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_approve_definition`
-- ----------------------------
DROP TABLE IF EXISTS `t_approve_definition`;
CREATE TABLE `t_approve_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `name` varchar(200) NOT NULL COMMENT '审批流程名称',
  `is_default` int(11) DEFAULT NULL COMMENT '是否默认审批流程',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_approve_definition
-- ----------------------------
INSERT INTO `t_approve_definition` VALUES ('1', '默认', '1', '2018-01-29 22:37:39');

-- ----------------------------
-- Table structure for `t_approve_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_approve_detail`;
CREATE TABLE `t_approve_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '审批环节名称',
  `flow_id` int(11) NOT NULL COMMENT '所属审批流程id',
  `point_id` int(11) NOT NULL COMMENT '所属审批节点id',
  `senior_id` int(11) NOT NULL COMMENT '上一个环节的id，每个流程的起始节点该字段为0',
  `approver` varchar(36) NOT NULL COMMENT '该节点审批人guid',
  `result` int(10) DEFAULT NULL COMMENT '审批结果，-1为审批被驳回，0为审批进行中，1为审批通过，若null则未开始。',
  `remark` varchar(2000) DEFAULT NULL COMMENT '审批意见',
  `standard` int(10) NOT NULL COMMENT '是否标准环节',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_approve_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_approve_flow`
-- ----------------------------
DROP TABLE IF EXISTS `t_approve_flow`;
CREATE TABLE `t_approve_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '审批流程名称',
  `flow_id` int(11) NOT NULL COMMENT '所属审批流程id',
  `status` int(10) NOT NULL COMMENT '审批流程的执行状态，-1为审批被驳回，0为审批进行中，1为审批通过',
  `point_id` int(11) DEFAULT NULL COMMENT '审批流程执行的环节id',
  `type` int(10) DEFAULT NULL COMMENT '审批类型，1为文件导出。2为文件外发。',
  `tran_unique` varchar(36) DEFAULT NULL COMMENT '区分用户文件唯一标记',
  `applicant_id` varchar(36) DEFAULT NULL COMMENT '申请人guid',
  `applicant_name` varchar(200) DEFAULT NULL COMMENT '申请人姓名',
  `reason` varchar(2000) DEFAULT NULL COMMENT '申请原因',
  `apply_time` timestamp NULL DEFAULT NULL COMMENT '申请提交时间',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '审批终结时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_approve_flow
-- ----------------------------

-- ----------------------------
-- Table structure for `t_approve_flow_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_approve_flow_info`;
CREATE TABLE `t_approve_flow_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '所属审批流程id',
  `policy_param` varchar(2000) DEFAULT NULL COMMENT '流程使用的策略参数',
  `file_path` varchar(2000) DEFAULT NULL COMMENT '文件路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_approve_flow_info
-- ----------------------------

-- ----------------------------
-- Table structure for `t_approve_model`
-- ----------------------------
DROP TABLE IF EXISTS `t_approve_model`;
CREATE TABLE `t_approve_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) NOT NULL COMMENT '审批环节名称',
  `approvers` varchar(2000) NOT NULL COMMENT '审批人guid，多个时以";"隔开',
  `flow_id` int(11) NOT NULL COMMENT '所属审批流程id',
  `senior_id` int(11) NOT NULL COMMENT '上一个环节的id，每个流程的起始节点该字段为0',
  `standard` int(10) NOT NULL COMMENT '是否标准环节,1为标准环节，0为严格环节',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_approve_model
-- ----------------------------
INSERT INTO `t_approve_model` VALUES ('1', '默认节点', '3', '1', '0', '1', '2018-04-26 13:55:29');

-- ----------------------------
-- Table structure for `t_child_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_child_node`;
CREATE TABLE `t_child_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `node_name` varchar(200) DEFAULT NULL COMMENT '子级服务器节点名称',
  `node_ip` varchar(30) DEFAULT NULL COMMENT '子级服务器节点ip',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '子级节点创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_child_node
-- ----------------------------

-- ----------------------------
-- Table structure for `t_client_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_client_user`;
CREATE TABLE `t_client_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` char(36) NOT NULL COMMENT '用户唯一标识',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `truename` varchar(32) NOT NULL COMMENT '真实姓名',
  `deptguid` int(11) NOT NULL COMMENT '所属部门id',
  `computerguid` char(36) DEFAULT NULL COMMENT '计算机唯一标识',
  `computername` varchar(32) DEFAULT NULL COMMENT '计算机名',
  `ip` varchar(18) DEFAULT NULL COMMENT 'ip',
  `mac` varchar(50) DEFAULT NULL COMMENT '计算机mac地址',
  `regtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `policyid` int(11) DEFAULT '0' COMMENT '策略id',
  `isbinded_usbkey` int(11) NOT NULL COMMENT '是否绑定USB_KEY',
  `online` char(1) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `online_time` timestamp NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` timestamp NULL DEFAULT NULL COMMENT '离线时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_vdpusers_guid` (`guid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_client_user
-- ----------------------------

-- ----------------------------
-- Table structure for `t_department`
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级部门Id',
  `department_remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `owner` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门负责人',
  `department_tel` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '部门电话',
  `ip_part` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '所属IP段',
  `tree_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '路径',
  `status` int(11) unsigned DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='部门信息表';

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES ('1', '顶级部门', '-1', null, '', '', null, ',', '1');
INSERT INTO `t_department` VALUES ('2', '未分组', '1', null, '', '', null, ',1,', '1');

-- ----------------------------
-- Table structure for `t_deployment_status`
-- ----------------------------
DROP TABLE IF EXISTS `t_deployment_status`;
CREATE TABLE `t_deployment_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deployment_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '部署ID',
  `status` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='流程部署状态';

-- ----------------------------
-- Records of t_deployment_status
-- ----------------------------

-- ----------------------------
-- Table structure for `t_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '字典类型',
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '值',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `sort_by` int(50) DEFAULT NULL COMMENT '排序',
  `status` int(11) unsigned DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据字典';

-- ----------------------------
-- Records of t_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for `t_fingerprint`
-- ----------------------------
DROP TABLE IF EXISTS `t_fingerprint`;
CREATE TABLE `t_fingerprint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fingerprint_pwd` text NOT NULL COMMENT '指纹密码串',
  `client_user_guid` char(36) NOT NULL COMMENT '用户guid',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fingerprint
-- ----------------------------

-- ----------------------------
-- Table structure for `t_illegal_operation_alarm`
-- ----------------------------
DROP TABLE IF EXISTS `t_illegal_operation_alarm`;
CREATE TABLE `t_illegal_operation_alarm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `truename` varchar(200) NOT NULL COMMENT '用户真实姓名',
  `username` varchar(200) NOT NULL COMMENT '用户名',
  `userguid` varchar(36) DEFAULT NULL COMMENT '用户唯一识别',
  `department_id` int(11) DEFAULT NULL COMMENT '部门id',
  `department_name` varchar(200) DEFAULT NULL COMMENT '部门名',
  `devunique` varchar(36) DEFAULT NULL COMMENT '设备id',
  `extradata` varchar(2000) DEFAULT NULL COMMENT '额外数据，JSON类型',
  `ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `receiver` varchar(2000) DEFAULT NULL COMMENT '接收方',
  `file_name` varchar(2000) DEFAULT NULL COMMENT '流转文件',
  `has_read` int(11) DEFAULT NULL COMMENT '是否已读，0代表未读，1代表已读',
  `warning_type` int(11) DEFAULT NULL COMMENT '报警类型：1为非法登录，2为非法导出，3为非法外发',
  `warning_time` timestamp NULL DEFAULT NULL COMMENT '报警时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_illegal_operation_alarm
-- ----------------------------

-- ----------------------------
-- Table structure for `t_navigation`
-- ----------------------------
DROP TABLE IF EXISTS `t_navigation`;
CREATE TABLE `t_navigation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `compositor` int(10) unsigned NOT NULL COMMENT '排序',
  `title` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '页签显示名称',
  `url` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '页签跳转链接',
  `parent_id` int(10) DEFAULT NULL COMMENT '父级页签Id',
  `icon_url` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '页签图标',
  `n_level` int(10) unsigned NOT NULL COMMENT '页签级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='页签-导航信息表';

-- ----------------------------
-- Records of t_navigation
-- ----------------------------
INSERT INTO `t_navigation` VALUES ('1', '1', '首页', '/homepage/index?navId=1', null, 'nav_home', '1');
INSERT INTO `t_navigation` VALUES ('2', '2', '用户', '', null, 'nav_user', '1');
INSERT INTO `t_navigation` VALUES ('3', '3', '准入', '/access/index?navId=1', null, 'nav_check', '1');
INSERT INTO `t_navigation` VALUES ('4', '4', '策略', '', null, 'nav_policy', '1');
INSERT INTO `t_navigation` VALUES ('5', '5', '审批', '', null, 'nav_approve', '1');
INSERT INTO `t_navigation` VALUES ('6', '6', '日志', '', null, 'nav_report', '1');
INSERT INTO `t_navigation` VALUES ('7', '7', '系统', '', null, 'nav_system', '1');
INSERT INTO `t_navigation` VALUES ('8', '4', '关于', '/about/index?navId=1', '7', '', '2');
INSERT INTO `t_navigation` VALUES ('9', '1', '用户管理', '/clientUser/index', '2', null, '2');
INSERT INTO `t_navigation` VALUES ('10', '2', '部门管理', '/department/index', '2', null, '2');
INSERT INTO `t_navigation` VALUES ('11', '1', '视频平台', '/systemClient/index', '7', '', '2');
INSERT INTO `t_navigation` VALUES ('13', '2', '系统日志', '/systemLog/index', '6', null, '2');
INSERT INTO `t_navigation` VALUES ('14', '3', '设置', '/systemSetting/index', '7', null, '2');
INSERT INTO `t_navigation` VALUES ('15', '2', '审批流程', '/approveFlow/index', '5', null, '2');
INSERT INTO `t_navigation` VALUES ('16', '1', '审批请求', '/approveDefinition/index', '5', null, '2');
INSERT INTO `t_navigation` VALUES ('17', '1', '视频流转', '/report/index', '6', null, '2');
INSERT INTO `t_navigation` VALUES ('18', '2', 'UKey库', '/usbKey/index', '7', null, '2');
INSERT INTO `t_navigation` VALUES ('20', '4', '级联日志', '/report/childIndex', '6', null, '2');

-- ----------------------------
-- Table structure for `t_operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `ip` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '管理员IP',
  `user_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '管理员username',
  `log_type` int(11) DEFAULT NULL COMMENT '日志类型',
  `log_page` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对应页面',
  `log_operate_param` text COLLATE utf8_unicode_ci COMMENT '请求参数',
  `log_desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '权限名称',
  `visible` int(1) DEFAULT '1' COMMENT '是否可见',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限信息表';

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '默认角色', '1', null);

-- ----------------------------
-- Table structure for `t_permission_navigation`
-- ----------------------------
DROP TABLE IF EXISTS `t_permission_navigation`;
CREATE TABLE `t_permission_navigation` (
  `permission_id` int(10) unsigned NOT NULL COMMENT '权限Id',
  `navigation_id` int(10) unsigned NOT NULL COMMENT '权限关联导航Id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限与页签关联关系';

-- ----------------------------
-- Records of t_permission_navigation
-- ----------------------------
INSERT INTO `t_permission_navigation` VALUES ('1', '1');
INSERT INTO `t_permission_navigation` VALUES ('1', '3');
INSERT INTO `t_permission_navigation` VALUES ('1', '4');
INSERT INTO `t_permission_navigation` VALUES ('1', '5');
INSERT INTO `t_permission_navigation` VALUES ('1', '15');
INSERT INTO `t_permission_navigation` VALUES ('1', '7');
INSERT INTO `t_permission_navigation` VALUES ('1', '11');
INSERT INTO `t_permission_navigation` VALUES ('1', '12');
INSERT INTO `t_permission_navigation` VALUES ('1', '14');
INSERT INTO `t_permission_navigation` VALUES ('1', '8');
INSERT INTO `t_permission_navigation` VALUES ('2', '16');
INSERT INTO `t_permission_navigation` VALUES ('2', '15');
INSERT INTO `t_permission_navigation` VALUES ('2', '5');
INSERT INTO `t_permission_navigation` VALUES ('2', '10');
INSERT INTO `t_permission_navigation` VALUES ('2', '9');
INSERT INTO `t_permission_navigation` VALUES ('2', '2');
INSERT INTO `t_permission_navigation` VALUES ('2', '1');
INSERT INTO `t_permission_navigation` VALUES ('3', '6');
INSERT INTO `t_permission_navigation` VALUES ('3', '13');
INSERT INTO `t_permission_navigation` VALUES ('3', '7');
INSERT INTO `t_permission_navigation` VALUES ('3', '1');

-- ----------------------------
-- Table structure for `t_policy`
-- ----------------------------
DROP TABLE IF EXISTS `t_policy`;
CREATE TABLE `t_policy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '策略名',
  `path` varchar(2000) DEFAULT NULL COMMENT '策略所在路径',
  `default_id` int(11) DEFAULT NULL COMMENT '是否是默认策略',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_policy
-- ----------------------------
INSERT INTO `t_policy` VALUES ('1', '默认', '/resource/policy/1/bdppolicy.json', '1', '2018-08-13 15:37:20');

-- ----------------------------
-- Table structure for `t_policy_potential_risk`
-- ----------------------------
DROP TABLE IF EXISTS `t_policy_potential_risk`;
CREATE TABLE `t_policy_potential_risk` (
  `risk_id` int(11) NOT NULL COMMENT '风险id',
  `policy_id` int(11) NOT NULL COMMENT '策略id',
  PRIMARY KEY (`risk_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_policy_potential_risk
-- ----------------------------

-- ----------------------------
-- Table structure for `t_prompt_refuse`
-- ----------------------------
DROP TABLE IF EXISTS `t_prompt_refuse`;
CREATE TABLE `t_prompt_refuse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='提示白名单用户表';

-- ----------------------------
-- Records of t_prompt_refuse
-- ----------------------------

-- ----------------------------
-- Table structure for `t_scheduled_task`
-- ----------------------------
DROP TABLE IF EXISTS `t_scheduled_task`;
CREATE TABLE `t_scheduled_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(36) DEFAULT NULL COMMENT '定时任务guid',
  `task_reference` varchar(500) DEFAULT NULL COMMENT '定时任务所调用方法的引用',
  `corn` varchar(100) DEFAULT NULL COMMENT '执行的corn表达式',
  `processing` int(11) DEFAULT NULL COMMENT '当前任务是否在执行中，0为未执行中，1为正在执行中',
  `task_switch` int(11) DEFAULT NULL COMMENT '任务开关，当前任务是否执行，0为关闭不执行，1为打开需要执行',
  `last_execution_time` timestamp NULL DEFAULT NULL COMMENT '上一次执行任务的时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '任务创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_scheduled_task
-- ----------------------------
INSERT INTO `t_scheduled_task` VALUES ('1', '9dd0f0a0-75fb-4b64-bfd3-dbef525d7015', 'cn.goldencis.tdp.report.service.impl.VideoTransferLogServiceImpl.collectVideoTransferLogsWithAttachment', '0 30 11 * * ?', '0', '1', '2018-08-24 10:00:00', '2018-08-07 16:21:15');

-- ----------------------------
-- Table structure for `t_scrnwatermark_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_scrnwatermark_log`;
CREATE TABLE `t_scrnwatermark_log` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `scrnwatermark_id` varchar(50) DEFAULT NULL COMMENT '水印id',
  `auth_id` varchar(36) NOT NULL COMMENT '设备id',
  `applicant_id` varchar(36) NOT NULL COMMENT '申请人guid',
  `applicant_name` varchar(200) DEFAULT NULL COMMENT '申请人姓名',
  `apply_info` varchar(2000) DEFAULT NULL COMMENT '审批信息',
  `apply_time` timestamp NULL DEFAULT NULL COMMENT '申请提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_scrnwatermark_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `t_sequence`;
CREATE TABLE `t_sequence` (
  `NAME` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sequence
-- ----------------------------
INSERT INTO `t_sequence` VALUES ('GroupSeq', '11', '1');

-- ----------------------------
-- Table structure for `t_system_validate`
-- ----------------------------
DROP TABLE IF EXISTS `t_system_validate`;
CREATE TABLE `t_system_validate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `system_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '系统id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统验证表,接口认证';

-- ----------------------------
-- Records of t_system_validate
-- ----------------------------
INSERT INTO `t_system_validate` VALUES ('1', 'system', 'F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751', 'vdpNB');

-- ----------------------------
-- Table structure for `t_usbkey`
-- ----------------------------
DROP TABLE IF EXISTS `t_usbkey`;
CREATE TABLE `t_usbkey` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '昵称',
  `keysn` varchar(36) DEFAULT NULL COMMENT '设备标识',
  `keynum` varchar(36) DEFAULT NULL COMMENT '唯一标识',
  `regtime` timestamp NULL DEFAULT NULL COMMENT '注册时间',
  `userguid` varchar(36) DEFAULT NULL COMMENT '绑定的用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_usbkey
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `guid` varchar(36) NOT NULL COMMENT '唯一标示',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `department` int(11) DEFAULT NULL COMMENT '所属部门',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `sex` int(11) DEFAULT '0' COMMENT '性别',
  `visible` int(11) DEFAULT '0' COMMENT '是否可见',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(15) NOT NULL COMMENT '电话号码',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `status` int(10) unsigned DEFAULT '1' COMMENT '管理员状态',
  `role_type` int(11) NOT NULL DEFAULT '0' COMMENT '管理员角色类型0管理员、1操作员、2审计员',
  `readonly` int(11) NOT NULL DEFAULT '0' COMMENT '策略只读',
  `skin` varchar(15) DEFAULT 'blue',
  `error_login_count` int(3) DEFAULT '0' COMMENT '错误登录次数',
  `error_login_last_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最近错误登录时间',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户信息表-定义用户基本信息';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '1', 'SYSTEM', 'F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751', '0', '超级管理员', '0', '0', '', '', '', '11', '0', '0', 'black', '0', '2018-04-26 17:00:06', '2018-04-26 17:00:06');
INSERT INTO `t_user` VALUES ('2', '2', 'ADMIN', 'F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751', '0', '管理员', '0', '0', '', '', '', '11', '1', '0', 'blue', '0', '2018-02-05 13:18:40', '2018-02-05 13:18:40');
INSERT INTO `t_user` VALUES ('3', '3', 'OPERATOR', 'F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751', '0', '操作员', '0', '0', '', '', '', '11', '2', '0', 'blue', '0', '2018-02-05 13:18:40', '2018-02-05 13:18:40');
INSERT INTO `t_user` VALUES ('4', '4', 'AUDITOR', 'F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751', '0', '审计员', '0', '0', '', '', '', '11', '3', '0', 'blue', '0', '2018-02-05 13:18:41', '2018-02-05 13:18:41');

-- ----------------------------
-- Table structure for `t_user_department`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_department`;
CREATE TABLE `t_user_department` (
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `department_id` int(10) unsigned NOT NULL COMMENT '部门Id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户部门关联';

-- ----------------------------
-- Records of t_user_department
-- ----------------------------
INSERT INTO `t_user_department` VALUES ('3', '1');
INSERT INTO `t_user_department` VALUES ('3', '2');
INSERT INTO `t_user_department` VALUES ('2', '1');
INSERT INTO `t_user_department` VALUES ('2', '2');
INSERT INTO `t_user_department` VALUES ('1', '1');
INSERT INTO `t_user_department` VALUES ('1', '2');
INSERT INTO `t_user_department` VALUES ('4', '1');
INSERT INTO `t_user_department` VALUES ('4', '2');

-- ----------------------------
-- Table structure for `t_user_navigation`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_navigation`;
CREATE TABLE `t_user_navigation` (
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `navigation_id` int(10) unsigned NOT NULL COMMENT '权限关联导航Id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与页签关联关系';

-- ----------------------------
-- Records of t_user_navigation
-- ----------------------------
INSERT INTO `t_user_navigation` VALUES ('3', '1');
INSERT INTO `t_user_navigation` VALUES ('3', '2');
INSERT INTO `t_user_navigation` VALUES ('3', '9');
INSERT INTO `t_user_navigation` VALUES ('3', '10');
INSERT INTO `t_user_navigation` VALUES ('3', '5');
INSERT INTO `t_user_navigation` VALUES ('3', '15');
INSERT INTO `t_user_navigation` VALUES ('3', '16');
INSERT INTO `t_user_navigation` VALUES ('2', '1');
INSERT INTO `t_user_navigation` VALUES ('2', '3');
INSERT INTO `t_user_navigation` VALUES ('2', '4');
INSERT INTO `t_user_navigation` VALUES ('2', '5');
INSERT INTO `t_user_navigation` VALUES ('2', '15');
INSERT INTO `t_user_navigation` VALUES ('2', '7');
INSERT INTO `t_user_navigation` VALUES ('2', '11');
INSERT INTO `t_user_navigation` VALUES ('2', '12');
INSERT INTO `t_user_navigation` VALUES ('2', '14');
INSERT INTO `t_user_navigation` VALUES ('2', '8');
INSERT INTO `t_user_navigation` VALUES ('1', '1');
INSERT INTO `t_user_navigation` VALUES ('1', '2');
INSERT INTO `t_user_navigation` VALUES ('1', '9');
INSERT INTO `t_user_navigation` VALUES ('1', '10');
INSERT INTO `t_user_navigation` VALUES ('1', '3');
INSERT INTO `t_user_navigation` VALUES ('1', '4');
INSERT INTO `t_user_navigation` VALUES ('1', '5');
INSERT INTO `t_user_navigation` VALUES ('1', '15');
INSERT INTO `t_user_navigation` VALUES ('1', '16');
INSERT INTO `t_user_navigation` VALUES ('1', '6');
INSERT INTO `t_user_navigation` VALUES ('1', '7');
INSERT INTO `t_user_navigation` VALUES ('1', '11');
INSERT INTO `t_user_navigation` VALUES ('1', '12');
INSERT INTO `t_user_navigation` VALUES ('1', '13');
INSERT INTO `t_user_navigation` VALUES ('1', '14');
INSERT INTO `t_user_navigation` VALUES ('1', '8');
INSERT INTO `t_user_navigation` VALUES ('4', '1');
INSERT INTO `t_user_navigation` VALUES ('4', '6');
INSERT INTO `t_user_navigation` VALUES ('4', '7');
INSERT INTO `t_user_navigation` VALUES ('4', '13');

-- ----------------------------
-- Table structure for `t_video_transfer_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_video_transfer_log`;
CREATE TABLE `t_video_transfer_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(36) DEFAULT NULL COMMENT '日志唯一标示',
  `truename` varchar(200) NOT NULL COMMENT '用户真实姓名',
  `username` varchar(200) NOT NULL COMMENT '用户名',
  `userguid` varchar(36) DEFAULT NULL COMMENT '用户唯一识别',
  `department_id` int(11) DEFAULT NULL COMMENT '部门id',
  `department_name` varchar(200) DEFAULT NULL COMMENT '部门名',
  `devunique` varchar(36) DEFAULT NULL COMMENT '设备id',
  `extradata` varchar(2000) DEFAULT NULL COMMENT '额外数据，JSON类型',
  `file_path` varchar(2000) DEFAULT NULL COMMENT '文件路径',
  `file_name` varchar(2000) DEFAULT NULL COMMENT '文件名称',
  `receiver` varchar(2000) DEFAULT NULL COMMENT '接收方',
  `transfer_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发送时间',
  `tranunique` varchar(36) DEFAULT NULL COMMENT '文件唯一标示',
  `fftype` int(11) DEFAULT NULL COMMENT '文件流转类型：1表示自主导出，3表示审批导出，10表示自主外发，11表示审批外发',
  `node_level` int(11) DEFAULT NULL COMMENT '节点层级',
  `node_name` varchar(255) DEFAULT NULL COMMENT '子级服务器节点名称',
  `node_ip` varchar(15) DEFAULT NULL COMMENT '子级服务器节点ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_video_transfer_log
-- ----------------------------
