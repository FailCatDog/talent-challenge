-- 里程碑交付物多文件上传功能 - 数据库迁移脚本
-- 执行时间：2026-03-13
-- 说明：为 project_milestone 表添加 deliverable_files 字段，支持学生上传多个交付物文件

USE talent_challenge;

-- 添加 deliverable_files 字段
ALTER TABLE `project_milestone`
ADD COLUMN `deliverable_files` json NULL COMMENT '学生提交的交付物文件列表，JSON数组格式' AFTER `deliverable_url`;

-- 验证字段是否添加成功
DESC project_milestone;

-- 查看表结构
SHOW CREATE TABLE project_milestone;
