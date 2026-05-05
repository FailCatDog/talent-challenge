package cn.hgy.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2026-03-09
 * @Description: 学生资料实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 技术标签
     */
    private List<String> techTags;

    /**
     * 个人技术介绍
     */
    private String techIntro;
}
