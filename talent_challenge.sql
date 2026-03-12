/*
 Navicat Premium Dump SQL

 Source Server         : 腾讯云MySQL
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44-0ubuntu0.24.04.2)
 Source Host           : 43.142.157.145:3306
 Source Schema         : talent_challenge

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44-0ubuntu0.24.04.2)
 File Encoding         : 65001

 Date: 12/03/2026 18:23:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bid
-- ----------------------------
DROP TABLE IF EXISTS `bid`;
CREATE TABLE `bid`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键，内部使用',
  `bid_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞榜记录全局唯一标识',
  `project_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联项目的全局唯一标识',
  `team_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请人（团队）全局唯一标识',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'applied' COMMENT '竞榜状态：applied, shortlisted, confirmed, rejected, cancelled, expired',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '申请说明/自荐内容',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件（作品集、简历）链接',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '企业审核备注或拒绝理由',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bid_uuid`(`bid_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_bid_project_student`(`project_id` ASC, `team_id` ASC) USING BTREE,
  INDEX `idx_bid_project_status`(`project_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_bid_student_status`(`team_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目竞榜申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bid
-- ----------------------------
INSERT INTO `bid` VALUES (1, '2021456663671869440', '2021453479545016320', '2020476698205753344', 'shortlisted', '擅长Java后端与MySQL调优，近期完成过校园实验室排班系统。', 'https://resume.example.com/student-001.pdf', NULL, '2026-02-11 13:30:00', '2026-02-11 13:32:40', 1);
INSERT INTO `bid` VALUES (2, '2022959748642639872', '2022959380340805632', '2020476698205753344', 'applied', '擅长Java后端与MySQL调优，近期完成过校园实验室排班系统。', 'https://resume.example.com/student-001.pdf', NULL, '2026-02-15 17:02:44', '2026-02-15 17:02:44', 0);
INSERT INTO `bid` VALUES (3, '2022960911228211200', '2022960804281847808', '2020476698205753344', 'applied', '擅长Java后端与MySQL调优，近期完成过校园实验室排班系统。', 'https://resume.example.com/student-001.pdf', NULL, '2026-02-15 17:07:21', '2026-02-15 17:07:21', 0);
INSERT INTO `bid` VALUES (4, '2023249295125385216', '2023249119094640640', '2020476698205753344', 'confirmed', '擅长Java后端与MySQL调优，近期完成过校园实验室排班系统。', 'https://resume.example.com/student-001.pdf', NULL, '2026-02-16 12:13:17', '2026-02-17 16:58:05', 2);

-- ----------------------------
-- Table structure for company_profile
-- ----------------------------
DROP TABLE IF EXISTS `company_profile`;
CREATE TABLE `company_profile`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联user表的user_id',
  `company_intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '企业介绍',
  `company_tags` json NULL COMMENT '企业标签',
  `credit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `company_industry` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行业领域',
  `company_scale` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业规模',
  `company_region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所在地区',
  `company_website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '官方网址',
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主要联系人',
  `contact_position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系人职务',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `qualification_files` json NULL COMMENT '资质文件URL数组',
  `certification_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'uncertified' COMMENT '认证状态:\n  uncertified/pending_review/certified/rejected',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `certified_at` datetime NULL DEFAULT NULL COMMENT '认证通过时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_credit_code`(`credit_code` ASC) USING BTREE,
  INDEX `idx_contact_email`(`contact_email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '企业资料扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_profile
-- ----------------------------

-- ----------------------------
-- Table structure for milestone_template
-- ----------------------------
DROP TABLE IF EXISTS `milestone_template`;
CREATE TABLE `milestone_template`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `template_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板唯一标识',
  `milestone_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '里程碑描述',
  `milestone_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑类型：required(必须), optional(可选)',
  `default_sequence_order` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '默认顺序',
  `default_approval_rule` json NULL COMMENT '默认审批规则配置',
  `default_remind_before_hours` int NULL DEFAULT 24 COMMENT '默认提醒提前小时数',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_template_id`(`template_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_milestone_code`(`milestone_code` ASC) USING BTREE,
  INDEX `idx_milestone_type`(`milestone_type` ASC) USING BTREE,
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '里程碑模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of milestone_template
-- ----------------------------
INSERT INTO `milestone_template` VALUES (1, '2000893880307945472', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 'required', 1, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 72, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');
INSERT INTO `milestone_template` VALUES (2, '2000893880307945473', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 'required', 3, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 48, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');
INSERT INTO `milestone_template` VALUES (3, '2000893880307945474', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 'required', 5, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 24, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');
INSERT INTO `milestone_template` VALUES (4, '2000893880307945475', 'mid_term_defense', '中期答辩', '安排中期答辩、准备材料并确认实际时间', 'optional', 2, '{\"reviewTy pe\": \"defense\", \"approverRoles\": [\"academic\"]}', 48, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');
INSERT INTO `milestone_template` VALUES (5, '2000893880307945476', 'result_review', '成果评审', '专家组评审成果并填写审批意见', 'optional', 4, '{\"auditLevel\": \"committee\", \"approverRoles\": [\"review_committee\"]}', 24, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');
INSERT INTO `milestone_template` VALUES (6, '2000893880307945477', 'closure_announcement', '结项公示', '发布结项公示、归档成果与通知相关角色', 'optional', 6, '{\"approverRoles\": [\"platform\"], \"notifyChannels\": [\"homepage\"]}', 24, 1, '2025-12-16 19:40:50', '2025-12-16 19:40:50');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '项目唯一主键（内部使用）',
  `project_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目全局唯一标识符（对外暴露）',
  `serial_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目流水号，格式：PRJ{年月日}{6位随机数}，便于线下沟通',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目详细描述',
  `category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '项目分类逻辑ID（关联分类表）',
  `tags` json NULL COMMENT '项目技术标签，JSON数组格式，e.g: [\"Java\", \"Spring Boot\", \"Vue.js\"]',
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '具体技术要求和技能需求',
  `skill_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'intermediate' COMMENT '所需技能水平：beginner, intermediate, expert',
  `team_size_min` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '最低团队人数要求',
  `team_size_max` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '最高团队人数要求',
  `duration_days` int NULL DEFAULT NULL COMMENT '预计完成所需天数',
  `budget_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'fixed' COMMENT '预算类型：fixed, hourly, negotiable',
  `budget_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '预算金额（对于固定总价和按时计费）',
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'CNY' COMMENT '货币代码，默认CNY（人民币）',
  `published_at` datetime NULL DEFAULT NULL COMMENT '项目发布时间（审核通过后更新）',
  `application_deadline` datetime NULL DEFAULT NULL COMMENT '揭榜申请截止时间',
  `expected_start_date` date NULL DEFAULT NULL COMMENT '期望开始日期',
  `expected_end_date` date NULL DEFAULT NULL COMMENT '期望结束日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'draft' COMMENT '项目状态：draft, pending_review, rejected, published, in_progress, completed, closed',
  `reject_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核驳回理由（如果状态为rejected）',
  `progress` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '项目完成进度百分比（0-100）',
  `publisher_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发布者逻辑ID（关联user表，是企业用户）',
  `accepted_team_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '中标团队逻辑ID（关联team表，揭榜成功后更新）',
  `application_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '揭榜申请人数',
  `view_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目浏览次数',
  `favorite_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目被收藏次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号，用于并发控制',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_project_uuid`(`project_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_project_serial_number`(`serial_number` ASC) USING BTREE,
  INDEX `idx_project_publisher_id`(`publisher_id` ASC) USING BTREE,
  INDEX `idx_project_status`(`status` ASC) USING BTREE,
  INDEX `idx_project_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_project_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_project_published_at`(`published_at` ASC) USING BTREE,
  INDEX `idx_project_application_deadline`(`application_deadline` ASC) USING BTREE,
  INDEX `idx_project_status_published_at`(`status` ASC, `published_at` ASC) USING BTREE,
  INDEX `idx_project_category_status`(`category_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, '2019741230241550336', 'PRJ20260206000028', '电商平台前端开发', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-06 19:53:29', '2026-02-06 19:53:29', 0);
INSERT INTO `project` VALUES (2, '2020474556745125888', 'PRJ20260208106447', '保存项目草稿测试1', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-08 20:27:28', '2026-02-08 20:27:28', 0);
INSERT INTO `project` VALUES (3, '2021192607220240384', 'PRJ20260210989059', '里程碑配置测试2', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-10 20:00:44', '2026-02-10 20:00:44', 0);
INSERT INTO `project` VALUES (4, '2021450294445084672', 'PRJ20260211060715', '里程碑配置测试3', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-11 13:04:42', '2026-02-11 13:04:42', 0);
INSERT INTO `project` VALUES (5, '2021451590602461184', 'PRJ20260211515113', '里程碑配置测试4', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-11 13:09:51', '2026-02-11 13:09:51', 0);
INSERT INTO `project` VALUES (6, '2021453479545016320', 'PRJ20260211029037', '里程碑配置测试5', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 1, 0, 0, '2026-02-11 13:17:21', '2026-02-11 13:30:00', 0);
INSERT INTO `project` VALUES (7, '2021793723754614784', 'PRJ20260212042391', '保存项目草稿测试333', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-12 11:49:22', '2026-02-12 11:49:22', 0);
INSERT INTO `project` VALUES (8, '2021800217111826432', 'PRJ20260212094896', '保存项目草稿测试334443', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-12 12:15:10', '2026-02-12 12:15:10', 0);
INSERT INTO `project` VALUES (9, '2021845090804305920', 'PRJ20260212428263', '保存项目草稿测试334443', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-02-12 15:13:28', '2026-02-12 15:13:28', 0);
INSERT INTO `project` VALUES (10, '2022959380340805632', 'PRJ20260215450132', '里程碑配置测试6-测试拒绝', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 1, 0, 0, '2026-02-15 17:01:16', '2026-02-15 17:02:43', 0);
INSERT INTO `project` VALUES (11, '2022960804281847808', 'PRJ20260215292027', '里程碑配置测试7-测试拒绝2', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 1, 2, 0, '2026-02-15 17:06:55', '2026-03-03 23:32:31', 0);
INSERT INTO `project` VALUES (12, '2023249119094640640', 'PRJ20260216293856', '里程碑配置测试7-测试拒绝3', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', '2025-10-19 16:00:00', '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'in_progress', NULL, 0, '2018608172406804480', '2020476698205753344', 1, 22, 0, '2026-02-16 12:12:35', '2026-03-12 17:58:07', 0);
INSERT INTO `project` VALUES (13, '2029219188194807808', 'PRJ20260304455623', '前端发布项目测试', '请详细描述项目信息，建议包含以下内容：\n项目背景：项目的需求来源和背景说明\n项目目标：预期达成的目标和成果\n成果形式：最终交付物的形式（源代码、文档、视频等）\n功能要求：详细的功能需求和技术规范\n技术栈要求：所需的技术栈（如Vue3、Spring Boot等）\n性能指标：响应时间、并发数、可用性等要求\n：联系方式：项目联系人及联系电话', '人工智能', '[]', '要求要求要求要求要求要求要求要求要求要求', 'intermediate', 2, 4, 60, 'fixed', 5000.00, 'CNY', NULL, '2026-03-20 00:00:00', '2026-03-20', '2026-05-19', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-03-04 23:35:30', '2026-03-05 13:53:16', 0);
INSERT INTO `project` VALUES (14, '2029437312420679680', 'PRJ20260305355836', '前端发布测试2', '请详细描述项目信息，建议包含以下内容：\n·项目背景：项目的需求来源和背景说明\n·项目目标：预期达成的目标和成果\n：成果形式：最终交付物的形式（源代码、文档、视频等）\n·功能要求：详细的功能需求和技术规范\n·技术栈要求：所需的技术栈（如Vue3、SpringBoot等）\n·性能指标：响应时间、并发数、可用性等要求\n·联系方式：项目联系人及联系电话', '人工智能', '[]', '要求要求要求要求要求要求要求', 'intermediate', 2, 4, 60, 'fixed', 5000.00, 'CNY', NULL, '2026-03-13 00:00:00', '2026-03-13', '2026-05-12', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 3, 0, '2026-03-05 14:02:15', '2026-03-05 17:24:56', 0);
INSERT INTO `project` VALUES (15, '2029439365637345280', 'PRJ20260305608412', '前端发布测试3', '任务详情\n\n创客任务背景：\n        根据工作安排和进展情况，近期拟采购面向GEO的空间博弈仿真推演，主要内容包括两种场景的空间博弈仿真推演，包括一对一在轨追逃博弈、多对多集群博弈，其中多对多集群博弈仿真涉及双方飞行器总数不少于9个。推演过程需要素齐全，方案合理，可支撑开展空间博弈场景研究，重点推演微小卫星在GEO空间博弈中的应用模式、关键技术和性能需求。\n任务指标要求：\n1.空间博弈仿真推演需求\n1）仿真推演功能模块\n围绕上述两种场景，基于国产化航天仿真平台软件如ATK、SpaceSim等，开发符合功能需求的仿真推演功能模块，具体包括以下功能需求：\n①场景编辑功能：支持用户对空间博弈场景进行编辑，包括博弈双方飞行器模型参数、部署位置、轨道参数、任务规则等的配置；\n②仿真运行控制功能：提供仿真运行的控制功能，如启动、暂停、重启、快进、慢回放等；\n③可视化展示功能：支持二维和三维视景展示，能够直观显示飞行器的运动轨迹、态势变化等；\n④数据管理功能：实现对飞行器数据、推演数据的存储、查询、分析和可视化；\n⑤规则配置功能：支持对博弈规则的配置，包括胜负裁决规则、资源分配规则、环境规则等；\n⑥仿真评估功能：支持定性、定量的对飞行器的相关指标进行分析评估，以图、表形式展现评估结果。\n2）仿真推演规则研究\n围绕上述两种场景，研究场景中的博弈方案、关键技术，研究空间博弈推演规则，确保推演过程的全面性和科学性。包括但不限于胜负规则、资源（任务时间）分配规则、环境规则等。\n3）仿真推演验证\n①数据收集：围绕上述两种场景，调研场景相关的飞行器。数据应包含飞行器的性能参数、部署位置、功能特性等。\n②参数定义：根据场景设定定义飞行器运行环境及参数，如轨道参数、姿态参数、飞行器探测范围参数等。\n③场景验证：基于上述两种场景，验证场景中的博弈方案设计是否合理、科学，并提出优化建议。\n4）数据分析与评价\n①推演数据集：提供结构化的仿真推演数据，包括博弈过程时间序列、资源消耗记录、飞行器状态变化等。\n②研究报告：包含规则研究、推演过程描述、关键节点分析和结果解读等。根据评价规则，对不同部署位置、初始状态、博弈策略、技术参数下仿真结果打分评价，获得最优参数方案并提供优化建议。\n2.成果指标要求\n1）仿真推演功能模块指标要求：\n①模块功能完整性：所有功能模块需按需求开发完成，确保场景编辑、仿真运行、可视化展示等功能正常使用；\n②模块运行效率：仿真支持至少10倍的快进播放；\n③模块可扩展性：模块设计需支持后续功能扩展，确保系统具有良好的可扩展性；\n④模块用户体验：模块设计需注重用户体验，确保操作直观、界面友好，降低用户使用门槛。\n2）研究报告：包含仿真需求分析、空间博弈推演规则研究、仿真推演验证、数据分析与评价等章节，报告字数不少于2万字。报告内容覆盖规则研究、场景相关飞行器概述、两种场景的推演过程与结果（推演过程描述、关键节点分析和结果解读）、数据分析等内容。\n3）推演数据集：不同场景的数据集2套，包括两种场景的空间博弈仿真推演，包括一对一在轨追逃博弈与多对多集群博弈，其中多对多集群博弈仿真涉及双方飞行器总数不少于9个，单个数据集覆盖完整任务周期，推演数据可进行可视化展示。数据集内容包括但不限于：收集场景相关飞行器数据，推演过程中产生的轨道数据、载荷性能、能源消耗等；场景要素齐全，方案合理，二维、三维视景展示可支撑演示片内容制作。\n3.质量标准\n1）结果符合合同约定的质量标准；\n2）在进行运行及验收过程中发生的故障和发现的隐患已被排除并得到甲方的认可；\n3）所有合同中规定的数据集、资料、技术文件已提交并得到甲方认可；\n4）成果在交由甲方使用之前需通过专家评审；\n成果形式：\n1.面向GEO的空间博弈推演研究报告\n2.面向GEO的空间博弈仿真推演数据集\n3.仿真推演功能模块\n4.仿真功能模块设计方案、使用说明等相关技术文档。\n征集方式：单人悬赏\n经费：39万元\n知识产权归属：以合同约定为准\n成果评审方式：专家线下评审\n联系人及电话：平台流程问题请联系孙老师/15522100850；技术指标问题请联系李老师/13811063368', '人工智能', '[]', '要求要求要求要求要求要求要求', 'intermediate', 2, 4, 60, 'fixed', 5000.00, 'CNY', NULL, '2026-03-06 00:00:00', '2026-03-06', '2026-05-05', 'published', NULL, 0, '2018608172406804480', NULL, 0, 7, 0, '2026-03-05 14:10:25', '2026-03-05 22:52:26', 0);
INSERT INTO `project` VALUES (16, '2029476588432789504', 'PRJ20260305862926', '保存项目草稿测试8', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'draft', NULL, 0, '2018608172406804480', NULL, 0, 3, 0, '2026-03-05 16:38:19', '2026-03-05 16:46:40', 0);
INSERT INTO `project` VALUES (17, '2029491113483702272', 'PRJ20260305014191', '面向GEO的空间博弈仿真推演（二次）', '任务详情\n\n创客任务背景：\n        根据工作安排和进展情况，近期拟采购面向GEO的空间博弈仿真推演，主要内容包括两种场景的空间博弈仿真推演，包括一对一在轨追逃博弈、多对多集群博弈，其中多对多集群博弈仿真涉及双方飞行器总数不少于9个。推演过程需要素齐全，方案合理，可支撑开展空间博弈场景研究，重点推演微小卫星在GEO空间博弈中的应用模式、关键技术和性能需求。\n任务指标要求：\n1.空间博弈仿真推演需求\n1）仿真推演功能模块\n围绕上述两种场景，基于国产化航天仿真平台软件如ATK、SpaceSim等，开发符合功能需求的仿真推演功能模块，具体包括以下功能需求：\n①场景编辑功能：支持用户对空间博弈场景进行编辑，包括博弈双方飞行器模型参数、部署位置、轨道参数、任务规则等的配置；\n②仿真运行控制功能：提供仿真运行的控制功能，如启动、暂停、重启、快进、慢回放等；\n③可视化展示功能：支持二维和三维视景展示，能够直观显示飞行器的运动轨迹、态势变化等；\n④数据管理功能：实现对飞行器数据、推演数据的存储、查询、分析和可视化；\n⑤规则配置功能：支持对博弈规则的配置，包括胜负裁决规则、资源分配规则、环境规则等；\n⑥仿真评估功能：支持定性、定量的对飞行器的相关指标进行分析评估，以图、表形式展现评估结果。\n2）仿真推演规则研究\n围绕上述两种场景，研究场景中的博弈方案、关键技术，研究空间博弈推演规则，确保推演过程的全面性和科学性。包括但不限于胜负规则、资源（任务时间）分配规则、环境规则等。\n3）仿真推演验证\n①数据收集：围绕上述两种场景，调研场景相关的飞行器。数据应包含飞行器的性能参数、部署位置、功能特性等。\n②参数定义：根据场景设定定义飞行器运行环境及参数，如轨道参数、姿态参数、飞行器探测范围参数等。\n③场景验证：基于上述两种场景，验证场景中的博弈方案设计是否合理、科学，并提出优化建议。\n4）数据分析与评价\n①推演数据集：提供结构化的仿真推演数据，包括博弈过程时间序列、资源消耗记录、飞行器状态变化等。\n②研究报告：包含规则研究、推演过程描述、关键节点分析和结果解读等。根据评价规则，对不同部署位置、初始状态、博弈策略、技术参数下仿真结果打分评价，获得最优参数方案并提供优化建议。\n2.成果指标要求\n1）仿真推演功能模块指标要求：\n①模块功能完整性：所有功能模块需按需求开发完成，确保场景编辑、仿真运行、可视化展示等功能正常使用；\n②模块运行效率：仿真支持至少10倍的快进播放；\n③模块可扩展性：模块设计需支持后续功能扩展，确保系统具有良好的可扩展性；\n④模块用户体验：模块设计需注重用户体验，确保操作直观、界面友好，降低用户使用门槛。\n2）研究报告：包含仿真需求分析、空间博弈推演规则研究、仿真推演验证、数据分析与评价等章节，报告字数不少于2万字。报告内容覆盖规则研究、场景相关飞行器概述、两种场景的推演过程与结果（推演过程描述、关键节点分析和结果解读）、数据分析等内容。\n3）推演数据集：不同场景的数据集2套，包括两种场景的空间博弈仿真推演，包括一对一在轨追逃博弈与多对多集群博弈，其中多对多集群博弈仿真涉及双方飞行器总数不少于9个，单个数据集覆盖完整任务周期，推演数据可进行可视化展示。数据集内容包括但不限于：收集场景相关飞行器数据，推演过程中产生的轨道数据、载荷性能、能源消耗等；场景要素齐全，方案合理，二维、三维视景展示可支撑演示片内容制作。\n3.质量标准\n1）结果符合合同约定的质量标准；\n2）在进行运行及验收过程中发生的故障和发现的隐患已被排除并得到甲方的认可；\n3）所有合同中规定的数据集、资料、技术文件已提交并得到甲方认可；\n4）成果在交由甲方使用之前需通过专家评审；\n成果形式：\n1.面向GEO的空间博弈推演研究报告\n2.面向GEO的空间博弈仿真推演数据集\n3.仿真推演功能模块\n4.仿真功能模块设计方案、使用说明等相关技术文档。\n征集方式：单人悬赏\n经费：39万元\n知识产权归属：以合同约定为准\n成果评审方式：专家线下评审\n联系人及电话：平台流程问题请联系孙老师/15522100850；技术指标问题请联系李老师/13811063368', '人工智能', '[]', '技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求技能要求', 'intermediate', 2, 4, 120, 'fixed', 5000.00, 'CNY', NULL, '2026-03-06 00:00:00', '2026-03-06', '2026-07-04', 'published', NULL, 0, '2018608172406804480', NULL, 0, 16, 0, '2026-03-05 17:36:02', '2026-03-09 15:26:29', 0);
INSERT INTO `project` VALUES (18, '2032029364287311872', 'PRJ20260312823886', '项目审核测试1', '需要开发一个现代化的电商平台前端系统，包含商品展示、购物车、订单管理等功能。要求使用Vue 3框架，具备良好的用户体验和响应式设计。', 'WEB_DEVELOPMENT', '[\"Vue.js\", \"Element Plus\", \"TypeScript\", \"Vite\", \"Pinia\"]', '1. 熟练掌握Vue 3组合式API\n2. 具备TypeScript开发经验\n3. 了解前端工程化', 'intermediate', 2, 4, 60, 'fixed', 50000.00, 'CNY', NULL, '2025-11-30 23:59:59', '2025-12-01', '2026-01-30', 'published', NULL, 0, '2018608172406804480', NULL, 0, 0, 0, '2026-03-12 17:42:08', '2026-03-12 17:59:36', 0);

-- ----------------------------
-- Table structure for project_category
-- ----------------------------
DROP TABLE IF EXISTS `project_category`;
CREATE TABLE `project_category`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增物理主键ID（内部使用）',
  `project_category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类唯一编码（业务逻辑主键）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '父分类逻辑ID（NULL表示自身为一级分类）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序权重',
  `is_visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否在前端可见',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_category_code`(`project_category_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_category_natural`(`parent_id` ASC, `name` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_category_order_visible`(`sort_order` ASC, `is_visible` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_category
-- ----------------------------
INSERT INTO `project_category` VALUES (1, '1', '人工智能', 'CAT_1980117086965469184', '123456', NULL, 0, 1, 1, '2026-02-06 12:18:14', '2026-02-06 12:19:54');

-- ----------------------------
-- Table structure for project_milestone
-- ----------------------------
DROP TABLE IF EXISTS `project_milestone`;
CREATE TABLE `project_milestone`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `milestone_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑全局唯一标识',
  `project_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联项目ID',
  `milestone_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑编码（自定义，用于标识里程碑类型，如：proposal_submission、mid_term_defense等）',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '里程碑名称（可自定义展示文案）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '里程碑描述',
  `sequence_order` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '阶段顺序，从1开始',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'planned' COMMENT '阶段状态：planned, in_progress, delivered, under_review, completed, rejected, expired, skipped',
  `progress_percent` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '阶段进度0-100',
  `deliverable_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '成果附件/链接',
  `approval_rule` json NULL COMMENT '审批规则配置（固定里程碑的角色/节点要求）',
  `approval_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'pending' COMMENT '审批状态：pending, partial, approved, rejected',
  `current_owner_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '当前负责人（如企业或获奖团队用户ID）',
  `planned_start_time` datetime NULL DEFAULT NULL COMMENT '计划开始时间',
  `planned_end_time` datetime NULL DEFAULT NULL COMMENT '计划结束时间',
  `actual_start_time` datetime NULL DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL DEFAULT NULL COMMENT '实际完成时间',
  `auto_extended_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '自动延期次数',
  `remind_before_hours` int NULL DEFAULT NULL COMMENT '提醒提前小时数（实例化时可Override）',
  `is_skipped` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否跳过该阶段',
  `skip_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跳过原因说明',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '阶段备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_milestone_id`(`milestone_id` ASC) USING BTREE,
  INDEX `idx_project_milestone_code`(`project_id` ASC, `milestone_code` ASC) USING BTREE,
  INDEX `idx_milestone_project`(`project_id` ASC, `sequence_order` ASC) USING BTREE,
  INDEX `idx_milestone_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_milestone_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目里程碑实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_milestone
-- ----------------------------
INSERT INTO `project_milestone` VALUES (1, '2021450294658994176', '2021450294445084672', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2026-03-20 00:00:00', '2026-03-26 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:04:42', '2026-03-04 23:36:14', 0);
INSERT INTO `project_milestone` VALUES (2, '2021450294789017600', '2021450294445084672', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2026-03-20 00:00:00', '2026-03-25 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:04:42', '2026-03-04 23:36:14', 0);
INSERT INTO `project_milestone` VALUES (3, '2021450294902263808', '2021450294445084672', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2026-03-26 00:00:00', '2026-03-28 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:04:42', '2026-03-04 23:36:14', 0);
INSERT INTO `project_milestone` VALUES (4, '2021451590757650432', '2021451590602461184', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2026-03-13 00:00:00', '2026-03-14 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:09:51', '2026-03-05 14:02:46', 0);
INSERT INTO `project_milestone` VALUES (5, '2021451590854119424', '2021451590602461184', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2026-03-16 00:00:00', '2026-03-17 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:09:51', '2026-03-05 14:02:46', 0);
INSERT INTO `project_milestone` VALUES (6, '2021451590950588416', '2021451590602461184', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2026-03-18 00:00:00', '2026-03-20 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:09:51', '2026-03-05 14:02:46', 0);
INSERT INTO `project_milestone` VALUES (7, '2021453479675039744', '2021453479545016320', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-11 13:17:21', '2026-02-11 13:17:21', 0);
INSERT INTO `project_milestone` VALUES (8, '2021453479784091648', '2021453479545016320', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-11 13:17:21', '2026-02-11 13:17:21', 0);
INSERT INTO `project_milestone` VALUES (9, '2021453479880560640', '2021453479545016320', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-11 13:17:21', '2026-02-11 13:17:21', 0);
INSERT INTO `project_milestone` VALUES (10, '2021793725210038272', '2021793723754614784', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-12 11:49:22', '2026-02-12 11:49:22', 0);
INSERT INTO `project_milestone` VALUES (11, '2021793726250225664', '2021793723754614784', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-12 11:49:22', '2026-02-12 11:49:22', 0);
INSERT INTO `project_milestone` VALUES (12, '2021793727282024448', '2021793723754614784', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-12 11:49:22', '2026-02-12 11:49:22', 0);
INSERT INTO `project_milestone` VALUES (13, '2021800218680496128', '2021800217111826432', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-12 12:15:10', '2026-02-12 12:15:10', 0);
INSERT INTO `project_milestone` VALUES (14, '2021800219821346816', '2021800217111826432', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-12 12:15:10', '2026-02-12 12:15:10', 0);
INSERT INTO `project_milestone` VALUES (15, '2021800220928643072', '2021800217111826432', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-12 12:15:11', '2026-02-12 12:15:11', 0);
INSERT INTO `project_milestone` VALUES (16, '2021845091026604032', '2021845090804305920', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-12 15:13:28', '2026-02-12 15:13:28', 0);
INSERT INTO `project_milestone` VALUES (17, '2021845091144044544', '2021845090804305920', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-12 15:13:28', '2026-02-12 15:13:28', 0);
INSERT INTO `project_milestone` VALUES (18, '2021845091248902144', '2021845090804305920', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-12 15:13:28', '2026-02-12 15:13:28', 0);
INSERT INTO `project_milestone` VALUES (19, '2022959380462440448', '2022959380340805632', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-15 17:01:16', '2026-02-15 17:01:16', 0);
INSERT INTO `project_milestone` VALUES (20, '2022959380558909440', '2022959380340805632', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-15 17:01:16', '2026-02-15 17:01:16', 0);
INSERT INTO `project_milestone` VALUES (21, '2022959380646989824', '2022959380340805632', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-15 17:01:16', '2026-02-15 17:01:16', 0);
INSERT INTO `project_milestone` VALUES (22, '2022960804403482624', '2022960804281847808', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-02-15 17:06:55', '2026-02-15 17:06:55', 0);
INSERT INTO `project_milestone` VALUES (23, '2022960804499951616', '2022960804281847808', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-15 17:06:55', '2026-02-15 17:06:55', 0);
INSERT INTO `project_milestone` VALUES (24, '2022960804621586432', '2022960804281847808', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-15 17:06:55', '2026-02-15 17:06:55', 0);
INSERT INTO `project_milestone` VALUES (25, '2023249119220469760', '2023249119094640640', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'delivered', 30, '成果文件URL2', '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, '2026-03-03 22:14:58', NULL, 0, 72, 0, NULL, NULL, '2026-02-16 12:12:35', '2026-03-03 22:14:58', 0);
INSERT INTO `project_milestone` VALUES (26, '2023249119316938752', '2023249119094640640', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-02-16 12:12:35', '2026-02-16 12:12:35', 0);
INSERT INTO `project_milestone` VALUES (27, '2023249119409213440', '2023249119094640640', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-02-16 12:12:35', '2026-02-16 12:12:35', 0);
INSERT INTO `project_milestone` VALUES (28, '2029219188316442624', '2029219188194807808', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-04 23:35:30', '2026-03-05 13:53:01', 0);
INSERT INTO `project_milestone` VALUES (29, '2029219188400328704', '2029219188194807808', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-04 23:35:30', '2026-03-05 13:53:08', 0);
INSERT INTO `project_milestone` VALUES (30, '2029219188484214784', '2029219188194807808', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-04 23:35:30', '2026-03-04 23:45:47', 0);
INSERT INTO `project_milestone` VALUES (31, '2029437312567480320', '2029437312420679680', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-03-05 14:02:15', '2026-03-05 14:02:15', 0);
INSERT INTO `project_milestone` VALUES (32, '2029437312672337920', '2029437312420679680', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-03-05 14:02:15', '2026-03-05 14:02:15', 0);
INSERT INTO `project_milestone` VALUES (33, '2029437312773001216', '2029437312420679680', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 14:02:15', '2026-03-05 14:02:15', 0);
INSERT INTO `project_milestone` VALUES (34, '2029439365771563008', '2029439365637345280', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2026-03-06 00:00:00', '2026-03-10 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 14:10:25', '2026-03-05 14:10:47', 0);
INSERT INTO `project_milestone` VALUES (35, '2029439365855449088', '2029439365637345280', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2026-03-12 00:00:00', '2026-03-14 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 14:10:25', '2026-03-05 14:10:47', 0);
INSERT INTO `project_milestone` VALUES (36, '2029439365935140864', '2029439365637345280', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2026-03-12 00:00:00', '2026-03-14 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 14:10:25', '2026-03-05 14:10:47', 0);
INSERT INTO `project_milestone` VALUES (37, '2029476588550230016', '2029476588432789504', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 72, 0, NULL, NULL, '2026-03-05 16:38:19', '2026-03-05 16:38:19', 0);
INSERT INTO `project_milestone` VALUES (38, '2029476588634116096', '2029476588432789504', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 48, 0, NULL, NULL, '2026-03-05 16:38:19', '2026-03-05 16:38:19', 0);
INSERT INTO `project_milestone` VALUES (39, '2029476588713807872', '2029476588432789504', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, NULL, NULL, NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 16:38:19', '2026-03-05 16:38:19', 0);
INSERT INTO `project_milestone` VALUES (40, '2029491113622114304', '2029491113483702272', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2026-03-06 00:00:00', '2026-03-09 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 17:36:02', '2026-03-05 17:40:48', 0);
INSERT INTO `project_milestone` VALUES (41, '2029491113722777600', '2029491113483702272', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 4, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2026-03-13 00:00:00', '2026-03-14 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 17:36:02', '2026-03-05 17:40:48', 0);
INSERT INTO `project_milestone` VALUES (42, '2029491113806663680', '2029491113483702272', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 6, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2026-03-18 00:00:00', '2026-03-19 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 17:36:02', '2026-03-05 17:40:48', 0);
INSERT INTO `project_milestone` VALUES (43, '2029492309405601792', '2029491113483702272', 'mid_term_defense', '中期答辩', '安排中期答辩、准备材料并确认实际时间', 2, 'planned', 0, NULL, '{\"reviewTy pe\": \"defense\", \"approverRoles\": [\"academic\"]}', 'pending', NULL, '2026-03-11 00:00:00', '2026-03-12 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-05 17:40:47', '2026-03-05 17:40:48', 0);
INSERT INTO `project_milestone` VALUES (44, '2032029366124417024', '2032029364287311872', 'proposal_submission', '方案提交', '企业/团队提交完整方案、附录与预计交付时间', 1, 'planned', 0, NULL, '{\"autoApprove\": true, \"approverRoles\": [\"enterprise\"]}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-12 17:42:09', '2026-03-12 17:45:21', 0);
INSERT INTO `project_milestone` VALUES (45, '2032029367382708224', '2032029364287311872', 'deliverable_submission', '成果提交', '上传交付成果包并同步进度与实际时间', 3, 'planned', 0, NULL, '{\"autoApprove\": false, \"approverRoles\": [\"enterprise\", \"team\"]}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-12 17:42:09', '2026-03-12 17:45:41', 0);
INSERT INTO `project_milestone` VALUES (46, '2032029368817160192', '2032029364287311872', 'project_settlement', '项目结算', '产出结算凭证并推进财务付款流程', 5, 'planned', 0, NULL, '{\"approverRoles\": [\"finance\"], \"requiresReceipt\": true}', 'pending', NULL, '2025-04-01 00:00:00', '2025-04-15 00:00:00', NULL, NULL, 0, 24, 0, NULL, NULL, '2026-03-12 17:42:10', '2026-03-12 17:45:47', 0);

-- ----------------------------
-- Table structure for student_profile
-- ----------------------------
DROP TABLE IF EXISTS `student_profile`;
CREATE TABLE `student_profile`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联user表的user_id',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年级',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `tech_tags` json NULL COMMENT '技术标签',
  `tech_intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '个人技术介绍',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_student_id`(`student_id` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生资料扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_profile
-- ----------------------------

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键，内部使用',
  `team_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '团队全局唯一标识符（对外暴露）',
  `team_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '团队名称',
  `leader_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '队长用户ID（关联user表）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '团队描述',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '团队头像URL',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active' COMMENT '团队状态：active（活跃）, inactive（不活跃）, disbanded（已解散）',
  `member_count` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '当前成员数量（冗余字段，便于查询统计）',
  `max_member_count` int UNSIGNED NOT NULL DEFAULT 10 COMMENT '最大成员数量限制',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_team_id`(`team_id` ASC) USING BTREE,
  INDEX `idx_team_leader_id`(`leader_id` ASC) USING BTREE,
  INDEX `idx_team_status`(`status` ASC) USING BTREE,
  INDEX `idx_team_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_team_leader` FOREIGN KEY (`leader_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '团队表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES (1, '2020476698205753344', '蛋仔派对', '2018608120762339328', '111', '123456', 'active', 1, 5, '2026-02-08 20:35:58', '2026-02-08 20:35:58');

-- ----------------------------
-- Table structure for team_member
-- ----------------------------
DROP TABLE IF EXISTS `team_member`;
CREATE TABLE `team_member`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `team_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '团队ID',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '成员用户ID',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'member' COMMENT '角色：leader / member',
  `join_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_team_user`(`team_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '团队成员关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of team_member
-- ----------------------------
INSERT INTO `team_member` VALUES (1, '2020476698205753344', '2018608120762339328', 'leader', '2026-03-09 23:04:01');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键，内部使用',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '全局唯一标识符，对外暴露，不可预测',
  `type` tinyint(1) NOT NULL COMMENT '用户类型',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL地址',
  `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个人简介',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `region_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行政区划代码',
  `industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所在行业',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头衔/职称',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '账号是否激活',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_uuid`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_created_at`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_updated_at`(`update_time` ASC) USING BTREE,
  INDEX `idx_user_nickname`(`nickname` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户核心信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2031391299944452096', 3, '超级管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-03-10 23:26:42', '2026-03-10 23:29:18');
INSERT INTO `user` VALUES (2, '2018604030183477248', 2, 'Job', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-02-03 16:34:39', '2026-02-03 16:34:39');
INSERT INTO `user` VALUES (3, '2018606776374661120', 1, 'kun', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-02-03 16:45:34', '2026-02-03 16:45:34');
INSERT INTO `user` VALUES (4, '2018608120762339328', 1, '测试学生', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-03-09 18:28:16', '2026-02-03 16:50:55', '2026-03-09 18:28:12');
INSERT INTO `user` VALUES (5, '2018608172406804480', 2, '测试企业', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-02-04 23:36:00', '2026-02-03 16:51:07', '2026-02-04 23:36:00');
INSERT INTO `user` VALUES (6, '2018608565266288640', 1, 'testuser99', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-02-03 16:52:41', '2026-02-03 16:52:41');
INSERT INTO `user` VALUES (7, '2018608783495925760', 1, 'testuser98', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-02-03 16:53:33', '2026-02-03 16:53:33');
INSERT INTO `user` VALUES (11, '2028847268169388032', 1, '测试学生2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-03-09 18:27:36', '2026-03-03 22:57:38', '2026-03-09 18:27:32');
INSERT INTO `user` VALUES (12, '2028847298607452160', 1, '测试学生3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-03-03 22:57:45', '2026-03-03 22:57:45');
INSERT INTO `user` VALUES (14, '2031571468617388032', 3, '普通管理员01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-03-11 11:22:38', '2026-03-11 11:22:38');
INSERT INTO `user` VALUES (15, '2032037851113328640', 1, '测试企业211111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-03-12 18:15:52', '2026-03-12 18:15:52');
INSERT INTO `user` VALUES (16, '2032039165281046528', 1, '测试企业111111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2026-03-12 18:21:05', '2026-03-12 18:21:05');

-- ----------------------------
-- Table structure for user_auth_credential
-- ----------------------------
DROP TABLE IF EXISTS `user_auth_credential`;
CREATE TABLE `user_auth_credential`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_auth_credential_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '认证记录全局唯一标识符',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联用户的全局唯一标识符',
  `identity_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '认证类型：phone, email, wechat, github, username等',
  `identifier` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '唯一标识符：手机号、邮箱、用户名、第三方开放ID等',
  `credential` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '凭证：密码哈希、Token等（可为NULL，如第三方登录）',
  `is_verified` tinyint(1) NOT NULL DEFAULT 0 COMMENT '该凭证是否已验证：0-未验证，1-已验证',
  `verify_time` datetime NULL DEFAULT NULL COMMENT '验证通过时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_auth_identity_identifier`(`identity_type` ASC, `identifier` ASC) USING BTREE,
  UNIQUE INDEX `ukuser_auth_credential_id`(`user_auth_credential_id` ASC) USING BTREE,
  INDEX `idx_auth_credential_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_auth_credential_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '统一认证凭证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_auth_credential
-- ----------------------------
INSERT INTO `user_auth_credential` VALUES (2, '2018604030183477249', '2018604030183477248', 'email', '17878660696', '$2a$10$hRtW6OVdQwXMNITkM/kvuuUwAyh4oG9pyaBvD6/qlKwAak0mzxAou', 1, NULL, '2026-02-03 16:34:39', '2026-02-03 16:34:39');
INSERT INTO `user_auth_credential` VALUES (3, '2018606776374661121', '2018606776374661120', 'email', '17878660697', '$2a$10$9zjRDvWPAOCOnSPSzTuFQeeEOPzJ48T4ufHWmqvUEaISl/liPX0Xm', 1, NULL, '2026-02-03 16:45:34', '2026-02-03 16:45:34');
INSERT INTO `user_auth_credential` VALUES (4, '2018608120762339329', '2018608120762339328', 'username', 'test_student', '$2a$10$T/G.I7YcOgXhMidJbzJF3eAQHHzlfcq25mmeseVGJnNKusyWt8NSS', 1, NULL, '2026-02-03 16:50:55', '2026-02-03 16:50:55');
INSERT INTO `user_auth_credential` VALUES (5, '2018608172410998784', '2018608172406804480', 'username', 'test_enterprise', '$2a$10$4QlD8af..5J.P0VAGd.qGeqvb62qdRscFkMkmeE3zAACjSfAUszji', 1, NULL, '2026-02-03 16:51:07', '2026-02-03 16:51:07');
INSERT INTO `user_auth_credential` VALUES (6, '2018608565266288641', '2018608565266288640', 'username', 'testuser99', '$2a$10$f7Zmgl271ZtpdLfy4RWLAOQ/7hiTGYD3U34pI8o6Zsbi7SwSQZt1y', 1, NULL, '2026-02-03 16:52:41', '2026-02-03 16:52:41');
INSERT INTO `user_auth_credential` VALUES (7, '2018608783495925761', '2018608783495925760', 'username', 'testuser98', '$2a$10$Zx74PAMJ4gogHnV4cMDzruTBHsOkWs69WP8iCy0WYXG5KXE/ZoaKW', 1, NULL, '2026-02-03 16:53:33', '2026-02-03 16:53:33');
INSERT INTO `user_auth_credential` VALUES (11, '2028847268169388033', '2028847268169388032', 'username', 'test_student2', '$2a$10$7Mz.VxRwB10GnfCrdlPMyuDbuILFQZ.W0VU0k0C2aFYd8L5VMBkwS', 1, NULL, '2026-03-03 22:57:38', '2026-03-03 22:57:38');
INSERT INTO `user_auth_credential` VALUES (12, '2028847298607452161', '2028847298607452160', 'username', 'test_student3', '$2a$10$ABo3A1D8RcnaObOfdnGqEeHlz/.FCIvEPNE4PqaVtUPZKvNHFMKnu', 1, NULL, '2026-03-03 22:57:45', '2026-03-03 22:57:45');
INSERT INTO `user_auth_credential` VALUES (13, '2031391299944452097', '2031391299944452096', 'username', 'admin', '$2a$10$WOBCJxe0WKuiXt4lzYp/i.nAbHyZVZhKUcDkeDKllyW72FkKOJu1q', 1, NULL, '2026-03-10 23:26:42', '2026-03-10 23:26:42');
INSERT INTO `user_auth_credential` VALUES (14, '2031571468617388033', '2031571468617388032', 'username', 'admin01', '$2a$10$VB01cgZtFWxjBfGjkZbrfeWFM88ubx.DiJ8d0iAuCAKocfkYWbyrO', 1, NULL, '2026-03-11 11:22:38', '2026-03-11 11:22:38');
INSERT INTO `user_auth_credential` VALUES (15, '2032037851113328641', '2032037851113328640', 'username', '测试企业211111', '$2a$10$xKO5g8jvKwtw92lE986poew3m1mFQEc5Hq0WVSWQLpL2vvFNDx8mu', 1, NULL, '2026-03-12 18:15:52', '2026-03-12 18:15:52');
INSERT INTO `user_auth_credential` VALUES (16, '2032039165281046529', '2032039165281046528', 'username', '测试企业111111', '$2a$10$wwtsSxpL6suGDl1AIhglCOUuRQuuAV6drzDrXbxrjcrWZciIUXODS', 1, NULL, '2026-03-12 18:21:05', '2026-03-12 18:21:05');

SET FOREIGN_KEY_CHECKS = 1;
