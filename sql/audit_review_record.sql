CREATE TABLE IF NOT EXISTS `audit_review_record` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `record_id` varchar(36) NOT NULL COMMENT '审核记录ID',
  `audit_type` varchar(20) NOT NULL COMMENT '审核类型：project/company',
  `target_id` varchar(36) NOT NULL COMMENT '被审核对象ID',
  `action` varchar(20) NOT NULL COMMENT '审核动作：approve/reject',
  `from_status` varchar(30) NULL COMMENT '审核前状态',
  `to_status` varchar(30) NOT NULL COMMENT '审核后状态',
  `reason` text NULL COMMENT '审核意见或拒绝原因',
  `reviewer_id` varchar(36) NOT NULL COMMENT '审核管理员用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_id` (`record_id`),
  KEY `idx_audit_type_target` (`audit_type`, `target_id`),
  KEY `idx_action` (`action`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员审核历史记录';
