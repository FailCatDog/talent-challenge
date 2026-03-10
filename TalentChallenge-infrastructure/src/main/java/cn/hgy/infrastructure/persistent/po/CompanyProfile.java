package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业资料PO对象
 *
 * @author hgy
 */
@Data
public class CompanyProfile {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 企业介绍
     */
    private String companyIntro;

    /**
     * 企业标签（JSON字符串）
     */
    private String companyTags;

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
     * 资质文件URL数组（JSON字符串）
     */
    private String qualificationFiles;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updateTime;
}
