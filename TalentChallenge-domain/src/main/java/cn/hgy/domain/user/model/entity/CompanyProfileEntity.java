package cn.hgy.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2026-03-09
 * @Description: 企业资料实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 企业介绍
     */
    private String companyIntro;

    /**
     * 企业标签
     */
    private List<String> companyTags;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 行业领域
     */
    private String companyIndustry;

    /**
     * 企业规模
     */
    private String companyScale;

    /**
     * 所在地区
     */
    private String companyRegion;

    /**
     * 官方网址
     */
    private String companyWebsite;

    /**
     * 主要联系人
     */
    private String contactPerson;

    /**
     * 联系人职务
     */
    private String contactPosition;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 资质文件URL数组
     */
    private List<String> qualificationFiles;
}
