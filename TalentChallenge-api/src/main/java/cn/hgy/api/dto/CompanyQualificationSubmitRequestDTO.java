package cn.hgy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 企业提交资质审核请求DTO
 * 包含认证核心字段（审核通过后锁定）和资质文件URL列表
 */
@Data
public class CompanyQualificationSubmitRequestDTO {

    // 认证核心字段（审核通过后不可再修改）
    /** 统一社会信用代码 */
    private String creditCode;

    /** 企业行业 */
    private String companyIndustry;

    /** 企业规模 */
    private String companyScale;

    /** 联系人姓名 */
    private String contactPerson;

    /** 联系人职位 */
    private String contactPosition;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 资质文件URL列表 */
    private List<String> fileUrls;
}
