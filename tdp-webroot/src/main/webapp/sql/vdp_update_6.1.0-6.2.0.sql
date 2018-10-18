-- ----------------------------
-- Table structure for `t_child_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_child_node`;
CREATE TABLE `t_child_node` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`node_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级服务器节点名称' ,
`node_ip`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级服务器节点ip' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '子级节点创建时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for `t_scheduled_task`
-- ----------------------------
DROP TABLE IF EXISTS `t_scheduled_task`;
CREATE TABLE `t_scheduled_task` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`guid`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务guid' ,
`task_reference`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务所调用方法的引用' ,
`corn`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行的corn表达式' ,
`processing`  int(11) NULL DEFAULT NULL COMMENT '当前任务是否在执行中，0为未执行中，1为正在执行中' ,
`task_switch`  int(11) NULL DEFAULT NULL COMMENT '任务开关，当前任务是否执行，0为关闭不执行，1为打开需要执行' ,
`last_execution_time`  timestamp NULL DEFAULT NULL COMMENT '上一次执行任务的时间' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '任务创建时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Dynamic;

-- ----------------------------
-- Table structure for `t_video_transfer_log`
-- ----------------------------
ALTER TABLE `t_video_transfer_log` ADD COLUMN `guid`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志唯一标示' AFTER `id`;
ALTER TABLE `t_video_transfer_log` ADD COLUMN `node_level`  int(11) NULL DEFAULT NULL COMMENT '节点层级' AFTER `fftype`;
ALTER TABLE `t_video_transfer_log` ADD COLUMN `node_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级服务器节点名称' AFTER `node_level`;
ALTER TABLE `t_video_transfer_log` ADD COLUMN `node_ip`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级服务器节点ip' AFTER `node_name`;
ALTER TABLE `t_video_transfer_log` DROP COLUMN `watermarkMode`;

-- ----------------------------
-- Records of `t_video_transfer_log`
-- ----------------------------
UPDATE t_video_transfer_log
SET node_level = 0;

UPDATE t_video_transfer_log o,
 (
	SELECT
		UUID() uuid,
		id
	FROM
		t_video_transfer_log
) n
SET o.guid = n.uuid
WHERE o.id = n.id;

-- ----------------------------
-- Records of `t_video_transfer_log`
-- ----------------------------
INSERT INTO `t_navigation` VALUES ('20', '4', '级联日志', '/report/childIndex', '6', null, '2');











