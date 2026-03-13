package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业资质认证详情DTO（管理员查看）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyQualificationDetailDTO {

    /** 用户ID */
    private String userId;

    /** 企业名称（user表nickname） */
    private String companyName;

    /** 企业介绍 */
    private String companyIntro;

    /** 企业标签 */
    private List<String> companyTags;

    /** 统一社会信用代码 */
    private String creditCode;

    /** 行业领域 */
    private String companyIndustry;

    /** 企业规模 */
    private String companyScale;

    /** 所在地区 */
    private String companyRegion;

    /** 官方网址 */
    private String companyWebsite;

    /** 主要联系人 */
    private String contactPerson;

    /** 联系人职务 */
    private String contactPosition;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 资质文件URL列表（JSON格式） */
    private String qualificationFiles;

    /** 认证状态 */
    private String certificationStatus;

    /** 拒绝原因 */
    private String rejectReason;

    /** 认证通过时间 */
    private LocalDateTime certifiedAt;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
