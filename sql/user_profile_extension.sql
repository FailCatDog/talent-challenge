-- 用户资料扩展 - 三表方案
-- 执行日期: 2026-03-09

-- 学生资料扩展表
CREATE TABLE `student_profile` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(36) NOT NULL COMMENT '关联user表的user_id',
  `real_name` varchar(50) NULL COMMENT '真实姓名',
  `college` varchar(100) NULL COMMENT '学院',
  `major` varchar(100) NULL COMMENT '专业',
  `grade` varchar(20) NULL COMMENT '年级',
  `student_id` varchar(50) NULL COMMENT '学号',
  `email` varchar(100) NULL COMMENT '邮箱',
  `phone` varchar(20) NULL COMMENT '联系方式',
  `tech_tags` json NULL COMMENT '技术标签',
  `tech_intro` text NULL COMMENT '个人技术介绍',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生资料扩展表';

-- 企业资料扩展表
CREATE TABLE `company_profile` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(36) NOT NULL COMMENT '关联user表的user_id',
  `company_intro` text NULL COMMENT '企业介绍',
  `company_tags` json NULL COMMENT '企业标签',
  `credit_code` varchar(50) NULL COMMENT '统一社会信用代码',
  `company_industry` varchar(100) NULL COMMENT '行业领域',
  `company_scale` varchar(50) NULL COMMENT '企业规模',
  `company_region` varchar(100) NULL COMMENT '所在地区',
  `company_website` varchar(255) NULL COMMENT '官方网址',
  `contact_person` varchar(50) NULL COMMENT '主要联系人',
  `contact_position` varchar(50) NULL COMMENT '联系人职务',
  `contact_phone` varchar(20) NULL COMMENT '联系电话',
  `contact_email` varchar(100) NULL COMMENT '联系邮箱',
  `qualification_files` json NULL COMMENT '资质文件URL数组',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_credit_code` (`credit_code`),
  KEY `idx_contact_email` (`contact_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业资料扩展表';
