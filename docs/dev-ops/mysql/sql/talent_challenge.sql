/*
 Navicat Premium Data Transfer

 Source Server         : Docker Desktop
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:13306
 Source Schema         : talent_challenge

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 27/09/2025 13:50:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project
-- ----------------------------
CREATE TABLE IF NOT EXISTS `project`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `project_category`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bid
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bid`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键，内部使用',
  `bid_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '竞榜记录全局唯一标识',
  `project_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联项目的全局唯一标识',
  `student_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请人（学生）全局唯一标识',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'applied' COMMENT '竞榜状态：applied, shortlisted, confirmed, rejected, cancelled, expired',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '申请说明/自荐内容',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件（作品集、简历）链接',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '企业审核备注或拒绝理由',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bid_uuid`(`bid_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_bid_project_student`(`project_id` ASC, `student_id` ASC) USING BTREE,
  INDEX `idx_bid_project_status`(`project_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_bid_student_status`(`student_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目竞榜申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户核心信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_auth_credential
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_auth_credential`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '统一认证凭证表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
